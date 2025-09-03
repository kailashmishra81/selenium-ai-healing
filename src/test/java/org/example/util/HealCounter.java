package org.example.util;

public class HealCounter {
    private static int healedcount=0;
    public static void HealCount()
    {
        healedcount++;
    }
    public static int getHealedLocators() {
        return healedcount;
    }
}
