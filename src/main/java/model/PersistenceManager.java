package model;

import com.google.gson.Gson;
import org.hildan.fxgson.FxGson;
import org.hildan.fxgson.FxGsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {

    public static<T> String serializeParametrizedObject(T object){
        Gson gson = new FxGsonBuilder().builder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static<T> T deserializeParametrizedObject(String json, Class<T> tClass) {
        Gson gson = FxGson.create();
        return  gson.fromJson(json, tClass);
    }


    public static String serializeObjectsArrayToJson(List arrayList){
        Gson gson = FxGson.create();
        return gson.toJson(arrayList);
    }


    public static <T> List<T> deserializeFromJsonArray(String sJson, Class<T> tClass, boolean withFormat){
        try {
            Gson gson;
            if (withFormat) gson = new FxGsonBuilder().builder().setDateFormat("dd-MM-yyyy").create();
            else gson = FxGson.create();
            List<T> objectsList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(sJson);

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                objectsList.add(gson.fromJson(jsonObject.toString(), tClass));
            }
            return objectsList;

        } catch(Exception e) { e.printStackTrace(); }

        return null;
    }
}
