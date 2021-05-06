package org.nautilus.core.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.nautilus.core.listener.AlgorithmListener;
import org.nautilus.core.listener.OnProgressListener;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import Jama.Matrix;

@SuppressWarnings({"unchecked", "rawtypes", "unlikely-arg-type"})
public class NSGAIII<S extends Solution<?>> implements Algorithm<List<S>>, AlgorithmListener{

    private static final long serialVersionUID = -8000998001765174131L;

    private OnProgressListener onProgressListener;
    
	protected int populationSize_;

    protected List<S> population_;
    
    List<S> offspringPopulation_;
    List<S> union_;

    int evaluations;

    CrossoverOperator<S> crossover_;
    MutationOperator<S> mutation_;
    SelectionOperator<List<S>, S> selection_;

    double[][] lambda_; // reference points

    boolean normalize_; // do normalization or not

    protected final Problem<S> problem_;

    protected int maxEvaluations;

    private List<S> initialPopulation;
    
    public NSGAIII(Builder builder) {

        problem_ = builder.getProblem();

        maxEvaluations = builder.getMaxEvaluations();

        normalize_ = false;

        populationSize_ = builder.getPopulationSize();

        mutation_ = builder.getMutation();

        crossover_ = builder.getCrossover();

        selection_ = builder.getSelection();
        
        this.initialPopulation = (List<S>) builder.getInitialPopulation();

    } // NSGAIII
    
    protected void updateProgress() {
		
		double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(progress);
		}
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

    protected void initializeUniformWeight() {

    	String dataFileName = "W" + problem_.getNumberOfObjectives() + "D_" + populationSize_ + ".dat";

    	String path = "files"+File.separator+"weight-vectors"+File.separator + dataFileName;
    	
        lambda_ = new double[populationSize_][problem_.getNumberOfObjectives()];

        try {
        	FileReader fr = new FileReader(path);
            
            try (BufferedReader br = new BufferedReader(fr)) {
                int i = 0;
                int j;
                String aux = br.readLine();
                while (aux != null) {
                    StringTokenizer st = new StringTokenizer(aux);
                    j = 0;
                    while (st.hasMoreTokens()) {
                        double value = new Double(st.nextToken());
                        lambda_[i][j] = value;
                        j++;
                    }
                    aux = br.readLine();
                    i++;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new JMetalException("initializeUniformWeight: failed when reading for file: /WeightVectors/" + dataFileName, e);
        }
    }

    @Override
    public void run() {

        evaluations = 0;

        initializeUniformWeight();

        if (populationSize_ % 2 != 0) {
            populationSize_ += 1;
        }

        initPopulation();
        evaluations += populationSize_;

        while (!isStoppingConditionReached()) {
            offspringPopulation_ = new ArrayList<>(populationSize_);
            for (int i = 0; i < (populationSize_ / 2); i++) {
                if (evaluations < maxEvaluations) {
                    // obtain parents

                    List<S> parents = new ArrayList<>();
                    parents.add(selection_.execute(population_));
                    parents.add(selection_.execute(population_));

                    List<S> offSpring = crossover_.execute(parents);

                    mutation_.execute(offSpring.get(0));
                    mutation_.execute(offSpring.get(1));

                    problem_.evaluate(offSpring.get(0));
                    problem_.evaluate(offSpring.get(1));
                    evaluations += 2;

                    offspringPopulation_.add(offSpring.get(0));
                    offspringPopulation_.add(offSpring.get(1));

                } // if
            } // for

            union_ = new ArrayList<>();
            union_.addAll(population_);
            union_.addAll(offspringPopulation_);

            // Ranking the union
            Ranking ranking = (new DominanceRanking()).computeRanking(union_);

            int remain = populationSize_;
            int index = 0;
            List<S> front;
            population_.clear();

            // Obtain the next front
            front = ranking.getSubfront(index);

            while ((remain > 0) && (remain >= front.size())) {

                for (int k = 0; k < front.size(); k++) {
                    population_.add(front.get(k));
                } // for

                // Decrement remain
                remain = remain - front.size();

                // Obtain the next front
                index++;
                if (remain > 0) {
                    front = ranking.getSubfront(index);
                } // if
            }

            if (remain > 0) { // front contains individuals to insert
                new Niching(population_, front, lambda_, remain, normalize_)
                        .execute();
            }

            updateProgress();
        }

    }
    
    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    protected void initPopulation() {

		if (this.initialPopulation == null) {

			population_ = new ArrayList<>(populationSize_);

			for (int i = 0; i < populationSize_; i++) {
				S newSolution = problem_.createSolution();
				problem_.evaluate(newSolution);
				population_.add(newSolution);
			}
		} else {

			while (initialPopulation.size() != populationSize_) {
				((List<S>) this.initialPopulation).add(problem_.createSolution());
			}

			population_ = initialPopulation;
		}
	}

    @Override
    public List<S> getResult() {
        return SolutionListUtils.getNondominatedSolutions(population_);
    }

    @Override
    public String getName() {
        return "NSGAIII";
    }

    @Override
    public String getDescription() {
        return "\"unofficial\" implementation of NSGA-III";
    }

    public static class Niching<S extends Solution<?>> {

        List<S> population;
        List<S> lastFront;
        List<S> mgPopulation;

        List<S> union;

        int obj;
        int remain;

        boolean normalization;

        double[][] lambda;

        double[] zideal;

        double[] zmax;

        double[][] extremePoints;

        double[] intercepts;

        public Niching(List<S> population, List<S> lastFront,
                double[][] lambda, int remain, boolean normalization) {

            this.population = population;
            this.lastFront = lastFront;

            this.remain = remain;
            this.lambda = lambda;

            this.normalization = normalization;

            this.mgPopulation = new ArrayList<>();
            this.mgPopulation.addAll(population);
            this.mgPopulation.addAll(lastFront);

            if (population.size() > 0) {
                this.obj = population.get(0).getNumberOfObjectives();
            } else {
                this.obj = lastFront.get(0).getNumberOfObjectives();
            }
        }

        public void execute() {
            computeIdealPoint();

            if (normalization) {
                computeMaxPoint();
                computeExtremePoints();
                computeIntercepts();
                normalizePopulation();
            }

            associate();
            assignment();
        }

        void computeIdealPoint() {
            zideal = new double[obj];

            for (int j = 0; j < obj; j++) {
                zideal[j] = Double.MAX_VALUE;

                for (int i = 0; i < mgPopulation.size(); i++) {
                    if (mgPopulation.get(i).getObjective(j) < zideal[j]) {
                        zideal[j] = mgPopulation.get(i).getObjective(j);
                    }
                }
            }

        }

        void computeMaxPoint() {
            zmax = new double[obj];

            for (int j = 0; j < obj; j++) {
                zmax[j] = Double.MIN_VALUE;

                for (int i = 0; i < mgPopulation.size(); i++) {
                    if (mgPopulation.get(i).getObjective(j) > zmax[j]) {
                        zmax[j] = mgPopulation.get(i).getObjective(j);
                    }
                }
            }
        }

        void computeExtremePoints() {
            extremePoints = new double[obj][obj];

            for (int j = 0; j < obj; j++) {
                int index = -1;
                double min = Double.MAX_VALUE;

                for (int i = 0; i < mgPopulation.size(); i++) {
                    double asfValue = asfFunction(mgPopulation.get(i), j);
                    if (asfValue < min) {
                        min = asfValue;
                        index = i;
                    }
                }

                for (int k = 0; k < obj; k++) {
                    extremePoints[j][k] = mgPopulation.get(index).getObjective(k);
                }
            }
        }

        void computeIntercepts() {

            intercepts = new double[obj];

            double[][] temp = new double[obj][obj];

            for (int i = 0; i < obj; i++) {
                for (int j = 0; j < obj; j++) {
                    double val = extremePoints[i][j] - zideal[j];
                    temp[i][j] = val;
                }
            }

            Matrix EX = new Matrix(temp);

            if (EX.rank() == EX.getRowDimension()) {
                double[] u = new double[obj];
                for (int j = 0; j < obj; j++) {
                    u[j] = 1;
                }

                Matrix UM = new Matrix(u, obj);

                Matrix AL = EX.inverse().times(UM);

                int j;
                for (j = 0; j < obj; j++) {

                    double aj = 1.0 / AL.get(j, 0) + zideal[j];

                    if ((aj > zideal[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj))) {
                        intercepts[j] = aj;
                    } else {
                        break;
                    }
                }
                if (j != obj) {
                    System.arraycopy(zmax, 0, intercepts, 0, obj);
                }

            } else {
                System.arraycopy(zmax, 0, intercepts, 0, obj);
            }

        }

        void normalizePopulation() {
            for (int i = 0; i < mgPopulation.size(); i++) {
                Solution sol = mgPopulation.get(i);

                double[] normalized_ = new double[sol.getNumberOfObjectives()];
                for (int j = 0; j < obj; j++) {

                    double val = (sol.getObjective(j) - zideal[j])
                            / (intercepts[j] - zideal[j]);
                    normalized_[j] = val;
                }
                sol.setAttribute("NormalizedObjective", normalized_);
            }
        }

        public void associate() {

            for (int k = 0; k < mgPopulation.size(); k++) {

                Solution sol = mgPopulation.get(k);

                double min = calVDistance(sol, lambda[0]);
                int index = 0;

                for (int j = 1; j < lambda.length; j++) {
                    double dist = calVDistance(sol, lambda[j]);
                    if (dist < min) {
                        min = dist;
                        index = j;
                    }
                }
                sol.setAttribute("ClusterID", index);
                sol.setAttribute("VDistance", min);
            }

        }

        public void assignment() {
            
            JMetalRandom rand = JMetalRandom.getInstance();
            
            int[] ro = new int[lambda.length];
            boolean[] flag = new boolean[lambda.length];

            for (int k = 0; k < population.size(); k++) {
                ro[(int) population.get(k).getAttribute("ClusterID")]++;
            }

            int num = 0;

            while (num < remain) {
                int[] perm = new Permutation().intPermutation(ro.length);

                int min = Integer.MAX_VALUE;
                int id = -1;

                for (int i = 0; i < perm.length; i++) {
                    if ((!flag[perm[i]]) && (ro[perm[i]] < min)) {
                        min = ro[perm[i]];
                        id = perm[i];
                    }
                }

                List<Integer> list = new ArrayList<>();

                for (int k = 0; k < lastFront.size(); k++) {
                    if ((int) lastFront.get(k).getAttribute("ClusterID") == id) {
                        list.add(k);
                    }
                }

                if (!list.isEmpty()) {
                    int index = 0;
                    if (ro[id] == 0) {
                        double minDist = Double.MAX_VALUE;

                        for (int j = 0; j < list.size(); j++) {
                            if ((double) lastFront.get(list.get(j)).getAttribute("VDistance") < minDist) {
                                minDist = (double) lastFront.get(list.get(j)).getAttribute("VDistance");
                                index = j;
                            }
                        }
                    } else {
                        index = rand.nextInt(0, list.size() - 1);
                    }

                    population.add(lastFront.get(list.get(index)));
                    ro[id]++;

                    lastFront.remove(list.get(index));
                    num++;
                } else {
                    flag[id] = true;
                }

            }
        }

        double asfFunction(Solution sol, int j) {
            double max = Double.MIN_VALUE;
            double epsilon = 1.0E-6;

            for (int i = 0; i < obj; i++) {

                double val = Math.abs(sol.getObjective(i) - zideal[i]);

                if (j != i) {
                    val = val / epsilon;
                }

                if (val > max) {
                    max = val;
                }
            }

            return max;
        }

        public double calVDistance(Solution sol, double[] ref) {
            if (normalization) {
                return calNormlizedVDistance(sol, ref);
            } else {
                return calUnNormalizedVDistance(sol, ref);
            }
        }

        public double calNormlizedVDistance(Solution sol, double[] ref) {

            double ip = 0;
            double refLenSQ = 0;

            double [] normalized_ = (double[]) sol.getAttribute("NormalizedObjective");
            
            for (int j = 0; j < obj; j++) {

                ip += normalized_[j] * ref[j];
                refLenSQ += (ref[j] * ref[j]);
            }
            refLenSQ = Math.sqrt(refLenSQ);

            double d1 = Math.abs(ip) / refLenSQ;

            double d2 = 0;
            for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
                d2 += (normalized_[i] - d1 * (ref[i] / refLenSQ))
                        * (normalized_[i] - d1 * (ref[i] / refLenSQ));
            }
            d2 = Math.sqrt(d2);

            return d2;
        }

        double calUnNormalizedVDistance(Solution sol, double[] ref) {

            double d1=.0, d2, nl=.0;

            for (int i = 0; i < sol.getNumberOfObjectives(); i++) {
                d1 += (sol.getObjective(i) - zideal[i]) * ref[i];
                nl += (ref[i] * ref[i]);
            }
            nl = Math.sqrt(nl);
            d1 = Math.abs(d1) / nl;

            d2 = 0;
            for (int i = 0; i < sol.getNumberOfObjectives(); i++) {

                d2 += ((sol.getObjective(i) - zideal[i]) - d1
                        * (ref[i] / nl)) * ((sol.getObjective(i) - zideal[i]) - d1
                        * (ref[i] / nl));
            }
            d2 = Math.sqrt(d2);

            return d2;
        }
    }
    
    public static class Permutation {

        /**
         * Return a permutation vector between the 0 and (length - 1)
         *
         * @param length
         * @return
         */
        public int[] intPermutation(int length) {
            int[] aux = new int[length];
            int[] result = new int[length];
            JMetalRandom randomGenerator = JMetalRandom.getInstance();
            // First, create an array from 0 to length - 1. We call them result
            // Also is needed to create an random array of size length
            for (int i = 0; i < length; i++) {
                result[i] = i;
                aux[i] = randomGenerator.nextInt(0, length - 1);
            } // for

            // Sort the random array with effect in result, and then we obtain a
            // permutation array between 0 and length - 1
            for (int i = 0; i < length; i++) {
                for (int j = i + 1; j < length; j++) {
                    if (aux[i] > aux[j]) {
                        int tmp;
                        tmp = aux[i];
                        aux[i] = aux[j];
                        aux[j] = tmp;
                        tmp = result[i];
                        result[i] = result[j];
                        result[j] = tmp;
                    } // if
                } // for
            } // for   
            return result;
        }
    } // Permutation
}
