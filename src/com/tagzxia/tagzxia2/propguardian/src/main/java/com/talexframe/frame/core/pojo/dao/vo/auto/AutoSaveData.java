package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.IDataLoaded;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.TAutoColumn;
import com.talexframe.frame.core.pojo.wrapper.WrappedData;
import com.talexframe.frame.core.talex.FrameData;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * 自动保存数据类 <br /> {@link com.talexframe.frame.function.auto.data Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 14:49 <br /> Project: TalexFrame <br />
 */
public abstract class AutoSaveData extends FrameData {

    public AutoSaveData(String provider) {

        super(provider);
    }

    @SneakyThrows
    public static WrappedData<?> deserialize(Class<? extends AutoSaveData> clz, ResultSet rs) {

        return deserializeByBase64Str(clz, rs.getString("as_info"));

    }

    public static WrappedData<?> deserializeByBase64Str(Class<? extends AutoSaveData> clz, String base64) {

        return deserialize(clz, Base64.decodeStr(base64));

    }

    public static WrappedData<?> deserialize(Class<? extends AutoSaveData> clz, String json) {

        return deserialize(clz, JSONUtil.parseObj(json));

    }

    @SneakyThrows
    public static WrappedData<?> deserialize(Class<? extends AutoSaveData> clz, JSONObject json) {

        AutoSaveData type = clz.newInstance();

        for ( Field field : type.getClass().getDeclaredFields() ) {

            com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.TAutoColumn as = field.getAnnotation(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.TAutoColumn.class);

            if ( as == null ) {
                continue;
            }

            if ( !json.containsKey(field.getName()) ) {

                if ( !as.ignoreValue() ) {
                    throw new IllegalArgumentException("Cannot auto load data because " + field + " is not in provide data.");
                }

            }

            if ( !field.isAccessible() ) {
                field.setAccessible(true);
            }

            if ( as.String() ) {

                Constructor<?> constructor = field.getType().getConstructor(String.class);

                constructor.setAccessible(true);

                field.set(type, constructor.newInstance(json.getStr(field.getName())));

            } else {

                field.set(type, json.get(field.getName(), field.getType()));

            }

        }

        if ( type instanceof com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.IDataLoaded) {

            ( (IDataLoaded) type ).onDataLoaded();

        }

        return new WrappedData<>(type);

    }

    @SneakyThrows
    @Override
    public JSONObject toJSONObject() {

        JSONObject json = new JSONObject();

        Class<?> clz = getClass();

        for ( Field field : clz.getDeclaredFields() ) {

            com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto.TAutoColumn as = field.getAnnotation(TAutoColumn.class);

            if ( as == null ) {
                continue;
            }

            if ( !field.isAccessible() ) {

                field.setAccessible(true);

            }

            if ( as.String() ) {

                json.putOpt(field.getName(), field.get(this).toString());

            } else {

                json.putOpt(field.getName(), field.get(this));

            }

        }

        return json;

    }

    /**
     * 获得主key
     */
    public abstract String getMainKey();

}
