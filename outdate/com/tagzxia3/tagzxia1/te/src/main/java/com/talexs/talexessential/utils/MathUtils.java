package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.utils;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Random;

public class MathUtils {

    public static Boolean Chance(int chance) {

        return Math.random() * 100 < chance;
    }

    public static String Format(Integer i) {

        return String.format("%,d", i);
    }

    public static String toRome(int number) {

        StringBuilder rNumber = new StringBuilder();
        int[] aArray = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        String[] rArray = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X",
                "IX", "V", "IV", "I" };
        if ( number < 1 || number > 3999 ) {
            rNumber = new StringBuilder("-1");
        } else {
            for ( int i = 0; i < aArray.length; i++ ) {
                while ( number >= aArray[i] ) {
                    rNumber.append(rArray[i]);
                    number -= aArray[i];
                }
            }
        }
        return rNumber.toString();
    }

    public static String getPercent(Integer num, Integer totalPeople) {

        String percent;
        Double p3;
        if ( totalPeople == 0 ) {
            p3 = 0.0;
        } else {
            p3 = num * 1.0 / totalPeople;
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);//控制保留小数点后几位，2：表示保留2位小数点
        percent = nf.format(p3);
        return percent;
    }

    public static String CalculateUtil(BigDecimal a, BigDecimal b) {

        String percent =
                b == null ? "-" :
                        b.compareTo(new BigDecimal(0)) == 0 ? "-" :
                                a == null ? "0.00%" :
                                        a.multiply(new BigDecimal(100)).divide(b, 2, BigDecimal.ROUND_HALF_UP) + "%";
        return percent;
    }

    public static double randomRate(String text, double baseNumber) {
        Random rand = new Random();

        if (text.matches("([0-9]+%|[0-9]+\\.[0-9]+|[0-9]+\\.[0-9]+%|[0-9]+)-([0-9]+%|[0-9]+\\.[0-9]+|[0-9]+\\.[0-9]+%|[0-9]+)")) {
            String[] range = text.split("-");

            // if range contains % return percentage random number
            if (range[0].contains("%") || range[1].contains("%")) {
                double min = range[0].contains("%") ? Double.parseDouble(range[0].replace("%", "")) / 100 : Double.parseDouble(range[0]);
                double max = range[1].contains("%") ? Double.parseDouble(range[1].replace("%", "")) / 100 : Double.parseDouble(range[1]);
                return min + (max - min) * baseNumber * rand.nextDouble() / 100;
            } else {
                return Double.parseDouble(range[0]) + (Double.parseDouble(range[1]) - Double.parseDouble(range[0])) * baseNumber / 100 * rand.nextDouble();
            }

//            double min = range[0].contains("%") ? Double.parseDouble(range[0]) / 100 : Double.parseDouble(range[0]);
//            double max = range[1].contains("%") ? Double.parseDouble(range[1]) / 100 : Double.parseDouble(range[1]);
//            return min + (max - min) * rand.nextDouble();
        } else {
            return Double.parseDouble(text) * baseNumber * rand.nextDouble() / 100;
        }

    }

    public static void main(String[] args) {
        System.out.println(randomRate("1%-20%", 100));
    }

}
