
class LockTwoApp {
    public static void main(String[] args) throws InterruptedException {
        final int iters = 1000000;
        Thread[] threads = new Thread[2];
        SafeCounter c = new SafeCounter(new LockTwo());
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for(int i = 0; i < iters; i++) {
                        c.getAndIncrement();
                    }
                }
            });
        }

        for(int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        
        System.out.println("When thread executions are interleaved, this will dead-lock.");
        System.out.println("Time out in 5 seconds");

        for(int i = 0; i < threads.length; i++) {
            threads[i].join(2500);
//            threads[i].stop();
        }

        System.out.println(String.format("counter should be: %d", iters * threads.length));
        System.out.println(c);
        System.exit(1);
    }
}
