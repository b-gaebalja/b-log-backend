package com.bgaebalja.blogbackend.util;

public class PerformanceMeasurer {
    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static long computeElapsedTime(long startedAt) {
        return System.currentTimeMillis() - startedAt;
    }

    public static long computeUsedMemory(long beforeMemory) {
        return RUNTIME.totalMemory() - RUNTIME.freeMemory() - beforeMemory;
    }
}
