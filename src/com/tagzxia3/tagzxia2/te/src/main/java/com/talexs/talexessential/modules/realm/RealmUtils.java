package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * Define a util that assit with dealing realms.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/22 下午 08:01
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmUtils {

    public static void main(String[] args) {

        // establish a normal test
        double totalCost = 100_00;
        long thirtyHoursAgo = DateUtil.offsetHour(DateUtil.date(), -48).getTime();

        System.out.println(10 + " level as ADMIN per test: " + getRealmBackBal(
                totalCost, 10, ADMIN_AFFORD, thirtyHoursAgo
        ));

        System.out.println(10 + " level as PRO per test: " + getRealmBackBal(
                totalCost, 10, PRO_AFFORD, thirtyHoursAgo
        ));

        System.out.println(10 + " level as Plus per test: " + getRealmBackBal(
                totalCost, 10, PLUS_AFFORD, thirtyHoursAgo
        ));

        System.out.println(10 + " level as Normal per test: " + getRealmBackBal(
                totalCost, 10, NORMAL_AFFORD, thirtyHoursAgo
        ));

    }

    public static double IN_DAYS_DECREASE_BASE_PER = 0.05, HOURS_DECREASE_BASE_PER = 0.1;

    public static double ADMIN_AFFORD = 0, PRO_AFFORD = .15, PLUS_AFFORD = .3, NORMAL_AFFORD = .5;

    /**
     * Define a method that calculate realm return back money when deleted!
     * Its has three key params: rankLevel per and created.
     * @param createdMoney Define the money that created cost
     * @param rankLevel  Define the player access rank level (tax replace)
     * @param perRate Define the player permission level (plus pro admin)
     * @param created Define the player realm created time (unit: milliseconds)
     * @return Define the realm turn back money
     */
    public static double getRealmBackBal(double createdMoney, int rankLevel, double perRate, long created) {
        long now = System.currentTimeMillis();
        long diff = now - created;

        // Obviously error
        if ( diff < 0 ) return createdMoney;

        Date createdDate = new Date(created);
        Date nowDate = new Date(now);

        // If in 24hours => all back
        if ( DateUtil.offsetHour(createdDate, 24).isAfter(nowDate) ) return createdMoney;

        // get hours that decline 2 24hours
        // 24 hours => 5% base decrease
        // offset after => 10% base decrease
        long diffHours = DateUtil.between(createdDate, nowDate, DateUnit.HOUR, true);

        long diffHoursFormatted = diffHours - 24;

        double inDaysRate = IN_DAYS_DECREASE_BASE_PER - (rankLevel * 0.00125);
        double hoursRate = HOURS_DECREASE_BASE_PER + (diffHoursFormatted * 0.00125);

        double lowestMoney = createdMoney * perRate;

        double inDaysMoney = Math.max(lowestMoney * 0.1, inDaysRate * createdMoney);
        double hoursMoney = Math.max(lowestMoney, hoursRate * createdMoney);

        double left = createdMoney - inDaysMoney - hoursMoney;

        return Math.max(left, lowestMoney);

    }

}
