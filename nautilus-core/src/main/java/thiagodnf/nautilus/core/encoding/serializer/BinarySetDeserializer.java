package thiagodnf.nautilus.core.encoding.serializer;

import java.lang.reflect.Type;

import org.uma.jmetal.util.binarySet.BinarySet;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class BinarySetDeserializer implements JsonDeserializer<BinarySet>{

	@Override
	public BinarySet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		
		JsonArray jsonArray = json.getAsJsonArray();

		BinarySet binarySet = new BinarySet(jsonArray.size());

		for (int i = 0; i < jsonArray.size(); i++) {
			
			if (jsonArray.get(i).getAsInt() == 1) {
				binarySet.set(i, true);
			} else {
				binarySet.set(i, false);
			}
		}
		
		return binarySet;
	}	
}
