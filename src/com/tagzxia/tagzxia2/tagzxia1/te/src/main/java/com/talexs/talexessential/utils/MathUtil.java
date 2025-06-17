package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class MathUtil {

    /**
     * 获取椭圆上坐标的集合
     *
     * @param center 中心点
     * @param a      半轴
     * @param b      半轴
     *
     * @return 返回坐标集合
     */
    public static Set<Location> findOval(Location center, double a, double b) {

        Set<Location> locationSet = new HashSet<>();
        for ( int degree = 0; degree < 360; degree++ ) {
            Location cloner = center.clone();
            double k = Math.abs(Math.tan(Math.toRadians(degree)));
            double x = Math.abs(a * b) / ( Math.sqrt(b * b + k * k * a * a) );
            double y = k * x;
            //或许这段代码太麻烦了，不过这样可能看起来好一点 :-)
            if ( degree <= 90 ) {
                //第一象限
                x = x;
                y = y;
            } else if ( degree <= 180 ) {
                //第二象限
                x = -x;
                y = y;
            } else if ( degree <= 270 ) {
                //第三象限
                x = -x;
                y = -y;
            } else {
                //第四象限
                x = x;
                y = -y;
            }
            cloner.add(x, 0D, y);
            locationSet.add(cloner);
        }
        return locationSet;
    }

}
