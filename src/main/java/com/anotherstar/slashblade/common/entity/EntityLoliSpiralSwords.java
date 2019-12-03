package com.anotherstar.slashblade.common.entity;

import java.util.List;
import java.util.Random;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.entity.EntityPhantomSwordBase;
import mods.flammpfeil.slashblade.entity.EntitySpiralSwords;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = "AnotherStar"),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = "AnotherStar") })
public class EntityLoliSpiralSwords extends EntitySpiralSwords {

	public EntityLoliSpiralSwords(World world) {
		super(world);
	}

	public EntityLoliSpiralSwords(World world, EntityLivingBase entity, float AttackLevel, float roll,
			float rotOffset) {
		super(world, entity, AttackLevel, roll, rotOffset);
	}

	public EntityLoliSpiralSwords(World world, EntityLivingBase entity, float AttackLevel) {
		super(world, entity, AttackLevel);
	}

	@Override
	protected MovingObjectPosition getMovingObjectPosition() {
		Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
				this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
		vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (movingobjectposition != null) {
			int x = MathHelper.floor_double(movingobjectposition.hitVec.xCoord);
			int y = MathHelper.floor_double(movingobjectposition.hitVec.yCoord);
			int z = MathHelper.floor_double(movingobjectposition.hitVec.zCoord);
			int offset = -1;
			Block block = worldObj.getBlock(x, y + offset, z);
			if (block != null)
				if (block.getCollisionBoundingBoxFromPool(worldObj, x, y + offset, z) == null)
					movingobjectposition = null;
				else
					vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord,
							movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}
		Entity entity = null;
		AxisAlignedBB bb = this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D);
		List list = this.worldObj.getEntitiesWithinAABB(
				ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class, bb);
		list.removeAll(alreadyHitEntity);
		if (getTargetEntityId() != 0) {
			Entity target = worldObj.getEntityByID(getTargetEntityId());
			if (target != null) {
				if (target.boundingBox.intersectsWith(bb))
					list.add(target);
			}
		}
		double d0 = 0.0D;
		int i;
		float f1;
		for (i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);
			if (entity1 instanceof EntityPhantomSwordBase)
				if (((EntityPhantomSwordBase) entity1).getThrower() == this.getThrower())
					continue;
			if (entity1.canBeCollidedWith()) {
				f1 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
				MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
				if (movingobjectposition1 != null) {
					double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}
		if (entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
			movingobjectposition.hitInfo = ItemSlashBlade.AttackableSelector;
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
			if (ticksExisted % 3 != 0)
				return;
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
