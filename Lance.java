package knight37x.lance;

import java.util.Locale.Category;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
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

@Mod(modid = "lance", name = "Lances Mod", version = "2.3.0.164")
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
	// Lances:

	public static Item lanceOnIron;
	private int lanceOnIronID = 450;
	
	public static Item lanceUpIron;
	private int lanceUpIronID = 451;
	
	public static Item lanceOnDia;
	private int lanceOnDiaID = 452;
	
	public static Item lanceUpDia;
	private int lanceUpDiaID = 453;
	
	public static Item lanceOnCopper;
	private int lanceOnCopperID = 454;
	
	public static Item lanceUpCopper;
	private int lanceUpCopperID = 455;
	
	public static Item lanceOnSteel;
	private int lanceOnSteelID = 456;
	
	public static Item lanceUpSteel;
	private int lanceUpSteelID = 457;
	
	public static Item shaft;
	private int shaftID = 460;
	
	// Test Block:
	public static Block testBlock;
	private static int testBlockID = 249;
	private static boolean createTestBlock = false;
	
	//Other Configurations:
	public static boolean shouldLanceBreak = true;
	private int numberOfHits = 500;
	public static boolean shouldTakeDamageFromArmour = true;
	public static int armorBehaviour = 20;
	
	//-----------------------------------------------------------
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		this.packetHandler = new PacketHandler();
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		lanceOnIronID = config.get(Configuration.CATEGORY_ITEM, "Iron Lance ID", 450).getInt();
		lanceUpIronID = config.get(Configuration.CATEGORY_ITEM, "Iron Lance Up ID", 451).getInt();
		lanceOnDiaID = config.get(Configuration.CATEGORY_ITEM, "Diamond Lance ID", 452).getInt();
		lanceUpDiaID = config.get(Configuration.CATEGORY_ITEM, "Diamond Lance Up ID", 453).getInt();
		lanceOnCopperID = config.get(Configuration.CATEGORY_ITEM, "Copper Lance ID", 454).getInt();
		lanceUpCopperID = config.get(Configuration.CATEGORY_ITEM, "Copper Lance Up ID", 455).getInt();
		lanceOnSteelID = config.get(Configuration.CATEGORY_ITEM, "Steel Lance ID", 456).getInt();
		lanceUpSteelID = config.get(Configuration.CATEGORY_ITEM, "Steel Lance Up ID", 457).getInt();
		
		shaftID = config.get(Configuration.CATEGORY_ITEM, "Shaft ID", 460).getInt();
		
		createTestBlock = config.get(Configuration.CATEGORY_GENERAL, "Create Test Block (Don't use it)", false).getBoolean(false);
		testBlockID = config.get(Configuration.CATEGORY_GENERAL, "Test Block ID (Don't use)", 249).getInt();
		
		shouldLanceBreak = config.get(Configuration.CATEGORY_GENERAL, "Should the lance take damage?", true).getBoolean(true);
		shouldTakeDamageFromArmour = config.get(Configuration.CATEGORY_GENERAL, "Should the lance take more damage when hitting an armoured mob?", true).getBoolean(true);
		
		numberOfHits = config.get(Configuration.CATEGORY_GENERAL, "How often you can hit a mob until the lance will break (Iron Lance)", 500).getInt();
		armorBehaviour = config.get(Configuration.CATEGORY_GENERAL, "Armour rating to instantly break a lance = <settable to any value between 0 and 10 with increments of 0.5>", 5).getInt() * 2;
		
		if(armorBehaviour > 20) {
			armorBehaviour = 20;
		} else if(armorBehaviour < 0) {
			armorBehaviour = 0;
		}
		
		config.save();
	}

	@Init
	public void load(FMLInitializationEvent event) {
		
		lanceOnIron = new ItemLance(lanceOnIronID, 7, "Iron").setUnlocalizedName("lanceI").setMaxStackSize(1).setMaxDamage(numberOfHits);
		lanceUpIron = new ItemLanceUp(lanceUpIronID, Lance.lanceOnIron, "Iron").setUnlocalizedName("lanceUpI").setMaxStackSize(1).setMaxDamage(numberOfHits);
		lanceOnDia = new ItemLance(lanceOnDiaID, 10, "Dia").setUnlocalizedName("lanceD").setMaxStackSize(1).setMaxDamage(numberOfHits * 6);
		lanceUpDia = new ItemLanceUp(lanceUpDiaID, Lance.lanceOnDia, "Dia").setUnlocalizedName("lanceUpD").setMaxStackSize(1).setMaxDamage(numberOfHits * 6);
		
		((ItemLance) lanceOnIron).setSwitchTo(Lance.lanceUpIron);
		((ItemLance) lanceOnDia).setSwitchTo(Lance.lanceUpDia);
		
		shaft = new ItemShaft(shaftID).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("shaft");
		if(this.createTestBlock) {
			testBlock = new TestBlock(testBlockID).setCreativeTab(CreativeTabs.tabBlock).setUnlocalizedName("testBlock");
		}
		
		registerRecipes();
		registerItems();
		registerLanguage();
		
		proxy.registerRenderers();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
		if(this.isAvailable("ingotCopper")) {
			lanceOnCopper = new ItemLance(lanceOnCopperID, 4, "Copper").setUnlocalizedName("lanceC").setMaxStackSize(1).setMaxDamage((numberOfHits / 5) * 4);
			lanceUpCopper = new ItemLanceUp(lanceUpCopperID, Lance.lanceOnCopper, "Copper").setUnlocalizedName("lanceUpC").setMaxStackSize(1).setMaxDamage((numberOfHits / 5) * 4);
			
			((ItemLance) lanceOnCopper).setSwitchTo(Lance.lanceUpCopper);
			
			GameRegistry.addRecipe(new ShapedOreRecipe(lanceUpCopper, "  X", " # ", "#  ", '#', shaft, 'X', "ingotCopper"));
			
			GameRegistry.registerItem(lanceOnCopper, "lanceC");
			GameRegistry.registerItem(lanceUpCopper, "lanceUpC");
			
			LanguageRegistry.addName(lanceOnCopper, "Copper Lance");
			LanguageRegistry.addName(lanceUpCopper, "Copper Lance");

			proxy.registerCopper();
		}
		
		
		
		if(this.isAvailable("ingotSteel")) {
			lanceOnSteel = new ItemLance(lanceOnSteelID, 6, "Steel").setUnlocalizedName("lanceS").setMaxStackSize(1).setMaxDamage(numberOfHits * 2);
			lanceUpSteel = new ItemLanceUp(lanceUpSteelID, Lance.lanceOnSteel, "Steel").setUnlocalizedName("lanceUpS").setMaxStackSize(1).setMaxDamage(numberOfHits * 2);
			
			((ItemLance) lanceOnSteel).setSwitchTo(Lance.lanceUpSteel);
			
			GameRegistry.addRecipe(new ShapedOreRecipe(lanceUpSteel, "  X", " # ", "#  ", '#', shaft, 'X', "ingotSteel"));
			
			GameRegistry.registerItem(lanceOnSteel, "lanceS");
			GameRegistry.registerItem(lanceUpSteel, "lanceUpS");
			
			LanguageRegistry.addName(lanceOnSteel, "Steel Lance");
			LanguageRegistry.addName(lanceUpSteel, "Steel Lance");

			proxy.registerSteel();
		}
	}
	
	private void registerRecipes()
	{
		GameRegistry.addShapedRecipe(new ItemStack(shaft, 1), "#  ", " # ", "  #", '#', Item.stick);
		GameRegistry.addRecipe(new ShapedOreRecipe(lanceUpIron, "  X", " # ", "#  ", '#', shaft, 'X', Item.ingotIron));
		GameRegistry.addRecipe(new ShapedOreRecipe(lanceUpDia, "  X", " # ", "#  ", '#', shaft, 'X', Item.diamond));
	}
	
	private void registerItems()
	{
		GameRegistry.registerItem(lanceOnIron, "lanceI");
		GameRegistry.registerItem(lanceUpIron, "lanceUpI");
		GameRegistry.registerItem(lanceOnDia, "lanceD");
		GameRegistry.registerItem(lanceUpDia, "lanceUpD");
		
		GameRegistry.registerItem(shaft, "shaft");
		
		// Test Block:
		if(this.createTestBlock) {
			GameRegistry.registerBlock(testBlock, "testBlock");
		}
	}
	
	private void registerLanguage()
	{
		LanguageRegistry.addName(lanceOnIron, "Iron Lance");
		LanguageRegistry.addName(lanceUpIron, "Iron Lance");
		LanguageRegistry.addName(lanceOnDia, "Diamond Lance");
		LanguageRegistry.addName(lanceUpDia, "Diamond Lance");
		
		LanguageRegistry.addName(shaft, "Lance Shaft");
		
		// Test Block:
		if(this.createTestBlock) {
			LanguageRegistry.addName(testBlock, "Test Block");
		}
	}
	
	private boolean isAvailable(String item) {
		String[] names = OreDictionary.getOreNames();
		for(int i = 0; i < names.length; i++) {
			if(names[i].equals(item)) {
				return true;
			}
		}
		return false;
	}
}