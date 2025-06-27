package cn.grainalcohol.temp;

import cn.grainalcohol.OAPMod;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.client.toast.AdvancementToast;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Collections;

public class TempAdvancement {
    public static AdvancementToast createTempAdvancementToast(Text title, Text description, ItemStack icon, String advancementType) {
        if (icon.isEmpty()) return null;

        AdvancementFrame advancementFrame = AdvancementFrame.valueOf(advancementType.toUpperCase());

        // 创建临时的AdvancementDisplay
        AdvancementDisplay tempDisplay = new AdvancementDisplay(
                icon,
                title,
                description,
                null,
                advancementFrame,
                true,
                false,
                false
        );

        // 创建临时的Advancement
        Advancement tempAdvancement = new Advancement(
                OAPMod.id("temp_advancement_" + System.currentTimeMillis()),
                null,                           // parent
                tempDisplay,                    // display
                AdvancementRewards.NONE,        // rewards
                Collections.emptyMap(),         // criteria
                null,                          // requirements
                false                          // sendsTelemetryEvent
        );

        return new AdvancementToast(tempAdvancement);
    }
}
