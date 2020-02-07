package com.anotherstar.slashblade.network;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.slashblade.common.item.ItemLoliSlashBlade;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliSlashBladeChangeRangePacket implements IMessage {

	public LoliSlashBladeChangeRangePacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class MessageHandler implements IMessageHandler<LoliSlashBladeChangeRangePacket, IMessage> {

		@Override
		@Optional.Method(modid = LoliPickaxe.MODID)
		public IMessage onMessage(LoliSlashBladeChangeRangePacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLoliSlashBlade) {
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt == null) {
					nbt = new NBTTagCompound();
					nbt.setInteger("loliRange", 1);
					stack.setTagCompound(nbt);
				} else {
					if (nbt.hasKey("loliRange")) {
						nbt.setInteger("loliRange", nbt.getInteger("loliRange") >= ConfigLoader.loliPickaxeMaxRange ? 0
								: nbt.getInteger("loliRange") + 1);
					} else {
						nbt.setInteger("loliRange", 1);
					}
				}
				player.sendMessage(
						new TextComponentTranslation("loliPickaxe.range", 1 + 2 * nbt.getInteger("loliRange")));
				BlockPos pos = player.getPosition();
				player.connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS,
						pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));
			}
			return null;
		}

	}

}
