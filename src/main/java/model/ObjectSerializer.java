package model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ObjectSerializer {

    public static String serializeParticipantsArrayToJson(ArrayList<Participant> participants){

        Gson gson = new Gson();
        return gson.toJson(participants);
    }
}
