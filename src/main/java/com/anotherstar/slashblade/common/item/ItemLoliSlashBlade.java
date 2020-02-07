package com.anotherstar.slashblade.common.item;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.codec.digest.DigestUtils;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.gui.ILoliInventory;
import com.anotherstar.common.gui.InventoryLoliPickaxe;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.network.LoliDeadPacket;
import com.anotherstar.network.NetworkHandler;
import com.anotherstar.slashblade.common.entity.EntityLoliBlisteringSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliHeavyRainSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSpinningSword;
import com.anotherstar.slashblade.common.entity.EntityLoliSpiralSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliStormSwords;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedBlade;
import com.anotherstar.slashblade.common.entity.EntityLoliSummonedSwordBase;
import com.anotherstar.slashblade.common.event.LoliLoadEvent;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntitySpinningSword;
import mods.flammpfeil.slashblade.event.ScheduleEntitySpawner;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.network.MessageMoveCommandState;
import mods.flammpfeil.slashblade.network.MessageRangeAttack;
import mods.flammpfeil.slashblade.network.NetworkManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "com.anotherstar.common.item.tool.ILoli", modid = LoliPickaxe.MODID)
public class ItemLoliSlashBlade extends ItemSlashBladeNamed implements ILoli {

	public ItemLoliSlashBlade() {
		super(ToolMaterial.IRON, 4.0f);
		setMaxDamage(40);
		setUnlocalizedName("flammpfeil.slashblade.named");
		setCreativeTab(SlashBlade.tab);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int indexOfMainSlot, boolean isCurrent) {
		super.onUpdate(stack, world, entity, indexOfMainSlot, isCurrent);
		if (entity instanceof EntityPlayer) {
			NBTTagCompound nbt = getItemTagCompound(stack);
			if (!nbt.hasKey("SimpleVerification") || !sha512Hex2(nbt.getString("SimpleVerification")).equals(
					"eaf01c87460d4bbd903c95dd8bf337e37f38e432538dcf0114212382a6ed349e10692de9d22c4145f940ca58495933f2516fc7398e3d888de2a7ba3d9a899c92")) {
				stack.setCount(0);
				if (entity instanceof EntityPlayerMP) {
					NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliDeadPacket(false, true, false, false),
							(EntityPlayerMP) entity);
				}
			} else {
				if (!nbt.hasKey("LoliOwner")) {
					nbt.setString("LoliOwner", ((EntityPlayer) entity).getName());
				}
				ItemSlashBladeNamed.CurrentItemName.set(nbt, "flammpfeil.slashblade.named.loliblade");
				ItemSlashBladeNamed.CustomMaxDamage.set(nbt, Integer.valueOf(100));
				ItemSlashBlade.setBaseAttackModifier(nbt, 100.0F);
				ItemSlashBlade.SpecialAttackType.set(nbt, LoliLoadEvent.id);
				ItemSlashBlade.TextureName.set(nbt, "named/loliblade/texture");
				ItemSlashBlade.ModelName.set(nbt, "named/loliblade/model");
				ItemSlashBlade.RepairCount.set(nbt, 10000);
				ItemSlashBlade.KillCount.set(nbt, 100000);
				ItemSlashBlade.ProudSoul.set(nbt, 1000000);
				nbt.setBoolean("Unbreakable", true);
			}
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
		World w = entity.world;
		NBTTagCompound tag = getItemTagCompound(item);
		EnumSet<SwordType> types = getSwordType(item);
		if (!types.contains(SwordType.Bewitched))
			return;
		if (types.contains(SwordType.Broken))
			return;
		int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, item);
		if (level <= 0)
			return;
		if (w.isRemote) {
			NetworkManager.INSTANCE.sendToServer(new MessageRangeAttack(mode));
			return;
		}
		int rank = StylishRankManager.getStylishRank(entity);
		switch (mode) {
		case UPKEY: {
			if (entity.getEntityData().hasKey("SB.BSHOLDLIMIT")) {
				long holdLimit = entity.getEntityData().getLong("SB.BSHOLDLIMIT");
				long currentTime = entity.getEntityWorld().getTotalWorldTime();
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
			entity.world.playSound((EntityPlayer) null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
					SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.35F, 1.0F);
			final String tauntIdKey = "SB.TauntId";
			int tauntId = entity.getEntityData().getInteger(tauntIdKey);
			if (tauntId != 0) {
				Entity tauntEntity = entity.getEntityWorld().getEntityByID(tauntId);
				if (tauntEntity == null || tauntEntity.isDead || !(tauntEntity instanceof EntitySpinningSword)) {
					tauntId = 0;
					entity.getEntityData().removeTag(tauntIdKey);
				}
			}
			byte command = entity.getEntityData().getByte("SB.MCS");
			if (tauntId == 0 && entity.onGround && entity.motionX == 0 && entity.motionZ == 0
					&& 0 < (command & MessageMoveCommandState.CAMERA)) {
				EntityLoliSpinningSword entityDrive = new EntityLoliSpinningSword(w, entity);
				if (entityDrive != null) {
					entityDrive.setLifeTime(40);
					if (SummonedSwordColor.exists(tag))
						entityDrive.setColor(SummonedSwordColor.get(tag));
					else
						entityDrive.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
					ScheduleEntitySpawner.getInstance().offer(entityDrive);
					entity.getEntityData().setInteger(tauntIdKey, entityDrive.getEntityId());
				}
			} else if (tag.getInteger("RangeAttackType") == 0) {
				EntityLoliSummonedSwordBase entityDrive = new EntityLoliSummonedSwordBase(w, entity, magicDamage,
						90.0f);
				if (entityDrive != null) {
					entityDrive.setLifeTime(30);
					int targetid = ItemSlashBlade.TargetEntityId.get(tag);
					entityDrive.setTargetEntityId(targetid);
					if (SummonedSwordColor.exists(tag))
						entityDrive.setColor(SummonedSwordColor.get(tag));
					else
						entityDrive.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
					ScheduleEntitySpawner.getInstance().offer(entityDrive);
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
						summonedBlade.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
					ScheduleEntitySpawner.getInstance().offer(summonedBlade);
				}
			}
			break;
		}
		case BLISTERING: {
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			long currentTime = entity.getEntityWorld().getTotalWorldTime();
			final int holdLimit = 400;
			entity.getEntityData().setLong("SB.BSHOLDLIMIT", currentTime + holdLimit);
			entity.world.playSound((EntityPlayer) null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
					SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.7F, 1.0F);
			float magicDamage = level * 2;
			for (int i = 0; i < 8; i++) {
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
						summonedSword.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
					ScheduleEntitySpawner.getInstance().offer(summonedSword);
				}
			}
			break;
		}
		case SPIRAL: {
			int currentTime = (int) entity.getEntityWorld().getWorldTime();
			final int holdLimit = 200;
			if (entity.getEntityData().hasKey("SB.SPHOLDID")) {
				if (currentTime < (entity.getEntityData().getInteger("SB.SPHOLDID") + holdLimit)) {
					entity.getEntityData().removeTag("SB.SPHOLDID");
					return;
				}
			}
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			entity.world.playSound((EntityPlayer) null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
					SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.7F, 1.0F);
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
						summonedSword.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
					ScheduleEntitySpawner.getInstance().offer(summonedSword);
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
			entity.world.playSound((EntityPlayer) null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
					SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.7F, 1.0F);
			int count = 6;
			if (rank < 3)
				level = Math.min(1, level);
			float magicDamage = level / 2.0f;
			float arc = 360.0f / count;
			final int holdLimit = 20 * 2;
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
						summonedSword.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
					ScheduleEntitySpawner.getInstance().offer(summonedSword);
				}
			}
			break;
		}
		case HEAVY_RAIN: {
			if (!ProudSoul.tryAdd(tag, -10, false))
				return;
			entity.world.playSound((EntityPlayer) null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
					SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.7F, 1.0F);
			int count = 10;
			int multiplier = 5;
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
							summonedSword.setColor(entity.world.rand.nextInt() & 0x00FFFFFF);
						ScheduleEntitySpawner.getInstance().offer(summonedSword);
					}
				}
			}
			break;
		}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format("loliPickaxe.curRange", 1 + 2 * getRange(stack)));
		if (ConfigLoader.getBoolean(stack, "loliPickaxeMandatoryDrop")) {
			tooltip.add(I18n.format("loliPickaxe.mandatoryDrop"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoAccept")) {
			tooltip.add(I18n.format("loliPickaxe.autoAccept"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeSilkTouch")) {
			tooltip.add(I18n.format("loliPickaxe.silkTouch"));
		} else {
			if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoFurnace")) {
				tooltip.add(I18n.format("loliPickaxe.autoFurnace"));
			}
			int level = ConfigLoader.getInt(stack, "loliPickaxeFortuneLevel");
			if (level > 0) {
				tooltip.add(I18n.format("loliPickaxe.fortuneLevel", level));
			}
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeThorns")) {
			tooltip.add(I18n.format("loliPickaxe.thorns"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeKillRangeEntity")) {
			tooltip.add(I18n.format("loliPickaxe.killRange", 2 * ConfigLoader.getInt(stack, "loliPickaxeKillRange")));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoKillRangeEntity")) {
			tooltip.add(I18n.format("loliPickaxe.autoKillRange",
					2 * ConfigLoader.getInt(stack, "loliPickaxeAutoKillRange")));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeCompulsoryRemove")) {
			tooltip.add(I18n.format("loliPickaxe.compulsoryRemove"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
			tooltip.add(I18n.format("loliPickaxe.validToAmityEntity"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity")) {
			tooltip.add(I18n.format("loliPickaxe.validToAllEntity"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeClearInventory")) {
			tooltip.add(I18n.format("loliPickaxe.clearInventory"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeDropItems")) {
			tooltip.add(I18n.format("loliPickaxe.dropItems"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeKickPlayer")) {
			tooltip.add(I18n.format("loliPickaxe.kickPlayer"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeReincarnation")) {
			tooltip.add(I18n.format("loliPickaxe.reincarnation"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeBeyondRedemption")) {
			tooltip.add(I18n.format("loliPickaxe.beyondRedemption"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeBlueScreenAttack")) {
			tooltip.add(I18n.format("loliPickaxe.blueScreenAttack"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeExitAttack")) {
			tooltip.add(I18n.format("loliPickaxe.exitAttack"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeFailRespondAttack")) {
			tooltip.add(I18n.format("loliPickaxe.failRespondAttack"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeKillFacing")) {
			tooltip.add(I18n.format("loliPickaxe.killFacing", ConfigLoader.getInt(stack, "loliPickaxeKillFacingRange"),
					ConfigLoader.getDouble(stack, "loliPickaxeKillFacingSlope")));
		}
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		int time = ConfigLoader.loliPickaxeDropProtectTime;
		if (time <= 0) {
			return true;
		}
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			nbt.setLong("preDropTime", System.currentTimeMillis());
			stack.setTagCompound(nbt);
			return false;
		} else {
			if (nbt.hasKey("preDropTime")) {
				long preDropTime = nbt.getLong("preDropTime");
				long curDropTime = System.currentTimeMillis();
				nbt.setLong("preDropTime", curDropTime);
				return curDropTime - preDropTime < time;
			} else {
				nbt.setLong("preDropTime", System.currentTimeMillis());
				return false;
			}
		}
	}

	@Override
	public int getRange(ItemStack stack) {
		int range = 1;
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("loliRange")) {
			range = nbt.getInteger("loliRange");
		}
		return range;
	}

	@Override
	public ILoliInventory getInventory(ItemStack stack) {
		return new InventoryLoliPickaxe(stack);
	}

	@Override
	public boolean hasInventory(ItemStack stack) {
		return true;
	}

	private String sha512Hex2(String key) {
		for (int i = 0; i < 2; i++) {
			key = DigestUtils.sha512Hex(key);
		}
		return key;
	}

}
