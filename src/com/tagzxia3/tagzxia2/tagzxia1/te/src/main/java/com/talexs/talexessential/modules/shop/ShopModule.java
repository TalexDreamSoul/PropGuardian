package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.shop;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.BuyShopMenu;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.SellShopMenu;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.ShopSlot;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.modules.shop.entity.GoodCategory;
import com.talexs.talexessential.modules.shop.sell.SellCategory;
import com.talexs.talexessential.utils.LogUtil;
import lombok.SneakyThrows;
import org.bukkit.Material;

import java.util.List;

public class ShopModule extends BaseModule {

    private static List<String> sells, buys;

    public ShopModule() {
        super("shops");
    }

    @SneakyThrows
    private void initTables() {

        String sql =
                "-- 创建商品分类表\n" +
                        "CREATE TABLE IF NOT EXISTS shop_categories (\n" +
                        "    category_id INT PRIMARY KEY,\n" +
                        "    name VARCHAR(50) NOT NULL,\n" +
                        "    symbol VARCHAR(20) NOT NULL\n" +
                        ");\n" +
                        "\n" +
                        "-- 创建商品表\n" +
                        "CREATE TABLE IF NOT EXISTS products (\n" +
                        "    product_id INT PRIMARY KEY,\n" +
                        "    item_stack TEXT NOT NULL,\n" +
                        "    price DOUBLE NOT NULL,\n" +
                        "    category_id INT,\n" +
                        "    transaction_type ENUM('SELL', 'BUY') NOT NULL,\n" +
                        "    last_updated DATETIME,\n" +
                        "    inventory INT,\n" +
                        "    version INT,\n" +
                        "    `rank` INT,\n" +
                        "    `delete` INT,\n" +
                        "    FOREIGN KEY (category_id) REFERENCES shop_categories(category_id)\n" +
                        ");\n" +
                        "\n" +
                        "-- 创建交易记录表\n" +
                        "CREATE TABLE IF NOT EXISTS transactions (\n" +
                        "    transaction_id INT PRIMARY KEY,\n" +
                        "    product_id INT,\n" +
                        "    transaction_type ENUM('SELL', 'BUY') NOT NULL,\n" +
                        "    player_id INT,\n" +
                        "    transaction_time DATETIME,\n" +
                        "    quantity INT,\n" +
                        "    total_price DOUBLE,\n" +
                        "    FOREIGN KEY (product_id) REFERENCES products(product_id)\n" +
                        ");\n" +
                        "\n" +
                        "-- 创建购买限制表\n" +
                        "CREATE TABLE IF NOT EXISTS purchase_limits (\n" +
                        "    limit_id INT PRIMARY KEY,\n" +
                        "    product_id INT,\n" +
                        "    server_wide_limit INT,\n" +
                        "    server_wide_sales_limit INT,\n" +
                        "    per_player_limit INT,\n" +
                        "    per_player_sales_limit INT,\n" +
                        "    FOREIGN KEY (product_id) REFERENCES products(product_id)\n" +
                        ");";

        try {

            TalexEssential.getInstance().getMySQLSource().db().execute(sql);

        } catch ( Exception e ) {

            LogUtil.log("§c商店数据库初始化失败: " + e.getMessage());
            LogUtil.log("§e请自行执行: " + sql);

            e.printStackTrace();

        }

        if (GoodCategory.getAllCategories().isEmpty()) {

            GoodCategory.createCategory("默认", Material.BEDROCK);

        }

    }

    private void load() {

        SellShopMenu.shopSlots.clear();

        try {

            sells.forEach(str -> SellShopMenu.shopSlots.add(process(str)));
            buys.forEach(str -> BuyShopMenu.shopSlots.add(process(str)));

        } catch ( Exception e ) {

            e.printStackTrace();
            TalexEssential.getInstance().log("§c商店配置文件出错: " + e.getMessage());

        }

    }

    private ShopSlot process(String str) {
        String[] split = str.split(":");
        String name = split[0];
        String type = split[2];
        double originalPrice = Double.parseDouble(split[1]);

        SellCategory category = SellCategory.valueOf(type.toUpperCase());

        Material material = Material.getMaterial(name.toUpperCase());

        if ( material == null )
            throw new RuntimeException("Material not found: " + name.toUpperCase());

        return new ShopSlot(originalPrice, material, category);
    }

    @Override
    public void onEnable() {
        sells = super.yaml.getStringList("Settings.sells");
        buys = super.yaml.getStringList("Settings.buys");
        this.load();

        this.initTables();
    }
}
