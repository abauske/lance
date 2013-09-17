package knight37x.lance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemShaft extends Item {

	public ItemShaft(int par1) {
		super(par1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg) {
		this.itemIcon = reg.registerIcon("Lance:lanceShaft");
	}

}
