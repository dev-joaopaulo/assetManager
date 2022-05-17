package com.assets.manager.types;

import java.util.ArrayList;
import java.util.List;

public enum FixedIncomeTypes {

    TESOURO_DIRETO("tesouro direto"),
    LCI("lci"),
    LCA("lca"),
    CDB("cdb"),
    CRI("cri"),
    CRA("cra");

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
