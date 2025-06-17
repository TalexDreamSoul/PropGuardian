package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.enums;

public enum FrameType {

    WEB_VUE(),
    MINECRAFT_BUNGEE(),
    MINECRAFT_BUKKIT(),
    MINECRAFT_REDIS(),
    ROBOT(),
    WINDOWS(),
    MAC_OS(),
    ANDROID(),
    UNKNOWN();

    FrameType() {


    }

    public static FrameType match(String str) {

        for ( FrameType frame : FrameType.values() ) {

            if ( str.equalsIgnoreCase(frame.name()) ) {
                return frame;
            }

        }

        return UNKNOWN;

    }


}
