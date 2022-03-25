package com.assets.manager.domain.types;

import java.util.ArrayList;
import java.util.List;

public enum VariableIncomeTypes {

    STOCK("Stock"),
    FII("FII"),
    ETF("ETF");

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
