# Remit

A tiny ClojureScript event-handling library using core.async

## Purpose

We like reframe's approach to building ClojureScript SPAs using Reagent. In particular, we love the idea of the unidirectional reactive loop. Like the author, we have found that emitting events and having them handle state changes simplifies code quite a bit. This project takes a slightly different approach to central event dispatch, however:

* core.async channels are used for event pub/sub
we feel that this provides for greater flexibility (e.g. multiple handlers for a single event, dynamic event un/subscription)

TODO: more info

## Copyright

Copyright 2015 Point Slope, LLC

Licensed under the terms of the [Eclipse Public License](https://www.eclipse.org/legal/epl-v10.html)
(same as Clojure & ClojureScript)
