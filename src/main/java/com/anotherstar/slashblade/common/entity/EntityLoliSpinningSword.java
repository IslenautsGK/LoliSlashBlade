package com.anotherstar.slashblade.common.entity;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.util.LoliPickaxeUtil;

import mods.flammpfeil.slashblade.entity.EntitySpinningSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = LoliPickaxe.MODID),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = LoliPickaxe.MODID) })
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
