package knight37x.lance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.ServerAboutToStart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemLance extends ItemSword {

	private int counter = 0;
	private int counter2 = 0;
	
	private Minecraft minecraft;
	
	private Entity pointedEntity;
	private EntityLiving pointedEntityLiving;
	private MovingObjectPosition objectMouseOver;
	public float knockTime = 0.0F;
	private boolean lastTickMouseButton0 = false;
	private float hit = 0.0F;
	private int knockCounter = 0;
	
	private int countedTicks = 0;
	private boolean lastTickMouseButton1 = false;
	
	private int leftClickCounter = 0;
	private final String material;
	
	private int strengh;
	
	private Item switchTo;
	
	public ItemLance(int par1, int strengh, String material) {
		super(par1, EnumToolMaterial.IRON);
		setCreativeTab(null);
		this.strengh = strengh;
		this.material = material;
	}
	
	public void setSwitchTo(Item switchTo) {

		this.switchTo = switchTo;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg) {
		this.minecraft = Minecraft.getMinecraft();
		this.itemIcon = reg.registerIcon("Lance:lance" + this.material);
	}
	
	@SideOnly(Side.CLIENT)

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return false;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }

	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity entity, int par4, boolean par5) {
		if(!this.isRunningOnClient() && this.counter2 >= 10/*  && PacketHandler.hit != 0.0F*/) {
			this.hit = (float) PacketHandler.hit;
			System.out.println(hit);
			PacketHandler.hit = 0.0F;
			if(this.hit != 0.0F) {
				this.counter2 = 0;
			}
		}
		if(entity != null && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			ItemStack canPlayerMove = player.getCurrentEquippedItem();
			ItemStack canPlayerMove1 = player.getHeldItem();
			if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == this.itemID) {
				if (this.isRunningOnClient() ? this.getMouseOver(1.0F) != null : true) {
					Entity aim = this.isRunningOnClient() ? this.getMouseOver(1.0F) : null;
					aim = this.getRightEntity(world, this.isRunningOnClient() ? aim.entityId : PacketHandler.entityID);
					if (!this.isRunningOnClient()) {
						PacketHandler.entityID = 0;
					}
					if (aim instanceof EntityLiving && player.getDistanceToEntity(aim) <= 12 && !aim.isDead) {
						boolean attacked = this.attack((EntityLiving) aim, (EntityPlayer) entity);
						if (attacked && player.getCurrentEquippedItem() != null) {
							this.setDamage(player.getCurrentEquippedItem(), player.getCurrentEquippedItem().getItemDamage() + 1);
						}
						int armor = ((EntityLiving) aim).getTotalArmorValue();
						if (attacked && Lance.shouldTakeDamageFromArmour && this.counter >= 10) {
							this.counter = 0;
							if(armor > 0) {
								this.setDamage(player.getCurrentEquippedItem(), player.getCurrentEquippedItem().getItemDamage() + (int) ((100 / (11 - (armor / 2))) / 10) * Lance.armorBehaviour);
							} else {
								this.setDamage(player.getCurrentEquippedItem(), player.getCurrentEquippedItem().getItemDamage() + 1);
							}
						}
//						ItemStack armor = ((EntityLiving) aim).getCurrentItemOrArmor(3);
//						if (attacked && armor != null && Lance.shouldTakeDamageFromArmour) {
//							ItemArmor chestplate = (ItemArmor) armor.getItem();
//							int protection = chestplate.getArmorMaterial().getDamageReductionAmount(1);
//							if (protection >= 6 && protection < 8) {
//								if (player.getCurrentEquippedItem() != null) {
//									this.setDamage(player.getCurrentEquippedItem(), player.getCurrentEquippedItem().getItemDamage() + (int) (this.getMaxDamage() / 3));
//								}
//							}
//							if (protection >= 8) {
//								this.destroy(player);
//							}
//						}
					}
				}
				if(player.getCurrentEquippedItem() != null) {
					if(((EntityPlayer) entity).getCurrentEquippedItem().getItemDamage() >= ((EntityPlayer) entity).getCurrentEquippedItem().getMaxDamage()) {
						this.destroy(player);
					}
				}
			}

			if(this.minecraft != null) {
				this.hit = 0.0F;
//				boolean isButton0Down = Mouse.isButtonDown(0);
				boolean isButton0Down = this.minecraft.gameSettings.keyBindAttack.pressed;
				if(isButton0Down && knockTime < 1) {
					this.knockTime += 0.03F;
				} else if(!isButton0Down && this.lastTickMouseButton0) {
					this.knockTime = -knockTime;
					this.hit = Math.abs(knockTime);
					System.out.println(this.hit);
					this.knockCounter = 20;
				} else if (knockTime < 0 && this.knockCounter <= 0) {
					this.knockTime = 0.0F;
//					this.hit = true;
					this.knockCounter = 0;
				}
				if(this.knockCounter > 0) {
					this.knockCounter--;
				}
				if(this.knockTime < 0) {
					this.knockTime += 0.1F;
				}
				this.lastTickMouseButton0 = isButton0Down;
			}
			if(this.isRunningOnClient()) {
				if(hit != 0) {
					this.sendHitValue(hit, (EntityClientPlayerMP) player);
				}
//				else {
//					this.sendHitValue(0, (EntityClientPlayerMP) player);
//				}
			}
		}
		if(this.leftClickCounter > 0) {
			this.leftClickCounter--;
		}
		
		this.counter++;
		if(this.counter > 9000) {
			this.counter = 20;
		}
		
		this.counter2++;
		if(this.counter2 > 9000) {
			this.counter2 = 40;
		}
	}
	
	public Entity getMouseOver(float par1)
    {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityLivingBase entityRender = Minecraft.getMinecraft().renderViewEntity;
        if (entityRender != null)
        {
            if (Minecraft.getMinecraft().theWorld != null)
            {
                this.pointedEntityLiving = null;
                double d0 = (double)Minecraft.getMinecraft().playerController.getBlockReachDistance();
                this.objectMouseOver = entityRender.rayTrace(d0, par1);
                double d1 = d0;
                Vec3 vec3 = Minecraft.getMinecraft().renderViewEntity.getPosition(par1);
                
                boolean test = true;
                if (Minecraft.getMinecraft().playerController.extendedReach() || test)
                {
                    d0 = 7.0D;
                    d1 = 7.0D;
                }
                else
                {
                    if (d0 > 3.0D)
                    {
                        d1 = 3.0D;
                    }

                    d0 = d1;
                }

                if (this.objectMouseOver != null)
                {
                    d1 = this.objectMouseOver.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = Minecraft.getMinecraft().renderViewEntity.getLook(par1);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                this.pointedEntity = null;
                float f1 = 20.0F;
                List list = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(Minecraft.getMinecraft().renderViewEntity, Minecraft.getMinecraft().renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f1, (double)f1, (double)f1));
                double d2 = d1;

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = (Entity)list.get(i);

                    if (entity.canBeCollidedWith())
                    {
                        float f2 = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                        
                        if (axisalignedbb.isVecInside(vec3))
                        {
                            if (0.0D < d2 || d2 == 0.0D)
                            {
                                this.pointedEntity = entity;
                                d2 = 0.0D;
                            }
                        }
                        else if (movingobjectposition != null)
                        {
                            double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D || d3 >= 20.0d)
                            {
                                if (entity == Minecraft.getMinecraft().renderViewEntity.ridingEntity)
                                {
                                    if (d2 == 0.0D)
                                    {
                                        this.pointedEntity = entity;
                                    }
                                }
                                else
                                {
                                    this.pointedEntity = entity;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (this.pointedEntity != null && (d2 < d1 || Minecraft.getMinecraft().objectMouseOver == null))
                {
                    this.objectMouseOver = new MovingObjectPosition(this.pointedEntity);

                    if (this.pointedEntity instanceof EntityLiving)
                    {
                        this.pointedEntityLiving = (EntityLiving)this.pointedEntity;
                    }
                }
            }
        }
        if(this.pointedEntity != null) {
        	this.sendID(this.pointedEntity.entityId, (EntityClientPlayerMP) Minecraft.getMinecraft().renderViewEntity);
        }
        return this.pointedEntity;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World par2World, EntityPlayer par3EntityPlayer) {
//		if(this.leftClickCounter <= 0) {
//			if(this.standby) {
//				this.standby = false;
//			} else {
//				this.standby = true;
//			}
//			this.leftClickCounter = 6;
//		}
		if(this.switchTo != null) {
			ItemStack newLance = new ItemStack(this.switchTo, 1);
			newLance.setItemDamage(itemstack.getItemDamage());
			return newLance;
		}
		return itemstack;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return true;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		return true;
	}

	private void knockBack(EntityLiving entity, EntityPlayer player) {
		int speed;
		if(player.isSprinting()) {
			speed = 10;
		} else if(player.isRiding()) {
			Entity ridingEntity = player.ridingEntity;
			/*if(ridingEntity instanceof EntityHorse) {
				speed = 20;
			} else */if(ridingEntity instanceof EntityPig) {
				speed = 5;
			} else {
				speed = 2;
			}
		} else if(player.isSneaking()) {
			speed = 0;
		}  else {
			speed = 2;
		}
		
		double d0 = player.posX - entity.posX;
        double d1;

        for (d1 = player.posZ - entity.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
        {
            d0 = (Math.random() - Math.random()) * 0.01D;
        }

        entity.attackedAtYaw = (float)(Math.atan2(d1, d0) * 180.0D / Math.PI) - entity.rotationYaw;
        for(int i = 0; i < speed; i++) {
        	entity.knockBack(player, speed, d0, d1);
        }
	}

	private boolean attack(EntityLiving entity, EntityPlayer player) {
		boolean isForwardKeyPressed;
		if(this.isRunningOnClient()) {
			isForwardKeyPressed = Minecraft.getMinecraft().gameSettings.keyBindForward.pressed;
			this.sendIsForwardKeyPressed(isForwardKeyPressed, (EntityClientPlayerMP) player);
		} else {
			isForwardKeyPressed = PacketHandler.isForwardKeyPressed;
			PacketHandler.isForwardKeyPressed = false;
		}
		float hit = Math.abs(this.hit) * 4;
		if(hit != 0 || player.getDistanceToEntity(entity) <= 6) {
			float hurt = 0;
			if(player.isSprinting()) {
				if(isForwardKeyPressed) {
					hurt += 3F;
				}
				hurt *= 1.2F;
			} else if(player.isRiding()) {
				Entity ridingEntity = player.ridingEntity;
				/*if(ridingEntity instanceof EntityHorse) {
					if(isForwardKeyPressed) {
						hurt += 10F;
					}
					hurt *= 2F;
				} else */if(ridingEntity instanceof EntityPig) {
					if(isForwardKeyPressed) {
						hurt += 1F;
					}
					hurt *= 1.1;
				} else if(ridingEntity != null) {
					hurt += this.getSpeed((EntityLiving) ridingEntity);
				} else if(isForwardKeyPressed) {
					hurt += 1F;
				}
			} else if(player.isSneaking()) {
				hurt *= 0.2F;
			} else if(isForwardKeyPressed) {
				hurt += 1F;
			}
			hurt += hit;
			hurt /= 10;
			hurt *= this.strengh;
			if(hurt != 0) {
				entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (int) hurt);
				if(!player.capabilities.isCreativeMode && Lance.shouldLanceBreak) {
					return true;
				}
			}
		}
		return false;
//		this.knockBack(entity, player);
	}
	
 	private double getSpeed(EntityLiving entity) {
 //		double dist = entity.getAIMoveSpeed();
 //		return dist;
		double distPerSekond = entity.getDistance(entity.prevPosX, entity.prevPosY, entity.prevPosZ) * 35;
 		return distPerSekond;
 	}

	
	private Entity getRightEntity(World world, int entityID) {
		List list = world.loadedEntityList;
		for(int i = 0; i < world.loadedEntityList.size(); i++) {
			Entity current = (Entity) list.toArray()[i];
			if(current != null) {
				if(current.entityId == entityID) {
					return current;
				}
			}
		}
		return null;
	}
	
	private void destroy(EntityPlayer player) {
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == this.itemID) {
			player.inventory.mainInventory[player.inventory.currentItem] = null;
			player.dropPlayerItem(new ItemStack(Item.stick, 2));
		}
	}
	
	private void sendID(int entityID, EntityClientPlayerMP player)  {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(entityID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "lanceHitEntity";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		player.sendQueue.addToSendQueue(packet);
	}
	
	private void sendHitValue(float hitValue, EntityClientPlayerMP player)  {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			System.out.println((int) (hitValue * 10000));
			outputStream.writeInt((int) (hitValue * 10000));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "lanceHitValue";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		player.sendQueue.addToSendQueue(packet);
	}
	
	private void sendIsForwardKeyPressed(boolean isForwardKeyPressed, EntityClientPlayerMP player)  {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeBoolean(isForwardKeyPressed);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "lanceIsForward";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		player.sendQueue.addToSendQueue(packet);
	}
	
	public boolean isRunningOnClient() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            return false;
        } else if (side == Side.CLIENT) {
            return true;
        } else {
                // We are on the Bukkit server.
        	return false;
        }
	}
}
