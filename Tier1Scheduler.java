import java.util.LinkedList;
import java.util.Queue;

public class Tier1Scheduler {
    private Queue<Process> queue;
    private int quantum;

    public Tier1Scheduler(int quantum) {
        this.quantum = quantum;
        this.queue = new LinkedList<>();
    }

    // إضافة عملية لطابور Tier 1
    public void addProcess(Process p) {
        queue.add(p);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getQuantum() {
        return quantum;
    }

    /**
     * تنفيذ عملية واحدة بشريحة الـ Quantum:
     * - تحديث وقت أول استجابة (First Start Time)
     * - خصم الوقت من Remaining Time
     * - لو انتهت: نسجل وقت الانتهاء
     * - لو ما انتهتش: ننقلها لطابور Tier 2
     * 
     * بترجع الوقت المستهلك عشان الـ ACS يقدم الـ currentTime
     */
    public int executeNext(int currentTime, Queue<Process> tier2Queue) {
        if (queue.isEmpty()) {
            return 0;
        }

        Process p = queue.poll();

        // تسجيل زمن أول استجابة
        if (p.getFirstStartTime() == -1) {
            p.setFirstStartTime(currentTime);
        }

        // تشغيل العملية (أيهما أقل: الوقت المتبقي أو الـ Quantum)
        int runTime = Math.min(p.getRemainingTime(), quantum);
        p.setRemainingTime(p.getRemainingTime() - runTime);

        int endTime = currentTime + runTime;

        // فحص حالة العملية
        if (p.getRemainingTime() == 0) {
            p.setCompletionTime(endTime); // انتهت وخرجت
        } else {
            tier2Queue.add(p); // استهلكت الـ Quantum وما خلصتش -> تروح Tier 2
        }

        return runTime;
    }
}
