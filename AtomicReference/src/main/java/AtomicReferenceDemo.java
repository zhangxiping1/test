import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    private Reference reference;

    private AtomicReference<Reference> atomicReference;

    /**
     * 构建器中初始化AtomicReference
     *
     * @param reference
     */
    public AtomicReferenceDemo(Reference reference) {
        this.reference = reference;
        this.atomicReference = new AtomicReference<>(reference);
    }

    public void atomic(Reference reference) {
        Reference referenceOld;
        Reference referenceNew;

        long sequence;
        long timestamp;

        while (true) {
            referenceOld = this.atomicReference.get();
            sequence = referenceOld.getSequence();
            sequence++;
            timestamp = System.currentTimeMillis();

            referenceNew = new Reference(sequence, timestamp);
            /**
             * 比较交换
             */
            if (this.atomicReference.compareAndSet(referenceOld, referenceNew)) {
                reference.setSequence(sequence);
                reference.setTimestamp(timestamp);
                break;
            }
        }
    }
}

/**
 * 业务场景模拟
 * 序列需要自增并且时间需要更新成最新的时间戳
 */

class Reference {
    /**
     * 序列
     */
    private long sequence;
    /**
     * 时间戳
     */
    private long timestamp;

    public Reference(long sequence, long timestamp) {
        this.sequence = sequence;
        this.timestamp = timestamp;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
