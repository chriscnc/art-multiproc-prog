
class LockOneCounter {
    private long value = 0;
    Lock lock = new LockOne();

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
        IndexedThread[] threads = new IndexedThread[2];
        LockOneCounter c = new LockOneCounter();
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new IndexedThread(i, new Runnable() {
                public void run() {
                    for(int i = 0; i <= 1000000; i++) {
                        c.getAndIncrement();
                    }
                }
            });
        }

        for(int i = 0; i < threads.length; i++) {
            System.out.println(String.format("Starting thread: %d", threads[i].getIndex()));
            threads[i].start();
        }
        
        System.out.println("When thread executions are interleaved, this will dead-lock.");

        for(int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println(String.format("counter should be: 2000000, actual: %d", c.getAndIncrement()));
    }
}
