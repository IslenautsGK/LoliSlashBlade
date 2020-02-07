package com.anotherstar.slashblade.network;

import com.anotherstar.slashblade.LoliSlashBlade;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public enum NetworkHandler {

	INSTANCE;

	private final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(LoliSlashBlade.MODID);

	private NetworkHandler() {
		int index = 0;
		this.channel.registerMessage(LoliSlashBladeChangeRangePacket.MessageHandler.class,
				LoliSlashBladeChangeRangePacket.class, index++, Side.SERVER);
		this.channel.registerMessage(LoliSlashBladeChangeSlashBladePacket.MessageHandler.class,
				LoliSlashBladeChangeSlashBladePacket.class, index++, Side.SERVER);
	}

	public void sendMessageToDim(IMessage msg, int dim) {
		channel.sendToDimension(msg, dim);
	}

	public void sendMessageAroundPos(IMessage msg, int dim, BlockPos pos) {
		channel.sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 2.0D));
	}

	public void sendMessageToPlayer(IMessage msg, EntityPlayerMP player) {
		channel.sendTo(msg, player);
	}

	public void sendMessageToAll(IMessage msg) {
		channel.sendToAll(msg);
	}

	public void sendMessageToServer(IMessage msg) {
		channel.sendToServer(msg);
	}

}
