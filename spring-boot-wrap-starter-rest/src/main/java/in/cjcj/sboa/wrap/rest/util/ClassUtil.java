package in.cjcj.sboa.wrap.rest.util;

import java.util.HashSet;
import java.util.Set;

public class ClassUtil {
    public static Set<Class> forStringValues(Set<String> set) {
        Set<Class> clsList = new HashSet<>();
        for (Object obj : set) {
            try {
                Class cls = Class.forName(obj.toString());
                clsList.add(cls);
            } catch (Exception ex) {
                // do nothing there may be anonymous class caught that implement error detail but not as enum
            }
        }
        return clsList;
    }
}
