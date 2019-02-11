package thiagodnf.nautilus.plugin.spl.encoding.runner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import thiagodnf.nautilus.plugin.spl.encoding.instance.OldTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.util.Converter;

public class ConverterRunner {

	public static void main(String[] args) {
		
		System.out.println("Converting...");
		
		String file = "drupal";
		
		Path input = Paths.get("src", "main", "resources", "old-instances", "old-" + file + ".txt");

		Path output = Paths.get("src", "test", "resources", file + ".txt");

		OldTXTInstanceData instance = new OldTXTInstanceData(input);
		
		String part1Content = Converter.getPartOne(instance);
		

		System.out.println("Saving...");
		
		try {
			FileUtils.write(output.toFile(), part1Content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Converter.getPartTwo(output.toFile(), instance);

		System.out.println("Done");
	}
}
