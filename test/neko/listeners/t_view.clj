(ns neko.listeners.t-view
  (:require [clojure.test :refer :all]
            [neko.listeners.view :as l])
  (:import [android.view KeyEvent MotionEvent]
           org.robolectric.RuntimeEnvironment
           neko.App))

(defmacro test-listener [& body]
  `(let [~'pr (promise)
         ~'v (android.view.View. RuntimeEnvironment/application)
         ~'called (fn [] (deliver ~'pr :success))]
     ~@body
     (is (= :success (deref ~'pr 1000 :fail)))))

(deftest on-click
  (test-listener
   (.setOnClickListener v (l/on-click (called)))
   (.performClick v))

  (test-listener
   (.setOnClickListener v (l/on-click-call (fn [_] (called))))
   (.performClick v)))

(deftest on-long-click
  (test-listener
   (.setOnLongClickListener v (l/on-long-click (called)))
   (.performLongClick v))

  (test-listener
   (.setOnLongClickListener v (l/on-long-click-call (fn [_] (called))))
   (.performLongClick v)))

(deftest on-touch
  (let [touch-event (MotionEvent/obtain 50 300 MotionEvent/ACTION_UP 0 0 0)]
    (test-listener (.onTouch (l/on-touch (called)) v touch-event))
    (test-listener (.onTouch (l/on-touch-call (fn [_ __] (called))) v touch-event))))

(deftest on-key
  (let [test-code  KeyEvent/KEYCODE_EQUALS
        test-event (KeyEvent. KeyEvent/ACTION_DOWN test-code)]
    (test-listener
     (.onKey (l/on-key (called)
                       (is (= key-code test-code)))
             v test-code test-event))

    (test-listener
     (.onKey (l/on-key-call (fn [_ code __] (called)
                              (is (= code test-code))))
             v test-code test-event))))
