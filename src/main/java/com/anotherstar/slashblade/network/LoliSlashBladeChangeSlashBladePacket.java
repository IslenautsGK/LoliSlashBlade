package com.anotherstar.slashblade.network;

import com.anotherstar.slashblade.common.item.ItemLoliSlashBlade;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliSlashBladeChangeSlashBladePacket implements IMessage {

	public LoliSlashBladeChangeSlashBladePacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class MessageHandler implements IMessageHandler<LoliSlashBladeChangeSlashBladePacket, IMessage> {

		@Override
		public IMessage onMessage(LoliSlashBladeChangeSlashBladePacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLoliSlashBlade) {
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt == null) {
					nbt = new NBTTagCompound();
					nbt.setInteger("RangeAttackType", 1);
					stack.setTagCompound(nbt);
				} else {
					if (nbt.hasKey("RangeAttackType")) {
						nbt.setInteger("RangeAttackType", (nbt.getInteger("RangeAttackType") + 1) % 2);
					} else {
						nbt.setInteger("RangeAttackType", 1);
					}
				}
			}
			return null;
		}
	}

}
