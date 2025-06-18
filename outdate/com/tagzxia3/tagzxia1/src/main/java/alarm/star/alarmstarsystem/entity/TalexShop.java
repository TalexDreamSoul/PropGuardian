package com.tagzxia3.tagzxia1.src.main.java.alarm.star.alarmstarsystem.entity;

import alarm.star.alarmstarsystem.config.TalexShopConfig;
import alarm.star.alarmstarsystem.utils.MathUtils;
import alarm.star.alarmstarsystem.utils.StringUtil;
import alarm.star.alarmstarsystem.utils.item.ItemUtil;
import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
public class TalexShop {

    private final Map<Integer, ShopGood> shopGoods = new HashMap<>();

    private final String shopOwner;

    @Setter
    private double shopBalance;

    @Setter
    private int type = 0;

    public TalexShop addBalance(double amount) {
        this.shopBalance = new BigDecimal(amount).add(new BigDecimal(this.shopBalance)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return this;
    }

    private TalexShop(String shopOwner, double shopBalance) {
        this.shopOwner = shopOwner;
        this.shopBalance = shopBalance;

        TalexShop shop = TalexShopConfig.shops.get(shopOwner);
        if ( shop != null ) {
            throw new RuntimeException("Shop already exists!");
//            TalexShopConfig.saveShop(shop);
        }

        TalexShopConfig.shops.put(shopOwner.toLowerCase(), this);
    }

    public static TalexShop create(String shopOwner, double shopBalance) {
        return new TalexShop(shopOwner, shopBalance);
    }

    public static TalexShop deserialize(ConfigurationSection yaml) {
        String shopOwner = yaml.getString("Manifest.shopOwner", null);
        double shopBalance = yaml.getDouble("Manifest.shopBalance", 0);
        int type = yaml.getInt("Manifest.type", 0);

        TalexShop shop = TalexShop.create(shopOwner, shopBalance);

        shop.type = type;

        if ( !yaml.contains("Shop.Goods") ) return shop;

        Set<String> keys = Objects.requireNonNull(yaml.getConfigurationSection("Shop.Goods")).getKeys(false);
        if ( keys.size() > 0 ) {
            for ( String key : keys ) {
                String path = "Shop.Goods." + key;

                int slot = yaml.getInt(path + ".slot", -1);
                if ( slot == -1 ) continue;

                String goodStr = yaml.getString(path + ".value", "");

                ShopGood good = ShopGood.deserialize(shop, goodStr);

                shop.shopGoods.put(slot, good);
            }
        }

        return shop;
    }

    public YamlConfiguration serialize() {
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.set("Manifest.type", this.type);
        yaml.set("Manifest.shopOwner", this.shopOwner);
        yaml.set("Manifest.shopBalance", this.shopBalance);

        for ( Map.Entry<Integer, ShopGood> entry : this.shopGoods.entrySet() ) {
            ShopGood good = entry.getValue();
            if ( good == null ) continue;

            String path = "Shop.Goods." + StringUtil.genRandomStr(8);
            yaml.set(path + ".slot", entry.getKey());
            yaml.set(path + ".value", good.serialize());
        }

//         this.shopGoods.forEach((key, good) -> {
//            String path = "Shop.Goods." + key;
//
//            yaml.set(path, good.serialize());
//        });

        return yaml;
    }

    @Getter
    public static class ShopGood {

            private final TalexShop ownerShop;

            private final ItemStack goodItem;

            private final double price;

            private final int amount;

            public ShopGood(TalexShop ownerShop, ItemStack goodItem, double price, int amount) {
                    this.ownerShop = ownerShop;
                    this.goodItem = goodItem;
                    this.price = price;
                    this.amount = amount;
            }

            public static ShopGood deserialize(TalexShop shop, String str) {
                String jsonStr = new String(Base64.getDecoder().decode(str));
                if ( jsonStr.length() == 0 ) return null;

                JsonElement element = new JsonParser().parse(jsonStr);
                JsonObject json = element.getAsJsonObject();

                double price = json.get("price").getAsDouble();
                int amount = json.get("amount").getAsInt();
//                String shopOwner = json.get("shop").getAsString();
//                ItemStack item = new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(json.get("item").getAsString(), ItemStack.class);
                ItemStack item = ItemUtil.str2Item(json.get("item").getAsString());

                return new ShopGood(shop, item, price, amount);
            }

            public String serialize() {
                JsonObject json = new JsonObject();

                json.addProperty("price", this.price);
                json.addProperty("amount", this.amount);
//                json.addProperty("shop", this.ownerShop.shopOwner);
                json.addProperty("item", ItemUtil.item2Str(this.goodItem));
//                json.addProperty("item", gsonBuilder.enableComplexMapKeySerialization().create().toJson(this.goodItem));

                return Base64.getEncoder().encodeToString(json.toString().getBytes(StandardCharsets.UTF_8));
            }

    }

}
