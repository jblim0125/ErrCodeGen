<#assign className = data.output?keep_before(".")>
<#assign errAndMsg = data.content>
package com.mobigen.platform.webcore.response;

import java.lang.String;

public enum ${className} {

    SUCCESS("0"),
    ERROR_UNKNOWN("UNKNOWN"),
<#list errAndMsg as code, msg >
    ${code}("${code}"),     // ${msg}
</#list>
    ;

    private final String name;

    ResponseCode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}