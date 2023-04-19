package com.qc.competition.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 06/11/2015.
 */
public class MathUtils {
    public static boolean isPrime(int n) {
        //check if n is a multiple of 2
        if (n % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    public static long gcd(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = gcd(result, input[i]);
        return result;
    }

    public static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    public static long lcm(Long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    public static List<Integer> getPrimeNumbers(int min, int max) {
        List<Integer> primeNumbers = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            int counter = 0;
            for (int num = i; num >= 1; num--) {
                if (i % num == 0) {
                    counter = counter + 1;
                }
            }
            if (counter == 2) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    public static int factorial(int n, int m) {
        if (n == 1 || n < m) {
            return 1;
        }
        return n * factorial(n - 1, m);
    }

    public static int getPointBonus(Integer round) {
        if (round <= 1)
            return 0;
        return fibonacci(round + 1);
    }

    public static int getMultiplicatorBonus(Integer lane, Integer maxlane) {
        if (lane <= 1)
            return 1;
        return (int) Math.round(Math.pow(2, maxlane - lane));
    }

    public static int getPointMalus(Integer round) {
        if (round <= 1)
            return 0;
        return -fibonacci(round + 1);
    }

    private static int fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static int getRankingPoints(int rank, int numberOfPlayers) {
        double points = 10 * (Math.sqrt(numberOfPlayers) / Math.sqrt(rank)) * Math.log10(10 + numberOfPlayers * (numberOfPlayers - rank + 1));
        int pointsRounded = (int) (Math.rint(points / 10.0));
        return pointsRounded;
    }

}
