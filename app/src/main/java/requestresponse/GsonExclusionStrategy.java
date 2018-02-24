package requestresponse;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by gigin.ginanjar on 10/06/2016.
 */
public class GsonExclusionStrategy implements ExclusionStrategy {
    private Class<?> typeToSkip;
    public GsonExclusionStrategy(){}
    public GsonExclusionStrategy(Class<?> typeToSkip){
        this.typeToSkip = typeToSkip;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(ExcludeFromGson.class) !=null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return (aClass == typeToSkip);
    }
}
