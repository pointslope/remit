(ns pointslope.remit.middleware
  "This namespace contains common and reusable middleware
used in event processing.")

;;; - handler design -
;;; Remit event handlers are expected to be functions
;;; that take a single event map parameter and pull the
;;; data they need from its keys.
;;; Event maps are guaranteed to have to two members/keys:
;;; :event containing the name of the event that was emitted
;;; :data containing any additional data (or nil) emitted with the event

;; - predefined middleware -

(defn event-data-middleware
  "A middleware function to assoc a given 
  key/value pair onto every event prior
  to delegating the call to the supplied
  event-handler function."
  [f key val]
  (fn [data]
    (f (assoc data key val))))
