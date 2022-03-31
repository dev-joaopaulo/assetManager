package com.assets.manager.types;

import java.util.Arrays;

public enum OperationType {

    BUY("buy"),
    SELL("sell");

    private String value;

    OperationType(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
