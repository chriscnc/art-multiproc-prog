
class PetersonCounter {
    private long value = 0;
    Lock lock = new Peterson();

    public long getAndIncrement() {
        lock.acquire();
        try {
            long temp = value;
            value = temp + 1;
            return temp;
        } finally {
            lock.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AltIndexedThread[] threads = new AltIndexedThread[2];
        PetersonCounter c = new PetersonCounter();
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new AltIndexedThread(i, c);
            /*
            threads[i] = new IndexedThread(i, new Runnable() {
                public void run() {
                    for(int i = 0; i <= 1000000; i++) {
                        c.getAndIncrement();
                    }
                }
            });
            */
        }

        for(int i = 0; i < threads.length; i++) {
            System.out.println(String.format("Starting thread: %d", threads[i].getIndex()));
            threads[i].start();
        }

        for(int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println(String.format("counter should be: 2000000, actual: %d", c.getAndIncrement()));
    }
}
