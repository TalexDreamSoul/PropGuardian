package com.tagzxia3.te.src.main.resources.de.talex.electricity.achieve.transfer;

import lombok.Data;
import lombok.Setter;
import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.IPather;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.addon.TalexLocation;

/**
 * 通过提供一个 转移接口类 进行实例化 RouterPath - 路由路径映射
 * <p>
 * PathAlgorithm 将生成 n(N个接收器) 个RouterPath 每一个RouterPath 中包含 finalFrom 以及 finalTo 代表最终的接收器和 起点的位置 通过 while 或者 queue
 * 进行遍历每一个routerpath 不断获取其father 可以获得其所有路径
 *
 * @author TalexDreamSoul
 */
@Data
@Deprecated
public class RouterPath implements IPather {

    private final TalexLocation from;
    private final IPower fromIns;

    private final TalexLocation to;
    private final IPower toIns;

    /**
     * 最终的起点
     **/
    private Location finalFrom;

    /**
     * 最终的终点
     **/
    private Location finalTo;

    /**
     * 实际距离格数 非最近 最近请使用 finalFrom.distance(finalTo);
     **/
    @Setter
    private int distance;

}
