package cn.topiam.employee.support.async;

import cn.topiam.employee.support.util.DesensitizationUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.lang.NonNull;

/**
 * Exception handling asynchronous task executor
 * Wraps AsyncTaskExecutor to provide a unified exception handling mechanism
 */
public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {
    
    /**
     * Delegated asynchronous task executor
     */
    private final AsyncTaskExecutor delegateExecutor;
    
    /**
     * Logger
     */
    private final Logger logger;

    /**
     * Submit Runnable task
     * 
     * @param task Task to execute
     * @return Future object that can be used to check task status
     */
    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return this.delegateExecutor.submit(this.wrap(task));
    }

    /**
     * Wrap Runnable task to handle exceptions
     * 
     * @param task Original task
     * @return Wrapped task
     */
    private Runnable wrap(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception ex) {
                this.handle(ex);
            }
        };
    }

    /**
     * Handle exception
     * 
     * @param ex Exception object
     */
    public void handle(Exception ex) {
        this.logger.error("Exception occurred in async task execution", ex);
    }

    /**
     * Initialize after properties are set
     * 
     * @throws Exception Initialization exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.delegateExecutor instanceof InitializingBean) {
            ((InitializingBean) this.delegateExecutor).afterPropertiesSet();
        }
    }

    /**
     * Constructor
     * 
     * @param delegateExecutor Delegated asynchronous task executor
     */
    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor delegateExecutor) {
        this.logger = LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);
        this.delegateExecutor = delegateExecutor;
    }

    /**
     * Destroy method
     * 
     * @throws Exception Destruction exception
     */
    @Override
    public void destroy() throws Exception {
        if (this.delegateExecutor instanceof DisposableBean) {
            ((DisposableBean) this.delegateExecutor).destroy();
        }
    }

    /**
     * Wrap Callable task to handle exceptions
     * 
     * @param task Original task
     * @param <T> Task return value type
     * @return Wrapped task
     */
    private <T> Callable<T> wrap(Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception ex) {
                this.handle(ex);
                throw ex;
            }
        };
    }

    /**
     * Execute task
     * 
     * @param task Task to execute
     */
    @Override
    public void execute(@NonNull Runnable task) {
        this.delegateExecutor.execute(this.wrap(task));
    }

    /**
     * Submit Callable task
     * 
     * @param task Task to execute
     * @param <T> Task return value type
     * @return Future object that can be used to get task result
     */
    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return this.delegateExecutor.submit(this.wrap(task));
    }
}