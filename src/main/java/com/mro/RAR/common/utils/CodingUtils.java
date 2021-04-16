package com.mro.RAR.common.utils;

import com.mro.RAR.RARUtils;

import static com.mro.RAR.RARUtils.COMMON_RESOURCE_MANAGER;

public class CodingUtils {
    public CodingUtils() {
    }

    public static void assertStringNotNullOrEmpty(String param, String paramName) {
        assertParameterNotNull(param, paramName);
        if (param.trim().length() == 0) {
            throw new IllegalArgumentException(
                    COMMON_RESOURCE_MANAGER.getFormattedString("ParameterStringIsEmpty", paramName));
        }
    }

    public static void assertParameterNotNull(Object param, String paramName) {
        if (param == null) {
            throw new NullPointerException(COMMON_RESOURCE_MANAGER.getFormattedString("ParameterIsNull", new Object[]{paramName}));
        }
    }

    public static void assertParameterInRange(long param, long lower, long upper) {
        if (!checkParamRange(param, lower, true, upper, true)) {
            throw new IllegalArgumentException(String.format("%d not in valid range [%d, %d]", param, lower, upper));
        }
    }

    public static boolean checkParamRange(long param, long from, boolean leftInclusive, long to, boolean rightInclusive) {
        if (leftInclusive && rightInclusive) {
            return from <= param && param <= to;
        } else if (leftInclusive && !rightInclusive) {
            return from <= param && param < to;
        } else if (!leftInclusive && !rightInclusive) {
            return from < param && param < to;
        } else {
            return from < param && param <= to;
        }
    }
}
