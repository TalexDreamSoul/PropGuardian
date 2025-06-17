package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao.BaseDAOEvent;
import com.talexframe.frame.core.modules.event.service.Cancellable;

/**
 * <br /> {@link com.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:50 <br /> Project: TalexFrame <br />
 */
public class DAOProcessorPreCheckStatusEvent<T> extends BaseDAOEvent<T> implements Cancellable {

    private boolean cancel;

    public DAOProcessorPreCheckStatusEvent(T processor) {

        super(processor);

    }

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        cancel = var;

    }

}
