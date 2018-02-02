/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tiem625.editorapp.enums;

import java.util.stream.Stream;

/**
 *
 * @author Tiem625
 */
public enum BrickColors {
    
    RED("#FF0000", "R"),
    WHITE("#FFFFFF", "W"),
    BLACK("#000000", "B");

    private String jsonCode;
    private String colorCode;
    
    private BrickColors(String colorCode, String jsonCode) {
        this.colorCode = colorCode;
        this.jsonCode = jsonCode;
    }

    public String getJsonCode() {
        return jsonCode;
    }

    public String getColorCode() {
        return colorCode;
    }
    
    public static BrickColors fromJsonCode(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(values()).
                filter(color -> color.getJsonCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }
}
