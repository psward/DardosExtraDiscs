package com.mits92.dardos_extra_discs;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.ArrayList;

import static com.mits92.dardos_extra_discs.DedStart.LOGGER;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class AutoReload {
    private static boolean reload = false;
    private static final GetAndLoadSounds load_sounds = new GetAndLoadSounds();

    @SubscribeEvent
    public static void reloadListener(GuiScreenEvent.MouseReleasedEvent.Pre event) throws IOException {
        try {
            if (event.getGui().getTitle().getString().equals("Select Resource Packs")) {
                reload = true;
            }
            if (reload && !event.getGui().getTitle().getString().equals("Select Resource Packs")) {
                reload = false;
                ArrayList<String> before = load_sounds.readFiles();
                ArrayList<String> after = MasterRecord.read_master_record();
                if (!before.containsAll(after) || !after.containsAll(before)) {
                    DedStart.loader();
                    RestartGui.open();
                }

            }
        } catch(NullPointerException | IOException | IllegalArgumentException e){
            LOGGER.info("LAME: " + e.getMessage());
        }
    }

}
