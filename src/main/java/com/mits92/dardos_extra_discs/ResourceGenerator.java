package com.mits92.dardos_extra_discs;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static com.mits92.dardos_extra_discs.DedStart.*;

public class ResourceGenerator {
    public static void generate_resources(ArrayList<String> titles) throws IOException {
        generate_jsons(titles);
        generate_image_textures(titles);
    }

    private static void generate_jsons(ArrayList<String> titles) throws IOException {
        //----------------------
        // Open file connections
        //----------------------
        File lang_json = new File(LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/lang/en_us.json");
        String model_file = LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/models/item/";
        File music_discs_tag_json = new File(LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/data/minecraft/tags/items/music_discs.json");

        File loot_inject_json = new File(LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/data/dardos_extra_discs/loot_tables/inject/loot_inject.json");
        File sounds_json = new File(LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/sounds.json");
        File model_directory = new File(model_file);
        FileUtils.cleanDirectory(model_directory);

        //-------------------------
        // Generate json structures
        //-------------------------
        Map<String, Object> en_us_map = new HashMap<>();
        Map<String, Object> music_discs_tag_map = new HashMap<>();

        String sounds_json_str = "";
        ArrayList<String> identifiers = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            String clean_title = titles.get(i).substring(0, titles.get(i).length() - 4);
            identifiers.add(MOD_ID+":"+clean_title);

            //Model
            Map<String, Object> model_file_map = new HashMap<>();
            Map<String, Object> model_file_map_nested = new HashMap<>();
            File model_file_json = new File(model_file + clean_title + ".json");
            model_file_map.put("parent", "item/generated");
            model_file_map_nested.put("layer0", MOD_ID + ":item/" + clean_title);
            model_file_map.put("textures", model_file_map_nested);
            Writer model_file_writer = new FileWriter(model_file_json);
            new Gson().toJson(model_file_map, model_file_writer);
            model_file_writer.close();

            en_us_map.put("item." + MOD_ID + "." + clean_title, "\u00a7bMusic Disc");
            en_us_map.put("item." + MOD_ID + "." + clean_title + ".desc", clean_title);

            //Sounds
            String s = "";
            if (i == 0) s += "{";
            s += "  \"music.record." + clean_title + "\": {\n" +
                    "    \"category\": \"record\",\n" +
                    "    \"sounds\": [\n" +
                    "      {\n" +
                    "        \"name\": \"" + MOD_ID + ":record/"+ clean_title +"\",\n" +
                    "        \"stream\": true\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }";
            if (i == titles.size() - 1) s += "}";
            else s += ",";

            sounds_json_str += s;
            model_file_map.put("parent", "item/generated");
            model_file_map_nested.put("layer0", MOD_ID + ":item/" + clean_title);
            model_file_map.put("textures", model_file_map_nested);
        }

        String loot_inject_str = "{\n" +
                "  \"pools\": [\n" +
                "    {\n" +
                "      \"name\": \"main\",\n" +
                "      \"rolls\": 1,\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"type\": \"minecraft:tag\",\n" +
                "          \"name\": \"minecraft:music_discs\",\n" +
                "          \"expand\": true,\n" +
                "          \"weight\":" + 1.0/titles.size() +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"empty\",\n" +
                "          \"weight\": " + 4 + "\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        en_us_map.put("itemGroup." + MOD_ID, "Dardo's Extra Discs");

        Gson gson = new GsonBuilder().create();
        music_discs_tag_map.put("replace",false);
        music_discs_tag_map.put("values",gson.toJsonTree(identifiers).getAsJsonArray());

        //------------
        // Write jsons
        //------------
        //Parses lang hash map
        Writer en_us_writer = new FileWriter(lang_json);
        new Gson().toJson(en_us_map, en_us_writer);
        en_us_writer.close();

        //Parses music disc tags hash map
        Writer music_discs_tag_writer = new FileWriter(music_discs_tag_json);
        new Gson().toJson(music_discs_tag_map, music_discs_tag_writer);
        music_discs_tag_writer.close();

        //Parses sounds.json
        JsonObject sounds_parser = new JsonParser().parse(sounds_json_str).getAsJsonObject();
        Writer sounds_json_writer = new FileWriter(sounds_json);
        new Gson().toJson(sounds_parser, sounds_json_writer);
        sounds_json_writer.close();

        //Parses loot_inject.json
        JsonObject loot_parser = new JsonParser().parse(loot_inject_str).getAsJsonObject();
        Writer loot_json_writer = new FileWriter(loot_inject_json);
        new Gson().toJson(loot_parser, loot_json_writer);
        loot_json_writer.close();
    }

    private static void generate_image_textures(ArrayList<String> titles) {
        String template_path = LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/textures/templates/";
        String texture_path = LOCALDIR.substring(0, LOCALDIR.length() - 3)
                + "src/main/resources/assets/dardos_extra_discs/textures/item/";
        File texture_directory = new File(texture_path);
        try {
            FileUtils.cleanDirectory(texture_directory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String title : titles) {
            //TODO MAKE THIS RANDOM
            BufferedImage base = null;
            try {
                base = ImageIO.read(new File(template_path + "base1.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage sticker = null;
            try {
                sticker = ImageIO.read(new File(template_path + "sticker1.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage substicker = null;
            try {
                substicker = ImageIO.read(new File(template_path + "substicker1.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int w = 16;
            int h = 16;
            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = combined.getGraphics();
            g.drawImage(base, 0, 0, null);
            g.drawImage(sticker, 0, 0, null);
            g.drawImage(substicker, 0, 0, null);
            g.dispose();
            String clean_title = title.substring(0, title.length() - 4);
            File out = new File(texture_path + clean_title + ".png");
            try {
                ImageIO.write(combined, "png", out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //TODO: Algorithm for texturing discs by name/size
            /*
            2 bases: Big and small. Assume size in bytes = B. B/96000 < 120s:
                >Use base2.png

            Coloring: base color by artist (stuff before the dash)
                      sticker by track (stuff after the dash)
                      substickers added for variation with no specific rule

                      If no dash, fully random

            Fuck j a v a
             */
        }
    }
}
