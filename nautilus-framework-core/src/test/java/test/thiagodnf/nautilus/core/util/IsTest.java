package test.thiagodnf.nautilus.core.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.nautilus.core.util.Is;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;
import org.uma.jmetal.solution.impl.DefaultIntegerSolution;
import org.uma.jmetal.util.binarySet.BinarySet;

import com.google.common.primitives.Doubles;

public class IsTest {
	
	class FakeBinaryProblem extends AbstractBinaryProblem {
		
		private static final long serialVersionUID = 19373637332L;

		public FakeBinaryProblem() {
			this(1, 2);
		}
		
		public FakeBinaryProblem(int numberOfVariables, int numberOfObjectives) {
			setNumberOfVariables(numberOfVariables);
			setNumberOfObjectives(numberOfObjectives);
		}
		
		@Override
		public void evaluate(BinarySolution solution) {}
		
		@Override
		protected int getBitsPerVariable(int index) {
			return 10;
		}
	};
	
	class FakeIntegerProblem extends AbstractIntegerProblem {
		
		private static final long serialVersionUID = 193736372332L;

		public FakeIntegerProblem() {
			this(1, 2);
		}
		
		public FakeIntegerProblem(int numberOfVariables, int numberOfObjectives) {
			setNumberOfVariables(numberOfVariables);
			setNumberOfObjectives(numberOfObjectives);

			List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
			List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

			for (int i = 0; i < getNumberOfVariables(); i++) {
				lowerLimit.add(0);
				upperLimit.add(1);
			}

			setLowerLimit(lowerLimit);
			setUpperLimit(upperLimit);
		}

		@Override
		public void evaluate(IntegerSolution solution) {}
	};

	@Test
	public void shouldThrowExceptionWhenInitiateThisClass() throws IllegalAccessException, InstantiationException {
		
		final Class<?> cls = Is.class;
		
		final Constructor<?> c = cls.getDeclaredConstructors()[0];
		
		c.setAccessible(true);

		assertThrows(InvocationTargetException.class, () -> {
			c.newInstance((Object[]) null);
		});
	}

	@Test
	public void shouldThrowNullExceptionWithNullParameters() {

		assertThrows(NullPointerException.class, () -> {
			Is.equal((BinarySet)null, (BinarySet)null);
		});
		
		assertThrows(NullPointerException.class, () -> {
			Is.equal((List<Double>)null, (List<Double>)null);
		});
		
		assertThrows(NullPointerException.class, () -> {
			Is.equal((BinarySolution)null, (BinarySolution)null);
		});
		
		assertThrows(NullPointerException.class, () -> {
			Is.equal((IntegerSolution)null, (IntegerSolution)null);
		});
	}
	
	@Test
	public void shouldReturnFalseWhenAListHasDifferentSize() {
		
		List<Double> a = Arrays.asList(1.0, 2.0);
		List<Double> b = Arrays.asList(1.0, 2.0, 3.0);
		
		assertFalse(Is.equal(a, b));
	}
	
	@Test
	public void shouldReturnFalseWhenAnArraysHasDifferentSize() {
		
		assertFalse(Is.equal(new double[] {1.0}, new double[] {1.0, 2.0}));
	}
	
	@Test
	public void shouldReturnFalseWhenTwoListAreDifferent() {
		
		List<Double> a = Arrays.asList(1.0, 2.0);
		List<Double> b = Arrays.asList(2.0, 2.0);
		
		assertFalse(Is.equal(a, b));
	}
	
	@Test
	public void shouldReturnFalseWhenTwoArraysAreDifferent() {
		
		assertFalse(Is.equal(new double[] {1.0,1.0}, new double[] {1.0, 2.0}));
	}
	
	@Test
	public void shouldReturnTrueWhenTwoListAreEquals() {
		
		List<Double> a = Doubles.asList(1.0, 2.0);
		List<Double> b = Doubles.asList(1.0, 2.0);
		
		assertTrue(Is.equal(a, b));
	}
	
	@Test
	public void shouldReturnTrueWhenTwoArraysAreEquals() {
		
		assertTrue(Is.equal(new double[] {1.0,2.0}, new double[] {1.0, 2.0}));
	}
	
	// Test Binary Solutions
	
	@Test
	public void shouldReturnTrueWhenTwoBinarySolutionsAreEquals() {
		
		DefaultBinarySolution s1 = new DefaultBinarySolution(new FakeBinaryProblem());
		DefaultBinarySolution s2 = new DefaultBinarySolution(new FakeBinaryProblem());
		
		s1.getVariableValue(0).clear();
		s2.getVariableValue(0).clear();
		
		s1.getVariableValue(0).set(1);
		s2.getVariableValue(0).set(1);
		
		assertTrue(Is.equal(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenTwoBinarySolutionsAreDifferent() {
		
		DefaultBinarySolution s1 = new DefaultBinarySolution(new FakeBinaryProblem());
		DefaultBinarySolution s2 = new DefaultBinarySolution(new FakeBinaryProblem());
		
		s1.getVariableValue(0).clear();
		s2.getVariableValue(0).clear();
		
		s1.getVariableValue(0).set(1);
		
		assertFalse(Is.equal(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenABinarySolutionHasDifferentNumberOfVariables() {
		
		DefaultBinarySolution s1 = new DefaultBinarySolution(new FakeBinaryProblem(1,2));
		DefaultBinarySolution s2 = new DefaultBinarySolution(new FakeBinaryProblem(2,2));
		
		assertFalse(Is.equal(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenABinarySolutionHasDifferentNumberOfObjectives() {
		
		DefaultBinarySolution s1 = new DefaultBinarySolution(new FakeBinaryProblem(1,2));
		DefaultBinarySolution s2 = new DefaultBinarySolution(new FakeBinaryProblem(1,1));
		
		assertFalse(Is.equal(s1, s2));
	}
	
	// Test Integer Solutions
	
	@Test
	public void shouldReturnTrueWhenTwoIntegerSolutionsAreEquals() {
		
		DefaultIntegerSolution s1 = new DefaultIntegerSolution(new FakeIntegerProblem(2, 2));
		DefaultIntegerSolution s2 = new DefaultIntegerSolution(new FakeIntegerProblem(2, 2));
		
		s1.setVariableValue(0, 1);
		s1.setVariableValue(1, 2);
		
		s2.setVariableValue(0, 1);
		s2.setVariableValue(1, 2);
		
		assertTrue(Is.equal(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenTwoIntegerSolutionsAreDifferent() {
		
		DefaultIntegerSolution s1 = new DefaultIntegerSolution(new FakeIntegerProblem(2, 2));
		DefaultIntegerSolution s2 = new DefaultIntegerSolution(new FakeIntegerProblem(2, 2));
		
		s1.setVariableValue(0, 1);
		s1.setVariableValue(1, 2);
		
		s2.setVariableValue(0, 2);
		s2.setVariableValue(1, 2);
		
		assertFalse(Is.equal(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenAnIntegerSolutionHasDifferentNumberOfVariables() {
		
		DefaultIntegerSolution s1 = new DefaultIntegerSolution(new FakeIntegerProblem(1, 2));
		DefaultIntegerSolution s2 = new DefaultIntegerSolution(new FakeIntegerProblem(2, 2));
		
		s1.setVariableValue(0, 1);
		
		s2.setVariableValue(0, 2);
		s2.setVariableValue(1, 2);
		
		assertFalse(Is.equal(s1, s2));
	}
	
	@Test
	public void shouldReturnFalseWhenAnIntegerSolutionHasDifferentNumberOfObjectives() {
		
		DefaultIntegerSolution s1 = new DefaultIntegerSolution(new FakeIntegerProblem(1, 2));
		DefaultIntegerSolution s2 = new DefaultIntegerSolution(new FakeIntegerProblem(1, 1));
		
		assertFalse(Is.equal(s1, s2));
	}
}
