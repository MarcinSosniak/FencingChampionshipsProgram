package model.resultsOperations;

import model.Competition;
import model.Participant;
import model.enums.WeaponType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultExporter {

    public static void exportResults(){
        new File("results").mkdir();

        String path = "results/" +  Competition.getInstance().getCompetitionName() + ".txt";

        try {
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(path));
            writer.write("WYNIKI EPEE\n\n");
            writeSmallSwordResults(writer);
            writer.write("\n\n\n");
            writer.write("WYNIKI SABRE\n\n");
            writeSabreResults(writer);
            writer.write("\n\n\n");
            writer.write("WYNIKI RAPIER\n\n");
            writeRapierResults(writer);
            writer.write("\n\n\n");
            writer.write("WYNIKI ALL\n\n");
            writeAllResults(writer);
            writer.write("\n\n\n");
            writer.write("WYNIKI KOBIET\n\n");
            writeWomenTotalResults(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void writeSmallSwordResults(BufferedWriter writer){
        List<Participant> participantList =
                Competition.getInstance().getSingleWeaponCompetition(WeaponType.SMALL_SWORD).getParticipants();

        Collections.sort(participantList, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                return p1.getParticipantResult().getSmallSwordResults().getPlace().compareTo(
                        p2.getParticipantResult().getSmallSwordResults().getPlace());
            }
        });
        for (Participant p: participantList){
            try {
                writer.write(p.getParticipantResult().getSmallSwordResults().getPlace() + "\t" +
                        p.getParticipantResult().getSmallSwordResults().getPointsBasedOnPlace() + "\t" +
                        p.getName() + " " + p.getSurname() + "\t" +
                        p.getLocation() +  "\n"
                );
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private static void writeSabreResults(BufferedWriter writer){
        List<Participant> participantList =
                Competition.getInstance().getSingleWeaponCompetition(WeaponType.SABRE).getParticipants();

        Collections.sort(participantList, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                return p1.getParticipantResult().getSabreResults().getPlace().compareTo(
                        p2.getParticipantResult().getSabreResults().getPlace());
            }
        });
        for (Participant p: participantList){
            try {
                writer.write(p.getParticipantResult().getSabreResults().getPlace() + "\t" +
                        p.getParticipantResult().getSabreResults().getPointsBasedOnPlace() + "\t" +
                        p.getName() + " " + p.getSurname() + "\t" +
                        p.getLocation() +  "\n"
                );
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private static void writeRapierResults(BufferedWriter writer){
        List<Participant> participantList =
                Competition.getInstance().getSingleWeaponCompetition(WeaponType.RAPIER).getParticipants();

        Collections.sort(participantList, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                return p1.getParticipantResult().getRapierResults().getPlace().compareTo(
                        p2.getParticipantResult().getRapierResults().getPlace());
            }
        });
        for (Participant p: participantList){
            try {
                writer.write(p.getParticipantResult().getRapierResults().getPlace() + "\t" +
                        p.getParticipantResult().getRapierResults().getPointsBasedOnPlace() + "\t" +
                        p.getName() + " " + p.getSurname() + "\t" +
                        p.getLocation() +  "\n"
                );
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private static void writeAllResults(BufferedWriter writer){
        List<Participant> participantList =
                Competition.getInstance().getParticipants();

        Collections.sort(participantList, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                return p1.getParticipantResult().getTriathlonOpenPoints().compareTo(
                        p2.getParticipantResult().getTriathlonOpenPoints());
            }
        });
        Collections.reverse(participantList);
        for (Participant p: participantList){
            try {
                writer.write( p.getParticipantResult().getTriathlonOpen() + "\t" +
                        p.getParticipantResult().getTriathlonOpenPoints() + "\t" +
                        p.getName() + " " + p.getSurname() + "\t" +
                        p.getLocation() +  "\n"
                );
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private static void writeWomenTotalResults(BufferedWriter writer) {
        List<Participant> participantList =
                new LinkedList<>(
                        Competition.getInstance().getParticipants())
                        .stream().filter(it -> it.isfFemale()).collect(Collectors.toList()
                );
        Collections.sort(participantList, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                return p1.getParticipantResult().getTriathlonOpenPoints().compareTo(
                        p2.getParticipantResult().getTriathlonOpenPoints());
            }
        });
        Collections.reverse(participantList);
        for (Participant p: participantList){
            try {
                writer.write( p.getParticipantResult().getTriathlonOpen() + "\t" +
                        p.getParticipantResult().getTriathlonOpenPoints() + "\t" +
                        p.getName() + " " + p.getSurname() + "\t" +
                        p.getLocation() +  "\n"
                );
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

}
