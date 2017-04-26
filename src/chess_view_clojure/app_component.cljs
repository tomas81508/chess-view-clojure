(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]))

(defn piece-component [{state       :state
                        coordinates :coordinates
                        piece       :piece}]
      (let [factor 125
            x (* factor (:column coordinates))
            y (* factor (:row coordinates))]
           [:img {:style {:pointerEvents "none"
                          :top           "1.25%"
                          :left          "1.25%"
                          :width         "10%"
                          :position      "absolute"
                          :transition    "all 1500ms ease"
                          :transform     (str "translate(" x "%, " y "%)")}
                  :src   (core/get-piece-image-url state piece)}]))

(defn cell-style [{coordinates :coordinates}]
      (merge {:background (if (odd? (+ (:row coordinates) (:column coordinates)))
                            "#ddd" "white")
              :position   "relative"
              :width      "12.5%"}))

(defn cell-component [{state :state coordinates :coordinates trigger-event :trigger-event}]
      (let [piece (s/get-piece state coordinates)
            selected (core/selected? state coordinates)]
           [:div {:style   (merge (cell-style {:coordinates coordinates})
                                  (when (core/can-move? piece)
                                        {:cursor "pointer"})
                                  (if selected
                                    {:padding-bottom "calc(12.5% - 8px)"
                                     :boxSizing      "border-box"
                                     :border         "4px solid rebeccapurple"}
                                    {:padding-bottom "12.5%"}))
                  :onClick (fn [] (trigger-event {:event :cell-click
                                                  :data  coordinates}))}]))


(defn app-component [{app-state-atom :app-state-atom
                      trigger-event  :trigger-event}]
      (let [state @app-state-atom]
           [:div {:style {:padding    "2%"
                          :background "#bbb"
                          :userSelect "none"}}
            [:div {:style {:position "relative"}}
             (map-indexed (fn [row-index row]
                              [:div {:style {:display "flex"}
                                     :key   row-index}
                               (map-indexed (fn [column-index cell]
                                                (let [piece (s/get-piece cell)
                                                      coordinates {:row    (:row cell)
                                                                   :column (:column cell)}]
                                                     [cell-component {:state         state
                                                                      :key           column-index
                                                                      :coordinates   coordinates
                                                                      :trigger-event trigger-event}]))
                                            row)
                               ])
                          (s/get-board-rows state))
             (map (fn [cell]
                      (let [piece (:piece cell)]
                           [piece-component {:state       state
                                             :coordinates cell
                                             :piece       piece
                                             :key         (str "piece-" (:id piece))}]))
                  (core/get-cells-with-pieces state))]]))