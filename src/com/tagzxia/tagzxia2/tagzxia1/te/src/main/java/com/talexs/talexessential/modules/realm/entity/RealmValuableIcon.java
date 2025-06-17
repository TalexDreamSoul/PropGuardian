package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.entity;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.entity.RealmIcon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Define a realm icon that cost any money.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/24 下午 10:46
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class RealmValuableIcon {

    private RealmIcon icon;

    private double cost;

}
