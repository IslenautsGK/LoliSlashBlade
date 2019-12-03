package com.anotherstar.slashblade.common.item;

import java.util.EnumSet;

import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.slashblade.common.entity.EntityLoliBlisteringSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliHeavyRainSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliPhantomSwordBase;
import com.anotherstar.slashblade.common.entity.EntityLoliSpiralSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliStormSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedBlade;

import cpw.mods.fml.common.Optional;
import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.MessageRangeAttack;
import mods.flammpfeil.slashblade.PacketHandler;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.stats.AchievementList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

@Optional.Interface(iface = "com.anotherstar.common.item.tool.ILoli", modid = "AnotherStar")
public class ItemLoliSlashBlade extends ItemSlashBladeNamed implements ILoli {

	public ItemLoliSlashBlade() {
		super(ToolMaterial.IRON, 4.0f);
		setMaxDamage(40);
		setUnlocalizedName("flammpfeil.slashblade.named");
		setTextureName("flammpfeil.slashblade:proudsoul");
		setCreativeTab(SlashBlade.tab);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int indexOfMainSlot, boolean isCurrent) {
		super.onUpdate(stack, world, entity, indexOfMainSlot, isCurrent);
		if (entity instanceof EntityPlayer) {
			NBTTagCompound nbt = getItemTagCompound(stack);
			if (!nbt.hasKey("LoliOwner")) {
				nbt.setString("LoliOwner", ((EntityPlayer) entity).getDisplayName());
			}
			ItemSlashBlade.RepairCount.set(nbt, 10000);
			ItemSlashBlade.KillCount.set(nbt, 100000);
			ItemSlashBlade.ProudSoul.set(nbt, 1000000);
		}
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, 0);
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public String getOwner(ItemStack stack) {
		NBTTagCompound nbt = getItemTagCompound(stack);
		if (nbt.hasKey("LoliOwner")) {
			return nbt.getString("LoliOwner");
		}
		return "";
	}

	@Override
	public void doRangeAttack(ItemStack item, EntityLivingBase entity, MessageRangeAttack.RangeAttackState mode) {
		World w = entity.worldObj;
		NBTTagCompound tag = getItemTagCompound(item);
		EnumSet<SwordType> types = getSwordType(item);
		if (!types.contains(SwordType.Bewitched))
			return;
		if (types.contains(SwordType.Broken))
			return;
		int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, item);
		if (level <= 0)
			return;
		if (w.isRemote) {
			PacketHandler.INSTANCE.sendToServer(new MessageRangeAttack(mode));
			return;
		}
		int rank = StylishRankManager.getStylishRank(entity);
		switch (mode) {
		case UPKEY: {
			if (entity.getEntityData().hasKey("SB.BSHOLDLIMIT")) {
				long holdLimit = entity.getEntityData().getLong("SB.BSHOLDLIMIT");
				long currentTime = entity.worldObj.getTotalWorldTime();
				if (currentTime < holdLimit) {
					entity.getEntityData().setLong("SB.BSHOLDLIMIT", currentTime);
					return;
				}
			}
			if (!ProudSoul.tryAdd(tag, -1, false))
				return;
			if (rank < 3)
				level = Math.min(1, level);
			float magicDamage = level;
			if (tag.getInteger("RangeAttackType") == 0) {
				EntityLoliPhantomSwordBase entityDrive = new EntityLoliPhantomSwordBase(w, entity, magicDamage, 90.0f);
				if (entityDrive != null) {
					entityDrive.setLifeTime(30);
					int targetid = ItemSlashBlade.TargetEntityId.get(tag);
					entityDrive.setTargetEntityId(targetid);
					if (SummonedSwordColor.exists(tag))
						entityDrive.setColor(SummonedSwordColor.get(tag));
					else
						entityDrive.setColor(entity.worldObj.rand.nextInt() & 0x00FFFFFF);
					w.spawnEntityInWorld(entityDrive);
					if (entity instanceof EntityPlayer)
						AchievementList.triggerAchievement((EntityPlayer) entity, "phantomSword");
				}
			} else {
				EntityLoliSummonedBlade summonedBlade = new EntityLoliSummonedBlade(w, entity, magicDamage, 90.0f);
				if (summonedBlade != null) {
					summonedBlade.setLifeTime(100);
					summonedBlade.setInterval(10);
					int targetid = ItemSlashBlade.TargetEntityId.get(tag);
					summonedBlade.setTargetEntityId(targetid);
					if (SummonedSwordColor.exists(tag))
						summonedBlade.setColor(SummonedSwordColor.get(tag));
					else
						summonedBlade.setColor(entity.worldObj.rand.nextInt() & 0x00FFFFFF);
					w.spawnEntityInWorld(summonedBlade);
				}
			}
			break;
		}
		case BLISTERING: {
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			long currentTime = entity.worldObj.getTotalWorldTime();
			final int holdLimit = 400;
			entity.getEntityData().setLong("SB.BSHOLDLIMIT", currentTime + holdLimit);
			entity.playSound("mob.endermen.portal", 0.5F, 1.0F);
			int count = 4;
			if (3 < rank)
				count += 2;
			if (5 <= rank)
				count += 2;
			float magicDamage = level * 2;
			for (int i = 0; i < count; i++) {
				EntityLoliBlisteringSwords summonedSword = new EntityLoliBlisteringSwords(w, entity, magicDamage, 90.0f,
						i);
				if (summonedSword != null) {
					summonedSword.setLifeTime(30);
					summonedSword.setIsJudgement(types.contains(SwordType.FiercerEdge));
					int targetid = ItemSlashBlade.TargetEntityId.get(tag);
					summonedSword.setTargetEntityId(targetid);
					if (SummonedSwordColor.exists(tag))
						summonedSword.setColor(SummonedSwordColor.get(tag));
					else
						summonedSword.setColor(entity.worldObj.rand.nextInt() & 0x00FFFFFF);
					w.spawnEntityInWorld(summonedSword);
				}
			}
			break;
		}
		case SPIRAL: {
			int currentTime = (int) entity.worldObj.getWorldTime();
			final int holdLimit = 200;
			if (entity.getEntityData().hasKey("SB.SPHOLDID")) {
				if (currentTime < (entity.getEntityData().getInteger("SB.SPHOLDID") + holdLimit)) {
					entity.getEntityData().removeTag("SB.SPHOLDID");
					return;
				}
			}
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			entity.worldObj.playSoundEffect(entity.prevPosX, entity.prevPosY, entity.prevPosZ, "mob.endermen.portal",
					0.7F, 1.0F);
			int count = 6;
			if (rank < 3)
				level = Math.min(1, level);
			float magicDamage = level;
			float arc = 360.0f / count;
			entity.getEntityData().setInteger("SB.SPHOLDID", currentTime);
			for (int i = 0; i < count; i++) {
				float offset = i * arc;
				EntityLoliSpiralSwords summonedSword = new EntityLoliSpiralSwords(w, entity, magicDamage, 0, offset);
				if (summonedSword != null) {
					summonedSword.setHoldId(currentTime);
					summonedSword.setInterval(holdLimit);
					summonedSword.setLifeTime(holdLimit);
					if (SummonedSwordColor.exists(tag))
						summonedSword.setColor(SummonedSwordColor.get(tag));
					else
						summonedSword.setColor(entity.worldObj.rand.nextInt() & 0x00FFFFFF);
					w.spawnEntityInWorld(summonedSword);
				}
			}
			break;
		}
		case STORM: {
			int targetId = TargetEntityId.get(tag);
			if (targetId == 0)
				return;
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			entity.worldObj.playSoundEffect(entity.prevPosX, entity.prevPosY, entity.prevPosZ, "mob.endermen.portal",
					0.7F, 1.0F);
			int count = 6;
			if (rank < 3)
				level = Math.min(1, level);
			float magicDamage = level / 2.0f;
			float arc = 360.0f / count;
			final int holdLimit = (int) (20 * 2);
			for (int i = 0; i < count; i++) {
				float offset = i * arc;
				EntityLoliStormSwords summonedSword = new EntityLoliStormSwords(w, entity, magicDamage, 0, offset,
						targetId);
				if (summonedSword != null) {
					summonedSword.setInterval(holdLimit);
					summonedSword.setLifeTime(holdLimit + 30);
					if (SummonedSwordColor.exists(tag))
						summonedSword.setColor(SummonedSwordColor.get(tag));
					else
						summonedSword.setColor(entity.worldObj.rand.nextInt() & 0x00FFFFFF);
					w.spawnEntityInWorld(summonedSword);
				}
			}
			break;
		}
		case HEAVY_RAIN: {
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			entity.worldObj.playSoundEffect(entity.prevPosX, entity.prevPosY, entity.prevPosZ, "mob.endermen.portal",
					0.7F, 1.0F);
			int count = 10;
			int multiplier = 2;
			if (5 <= rank)
				multiplier += 1;
			float magicDamage = 1;
			int targetid = ItemSlashBlade.TargetEntityId.get(tag);
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < multiplier; j++) {
					EntityLoliHeavyRainSwords summonedSword = new EntityLoliHeavyRainSwords(w, entity, magicDamage,
							entity.getRNG().nextFloat() * 360.0f, i, targetid);
					if (summonedSword != null) {
						summonedSword.setLifeTime(30 + i);
						if (SummonedSwordColor.exists(tag))
							summonedSword.setColor(SummonedSwordColor.get(tag));
						else
							summonedSword.setColor(entity.worldObj.rand.nextInt() & 0x00FFFFFF);
						w.spawnEntityInWorld(summonedSword);
					}
				}
			}
			break;
		}
		}
	}

}
