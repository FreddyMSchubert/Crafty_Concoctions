package com.freddy.craftyconcoctions.util;

public class MathUtil
{
    public static int clamp(int value, int min, int max)
    {
        return Math.max(min, Math.min(max, value));
    }
}
