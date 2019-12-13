package com.anotherstar.slashblade.common.entity;

import java.util.EnumSet;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import mods.flammpfeil.slashblade.EntityDrive;
import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityJudgmentCutManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = "LoliPickaxe"),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = "LoliPickaxe") })
public class EntityLoliSuperSA extends EntityJudgmentCutManager {

	public EntityLoliSuperSA(World world) {
		super(world);
		this.alreadyHitEntity.add(this);
	}

	public EntityLoliSuperSA(World world, EntityLivingBase entity) {
		super(world, entity);
		this.alreadyHitEntity.add(this);
	}

	@Override
	public void onUpdate() {
		if (this.thrower == null && this.getDataWatcher().getWatchableObjectInt(4) != 0) {
			this.thrower = this.worldObj.getEntityByID(this.getDataWatcher().getWatchableObjectInt(4));
		}
		if (this.blade == null && this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) this.getThrower();
			ItemStack stack = player.getHeldItem();
			if (stack.getItem() instanceof ItemSlashBlade)
				this.blade = stack;
		}
		if (this.thrower != null) {
			this.thrower.motionX = 0;
			this.thrower.motionY = 0;
			this.thrower.motionZ = 0;
			if (this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) this.getThrower();
				if (this.ticksExisted < 3)
					player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F,
							1.0F);
				if (this.ticksExisted < 8) {
					for (int i = 0; i < 20; i++) {
						double d0 = player.getRNG().nextGaussian() * 0.2D;
						double d1 = player.getRNG().nextGaussian() * 0.2D;
						double d2 = player.getRNG().nextGaussian() * 0.2D;
						double d3 = 16.0D;
						this.worldObj.spawnParticle("witchMagic",
								player.posX + (double) (player.getRNG().nextFloat() * player.width * 2.0F)
										- (double) player.width - d0 * d3,
								player.posY, player.posZ + (double) (player.getRNG().nextFloat() * player.width * 2.0F)
										- (double) player.width - d2 * d3,
								d0, d1, d2);
					}
					player.worldObj.playSoundAtEntity(player, "mob.blaze.hit", 1.0F, 1.0F);
				}
			}
		}
		if (!worldObj.isRemote) {
			if (this.ticksExisted == 2 && this.getThrower() != null) {
				List<Entity> list = this.worldObj.getEntitiesWithinAABB(
						ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class,
						AxisAlignedBB.getBoundingBox(this.posX - ConfigLoader.loliPickaxeKillRange,
								this.posY - ConfigLoader.loliPickaxeKillRange,
								this.posZ - ConfigLoader.loliPickaxeKillRange,
								this.posX + ConfigLoader.loliPickaxeKillRange,
								this.posY + ConfigLoader.loliPickaxeKillRange,
								this.posZ + ConfigLoader.loliPickaxeKillRange));
				list.removeAll(alreadyHitEntity);
				if (blade != null) {
					NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
					for (Entity curEntity : list) {
						if (curEntity instanceof EntityLivingBase) {
							int stanTicks = 40;
							if (!curEntity.worldObj.isRemote) {
								((EntityLivingBase) curEntity).addPotionEffect(
										new PotionEffect(Potion.moveSlowdown.getId(), stanTicks, 30, true));
								((EntityLivingBase) curEntity).attackTime = stanTicks;
							}
							StunManager.setStun((EntityLivingBase) curEntity, stanTicks);
							StunManager.setFreeze((EntityLivingBase) curEntity, stanTicks);
							for (int i = 0; i < 5; i++) {
								this.worldObj.spawnParticle("portal",
										curEntity.posX + (this.rand.nextDouble() - 0.5D) * (double) curEntity.width,
										curEntity.posY + this.rand.nextDouble() * (double) curEntity.height - 0.25D,
										curEntity.posZ + (this.rand.nextDouble() - 0.5D) * (double) curEntity.width,
										(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
										(this.rand.nextDouble() - 0.5D) * 2.0D);
							}
						}
					}
				}
			}
			if (this.ticksExisted == 25 && this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) this.getThrower();
				List<Entity> list = this.worldObj.getEntitiesWithinAABB(
						ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class,
						AxisAlignedBB.getBoundingBox(this.posX - ConfigLoader.loliPickaxeKillRange,
								this.posY - ConfigLoader.loliPickaxeKillRange,
								this.posZ - ConfigLoader.loliPickaxeKillRange,
								this.posX + ConfigLoader.loliPickaxeKillRange,
								this.posY + ConfigLoader.loliPickaxeKillRange,
								this.posZ + ConfigLoader.loliPickaxeKillRange));
				list.removeAll(alreadyHitEntity);
				list.removeIf(entity -> entity instanceof EntityLoliSA || entity instanceof EntityDrive
						|| entity instanceof EntityLoliSuperSA);
				if (blade != null) {
					NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
					ItemSlashBlade bladeItem = (ItemSlashBlade) blade.getItem();
					int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, blade);
					float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
					for (Entity curEntity : list) {
						if (ConfigLoader.loliPickaxeValidToAllEntity) {
							attack(curEntity);
						} else {
							for (int i = 0; i < 5; i++) {
								EntityDrive entityDrive = new EntityDrive(this.worldObj,
										(EntityLivingBase) this.getThrower(), Math.min(1.0f, magicDamage), true, 0);
								float rotationYaw = curEntity.rotationYaw + 60 * i
										+ (entityDrive.getRand().nextFloat() - 0.5f) * 60;
								float rotationPitch = (entityDrive.getRand().nextFloat() - 0.5f) * 60;
								float fYawDtoR = (rotationYaw / 180F) * (float) Math.PI;
								float fPitDtoR = (rotationPitch / 180F) * (float) Math.PI;
								float fYVecOfst = 0.5f;
								float motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst * 2;
								float motionY = -MathHelper.sin(fPitDtoR) * fYVecOfst;
								float motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst * 2;
								entityDrive.setLocationAndAngles(curEntity.posX - motionX,
										curEntity.posY + (double) curEntity.getEyeHeight() / 2D - motionY,
										curEntity.posZ - motionZ, rotationYaw, rotationPitch);
								entityDrive.setDriveVector(fYVecOfst);
								entityDrive.setLifeTime(8);
								entityDrive.setIsMultiHit(false);
								int rank = StylishRankManager.getStylishRank(this.getThrower());
								if (5 <= rank) {
									EnumSet<ItemSlashBlade.SwordType> type = bladeItem.getSwordType(blade);
									entityDrive
											.setIsSlashDimension(type.contains(ItemSlashBlade.SwordType.FiercerEdge));
								}
								entityDrive.setRoll(90.0f + 120 * (entityDrive.getRand().nextFloat() - 0.5f));
								if (entityDrive != null) {
									this.worldObj.spawnEntityInWorld(entityDrive);
								}
							}
							if (!curEntity.worldObj.isRemote) {
								for (int i = 0; i < 2; i++) {
									EntityLoliSA dim = new EntityLoliSA(curEntity.worldObj,
											(EntityLivingBase) getThrower(), 1);
									if (dim != null) {
										dim.setPosition(curEntity.posX + (this.rand.nextFloat() - 0.5) * 5.0,
												curEntity.posY + curEntity.height * this.rand.nextFloat(),
												curEntity.posZ + (this.rand.nextFloat() - 0.5) * 5.0);
										dim.setLifeTime(10 + i * 3);
										dim.setIsSlashDimension(true);
										curEntity.worldObj.spawnEntityInWorld(dim);
									}
								}
							}
						}
					}
				}
			}
		}
		if (ticksExisted >= 30) {
			if (blade != null) {
				NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
				ItemSlashBlade bladeItem = (ItemSlashBlade) blade.getItem();
				ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Battou);
				if (this.getThrower() != null && this.getThrower() instanceof EntityPlayer)
					bladeItem.doSwingItem(blade, (EntityPlayer) this.getThrower());
			}
			alreadyHitEntity.clear();
			alreadyHitEntity = null;
			setDead();
		}
	}

	private void attack(Entity target) {
		if (getThrower() instanceof EntityLivingBase) {
			if (target instanceof EntityPlayer) {
				LoliPickaxeUtil.killPlayer((EntityPlayer) target, (EntityLivingBase) thrower);
			} else if (target instanceof EntityLivingBase) {
				LoliPickaxeUtil.killEntityLiving((EntityLivingBase) target, (EntityLivingBase) thrower);
			} else if (ConfigLoader.loliPickaxeValidToAllEntity) {
				LoliPickaxeUtil.killEntity(target);
			}
		}
	}

}
