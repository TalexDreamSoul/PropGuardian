package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <br /> {@link com.talexframe.frame.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 10:19 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@Accessors( chain = true )
@AllArgsConstructor
@NoArgsConstructor
public class ResultData<T> {

    /**
     * 结果状态
     */
    private int status;
    /**
     * 结果消息
     */
    private String message;
    /**
     * 数据
     */
    private T data;
    /**
     * 接口耗时
     */
    private long timeStamp;

    public ResultData(ResultEnum rEnum, T data, long timeStamp) {

        this(rEnum.getCode(), rEnum.getMsg(), data, timeStamp);

    }

    public ResultData(ResultEnum rEnum, T data) {

        this(rEnum, data, 0);

    }

    /**
     * 成功返回
     *
     * @param data 数据实例
     * @param <T>  数据类型
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> SUCCESS(T data) {

        return new ResultData<>(ResultEnum.SUCCESS, data);

    }

    /**
     * 成功返回
     *
     * @param data      数据实例
     * @param <T>       数据类型
     * @param timeStamp 执行消耗
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> SUCCESS(T data, long timeStamp) {

        return new ResultData<>(ResultEnum.SUCCESS, data, timeStamp);

    }

    /**
     * 失败返回
     *
     * @param data 数据实例
     * @param <T>  数据类型
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(T data) {

        return new ResultData<>(ResultEnum.FAILED, data);

    }

    /**
     * 失败返回
     *
     * @param data      数据实例
     * @param <T>       数据类型
     * @param timeStamp 执行消耗
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(T data, long timeStamp) {

        return new ResultData<>(ResultEnum.FAILED, data, timeStamp);

    }

    /**
     * 失败返回
     *
     * @param rEnum 结果枚举
     * @param data  数据实例
     * @param <T>   数据类型
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(ResultEnum rEnum, T data) {

        return new ResultData<>(rEnum, data);

    }

    /**
     * 失败返回
     *
     * @param rEnum     结果枚举
     * @param data      数据实例
     * @param <T>       数据类型
     * @param timeStamp 执行消耗
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(ResultEnum rEnum, T data, long timeStamp) {

        return new ResultData<>(rEnum, data, timeStamp);

    }

    public ResultData<T> setStatusByEnum(ResultEnum rEnum) {

        this.status = rEnum.getCode();
        this.message = rEnum.getMsg();

        return this;

    }

    @Getter
    @AllArgsConstructor
    public enum ResultEnum {

        /**
         * 操作成功
         **/
        SUCCESS(200, "OK"),
        /**
         * 操作失败
         **/
        FAILED(999, "FAILED"),
        /**
         * 服务限流
         **/
        SERVICE_LIMITED(10000, "server busy"),
        /**
         * 服务降级
         **/
        SERVICE_DEGRADATION(10001, "service degradation"),
        /**
         * 热点参数限流
         **/
        HOT_PARAM_LIMITED(10002, "hot param limited"),
        /**
         * 非法访问
         **/
        ILLEGAL_ACCESS(10003, "illegal access"),
        /**
         * 授权规则不通过
         **/
        ACCESS_DENIED(10004, "access denied"),
        /**
         * 缺失权限
         **/
        INSUFFICIENT_PERMISSION(10005, "insufficient permissions"),
        /**
         * 服务异常
         **/
        SERVER_ERROR(500, "server error"),

        /**
         * 找不到服务
         */
        NOT_FOUND(404, "Error url"),

        INVALID_TOKEN(10010, "illegal token"),

        CLIENT_AUTHENTICATION_FAILED(10011, "unauthorised token"),

        INFORMATION_ERROR(10012, "information error"),

        UNSUPPORTED_GRANT_TYPE(10013, "unsupported token type"),

        INFORMATION_UNKNOWN(10014, "unknown information");

        final int code;
        final String msg;

    }

}
