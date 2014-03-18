package info.jbcs.minecraft.chisel.handlers;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.NetworkModHandler;
import info.jbcs.minecraft.chisel.Chisel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet131MapData;

public class TinyChiselPacketHandler implements ITinyPacketHandler {
    // This is going to be a pain to update;
    @Override
    public void handle(NetHandler handler, Packet131MapData mapData) {
        if (handler instanceof NetServerHandler)
            ChiselEventHandler.place(handler.getPlayer(),
                    handler.getPlayer().worldObj);
    }

    static Packet packet = null;

    public TinyChiselPacketHandler() {

    }

    public static void signalServer() {
        if (packet == null) {
            NetworkModHandler handler = FMLNetworkHandler.instance()
                    .findNetworkModHandler(Chisel.instance);
            packet = new Packet131MapData((short) handler.getNetworkId(),
                    (short) 0, new byte[]{1});
        }
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }
}
