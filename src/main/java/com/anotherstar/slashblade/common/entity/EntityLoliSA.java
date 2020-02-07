package com.anotherstar.slashblade.common.entity;

import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntitySlashDimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityLoliSA extends EntitySlashDimension {

	public EntityLoliSA(World world) {
		super(world);
		alreadyHitEntity.add(this);
	}

	public EntityLoliSA(World world, EntityLivingBase entity, float AttackLevel) {
		super(world, entity, AttackLevel);
		alreadyHitEntity.add(this);
	}

	public EntityLoliSA(World world, EntityLivingBase entity, float AttackLevel, boolean multiHit) {
		super(world, entity, AttackLevel, multiHit);
		alreadyHitEntity.add(this);
	}

	@Override
	public void onUpdate() {
		if (!this.world.isRemote) {
			this.setFlag(6, this.isGlowing());
		}
		this.onEntityUpdate();
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		if (!world.isRemote) {
			if (ticksExisted < 8 && ticksExisted % 2 == 0) {
				this.playSound(SoundEvents.ENTITY_WITHER_HURT, 0.2F, 0.5F + 0.25f * this.rand.nextFloat());
			}
			AxisAlignedBB bb = this.getEntityBoundingBox();
			if (this.getThrower() instanceof EntityLivingBase) {
				EntityLivingBase entityLiving = (EntityLivingBase) this.getThrower();
				List<Entity> list = this.world.getEntitiesWithinAABB(
						ConfigLoader.getBoolean(blade, "loliPickaxeValidToAllEntity") ? Entity.class
								: EntityLivingBase.class,
						bb);
				list.removeAll(alreadyHitEntity);
				list.removeIf(entity -> entity instanceof EntityLoliSA || entity instanceof EntityDrive
						|| entity instanceof EntityLoliSuperSA);
				alreadyHitEntity.addAll(list);
				for (Entity curEntity : list) {
					attack(blade, curEntity);
				}
			}
		}
		if (ticksExisted >= getLifeTime()) {
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
