# Remit

A tiny (experimental) ClojureScript event-handling library using core.async

## Getting It

remit is available on Clojars:

[![Clojars Project](http://clojars.org/pointslope/remit/latest-version.svg)](http://clojars.org/pointslope/remit)

## Motivation

We like [reframe's](https://github.com/Day8/re-frame) approach to building ClojureScript SPAs using Reagent. In particular, we love the idea of the unidirectional reactive loop. Like reframe's author, we have found that emitting events and having them handle state changes makes it easier to reason about our code. This project takes a slightly different approach to the central event dispatch, however:

* events are maps

when you **emit** an event, we create a map containing two keys `:event` and `:data`.
the `:event` key specifies the name of the event passed as the first argument to emit.
the `:data` key contains any (optional) data you might have passed as the second argument
to emit.

* event maps are published onto core.async channels using pub/sub

we feel that this provides for greater flexibility by allowing multiple event handlers for a single event, and dynamic event un/subscription.

* emit/subscribe don't know about your app-db

we didn't feel it was appropriate for an event library to manage your state atom.
we did, however, ship an **event-map-middleware** which makes it easy for you to
add your app-db atom to the event map (much the way we do in Ring apps).

```clojure

;;; Write something like this...
(defn wrap-db
  "Adds the app-db to the event map under the :db key"
  [handler]
  (-> handler
    (event-map-middleware :db app-db)))

(subscribe :missiles-launched
           (wrap-db
             (fn [{db :db {num :icbms} :data}]
               (swap! db #(update-in % [:missiles] (partial + num))))))
```

### Why not simply use DOM event handlers?

DOM events like `:on-click` live at a different level of abstraction. They are passed a browser-oriented event object (React synthetic event to be precise). By defining events that are application-centric instead of browser-centric, we force the DOM event handler to parse out and repackage information using keys that make more sense to the application domain. Also, it helps us resist the temptation to push logic that changes *application state* inside of our view components. Component-local state can and should still be managed by the components, but not app state. We can also have several browser events emit the same application event. For instance, both an `:on-click` and `:key-down` could emit a `:missiles-launched` event.

## Maturity

This is a *very* young project that we are using to vet out our ideas about developing SPAs with [reagent](https://github.com/reagent-project/reagent). It has not been significantly battle tested, blah, blah, blah. Oh, what the hell&mdash;just go ahead and try it out. The whole library is under 150 lines of code including docstrings. If you encounter a bug, please open an issue (or better yet submit a pull request) and we'll get right to fixing it.

## Copyright

Copyright 2015 Point Slope, LLC

Licensed under the terms of the [Eclipse Public License](https://www.eclipse.org/legal/epl-v10.html)
(same as Clojure & ClojureScript)
