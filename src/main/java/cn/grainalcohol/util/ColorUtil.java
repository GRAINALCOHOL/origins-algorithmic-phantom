package cn.grainalcohol.util;

import net.minecraft.util.DyeColor;

public class ColorUtil {
    public static int getIntFromDyeColor(DyeColor color) {
        float[] components = color.getColorComponents();
        int r = (int)(components[0] * 255);
        int g = (int)(components[1] * 255);
        int b = (int)(components[2] * 255);

        return (r << 16) | (g << 8) | b;
    }
}
