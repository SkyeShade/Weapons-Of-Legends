package net.skyeshade.wol.util;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class LongHudFormatter {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "K");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "B");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "q");
        suffixes.put(1_000_000_000_000_000_000L, "Q");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();
        //System.out.println(value);
        long truncated = (value / (divideBy / 10))*10; //the number part of the output times 10
        //boolean hasDecimal = truncated < 10000 && (truncated / 100d) != (truncated / 100);
        //return hasDecimal ? (truncated / 100d) + suffix : (truncated / 100) + suffix;
        return (truncated / 100D) + suffix;
    }
}
