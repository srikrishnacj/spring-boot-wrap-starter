package in.cjcj.sboa.wrap.rest.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionUtil {

    @FunctionalInterface
    public interface RunnableWithException {
        void run() throws Exception;
    }

    /**
     * Can be used to suppress any exception that code can throw
     *
     * @param runnableWithException
     */
    public static void suppress(RunnableWithException runnableWithException) {
        try {
            runnableWithException.run();
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * Converts an exception into list strings. ex: each stack trace line is converted string
     *
     * @param exception
     * @return
     */
    public static List<String> formatToPrettyString(Throwable exception) {
        if (exception == null) return new LinkedList<>();
        List<String> list = Arrays.stream(exception.getStackTrace()).map(trace -> trace.toString()).collect(Collectors.toList());
        if (StringUtils.hasText(exception.getMessage()))
            list.add(0, exception.getMessage());
        list.add(0, exception.getClass().getName());
        return list;
    }
}