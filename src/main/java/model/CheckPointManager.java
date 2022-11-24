package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import controller.ApplicationController;
import model.config.ConfigReader;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CheckPointManager {

    // path should be e.g saves/{name}/11-11-2019__14-39-53
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

    public static void createCheckPoint(String path) {
        String save_name = path.contains("/") ? path.substring(0,path.lastIndexOf("/")) : path.substring(0,path.lastIndexOf("\\"));
        new File(save_name).mkdir();
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
        try {
            saveTextState();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static String getDirectionNameForSaves(){
        ZonedDateTime current = ZonedDateTime.now();
        int year = current.getYear();
        int month = current.getMonthValue();
        int day = current.getDayOfMonth();
        int hour =  current.getHour();
        int minutes =  current.getMinute();
        int seconds =  current.getSecond();


        String strDate = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss").format(current);

        if (Competition.getInstance().getCompetitionName() == null)
            Competition.getInstance().setCompetitionName(strDate);

//        return "saves/" + Competition.getInstance().getCompetitionName() + "__" + + hour+"-"+minutes+"-"+seconds;
        return "saves/" + Competition.getInstance().getCompetitionName()+ "/" + strDate;
    }


    public static void saveTextState() {
        try {
            File f = new File("backup_dumps");
            if (!f.isDirectory()) {
                f.mkdir();
            }

            String filename = "backup_dumps/"
                    + DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss").format(ZonedDateTime.now())
                    +".txt";

            FileOutputStream os2 = new FileOutputStream(new File(filename), true);

            try (BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(os2))) {
                Competition instance = Competition.getInstance();
                for (WeaponCompetition wc : instance.getWeaponCompetitions()) {
                    bw2.append("=============================================================\n");
                    bw2.append("\n\n    ").append(wc.getWeaponType().name()).append("\n\n");
                    for (Round r : wc.getRoundsCopy()) {
                        bw2.append("===\n");
                        bw2.append("ROUND ").append(String.valueOf(r.getRoundNumber())).append("\n");
                        for (CompetitionGroup g : new ArrayList<CompetitionGroup>(r.getGroups())) {
                            bw2.append("  Group Name : ").append(g.getGroupID()).append("\n");
                            for (Fight fight : new ArrayList<Fight>(g.getFightsList())) {
                                bw2.append("    ")
                                        .append(fight.getFirstParticipantStringProperty())
                                        .append(" vs ")
                                        .append(fight.getSecondParticipantStringProperty())
                                        .append("  result = ")
                                        .append(fight.getScore().name())
                                        .append("\n");
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


}
