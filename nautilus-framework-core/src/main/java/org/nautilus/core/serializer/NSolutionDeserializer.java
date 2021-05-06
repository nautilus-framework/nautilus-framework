package org.nautilus.core.serializer;

import java.lang.reflect.Type;

import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.encoding.solution.NDoubleSolution;
import org.nautilus.core.encoding.solution.NIntegerSolution;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class NSolutionDeserializer implements JsonDeserializer<NSolution<?>>{

	@Override
	public NSolution<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jsonObject = json.getAsJsonObject();

		JsonElement type = jsonObject.get("_class");

		if (type != null) {

			switch (type.getAsString()) {
				case "NIntegerSolution":
					return context.deserialize(jsonObject, NIntegerSolution.class);
				case "NDoubleSolution":
					return context.deserialize(jsonObject, NDoubleSolution.class);
				case "NBinarySolution":
					return context.deserialize(jsonObject, NBinarySolution.class);
			}
		}
		
		return null;
	}	
}
