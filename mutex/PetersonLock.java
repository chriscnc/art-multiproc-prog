
class PetersonLock implements Lock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public int getIndex() {
        return ((IndexedThread)Thread.currentThread()).getIndex();
    }

    public void acquire() {
        int i = getIndex();
        int j = 1 - i;
        flag[i] = true; // I'm interested
        victim = i;     // you go first
        while(flag[j] && victim == i) {} // wait
    }

    public void release() {
        int i = getIndex();
        flag[i] = false;
    }
}

