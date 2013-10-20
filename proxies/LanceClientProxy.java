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
		MinecraftForgeClient.registerItemRenderer(Lance.lanceOnIron.itemID, (IItemRenderer) new RenderLance("textures/models/mod/modelLanceUpIron.png"));
		MinecraftForgeClient.registerItemRenderer(Lance.lanceUpIron.itemID, (IItemRenderer) new RenderLanceUp("textures/models/mod/modelLanceUpIron.png"));
		MinecraftForgeClient.registerItemRenderer(Lance.lanceOnDia.itemID, (IItemRenderer) new RenderLance("textures/models/mod/modelLanceUpDia.png"));
		MinecraftForgeClient.registerItemRenderer(Lance.lanceUpDia.itemID, (IItemRenderer) new RenderLanceUp("textures/models/mod/modelLanceUpDia.png"));
    }
	
	@Override
	public void registerCopper() {
		MinecraftForgeClient.registerItemRenderer(Lance.lanceOnCopper.itemID, (IItemRenderer) new RenderLance("textures/models/mod/modelLanceUpCopper.png"));
		MinecraftForgeClient.registerItemRenderer(Lance.lanceUpCopper.itemID, (IItemRenderer) new RenderLanceUp("textures/models/mod/modelLanceUpCopper.png"));
	}
	
	@Override
	public void registerSteel() {
		MinecraftForgeClient.registerItemRenderer(Lance.lanceOnSteel.itemID, (IItemRenderer) new RenderLance("textures/models/mod/modelLanceUpSteel.png"));
		MinecraftForgeClient.registerItemRenderer(Lance.lanceUpSteel.itemID, (IItemRenderer) new RenderLanceUp("textures/models/mod/modelLanceUpSteel.png"));
	}
}
