package info.jbcs.minecraft.chisel.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemChiselRenderer implements IItemRenderer {
    RenderBlocks renderBlock = new RenderBlocks();
    RenderItem renderItem = new RenderItem();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        RenderHelper.enableGUIStandardItemLighting();

        renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, 0, 0);

        if (stack.stackTagCompound == null) return;

        ItemStack chiselTarget = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
        if (chiselTarget == null) return;

        GL11.glPushMatrix();
        GL11.glScalef(0.65f, 0.65f, 0.65f);
        GL11.glTranslatef(-8f, -8f, 0.0f);

        renderItem.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, chiselTarget, 8, 8);

        GL11.glPopMatrix();
    }

}
