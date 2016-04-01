import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    static Logger log = LoggerFactory.getLogger(App.class);

    final static int pCount = 5;

    static String[] forks = new String[pCount];

    static void initForks() {
        for(int i = 0; i < pCount; i++) {
            forks[i] = "avail";
        }
    }

    static void think(int pid) throws InterruptedException {
        Thread.sleep(1000);
    }

    static void eat(int pid) throws InterruptedException {
        Thread.sleep(1000);
    }

    static synchronized boolean pickupForks(int leftFork, int rightFork) {
        boolean success = false;
        if (forks[leftFork] == "avail") {
            forks[leftFork] = "in-use";
            if (forks[rightFork] == "avail") {
                forks[rightFork] = "in-use";
                success = true;
            } else {
                forks[leftFork] = "avail";
            }
        }
        return success;
    }

    static synchronized void putdownForks(int leftFork, int rightFork) {
        forks[leftFork] = "avail";
        forks[rightFork] = "avail";
    }

    public static void main(String[] args) throws Exception {
        initForks();

        // create some philosophers
        Thread[] philosophers = new Thread[pCount];
        for (int i = 0; i < pCount; i++) {
            final int pid = i;
            final int leftFork = pid;
            final int rightFork = (pid + 1) % pCount;
            
            philosophers[i] = new Thread(new Runnable() {
                public void run() {
                    int timeToEat = 10;
                    try {
                        while (timeToEat > 0) {
                            think(pid);
                            boolean canEat = pickupForks(leftFork, rightFork);
                            if (canEat) {
                                eat(pid);
                                log.info(String.format("Philosopher %d: eating... %d left", pid, timeToEat));
                                putdownForks(leftFork, rightFork);
                                timeToEat--;
                            }
                        }
                    } catch(InterruptedException e)  {

                    }
                }
            });
        }
        for (int i = 0; i < pCount; i++) {
            philosophers[i].start();
        }
        for (int i = 0; i < pCount; i++) {
            philosophers[i].join();
        }
        System.out.println("Clearing the table");
    }
}

