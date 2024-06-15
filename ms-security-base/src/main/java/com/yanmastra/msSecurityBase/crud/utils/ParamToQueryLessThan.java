package com.yanmastra.msSecurityBase.crud.utils;

import java.util.List;
import java.util.Map;

public class ParamToQueryLessThan extends ParamToQuery{
    ParamToQueryLessThan(String key, List<String> value, String alias) {
        super(key, value, alias);
        this.sKey = key.replace(".", "_");
        this.value = value.get(1);
    }
    private final String sKey;
    private final String value;

    @Override
    public String getWhereClause() {
        return alias + key + " <= :"+sKey;
    }

    @Override
    public void attachValue(Map<String, Object> hibernateQueryParams) {
        hibernateQueryParams.put(sKey, value);
    }
}
