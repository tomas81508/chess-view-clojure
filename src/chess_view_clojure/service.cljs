(ns chess-view-clojure.service
  (:require [chess-view-clojure.core :as core]
            [chess-view-clojure.state :as s]
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

(defn move-piece!
      [state-atom]
      (swap! state-atom core/set-waiting-for-service)
      (let [state (deref state-atom)
            player-id (s/get-player-in-turn state)
            from-coordinates (s/get-selected-piece-coordinates state)
            to-coordinates (s/get-selected-target-coordinates state)]
           (ajax {:route      "/move"
                  :data       {:from-position [(:row from-coordinates) (:column from-coordinates)]
                               :to-position   [(:row to-coordinates) (:column to-coordinates)]
                               :player-id     player-id}
                  :on-success (fn [response]
                                  (swap! state-atom core/receive-move-piece-service-response response))
                  :on-error   (fn [_]
                                  (println "error"))})))

(defn redo!
  [state-atom]
  (swap! state-atom core/set-waiting-for-service)
  (let [state (deref state-atom)
        player-id (s/get-player-in-turn state)]
    (ajax {:route      "/redo"
           :data       {:player-id player-id}
           :on-success (fn [response]
                         (swap! state-atom core/receive-move-piece-service-response response))
           :on-error   (fn [_]
                         (println "error"))})))

(defn undo!
      [state-atom]
      (swap! state-atom core/set-waiting-for-service)
      (let [state (deref state-atom)
            player-id (s/get-player-in-turn state)]
           (ajax {:route      "/undo"
                  :data       {:player-id player-id}
                  :on-success (fn [response]
                                  (swap! state-atom core/receive-move-piece-service-response response))
                  :on-error   (fn [_]
                                  (println "error"))})))
