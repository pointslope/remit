(ns pointslope.remit.events
  "This namespace contains functions dealing with
  the publishing and subscription of events. 

  Events are maps containing an :event key which
  is used as a core.async pub/sub topic.

  Any data emitted with the event is stored under
  the :data key of the same event map."
  (:require [cljs.core.async :as async
             :refer [<! >! chan close! put! pub sub unsub unsub-all mult tap untap]])
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]]))

;; --- core.async pub/sub channels for emitting events ---

(def ^:private event-channel
  "This channel is used by the emit function
  to publish event notifications."
  (chan))

(def ^:private event-multiplexer
  "This multiplexer sits in between the
  event channel and the pub/sub mechanism to 
  allow access to the raw stream of events"
  (mult event-channel))

(def ^:private publication-sink
  "This channel will tap into the event
  multiplexer and feed messages to the 
  publication."
  (chan))

(def ^:private event-publication
  "This publication of the event-channel
  enforces a convention for the event 
  emitter where all event notifications must
  take the form of a map with an :event key
  that designates the publication topic"
  (pub publication-sink :event))

;; --- Public API --- 

(defn pubsub
  "Initializes the pub/sub mechanism by
  tapping into the event multiplexer and
  funnelling the messages to the publication
  sink."
  []
  (tap event-multiplexer publication-sink))

(defn un-pubsub
  "Tears down the tap setup by pubsub"
  []
  (untap event-multiplexer publication-sink))

(defn wiretap
  "Creates a new channel that taps into the 
  event multiplexer. Any message received
  on this tap will be passed to the supplied
  function, f, which should accept this single
  argument. Returns the wiretap channel, which
  can be passed to un-wiretap."
  [f]
  (let [wtap (chan)]
    (tap event-multiplexer wtap)
    (go-loop []
      (when-let [event (<! wtap)]
        (f event)
        (recur)))
    wtap))

(defn un-wiretap
  "Stops the funneling of messages from
  the event multiplexer to the supplied
  wiretap channel"
  [wtap]
  (untap event-multiplexer wtap))

;; --- Initialize the pub/sub framework ---

(pubsub)

;; --- event emitting (publication) ---

(defn emit
  "This function emits events by publishing
  them via core.async's pub/sub mechanism.

  Expects an event-key and (optionally)
  event data.  

  Returns the message data structure
  that was emitted."
  ([event-key]
   (emit event-key nil))
  
  ([event-key event-data]
   (emit event-channel event-key event-data))

  ([event-chan event-key event-data]
   (let [message {:event event-key :data event-data}]
     (put! event-chan message)
     message)))

;; --- event subscription ---

(defn subscribe
  "This function registers a handler function for
  a given event (topic). It does this by establishing
  a core.async subscription on a new channel.

  Expects an event key and handler function.
  Optionally, a pre-existing subscription
  channel can be passed in. This makes it 
  easy to re-connect a previous listener 
  dynamically.

  If no subscription channel is supplied, a
  new one will be created.

  Returns the subscription channel (which)
  can later be used to unsubscribe.

  Received events will be delegated to the 
  supplied function in a go block. The go
  block will exit if the channel is closed.

  In the event of an exception, subscribe 
  will emit a new event under the event key 
  :pointslope.remit.events/exception. 
  
  The :data for this event is a map containing
  three keys: 

  :exception  (the exception that was thrown)
  :event-key  (the original event that led to the error)
  :handler-fn (the function that caused the exception)

  This allows/forces the subscribing application
  to establish the exception handling policy.
  
  usage:

  (subscribe :click (fn [data] ...))
  
  (let [my-chan (chan)]
    ... more work here ...
    (subscribe my-chan :click (fn [data] ...)))
  "
  ([event-key f]
   (subscribe event-publication (chan) event-key f))
  
  ([sub-chan event-key f]
   (subscribe event-publication sub-chan event-key f))

  ([pub-chan sub-chan event-key f]
   (let [_ (sub pub-chan event-key sub-chan)]
     (go-loop []
       (when-let [event (<! sub-chan)]
         (try
           (f event)
           (catch :default e
             (emit ::exception
                   {:exception e
                    :event-key event-key
                    :handler-fn f})))
         (recur)))
     sub-chan)))

(defn unsubscribe
  "Unsubscribes a channel from an event publication"
  ([sub-chan topic]
   (unsubscribe event-publication sub-chan topic))

  ([pub-chan sub-chan topic]
   (unsub pub-chan topic sub-chan)))

(defn unsubscribe-all
  "Unsubscribes all listeners from an event topic"
  ([topic]
   (unsubscribe-all event-publication topic))
  
  ([pub-chan topic]
   (unsub-all pub-chan topic)))
