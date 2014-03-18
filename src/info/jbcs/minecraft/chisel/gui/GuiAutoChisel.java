package info.jbcs.minecraft.chisel.gui;

import codechicken.lib.lang.LangUtil;
import info.jbcs.minecraft.chisel.tiles.TileEntityAutoChisel;
import info.jbcs.minecraft.utilities.GeneralClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAutoChisel extends GuiContainer {

    EntityPlayer player;
    ContainerAutoChisel container;

    public GuiAutoChisel(InventoryPlayer iinventory, TileEntityAutoChisel menu) {
        super(new ContainerAutoChisel(menu, iinventory));
        player = iinventory.player;
        height = 177;

        container = (ContainerAutoChisel) inventorySlots;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        inventorySlots.onContainerClosed(player);
    }

    boolean isExtended() {
        return false;
    }

    @Override
    public boolean isPointInRegion(int x, int y, int w, int h, int px, int py) {
        px -= this.guiLeft;
        py -= this.guiTop;

        return px >= x - 1 && px < x + w + 1 && py >= y - 1 && py < y + h + 1;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        String line = LangUtil.translateG("gui.autochisel.name");
        //		this.drawCenteredString(fontRenderer, isExtended()?"Carve":"Carve blocks",  88, 13, 0x888888);
        fontRenderer.drawString(line, (xSize - fontRenderer.getStringWidth(line)) / 2, -6, 0x404040);


        line = LangUtil.translateG("gui.autochisel.patterns");
        fontRenderer.drawString(line, 25, 34, 0x404040);
        line = LangUtil.translateG("gui.autochisel.input");
        fontRenderer.drawString(line, 48 - fontRenderer.getStringWidth(line), 14, 0x404040);
        line = LangUtil.translateG("gui.autochisel.output");
        ;
        fontRenderer.drawString(line, 129, 14, 0x404040);
        //fontRenderer.drawString(line, 5-fontRenderer.getStringWidth(line) / 2, 13, 0x404040);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - xSize >> 1;
        int j = height - ySize >> 1;

        String texture = "chisel:textures/autochisel-gui.png";

        GeneralClient.bind(texture);
        drawTexturedModalRect(i, j - 10, 0, 0, xSize + 10, ySize + 20);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);
    }

}
