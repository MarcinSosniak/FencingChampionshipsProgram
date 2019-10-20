package model;

import com.google.gson.Gson;
import org.hildan.fxgson.FxGson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {

    public static String serializeObjectsArrayToJson(ArrayList arrayList){
        Gson gson = FxGson.create();
        return gson.toJson(arrayList);
    }

    public static Participant[] convertJsonToParticipantArray(String json){
        Gson gson = FxGson.create();
        return  gson.fromJson(json, Participant[].class);
    }


    public static <T> List<T> deserializeFromJsonArray(String sJson, Class<T> tClass){
        try{
            Gson gson = FxGson.create();
            List<T> objectsList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(sJson);

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                gson.fromJson(jsonObject.toString(), tClass);
                objectsList.add(gson.fromJson(jsonObject.toString(), tClass));
            }
            return objectsList;

        } catch(Exception e){ e.printStackTrace(); }

        return null;
    }
}
