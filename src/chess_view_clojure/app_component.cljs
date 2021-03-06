(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]))

(defn piece-component [{state :state
                        [x y] :coordinates
                        piece :piece}]
  (let [factor 12.5
        offset 1.25]
    [:img {:style {:pointerEvents "none"
                   :top           (str (+ (* factor (- 7 y)) offset) "%")
                   :left          (str (+ (* factor x) offset) "%")
                   :width         "10%"
                   :position      "absolute"
                   :transition    "all 200ms ease"}
           :src   (core/get-piece-image-url state piece)}]))

(defn cell-style [{[x y] :coordinates}]
  (merge {:background (if (even? (+ y x))
                        "#ddd" "white")
          :position   "relative"
          :width      "12.5%"}))

(defn cell-component [{state :state coordinates :coordinates trigger-event :trigger-event}]
  (let [piece (s/get-piece state coordinates)
        selected (core/selected? state coordinates)
        selected-piece (core/get-selected-piece state)
        previous-move (s/get-previous-move state)]
    [:div {:style   (merge (cell-style {:coordinates coordinates})
                           (when (core/can-move? piece)
                             {:cursor "pointer"})
                           (cond selected
                                 {:padding-bottom "calc(12.5% - 8px)"
                                  :boxSizing      "border-box"
                                  :border         "4px solid rebeccapurple"}

                                 (and selected-piece
                                      (contains? (set (:valid-moves selected-piece))
                                                 coordinates))
                                 {:padding-bottom "calc(12.5% - 8px)"
                                  :boxSizing      "border-box"
                                  :border         "4px solid green"}

                                 :else
                                 {:padding-bottom "12.5%"}))
           :onClick (fn [] (trigger-event {:event :cell-click
                                           :data  coordinates}))}
     (when (core/can-move? piece)
       [:div {:style {:position   "absolute"
                      :height     "100%"
                      :width      "100%"
                      :background (str "rgba(40, 180, 0, "
                                       (s/get-movable-pieces-hint-opacity state)
                                       ")")}}])
     (when (and previous-move (= coordinates (:from-coordinates previous-move)))
       [:div {:style {:position      "absolute"
                      :height        "40%"
                      :width         "40%"
                      :left          "30%"
                      :top           "30%"
                      :border-radius "50%"
                      :background    "rgba(255, 152, 0, 0.8)"}}])
     (when (and previous-move (= coordinates (:to-coordinates previous-move)))
       [:div {:style {:position   "absolute"
                      :height     "100%"
                      :width      "100%"
                      :background "rgba(255, 152, 0, 0.5)"}}])]))


(defn app-component [{app-state-atom :app-state-atom
                      trigger-event  :trigger-event}]
  (let [state @app-state-atom]
    [:div
     [:div {:style {:padding    "2%"
                    :background "#bbb"
                    :userSelect "none"}}
      [:div {:style {:position "relative"}}
       (map-indexed (fn [row-index row]
                      [:div {:style {:display "flex"}
                             :key   row-index}
                       (map-indexed (fn [column-index cell]
                                      (let [piece (s/get-piece cell)
                                            coordinates (:coordinates cell)]
                                        [cell-component {:state         state
                                                         :key           column-index
                                                         :coordinates   coordinates
                                                         :trigger-event trigger-event}]))
                                    row)])
                    (s/get-board-rows state))
       (map (fn [cell]
              [piece-component {:state       state
                                :key         (str "p-" (get-in cell [:piece :id]))
                                :coordinates (:coordinates cell)
                                :piece       (:piece cell)}])
            (sort-by (comp :id :piece) (core/get-cells-with-pieces state)))]]
     [:button {:style    {:width      "200px"
                          :height     "80px"
                          :margin-top "10px"}
               :on-click (fn [] (trigger-event {:event :undo}))}
      "Undo"]
     [:button {:style    {:width      "200px"
                          :height     "80px"
                          :margin-top "10px"}
               :on-click (fn [] (trigger-event {:event :redo}))}
      "Redo"]
     [:input {:type     "range"
              :min      "0"
              :max      "100"
              :value    (* 100 (s/get-movable-pieces-hint-opacity state))
              :onChange (fn [e]
                          (trigger-event {:event :movable-pieces-hint-opacity-change
                                          :data  {:value (/ (aget e "target" "value") 100)}}))}]
     [:h2 (s/get-player-in-turn state)]
     [:ol
      (->> (s/get-previous-moves state)
           (map-indexed (fn [index previous-move]
                          [:li {:key index}
                           [:div
                            [:img {:style {:position      "relative"
                                           :top           "2px"
                                           :pointerEvents "none"
                                           :width         "25px"}
                                   :src   (core/get-piece-image-url state (core/previous-move->piece previous-move))}]
                            [:span {:style {:color "orange"}} " | "]
                            [:span (str (:from-coordinates previous-move) " - " (:to-coordinates previous-move))]]])))]]))















