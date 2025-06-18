package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.shop.entity;

import cn.hutool.db.*;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.item.ItemUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Define a shop good product.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 07:10
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@Data
public class ShopProduct {

    private int productId;
    private ItemStack itemStack;
    private double price;
    private int categoryId;
    private ProductType transactionType;
    private Date lastUpdated;
    private int inventory, version, delete, rank;

    public ShopProduct() {

    }

    public ShopProduct(int productId, ItemStack itemStack, double price, int categoryId, ProductType transactionType, Date lastUpdated, int inventory, int version, int delete, int rank) {
        this.productId = productId;
        this.itemStack = itemStack;
        this.price = price;
        this.categoryId = categoryId;
        this.transactionType = transactionType;
        this.lastUpdated = lastUpdated;
        this.inventory = inventory;
        this.version = version;
        this.delete = delete;
        this.rank = rank;
    }

    public static ShopProduct defaultProduct() {
        ShopProduct product = new ShopProduct();
        product.setProductId(0);
        product.setItemStack(null);
        product.setPrice(0);
        product.setCategoryId(0);
        product.setTransactionType(ProductType.BUY);
        product.setLastUpdated(new Date());
        product.setInventory(0);
        product.setVersion(0);
        product.setRank(0);
        product.setDelete(0);
        return product;
    }

    private static final String UPSERT_PRODUCT = "INSERT INTO products " +
            "(product_id, item_stack, price, category_id, transaction_type, last_updated, inventory, version, rank, delete) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "item_stack = VALUES(item_stack), " +
            "price = VALUES(price), " +
            "category_id = VALUES(category_id), " +
            "transaction_type = VALUES(transaction_type), " +
            "last_updated = VALUES(last_updated), " +
            "inventory = VALUES(inventory)" +
            "version = VALUES(version)" +
            "rank = VALUES(rank)" +
            "delete = VALUES(delete)";

    @SneakyThrows
    public void update() {

        PreparedStatement preparedStatement =TalexEssential.getInstance().getMySQLSource().ps(UPSERT_PRODUCT);

        preparedStatement.setInt(1, this.getProductId());
        preparedStatement.setString(2, ItemUtil.item2Str(this.getItemStack()));
        preparedStatement.setDouble(3, this.getPrice());
        preparedStatement.setInt(4, this.getCategoryId());
        preparedStatement.setString(5, this.getTransactionType().name());
        preparedStatement.setTimestamp(6, new java.sql.Timestamp(this.getLastUpdated().getTime()));
        preparedStatement.setInt(7, this.getInventory());
        preparedStatement.setInt(8, this.getVersion());
        preparedStatement.setInt(9, this.getRank());
        preparedStatement.setInt(10, this.getDelete());

        preparedStatement.executeUpdate();

    }

    @SneakyThrows
    public void delete() {

        TalexEssential.getInstance().getMySQLSource().db().update(
                Entity.create().set("delete", 1),
                Entity.create("products").set("product_id", this.getProductId())
        );

//        TalexEssential.getInstance().getMySQLSource().db().del(
//                Entity.create("products").set("product_id", this.getProductId()));

    }

    @SneakyThrows
    public static long getProductSize(ProductType type) {
        Db db = TalexEssential.getInstance().getMySQLSource().db();

        return db.count(Entity.create("products").set("transaction_type", type.name()));
    }

    @SneakyThrows
    public static List<ShopProduct> getPlayerProducts(ProductType productType, int rank) {
        List<ShopProduct> categories = new ArrayList<>();

        Db db = TalexEssential.getInstance().getMySQLSource().db();

        // 获取玩家可使用的，条件如下
        // 1. productType 对应
        // 2. rank >= 参数rank
        // 3. inventory 库存应为 -1(无限) 或 >0

        String sql = "SELECT * FROM products WHERE transaction_type = ? AND rank >= ? AND (inventory = -1 OR inventory > 0)";

        List<Entity> result = db.query(
                sql, productType.name(), rank
        );

        deserializeFromEntity(result, categories);

        return categories;
    }

    @SneakyThrows
    public static List<ShopProduct> getAllProducts(ProductType productType) {
        List<ShopProduct> categories = new ArrayList<>();

        Db db = TalexEssential.getInstance().getMySQLSource().db();

        List<Entity> result = db.findAll(
                Entity.create("products").set("transaction_type", productType.name())
        );

        deserializeFromEntity(result, categories);

        return categories;
    }

    private static void deserializeFromEntity(List<Entity> result, List<ShopProduct> categories) {
        for (Entity shopCategory : result) {

            ShopProduct sp = new ShopProduct();

            sp.productId = shopCategory.getInt("product_id");
            sp.itemStack = NBTsUtil.GetItemStack(shopCategory.getStr("item_stack"));
            sp.price = shopCategory.getDouble("price");
            sp.categoryId = shopCategory.getInt("category_id");
            sp.transactionType = ProductType.valueOf(shopCategory.getStr("transaction_type"));
            sp.lastUpdated = shopCategory.getDate("last_updated");
            sp.inventory = shopCategory.getInt("inventory");
            sp.version = shopCategory.getInt("version");
            sp.rank = shopCategory.getInt("rank");
            sp.delete = shopCategory.getInt("delete");

            categories.add(sp);
        }
    }

    @SneakyThrows
    public static List<ShopProduct> getProducts(ProductType productType, int page) {
        List<ShopProduct> categories = new ArrayList<>();

        Db db = TalexEssential.getInstance().getMySQLSource().db();

        PageResult<Entity> result = db.page(
                Entity.create("products").set("transaction_type", productType),
                new Page(page, 28)
        );

        deserializeFromEntity(result, categories);

        return categories;
    }

    public static enum ProductType {
        BUY(), SELL();
    }

}
