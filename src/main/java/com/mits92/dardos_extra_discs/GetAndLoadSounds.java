package com.mits92.dardos_extra_discs;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.mits92.dardos_extra_discs.DedStart.LOCALDIR;
import static com.mits92.dardos_extra_discs.DedStart.LOGGER;

public class GetAndLoadSounds {

    public ArrayList<String> readFiles() throws IOException {
        File file = new File(LOCALDIR + "/resourcepacks/Dardos_Extra_Discs/");
        String sound_directory = LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/sounds/record/";
        String texture_path = DedStart.LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/textures/item/";
        FileUtils.cleanDirectory(new File(sound_directory));
        FileUtils.cleanDirectory(new File(texture_path));

        File[] getFiles = file.listFiles();
        ArrayList<String> final_files = new ArrayList<>();
        for (File f : getFiles != null ? getFiles : new File[0]) {
            boolean check = f.getName().endsWith(".ogg");
            if (check) {
                String new_name = f.getName().substring(0, f.getName().length()-4)
                        .replaceAll("[^a-zA-Z0-9\\_]", "_").toLowerCase() + ".ogg";
                File sound_file = new File(sound_directory + new_name);
                copy_sound_files(f, sound_file);
                final_files.add(new_name);
            }
        }
        return final_files;
    }
    //TODO: JAVE2 audio conversion
    /*
    AudioAttributes audio = new AudioAttributes();
    audio.setBitRate(96000);
    audio.setChannels(1);

    EncodingAttributes attrs = new EncodingAttributes()
    attrs.setFormat("ogg");
    attrs.setAudioAttributes(audio);
    Encoder encoder = new Encoder();
    encoder.encoder(new MultimediaObject(source), target, attrs);
     */



    private static void copy_sound_files(File from, File to) {
        try {
            FileUtils.copyFile(from, to);
        } catch (IOException e) {
            String test = e.getMessage();
            LOGGER.info("SOUND FILES BROKEN: " + test);
        }
    }
}
