package in.cjcj.sboa.wrap.rest.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloCtrl {

    @GetMapping("/wrap/hello")
    public Map hello() {
        Map map = new HashMap();
        map.put("name", "cj");
        return map;
    }
}
