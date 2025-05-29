package cn.grainalcohol.util;

import toni.immersivemessages.api.TextAnchor;

public class DataUtil {
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
}
