
/*
 * A Thread class where a pseudo id can be controlled
 */
class AltIndexedThread extends Thread {
    int index;
    PetersonCounter c;

    public AltIndexedThread(int index, PetersonCounter c) {
        this.index = index;
        this.c = c;
    }

    public void run() {
        for(int i = 0; i <= 1000000; i++) {
            c.getAndIncrement();
        }
    }

    int getIndex() {
        return index;
    }
}
