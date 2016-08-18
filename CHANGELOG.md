
0.3.0 / 2016-08-18
==================

  * Convert to cljc for Clojure / Clojure Script
  * Fixed bug where un-pubsub wasn't working

0.2.0 / 2016-06-10
==================

  * Wiretap and better exception handling
    
    Exception handling code for subscribe was
    broken (throwing an exception inside a go
    loop is useless). We now catch exceptions
    thrown from subscription handlers and emit
    a :pointslope.remit.events/exception event.

    Added wiretap capabilities to see all
    messages published onto the event channel.
    This makes it easy to log everything going
    through the pub/sub mechanism with a single
    call.

    Removed unneeded dependency on Reagent.
    Updated dependencies to latest current
    versions.
