
class PetersonApp {

    public static void main(String[] args) throws InterruptedException {
        final int iters = 1000000;
        Thread[] threads = new Thread[2];
        SafeCounter c = new SafeCounter(new PetersonLock());
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

        for(int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println(String.format("counter should be: %d", iters * threads.length));
        System.out.println(c);
    }
}
