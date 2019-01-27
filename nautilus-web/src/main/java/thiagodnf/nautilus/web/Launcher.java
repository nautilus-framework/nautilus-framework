package thiagodnf.nautilus.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Launcher {

	public static void main(String[] args) {
		SpringApplication.run(Launcher.class, args);
		
//		double[][] data = new double[][] {
//			{64.0, 580.0, 29.0},
//		    {66.0, 570.0, 33.0},
//		    {68.0, 590.0, 37.0},
//		    {69.0, 660.0, 46.0},
//		    {73.0, 600.0, 55.0}
//		};
//		
//		Covariance covariance = new Covariance(data);
//		
//		double[][] matrix = covariance.getCovarianceMatrix().getData();
//
//		for (int i = 0; i < matrix.length; i++) {
//			System.out.println(Arrays.toString(matrix[i]));
//		}
//		
//		System.out.println("------");
//		
//		matrix = new PearsonsCorrelation().computeCorrelationMatrix(data).getData();
//
//		for (int i = 0; i < matrix.length; i++) {
//			System.out.println(Arrays.toString(matrix[i]));
//		}
//		
//		System.out.println("------");
//		
////		double[][] m = new double[][] {
////			{1.0000, 0.9997, -0.9182},
////			{0.9997, 1.0000, -0.9190},
////			{-0.9182, -0.9190, 1.0000}
////		};
//		
//		double[][] m = new double[][] {
//			{1, -3, 3},
//			{3, -5, 3},
//			{6, -6, 4},
//		};
//		
//		RealMatrix rm = MatrixUtils.createRealMatrix(m);
//		
//		
//		
//		EigenDecomposition eigen = new EigenDecomposition(rm);
//		
//		double[] eigenvalues = eigen.getRealEigenvalues();
//		
//		
//		System.out.println("Eignvalues");
//		System.out.println(Arrays.toString(eigenvalues));
//		
//		System.out.println("Eignvectors");
//		System.out.println(eigen.getEigenvector(0));
//		System.out.println(eigen.getEigenvector(1));
//		System.out.println(eigen.getEigenvector(2));
	}
}
