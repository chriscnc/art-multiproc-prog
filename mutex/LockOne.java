
class LockOne implements Lock {
    private boolean[] flag = new boolean[2];

    public int getIndex() {
        return ((IndexedThread)Thread.currentThread()).getIndex();
    }

    public void acquire() {
        int i = getIndex();
        int j = 1 - i;
        flag[i] = true;
        while(flag[j]) {} // wait
    }

    public void release() {
        int i = getIndex();
        flag[i] = false;
    }
}

