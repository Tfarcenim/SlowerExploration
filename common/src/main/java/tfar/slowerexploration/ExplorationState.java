package tfar.slowerexploration;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum ExplorationState implements StringRepresentable {
    LOCKED("locked"),UNLOCKED("unlocked"),ENHANCED("enhanced");

    public static final Codec<ExplorationState> CODEC = StringRepresentable.fromEnum(ExplorationState::values);
    private static final IntFunction<ExplorationState> BY_ID = ByIdMap.continuous(ExplorationState::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf,ExplorationState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ExplorationState::ordinal);

    private final String name;
    ExplorationState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
