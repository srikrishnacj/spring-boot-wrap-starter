package in.cjcj.sboa.wrap.rest.web;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@RestController
@ApiIgnore
public class RootSwaggerCtrl {
    private String uiPath;

    public RootSwaggerCtrl(Environment environment) {
        this.uiPath = environment.getProperty("server.servlet.context-path", "");
        // possible context path is "/"
        if(uiPath.length() == 1){
            this.uiPath = "";
        }
        this.uiPath = this.uiPath + "/swagger-ui/#/";
    }

    @GetMapping(path = "/")
    public ResponseEntity<Object> redirectToSwagger() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(uiPath));
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }
}
