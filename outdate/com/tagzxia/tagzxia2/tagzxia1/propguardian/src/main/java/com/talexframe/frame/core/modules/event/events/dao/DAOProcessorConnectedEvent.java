package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao.BaseDAOEvent;
import com.talexframe.frame.core.modules.event.service.IContinue;
import lombok.Getter;
import lombok.Setter;

/**
 * <br /> {@link com.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:38 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
public class DAOProcessorConnectedEvent<T> extends BaseDAOEvent<T> implements IContinue {

    public DAOProcessorConnectedEvent(T t) {

        super(t);

    }

    @Override
    public String getMatchKey() {

        return System.nanoTime() + " # DAOProcessorConnectedEvent @" + this.hashCode();
    }

}
