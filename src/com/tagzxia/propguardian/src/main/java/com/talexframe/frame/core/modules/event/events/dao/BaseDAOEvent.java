package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.dao;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@link com.talexframe.frame.core.modules.event.events.dao Package }
 *
 * @author TalexDreamSoul 22/03/06 下午 03:49 Project: TalexFrame
 */
@Getter
@AllArgsConstructor
public class BaseDAOEvent<T> extends TalexEvent {

    private final T processor;

}
