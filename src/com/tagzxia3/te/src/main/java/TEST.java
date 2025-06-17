package com.tagzxia3.te.src.main.java;

import com.talexs.talexessential.modules.ModuleManager;

/**
 * e
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 10:32
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class TEST {


    public static void main(String[] args) {
        int size = 13000;

        long time = System.currentTimeMillis() - (3600000 * 24);

//        System.out.println(calcVault(size, time));


//        System.out.println("-- 创建商品分类表\n" +
//                "CREATE TABLE shop_categories IF NOT EXIST (\n" +
//                "    category_id INT PRIMARY KEY,\n" +
//                "    name VARCHAR(50) NOT NULL,\n" +
//                "    symbol VARCHAR(20) NOT NULL\n" +
//                ");\n" +
//                "\n" +
//                "-- 创建商品表\n" +
//                "CREATE TABLE products IF NOT EXIST (\n" +
//                "    product_id INT PRIMARY KEY,\n" +
//                "    item_stack TEXT NOT NULL,\n" +
//                "    price DOUBLE NOT NULL,\n" +
//                "    category_id INT,\n" +
//                "    transaction_type ENUM('SELL', 'BUY') NOT NULL,\n" +
//                "    last_updated DATETIME,\n" +
//                "    inventory INT,\n" +
//                "    FOREIGN KEY (category_id) REFERENCES shop_categories(category_id)\n" +
//                ");\n" +
//                "\n" +
//                "-- 创建交易记录表\n" +
//                "CREATE TABLE transactions IF NOT EXIST (\n" +
//                "    transaction_id INT PRIMARY KEY,\n" +
//                "    product_id INT,\n" +
//                "    transaction_type ENUM('SELL', 'BUY') NOT NULL,\n" +
//                "    player_id INT,\n" +
//                "    transaction_time DATETIME,\n" +
//                "    quantity INT,\n" +
//                "    total_price DOUBLE,\n" +
//                "    FOREIGN KEY (product_id) REFERENCES products(product_id)\n" +
//                ");\n" +
//                "\n" +
//                "-- 创建购买限制表\n" +
//                "CREATE TABLE purchase_limits IF NOT EXIST (\n" +
//                "    limit_id INT PRIMARY KEY,\n" +
//                "    product_id INT,\n" +
//                "    server_wide_limit INT,\n" +
//                "    server_wide_sales_limit INT,\n" +
//                "    per_player_limit INT,\n" +
//                "    per_player_sales_limit INT,\n" +
//                "    FOREIGN KEY (product_id) REFERENCES products(product_id)\n" +
//                ");");
    }

}
