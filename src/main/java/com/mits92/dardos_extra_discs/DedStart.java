package com.mits92.dardos_extra_discs;

import static com.mits92.dardos_extra_discs.DedStart.MOD_ID;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

@Mod(MOD_ID)
public class DedStart {
    public static final String MOD_ID = "dardos_extra_discs";
    public static final String LOCALDIR = System.getProperty("user.dir");
    public static final Logger LOGGER = LogManager.getLogger();
    private static final GetAndLoadSounds load_sounds = new GetAndLoadSounds();

    public DedStart() {
        RegisterDisc rdi = new RegisterDisc();
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<FMLClientSetupEvent>) event -> {
            try {
                onClientStarting(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        RegisterDisc.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onClientStarting(FMLClientSetupEvent event) throws IOException {
        loader();
    }

    public static void loader() throws IOException {
        ArrayList<String> sound_files = load_sounds.readFiles();
        ArrayList<String> master_record = MasterRecord.master_record;
        master_record.addAll(sound_files);
        List<String> need_to_be_removed = new ArrayList<>(master_record);
        need_to_be_removed.removeAll(sound_files);
        master_record.removeAll(need_to_be_removed);
        removeDuplicates(master_record);
        MasterRecord.update(master_record);
        ResourceGenerator.generate_resources(master_record);
    }

    private static <T> void removeDuplicates(ArrayList<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
    }
}
