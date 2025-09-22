package cn.topiam.employee.support.async;

import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.MDC;

/**
 * Asynchronous logging utility class
 * Used to pass MDC context information in asynchronous tasks
 */
public class AsyncLoggingUtils {
    
    /**
     * Wrap Supplier to pass MDC context
     * 
     * @param supplier Original Supplier
     * @return Wrapped Supplier that can be used with MDC context in asynchronous tasks
     * @param <U> Return value type
     */
    public static <U> Supplier<U> withMdc(Supplier<U> supplier) {
        // Get the MDC context of the current thread
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            // Set MDC context in asynchronous tasks
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            // Execute the original Supplier and return the result
            return supplier.get();
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AsyncLoggingUtils() {
    }
}