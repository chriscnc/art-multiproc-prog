
/*
 * A Thread class where a pseudo id can be controlled
 */
class IndexedThread extends Thread {
    int index;
    Runnable runnable;

    public IndexedThread(int index, Runnable runnable) {
        this.index = index;
        this.runnable = runnable;
    }

    public void run() {
        runnable.run();
    }

    int getIndex() {
        return index;
    }
}
