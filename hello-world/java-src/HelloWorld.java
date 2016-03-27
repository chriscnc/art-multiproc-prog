public class HelloWorld {
    public static void main(String[] args) throws InterruptedException {
        Thread[] thread = new Thread[8];
        for (int i = 0; i < thread.length; i++) {
            final String message = "Hello world from thread" + i;
            thread[i] = new Thread(new Runnable() {
                public void run() {
                    System.out.println(message);
                }
            });
        }
        for (int i = 0; i < thread.length; i++) {
            thread[i].start();
        }
        for (int i = 0; i < thread.length; i++) {
            thread[i].join();
        }
    }
}
