package knight37x.lance.proxies;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import knight37x.lance.Lance;
import knight37x.lance.RenderLance;
import knight37x.lance.RenderLanceUp;
import knight37x.lance.proxies.*;

public class LanceClientProxy extends LanceCommonProxy {
	
	@Override
	public void registerRenderers() {
//		MinecraftForgeClient.registerItemRenderer(Lance.lance1.itemID, (IItemRenderer) new RenderLance());
		MinecraftForgeClient.registerItemRenderer(Lance.lanceOn.itemID, (IItemRenderer) new RenderLance());
		MinecraftForgeClient.registerItemRenderer(Lance.lanceUp.itemID, (IItemRenderer) new RenderLanceUp());
    }
}
