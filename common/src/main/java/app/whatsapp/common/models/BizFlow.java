package app.whatsapp.common.models;

import java.util.*;

public abstract class BizFlow {

    private List<Set<BizTask>> taskSetList;

    public BizFlow() {
        taskSetList = new ArrayList<>();
        populateTaskSetList();
    }

    public List<Set<BizTask>> getTaskSetList() {
        return taskSetList;
    }

    public void addTask(int index, BizTask bizTask) {
        if (index < 0) {
            return;
        }
        if (taskSetList.isEmpty() || index >= taskSetList.size()) {
            int currentSize = taskSetList.size();
            while (currentSize <= index) {
                taskSetList.add(new HashSet<>());
                currentSize++;
            }
        }
        taskSetList.get(index).add(bizTask);
    }

    public abstract void populateTaskSetList();

}
