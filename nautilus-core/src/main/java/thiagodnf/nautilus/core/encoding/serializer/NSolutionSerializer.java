package thiagodnf.nautilus.core.encoding.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.encoding.solution.NDoubleSolution;
import thiagodnf.nautilus.core.encoding.solution.NIntegerSolution;

public class NSolutionSerializer implements JsonSerializer<NSolution<?>>{

	@Override
	public JsonElement serialize(NSolution<?> solution, Type typeOfSrc, JsonSerializationContext context) {
		
		if (solution == null)
			return null;

		JsonElement jElement = null;

		if (solution instanceof NIntegerSolution) {
			jElement = context.serialize(solution, NIntegerSolution.class);
		} else if (solution instanceof NDoubleSolution) {
			jElement = context.serialize(solution, NDoubleSolution.class);
		} else if (solution instanceof NBinarySolution) {
			jElement = context.serialize(solution, NBinarySolution.class);
		} else {
			throw new RuntimeException("Unknow class: " + solution.getClass());
		}

		jElement.getAsJsonObject().addProperty("_class", solution.getClass().getSimpleName());

		return jElement;
	}
}
