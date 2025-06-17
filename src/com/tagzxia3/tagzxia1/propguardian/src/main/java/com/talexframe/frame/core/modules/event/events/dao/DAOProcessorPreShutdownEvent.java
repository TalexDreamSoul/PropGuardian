package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao.BaseDAOEvent;
import com.talexframe.frame.core.modules.event.service.Cancellable;
import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:44 <br /> Project: TalexFrame <br />
 */
@Getter
public class DAOProcessorPreShutdownEvent<T> extends BaseDAOEvent<T> implements Cancellable {

    private boolean cancel;

    public DAOProcessorPreShutdownEvent(T processor) {

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
