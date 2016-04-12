
class SafeCounter implements Counter {
    private long value = 0;
    final Lock lock;

    public SafeCounter(Lock lock) {
        this.lock = lock;
    }

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

    public String toString() {
        return String.format("Counter value: %d", value);
    }

}
