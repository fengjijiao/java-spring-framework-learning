package us.fjj.spring.learning.enableasynandasynccusage.test2;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Async
@Component
public class GoodsService {
    public Future<String> getGoodsInfo(long goodsId) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        return AsyncResult.forValue(String.format("商品%d的基本信息。", goodsId));
    }
    public Future<String> getGoodsDesc(long goodsId) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        return AsyncResult.forValue(String.format("商品%d的描述信息，", goodsId));
    }
    public Future<List<String>> getGoodsComments(long goodsId) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        return AsyncResult.forValue(Arrays.asList("评论1","评论2","评论3"));
    }
}
