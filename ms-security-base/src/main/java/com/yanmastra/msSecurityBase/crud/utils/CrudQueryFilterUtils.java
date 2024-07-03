package com.yanmastra.msSecurityBase.crud.utils;

import com.yanmastra.msSecurityBase.crud.CountValue;
import com.yanmastra.msSecurityBase.security.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

public class CrudQueryFilterUtils {
    private static final Logger log = LogManager.getLogger(CrudQueryFilterUtils.class);

    public static String getQueryWhereClause(Map<String, String> otherQueries, Map<String, Object> queryParams) {
        return getQueryWhereClause(otherQueries, queryParams, "");
    }

    public static String getQueryWhereClause(Map<String, String> otherQueries, Map<String, Object> queryParams, String alias) {
        Set<String> whereClauses = otherQueries.entrySet().stream()
                .filter(stringListEntry -> !Set.of("page", "size", "keyword").contains(stringListEntry.getKey()))
                .map(entry -> {
                    ParamToQuery query = ParamToQuery.factory(entry.getKey(), entry.getValue(), alias);
                    log.debug("query created:"+query);
                    return query;
                })
                .filter(Objects::nonNull)
                .map(item -> {
                    item.attachValue(queryParams);
                   return item.getWhereClause();
                })
                .collect(Collectors.toSet());

        return String.join(" and ", whereClauses);
    }

    public static Long executeCount(String countFilterQuery, Map<String, Object> queryParams, EntityManager entityManager) {
        TypedQuery<CountValue> countValueQuery = entityManager.createQuery(countFilterQuery, CountValue.class);
        for (String key: queryParams.keySet()) {
            countValueQuery.setParameter(key, queryParams.get(key));
        }

        CountValue result = countValueQuery.getSingleResult();
        return result.getValue();
    }

    public static String createFilterQueryCount(UserPrincipal principal, Map<String, String> requestParams, Map<String, Object> sqlParams, Set<String> searchableColumn, Class<?> entityClass) {
        return createFilterQueryCount(principal, requestParams, sqlParams, searchableColumn, "", entityClass);
    }

    public static String createFilterQueryCount(UserPrincipal principal, Map<String, String> requestParams, Map<String, Object> sqlParams, Set<String> searchableColumn, String alias, Class<?> entityClass) {
        if (entityClass == null) throw new IllegalArgumentException("entityClass value should not be null!");

        String whereQuery = createFilterQuery(principal, requestParams, sqlParams, searchableColumn, alias, null);
        whereQuery = "select new com.yanmastra.msSecurityBase.crud.CountValue(count(*)) as value from "+entityClass.getName()+" " + alias + " " + whereQuery;
        return whereQuery;
    }

    public static String createFilterQuery(UserPrincipal principal, Map<String, String> requestParams, Map<String, Object> sqlParams, Set<String> searchableColumn, Sort sort) {
        return createFilterQuery(principal, requestParams, sqlParams, searchableColumn, "", sort);
    }

    public static String createFilterQuery(UserPrincipal principal, Map<String, String> requestParams, Map<String, Object> sqlParams, Set<String> searchableColumn, String alias, Sort sort) {
        requestParams = new HashMap<>(requestParams);
        requestParams.remove("page");
        requestParams.remove("size");

        String keyword = "";
        if (requestParams.containsKey("keyword") && requestParams.get("keyword") != null)
            keyword = requestParams.remove("keyword");

        sqlParams.put("companyIds", new ArrayList<>(principal.getCompanyAccess()));
        String where = "where "+alias+"companyId in (:companyIds) and "+alias+"deletedAt is null";

        StringBuilder sbQuery = new StringBuilder(where);
        if (StringUtils.isNotBlank(keyword)) {
            sqlParams.put("keyword", "%"+keyword+"%");
            Set<String> searchKey = new HashSet<>();
            sbQuery.append(" and (");
            for (String column: searchableColumn) {
                searchKey.add("cast(" + alias + column + " as string) like :keyword");
            }
            sbQuery.append(String.join(" or ", searchKey)).append(")");
        }

        if (!requestParams.isEmpty()) {
            sbQuery.append(" and ");
        }

        if (!requestParams.isEmpty()) {
            sbQuery.append(getQueryWhereClause(requestParams, sqlParams));
        }

        if (sort != null) {
            sbQuery.append(" order by ");
            Iterator<Sort.Order> orderIterator = sort.iterator();

            while (orderIterator.hasNext()) {
                Sort.Order order = orderIterator.next();
                sbQuery.append(order.getProperty()).append(" ")
                        .append(order.getDirection().name());
                if (orderIterator.hasNext())
                    sbQuery.append(", ");
            }
        }
        return sbQuery.toString();
    }
}
