package model;

import model.enums.WeaponType;

import java.io.*;

public class ResultExporter {

    public static void exportResults(){
        new File("results").mkdir();

        //String path = "results/" +  Competition.getInstance().getCompetitionName();
        Competition.getInstance().getSingleWeaponCompetition(WeaponType.SMALL_SWORD).
        try {
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("results/kot"));
            writer.write("WYNIKI EPEE\n\n");
            writer.write("1\t100\tMalawski Filip\tSFA Kraków\n");
            writer.write("2\t99\tCzarny Grzegorz\tSFA Kraków\n");
            writer.write("10\t91\tSośniak Marcin\tSFA Kraków");
            writer.write("\n\n\n");
            writer.write("WYNIKI SABRE\n\n");
            writer.write("1\t100\tMalawski Filip\tSFA Kraków\n");
            writer.write("2\t99\tCzarny Grzegorz\tSFA Kraków\n");
            writer.write("\n\n\n");
            writer.write("WYNIKI RAPIER\n\n");
            writer.write("1\t100\tMalawski Filip\tSFA Kraków\n");
            writer.write("2\t99\tCzarny Grzegorz\tSFA Kraków\n");
            writer.write("\n\n\n");
            writer.write("WYNIKI ALL\n\n");
            writer.write("1\t100\tMalawski Filip\tSFA Kraków\n");
            writer.write("2\t99\tCzarny Grzegorz\tSFA Kraków\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
