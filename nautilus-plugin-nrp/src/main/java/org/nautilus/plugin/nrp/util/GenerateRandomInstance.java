package org.nautilus.plugin.nrp.util;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.plugin.nrp.encoding.model.Requirement;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import com.github.javafaker.Faker;

public class GenerateRandomInstance {
	
	protected static JMetalRandom random = JMetalRandom.getInstance();
	
	protected static Faker faker = new Faker();
	
	public static String generate(int numberOfRequirements) {

		StringBuilder builder = new StringBuilder();

		builder.append("# number of requirements").append("\n");
		builder.append(numberOfRequirements).append("\n");

		List<Requirement> requirements = new ArrayList<>(numberOfRequirements);

		for (int i = 0; i < numberOfRequirements; i++) {
			requirements.add(Requirement.getRandom());
		}

		builder.append("# number of tasks").append("\n");

		for (int i = 0; i < requirements.size(); i++) {
			
			builder.append(requirements.get(i).tasks.size());

			if (i + 1 != requirements.size()) {
				builder.append(" ");
			}
		}

		builder.append("\n");
		
		for (int i = 0; i < requirements.size(); i++) {

			builder.append("# requirement " + i).append("\n");

			Requirement requirement = requirements.get(i);
			
			for (int j = 0; j < requirement.tasks.size(); j++) {
				
				builder.append(requirement.tasks.get(j).cost)
						.append(" ")
						.append(requirement.tasks.get(j).profit)
						.append(" ")
						.append(requirement.tasks.get(j).importance)
						.append("\n");		
			}
		}
		
		return builder.toString();
	}

}
