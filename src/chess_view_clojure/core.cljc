(ns chess-view-clojure.core
  (:require [chess-view-clojure.state :as s]
            [test.core :refer [is=]]))

(defn
  ^{:test (fn []
            (is= (let [state (s/create-initial-state)]
                   (starting-player? state (s/get-player-in-turn state)))
                 true))}
  starting-player?
  [state player-id]
  (= (:id (first (s/get-players state))) player-id))

(defn
  ^{:test (fn []
            (is= (let [state (s/create-initial-state)]
                   (player-id->color state (s/get-player-in-turn state)))
                 "white")
            (is= (let [state (s/create-initial-state)]
                   (player-id->color state (:owner (s/get-piece state 0 0))))
                 "black"))}
  player-id->color [state player-id]
  (if (starting-player? state player-id)
    "white"
    "black"))

(defn
  ^{:test (fn []
            (is= (let [state (s/create-initial-state)]
                   (get-piece-image-url state (s/get-piece state 0 0)))
                 "asset/piece/rook-black.svg")
            (is= (let [state (s/create-initial-state)]
                   (get-piece-image-url state (s/get-piece state 6 3)))
                 "asset/piece/pawn-white.svg"))}
  get-piece-image-url [state piece]
  (str "asset/piece/"
       (:type piece)
       "-"
       (player-id->color state (:owner piece))
       ".svg"))