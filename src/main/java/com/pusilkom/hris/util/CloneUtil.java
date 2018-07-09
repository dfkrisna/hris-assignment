package com.pusilkom.hris.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CloneUtil {
    public static Object cloneViaJSON(Object source, String className) throws ClassNotFoundException{
        Gson gson = new GsonBuilder().serializeNulls().create();
        Object dest = gson.fromJson(gson.toJson(source), Class.forName(className));
        return dest;
    }
}
