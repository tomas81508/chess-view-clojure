(ns chess-view-clojure.core
  (:require [chess-view-clojure.state :as s]
            [ysera.test #?(:clj :refer :cljs :refer-macros) [is= is is-not]]))

(defn starting-player?
  {:test (fn []
           (is= (let [state (s/create-initial-state)]
                  (starting-player? state (s/get-player-in-turn state)))
                true))}
  [state player-id]
  (= (:id (first (s/get-players state))) player-id))

(defn player-id->color
  {:test (fn []
           (is= (let [state (s/create-initial-state)]
                  (player-id->color state (s/get-player-in-turn state)))
                "white")
           (is= (let [state (s/create-initial-state)]
                  (player-id->color state (:owner (s/get-piece state [0 7]))))
                "black"))}
  [state player-id]
  (if (starting-player? state player-id)
    "white"
    "black"))

(defn get-piece-image-url
  {:test (fn []
           (is= (let [state (s/create-initial-state)]
                  (get-piece-image-url state (s/get-piece state [0 7])))
                "asset/piece/rook-black.svg")
           (is= (let [state (s/create-initial-state)]
                  (get-piece-image-url state (s/get-piece state [3 1])))
                "asset/piece/pawn-white.svg"))}
  [state piece]
  (str "asset/piece/"
       (:type piece)
       "-"
       (player-id->color state (:owner piece))
       ".svg"))

(defn can-move?
  {:test (fn []
           (is (-> (s/create-initial-state)
                   (s/get-piece [0 1])
                   (can-move?)))
           (is-not (-> (s/create-initial-state)
                       (s/get-piece [0 7])
                       (can-move?))))}
  [piece]
  (not (empty? (s/get-valid-moves piece))))

(defn selected?
  {:test (fn []
           (is= (-> (s/create-initial-state)
                    (s/set-selected-piece-coordinates :value)
                    (selected? :value))
                true))}
  [state coordinates]
  (= (s/get-selected-piece-coordinates state)
     coordinates))

(defn get-selected-piece
  {:test (fn []
           (is= (-> (s/create-initial-state)
                    (s/set-selected-piece-coordinates [0 7])
                    (get-selected-piece))
                {:type        "rook"
                 :owner       "small"
                 :valid-moves []})
           (is= (-> (s/create-initial-state)
                    (get-selected-piece))
                nil))}
  [state]
  (when-let [selected-piece-coordinates (s/get-selected-piece-coordinates state)]
    (s/get-piece state selected-piece-coordinates)))

(defn valid-piece-move?
  {:test (fn []
           (is (-> (s/create-initial-state)
                   (s/get-piece [0 1])
                   (valid-piece-move? [0 2])))
           (is-not (-> (s/create-initial-state)
                       (s/get-piece [0 1])
                       (valid-piece-move? [1 2]))))}
  [piece target-coordinates]
  (contains? (s/get-valid-moves piece) target-coordinates))

(defn handle-cell-click
  {:test (fn []
           ;; When clicking on a movable piece, select it.
           (let [selected-piece-coordinates (-> (s/create-initial-state)
                                                (handle-cell-click [0 1])
                                                (s/get-selected-piece-coordinates))]
             (is= selected-piece-coordinates [0 1]))

           ;; When clicking on a cell that has no piece, nothing should happen.
           (let [state (s/create-initial-state)]
             (is= (-> state
                      (handle-cell-click [0 4]))
                  state))

           ;; When a piece is selected, and clicking on a non-target empty cell, unselect the piece.
           (is= (-> (s/create-initial-state)
                    (s/set-selected-piece-coordinates [0 1])
                    (handle-cell-click [0 4]))
                (s/create-initial-state))

           ;; When clicking an already selected piece, unselect it.
           (is= (-> (s/create-initial-state)
                    (s/set-selected-piece-coordinates [0 1])
                    (handle-cell-click [0 1]))
                (s/create-initial-state))

           ;; When having a piece selected, and clicking on a valid target cell.
           (let [state (-> (s/create-initial-state)
                           (s/set-selected-piece-coordinates [0 1])
                           (handle-cell-click [0 2]))]
             (is= (s/get-selected-target-coordinates state)
                  [0 2])))}
  [state coordinates]
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

(defn handle-undo-click [state]
  (assoc-in state [:view-state :undo-selected] true))


(defn handle-redo-click [state]
  (assoc-in state [:view-state :redo-selected] true))

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

(defn set-waiting-for-service
  ;; This one is tested in the waiting-for-move-piece-service? function tests.
  [state]
  (s/set-waiting-for-service state true))

(defn waiting-for-service?
  {:test (fn []
           (is (-> (s/create-state)
                   (set-waiting-for-service)
                   (waiting-for-service?)))
           (is-not (-> (s/create-state)
                       (waiting-for-service?))))}
  [state]
  (s/waiting-for-service? state))

(defn should-call-move-piece?
  {:test (fn []
           (is (should-call-move-piece? (-> (s/create-initial-state)
                                            (s/set-selected-piece-coordinates [0 1])
                                            (s/set-selected-target-coordinates [0 2]))))
           (is-not (should-call-move-piece? (-> (s/create-initial-state)
                                                (s/set-selected-piece-coordinates [0 1])
                                                (s/set-selected-target-coordinates [0 2])
                                                (set-waiting-for-service))))
           (is-not (should-call-move-piece? (-> (s/create-initial-state)
                                                (s/set-selected-piece-coordinates [0 1]))))
           (is-not (should-call-move-piece? (s/create-initial-state))))}
  [state]
  (and (s/get-selected-piece-coordinates state)
       (s/get-selected-target-coordinates state)
       (not (waiting-for-service? state))))

(defn should-call-undo?
  [state]
  (and (get-in state [:view-state :undo-selected])
       (not (waiting-for-service? state))))

(defn should-call-redo?
  [state]
  (and (get-in state [:view-state :redo-selected])
       (not (waiting-for-service? state))))


(defn receive-undo-service-response
  [state response]
  (-> state
      (s/set-waiting-for-service false)
      (s/set-game-state (:data response))
      (s/set-selected-piece-coordinates nil)
      (s/set-selected-target-coordinates nil)
      (assoc-in [:view-state :undo-selected] false)))

(defn receive-redo-service-response
  [state response]
  (-> state
      (s/set-waiting-for-service false)
      (s/set-game-state (:data response))
      (s/set-selected-piece-coordinates nil)
      (s/set-selected-target-coordinates nil)
      (assoc-in [:view-state :redo-selected] false)))


(defn receive-move-piece-service-response
  {:test (fn []
           (let [state (receive-move-piece-service-response (-> (s/create-state)
                                                                (s/set-selected-piece-coordinates [0 1])
                                                                (s/set-selected-target-coordinates [0 2]))
                                                            {:status 200 :data "game-state"})]
             (is= (s/get-game-state state) "game-state")
             (is= (s/get-selected-piece-coordinates state) nil)
             (is= (s/get-selected-target-coordinates state) nil)
             (is-not (waiting-for-service? state))))}
  [state response]
  (-> state
      (s/set-waiting-for-service false)
      (s/set-game-state (:data response))
      (s/set-selected-piece-coordinates nil)
      (s/set-selected-target-coordinates nil)
      (assoc-in [:view-state :undo-selected] false)
      (assoc-in [:view-state :redo-selected] false)))

(defn get-cells-with-pieces
  {:test (fn []
           (is= (->> (get-cells-with-pieces (s/create-initial-state))
                     (first))
                {:coordinates [0 7]
                 :piece       {:type        "rook"
                               :owner       "small"
                               :valid-moves []}}))}
  [state]
  (->> (s/get-board state)
       (filter :piece)))

(defn previous-move->piece
  {:test (fn []
           (is= (previous-move->piece {:piece-type       "rook"
                                       :owner            "large"
                                       :from-coordinates [0 1]
                                       :to-coordinates   [3 1]})
                {:type  "rook"
                 :owner "large"}))}
  [previous-move]
  {:type  (:piece-type previous-move)
   :owner (:owner previous-move)})