package com.railwayservice.application.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class RailwayStationCache {

    private static net.sf.ehcache.Cache cache;

    static {
        cache = new net.sf.ehcache.Cache("RailwayStationCache", 10000, false, false, 7000, 0);
        CacheManager.getInstance().addCache(cache);
    }

    public static void put(String key, Object value) {
        Element element = new Element(key, value);
        cache.put(element);
    }

    public static <E> E get(String key) {
        Element element = cache.get(key);
        return element == null ? null : (E) element.getObjectValue();
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static void removeAll() {
        cache.removeAll();
    }

    public static long getSize() {
        return cache.getSize();
    }

}
