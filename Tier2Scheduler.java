import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class Tier2Scheduler extends PriorityQueue<Process> {

    public Tier2Scheduler() {
        super(
            Comparator.comparingInt(Process::getPriority)
                .thenComparingInt(Process::getArrivalTime)
                .thenComparing(Process::getProcessId)
        );
    }

    public void addProcess(Process p) {
        add(p);
    }

    public int executeNext(int currentTime) {

        if (isEmpty()) {
            return 0;
        }
        
        Process p = poll();

        if (p.getFirstStartTime() == -1) {
            p.setFirstStartTime(currentTime);
        }

        int runTime = p.getRemainingTime();

        p.setRemainingTime(0);

        int completionTime = currentTime + runTime;

        p.setCompletionTime(completionTime);

        return runTime;
    }

    public List<Process> getQueueSnapshot() {

        List<Process> snapshot = new ArrayList<>(this);

        snapshot.sort(
            Comparator.comparingInt(Process::getPriority)
                .thenComparingInt(Process::getArrivalTime)
                .thenComparing(Process::getProcessId)
        );

        return snapshot;
    }

    public void rebuildQueue() {
        List<Process> processes = new ArrayList<>(this);
        clear();
        addAll(processes);
    }
}
