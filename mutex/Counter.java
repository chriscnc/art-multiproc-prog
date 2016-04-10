
class Counter {
    private long value;
    private Lock lock;

    public Counter(int c) {
        value = c;
    }

    public long getAndIncrement() {
//        lock.acquire();
        try {
            long temp = value;
            value = temp + 1;
            return temp;
        } finally {
 //           lock.release();
        }
    }
}
