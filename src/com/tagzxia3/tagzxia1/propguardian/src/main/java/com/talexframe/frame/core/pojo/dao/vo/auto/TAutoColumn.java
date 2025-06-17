package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.TAutoTable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动保存实例 标注在方法上，代表将执行这个方法并保存方法返回 <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 15:14 <br /> Project: TalexFrame <br />
 */
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface TAutoColumn {

    /**
     * 若为空将自动小写当前字段名
     **/
    String value() default "";

    /**
     * 是否为空
     **/
    boolean nullable() default true;

    /**
     * 如果该参数为真 则在加载时有可能为null 若为假数据不存在时会报错
     **/
    boolean ignoreValue() default true;

    /**
     * 是否为MySql字段 默认的数据在存储时会存储到mysql的info数据字段上 如果加入仍然会存储但会在字段中显示 <br /> 如果要存储数据字段仅支持 基本数据类型 以及 String 字段越多性能越低 必须 {@link
     * TAutoTable TAutoTable} 开启 fullJsonRecord 否则无效
     */
    boolean joinField() default false;

    /**
     * MYSQL数据字段的类型 如果什么都不填则会由框架自动判断 建议大文本数据必填
     **/
    String type() default "";

    /**
     * 设置为True后每次更新都会将此值设置为 最后一次更新时间
     *
     * @return 是否更新时间
     */
    boolean update() default false;

    /**
     * 当对象出现异常时请尝试开启 开启意味着该类需要一个 Override 的 toString 以及一个 constructor 只有一个参数(String) 注意, 开启此选项后请在类上加入注解
     *
     * @return 是否开启String模式
     */
    boolean String() default false;

    /**
     * 精度 如int(10) 则为10
     **/
    int precision() default 32;

    /**
     * 小数位数 如int(10,2) 则为2
     *
     * @return
     **/
    int scale() default 0;

    /** 不为空时当前行替换为这里的内容 **/
    String content() default "";

    /** 当前行的备注 **/
    String comment() default "";

}
