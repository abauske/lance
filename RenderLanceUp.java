package knight37x.lance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderLanceUp implements IItemRenderer {
	protected ModelLanceUp model = new ModelLanceUp();
	private ResourceLocation texture = new ResourceLocation("textures/models/mod/modelLanceUpIron.png");
	
	public RenderLanceUp(String location) {
		this.texture = new ResourceLocation(location);
	}
	
	public boolean handleRenderType(ItemStack var1, ItemRenderType type) {
		switch (type) {
		case INVENTORY:
			return false;
		case ENTITY:
			return false;

		default:
			return true;
		}
	}

	public boolean shouldUseRenderHelper(ItemRenderType var1, ItemStack var2,
			ItemRendererHelper var3) {
		return false;
	}

	public void renderItem(ItemRenderType var1, ItemStack var2, Object... var3) {
		switch (1) {
		case 1:
			GL11.glPushMatrix();
			Minecraft.getMinecraft().renderEngine.func_110577_a(texture);
			boolean var4 = false;

			if (var3[1] != null && var3[1] instanceof EntityPlayer) {
				float var5;

				if ((EntityPlayer) var3[1] == Minecraft.getMinecraft().renderViewEntity && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) && !(Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) || RenderManager.instance.playerViewY != 180.0F)) {
					var4 = true;
					var5 = 0.7F;

					GL11.glTranslatef(1.5F, 0.0F, 1.0F);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glRotatef(0.0F + 180, 0.0F + 0F, 0.0F + 0F, 0.0F + 1F);
					GL11.glRotatef(0.0F + 100F, 0.0F, 0.0F + 1F, 0.0F);
					GL11.glRotatef(0.0F + 0, 0.0F + 1F, 0.0F + 0, 0.0F + 0F);
				} else {
					var5 = 0.3F;
					
					GL11.glTranslatef(0.4F, 0.3F, -0.2F);
					GL11.glScalef(1.0F, 1.0F, 1.0F);
					GL11.glRotatef(0.0F + 195, 0.0F + 0F, 0.0F + 0F, 0.0F + 1F);
					GL11.glRotatef(0.0F + 0F, 0.0F, 0.0F + 1F, 0.0F);
					GL11.glRotatef(0.0F + 30, 0.0F + 1F, 0.0F + 0, 0.0F + 0F);
				}
			}

			this.model.render((Entity) var3[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
					0.0625F);
			GL11.glPopMatrix();

		default:
		}
	}
}