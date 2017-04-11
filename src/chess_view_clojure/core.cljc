(ns chess-view-clojure.core
  (:require [chess-view-clojure.state :as s]
            [ysera.test #?(:clj :refer :cljs :refer-macros) [is= is is-not]]))

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
                   (player-id->color state (:owner (s/get-piece state (s/create-coordinates 0 0)))))
                 "black"))}
  player-id->color [state player-id]
  (if (starting-player? state player-id)
    "white"
    "black"))

(defn
  ^{:test (fn []
            (is= (let [state (s/create-initial-state)]
                   (get-piece-image-url state (s/get-piece state (s/create-coordinates 0 0))))
                 "asset/piece/rook-black.svg")
            (is= (let [state (s/create-initial-state)]
                   (get-piece-image-url state (s/get-piece state (s/create-coordinates 6 3))))
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
                    (s/get-piece (s/create-coordinates 6 0))
                    (can-move?)))
            (is-not (-> (s/create-initial-state)
                        (s/get-piece (s/create-coordinates 0 0))
                        (can-move?))))}
  can-move? [piece]
  (not (empty? (s/get-valid-moves piece))))


;(defn
;  ^{:test (fn []
;            (is= (-> (s/create-initial-state)
;                     (select-piece [6 0])
;                     (s/get-selected-piece-coordinates))
;                 [6 0]))}
;  select-piece [state {row :row column :column}]
;
;  )

;(defn
;  ^{:test (fn []
;            (is= (-> (s/create-initial-state)
;                     (select-piece {:row 6 :column 0})
;                     (get-selected-piece))
;                 ))}
;  get-selected-piece [state]
;  )

(defn selected?
  {:test (fn []
           (is= (-> (s/create-initial-state)
                    (s/set-selected-piece-coordinates :value)
                    (selected? :value))
                true))}
  [state coordinates]
  (= (s/get-selected-piece-coordinates state)
     coordinates))

(defn valid-piece-move?
  {:test (fn []
           (is (-> (s/create-initial-state)
                   (s/get-piece (s/create-coordinates 6 0))
                   (valid-piece-move? (s/create-coordinates 5 0))))
           (is-not (-> (s/create-initial-state)
                       (s/get-piece (s/create-coordinates 6 0))
                       (valid-piece-move? (s/create-coordinates 5 1)))))}
  [piece target-coordinates]
  (contains? (s/get-valid-moves piece) [(:row target-coordinates) (:column target-coordinates)]))

(defn
  ^{:test (fn []
            ;; When clicking on a movable piece, select it.
            (let [selected-piece-coordinates (-> (s/create-initial-state)
                                                 (handle-cell-click (s/create-coordinates 6 0))
                                                 (s/get-selected-piece-coordinates))]
              (is= selected-piece-coordinates (s/create-coordinates 6 0)))

            ;; When clicking on a cell that has no piece, nothing should happen.
            (is= (-> (s/create-initial-state)
                     (handle-cell-click (s/create-coordinates 3 0)))
                 (s/create-initial-state))

            ;; When a piece is selected, and clicking on a non-target empty cell, unselect the piece.
            (is= (-> (s/create-initial-state)
                     (s/set-selected-piece-coordinates (s/create-coordinates 6 0))
                     (handle-cell-click (s/create-coordinates 3 0)))
                 (s/create-initial-state))

            ;; When clicking an already selected piece, unselect it.
            (is= (-> (s/create-initial-state)
                     (s/set-selected-piece-coordinates (s/create-coordinates 6 0))
                     (handle-cell-click (s/create-coordinates 6 0)))
                 (s/create-initial-state))

            ;; When having a piece selected, and clicking on a valid target cell.
            (let [state (-> (s/create-initial-state)
                            (s/set-selected-piece-coordinates (s/create-coordinates 6 0))
                            (handle-cell-click (s/create-coordinates 5 0)))]
              (is= (s/get-selected-target-coordinates state)
                   (s/create-coordinates 5 0)))
            )}
  handle-cell-click [state coordinates]
  (let [piece (s/get-piece state coordinates)
        coordinates-selected (s/get-selected-piece-coordinates state)]
    (cond
      (and coordinates-selected
           (valid-piece-move? (s/get-piece state coordinates-selected) coordinates))
      (s/set-selected-target-coordinates state coordinates)


      (and coordinates-selected (or (not piece)
                                    (= piece (s/get-piece state coordinates-selected))))
      (s/set-selected-piece-coordinates state nil)

      (and piece (can-move? piece))
      (s/set-selected-piece-coordinates state coordinates)

      :else
      state)))

(defn set-waiting-for-create-game-service
  ;; This one is tested in the waiting-for-create-game-service? function tests.
  [state]
  (s/set-waiting-for-service state true))

(defn waiting-for-create-game-service?
  {:test (fn []
           (is (-> (s/create-state)
                   (set-waiting-for-create-game-service)
                   (waiting-for-create-game-service?)))
           (is-not (-> (s/create-state)
                       (waiting-for-create-game-service?))))}
  [state]
  (s/waiting-for-service? state))

(defn receive-create-game-response
  {:test (fn []
           (let [state (receive-create-game-response (s/create-state) {:status 200 :data "game-state"})]
             (is= (s/get-game-state state) "game-state")
             (is-not (waiting-for-create-game-service? state))))}
  [state response]
  (-> state
      (s/set-waiting-for-service false)
      (s/set-game-state (:data response))))

(defn should-call-create-game?
  {:test (fn []
           (is (should-call-create-game? (s/create-state)))
           (is-not (should-call-create-game? (set-waiting-for-create-game-service (s/create-state))))
           (is-not (should-call-create-game? (s/create-initial-state))))}
  [state]
  (and (not (s/get-game-state state))
       (not (waiting-for-create-game-service? state))))

(defn set-waiting-for-move-piece-service
  ;; This one is tested in the waiting-for-move-piece-service? function tests.
  [state]
  (s/set-waiting-for-service state true))

(defn waiting-for-move-piece-service?
  {:test (fn []
           (is (-> (s/create-state)
                   (set-waiting-for-move-piece-service)
                   (waiting-for-move-piece-service?)))
           (is-not (-> (s/create-state)
                       (waiting-for-move-piece-service?))))}
  [state]
  (s/waiting-for-service? state))

(defn should-call-move-piece?
  {:test (fn []
           (is (should-call-move-piece? (-> (s/create-initial-state)
                                            (s/set-selected-piece-coordinates {:row 6 :column 0})
                                            (s/set-selected-target-coordinates {:row 5 :column 0}))))
           (is-not (should-call-move-piece? (-> (s/create-initial-state)
                                                (s/set-selected-piece-coordinates {:row 6 :column 0})
                                                (s/set-selected-target-coordinates {:row 5 :column 0})
                                                (set-waiting-for-move-piece-service))))
           (is-not (should-call-move-piece? (-> (s/create-initial-state)
                                                (s/set-selected-piece-coordinates {:row 6 :column 0}))))
           (is-not (should-call-move-piece? (s/create-initial-state))))}
  [state]
  (and (s/get-selected-piece-coordinates state)
       (s/get-selected-target-coordinates state)
       (not (waiting-for-move-piece-service? state))))

(defn receive-move-piece-response
  {:test (fn []
           (let [state (receive-move-piece-response (-> (s/create-state)
                                                        (s/set-selected-piece-coordinates {:row 6 :column 0})
                                                        (s/set-selected-target-coordinates {:row 5 :column 0}))
                                                    {:status 200 :data "game-state"})]
             (is= (s/get-game-state state) "game-state")
             (is= (s/get-selected-piece-coordinates state) nil)
             (is= (s/get-selected-target-coordinates state) nil)
             (is-not (waiting-for-move-piece-service? state))))}
  [state response]
  (-> state
      (s/set-waiting-for-service false)
      (s/set-game-state (:data response))
      (s/set-selected-piece-coordinates nil)
      (s/set-selected-target-coordinates nil)))
