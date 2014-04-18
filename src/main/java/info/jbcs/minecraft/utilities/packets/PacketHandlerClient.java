package info.jbcs.minecraft.utilities.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandlerClient {
	public static void send(Packet250CustomPayload packet){
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
	}
}
