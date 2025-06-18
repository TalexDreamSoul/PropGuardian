package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.shop.entity;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Session;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.MySQLSource;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Define a shop good category. (Filter)
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 06:44
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@Data
public class GoodCategory {

    private final int id;

    private String name;

    private Material symbol;

    @SneakyThrows
    public int update() {
        Db db = TalexEssential.getInstance().getMySQLSource().db();

        return db.update(
                Entity.create().set("name", name).set("symbol", symbol),
                Entity.create("shop_categories").set("category_id", id)
        );
    }

    @SneakyThrows
    public int delete() {
        Db db = TalexEssential.getInstance().getMySQLSource().db();

        return db.del(Entity.create("shop_categories").set("category_id", id));
    }

    public static GoodCategory deserialize(int id, String name, String symbol) {
        GoodCategory goodCategory = new GoodCategory(id);
        goodCategory.setName(name);
        goodCategory.setSymbol(Material.getMaterial(symbol));
        return goodCategory;
    }

    @SneakyThrows
    public static List<GoodCategory> getAllCategories() {
        List<GoodCategory> categories = new ArrayList<>();

        Session session = TalexEssential.getInstance().getMySQLSource().session();

        List<Entity> shopCategories = session.find(Entity.create("shop_categories"));

        for (Entity shopCategory : shopCategories) {
            categories.add(GoodCategory.deserialize(shopCategory.getInt("category_id"), shopCategory.getStr("name"), shopCategory.getStr("symbol")));
        }

        return categories;
    }

    @SneakyThrows
    public static int createCategory(String name, Material symbol) {
        Db db = TalexEssential.getInstance().getMySQLSource().db();

        return db.insert(Entity.create("shop_categories").set("category_id", 0).set("name", name).set("symbol", symbol.name()));
    }

}
