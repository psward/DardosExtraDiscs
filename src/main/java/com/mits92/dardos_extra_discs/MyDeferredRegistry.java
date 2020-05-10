package com.mits92.dardos_extra_discs;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.function.Supplier;

public class MyDeferredRegistry<T extends IForgeRegistryEntry<T>>
{
    private final IForgeRegistry<T> type;
    private final String modid;
    public final Map<RegistryObject<T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    public final Set<RegistryObject<T>> entriesView = entries.keySet();

    public MyDeferredRegistry(IForgeRegistry<T> reg, String modid)
    {
        this.type = reg;
        this.modid = modid;
    }

    /**
     * Adds a new supplier to the list of entries to be registered, and returns a RegistryObject that will be populated with the created entry automatically.
     *
     * @param name The new entry's name, it will automatically have the modid prefixed.
     * @param sup A factory for the new entry, it should return a new instance every time it is called.
     * @return A RegistryObject that will be updated with when the entries in the registry change.
     */
    @SuppressWarnings("unchecked")
    public <I extends T> RegistryObject<I> register(final String name, final Supplier<? extends I> sup)
    {
        Objects.requireNonNull(name);
        Objects.requireNonNull(sup);
        ResourceLocation key = new ResourceLocation(modid, name);
        RegistryObject<I> ret = RegistryObject.of(key, this.type);
        if (entries.putIfAbsent((RegistryObject<T>) ret, () -> sup.get().setRegistryName(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }
        return ret;
    }

    /**
     * Adds our event handler to the specified event bus, this MUST be called in order for this class to function.
     * See the example usage.
     *
     * @param bus The Mod Specific event bus.
     */
    public void register(IEventBus bus)
    {
        bus.addListener(this::addEntries);
    }

    /**
     * @return The unmodifiable view of registered entries. Useful for bulk operations on all values.
     */
    public Collection<RegistryObject<T>> getEntries()
    {
        return entriesView;
    }

    private void addEntries(RegistryEvent.Register<?> event)
    {
        if (event.getGenericType() == this.type.getRegistrySuperType())
        {
            @SuppressWarnings("unchecked")
            IForgeRegistry<T> reg = (IForgeRegistry<T>)event.getRegistry();
            for (Map.Entry<RegistryObject<T>, Supplier<? extends T>> e : entries.entrySet())
            {
                reg.register(e.getValue().get());
                e.getKey().updateReference(reg);
            }
        }
    }
}