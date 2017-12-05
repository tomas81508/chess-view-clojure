(ns chess-view-clojure.service-websocket
  (:require [chess-view-clojure.websocket :refer [send-data!]]
            [chess-view-clojure.state :as s]))

(defn create-game!
  [state-atom]
  (send-data! {:action "create-game"
               :data   nil}))

(defn move-piece!
  [state-atom]
  (let [state (deref state-atom)
        player-id (s/get-player-in-turn state)
        from-coordinates (s/get-selected-piece-coordinates state)
        to-coordinates (s/get-selected-target-coordinates state)]
    (send-data! {:action "move-piece"
                 :data   {:from-position from-coordinates
                          :to-position   to-coordinates
                          :player-id     player-id}})))

(defn redo!
  [state-atom]
  (let [state (deref state-atom)
        player-id (s/get-player-in-turn state)]
    (send-data! {:action "redo"
                 :data   {:player-id player-id}})))

(defn undo!
  [state-atom]
  (let [state (deref state-atom)
        player-id (s/get-player-in-turn state)]
    (send-data! {:action "undo"
                 :data   {:player-id player-id}})))


