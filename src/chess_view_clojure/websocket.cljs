(ns chess-view-clojure.websocket
  (:require [cognitect.transit :as transit]))

(defonce websocket-channel-atom (atom nil))

(def json-reader (transit/reader :json))
(def json-writer (transit/writer :json))

(defn receive-transit-msg!
  [update-fn]
  (fn [msg]
    (update-fn
      (->> msg .-data (transit/read json-reader)))))

(defn send-data!
  [msg]
  (println "msg" msg)
  (println "transit/write" (transit/write json-writer msg))
  (if @websocket-channel-atom
    (.send @websocket-channel-atom (transit/write json-writer msg))
    (throw (js/Error. "Websocket is not available!"))))

(defn connect!
  [url receive-handler on-connected]
  (if-let [ws (js/WebSocket. url)]
    (do
      (set! (.-onmessage ws) (receive-transit-msg! receive-handler))
      (set! (.-onopen ws)
            (fn [e] (on-connected)))
      (reset! websocket-channel-atom ws))
    (throw (js/Error. "Websocket connection failed!"))))

(defn connected?
  []
  (not (nil? @websocket-channel-atom)))