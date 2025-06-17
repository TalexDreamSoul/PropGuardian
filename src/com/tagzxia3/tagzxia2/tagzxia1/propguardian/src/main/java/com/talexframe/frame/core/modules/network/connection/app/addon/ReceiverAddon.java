package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon;

import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.Getter;

import java.lang.reflect.Parameter;

/**
 * 请求插件
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:08 Project: TalexFrame
 */
@Getter
public class ReceiverAddon extends FrameCreator {

    public enum ReceiverAddonType {

        CLASS_APP(), METHOD_APP(), PARAM_APP(), URL_PARAM_APP();

    }

    @Getter
    public enum ReceiverAddonPriority {

        SUPER_HIGHEST(10000) ,MOST_HIGHEST(1000), HIGHEST(100), HIGH(10), NORMAL(0), LOW(-10), LOWEST(-100);

        private final int priority;

        ReceiverAddonPriority(int priority) {

            this.priority = priority;

        }

    }

    private final ReceiverAddonType[] addonType;

    protected ReceiverAddonPriority priority = ReceiverAddonPriority.NORMAL;

    public ReceiverAddon( String receiverName, ReceiverAddonType[] addonType ) {

        super("ReceiverAddon", receiverName);

        this.addonType = addonType;

    }

    /**
     * 当检测AppReceiver时之前用
     *
     * @return 返回真则允许调用
     */
    public boolean onPreCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) { return true; };

    /**
     * 当AppReceiver运行完毕后，所有的方法也被调用完成后调用
     */
    public void onPostCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) {  };

    /**
     * 当即将调用方法时调用
     *
     * @return 返回真则允许调用
     */
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) { return true; };

    /**
     * 当方法调用完成时调用
     */
    public void onPostInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr, Object methodReturn) {  };

    /**
     * 当即将添加参数时调用
     *
     * @return 返回真则允许添加
     */
    public boolean onPreAddParam(MethodAppReceiver methodAppReceiver, Parameter parameter, WrappedResponse wr, Object addedParam) { return true; };

    /**
     * 当参数添加完毕时调用
     */
    public void onPostAddParam(MethodAppReceiver methodAppReceiver, Parameter parameter, WrappedResponse wr, Object addedParam) {  };

    /**
     * 当即将添加URL参数时调用
     *
     * @return 返回真则允许添加
     */
    public boolean onPreAddUrlParam(MethodAppReceiver methodAppReceiver, Parameter parameter, WrappedResponse wr, String originUrl, String comingUrl, String identifier, String value) { return true; };

    /**
     * 当URL参数添加完毕时调用
     */
    public void onPostAddUrlParam(MethodAppReceiver methodAppReceiver, Parameter parameter, WrappedResponse wr, String originUrl, String comingUrl, String identifier, String value) {  };

}
