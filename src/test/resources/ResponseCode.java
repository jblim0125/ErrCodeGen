package com.mobigen.platform.webcore.response;

import java.lang.String;

public enum ResponseCode {

    SUCCESS("0"),
    ERROR_UNKNOWN("UNKNOWN"),
    ERROR1000("ERROR1000"),     // ERROR1000 {0} is {1}, {0}
    ERROR1001("ERROR1001"),     // ERROR1001 {0} is {1}, {0}
    ERROR1002("ERROR1002"),     // ERROR1002 {0} is {1}, {0}
    ERROR1003("ERROR1003"),     // ERROR1003 {0} is {1}, {0}
    ;

    private final String name;

    ResponseCode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
