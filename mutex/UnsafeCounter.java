
class UnsafeCounter {
    private long value = 0;

    public long getAndIncrement() {
        long temp = value;
        value = temp + 1;
        return temp;
    }

    public static void main(String[] args) throws InterruptedException {
        IndexedThread[] threads = new IndexedThread[2];
        UnsafeCounter c = new UnsafeCounter();
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new IndexedThread(i, new Runnable() {
                public void run() {
                    for(int i = 0; i <= 1000000; i++) {
                        c.getAndIncrement();
                    }
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            System.out.println(String.format("Starting thread: %d", threads[i].get()));
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println(String.format("counter should be: 2000000, actual: %d", c.getAndIncrement()));
    }
}
