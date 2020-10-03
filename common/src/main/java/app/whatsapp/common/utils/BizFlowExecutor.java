package app.whatsapp.common.utils;

import app.whatsapp.common.models.*;
import app.whatsapp.common.models.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BizFlowExecutor {

    private static final int cpuCores = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(cpuCores);

    private BizFlowExecutor(){

    }

    public static FlowResponseBean execute(FlowRequestBean requestBean, BizFlow bizFlow) {
        FlowTransactionBean flowTransactionBean = new FlowTransactionBean(requestBean);
        FlowResponseBean flowResponseBean = new FlowResponseBean();
        List<Set<BizTask>> taskSets = bizFlow.getTaskSetList();
        for (Set<BizTask> taskSet : taskSets) {
            CountDownLatch countDownLatch = new CountDownLatch(taskSet.size());
            for (BizTask bizTask : taskSet) {
                bizTask.setLatch(countDownLatch);
                executorService.execute(() -> bizTask.execute(flowTransactionBean, flowResponseBean));
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flowResponseBean;
    }

}
