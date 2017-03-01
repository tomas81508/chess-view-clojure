(ns chess-view-clojure.core
  (:require [chess-view-clojure.state :as s]
            [test.core :refer [is= is is-not]]))

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

(defn
  ^{:test (fn []
            (is (-> (s/create-initial-state)
                    (s/get-piece 6 0)
                    (can-move?)))
            (is-not (-> (s/create-initial-state)
                        (s/get-piece 0 0)
                        (can-move?))))}
  can-move? [piece]
  (not (empty? (s/get-valid-moves piece))))


(defn
  ^{:test (fn []
            (is= (-> (s/create-initial-state)
                     (select-piece [6 0])
                     (s/get-selected-piece-coordinates))
                 [6 0]))}
  select-piece [state {row :row column :column}]

  )

(defn
  ^{:test (fn []
            (is= (-> (s/create-initial-state)
                     (select-piece {:row 6 :column 0})
                     (get-selected-piece))))}
  get-selected-piece [state]
  )

(defn
  ^{:test (fn []
            ;; When clicking on a movable piece, select it.
            (let [selected-piece (-> (s/create-initial-state)
                                     (handle-cell-click {:row 6 :column 0})
                                     (get-selected-piece))]
              (is= (:row selected-piece) 6)
              (is= (:column selected-piece) 0)))}
  handle-cell-click [state {row :row column :column}]

  )