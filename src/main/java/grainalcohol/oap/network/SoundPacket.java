package grainalcohol.oap.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundPacket extends AbstractPacket<SoundPacket.SoundData>{
    public static final SoundPacket INSTANCE = new SoundPacket();

    protected SoundPacket() {
        super("sound");
    }

    @Override
    protected void write(PacketByteBuf buf, SoundData data) {
        buf.writeIdentifier(data.soundId());
        buf.writeFloat(data.volume());
        buf.writeFloat(data.pitch());
    }

    public record SoundData(SoundEvent sound, float volume, float pitch){
        public Identifier soundId() {
            return sound.getId();
        }
    }
}
