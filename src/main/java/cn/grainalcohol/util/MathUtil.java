package cn.grainalcohol.util;

public class MathUtil {
    public static <T extends Number & Comparable<T>> T clamp(T min, T max, T target) {
        if (target.compareTo(min) < 0) {
            return min;
        }
        if (target.compareTo(max) > 0) {
            return max;
        }
        return target;
    }
}
