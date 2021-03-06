package info.jbcs.minecraft.chisel.handlers;

import cpw.mods.fml.common.FMLCommonHandler;
import info.jbcs.minecraft.chisel.core.CarvingRegistry;
import info.jbcs.minecraft.chisel.util.GeneralChiselClient;
import info.jbcs.minecraft.utilities.General;
import info.jbcs.minecraft.utilities.packets.PacketData;
import info.jbcs.minecraft.utilities.packets.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packets {

    public static PacketHandler chiseled = new PacketHandler("A block has been chiseled") {
        @Override
        public void onData(DataInputStream stream, EntityPlayer player) throws IOException {
            final int x = stream.readInt();
            final int y = stream.readInt();
            final int z = stream.readInt();
            int blockId = player.worldObj.getBlockId(x, y, z);
            int blockMeta = player.worldObj.getBlockMetadata(x, y, z);
            switch (FMLCommonHandler.instance().getEffectiveSide()) {
                case SERVER:
                    ServerConfigurationManager mgr = MinecraftServer.getServer().getConfigurationManager();
                    for (int j = 0; j < mgr.playerEntityList.size(); ++j) {
                        EntityPlayerMP p = (EntityPlayerMP) mgr.playerEntityList.get(j);

                        if (p.dimension != player.dimension) continue;
                        if (p == player) continue;
                        if (!General.isInRange(30.0f, p.posX, p.posY, p.posZ, x, y, z)) continue;

                        sendToPlayer(p, new PacketData() {
                            @Override
                            public void data(DataOutputStream stream) throws IOException {
                                stream.writeInt(x);
                                stream.writeInt(y);
                                stream.writeInt(z);
                            }
                        });
                    }

                    break;
                case CLIENT:
                    GeneralChiselClient.spawnChiselEffect(x, y, z, CarvingRegistry.chisel.getVariationSound(blockId, blockMeta));
                    break;
                default:
                    break;
            }

        }
    };

}
