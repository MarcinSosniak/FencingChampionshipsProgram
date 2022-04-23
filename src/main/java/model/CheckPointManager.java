package model;

import model.config.ConfigReader;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;


public class CheckPointManager {

    // path should be e.g saves/11-11-2019__14-39-53
    public static void readFromCheckPoint(String path) {
        // init competition
        try {
            FileInputStream file = new FileInputStream(path + "/competition.bin");
            ObjectInputStream in = new ObjectInputStream(file);
            Competition.init((Competition) in.readObject());
            in.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException while deserialization " + ex.toString());
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        // init config reader
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path + "/configReader.json"));
            String configString = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) configString = configString + line;
            ConfigReader.setDeserializedInstance(
                    PersistenceManager.deserializeParametrizedObject(configString, ConfigReader.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createCheckPoint() {
        List<String> path = getDirectionNameForSaves();
        createCheckPoint(path);
    }

    public static void createCheckPoint(List<String> path) {
        for (int i = 0; i < path.size(); i++) {
            String addedPath = path.subList(0, i+1).stream().reduce("", (partial, elem) -> partial + "/" + elem);
            File f = new File(addedPath.substring(1));
            if (!f.isDirectory()) {
                f.mkdir();
            }

        }
        createCheckPoint(path.stream().reduce("", (partial, elem) -> partial + "/" + elem).substring(1));

    }

    public static void createCheckPoint(String checkedPath) {
        try {
            String competitionPath = checkedPath + "/competition.bin";
            FileOutputStream file = new FileOutputStream(competitionPath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(Competition.getInstance());
            out.close();
            file.close();

            String configReaderString = PersistenceManager.serializeParametrizedObject(ConfigReader.getInstance());
            String configReaderPath = checkedPath + "/configReader.json";
            FileOutputStream os2 = new FileOutputStream(new File(configReaderPath), true);
            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(os2));
            bw2.append(configReaderString);
            bw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getDirectionNameForSaves() {
        int year = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getYear();
        int month = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getMonthValue();
        int day = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getDayOfMonth();
        int hour = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getHour();
        int minutes = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getMinute();
        int seconds = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getSecond();

        if (Competition.getInstance().getCompetitionName() == null)
            Competition.getInstance().setCompetitionName(day + "-" + month + "-" + year);
        return Arrays.asList("saves", Competition.getInstance().getCompetitionName(), String.format("%02d-%02d-%02d", hour, minutes, seconds));
    }


}
