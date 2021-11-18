package in.cjcj.sboa.wrap.rest.errors;

import in.cjcj.sboa.wrap.rest.errors.codes.ErrorDetail;
import in.cjcj.sboa.wrap.rest.util.AnnotationUtil;
import in.cjcj.sboa.wrap.rest.util.ClassFinder;
import in.cjcj.sboa.wrap.rest.util.ClassUtil;
import in.cjcj.sboa.wrap.rest.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

import java.util.*;

/**
 * All error codes should be represented as enum values in this application.
 */
@Slf4j
public class ErrorDetailResolver {
    private static Map<String, ErrorDetail> errorDetails;

    static {
        errorDetails = new HashMap<>();

    }

    private static void loadAllErrorDetails() {
        String packageName = AnnotationUtil.findPackageNameForClassWithAnnotation(SpringBootApplication.class);
        loadAllErrorDetails(packageName);
        loadAllErrorDetails(ErrorDetailResolver.class.getPackageName());
    }

    private static void loadAllErrorDetails(String packageName) {
        Set<String> errorDetailImplementationClass = new ClassFinder(ErrorDetail.class, packageName).getClasses();
        Set<Class> classSet = ClassUtil.forStringValues(errorDetailImplementationClass);

        for (Class cls : classSet) {
            ExceptionUtil.suppress(() -> {
                for (Object enumObj : EnumSet.allOf(cls)) {
                    Enum en = (Enum) enumObj;
                    if (errorDetails.containsKey(en.name())) {
                        log.error("error enum name should be uniq across all error enum class. " +
                                "error enum name {} is found on multiple enum class", en.name());
                        System.exit(0);
                    }
                    ErrorDetail errorDetail = (ErrorDetail) enumObj;
                    errorDetails.put(en.name(), errorDetail);
                }
            });
        }
    }

    public static ErrorDetail resolve(String code) {
        ErrorDetail errorDetail = ErrorDetailResolver.errorDetails.get(code);
        Assert.notNull(errorDetail, "errorDetail should not be null");
        return errorDetail;
    }

    public static Set<ErrorDetail> getAllErrorCodes() {
        return new HashSet<>(errorDetails.values());
    }
}
