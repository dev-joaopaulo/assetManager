package com.assets.manager.domain.types;

import java.util.ArrayList;
import java.util.List;

public enum FixedIncomeTypes {

    TESOURO_DIRETO("Tesouro Direto"),
    LCI("LCI"),
    LCA("LCA"),
    CDB("CDB"),
    CRI("CRI"),
    CRA("CRA");

    private final String typeName;

    FixedIncomeTypes(String typeName) {
        this.typeName = typeName;
    }

    public List<String> listTypes(){
        List<String > incomeTypes = new ArrayList<>();
        for(FixedIncomeTypes type : FixedIncomeTypes.values()){
            incomeTypes.add(type.toString());
        }
        return incomeTypes;
    }

    @Override
    public String toString(){
        return this.typeName;
    }

}
