package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.cache.redis;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.cache.redis.TRedisCache;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talexframe.frame.core.pojo.dao.factory.redis.Redis;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 * 命名Cache在前是方面输入的时候可以选择Cache分类的所有缓存
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@Slf4j
public class ReceiverCacheRedisAddon extends ReceiverAddon {

    private static final Redis redis = new DAOManager.ProcessorGetter<Redis>(Redis.class).getProcessor();

    private static final RedisTemplate<String, Object> template = redis.getConfig().getRedisTemplate();

    private static final ValueOperations<String, Object> vo = template.opsForValue();

    public ReceiverCacheRedisAddon() {

        super("ReceiverMethod", new ReceiverAddonType[] { ReceiverAddonType.METHOD_APP });

        super.priority = ReceiverAddonPriority.LOW;

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        TRedisCache tRedisCache = methodAppReceiver.getMethod().getAnnotation(TRedisCache.class);

        if( tRedisCache == null ) return true;

        Class<?> returnType = methodAppReceiver.getMethod().getReturnType();

        if( returnType.isAssignableFrom(Void.TYPE) ) {

            log.warn("[RedisCache] 方法返回类型为Void, 不支持缓存! ");

            return true;

        }

        RedisTemplate<String, Object> template = redis.getConfig().getRedisTemplate();

        ValueOperations<String, Object> vo = template.opsForValue();

        if ( !tRedisCache.delete() ) {

            String key = getRedisCacheKey( tRedisCache, wr.getRequest().getRequestURI(), wr.getParams() );

            Object obj = vo.get(key);

            if( obj == null ) return true;

            wr.returnData(obj);

            log.info("[RedisCache] Cache Hit!");

            return false;

        }

        return true;

    }

    @Override
    public void onPostInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr, Object methodReturn) {

        TRedisCache tRedisCache = methodAppReceiver.getMethod().getAnnotation(TRedisCache.class);

        if( tRedisCache == null ) return;

        if( redis == null || redis.getConfig() == null ) {

            // 缓存没法用 那么照常走流程

            log.error("[解析层] @RedisCache # Redis not ready");

            return;

        }

        Class<?> returnType = methodAppReceiver.getMethod().getReturnType();

        if( returnType.isAssignableFrom(Void.TYPE) ) {

            log.warn("[RedisCache] 方法返回类型为Void, 不支持缓存! ");

            return;

        }

        String key = getRedisCacheKey( tRedisCache, wr.getRequest().getRequestURI(), wr.getParams() );

        if ( tRedisCache.delete() ) {

            vo.getAndDelete(key);

        } else {

            JSONObject cacheValue = JSONUtil.parseObj(methodReturn);

            if ( tRedisCache.expireTime() > 0 ) {

                vo.set(key, cacheValue, Duration.ofSeconds(tRedisCache.expireTime()));

            } else {

                vo.set(key, cacheValue);

            }

            log.info("[Redis] cache key : " + key + " , value : " + cacheValue);

        }

    }

    private String getRedisCacheKey(TRedisCache tRedisCache, String url, List<Object> params ) {

        /*

           解析整个key
           首先解析文本 如果以#开头则解析为变量
           #params 解析为参数列表 如#params[x].xxx
           #urls 解析为完整的地址
           #urls[x] 解析为url的某个部分

         */
        String key = tRedisCache.value();

        if ( key.startsWith("#") ) {

            if ( key.startsWith("#params") ) {

                int index = Integer.parseInt(key.substring(8, 9));

                if ( index > params.size() ) {

                    log.warn("[RedisCache] @RedisCache #params index out of range");
                    log.warn("[RedisCache] For param index: {} with params: {}", index, params);

                    throw new RuntimeException("params index out of range");

                }

                Object obj = params.get(index);
                String value = (String) obj;
                JSONObject json1 = new JSONObject().putOpt("data", obj);

                if ( key.contains("].") ) {

                    String[] args = key.split("\\.");

                    for ( int i = 1; i < args.length; ++i ) {

                        String arg = args[i];

                        Object o = json1.get(arg);

                        if ( o instanceof JSONObject ) {

                            json1 = (JSONObject) o;
                            value = json1.toString();

                        } else {

                            value = String.valueOf(o);

                        }

                    }

                }

                key = value;

            } else if ( key.equalsIgnoreCase("#urls") ) {

                key = url;

            } else if ( key.startsWith("#url") ) {

                int index = Integer.parseInt(key.substring(5, 6));

                String[] urls = UrlUtil.formatUrl(url).split("/");

                if ( index + 1 > urls.length ) {

                    throw new RuntimeException("params index out of range");

                }

                key = urls[index];

            }

        }

        return tRedisCache.type() + ":" + key;

    }

}
