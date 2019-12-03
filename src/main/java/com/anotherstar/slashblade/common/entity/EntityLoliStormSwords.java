package com.anotherstar.slashblade.common.entity;

import java.util.Random;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = "AnotherStar"),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = "AnotherStar") })
public class EntityLoliStormSwords extends EntityStormSwords {

	public EntityLoliStormSwords(World world) {
		super(world);
	}

	public EntityLoliStormSwords(World world, EntityLivingBase entity, float AttackLevel, float roll, float rotOffset,
			int targetEntityId) {
		super(world, entity, AttackLevel, roll, rotOffset, targetEntityId);
	}

	public EntityLoliStormSwords(World world, EntityLivingBase entity, float AttackLevel) {
		super(world, entity, AttackLevel);
	}

	@Override
	protected void destructEntity(Entity target) {
		if (this.thrower == null) {
			return;
		}
		target.motionX = 0;
		target.motionY = 0;
		target.motionZ = 0;
		target.setDead();
		for (int var1 = 0; var1 < 10; ++var1) {
			Random rand = this.getRand();
			double var2 = rand.nextGaussian() * 0.02D;
			double var4 = rand.nextGaussian() * 0.02D;
			double var6 = rand.nextGaussian() * 0.02D;
			double var8 = 10.0D;
			this.worldObj.spawnParticle("explode",
					target.posX + (double) (rand.nextFloat() * target.width * 2.0F) - (double) target.width
							- var2 * var8,
					target.posY + (double) (rand.nextFloat() * target.height) - var4 * var8, target.posZ
							+ (double) (rand.nextFloat() * target.width * 2.0F) - (double) target.width - var6 * var8,
					var2, var4, var6);
		}
		this.setDead();
	}

	private int life = 10;

	@Override
	protected void attackEntity(Entity target) {
		if (!this.worldObj.isRemote) {
			if (this.alreadyHitEntity.contains(target))
				return;
			this.alreadyHitEntity.add(target);
			if (thrower instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) thrower;
				if (target instanceof EntityPlayer) {
					LoliPickaxeUtil.killPlayer((EntityPlayer) target, player);
				} else if (target instanceof EntityLivingBase) {
					LoliPickaxeUtil.killEntityLiving((EntityLivingBase) target, player);
				} else if (ConfigLoader.loliPickaxeValidToAllEntity) {
					LoliPickaxeUtil.killEntity(target);
				}
			}
			if (--life <= 0) {
				setDead();
			}
		}
	}

	@Override
	protected void blastAttackEntity(Entity target) {
		if (!this.worldObj.isRemote) {
			if (thrower instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) thrower;
				if (target instanceof EntityPlayer) {
					LoliPickaxeUtil.killPlayer((EntityPlayer) target, player);
				} else if (target instanceof EntityLivingBase) {
					LoliPickaxeUtil.killEntityLiving((EntityLivingBase) target, player);
				} else if (ConfigLoader.loliPickaxeValidToAllEntity) {
					LoliPickaxeUtil.killEntity(target);
				}
			}
		}
	}

}
