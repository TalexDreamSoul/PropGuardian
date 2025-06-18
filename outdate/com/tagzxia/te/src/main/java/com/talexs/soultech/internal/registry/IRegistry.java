package com.tagzxia.te.src.main.java.com.talexs.soultech.internal.registry;

import java.util.List;
import java.util.Set;

public interface IRegistry {

    public int getPriority();

    public Set<String> getScanFolders();

    public boolean isAssignable(Class<?> clazz);

    public boolean register(Class<?> clazz) throws InstantiationException, IllegalAccessException;

    public void onceScanDone(int amo);

}
