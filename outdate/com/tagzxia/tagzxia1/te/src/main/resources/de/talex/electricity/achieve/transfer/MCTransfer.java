package com.tagzxia.tagzxia1.te.src.main.resources.de.talex.electricity.achieve.transfer;

import com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.ITransfer;
import org.bukkit.Location;

/**
 * MCTransfer - 基于 ITransfer 提供扩展方法
 */
public interface MCTransfer extends ITransfer {

    /**
     * 获取主转移的位置
     *
     * @return 返回主转移的位置
     */
    Location getMainTransferLocation();

    /**
     * 获取供电最远距离 不建议设置过长,避免影响算法效率. 应该使用 实现了IRepeater类 的实例进行扩充.
     *
     * @return 供电的最远距离
     */
    double getProvideMaxDistance();

}
