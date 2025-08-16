package grainalcohol.oap.util;

import java.util.Random;

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

    private static final Random RANDOM = new Random();
    public static double randomAroundZero(double range) {
        return RANDOM.nextDouble() * (2 * range) - range;
    }

    public static boolean randomChance(double chance) {
        if (chance <= 0) return false;
        if (chance >= 1) return true;
        return RANDOM.nextDouble() < chance;
    }

    public static int nonNegative(int number) {
        return Math.max(0, number);
    }

    public static long nonNegative(long number) {
        return Math.max(0, number);
    }

    public static float nonNegative(float number) {
        return Math.max(0.0f, number);
    }

    public static double nonNegative(double number) {
        return Math.max(0.0,number);
    }
}
