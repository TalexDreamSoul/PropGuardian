package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.json.JSONUtil;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.ClassReceiverManager;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddonAdapter;
import com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.utils.UrlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 请求解析器 <br /> {@link com.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul
 * 22/03/05 下午 06:57 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class RequestAnalyser {

    private static final ClassReceiverManager managerIns = ClassReceiverManager.INSTANCE;

    private final WrappedResponse wr;
    private final HttpServletRequest request;
    private final long time;

    public RequestAnalyser(WrappedResponse wr, long time) {

        this.wr = wr;
        this.time = time;
        this.request = wr.getRequest();

        process();

    }

    @SneakyThrows
    private void process() {

        log.info("[解析层] RequestID: " + request.getSession().getId());

        // 解析请求
        String originalUrl = request.getRequestURI();

        if( originalUrl.endsWith("/") ) {

            originalUrl = originalUrl.substring(0, originalUrl.length() - 1);

        }

        UrlBuilder urlBuilder = UrlBuilder.ofHttp(originalUrl, Charset.forName(request.getCharacterEncoding()));

        UrlPath urlPath = urlBuilder.getPath();

        if( urlPath.getSegments().size() == 0 ) {

            log.info("[解析层] 请求路径为空!");

            wr.returnDataByFailed("请求路径为空!");

            return;

        }

        String parsedUrl = UrlUtil.formatUrl(urlPath.toString());

        log.debug("[解析层] Format url: " + parsedUrl);

        // AtomicBoolean matched = new AtomicBoolean(false);

        TrieNode node = managerIns.getTireRouter().matchRoute(parsedUrl);

        if( node == null ) return;

        this._process(node);

        // networkManager.matchUrlReceivers(parsedUrl).forEach((reqReceiver) -> {
        //
        //     if( matched.get() ) return;
        //
        //     String subUrl = parsedUrl.replaceFirst(reqReceiver.getTApp().getDefaultPath(), "");
        //
        //     reqReceiver.matchUrlSubReceivers(subUrl).forEach((subReqReceiver) -> {
        //
        //         if( matched.get() ) return;
        //
        //         matched.set(true);
        //
        //         if( wr.getResponse().isCommitted() ) return;
        //
        //         log.debug("[解析层] 匹配到的路径: " + subUrl);
        //
        //         AtomicBoolean could = new AtomicBoolean(true);
        //         LinkedList<ReceiverAddon> clsReceiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddon.ReceiverAddonType.CLASS_APP);
        //         clsReceiverAddons.forEach((addon) -> {
        //
        //             if( !could.get() ) return;
        //
        //             // log.debug("[解析层] # -- CLASS -- 开始执行接收器: " + addon.getClass().getName());
        //
        //             could.set(addon.onPreCheckAppReceiver(reqReceiver, wr));
        //
        //             // log.debug("[解析层] # -- CLASS -- 接收器执行结果: " + could.get());
        //
        //         });
        //
        //         if( !could.get() ) return;
        //
        //         Collection<ReceiverAddon> receiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddon.ReceiverAddonType.METHOD_APP);
        //
        //         receiverAddons.forEach((addon) -> {
        //
        //             if( !could.get() ) return;
        //
        //             // log.debug("[解析层]   # -- METHOD -- 开始执行接收器: " + addon.getClass().getName());
        //
        //             could.set(addon.onPreInvokeMethod(subReqReceiver, wr));
        //
        //             // log.debug("[解析层]   # -- METHOD -- 接收器执行结果: " + could.get());
        //
        //         });
        //
        //         log.debug("[解析层]   # -- METHOD -- 总体执行结果: " + could.get());
        //
        //         if( !could.get() ) return;
        //
        //         log.debug("[解析层]   # -- METHOD -- >>> 进入 (onRequest) | {}", could.get());
        //
        //         Object obj = subReqReceiver.onRequest(reqReceiver, subReqReceiver, wr, time);
        //
        //         log.debug("[解析层]   #   -- onRequest -- 执行结果: " + JSONUtil.toJsonStr(obj));
        //
        //         final Object finalObj = obj;
        //         receiverAddons.forEach((addon) -> addon.onPostInvokeMethod(subReqReceiver, wr, finalObj));
        //
        //         log.debug("[解析层]   # -- METHOD -- 执行结果完毕!");
        //
        //         if ( obj != null ) {
        //
        //             String tStr = JSONUtil.toJsonStr(obj);
        //
        //             wr.returnData(tStr);
        //
        //             log.info("[应用层] ## OK Return: " + tStr + " ##");
        //
        //         }
        //
        //         clsReceiverAddons.forEach((addon) -> addon.onPostCheckAppReceiver(reqReceiver, wr));
        //
        //         // log.debug("[解析层] # -- CLASS -- 接收器执行结果完毕!");
        //
        //         log.debug("-------------------------------------------");
        //
        //     });
        //
        // });


        // TODO 完整的 两套系统 热插拔 Path

    }

    private void _process(TrieNode node) {

        log.debug("[解析层] 匹配到的路径: " + node.getPath());

        ClassAppReceiver clsReceiver = managerIns.getReqMap().get(node.getReceiver().getOwnClass());

        if( clsReceiver == null ) return;

        AtomicBoolean could = new AtomicBoolean(true);
        LinkedList<ReceiverAddon> clsReceiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddon.ReceiverAddonType.CLASS_APP);
        clsReceiverAddons.forEach((addon) -> {

            if( !could.get() ) return;

            // log.debug("[解析层] # -- CLASS -- 开始执行接收器: " + addon.getClass().getName());

            could.set(addon.onPreCheckAppReceiver(clsReceiver, wr));

            // log.debug("[解析层] # -- CLASS -- 接收器执行结果: " + could.get());

        });

        if( !could.get() ) return;

        Collection<ReceiverAddon> receiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddon.ReceiverAddonType.METHOD_APP);

        receiverAddons.forEach((addon) -> {

            if( !could.get() ) return;

            // log.debug("[解析层]   # -- METHOD -- 开始执行接收器: " + addon.getClass().getName());

            could.set(addon.onPreInvokeMethod(node.getReceiver(), wr));

            // log.debug("[解析层]   # -- METHOD -- 接收器执行结果: " + could.get());

        });

        log.debug("[解析层]   # -- METHOD -- 总体执行结果: " + could.get());

        if( !could.get() ) return;

        log.debug("[解析层]   # -- METHOD -- >>> 进入 (onRequest) | {}", could.get());

        Object obj = node.getReceiver().onRequest(clsReceiver, wr, time);

        log.debug("[解析层]   #   -- onRequest -- 执行结果: " + JSONUtil.toJsonStr(obj));

        final Object finalObj = obj;
        receiverAddons.forEach((addon) -> addon.onPostInvokeMethod(node.getReceiver(), wr, finalObj));

        log.debug("[解析层]   # -- METHOD -- 执行结果完毕!");

        if ( obj != null ) {

            String tStr = JSONUtil.toJsonStr(obj);

            wr.returnData(tStr);

            log.info("[应用层] ## OK Return: " + tStr + " ##");

        }

        clsReceiverAddons.forEach((addon) -> addon.onPostCheckAppReceiver(clsReceiver, wr));

        // log.debug("[解析层] # -- CLASS -- 接收器执行结果完毕!");

        log.debug("-------------------------------------------");

    }

}
