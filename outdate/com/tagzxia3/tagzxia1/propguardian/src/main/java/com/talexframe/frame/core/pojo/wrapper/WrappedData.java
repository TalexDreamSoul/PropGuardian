package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.wrapper;

import com.talexframe.frame.core.pojo.dao.vo.auto.AutoSaveData;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:14 <br /> Project: TalexFrame <br />
 */
@Getter
@AllArgsConstructor
public class WrappedData<T extends AutoSaveData> {

    private T value;

}
