
public class App {

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[2];
        Counter c = new Counter(0);
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for(int i = 0; i <= 1000000; i++) {
                        c.getAndIncrement();
                    }
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println(c.getAndIncrement());
    }
}
