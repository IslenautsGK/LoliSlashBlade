package com.anotherstar.slashblade.common.sa;

import java.util.List;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.slashblade.common.entity.EntityLoliSA;
import com.anotherstar.slashblade.common.entity.EntityLoliSuperSA;
import com.anotherstar.util.LoliPickaxeUtil;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.event.ScheduleEntitySpawner;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
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
public class LoliSword extends SpecialAttackBase implements IJustSpecialAttack, ISuperSpecialAttack {

	@Override
	public String toString() {
		return "lolisword";
	}

	@Override
	public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
		World world = player.world;
		NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
		Entity target = null;
		int entityId = ItemSlashBlade.TargetEntityId.get(tag);
		if (entityId != 0) {
			Entity tmp = world.getEntityByID(entityId);
			if (tmp != null) {
				if (tmp.getDistance(player) < 100.0f)
					target = tmp;
			}
		}
		if (target == null) {
			target = getEntityToWatch(player);
		}
		if (target == null) {
			ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1.0F);
			final int cost = -20;
			if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
				ItemSlashBlade.damageItem(stack, 10, player);
			}
			if (!player.world.isRemote) {
				int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
				float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (0.5f + level / 5.0f);
				EntityLoliSA dim = new EntityLoliSA(world, player, magicDamage);
				if (dim != null) {
					Vec3d pos = player.getLookVec();
					pos = pos.scale(5);
					pos = pos.add(player.getPositionVector());
					pos = pos.addVector(0, player.getEyeHeight(), 0);
					Vec3d offset = player.getPositionVector().addVector(0, player.getEyeHeight(), 0);
					Vec3d offsettedLook = offset.add(player.getLookVec().scale(5));
					RayTraceResult movingobjectposition = world.rayTraceBlocks(offset, offsettedLook);
					if (movingobjectposition != null) {
						IBlockState state = null;
						BlockPos blockPos = movingobjectposition.getBlockPos();
						if (blockPos != null)
							state = world.getBlockState(blockPos);
						if (state != null && state.getCollisionBoundingBox(world, blockPos) == null)
							movingobjectposition = null;
						else {
							Vec3d tmppos = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y,
									movingobjectposition.hitVec.z);
							if (1 < tmppos.distanceTo(player.getPositionVector())) {
								pos = tmppos;
							}
						}
					}
					dim.setPosition(pos.x, pos.y, pos.z);
					dim.setLifeTime(10);
					dim.setIsSlashDimension(true);
					world.spawnEntity(dim);
				}
			}
		} else {
			ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
			spawnParticle(world, target);
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1.0F);
			final int cost = -20;
			if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
				ItemSlashBlade.damageItem(stack, 10, player);
			}
			AxisAlignedBB bb = target.getEntityBoundingBox();
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
			if (!target.world.isRemote) {
				int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
				float magicDamage = 0.5f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
				EntityLoliSA dim = new EntityLoliSA(world, player, magicDamage);
				if (dim != null) {
					dim.setPosition(target.posX, target.posY + target.height / 2.0, target.posZ);
					dim.setLifeTime(10);
					dim.setGlowing(true);
					world.spawnEntity(dim);
				}
			}
		}
	}

	private Entity getEntityToWatch(EntityPlayer player) {
		World world = player.world;
		Entity target = null;
		for (int dist = 2; dist < 100; dist += 2) {
			AxisAlignedBB bb = player.getEntityBoundingBox();
			Vec3d vec = player.getLookVec();
			vec = vec.normalize();
			bb = bb.grow(2.0f, 0.25f, 2.0f);
			bb = bb.offset(vec.x * (float) dist, vec.y * (float) dist, vec.z * (float) dist);
			List<Entity> list = world.getEntitiesWithinAABB(
					ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class, bb);
			if (list.contains(player)) {
				list.remove(player);
			}
			float distance = 100.0f;
			for (Entity curEntity : list) {
				float curDist = curEntity.getDistance(player);
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
		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX, target.posY + target.height, target.posZ,
				3.0, 3.0, 3.0);
		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX + 1.0, target.posY + target.height + 1.0,
				target.posZ, 3.0, 3.0, 3.0);
		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX, target.posY + target.height + 0.5,
				target.posZ + 1.0, 3.0, 3.0, 3.0);
	}

	@Override
	public void doJustSpacialAttack(ItemStack stack, EntityPlayer player) {
		doSpacialAttack(stack, player);
	}

	@Override
	public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
		EntityLoliSuperSA entityDA = new EntityLoliSuperSA(player.world, player);
		if (entityDA != null) {
			ScheduleEntitySpawner.getInstance().offer(entityDA);
		}
		UntouchableTime.setUntouchableTime(player, 30, true);
	}

}
