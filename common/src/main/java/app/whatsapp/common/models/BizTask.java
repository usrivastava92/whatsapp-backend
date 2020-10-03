package app.whatsapp.common.models;

import java.util.concurrent.CountDownLatch;

public abstract class BizTask {

    private CountDownLatch countDownLatch;

    public abstract boolean isExecutable();

    public void execute(FlowTransactionBean flowTransactionBean, FlowResponseBean flowResponseBean) {
        try {
            if (isExecutable()) {
                executeTask(flowTransactionBean, flowResponseBean);
            }
        } finally {
            this.countDownLatch.countDown();
        }
    }


    public abstract void executeTask(FlowTransactionBean flowTransactionBean, FlowResponseBean flowResponseBean);

    public void setLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

}
