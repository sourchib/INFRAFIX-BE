package com.infrafix.citizen_reporting.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransformPagination {
    private String sortByColumn;
    private String sort;

    String [] sortArr = new String[2];

    public Map<String,Object> transformPagination(
            List ls,
            Page page,
            String column,
            String value
    ){
        Sort s = page.getSort();
        sortArr = s.toString().split(":");
        sortByColumn = sortArr[0];
        Boolean isSorted = sortByColumn.equals("UNSORTED");
        sortByColumn = isSorted ? "id" : sortByColumn;
        sort = isSorted ? "asc" : sortArr[1];

        Map<String,Object> map = new HashMap<>();
        map.put("content", ls);
        map.put("total_data", page.getTotalElements());
        map.put("total_pages", page.getTotalPages());
        map.put("current_page", page.getNumber());
        map.put("size_per_page", page.getSize());
        map.put("sort_by", sortByColumn);
        map.put("sort", sort.trim().toLowerCase());
        map.put("column_name", column);
        map.put("value", value == null ? "":value);
        return map;
    }
}
