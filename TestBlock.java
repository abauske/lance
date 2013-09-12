package knight37x.lance;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TestBlock extends BlockStone{

	public TestBlock(int par1) {
		super(par1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("");
	}
	
	@Override
	public boolean onBlockActivated(World world, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if(!world.isDaytime())
		{
			world.setWorldTime(0);
		}
		if(world.prevRainingStrength != 0)
		{
			world.toggleRain();
		}
		return true;
	}
	
	

}
