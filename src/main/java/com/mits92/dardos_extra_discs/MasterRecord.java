package com.mits92.dardos_extra_discs;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;

public class MasterRecord {
    public static ArrayList<String> master_record = read_master_record();

    public static ArrayList<String> read_master_record() {
        File FUCKdir = new File(DedStart.LOCALDIR + "/resourcepacks/Dardos_Extra_Discs");
        FUCKdir.mkdir();

        File FUCKfile = new File(DedStart.LOCALDIR + "/resourcepacks/Dardos_Extra_Discs/MasterRecord.txt");
        try { FUCKfile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }

        File file = new File(DedStart.LOCALDIR + "/resourcepacks/Dardos_Extra_Discs/MasterRecord.txt");
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                result.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        master_record = result;
        return result;
    }

    public static void update(ArrayList<String> remove) throws IOException {
        FileWriter writer = new FileWriter(DedStart.LOCALDIR + "/resourcepacks/Dardos_Extra_Discs/MasterRecord.txt");
        for (String str : remove) {
            writer.write(str + '\n');
        }
        writer.close();
        master_record = read_master_record();
    }
}
