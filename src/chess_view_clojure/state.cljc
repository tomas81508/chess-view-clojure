(ns chess-view-clojure.state
  (:require [ysera.test #?(:clj :refer :cljs :refer-macros) [is= is is-not]]
    #?(:cljs [cljs.reader :refer [read-string]])))

(defn
  create-state
  ([] (create-state {}))
  ([{game-state    :game-state
     outside-world :outside-world}]
   {:game-state    game-state
    :view-state    {:selected-piece-coordinates nil
                    :movable-pieces-hint-opacity 0.2}
    :outside-world (or outside-world {:service nil})}))

(defn create-initial-state []
  (create-state {:view-state {:selected-piece-coordinates nil
                              :movable-pieces-hint-opacity 0.2}
                 :game-state {:playerInTurn "large",
                              :board        [{:coordinates [0 7],
                                              :piece       {:type "rook", :owner "small", :valid-moves []}}
                                             {:coordinates [1 7],
                                              :piece       {:type "knight", :owner "small", :valid-moves []}}
                                             {:coordinates [2 7],
                                              :piece       {:type "bishop", :owner "small", :valid-moves []}}
                                             {:coordinates [3 7],
                                              :piece       {:type "queen", :owner "small", :valid-moves []}}
                                             {:coordinates [4 7],
                                              :piece       {:type "king", :owner "small", :valid-moves []}}
                                             {:coordinates [5 7],
                                              :piece       {:type "bishop", :owner "small", :valid-moves []}}
                                             {:coordinates [6 7],
                                              :piece       {:type "knight", :owner "small", :valid-moves []}}
                                             {:coordinates [7 7],
                                              :piece       {:type "rook", :owner "small", :valid-moves []}}
                                             {:coordinates [0 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [1 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [2 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [3 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [4 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [5 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [6 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [7 6],
                                              :piece       {:type "pawn", :owner "small", :valid-moves []}}
                                             {:coordinates [0 5], :piece nil}
                                             {:coordinates [1 5], :piece nil}
                                             {:coordinates [2 5], :piece nil}
                                             {:coordinates [3 5], :piece nil}
                                             {:coordinates [4 5], :piece nil}
                                             {:coordinates [5 5], :piece nil}
                                             {:coordinates [6 5], :piece nil}
                                             {:coordinates [7 5], :piece nil}
                                             {:coordinates [0 4], :piece nil}
                                             {:coordinates [1 4], :piece nil}
                                             {:coordinates [2 4], :piece nil}
                                             {:coordinates [3 4], :piece nil}
                                             {:coordinates [4 4], :piece nil}
                                             {:coordinates [5 4], :piece nil}
                                             {:coordinates [6 4], :piece nil}
                                             {:coordinates [7 4], :piece nil}
                                             {:coordinates [0 3], :piece nil}
                                             {:coordinates [1 3], :piece nil}
                                             {:coordinates [2 3], :piece nil}
                                             {:coordinates [3 3], :piece nil}
                                             {:coordinates [4 3], :piece nil}
                                             {:coordinates [5 3], :piece nil}
                                             {:coordinates [6 3], :piece nil}
                                             {:coordinates [7 3], :piece nil}
                                             {:coordinates [0 2], :piece nil}
                                             {:coordinates [1 2], :piece nil}
                                             {:coordinates [2 2], :piece nil}
                                             {:coordinates [3 2], :piece nil}
                                             {:coordinates [4 2], :piece nil}
                                             {:coordinates [5 2], :piece nil}
                                             {:coordinates [6 2], :piece nil}
                                             {:coordinates [7 2], :piece nil}
                                             {:coordinates [0 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[0 2] [0 3]]}}
                                             {:coordinates [1 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[1 3] [1 2]]}}
                                             {:coordinates [2 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[2 3] [2 2]]}}
                                             {:coordinates [3 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[3 3] [3 2]]}}
                                             {:coordinates [4 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[4 2] [4 3]]}}
                                             {:coordinates [5 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[5 2] [5 3]]}}
                                             {:coordinates [6 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[6 2] [6 3]]}}
                                             {:coordinates [7 1],
                                              :piece       {:type "pawn", :owner "large", :valid-moves [[7 2] [7 3]]}}
                                             {:coordinates [0 0],
                                              :piece       {:type "rook", :owner "large", :valid-moves []}}
                                             {:coordinates [1 0],
                                              :piece       {:type "knight", :owner "large", :valid-moves [[2 2] [0 2]]}}
                                             {:coordinates [2 0],
                                              :piece       {:type "bishop", :owner "large", :valid-moves []}}
                                             {:coordinates [3 0],
                                              :piece       {:type "queen", :owner "large", :valid-moves []}}
                                             {:coordinates [4 0],
                                              :piece       {:type "king", :owner "large", :valid-moves []}}
                                             {:coordinates [5 0],
                                              :piece       {:type "bishop", :owner "large", :valid-moves []}}
                                             {:coordinates [6 0],
                                              :piece       {:type "knight", :owner "large", :valid-moves [[7 2] [5 2]]}}
                                             {:coordinates [7 0],
                                              :piece       {:type "rook", :owner "large", :valid-moves []}}],
                              :players      [{:id "large"} {:id "small"}]}}))

(defn create-coordinates
  {:test (fn []
           (is= (create-coordinates "a8")
                [0 7])
           (is= (create-coordinates "c2")
                [2 1])
           (is= (create-coordinates "h1")
                [7 0]))}
  ([chess-coordinates]
   (let []
     [(- (int (first chess-coordinates)) 97)
      (- (read-string (str (second chess-coordinates))) 1)])))

(defn get-board [state]
  (get-in state [:game-state :board]))

(defn get-board-rows
  {:test (fn []
           (is= (take 2 (first (get-board-rows (create-initial-state))))
                [{:coordinates [0 7], :piece {:type "rook", :owner "small", :valid-moves []}}
                 {:coordinates [1 7], :piece {:type "knight", :owner "small", :valid-moves []}}]))}
  [state]
  (->> (get-board state)
       (partition 8)))

(defn get-cell
  {:test (fn []
           (is= (get-cell (create-initial-state) [0 7])
                {:coordinates [0 7], :piece {:type "rook", :owner "small", :valid-moves []}})
           (is= (get-cell (create-initial-state) [3 6])
                {:coordinates [3 6], :piece {:type "pawn", :owner "small", :valid-moves []}}))}
  [state coordinates]
  (->> (get-board state)
       (filter (fn [cell]
                 (= (:coordinates cell) coordinates)))
       (first)))

(defn get-piece
  {:test (fn []
           (is= (get-piece {:piece {:type "rook", :owner "small"}})
                {:type "rook", :owner "small"})
           (is= (get-piece {:piece nil})
                nil)
           (is= (get-piece (create-initial-state) [0 7])
                {:type "rook", :owner "small" :valid-moves []}))}
  ([state coordinates]
   {:pre [(not (nil? coordinates))]}
   (get-piece (get-cell state coordinates)))
  ([cell]
   (:piece cell)))

(defn get-player-in-turn
  {:test (fn []
           (is= (get-player-in-turn (create-initial-state))
                "large"))}
  [state]
  (get-in state [:game-state :playerInTurn]))

(defn get-players
  {:test (fn []
           (is= (get-players (create-initial-state))
                [{:id "large"} {:id "small"}]))}
  [state]
  (get-in state [:game-state :players]))

(defn get-valid-moves
  {:test (fn []
           (is= (-> (create-initial-state)
                    (get-piece [0 7])
                    (get-valid-moves))
                #{})
           (is= (-> (create-initial-state)
                    (get-piece [0 1])
                    (get-valid-moves))
                #{[0 2] [0 3]}))}
  [piece]
  (set (:valid-moves piece)))

(defn set-selected-piece-coordinates
  {:test (fn []
           ;; This is tested by get-selected-piece-coordinates.
           )}
  [state coordinates]
  (assoc-in state [:view-state :selected-piece-coordinates] coordinates))

(defn get-selected-piece-coordinates
  {:test (fn []
           (is= (-> (create-initial-state)
                    (set-selected-piece-coordinates :value)
                    (get-selected-piece-coordinates))
                :value))}
  [state]
  (get-in state [:view-state :selected-piece-coordinates]))

(defn set-selected-target-coordinates
  {:test (fn []
           ;; This is tested by get-selected-target-coordinates.
           )}
  [state coordinates]
  (assoc-in state [:view-state :selected-target-coordinates] coordinates))

(defn get-selected-target-coordinates
  {:test (fn []
           (is= (-> (create-initial-state)
                    (set-selected-target-coordinates :value)
                    (get-selected-target-coordinates))
                :value))}
  [state]
  (get-in state [:view-state :selected-target-coordinates]))


(defn waiting-for-service?
  {:test (fn []
           (is (waiting-for-service? (create-state {:outside-world {:service {:loading true}}})))
           (is-not (waiting-for-service? (create-state {:outside-world {:service {:loading false}}})))
           (is-not (waiting-for-service? (create-state {:outside-world {:service nil}}))))}
  [state]
  (get-in state [:outside-world :service :loading]))

(defn set-waiting-for-service
  {:test (fn []
           (is (waiting-for-service? (set-waiting-for-service (create-state) true))))}
  [state value]
  (assoc-in state [:outside-world :service :loading] value))

(defn set-game-state
  ;; Tested by get-game-state function.
  [state game-state]
  (assoc state :game-state game-state))

(defn get-game-state
  {:test (fn []
           (is= (get-game-state (set-game-state (create-state) "game-state"))
                "game-state"))}
  [state]
  (:game-state state))

(defn get-previous-moves
  {:test (fn []
           (is= (-> (create-state {:game-state {:previous-moves []}})
                    (get-previous-moves))
                []))}
  [state]
  (:previous-moves (get-game-state state)))

(defn get-previous-move
  {:test (fn []
           (is= (-> (create-state {:game-state {:previous-moves [:a :b :c]}})
                    (get-previous-move))
                :c)
           (is= (-> (create-state {:game-state {:previous-moves []}})
                    (get-previous-move))
                nil))}
  [state]
  (last (get-previous-moves state)))

(defn get-movable-pieces-hint-opacity
  [state]
  (get-in state [:view-state :movable-pieces-hint-opacity]))

(defn set-movable-pieces-hint-opacity
  [state value]
  (assoc-in state [:view-state :movable-pieces-hint-opacity] value))