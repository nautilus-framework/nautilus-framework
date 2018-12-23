package thiagodnf.nautilus.core.util;

import java.util.Collections;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;

public class ProblemUtils {

	public static Problem<?> getFakeBinaryProblem(int numberOfObjectives, int numberOfVariables) {

		class FakeBinaryProblem extends AbstractBinaryProblem {

			private static final long serialVersionUID = -7078623923732675290L;

			public FakeBinaryProblem(int numberOfObjectives, int numberOfVariables) {
				setNumberOfVariables(numberOfVariables);
				setNumberOfObjectives(numberOfObjectives);
			}

			@Override
			public void evaluate(BinarySolution solution) {}

			@Override
			protected int getBitsPerVariable(int index) {
				return 0;
			}
		}

		return new FakeBinaryProblem(numberOfObjectives, numberOfVariables);
	}
	
	public static Problem<?> getFakeIntegerProblem(int numberOfObjectives, int numberOfVariables) {

		class FakeIntegerProblem extends AbstractIntegerProblem {

			private static final long serialVersionUID = -7078623923732675290L;

			public FakeIntegerProblem(int numberOfObjectives, int numberOfVariables) {

				setNumberOfVariables(numberOfVariables);
				setNumberOfObjectives(numberOfObjectives);

				setLowerLimit(Collections.nCopies(numberOfVariables, 0));
				setUpperLimit(Collections.nCopies(numberOfVariables, 10));
			}

			@Override
			public void evaluate(IntegerSolution solution) {
			}
		}

		return new FakeIntegerProblem(numberOfObjectives, numberOfVariables);
	}
	
}
