package com.minburg.movingtool.models;

import androidx.annotation.NonNull;

public enum Ownership {
    Keep("K"), Sell("S"), Store_Somewhere("?");

    private String string;

    Ownership(String name){string = name;}

    public String convertToString(){
        return string;
    }
}
