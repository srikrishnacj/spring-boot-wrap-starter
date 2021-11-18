package in.cjcj.sboa.wrap.rest.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtil {
    private final static Gson gson = new Gson();

    public static <T> T fromJson(Object data, Class<T> type) {
        String json = getJson(data);
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(Object data, Type type) {
        String json = getJson(data);
        return gson.fromJson(json, type);
    }

    private static String getJson(Object data) {
        String json = null;
        if (data instanceof String) {
            json = (String) data;
        } else {
            json = gson.toJson(data);
        }
        return json;
    }
}
