package tfar.slowerexploration.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import tfar.slowerexploration.ExplorationState;
import tfar.slowerexploration.platform.Services;


import java.util.*;

public class CommonDataAttachments {

    private static final Map<Identifier,CommonDataAttachment<?>> MAP =new HashMap<>();

    public static final CommonDataAttachment<ExplorationState> EXPLORATION_STATE =
            register(CommonDataAttachment.create(o -> ExplorationState.LOCKED)
                    .networkSynchronized(ExplorationState.STREAM_CODEC)
                    .codec(ExplorationState.CODEC)
                    .build("exploration_state"));

    public static final CommonDataAttachment<Integer> EXPLORATION_PROGRESS =
            register(CommonDataAttachment.create(o -> 0)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .codec(Codec.INT)
                    .build("exploration_progress"));

    public static CommonDataAttachment<?> lookup(Identifier location) {
        return MAP.get(location);
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.PLATFORM.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.name,type);
        return type;
    }

    public static void init() {

    }
}