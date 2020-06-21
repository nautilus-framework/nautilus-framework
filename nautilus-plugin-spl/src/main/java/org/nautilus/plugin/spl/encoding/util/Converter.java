package org.nautilus.plugin.spl.encoding.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Converter {
	
//	public static String getPartOne(OldTXTInstanceData instance) {
//		
//		System.out.println("Processing part 1...");
//		
//		StringBuilder buffer = new StringBuilder();
//		
//		buffer.append("#number-of-products").append("\n");
//		buffer.append(instance.getNumberOfProducts()).append("\n");
//		buffer.append("#number-of-features").append("\n");
//		buffer.append(instance.getNumberOfFeatures()).append("\n");
//		buffer.append("#number-of-mutants").append("\n");
//		buffer.append(instance.getNumberOfMutants()).append("\n");
//		buffer.append("#number-of-pairs").append("\n");
//		buffer.append(instance.getNumberOfPairs()).append("\n");
//		buffer.append("#features").append("\n");
//		buffer.append(Arrays.stream(instance.getFeatures()).reduce((x, y) -> x + " " + y).get()).append("\n");
//		buffer.append("#products").append("\n");
//		
//		Map<String, String> features = new HashMap<>();
//
//		for (int i = 0; i < instance.getFeatures().length; i++) {
//			features.put(instance.getFeatures()[i], String.valueOf(i));
//		}
//		
//		for (int i = 0; i < instance.getNumberOfProducts(); i++) {
//			
//			List<String> featuresIds = new ArrayList<>();
//
//			for (String feature : instance.getProduct(i)) {
//				featuresIds.add(features.get(feature));
//			}
//			
//			buffer.append(featuresIds.stream().reduce((x, y) -> x + " " + y).get()).append("\n");
//		}
//		
//		buffer.append("#mutants").append("\n");
//		
//		for (int i = 0; i < instance.getNumberOfProducts(); i++) {
//
//			List<String> mutantIds = new ArrayList<>();
//			
//			for (int j = 0; j < instance.getNumberOfMutants(); j++) {
//
//				if (instance.getMutantsProducts()[j][i] == 1) {
//					mutantIds.add(String.valueOf(j));
//				}
//			}
//			
//			buffer.append(mutantIds.stream().reduce((x, y) -> x + " " + y).get()).append("\n");
//		}
//		
//		buffer.append("#pairs").append("\n");
//		
//		for (int i = 0; i < instance.getNumberOfProducts(); i++) {
//
//			List<String> pairIds = new ArrayList<>();
//			
//			for (int j = 0; j < instance.getNumberOfPairs(); j++) {
//
//				if (instance.getPairsProducts()[j][i] == 1) {
//					pairIds.add(String.valueOf(j));
//				}
//			}
//			
//			buffer.append(pairIds.stream().reduce((x, y) -> x + " " + y).get()).append("\n");
//		}
//		
//		buffer.append("#costs").append("\n");
//		
//		for (int i = 0; i < instance.getNumberOfFeatures(); i++) {
//			
//			buffer.append(instance.getCosts()[i]);
//			
//			if(i+1 != instance.getNumberOfProducts()) {
//				buffer.append(" ");
//			}
//		}
//		
//		buffer.append("\n");
//		buffer.append("#importances").append("\n");
//		
//		for (int i = 0; i < instance.getNumberOfFeatures(); i++) {
//			
//			buffer.append(instance.getImportance()[i]);
//			
//			if(i+1 != instance.getNumberOfProducts()) {
//				buffer.append(" ");
//			}
//		}
//		
//		buffer.append("\n");
//		
//		return buffer.toString();
//	}
	
//	public static void getPartTwo(File output, OldTXTInstanceData instance) {
//		
//		System.out.println("Processing part 2...");
//		
//		write(output, "#similarities\n", true);
//		
//		double[][] similarity = instance.getSimilarity();
//
//		for (int i = 0; i < similarity.length; i++) {
//
//			for (int j = i; j < similarity[i].length; j++) {
//
//				if (i != j) {
//					
//					BigDecimal bd = new BigDecimal(similarity[i][j]);
//
//					bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//
//					String content = i + " " + j + " " + bd.doubleValue();
//					
//					if (j + 1 != similarity[i].length) {
//						//lines.add(i + " " + j + " " + bd.doubleValue() + "");
//						write(output, content+"\n", true);
//					} else {
//						write(output, content, true);
//					}
//						
//						
//				}
//			}
//
//			if (i + 1 != similarity.length) {
//				write(output, "\n", true);
//			}
//		}
//		
//		//lines.remove(lines.size()-1);
//		
//		//return lines;
//		
////		String result = "";
////		
////		for(String line : lines) {
////			result += line;
////		}
////		
////		return result;
//	}
	
	public static void write(File output, String content) {
		write(output, content, true);
	}

	public static void write(File output, String content, boolean append) {
		try {
			FileUtils.write(output, content, append);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
