package in.cjcj.sboa.wrap.demo.web;

import in.cjcj.sboa.wrap.rest.errors.AppErrors;
import in.cjcj.sboa.wrap.rest.errors.ErrorBuilder;
import in.cjcj.sboa.wrap.rest.errors.codes.CommonResourceErrorCode;
import in.cjcj.sboa.wrap.rest.exception.AppException;
import in.cjcj.sboa.wrap.rest.response.builder.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class DemoCtrl {

    @GetMapping("/demo/ok/single")
    public ResponseEntity ok() {
        return ResponseBuilder
                .okResponse()
                .data("cj")
                .ok()
                .build();
    }

    @GetMapping("/demo/ok/list")
    public ResponseEntity okList() {
        return ResponseBuilder
                .okResponse()
                .data(Arrays.asList("Cj", "MW"))
                .ok()
                .build();
    }

    @GetMapping("/demo/error/single")
    public ResponseEntity error() {
        CommonResourceErrorCode entityOverrideError = CommonResourceErrorCode.ENTITY_OVERRIDE_ERROR;
        throw new AppException(entityOverrideError);
    }

    @GetMapping("/demo/error/multi")
    public ResponseEntity errorMulti() {
        CommonResourceErrorCode entityOverrideError = CommonResourceErrorCode.ENTITY_OVERRIDE_ERROR;
        ErrorBuilder errorBuilder = ErrorBuilder.withError(CommonResourceErrorCode.VALIDATION_ERROR).subError(CommonResourceErrorCode.ENTITY_OVERRIDE_ERROR);
        throw new AppException(errorBuilder);
    }
}
