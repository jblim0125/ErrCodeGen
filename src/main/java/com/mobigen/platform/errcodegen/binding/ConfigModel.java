package com.mobigen.platform.errcodegen.binding;

import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public class ConfigModel {
    private String input;
    private String output;

    private Map<String, String> codeAndValue;

    public boolean check() {
        if( input.isEmpty() || output.isEmpty() ) return false;
        return true;
    }
}
