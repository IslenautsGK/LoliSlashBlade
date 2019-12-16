package com.anotherstar.slashblade.common.entity;

import java.util.List;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import mods.flammpfeil.slashblade.entity.EntitySummonedSwordBase;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = LoliPickaxe.MODID),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = LoliPickaxe.MODID) })
public class EntityLoliStormSwords extends EntityStormSwords {

	public EntityLoliStormSwords(World world) {
		super(world);
		alreadyHitEntity.add(this);
	}

	public EntityLoliStormSwords(World world, EntityLivingBase entity, float AttackLevel, float roll, float rotOffset,
			int targetEntityId) {
		super(world, entity, AttackLevel, roll, rotOffset, targetEntityId);
		alreadyHitEntity.add(this);
	}

	public EntityLoliStormSwords(World world, EntityLivingBase entity, float AttackLevel) {
		super(world, entity, AttackLevel);
		alreadyHitEntity.add(this);
	}

	@Override
	protected RayTraceResult getRayTraceResult() {
		Vec3d Vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d Vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult movingobjectposition = this.world.rayTraceBlocks(Vec3d, Vec3d1);
		Vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (movingobjectposition != null) {
			IBlockState state = null;
			BlockPos pos = movingobjectposition.getBlockPos();
			if (pos != null)
				state = world.getBlockState(pos);
			if (state != null && state.getCollisionBoundingBox(world, pos) == null)
				movingobjectposition = null;
			else
				Vec3d1 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y,
						movingobjectposition.hitVec.z);
		}
		Entity entity = null;
		AxisAlignedBB bb = this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D,
				1.0D);
		AxisAlignedBB bb2 = this.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D);
		List list = this.world.getEntitiesWithinAABB(
				ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class, bb);
		list.removeAll(alreadyHitEntity);
		if (getTargetEntityId() != 0) {
			Entity target = world.getEntityByID(getTargetEntityId());
			if (target != null) {
				if (target.getEntityBoundingBox().intersects(bb) || target.getEntityBoundingBox().intersects(bb2))
					list.add(target);
			}
		}
		double d0 = 0.0D;
		int i;
		float f1;
		for (i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);
			if (entity1 instanceof EntitySummonedSwordBase)
				if (((EntitySummonedSwordBase) entity1).getThrower() == this.getThrower())
					continue;
			if (entity1.canBeCollidedWith()) {
				f1 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().grow((double) f1, (double) f1,
						(double) f1);
				RayTraceResult movingobjectposition1 = axisalignedbb1.calculateIntercept(Vec3d1, Vec3d);
				if (movingobjectposition1 != null) {
					double d1 = Vec3d1.distanceTo(movingobjectposition1.hitVec);
					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}
		if (entity != null) {
			movingobjectposition = new RayTraceResult(entity);
			movingobjectposition.hitInfo = EntitySelectorAttackable.getInstance();
		}
		if (movingobjectposition != null && movingobjectposition.entityHit != null
				&& movingobjectposition.entityHit instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
			if (entityplayer.capabilities.disableDamage
					|| (this.getThrower() != null && this.getThrower() instanceof EntityPlayer
							&& !((EntityPlayer) this.getThrower()).canAttackPlayer(entityplayer))) {
				movingobjectposition = null;
			}
		}
		return movingobjectposition;
	}

	int life = 10;

	@Override
	protected void attackEntity(Entity target) {
		if (!this.world.isRemote) {
			if (this.alreadyHitEntity.contains(target))
				return;
			this.alreadyHitEntity.add(target);
			if (!this.world.isRemote) {
				if (thrower instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) thrower;
					LoliPickaxeUtil.kill(target, player);
				}
			}
			if (--life <= 0) {
				setDead();
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
