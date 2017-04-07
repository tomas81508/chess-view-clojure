(ns chess-view-clojure.state
  (:require [test.core :refer [is=]]))

(defn
  create-state
  ([] (create-state {}))
  ([{game-state :game-state}]
   {:game-state game-state
    :view-state {:selected-piece-coordinates nil}}))

(defn create-initial-state []
  (create-state {:view-state {:selected-piece-coordinates nil}
                 :game-state {:playerInTurn "large",
                              :board        [{:row    0,
                                              :column 0,
                                              :piece  {:type "rook", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 1,
                                              :piece  {:type "knight", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 2,
                                              :piece  {:type "bishop", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 3,
                                              :piece  {:type "queen", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 4,
                                              :piece  {:type "king", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 5,
                                              :piece  {:type "bishop", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 6,
                                              :piece  {:type "knight", :owner "small", :valid-moves []}}
                                             {:row    0,
                                              :column 7,
                                              :piece  {:type "rook", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 0,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 1,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 2,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 3,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 4,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 5,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 6,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row    1,
                                              :column 7,
                                              :piece  {:type "pawn", :owner "small", :valid-moves []}}
                                             {:row 2, :column 0, :piece nil}
                                             {:row 2, :column 1, :piece nil}
                                             {:row 2, :column 2, :piece nil}
                                             {:row 2, :column 3, :piece nil}
                                             {:row 2, :column 4, :piece nil}
                                             {:row 2, :column 5, :piece nil}
                                             {:row 2, :column 6, :piece nil}
                                             {:row 2, :column 7, :piece nil}
                                             {:row 3, :column 0, :piece nil}
                                             {:row 3, :column 1, :piece nil}
                                             {:row 3, :column 2, :piece nil}
                                             {:row 3, :column 3, :piece nil}
                                             {:row 3, :column 4, :piece nil}
                                             {:row 3, :column 5, :piece nil}
                                             {:row 3, :column 6, :piece nil}
                                             {:row 3, :column 7, :piece nil}
                                             {:row 4, :column 0, :piece nil}
                                             {:row 4, :column 1, :piece nil}
                                             {:row 4, :column 2, :piece nil}
                                             {:row 4, :column 3, :piece nil}
                                             {:row 4, :column 4, :piece nil}
                                             {:row 4, :column 5, :piece nil}
                                             {:row 4, :column 6, :piece nil}
                                             {:row 4, :column 7, :piece nil}
                                             {:row 5, :column 0, :piece nil}
                                             {:row 5, :column 1, :piece nil}
                                             {:row 5, :column 2, :piece nil}
                                             {:row 5, :column 3, :piece nil}
                                             {:row 5, :column 4, :piece nil}
                                             {:row 5, :column 5, :piece nil}
                                             {:row 5, :column 6, :piece nil}
                                             {:row 5, :column 7, :piece nil}
                                             {:row    6,
                                              :column 0,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[5 0] [4 0]]}}
                                             {:row    6,
                                              :column 1,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 1] [5 1]]}}
                                             {:row    6,
                                              :column 2,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 2] [5 2]]}}
                                             {:row    6,
                                              :column 3,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 3] [5 3]]}}
                                             {:row    6,
                                              :column 4,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[5 4] [4 4]]}}
                                             {:row    6,
                                              :column 5,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[5 5] [4 5]]}}
                                             {:row    6,
                                              :column 6,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 6] [5 6]]}}
                                             {:row    6,
                                              :column 7,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 7] [5 7]]}}
                                             {:row    7,
                                              :column 0,
                                              :piece  {:type "rook", :owner "large", :valid-moves []}}
                                             {:row    7,
                                              :column 1,
                                              :piece  {:type "knight", :owner "large", :valid-moves [[5 2] [5 0]]}}
                                             {:row    7,
                                              :column 2,
                                              :piece  {:type "bishop", :owner "large", :valid-moves []}}
                                             {:row    7,
                                              :column 3,
                                              :piece  {:type "queen", :owner "large", :valid-moves []}}
                                             {:row    7,
                                              :column 4,
                                              :piece  {:type "king", :owner "large", :valid-moves []}}
                                             {:row    7,
                                              :column 5,
                                              :piece  {:type "bishop", :owner "large", :valid-moves []}}
                                             {:row    7,
                                              :column 6,
                                              :piece  {:type "knight", :owner "large", :valid-moves [[5 7] [5 5]]}}
                                             {:row    7,
                                              :column 7,
                                              :piece  {:type "rook", :owner "large", :valid-moves []}}],
                              :players      [{:id "large"} {:id "small"}]}}))

(defn create-coordinates
  {:test (fn []
           (is= (create-coordinates 3 5)
                {:row 3 :column 5})
           (is= (create-coordinates "a8")
                {:row 0 :column 0})
           (is= (create-coordinates "c2")
                {:row 6 :column 2})
           (is= (create-coordinates "h1")
                {:row 7 :column 7}))}
  ([chess-coordinates]
   (let []
     (create-coordinates (- 8 (read-string (str (second chess-coordinates))))
                         (- (int (first chess-coordinates)) 97))))
  ([row column]
   {:row row :column column}))

(defn
  ^{:test (fn []
            (is= (take 2 (first (get-board-rows (create-initial-state))))
                 [{:row 0, :column 0, :piece {:type "rook", :owner "small", :valid-moves []}}
                  {:row 0, :column 1, :piece {:type "knight", :owner "small", :valid-moves []}}]))}
  get-board-rows [state]
  (->> (:board (:game-state state))
       (partition 8)))

(defn
  ^{:test (fn []
            (is= (->> (get-cells-with-pieces (create-initial-state))
                      (take 1))
                 [{:row 0
                   :column 0
                   :piece {:type "rook"
                           :owner "small"
                           :valid-moves []}}]))}
  get-cells-with-pieces [state]
  (->> (get-in state [:game-state :board])
       (filter :piece)))

(defn
  ^{:test (fn []
            (is= (get-cell (create-initial-state) (create-coordinates 0 0))
                 {:row 0, :column 0, :piece {:type "rook", :owner "small", :valid-moves []}})
            (is= (get-cell (create-initial-state) (create-coordinates 1 3))
                 {:row 1, :column 3, :piece {:type "pawn", :owner "small", :valid-moves []}}))}
  get-cell [state {row :row column :column}]
  (get-in state [:game-state :board (+ (* row 8) column)]))

(defn
  ^{:test (fn []
            (is= (get-piece {:piece {:type "rook", :owner "small"}})
                 {:type "rook", :owner "small"})
            (is= (get-piece {:piece nil})
                 nil)
            (is= (get-piece (create-initial-state) (create-coordinates 0 0))
                 {:type "rook", :owner "small" :valid-moves []}))}
  get-piece
  ([state coordinates]
   (get-piece (get-cell state coordinates)))
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

(defn
  ^{:test (fn []
            (is= (-> (create-initial-state)
                     (get-piece (create-coordinates 0 0))
                     (get-valid-moves))
                 []))}
  get-valid-moves [piece]
  (:valid-moves piece))

(defn
  ^{:test (fn []
            ;; This is tested by get-selected-piece-coordinates.
            )}
  set-selected-piece-coordinates [state coordinates]
  (assoc-in state [:view-state :selected-piece-coordinates] coordinates))

(defn
  ^{:test (fn []
            (is= (-> (create-initial-state)
                     (set-selected-piece-coordinates :value)
                     (get-selected-piece-coordinates))
                 :value))}
  get-selected-piece-coordinates [state]
  (get-in state [:view-state :selected-piece-coordinates]))

(defn get-coordinates
  {}
  [cell]
  (create-coordinates (:row cell) (:column cell)))