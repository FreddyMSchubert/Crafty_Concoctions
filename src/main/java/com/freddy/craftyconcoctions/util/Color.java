package com.freddy.craftyconcoctions.util;

import com.freddy.craftyconcoctions.CraftyConcoctions;

@SuppressWarnings("unused")
public class Color
{
    public int RED;
    public int GREEN;
    public int BLUE;
    public int ALPHA;

    // ----- CONSTRUCTORS & VALUE SETTING -----

    public Color()
    {
        RED = 255;
        GREEN = 255;
        BLUE = 255;
        ALPHA = 255;
    }

    public Color(int red, int green, int blue)
    {
        RED = red;
        GREEN = green;
        BLUE = blue;
        ALPHA = 255;
    }

    public Color(int red, int green, int blue, int alpha)
    {
        RED = red;
        GREEN = green;
        BLUE = blue;
        ALPHA = alpha;
    }

    public Color copy()
    {
        return new Color(RED, GREEN, BLUE, ALPHA);
    }

    public Color alpha(int alpha)
    {
        ALPHA = alpha;
        return this;
    }
    public Color red(int red)
    {
        RED = red;
        return this;
    }
    public Color green(int green)
    {
        GREEN = green;
        return this;
    }
    public Color blue(int blue)
    {
        BLUE = blue;
        return this;
    }

    public void log()
    {
        CraftyConcoctions.LOGGER.info("Color [" + RED + "|" + GREEN + "|" + BLUE + "|" + ALPHA + "]");
    }

    // ----- COLOR OPERATIONS -----

    public void shiftColorTowardsColor(Color target, int amount)
    {
        RED = shiftColorTowardsValue(RED, target.RED, amount);
        GREEN = shiftColorTowardsValue(GREEN, target.GREEN, amount);
        BLUE = shiftColorTowardsValue(BLUE, target.BLUE, amount);
        ALPHA = shiftColorTowardsValue(ALPHA, target.ALPHA, amount);
    }
    private int shiftColorTowardsValue(int current, int target, int amount)
    {
        if (current < target)
            return Math.min(current + amount, target);
        else
            return Math.max(current - amount, target);
    }

    // ----- COLOR COMPARISON -----

    public boolean equals(Color other)
    {
        return RED == other.RED && GREEN == other.GREEN && BLUE == other.BLUE && ALPHA == other.ALPHA;
    }

    // ----- COLOR CONVERSION -----

    public int getRgbInt()
    {
        return (RED << 16) | (GREEN << 8) | BLUE;
    }

    public String getHex()
    {
        return String.format("#%02x%02x%02x", RED, GREEN, BLUE);
    }

    public String getRGBA()
    {
        return String.format("rgba(%d, %d, %d, %d)", RED, GREEN, BLUE, ALPHA);
    }
}