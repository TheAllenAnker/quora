package com.allenanker.quora.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    private Map<String, Object> paramsMap = new HashMap<>();

    public void set(String key, Object object) {
        paramsMap.put(key, object);
    }

    public Object get(String key) {
        return paramsMap.get(key);
    }
}
