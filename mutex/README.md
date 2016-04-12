# mutex

Java implementations of the various locks in Chapter 2 on mutexes.

## Compiling

```bash
$ ./compile
```

## Running

UnsafeApp - An non-thread-safe counter used from two threads.

```bash
$ java UnsafeApp
```

LockOneApp - A thread-safe counter used from two threads, but with a lock that will dead-lock. acquire() will succeed if threads `are not` interleaved.

```bash
$ java LockOneApp
```

LockTwoApp - A thread-safe counter used from two threads, but with a lock that will dead-lock. acquire() will succeed if threads `are` interleaved.

```bash
$ java LockTwoApp
```

PetersonApp - A thread-safe counter used from two threads and uses a Peterson lock, which combines the approches of LockOne and LockTwo, to acheive starvation freedom, and thus dead-lock freedom.

```bash
$ java PetersonApp
```


Copyright © 2016 Chris Cornelison

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
