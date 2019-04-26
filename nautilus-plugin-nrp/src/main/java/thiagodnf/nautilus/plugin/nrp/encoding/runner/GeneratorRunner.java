package thiagodnf.nautilus.plugin.nrp.encoding.runner;

import thiagodnf.nautilus.plugin.nrp.util.GenerateRandomInstance;

public class GeneratorRunner {

	public static void main(String[] args) {
		
		System.out.println("Generating...");
		
		String content = GenerateRandomInstance.generate(100);
		
		System.out.println(content);
	}
}
