package com.github.guilhermevictorlima.lombardi.utils;

public class QuerySearchUtils {

    public static boolean isUrl(String query) {
        return query.startsWith("https://") || query.startsWith("http://");
    }

    public static String getFormattedQuery(String query) {
        return isUrl(query) ? query : "ytsearch: " + query;
    }


}
