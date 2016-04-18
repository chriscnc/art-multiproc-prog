
class LockOne implements Lock {
    private volatile boolean[] flag = new boolean[2];

    public void acquire() {
        int i = ThreadID.get();
        int j = 1 - i;
        flag[i] = true;
        while(flag[j]) {} // wait
    }

    public void release() {
        int i = ThreadID.get();
        flag[i] = false;
    }
}

