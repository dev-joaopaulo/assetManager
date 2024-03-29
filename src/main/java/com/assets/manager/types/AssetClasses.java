package com.assets.manager.types;

import java.util.ArrayList;
import java.util.List;

public enum AssetClasses {

    FIXED("fixed income"),
    VARIABLE("variable income"),
    CASH("cash");

    private final String className;

    AssetClasses(String className) {
        this.className = className;
    }

    public List<String> listClasses(){
        List<String > assetClasses = new ArrayList<>();
        for(AssetClasses c : AssetClasses.values()){
            assetClasses.add(c.toString());
        }
        return assetClasses;
    }

    @Override
    public String toString(){
        return this.className;
    }
}
