import java.util.concurrent.atomic.AtomicIntegerArray;

class FilterLock implements Lock {
//    volatile int[] level;
//    volatile int[] victim;
    AtomicIntegerArray level;
    AtomicIntegerArray victim;
    volatile int dummy;
    final int n;

    public FilterLock(int n) {
//        level = new int[n];
        level = new AtomicIntegerArray(n);
//        victim = new int[n]; // use 1..n-1
        victim = new AtomicIntegerArray(n); // use 1..n-1
        for (int i = 0; i < n; i++) {
            //level[i] = 0;
            level.set(i, 0);
        }
        dummy = 0;
        this.n = n;
    }

    public void acquire() {
        int me = ThreadID.get();
        for (int i = 1; i < n; i++) { // attempt level i
//            level[me] = i;
            level.set(me, i);
//            victim[i] = me;
            victim.set(i, me);
            dummy = 0;

            //spin while conflicts exist
            boolean conflict = true;
            while (conflict) {
                conflict = false;
                for (int k = 1; k < n; k++) {
                    //if (k != me && level[k] >= i && victim[i] == me) {
                    int d = dummy;
                    if (k != me && level.get(k) >= i && victim.get(i) == me) {
                        conflict = true;
                        break;
                    }
                }
            }
        }
    }

    public void release() {
        int me = ThreadID.get();
        //level[me] = 0;
        level.set(me, 0);
        dummy = 0;
    }
}
            


