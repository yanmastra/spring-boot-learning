package com.yanmastra.msSecurityBase.crud.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParamToQueryNotIn extends ParamToQuery{
    ParamToQueryNotIn(String key, List<String> value, String alias) {
        super(key, value, alias);
        this.sKey = key.replace(".", "_");
        for (int i = 1; i < value.size(); i++) {
            this.value.add(value.get(i));
        }
    }
    private final String sKey;
    private final Set<String> value = new HashSet<>();

    @Override
    public String getWhereClause() {
        return alias + key + " not in (:"+sKey+")";
    }

    @Override
    public void attachValue(Map<String, Object> hibernateQueryParams) {
        hibernateQueryParams.put(sKey, value);
    }
}
