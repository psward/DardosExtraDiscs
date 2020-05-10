package com.mits92.dardos_extra_discs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class RestartGui extends Screen {

    private static final int WIDTH = 193;
    private static final int HEIGHT = 107;


    private final ResourceLocation GUI = new ResourceLocation(DedStart.MOD_ID, "textures/gui/restartnew.png");

    public RestartGui() {
        super(new StringTextComponent("Would you like to restart to load in new sound files?"));
    }

    @Override
    protected void init() {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        addButton(new Button(relX + 16, relY + 32, 161, 20, "Restart now", button -> restart()));
        addButton(new Button(relX + 16, relY + 55, 161, 20, "Maybe later", button -> later()));
    }

    private void restart() {
        assert minecraft != null;
        minecraft.displayGuiScreen(null);
        minecraft.close();
    }

    private void later() {
        assert minecraft != null;
        minecraft.displayGuiScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(relX, relY, 0, 0, WIDTH, HEIGHT);
        super.render(mouseX, mouseY, partialTicks);
    }


    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new RestartGui());
    }
}