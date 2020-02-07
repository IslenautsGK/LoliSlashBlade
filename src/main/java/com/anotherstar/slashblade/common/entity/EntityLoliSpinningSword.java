package com.anotherstar.slashblade.common.entity;

import com.anotherstar.util.LoliPickaxeUtil;

import mods.flammpfeil.slashblade.entity.EntitySpinningSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityLoliSpinningSword extends EntitySpinningSword {

	public EntityLoliSpinningSword(World world) {
		super(world);
		alreadyHitEntity.add(this);
	}

	public EntityLoliSpinningSword(World world, EntityLivingBase entity) {
		super(world, entity);
		alreadyHitEntity.add(this);
	}

	@Override
	protected void attackEntity(Entity target) {
		if (this.thrower != null)
			this.thrower.getEntityData().setInteger("LastHitSummonedSwords", this.getEntityId());
		if (!this.world.isRemote) {
			if (thrower instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) thrower;
				LoliPickaxeUtil.kill(target, player);
			}
		}
	}

	@Override
	protected void blastAttackEntity(Entity target) {
		if (!this.world.isRemote) {
			if (thrower instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) thrower;
				LoliPickaxeUtil.kill(target, player);
			}
		}
	}

}
