package cn.grainalcohol.init;

import cn.grainalcohol.OAPMod;
import cn.grainalcohol.condition.bientity.IsTeamMemberCondition;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class OAPBientityConditionType {
    public static void init() {
        Registry.register(
                ApoliRegistries.BIENTITY_CONDITION,
                IS_TEAM_MEMBER.getSerializerId(),
                IS_TEAM_MEMBER
        );
    }

    public static final ConditionFactory<Pair<Entity, Entity>> IS_TEAM_MEMBER =
            new ConditionFactory<>(
                    OAPMod.id("is_team_member"),
                    IsTeamMemberCondition.DATA,
                    new IsTeamMemberCondition()
            );
}
