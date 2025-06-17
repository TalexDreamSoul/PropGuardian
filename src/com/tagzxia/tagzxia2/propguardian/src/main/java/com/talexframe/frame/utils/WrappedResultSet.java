package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;

/**
 * <br /> {@link com.talex.frame.utils Package }
 *
 * @author TalexDreamSoul
 * @date 22/02/08 下午 10:03 <br /> Project: TFunction <br />
 */
@AllArgsConstructor
@Getter
public class WrappedResultSet {

    private ResultSet rs;

    private int status;

}
