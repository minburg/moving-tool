package com.minburg.movingtool.models;

import androidx.annotation.NonNull;

public enum Ownership {
    Keep("Keep"), Sell("Sell"), Store_Somewhere("Store");

    private String string;

    Ownership(String name){string = name;}

    public String convertToString(){
        return string;
    }
}
