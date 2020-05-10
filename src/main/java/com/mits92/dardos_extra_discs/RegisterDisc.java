package com.mits92.dardos_extra_discs;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class RegisterDisc {
    public static MyDeferredRegistry<Item> ITEMS = new MyDeferredRegistry<>(ForgeRegistries.ITEMS, DedStart.MOD_ID);
    public static RegistryObject<DardoMusicDiscItem> disks = null;

    RegisterDisc() {
        ArrayList<String> files = MasterRecord.read_master_record();
        for (String title : files) {
            String clean_item_title = title.substring(0, title.length() - 4);
            String clean_sound_title = "music.record." + title.substring(0, title.length() - 4);

            final SoundEvent se = createEvent(clean_sound_title);

            disks = null;
            disks = ITEMS.register(clean_item_title, () -> new DardoMusicDiscItem(12, se, (new Item.Properties().maxStackSize(1))));
        }
    }

    private static SoundEvent createEvent(String name) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(DedStart.MOD_ID, name));
        sound.setRegistryName(new ResourceLocation(DedStart.MOD_ID, name));
        return sound;
    }

}
