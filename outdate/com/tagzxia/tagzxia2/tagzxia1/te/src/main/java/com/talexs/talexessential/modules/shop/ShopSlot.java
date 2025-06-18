package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.shop;

import com.talexs.talexessential.modules.shop.sell.SellCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class ShopSlot {

    private final double originalPrice;

    private final Material material;

    private final SellCategory sellCategory;

    public double calcPrice(int amo, int DAY_SELL_LIMIT) {
        if ( originalPrice <= 1 ) return originalPrice;

        double price = originalPrice;

        if ( amo > DAY_SELL_LIMIT ) {
            price = price * 0.5;

            // 超出数
            int out = amo - DAY_SELL_LIMIT;
            out = out % 1000;

            // 0.1的out次方
            double per = Math.pow(0.1, out);

            price = price * per;

            price = Math.max(price, originalPrice * 0.3);

        }

        return price;
    }

}
