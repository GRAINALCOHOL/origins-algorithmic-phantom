package cn.grainalcohol.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityUtil {
    public static boolean isTeamMember(Entity entity, Entity target)
    {
        if (entity.isTeammate(target)) return true;
        Entity superiorEntity = getSuperiorEntity(entity);
        Entity superiorTarget = getSuperiorEntity(target);
        if (superiorEntity == superiorTarget) return true;
        AbstractTeam thisTeam = superiorEntity.getScoreboardTeam();
        AbstractTeam targetTeam = superiorTarget.getScoreboardTeam();
        if (thisTeam == null || targetTeam == null) return false;
        return thisTeam.isEqual(targetTeam);
    }

    @NotNull
    public static Entity getSuperiorEntity(@NotNull Entity entity)
    {
        if (entity instanceof Tameable tameable)
        {
            Entity owner = tameable.getOwner();
            if (owner != null) return owner;
        }
        if (entity instanceof Ownable ownable)
        {
            Entity owner = ownable.getOwner();
            if (owner != null) return owner;
        }
        return entity;
    }

    @Nullable
    public static EntityAttribute getAttribute(String attributeId) {
        Identifier id = Identifier.tryParse(attributeId);
        if (id == null) return null;
        return Registries.ATTRIBUTE.get(id);
    }
}
