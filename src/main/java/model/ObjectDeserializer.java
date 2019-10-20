package model;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObjectDeserializer {

    public static <T> List<T> convertFromJsonArray(String sJson, Class<T> tClass){
        try{
            Gson gson = new Gson();
            List<T> objectsList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(sJson);

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                System.out.println(jsonObject.toString());
                gson.fromJson(jsonObject.toString(), tClass);
                System.out.println("pp");
                objectsList.add(gson.fromJson(jsonObject.toString(), tClass));
            }
            return objectsList;

        } catch(Exception e){ e.printStackTrace(); }

        return null;
    }
}
