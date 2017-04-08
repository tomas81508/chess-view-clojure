(ns chess-view-clojure.service
  (:require [chess-view-clojure.core :as core]
            [chess-view-clojure.ajax :refer [ajax]]))

(defn create-game!
  [state-atom]
  (swap! state-atom core/set-waiting-for-create-game-service)
  (ajax {:route      "/createGame"
         :data       nil
         :on-success (fn [response]
                       (swap! state-atom core/receive-create-game-response response))
         :on-error   (fn [_]
                       (println "error"))}))

(defn move-piece! [state-atom])