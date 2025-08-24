package com.alessandro.astages.util;

import com.alessandro.astages.api.nullability.NotNullParams;

import java.util.Collection;

@NotNullParams
public class AMath {
    @SuppressWarnings("All")
    private static int gcd(int x, int y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static int gcd(Collection<Integer> numbers) {
        return numbers.stream().reduce(0, AMath::gcd);
    }

    public static int lcm(Collection<Integer> numbers) {
        return numbers.stream().reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }
}
