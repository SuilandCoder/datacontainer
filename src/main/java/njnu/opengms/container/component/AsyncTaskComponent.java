package njnu.opengms.container.component;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName AsyncTaskComponent
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Component
public class AsyncTaskComponent {
    @Async
    public void executeAsyncTask(Integer integer) {
        System.out.println("执行异步任务：" + integer);
    }

    @Async
    public void executeAsyncTaskPlus(Integer integer) {
        System.out.println("执行异步任务Plus：" + (integer + 1));
    }
}
