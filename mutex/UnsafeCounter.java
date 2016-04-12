
class UnsafeCounter implements Counter {
    private long value = 0;

    public long getAndIncrement() {
        long temp = value;
        value = temp + 1;
        return temp;
    }

    public String toString() {
        return String.format("Counter value: %d", value);
    }
}

