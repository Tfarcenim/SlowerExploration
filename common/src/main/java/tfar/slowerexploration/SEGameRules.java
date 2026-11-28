package tfar.slowerexploration;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;

import java.util.function.ToIntFunction;

public class SEGameRules {

    public static final GameRuleCategory CATEGORY = GameRuleCategory.register(SlowerExploration.id("slowerexploration"));

    public static final GameRule<Integer> STARTING_RADIUS = registerInteger("starting_radius", CATEGORY, 0, 0,99);

    public static final GameRule<Integer> PROGRESS_THRESHOLD = registerInteger("progress_threshold", CATEGORY, 10, 0,999999);

    private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min) {
        return registerInteger(id, category, defaultValue, min, Integer.MAX_VALUE, FeatureFlagSet.of());
    }

    private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min, int max) {
        return registerInteger(id, category, defaultValue, min, max, FeatureFlagSet.of());
    }

    private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min, int max, FeatureFlagSet requiredFeatures) {
        return register(
                id,
                category,
                GameRuleType.INT,
                IntegerArgumentType.integer(min, max),
                Codec.intRange(min, max),
                defaultValue,
                requiredFeatures,
                GameRuleTypeVisitor::visitInteger,
                i -> i
        );
    }

    private static <T> GameRule<T> register(
            String id,
            GameRuleCategory category,
            GameRuleType typeHint,
            ArgumentType<T> argumentType,
            Codec<T> codec,
            T defaultValue,
            FeatureFlagSet requiredFeatures,
            GameRules.VisitorCaller<T> visitorCaller,
            ToIntFunction<T> commandResultFunction
    ) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE,
                SlowerExploration.id(id),
                new GameRule<>(category, typeHint, argumentType, visitorCaller, codec, commandResultFunction, defaultValue, requiredFeatures)
        );
    }

    public static void init(){}

}
