package com.assets.manager.types;

import java.util.ArrayList;
import java.util.List;

public enum VariableIncomeTypes {

    STOCK("stock"),
    FII("fii"),
    ETF("etf");

    private final String typeName;

    VariableIncomeTypes(String typeName) {
        this.typeName = typeName;
    }

    public List<String> listTypes(){
        List<String > incomeTypes = new ArrayList<>();
        for(VariableIncomeTypes type : VariableIncomeTypes.values()){
            incomeTypes.add(type.toString());
        }
        return incomeTypes;
    }

    @Override
    public String toString(){
        return this.typeName;
    }

}
