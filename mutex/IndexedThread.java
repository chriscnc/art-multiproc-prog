
/*
 * A Thread class where a pseudo id can be controlled
 */
class IndexedThread extends Thread {
    int index;

    public IndexedThread(int index, Runnable target) {
        super(target);
        this.index = index;
    }

    int getIndex() {
        return index;
    }
}
