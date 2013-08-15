package com.dslplatform.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

class Utils {
    public static <T> ArrayList<T> toArrayList(final Iterable<T> iterable) {
        final ArrayList<T> copy = new ArrayList<T>();
        for(final T t: iterable) copy.add(t);
        return copy;
    }

    private static void appendOrder(
            final StringBuilder sb,
            final Iterable<Map.Entry<String, Boolean>> order,
            final boolean isFirst) {
        if (order != null && order.iterator().hasNext()) {
            sb.append(isFirst ? "?order=" : "&order=");
            for(final Map.Entry<String, Boolean> el: order) {
                // null Boolean object will default to true
                if(el.getValue() == Boolean.FALSE) {
                    sb.append('-');
                }
                sb.append(el.getKey()).append(',');
            }
            sb.setLength(sb.length() - 1);
        }
    }

    public static String appendLimitOffsetOrder(
            final String url,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order,
            final Boolean isFirst) {
        final StringBuilder sB = new StringBuilder(url);
        final boolean limitnull = limit == null;
        final boolean offsetnull = offset == null;
        final boolean orderFirst = limitnull && offsetnull && isFirst;

        if(!limitnull)
            sB.append(isFirst ? "?limit=" : "&limit=").append(limit);
        if(!offsetnull)
            sB.append(limitnull && isFirst ? "?offset=" : "&offset=").append(offset);

        appendOrder(sB, order, orderFirst);
        return sB.toString();
    }

    public static void joinIfNonEmpty(
            final StringBuilder sB,
            final String param,
            final Iterable<String> args) {
        if (args == null) return;

        final Iterator<String> aI = args.iterator();
        if (!aI.hasNext()) return;

        sB.append(param).append(aI.next());
        while (aI.hasNext()) sB.append(',').append(aI.next());
    }

    public static String buildOlapArguments(
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order) {
        final StringBuilder query = new StringBuilder();

        if (order != null && order.iterator().hasNext()
            && !contains(dimensions, order)
            && !contains(facts, order)) throw new IllegalArgumentException("Order must be an element of dimmensions or facts!");
        joinIfNonEmpty(query, "?dimensions=", dimensions);
        joinIfNonEmpty(query, query.length() > 0 ? "&facts=" : "?facts=", facts);

        if (query.length() == 0)
            throw new IllegalArgumentException("At least one dimension or fact is required");

        appendOrder(query, order, false);

        return query.toString();
    }

    private static Boolean contains(final Iterable<String> iterSource, final Iterable<Map.Entry<String, Boolean>> orders) {
      if (iterSource == null) return false;
      for(final Map.Entry<String, Boolean> ord: orders) {
        if (! contains(iterSource, ord.getKey())) return false;
      }
      return true;
    }

    private static Boolean contains(final Iterable<String> iterSource, final String orders){
      for(final String src: iterSource){
        if (orders.equals(src)) return true;
      }
      return false;
    }
}
