
class LockTwo implements Lock {
    private volatile int victim;

    public int getIndex() {
        return ((IndexedThread)Thread.currentThread()).getIndex();
    }

    public void acquire() {
        int i = getIndex();
        victim = i; // let the other go first
        while(victim == i) {} // wait
    }

    public void release() {}
}

