(ns chess-view-clojure.state
  (:require [ysera.test #?(:clj :refer :cljs :refer-macros) [is= is is-not]]
            #?(:cljs [cljs.reader :refer [read-string]])))

(defn
  create-state
  ([] (create-state {}))
  ([{game-state :game-state
     outside-world :outside-world}]
   {:game-state game-state
    :view-state {:selected-piece-coordinates nil}
    :outside-world (or outside-world {:service nil})}))

(defn create-initial-state []
  (create-state {:view-state {:selected-piece-coordinates nil}
                 :game-state {:playerInTurn "large",
                              :board        [{:row    0,
                                              :column 0,
                                              :piece  {:type "rook", :owner "small", :valid-moves [], :id "1"}}
                                             {:row    0,
                                              :column 1,
                                              :piece  {:type "knight", :owner "small", :valid-moves [], :id "2"}}
                                             {:row    0,
                                              :column 2,
                                              :piece  {:type "bishop", :owner "small", :valid-moves [], :id "3"}}
                                             {:row    0,
                                              :column 3,
                                              :piece  {:type "queen", :owner "small", :valid-moves [], :id "4"}}
                                             {:row    0,
                                              :column 4,
                                              :piece  {:type "king", :owner "small", :valid-moves [], :id "5"}}
                                             {:row    0,
                                              :column 5,
                                              :piece  {:type "bishop", :owner "small", :valid-moves [], :id "6"}}
                                             {:row    0,
                                              :column 6,
                                              :piece  {:type "knight", :owner "small", :valid-moves [], :id "7"}}
                                             {:row    0,
                                              :column 7,
                                              :piece  {:type "rook", :owner "small", :valid-moves [], :id "8"}}
                                             {:row    1,
                                              :column 0,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "9"}}
                                             {:row    1,
                                              :column 1,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "10"}}
                                             {:row    1,
                                              :column 2,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "11"}}
                                             {:row    1,
                                              :column 3,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "12"}}
                                             {:row    1,
                                              :column 4,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "13"}}
                                             {:row    1,
                                              :column 5,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "14"}}
                                             {:row    1,
                                              :column 6,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "15"}}
                                             {:row    1,
                                              :column 7,
                                              :piece  {:type "pawn", :owner "small", :valid-moves [], :id "16"}}
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
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[5 0] [4 0]], :id "17"}}
                                             {:row    6,
                                              :column 1,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 1] [5 1]], :id "18"}}
                                             {:row    6,
                                              :column 2,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 2] [5 2]], :id "19"}}
                                             {:row    6,
                                              :column 3,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 3] [5 3]], :id "20"}}
                                             {:row    6,
                                              :column 4,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[5 4] [4 4]], :id "21"}}
                                             {:row    6,
                                              :column 5,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[5 5] [4 5]], :id "22"}}
                                             {:row    6,
                                              :column 6,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 6] [5 6]], :id "23"}}
                                             {:row    6,
                                              :column 7,
                                              :piece  {:type "pawn", :owner "large", :valid-moves [[4 7] [5 7]], :id "24"}}
                                             {:row    7,
                                              :column 0,
                                              :piece  {:type "rook", :owner "large", :valid-moves [], :id "25"}}
                                             {:row    7,
                                              :column 1,
                                              :piece  {:type "knight", :owner "large", :valid-moves [[5 2] [5 0]], :id "26"}}
                                             {:row    7,
                                              :column 2,
                                              :piece  {:type "bishop", :owner "large", :valid-moves [], :id "27"}}
                                             {:row    7,
                                              :column 3,
                                              :piece  {:type "queen", :owner "large", :valid-moves [], :id "28"}}
                                             {:row    7,
                                              :column 4,
                                              :piece  {:type "king", :owner "large", :valid-moves [], :id "29"}}
                                             {:row    7,
                                              :column 5,
                                              :piece  {:type "bishop", :owner "large", :valid-moves [], :id "30"}}
                                             {:row    7,
                                              :column 6,
                                              :piece  {:type "knight", :owner "large", :valid-moves [[5 7] [5 5]], :id "31"}}
                                             {:row    7,
                                              :column 7,
                                              :piece  {:type "rook", :owner "large", :valid-moves [], :id "32"}}],
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

(defn get-board [state]
  (get-in state [:game-state :board]))

(defn
  ^{:test (fn []
            (is= (take 2 (first (get-board-rows (create-initial-state))))
                 [{:row 0, :column 0, :piece {:type "rook", :owner "small", :valid-moves []}}
                  {:row 0, :column 1, :piece {:type "knight", :owner "small", :valid-moves []}}]))}
  get-board-rows [state]
  (->> (get-board state)
       (partition 8)))

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
                 #{})
            (is= (-> (create-initial-state)
                     (get-piece (create-coordinates 6 0))
                     (get-valid-moves))
                 #{[5 0] [4 0]}))}
  get-valid-moves [piece]
  (set (:valid-moves piece)))

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

(defn
  ^{:test (fn []
            ;; This is tested by get-selected-target-coordinates.
            )}
  set-selected-target-coordinates [state coordinates]
  (assoc-in state [:view-state :selected-target-coordinates] coordinates))

(defn
  ^{:test (fn []
            (is= (-> (create-initial-state)
                     (set-selected-target-coordinates :value)
                     (get-selected-target-coordinates))
                 :value))}
  get-selected-target-coordinates [state]
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
