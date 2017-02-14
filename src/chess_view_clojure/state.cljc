(ns chess-view-clojure.state
  (:require [test.core :refer [is=]]))

(defn
  create-state
  ([] (create-state {}))
  ([{game-state :game-state}]
   {:game-state game-state
    :view-state {}}))

(defn create-initial-state []
  (create-state {:game-state {:playerInTurn "large",
                              :board [{:row 0, :column 0, :piece {:type "rook", :owner "small"}} {:row 0, :column 1, :piece {:type "knight", :owner "small"}} {:row 0, :column 2, :piece {:type "bishop", :owner "small"}} {:row 0, :column 3, :piece {:type "queen", :owner "small"}} {:row 0, :column 4, :piece {:type "king", :owner "small"}} {:row 0, :column 5, :piece {:type "bishop", :owner "small"}} {:row 0, :column 6, :piece {:type "knight", :owner "small"}} {:row 0, :column 7, :piece {:type "rook", :owner "small"}} {:row 1, :column 0, :piece {:type "pawn", :owner "small"}} {:row 1, :column 1, :piece {:type "pawn", :owner "small"}} {:row 1, :column 2, :piece {:type "pawn", :owner "small"}} {:row 1, :column 3, :piece {:type "pawn", :owner "small"}} {:row 1, :column 4, :piece {:type "pawn", :owner "small"}} {:row 1, :column 5, :piece {:type "pawn", :owner "small"}} {:row 1, :column 6, :piece {:type "pawn", :owner "small"}} {:row 1, :column 7, :piece {:type "pawn", :owner "small"}} {:row 2, :column 0, :piece nil} {:row 2, :column 1, :piece nil} {:row 2, :column 2, :piece nil} {:row 2, :column 3, :piece nil} {:row 2, :column 4, :piece nil} {:row 2, :column 5, :piece nil} {:row 2, :column 6, :piece nil} {:row 2, :column 7, :piece nil} {:row 3, :column 0, :piece nil} {:row 3, :column 1, :piece nil} {:row 3, :column 2, :piece nil} {:row 3, :column 3, :piece nil} {:row 3, :column 4, :piece nil} {:row 3, :column 5, :piece nil} {:row 3, :column 6, :piece nil} {:row 3, :column 7, :piece nil} {:row 4, :column 0, :piece nil} {:row 4, :column 1, :piece nil} {:row 4, :column 2, :piece nil} {:row 4, :column 3, :piece nil} {:row 4, :column 4, :piece nil} {:row 4, :column 5, :piece nil} {:row 4, :column 6, :piece nil} {:row 4, :column 7, :piece nil} {:row 5, :column 0, :piece nil} {:row 5, :column 1, :piece nil} {:row 5, :column 2, :piece nil} {:row 5, :column 3, :piece nil} {:row 5, :column 4, :piece nil} {:row 5, :column 5, :piece nil} {:row 5, :column 6, :piece nil} {:row 5, :column 7, :piece nil} {:row 6, :column 0, :piece {:type "pawn", :owner "large"}} {:row 6, :column 1, :piece {:type "pawn", :owner "large"}} {:row 6, :column 2, :piece {:type "pawn", :owner "large"}} {:row 6, :column 3, :piece {:type "pawn", :owner "large"}} {:row 6, :column 4, :piece {:type "pawn", :owner "large"}} {:row 6, :column 5, :piece {:type "pawn", :owner "large"}} {:row 6, :column 6, :piece {:type "pawn", :owner "large"}} {:row 6, :column 7, :piece {:type "pawn", :owner "large"}} {:row 7, :column 0, :piece {:type "rook", :owner "large"}} {:row 7, :column 1, :piece {:type "knight", :owner "large"}} {:row 7, :column 2, :piece {:type "bishop", :owner "large"}} {:row 7, :column 3, :piece {:type "queen", :owner "large"}} {:row 7, :column 4, :piece {:type "king", :owner "large"}} {:row 7, :column 5, :piece {:type "bishop", :owner "large"}} {:row 7, :column 6, :piece {:type "knight", :owner "large"}} {:row 7, :column 7, :piece {:type "rook", :owner "large"}}],
                              :players [{:id "large"} {:id "small"}]}}))

(defn
  ^{:test (fn []
            (is= (take 2 (first (get-board-rows (create-initial-state))))
                 '({:row 0, :column 0, :piece {:type "rook", :owner "small"}} {:row 0, :column 1, :piece {:type "knight", :owner "small"}})))}
  get-board-rows [state]
  (->> (:board (:game-state state))
       (partition 8)))

(defn
  ^{:test (fn []
            (is= (get-cell (create-initial-state) 0 0)
                 {:row 0, :column 0, :piece {:type "rook", :owner "small"}})
            (is= (get-cell (create-initial-state) 1 3)
                 {:row 1, :column 3, :piece {:type "pawn", :owner "small"}}))}
  get-cell [state row column]
  (get-in state [:game-state :board (+ (* row 8) column)]))

(defn
  ^{:test (fn []
            (is= (get-piece {:piece {:type "rook", :owner "small"}})
                 {:type "rook", :owner "small"})
            (is= (get-piece {:piece nil})
                 nil)
            (is= (get-piece (create-initial-state) 0 0)
                 {:type "rook", :owner "small"}))}
  get-piece
  ([state row column]
   (get-piece (get-cell state row column)))
  ([cell]
   (:piece cell)))

(defn
  ^{:test (fn []
            (is= (get-player-in-turn (create-initial-state))
                 "large"))}
  get-player-in-turn [state]
  (get-in state [:game-state :playerInTurn]))

(defn
  ^{:test (fn []
            (is= (get-players (create-initial-state))
                 [{:id "large"} {:id "small"}]))}
  get-players [state]
  (get-in state [:game-state :players]))