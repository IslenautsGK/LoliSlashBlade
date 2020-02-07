package com.anotherstar.slashblade.common.entity;

import java.util.EnumSet;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntityJudgmentCutManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLoliSuperSA extends EntityJudgmentCutManager {

	public EntityLoliSuperSA(World world) {
		super(world);
		this.alreadyHitEntity.add(this);
	}

	public EntityLoliSuperSA(World world, EntityLivingBase entity) {
		super(world, entity);
		setThrowerID(thrower.getEntityId());
		this.alreadyHitEntity.add(this);
	}

	private static final DataParameter<Integer> ThrowerEntityID = EntityDataManager.createKey(EntityLoliSuperSA.class,
			DataSerializers.VARINT);

	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(ThrowerEntityID, 0);
	}

	private int getThrowerID() {
		return this.getDataManager().get(ThrowerEntityID);
	}

	private void setThrowerID(int id) {
		this.getDataManager().set(ThrowerEntityID, id);
	}

	@Override
	public void onUpdate() {
		if (this.thrower == null && this.getThrowerID() != 0) {
			this.thrower = this.world.getEntityByID(this.getThrowerID());
		}
		if (this.blade.isEmpty() && this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) this.getThrower();
			ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
			if (stack.getItem() instanceof ItemSlashBlade)
				this.blade = stack;
		}
		if (this.thrower != null) {
			ReflectionAccessHelper.setVelocity(getThrower(), 0, 0, 0);
			if (this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) this.getThrower();
				if (this.ticksExisted < 3)
					player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
				if (this.ticksExisted < 8) {
					for (int i = 0; i < 20; i++) {
						double d0 = player.getRNG().nextGaussian() * 0.2D;
						double d1 = player.getRNG().nextGaussian() * 0.2D;
						double d2 = player.getRNG().nextGaussian() * 0.2D;
						double d3 = 16.0D;
						this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH,
								player.posX + player.getRNG().nextFloat() * player.width * 2.0F
										- player.width - d0 * d3,
								player.posY, player.posZ + player.getRNG().nextFloat() * player.width * 2.0F
										- player.width - d2 * d3,
								d0, d1, d2);
					}
					player.playSound(SoundEvents.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
				}
			}
		}
		if (!world.isRemote) {
			if (this.ticksExisted == 2 && this.getThrower() != null) {
				int range = ConfigLoader.getInt(blade, "loliPickaxeKillRange");
				List<Entity> list = this.world.getEntitiesWithinAABB(
						ConfigLoader.getBoolean(blade, "loliPickaxeValidToAllEntity") ? Entity.class
								: EntityLivingBase.class,
						new AxisAlignedBB(this.posX - range, this.posY - range, this.posZ - range, this.posX + range,
								this.posY + range, this.posZ + range));
				list.removeAll(alreadyHitEntity);
				if (!blade.isEmpty()) {
					NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
					for (Entity curEntity : list) {
						if (curEntity instanceof EntityLivingBase) {
							int stanTicks = 40;
							if (!curEntity.world.isRemote) {
								((EntityLivingBase) curEntity).addPotionEffect(
										new PotionEffect(MobEffects.SLOWNESS, stanTicks, 30, true, false));
							}
							StunManager.setStun((EntityLivingBase) curEntity, stanTicks);
							StunManager.setFreeze((EntityLivingBase) curEntity, stanTicks);
							for (int i = 0; i < 5; i++) {
								this.world.spawnParticle(EnumParticleTypes.PORTAL,
										curEntity.posX + (this.rand.nextDouble() - 0.5D) * curEntity.width,
										curEntity.posY + this.rand.nextDouble() * curEntity.height - 0.25D,
										curEntity.posZ + (this.rand.nextDouble() - 0.5D) * curEntity.width,
										(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
										(this.rand.nextDouble() - 0.5D) * 2.0D);
							}
						}
					}
				}
			}
			if (this.ticksExisted == 25 && this.getThrower() != null && this.getThrower() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) this.getThrower();
				int range = ConfigLoader.getInt(blade, "loliPickaxeKillRange");
				List<Entity> list = this.world.getEntitiesWithinAABB(
						ConfigLoader.getBoolean(blade, "loliPickaxeValidToAllEntity") ? Entity.class
								: EntityLivingBase.class,
						new AxisAlignedBB(this.posX - range, this.posY - range, this.posZ - range, this.posX + range,
								this.posY + range, this.posZ + range));
				list.removeAll(alreadyHitEntity);
				list.removeIf(entity -> entity instanceof EntityLoliSA || entity instanceof EntityDrive
						|| entity instanceof EntityLoliSuperSA);
				if (!blade.isEmpty()) {
					NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
					ItemSlashBlade bladeItem = (ItemSlashBlade) blade.getItem();
					int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, blade);
					float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
					for (Entity curEntity : list) {
						if (ConfigLoader.getBoolean(blade, "loliPickaxeValidToAllEntity")) {
							attack(blade, curEntity);
						} else {
							for (int i = 0; i < 5; i++) {
								EntityDrive entityDrive = new EntityDrive(this.world,
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
										curEntity.posY + curEntity.getEyeHeight() / 2D - motionY,
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
									this.world.spawnEntity(entityDrive);
								}
							}
							if (!curEntity.world.isRemote) {
								for (int i = 0; i < 2; i++) {
									EntityLoliSA dim = new EntityLoliSA(curEntity.world,
											(EntityLivingBase) getThrower(), 1);
									if (dim != null) {
										dim.setPosition(curEntity.posX + (this.rand.nextFloat() - 0.5) * 5.0,
												curEntity.posY + curEntity.height * this.rand.nextFloat(),
												curEntity.posZ + (this.rand.nextFloat() - 0.5) * 5.0);
										dim.setLifeTime(10 + i * 3);
										dim.setIsSlashDimension(true);
										curEntity.world.spawnEntity(dim);
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

	private void attack(ItemStack blade, Entity target) {
		if (getThrower() instanceof EntityLivingBase) {
			if (target instanceof EntityPlayer) {
				LoliPickaxeUtil.killPlayer((EntityPlayer) target, (EntityLivingBase) thrower);
			} else if (target instanceof EntityLivingBase) {
				LoliPickaxeUtil.killEntityLiving((EntityLivingBase) target, (EntityLivingBase) thrower);
			} else if (ConfigLoader.getBoolean(blade, "loliPickaxeValidToAllEntity")) {
				LoliPickaxeUtil.killEntity(target);
			}
		}
	}

}
