package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.repository;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONObject;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.repository.TRepo;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.pojo.dao.vo.auto.AutoSaveData;
import com.talexframe.frame.core.pojo.dao.vo.auto.TAutoColumn;
import com.talexframe.frame.core.pojo.dao.vo.auto.TAutoSaveId;
import com.talexframe.frame.core.pojo.dao.vo.auto.TAutoTable;
import com.talexframe.frame.core.pojo.mapper.frame.FramePluginSender;
import com.talexframe.frame.core.pojo.wrapper.WrappedData;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自动化存储库 # 自动存储数据 <br /> {@link com.talexframe.frame.core.modules.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:03 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
@SuppressWarnings( "unused" )
public class TRepoPlus<T extends AutoSaveData> extends TRepo {

    protected final WebPlugin ownPlugin;
    protected final Class<? extends AutoSaveData> templateData;
    protected ConcurrentMap<String, T> dataMap;

    protected FramePluginSender logger;
    /**
     * 当预估自己的数据过大时请调整!
     */
    @Setter
    protected String infoType = "VARCHAR(512)";

    public TRepoPlus(WebPlugin webPlugin, String tableName) {

        super(tableName);

        this.ownPlugin = webPlugin;
        this.logger = webPlugin.getConsoleSender();

        //noinspection unchecked
        this.templateData = (Class<T>) ( (ParameterizedType) this.getClass().getGenericSuperclass() ).getActualTypeArguments()[0];

    }

    /**
     * 是否默认读取所有数据到 dataMap 中, 默认为假
     */
    public boolean doCached() {return false;}

    @SneakyThrows
    public T readData(String key, String identifier) {

        ResultSet rs =  readSearchData(key, identifier);

        if( rs == null || !rs.next() ) return null;

        //noinspection unchecked
        return (T) AutoSaveData.deserialize(this.templateData, rs).getValue();

    }

    public T getData(String identifyId) {

        if( !doCached() ) return null;

        if ( identifyId == null ) {
            return null;
        }
        return this.dataMap.get(identifyId);
    }

    public void delData(T data) {

        if( !doCached() ) return;

        this.dataMap.remove(data.getMainKey(), data);
        this.onSingleDataRemoved(new WrappedData<>(data));
    }

    public void onSingleDataRemoved(WrappedData<?> data) {

    }

    /**
     * @param data 数据实例 默认 不cache 所以每次数据一更新就自动塞到数据库
     */
    public void addData(T data) {

        if( !doCached() ) {

            saveDataToMysql(data);

            return;

        }

        this.onSingleDataLoaded(new WrappedData<>(data));

        this.dataMap.put(data.getMainKey(), data);

    }

    /**
     * @return 返回真不放入数据map
     */
    @SneakyThrows
    public boolean onSingleDataLoaded(WrappedData<T> data) {

        // 每次放入map也刷新数据库中数据

        saveDataToMysql( data.getValue() );

        return false;
    }

    /**
     * 当所有的Repo都加载完毕后触发
     * 请在这里做数据操作
     */
    public void onAllRepoDone() {}

    @SneakyThrows
    public void onInstall() {

        if ( !doCached() ) {

            return;

        }

        this.dataMap = new ConcurrentHashMap<>();

        List<Entity> entities = this.forDb().findAll(forEntity());

        for ( Entity entity : entities ) {

            // log.debug("[AutoSaveData] Load all data for first column {}", rs);

            @SuppressWarnings( "unchecked" ) WrappedData<T> data = (WrappedData<T>) AutoSaveData.deserializeByBase64Str(templateData, entity.getStr("as_info"));

            if ( data.getValue() == null || data.getValue().getMainKey() == null ) {

                log.error("[AutoSaveData] FatalError!! # " + templateData.getName());

            }

            if ( onSingleDataLoaded(data) ) {

                continue;

            }

            dataMap.put(( data.getValue() ).getMainKey(), data.getValue());

        }

        onDataLoaded();

    }

    public void onUninstall() {

        saveAllDataToMysql();

    }

    /**
     * &#064;Description  Please use saveDataToMysql instead
     */
    @SneakyThrows
    @Deprecated
    public void saveDataToMysqlSilence(T data) {

        saveDataToMysql(data);

    }

    @SneakyThrows
    public void saveAllDataToMysql() {

        if ( dataMap == null ) {
            return;
        }

        for ( T data : dataMap.values() ) {

            saveDataToMysql(data);

        }

        log.debug("[BaseAutoSaveData] " + getProvider() + " @" + getClass() + " 存储完毕!");

    }

    @SneakyThrows
    public int saveDataToMysql(T data) {

        Class<? extends AutoSaveData> clz = data.getClass();

        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder update = new StringBuilder();

        JSONObject json = new JSONObject();

        TAutoTable tableAnnotation = templateData.getAnnotation(TAutoTable.class);

        for ( Field field : clz.getDeclaredFields() ) {

            TAutoColumn as = field.getAnnotation(TAutoColumn.class);

            if ( as == null ) {
                continue;
            }

            field.setAccessible(true);
            
            String name = field.getName();

            if ( as.String() ) {

                json.putOpt(name, field.get(data).toString());

            } else {

                json.putOpt(name, field.get(data));

            }

            if( !as.joinField() ) {

                continue;

            }
            
            if(tableAnnotation != null && tableAnnotation.prefix()) {
                
                name = "as_" + name;
                
            }

            keys.append(name).append(", ");

            if ( as.update() ) {

                String ts = String.valueOf(System.currentTimeMillis());

                values.append("\"").append(ts).append("\", ");
                update.append(name).append("=\"").append(ts).append("\", ");

            } else {

                Object obj = field.get(data);
                if ( field.getType() == String.class ) {

                    values.append("\"").append(obj).append("\", ");
                    update.append(name).append("=\"").append(obj).append("\", ");

                } else {

                    values.append(obj).append(", ");
                    update.append(name).append("=").append(obj).append(", ");

                }

            }

        }

        String encStr = Base64.encode(json.toString());

        if( tableAnnotation != null && tableAnnotation.fullJsonRecord() ) {

            keys.append("as_info");
            values.append("\"").append(encStr).append("\"");
            update.append("as_info=\"").append(encStr).append("\"");
            
        } else {
            
            keys.delete(keys.length() - 2, keys.length());
            values.delete(values.length() - 3, values.length());
            update.delete(update.length() - 3, update.length());
            
        }

        String sql = "INSERT INTO " + getProvider() + "(" + keys + ") VALUES(" +
                values + ") ON DUPLICATE KEY UPDATE " + update;

        log.debug("[BaseAutoSaveData] " + getProvider() + " @" + getClass() + " 存储数据: " + sql);

        return mysql.prepareStatement(sql);

    }

    @SneakyThrows
    public void initTable() {

        List<String> idList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        StringBuilder createStr = new StringBuilder(1000);

        //获取表名
        TAutoTable tableAnnotation = templateData.getAnnotation(TAutoTable.class);
        String tableName = getProvider();

        // if ( tableAnnotation == null ) {
        //
        //     tableName = templateData.getName().toLowerCase(Locale.ROOT);
        //
        // } else {
        //
        //     tableName = tableAnnotation.value();
        //
        // }

        createStr.append("create table if not exists ").append(tableName).append("(");

        for ( Field field : templateData.getDeclaredFields() ) {

            TAutoColumn column = field.getAnnotation(TAutoColumn.class);

            if ( column == null || !column.joinField() ) {

                continue;

            }

            String fieldName = column.value();

            if ( StrUtil.isBlank(fieldName) ) {

                fieldName = field.getName().toLowerCase();

            }

            if( tableAnnotation != null && tableAnnotation.prefix() ) {

                fieldName = "as_" + fieldName;

            }

            // log.debug("Field Name: {}", fieldName);

            if ( field.isAnnotationPresent(TAutoSaveId.class) ) {

                idList.add(fieldName);

            }

            String columnStr = column.content();

            if( StrUtil.isBlank(columnStr) ) {

                Class<?> type = field.getType();
                boolean nullable = column.nullable();
                String nullableStr = nullable ? "" : "not null";
                String commentStr = StrUtil.isBlank(column.comment()) ? "" : "comment '" + column.comment() + "'";
                String typeStr = column.type();

                if( StrUtil.isBlankIfStr(typeStr) ) {

                    if ( Integer.class == type || type.getName().equalsIgnoreCase("int") ) {

                        typeStr = "int";

                    } else if ( Long.class == type || type.getName().equalsIgnoreCase("long") ) {

                        typeStr = "bigint(" + column.precision() + ")";

                    } else if ( String.class == type ) {

                        int precision = column.precision();

                        if ( 0 == precision ) {

                            precision = 64;

                        }

                        typeStr = "varchar(" + precision + ")";

                    } else if ( BigDecimal.class == type || type.getName().equalsIgnoreCase("double") || type.getName().equalsIgnoreCase("float") ) {

                        int precision = column.precision();
                        int scale = column.scale();

                        typeStr = "decimal(" + precision + "," + scale + ")";

                    } else {

                        log.debug("Unsupported type: {}", type);

                        continue;

                    }

                }

                columnStr +=
                        fieldName + " "
                                + typeStr + " "
                                + nullableStr + " " + commentStr;

            }

            columnList.add(columnStr);

        }

        if( tableAnnotation != null && tableAnnotation.fullJsonRecord() ) {

            columnList.add("as_info varchar(" + tableAnnotation.precision() + ") not null");

        }

        createStr.append(String.join(",", columnList));

        if ( 0 != idList.size() ) {

            createStr.append(", ").append("primary key(").append(String.join(",", idList)).append("));");

        } else {

            createStr.append(");");

        }

        int row = mysql.prepareStatement(createStr.toString());
        log.debug(createStr.toString());
        if( row == 1 ) {

            log.info("创建表成功：{}", tableName);

        }

        // SqlTableBuilder sqlTableBuilder = newSqlTableBuilder();
        //
        // for( Field field : templateData.getDeclaredFields()) {
        //
        //     TAutoColumn as = field.getAnnotation(TAutoColumn.app);
        //
        //     if(as == null || !as.isMySqlFiled()) { continue; }
        //
        //     if( !StrUtil.isBlankIfStr(as.columnContent()) ) {
        //
        //         sqlTableBuilder.addTableParam(new TableParam().setColumnContent(as.columnContent())
        //                 .setSubParamName("as_" + field.getName()).setMain(as.isMain()));
        //
        //     } else {
        //
        //         if( "info".equalsIgnoreCase(field.getName()) ) {
        //
        //             throw new IllegalArgumentException("MysqlField name cannot be info, because it will recover data.");
        //
        //         }
        //
        //         if(!field.getAddonType().isPrimitive() && field.getAddonType() != String.app) {
        //
        //             throw new IllegalArgumentException("MysqlField type can only use primitive type (" + field.getAddonType().getName() + ")");
        //
        //         }
        //
        //         String defaultNull = as.defaultNull();
        //
        //         if( "null".equalsIgnoreCase(defaultNull)) {
        //
        //             defaultNull = null;
        //
        //         } else if( "n_null".equalsIgnoreCase(defaultNull)) {
        //
        //             defaultNull = "null";
        //
        //         }
        //
        //         sqlTableBuilder.addTableParam(new TableParam()
        //
        //                 .setSubParamName("as_" + field.getName())
        //                 .setType(as.type())
        //                 .setDefaultNull(defaultNull)
        //                 .setMain(as.isMain())
        //
        //         );
        //
        //     }
        //
        // }
        //
        // this.createTable(sqlTableBuilder
        //
        //         .addTableParam(new TableParam()
        //                 .setDefaultNull(null).setType(infoType).setSubParamName("as_info").setMain(false)));

    }

    public void onDataLoaded() {}

}
