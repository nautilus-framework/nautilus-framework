package thiagodnf.nautilus.core.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

public class InstanceReader {

	protected FileReader reader;
	
	protected File filename;
	
	protected List<String> lines;
	
	protected String separator = " ";
	
	protected int index = 0;
	
	protected Splitter splitter = Splitter.on(separator);
	/**
	 * Constructor 
	 * 
	 * @param path  the file should be read
	 */
	public InstanceReader(Path path, String separator) {
		
		Preconditions.checkNotNull(path, "The path should not be null");
		Preconditions.checkArgument(Files.exists(path), "The path does not exists");

		this.separator = separator;
		
		try {
			
			this.lines = Files.readAllLines(path)
					.stream()
					.map(i -> i.trim())
					.collect(Collectors.toList());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public InstanceReader(Path path) {
		this(path, ",");
	}
	
	/**
	 * Return the number of lines in the read file;
	 * 
	 * @return the number of lines
	 */
	public int getNumberOfLines(){
		return this.lines.size();
	}
	
	/**
	 * Define the separator used when split the line
	 * 
	 * @param separator the character used to split the line
	 */
	public void setSeparator(String separator){
		this.separator = separator;
	}
	
	/**
	 * Return the separator used for splitting a string
	 * 
	 * @return the separator
	 */
	public String getSeparator(){
		return this.separator;
	}
	
	public int getInteger(){
		return getIntegerHorizontalArray()[0];
	}
	
	public int[][] getIntegerMatrix(int numberOfLines) {
		return Converter.toIntegerMatrix(getStringMatrix(numberOfLines));
	}
	
	public int[] getIntegerHorizontalArray() {
		return getIntegerMatrix(1)[0];
	}

	public int[] getIntegerVerticalArray(int numberOfLines) {

		int[] array = new int[numberOfLines];

		int[][] matrix = getIntegerMatrix(numberOfLines);

		for (int i = 0; i < numberOfLines; i++) {
			array[i] = matrix[i][0];
		}

		return array;
	}
	
	
	
	public double getDouble(){
		return getDoubleHorizontalArray()[0];
	}
	
	public double[][] getDoubleMatrix(int numberOfLines) {
		return Converter.toDoubleMatrix(getStringMatrix(numberOfLines));
	}
	
	public double[] getDoubleHorizontalArray() {
		return getDoubleMatrix(1)[0];
	}
	
	public double[] getDoubleVerticalArray(int numberOfLines) {

		double[] array = new double[numberOfLines];

		double[][] matrix = getDoubleMatrix(numberOfLines);

		for (int i = 0; i < numberOfLines; i++) {
			array[i] = matrix[i][0];
		}

		return array;
	}
	
	
	
	
	public String getString(){
		return getStringHorizontalArray()[0];
	}
	
	public String[] getStringHorizontalArray() {
		return getStringMatrix(1)[0];
	}
	
	public String[] getStringVerticalArray(int numberOfLines) {

		String[] array = new String[numberOfLines];

		String[][] matrix = getStringMatrix(numberOfLines);

		for (int i = 0; i < numberOfLines; i++) {
			array[i] = matrix[i][0];
		}

		return array;
	}
	
	public String[][] getStringMatrix(int numberOfLines) {

		String[][] matrix = new String[numberOfLines][];

		List<String> lines = readLines(numberOfLines);
		
		for (int i = 0; i < lines.size(); i++) {
			
			List<String> parts = new LinkedList<>();
			
			Iterable<String> it = splitter.split(lines.get(i));
			
			for(String s: it){
				parts.add(s);
			}
			
			String[] m = new String[parts.size()];

			for (int j = 0; j < parts.size(); j++) {
				m[j] = parts.get(j);
			}
			
			matrix[i] = m;
			
//			matrix[i] = lines.get(i).split(separator);
		}

		return matrix;
	}
	
	public void ignoreLine() {
		readLine();
	}
	
	public String readLine() {

		if (index >= getNumberOfLines()) {
			return null;
		}

		return this.lines.get(index++).trim();
	}
	
	public List<String> readLines(int numberOfLines) {

		List<String> lines = new ArrayList<>();

		for (int i = 0; i < numberOfLines; i++) {
			lines.add(readLine());
		}

		return lines;
	}
	
	public List<String> readStringValues() {

		String[] parts = readLine().split(separator);

		return new ArrayList<>(Arrays.asList(parts));
	}
	
	public List<Integer> readIntegerValues() {

		List<String> values = readStringValues();

		List<Integer> integerList = new ArrayList<>();

		for (String value : values) {
			integerList.add(Integer.valueOf(value));
		}

		return integerList;
	}
	
	public List<Double> readDoubleValues() {

		List<String> values = readStringValues();

		List<Double> doubleList = new ArrayList<>();

		for (String value : values) {
			doubleList.add(Double.valueOf(value));
		}

		return doubleList;
	}
	
	public int readIntegerValue() {
		return readIntegerValues().get(0);
	}
}
