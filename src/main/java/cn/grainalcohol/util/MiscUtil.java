package cn.grainalcohol.util;

import io.github.apace100.apoli.power.MultiplePowerType;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import toni.immersivemessages.api.ImmersiveMessage;
import toni.immersivemessages.api.TextAnchor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MiscUtil {
    /**
     * 将字符串转换为TextAnchor枚举值
     * @param str 要转换的字符串，支持的取值包括：
     *                  CENTER_CENTER, CENTER_LEFT, CENTER_RIGHT,
     *                  BOTTOM_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT,
     *                  TOP_CENTER, TOP_LEFT, TOP_RIGHT
     * @return 对应的TextAnchor枚举值，如果字符串无效则返回CENTER_CENTER作为默认值
     * @see TextAnchor
     */
    public static TextAnchor tryGetAnchorFromString(String str) {
        TextAnchor anchor;
        try {
            anchor = TextAnchor.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            anchor = TextAnchor.CENTER_CENTER; // 默认值
        }
        return anchor;
    }

    public static void sendToPlayer(ImmersiveMessage message, Entity entity, boolean autoFix) {
        if (entity instanceof ServerPlayerEntity serverPlayer){
            if (autoFix) {
                message.y(-1f); //自动修正位置
            }
            message.sendServer(serverPlayer);
        }
    }

    public static int getIntFromDyeColor(DyeColor color) {
        float[] components = color.getColorComponents();
        int r = (int)(components[0] * 255);
        int g = (int)(components[1] * 255);
        int b = (int)(components[2] * 255);

        return (r << 16) | (g << 8) | b;
    }

    public static boolean matchesPowerId(PowerType<?> powerType, Collection<Identifier> matches, LivingEntity entity) {
        Identifier targetId = powerType.getIdentifier();

        // 直接尝试匹配
        if (matches.contains(targetId)) {
            return true;
        }

        if (powerType instanceof MultiplePowerType) {
            // 多重能力
            String Prefix = targetId.toString() + "_";
            return ((MultiplePowerType<?>)powerType).getSubPowers().stream()
                    // 拼接完整子能力ID
                    .map(subId -> Prefix + subId.toString())
                    .anyMatch(fullId -> matches.contains(new Identifier(fullId)));
        } else {
            // 子能力
            Set<Identifier> allIds = EntityUtil.getAllPowerIds(entity);
            String currentId = targetId.toString();

            // 找出所有可能的父能力ID
            Set<String> possibleParents = allIds.stream()
                    .map(Identifier::toString)
                    // 找出所有比当前ID短且是当前ID前缀的ID
                    .filter(id -> id.length() < currentId.length() && currentId.startsWith(id + "_"))
                    // 进一步验证这些前缀是否是真正的父能力ID
                    .filter(parentCandidate ->
                            allIds.stream()
                                    .map(Identifier::toString)
                                    // 统计有多少个ID以此前缀开头
                                    .filter(id -> id.startsWith(parentCandidate + "_"))
                                    .count() >= 2) // 确保至少有2个子能力
                    .collect(Collectors.toSet());

            // 检查这些可能的父能力ID是否在匹配集合中
            return possibleParents.stream()
                    .map(Identifier::new)
                    .anyMatch(matches::contains);
        }
    }
}
