package com.anotherstar.slashblade.common.sa;

import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.slashblade.common.entity.EntityLoliSA;
import com.anotherstar.slashblade.common.entity.EntityLoliSuperSA;
import com.anotherstar.util.LoliPickaxeUtil;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.anotherstar.common.config.ConfigLoader", modid = "LoliPickaxe"),
		@Interface(iface = "com.anotherstar.util.LoliPickaxeUtil", modid = "LoliPickaxe") })
public class LoliSword extends SpecialAttackBase implements IJustSpecialAttack, ISuperSpecialAttack {

	@Override
	public String toString() {
		return "lolisword";
	}

	@Override
	public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
		World world = player.worldObj;
		NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
		Entity target = null;
		int entityId = ItemSlashBlade.TargetEntityId.get(tag);

		if (entityId != 0) {
			Entity tmp = world.getEntityByID(entityId);
			if (tmp != null) {
				if (tmp.getDistanceToEntity(player) < 100.0f)
					target = tmp;
			}
		}
		if (target == null) {
			target = getEntityToWatch(player);
		}
		if (target == null) {
			ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
			player.playSound("mob.endermen.portal", 0.5F, 1.0F);
			final int cost = -20;
			if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
				ItemSlashBlade.damageItem(stack, 10, player);
			}
			if (!player.worldObj.isRemote) {
				int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
				float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (0.5f + level / 5.0f);
				EntityLoliSA dim = new EntityLoliSA(world, player, magicDamage);
				if (dim != null) {
					Vec3 pos = player.getLookVec();
					float scale = 5;
					pos.xCoord *= scale;
					pos.yCoord *= scale;
					pos.zCoord *= scale;
					pos = pos.addVector(player.posX, player.posY, player.posZ);
					pos = pos.addVector(0, player.getEyeHeight(), 0);
					Vec3 offset = Vec3.createVectorHelper(player.posX, player.posY, player.posZ).addVector(0,
							player.getEyeHeight(), 0);
					Vec3 look = player.getLookVec();
					Vec3 offsettedLook = offset.addVector(look.xCoord * 5, look.yCoord * 5, look.zCoord * 5);
					MovingObjectPosition movingobjectposition = world.rayTraceBlocks(offset, offsettedLook);
					if (movingobjectposition != null
							&& movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
						Block block = world.getBlock(movingobjectposition.blockX, movingobjectposition.blockY,
								movingobjectposition.blockZ);
						if (block != null && block.isCollidable()) {
							Vec3 tmppos = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord,
									movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
							if (1 < tmppos.distanceTo(Vec3.createVectorHelper(player.posX, player.posY, player.posZ))) {
								pos = tmppos;
							}
						}
					}
					dim.setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
					dim.setLifeTime(10);
					dim.setIsSlashDimension(true);
					world.spawnEntityInWorld(dim);
				}
			}
		} else {
			ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
			spawnParticle(world, target);
			player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 0.5F, 1.0F);
			final int cost = -20;
			if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
				ItemSlashBlade.damageItem(stack, 10, player);
			}
			AxisAlignedBB bb = target.boundingBox.copy();
			bb = bb.expand(2.0f, 0.25f, 2.0f);
			List<Entity> list = world.getEntitiesWithinAABB(
					ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class, bb);
			if (list.contains(player)) {
				list.remove(player);
			}
			if (!list.contains(target)) {
				list.add(target);
			}
			for (Entity curEntity : list) {
				if (curEntity instanceof EntityPlayer) {
					LoliPickaxeUtil.killPlayer((EntityPlayer) curEntity, player);
				} else if (curEntity instanceof EntityLivingBase) {
					LoliPickaxeUtil.killEntityLiving((EntityLivingBase) curEntity, player);
				} else if (ConfigLoader.loliPickaxeValidToAllEntity) {
					LoliPickaxeUtil.killEntity(curEntity);
				}
			}
			if (!target.worldObj.isRemote) {
				int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
				float magicDamage = 0.5f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
				EntityLoliSA dim = new EntityLoliSA(world, player, magicDamage);
				if (dim != null) {
					dim.setPosition(target.posX, target.posY + target.height / 2.0, target.posZ);
					dim.setLifeTime(10);
					world.spawnEntityInWorld(dim);
				}
			}
		}
	}

	private Entity getEntityToWatch(EntityPlayer player) {
		World world = player.worldObj;
		Entity target = null;
		for (int dist = 2; dist < 100; dist += 2) {
			AxisAlignedBB bb = player.boundingBox.copy();
			Vec3 vec = player.getLookVec();
			vec = vec.normalize();
			bb = bb.expand(2.0f, 0.25f, 2.0f);
			bb = bb.offset(vec.xCoord * (float) dist, vec.yCoord * (float) dist, vec.zCoord * (float) dist);

			List<Entity> list = world.getEntitiesWithinAABB(
					ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class, bb);
			if (list.contains(player)) {
				list.remove(player);
			}
			float distance = 100.0f;
			for (Entity curEntity : list) {
				float curDist = curEntity.getDistanceToEntity(player);
				if (curDist < distance) {
					target = curEntity;
					distance = curDist;
				}
			}
			if (target != null)
				break;
		}
		return target;
	}

	private void spawnParticle(World world, Entity target) {
		world.spawnParticle("largeexplode", target.posX, target.posY + target.height, target.posZ, 3.0, 3.0, 3.0);
		world.spawnParticle("largeexplode", target.posX + 1.0, target.posY + target.height + 1.0, target.posZ, 3.0, 3.0,
				3.0);
		world.spawnParticle("largeexplode", target.posX, target.posY + target.height + 0.5, target.posZ + 1.0, 3.0, 3.0,
				3.0);
	}

	@Override
	public void doJustSpacialAttack(ItemStack stack, EntityPlayer player) {
		doSpacialAttack(stack, player);
	}

	@Override
	public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
		EntityLoliSuperSA entityDA = new EntityLoliSuperSA(player.worldObj, player);
		if (entityDA != null) {
			player.worldObj.spawnEntityInWorld(entityDA);
		}
		UntouchableTime.setUntouchableTime(player, 30, true);
	}

}
