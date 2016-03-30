# dining-philosophers

Simulation of the dining philosphers problem using Clojure's software transactional memory.

## Running

```bash
$ lein trampoline run
```

The simulation will run with 10 philosophers and until each one has eaten for 20 seconds.

These parameters can be changed by editing `core.clj`

```clojure
(def p-count 10)
(def max-eating-time 20)
```

Copyright © 2016 Chris Cornelison

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
