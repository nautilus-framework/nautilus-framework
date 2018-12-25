package thiagodnf.nautilus.core.serializer;

import java.lang.reflect.Type;

import org.uma.jmetal.util.binarySet.BinarySet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BinarySetSerializer implements JsonSerializer<BinarySet>{

	@Override
	public JsonElement serialize(BinarySet binarySet, Type typeOfSrc, JsonSerializationContext context) {
		
		JsonArray array = new JsonArray();

		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
			array.add(binarySet.get(i) ? 1 : 0);
		}

		return array;
	}
}
