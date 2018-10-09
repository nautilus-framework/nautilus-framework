package thiagodnf.nautilus.core.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class InstanceReader {

	protected FileReader reader;
	
	protected File filename;
	
	protected String content;
	
	protected String[] lines;
	
	protected String separator = ",";
	
	protected int index = 0;
	
	/**
	 * Constructor 
	 * 
	 * @param path  the file should be read
	 */
	public InstanceReader(Path path) {

		try {
			this.content = FileUtils.readFileToString(path.toFile());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.lines = this.content.split("\n");
	}
	
	/**
	 * Return the number of lines in the read file;
	 * 
	 * @return the number of lines
	 */
	public int getNumberOfLines(){
		return this.lines.length;
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
			matrix[i] = lines.get(i).split(separator);
		}

		return matrix;
	}
	
	public String readLine() {
		
		if(index >= getNumberOfLines()){
			return null;
		}
				
		return this.lines[index++].trim();
	}
	
	public List<String> readLines(int numberOfLines) {

		List<String> lines = new ArrayList<>();

		for (int i = 0; i < numberOfLines; i++) {
			lines.add(readLine());
		}

		return lines;
	}
}
