package requestresponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GsonHelper {
	private static Gson gson = new GsonBuilder().setDateFormat("ddMMyyyyHHmmss").registerTypeHierarchyAdapter(byte[].class,
			new ByteArrayToBase64TypeAdapter()).setExclusionStrategies(new GsonExclusionStrategy()).create();

	 public static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
	        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	            return Base64.decode(json.getAsString());
	        }
	 
	        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
	            return new JsonPrimitive(new String(Base64.encode(src)));
	        }
	    }

	public static <T> T fromJson(String json, Class<T> classOfT) {
		if (gson == null) {
			gson = new GsonBuilder().setDateFormat("ddMMyyyyHHmmss").registerTypeHierarchyAdapter(byte[].class,
					new ByteArrayToBase64TypeAdapter()).setExclusionStrategies(new GsonExclusionStrategy()).create();
		}
		return gson.fromJson(json, classOfT);
	}


	public static String toJson(Object src)
	{
		if(gson==null){
			gson = new GsonBuilder().setDateFormat("ddMMyyyyHHmmss").registerTypeHierarchyAdapter(byte[].class,
					new ByteArrayToBase64TypeAdapter()).setExclusionStrategies(new GsonExclusionStrategy()).create();
		}
		return gson.toJson(src);
	}





}
