package thiagodnf.nautilus.plugin.spl.encoding.runner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.spl.encoding.instance.OldTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.util.Converter;
import thiagodnf.nautilus.plugin.spl.extension.instance.SPLInstanceDataExtension;

public class ConverterRunner {

	public static void main(String[] args) {
		
		System.out.println("Converting...");
		
		Path input = Paths.get("src", "test", "resources", "old-smarthome.txt");
		
		Path output = Paths.get("src", "test", "resources", "smarthome.txt");

		OldTXTInstanceData instance = new OldTXTInstanceData(input);
		
		String content = Converter.toNewInstanceFormat(instance);

		try {
			FileUtils.write(output.toFile(), content);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}
}
