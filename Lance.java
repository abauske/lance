package knight37x.lance;

import java.util.Locale.Category;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import knight37x.lance.proxies.LanceClientProxy;
import knight37x.lance.proxies.LanceCommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "lance", name = "Lancess Mod", version = "2.2.162")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"lance", "lanceHitEntity", "lanceHitValue", "lanceIsForward"}, packetHandler = PacketHandler.class)
public class Lance {
	public PacketHandler packetHandler;
	
	@Instance("lance")
	public static Lance instance = new Lance();
//	public GuiHandler guihandler = new GuiHandler();
	
	@SidedProxy(clientSide="knight37x.lance.proxies.LanceClientProxy", serverSide="knight37x.lance.proxies.LanceCommonProxy")
	public static LanceCommonProxy proxy;
//	public static LanceClientProxy cProxy = new LanceClientProxy();
	
	//-----------------------------------------------------------
	// All Variables:
	// Lance:
//	public static Item lance1;
//	private int lance1ID = 452;

	public static Item lanceOn;
	private int lanceOnID = 450;
	
	public static Item lanceUp;
	private int lanceUpID = 451;
	
	public static Item shaft;
	private int shaftID = 460;
	// Test Block:
	public static Block testBlock;
	private static int testBlockID = 249;
	private static boolean createTestBlock = false;
	
	//-----------------------------------------------------------
	
	@Mod.EventHandler
	public void EventHandler(FMLPreInitializationEvent event) {
		this.packetHandler = new PacketHandler();
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
//		lance1ID = config.get(Configuration.CATEGORY_ITEM, "Lance ID", 452).getInt();
		lanceOnID = config.get(Configuration.CATEGORY_ITEM, "Lance ID", 450).getInt();
		lanceUpID = config.get(Configuration.CATEGORY_ITEM, "Lance Up ID", 451).getInt();
		shaftID = config.get(Configuration.CATEGORY_ITEM, "Shaft ID", 460).getInt();
		
		createTestBlock = config.get(Configuration.CATEGORY_GENERAL, "Create Test Block (Don't use it)", false).getBoolean(false);
		testBlockID = config.get(Configuration.CATEGORY_GENERAL, "Test Block ID (Don't use)", 249).getInt();
		
		config.save();
	}

	@Mod.EventHandler
	public void EventHandler(FMLInitializationEvent event) {
		
//		lance1 = new ItemLance1(lance1ID).setCreativeTab(CreativeTabs.tabCombat).setUnlocalizedName("lance").setMaxStackSize(1).setMaxDamage(500);
		lanceOn = new ItemLance(lanceOnID, EnumToolMaterial.IRON).setUnlocalizedName("lance").setMaxStackSize(1).setMaxDamage(500).setCreativeTab(null);
//		lanceUp = new ItemLance(lanceUpID, EnumToolMaterial.IRON).setUnlocalizedName("lance").setMaxStackSize(1).setMaxDamage(500);
		lanceUp = new ItemLanceUp(lanceUpID, EnumToolMaterial.IRON).setUnlocalizedName("lanceUp").setMaxStackSize(1).setMaxDamage(500);
		shaft = new ItemShaft(shaftID).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("shaft");
		if(this.createTestBlock) {
			testBlock = new TestBlock(testBlockID).setCreativeTab(CreativeTabs.tabBlock).setUnlocalizedName("testBlock");
		}
		
//		EntityRegistry.registerModEntity(EntityLance.class, "Lance", 1, this, 0, 20, true);
		
		registerRecipes();
		registerItems();
		registerLanguage();
		
//		cProxy.registerRenderers();
		proxy.registerRenderers();
	}

	@Mod.EventHandler
	public static void EventHandler(FMLPostInitializationEvent event) {
		
	}
	
	private void registerRecipes()
	{
		GameRegistry.addShapedRecipe(new ItemStack(shaft, 1), "#  ", " # ", "  #", '#', Item.stick);
		GameRegistry.addRecipe(new ItemStack(lanceUp, 1), "  X", " # ", "#  ", '#', shaft, 'X', Item.ingotIron);
	}
	
	private void registerItems()
	{
//		GameRegistry.registerItem(lance1, "lance1");
		GameRegistry.registerItem(lanceOn, "lance");
		GameRegistry.registerItem(lanceUp, "lanceUp");
		GameRegistry.registerItem(shaft, "shaft");
		
		// Test Block:
		if(this.createTestBlock) {
			GameRegistry.registerBlock(testBlock, "testBlock");
		}
	}
	
	private void registerLanguage()
	{
//		LanguageRegistry.addName(lance1, "Lance");
		LanguageRegistry.addName(lanceOn, "Lance");
		LanguageRegistry.addName(lanceUp, "Lance");
		LanguageRegistry.addName(shaft, "Lance Shaft");
		
		// Test Block:
		if(this.createTestBlock) {
			LanguageRegistry.addName(testBlock, "Test Block");
		}
	}
}