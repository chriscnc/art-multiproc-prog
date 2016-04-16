# clj-mutex

Implement the low-level locks from Chapter 2 in Clojure as an exercise in using interop features.

## Usage

```bash
$ lein trampoline run -m clj-mutex.unsafe-core
$ lein trampoline run -m clj-mutex.lock-one-core
$ lein trampoline run -m clj-mutex.lock-two-core
$ lein trampoline run -m clj-mutex.peterson-core
```

## License

Copyright © 2016 Chris Cornelison

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
