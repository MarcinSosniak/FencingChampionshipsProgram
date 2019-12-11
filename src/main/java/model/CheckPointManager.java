package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import controller.ApplicationController;
import model.config.ConfigReader;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class CheckPointManager {

    // path should be e.g saves/11-11-2019__14-39-53
    public static void readFromCheckPoint(String path){
        // init competition
        try {
            FileInputStream file = new FileInputStream(path + "/competition.bin");
            ObjectInputStream in = new ObjectInputStream(file);
            Competition.init((Competition) in.readObject());
            in.close();
            file.close();
        }
        catch(IOException ex) { System.out.println("IOException while deserialization " + ex.toString()); }
        catch(ClassNotFoundException ex) { System.out.println("ClassNotFoundException is caught"); }

        // init config reader
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "/configReader.json"));
            String configString = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) configString = configString + line;
            ConfigReader.setDeserializedInstance(
                    PersistenceManager.deserializeParametrizedObject(configString, ConfigReader.class));
        } catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createCheckPoint(){
        String path = getDirectionNameForSaves();
        createCheckPoint(path);
    }

    public static void createCheckPoint(String path){

        new File(path).mkdir();

        try {
            String competitionPath = path + "/competition.bin";
            FileOutputStream file = new FileOutputStream(competitionPath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(Competition.getInstance());
            out.close();
            file.close();

            String configReaderString = PersistenceManager.serializeParametrizedObject(ConfigReader.getInstance());
            String configReaderPath = path + "/configReader.json";
            FileOutputStream os2 = new FileOutputStream(new File(configReaderPath), true);
            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(os2));
            bw2.append(configReaderString);
            bw2.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private static String getDirectionNameForSaves(){
        int year = ZonedDateTime.now(ZoneId.of("Europe/Warsaw") ).getYear();
        int month = ZonedDateTime.now(ZoneId.of("Europe/Warsaw") ).getMonthValue();
        int day = ZonedDateTime.now(ZoneId.of("Europe/Warsaw") ).getDayOfMonth();
        int hour =  ZonedDateTime.now(ZoneId.of("Europe/Warsaw") ).getHour();
        int minutes =  ZonedDateTime.now(ZoneId.of("Europe/Warsaw") ).getMinute();
        int seconds =  ZonedDateTime.now(ZoneId.of("Europe/Warsaw") ).getSecond();

        if (Competition.getInstance().getCompetitionName() != null)
            return Competition.getInstance().getCompetitionName() + "_" + +hour+"-"+minutes+"-"+seconds;
        return  "saves/"+day +"-"+month +"-"+year+"__"+hour+"-"+minutes+"-"+seconds;
    }


}
