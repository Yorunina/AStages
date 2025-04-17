package com.alessandro.astages.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATime {
    private final String timeString;

    public ATime(String timeString) {
        this.timeString = timeString;
    }

    public int getTicks() {
        /*
            \\d+ captures one or more digits.
            \\s* allows optional spaces between the number and the unit.
            (h|m|s) captures the time unit
         */

        Pattern pattern = Pattern.compile("(\\d+)\\s*([hms])");
        Matcher matcher = pattern.matcher(timeString);
        int totalTicks = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "s":
                    totalTicks += value * 20;
                    break;
                case "m":
                    totalTicks += value * 1200;
                    break; // 1 minute -> 16.666666 ticks
                case "h":
                    totalTicks += value * 72000;
                    break; // 1 hour -> 1000 ticks;
            }
        }

        return totalTicks;
    }
}
