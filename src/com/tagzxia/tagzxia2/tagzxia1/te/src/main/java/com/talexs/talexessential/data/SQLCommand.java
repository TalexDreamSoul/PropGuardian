package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.data;

public enum SQLCommand {


    DELETE_DATA(
            "DELETE FROM `%table_name%` WHERE `%username%` = \"%value%\""
    ),

    UPDATE_DATA(

            "UPDATE `%table_name%` SET `amount`= %amount% WHERE username = \"%username%\" "

    ),

    SELECT_ALL_DATA(
            "SELECT * FROM `%table_name%`"
    ),

    SELECT_DATA(
            "SELECT * FROM `%table_name%` WHERE `%select_type%` = \"%username%\""
    );


    private String command;

    SQLCommand(String command) {

        this.command = command;
    }

    public String commandToString() {

        return this.command;
    }
}
