package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.union;

public enum MemberPermission {
    OWNER(0),
    ADMIN(1),
    MEMBER(2),
    ;

    private final int code;

    MemberPermission(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MemberPermission g(int code) {
        for (MemberPermission value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
