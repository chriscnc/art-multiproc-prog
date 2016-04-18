
class LockTwo implements Lock {
    private volatile int victim;

    public void acquire() {
        int i = ThreadID.get();
        victim = i; // let the other go first
        while(victim == i) {} // wait
    }

    public void release() {}
}

