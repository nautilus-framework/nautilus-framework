package thiagodnf.nautilus.plugin.spl.encoding.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thiagodnf.nautilus.plugin.spl.encoding.instance.NewTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.instance.OldTXTInstanceData;

public class Converter {
	
	public static String toNewInstanceFormat(OldTXTInstanceData instance) {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("#number-of-products").append("\n");
		buffer.append(instance.getNumberOfProducts()).append("\n");
		buffer.append("#number-of-features").append("\n");
		buffer.append(instance.getNumberOfFeatures()).append("\n");
		buffer.append("#number-of-mutants").append("\n");
		buffer.append(instance.getNumberOfMutants()).append("\n");
		buffer.append("#number-of-pairs").append("\n");
		buffer.append(instance.getNumberOfPairs()).append("\n");
		buffer.append("#features").append("\n");
		buffer.append(Arrays.stream(instance.getFeatures()).reduce((x, y) -> x + " " + y).get()).append("\n");
		buffer.append("#products").append("\n");
		
		Map<String, String> features = new HashMap<>();

		for (int i = 0; i < instance.getFeatures().length; i++) {
			features.put(instance.getFeatures()[i], String.valueOf(i));
		}
		
		for (int i = 0; i < instance.getNumberOfProducts(); i++) {
			
			List<String> featuresIds = new ArrayList<>();

			for (String feature : instance.getProduct(i)) {
				featuresIds.add(features.get(feature));
			}
			
			buffer.append(featuresIds.stream().reduce((x, y) -> x + " " + y).get()).append("\n");
		}
		
		buffer.append("#mutants").append("\n");
		
		for (int i = 0; i < instance.getNumberOfProducts(); i++) {

			List<String> mutantIds = new ArrayList<>();
			
			for (int j = 0; j < instance.getNumberOfMutants(); j++) {

				if (instance.getMutantsProducts()[j][i] == 1) {
					mutantIds.add(String.valueOf(j));
				}
			}
			
			buffer.append(mutantIds.stream().reduce((x, y) -> x + " " + y).get()).append("\n");
		}
		
		buffer.append("#pairs").append("\n");
		
		for (int i = 0; i < instance.getNumberOfProducts(); i++) {

			List<String> pairIds = new ArrayList<>();
			
			for (int j = 0; j < instance.getNumberOfPairs(); j++) {

				if (instance.getPairsProducts()[j][i] == 1) {
					pairIds.add(String.valueOf(j));
				}
			}
			
			buffer.append(pairIds.stream().reduce((x, y) -> x + " " + y).get()).append("\n");
		}
		
		buffer.append("#costs").append("\n");
		
		for (int i = 0; i < instance.getNumberOfFeatures(); i++) {
			
			buffer.append(instance.getCosts()[i]);
			
			if(i+1 != instance.getNumberOfProducts()) {
				buffer.append(" ");
			}
		}
		
		buffer.append("\n");
		buffer.append("#importances").append("\n");
		
		for (int i = 0; i < instance.getNumberOfFeatures(); i++) {
			
			buffer.append(instance.getImportance()[i]);
			
			if(i+1 != instance.getNumberOfProducts()) {
				buffer.append(" ");
			}
		}
		
		return buffer.toString();
	}
	
	private static Path oldPath = Paths.get("src", "test", "resources", "old-eshop.txt");
	
	private static Path newPath = Paths.get("src", "test", "resources", "eshop.txt");

	public static void main(String[] args) {
		

		
		
		
		for(int i=0;i<30;i++) {
//			runOld();
			runNew();
		}
		
	}
	
	public static void runOld() {
		double startOld = System.currentTimeMillis();
		new OldTXTInstanceData(oldPath);
		System.out.println("Old: " + (System.currentTimeMillis() - startOld));
	}
	
	public static void runNew() {
		double startNew = System.currentTimeMillis();
		new NewTXTInstanceData(newPath);
		System.out.println("New: " + (System.currentTimeMillis() - startNew));
	}
}
