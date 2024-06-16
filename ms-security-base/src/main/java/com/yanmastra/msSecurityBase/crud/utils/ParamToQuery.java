package com.yanmastra.msSecurityBase.crud.utils;

import com.yanmastra.msSecurityBase.configuration.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

abstract class ParamToQuery {
    private static final Logger log = LogManager.getLogger(ParamToQuery.class.getName());

    protected String key;
    protected String alias;

    ParamToQuery(String key, List<String> value, String alias){
        this.key = key;
        this.alias = alias;
    }

    abstract String getWhereClause();
    abstract void attachValue(Map<String, Object> hibernateQueryParams);

    static ParamToQuery factory(String key, String value, String alias) {
        if (StringUtils.isBlank(value))
            throw new IllegalArgumentException("Wrong values supplied to query param: "+key+", value:"+value);

        List<String> values = List.of(value.split(","));
        if (values.isEmpty()) return null;
        if ((value.startsWith("range") || value.startsWith("in") || value.startsWith("notIn") ||
                value.startsWith("greaterThan") || value.startsWith("lessThan") ||
                value.startsWith("notEquals")) && values.size() == 1
        ) {
            throw new IllegalArgumentException("Wrong values supplied to query param: "+key+", value:"+value);
        }

        log.info("creating query:"+key+", "+values.size()+", items:"+values);

        if (values.size() == 1) {
            String first = values.getFirst();
            if (StringUtils.isNotBlank(first) && first.contains(",")) {
                values = Arrays.asList(first.split(","));
            }
        }

        if (values.size() == 1)
            return new ParamToQueryEquals(key, values, alias);
        if (values.size() >= 3) {
            if ("range".equals(values.getFirst())) {
                return new ParamToQueryRange(key, values, alias);
            } else if ("in".equals(values.getFirst())) {
                return new ParamToQueryIn(key, values, alias);
            } else if ("notIn".equals(values.getFirst())) {
                return new ParamToQueryNotIn(key, values, alias);
            }
        } else {
            if ("greaterThan".equals(values.getFirst())) {
                return new ParamToQueryGreaterThan(key, values, alias);
            } else if ("lessThan".equals(values.getFirst())) {
                return new ParamToQueryLessThan(key, values, alias);
            } else if ("notEquals".equals(values.getFirst())) {
                return new ParamToQueryNotEquals(key, values, alias);
            }
        }
        throw new HttpException("Wrong values supplied to query param: "+key+", values:"+values, HttpStatus.BAD_REQUEST);
    }
}
