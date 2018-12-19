package me.crystal.runescape;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * @author Sven Olderaan (admin@heaven-craft.net)
 */
public class Calculator {

    //Constants
    private static final float WELL_MULTIPLIER = 1.05f;
    private static final float CLEANSING_MULTIPLIER = 1.13f;
    private static final int K = 1000;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Calculator: (1) Perfect Juju Prayer, (2) Juju Fishing");
        System.out.print("Make your choice: ");
        System.out.flush();
        String choice = in.nextLine();
        System.out.print("Amount of herbs: ");
        System.out.flush();
        int herbs = Integer.parseInt(in.nextLine());

        if (choice.equals("1")) {
            calcPerfectJujuPrayer(true, true, herbs);
        }
        if (choice.equals("2")) {
            calcJujuFishing(true, true, herbs);
        }
    }

    private static void calcPerfectJujuPrayer(boolean well, boolean cleansing, int herbs) {
        GrandExchangeClient grandExchangeClient = new GrandExchangeClient();
        int potionPrice = grandExchangeClient.getItemPrice(Item.PERFECT_JUJU_PRAYER_3);
        int mossPrice = grandExchangeClient.getItemPrice(Item.HARMONY_MOSS);

        float output = calculatePotionOutput(herbs, 2, well, cleansing);
        float outputValue = potionPrice * output;
        int mossUsed = calculateMossUsage(output, well, cleansing);
        int mossValue = mossUsed * mossPrice;

        float profit = outputValue - mossValue;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_UP);

        System.out.println("For " + herbs + " samaden herbs, the output would be: " + df.format(output) + " Perfect juju prayer potion (3)");
        System.out.println("GE Price (each): " + potionPrice + ", with a total value of: " + df.format(outputValue / K) + "k");
        System.out.println("Moss used: " + mossUsed + ", total cost of " + df.format(mossValue / K) + "k (" + mossPrice + " ea)");
        System.out.println("Profit per herb: " + df.format(profit / herbs / K) + "k");
        System.out.println("Profit total: " + df.format(profit / K) + "k");
    }

    private static void calcJujuFishing(boolean well, boolean cleansing, int herbs) {
        GrandExchangeClient grandExchangeClient = new GrandExchangeClient();
        int potionPrice = grandExchangeClient.getItemPrice(Item.JUJU_FISHING_4);

        float output = calculatePotionOutput(herbs, 1, well, cleansing);
        float outputIn4dose = output * 0.75f; //conversion from 3 to 4-dose
        float outputValue = outputIn4dose * potionPrice;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_UP);

        System.out.println("For " + herbs + " shengo herbs, the output would be: " + df.format(output) + " Juju fishing potion (3)");
        System.out.println("Decanting potions to 4-dose yields: " + df.format(outputIn4dose) + " potions");
        System.out.println("GE Price (each): " + potionPrice + ", with a total value of: " + df.format(outputValue / K) + "k");
        System.out.println("Profit per herb: " + df.format(outputValue / herbs / K) + "k");
        System.out.println("Profit total: " + df.format(outputValue / K) + "k");
    }

    private static int calculateMossUsage(float potionsMade, boolean usingWell, boolean usingCleansing) {
        float moss = potionsMade;
        if (usingWell) {
            moss /= WELL_MULTIPLIER;
        }
        if (usingCleansing) {
            moss /= CLEANSING_MULTIPLIER;
        }

        return Math.round(moss);
    }


    /**
     * Calculates the bonuses from herblore
     *
     * @param herbIn         The amount of herbs you start with
     * @param afterHerbSteps The amount of steps after adding herbs (typically 1)
     * @param usingWell      If using a well
     * @param usingCleansing If you have scroll of cleansing
     * @return Average output
     */
    private static float calculatePotionOutput(int herbIn, int afterHerbSteps, boolean usingWell, boolean usingCleansing) {


        float potions = (usingCleansing) ? herbIn * CLEANSING_MULTIPLIER : herbIn;
        if (usingWell) {
            for (int i = 0; i <= afterHerbSteps; i++) {
                potions *= WELL_MULTIPLIER;
            }
        }

        return potions;
    }
}
