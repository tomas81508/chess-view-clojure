(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]))


(defn piece-style [x y]
  {:left          "1.2%"
   :top           "1.2%"
   :height        "10%"
   :width         "10%"
   :zIndex        1
   :position      "absolute"
   :transition    "all 400ms"
   :pointerEvents "none"
   :transform     (str "translate(" x "%," y "%)")})

(defn piece-component [{key   :key
                        cell  :cell
                        state :state}]
  (let [piece (s/get-piece cell)
        amplitude 125]
    [:img {:key   key
           :style (piece-style (* amplitude (:column cell)) (* amplitude (:row cell)))
           :src   (core/get-piece-image-url state (:piece cell))}]))


(defn cell-style [coordinate]
  {:width      "12.5%"
   :display    "inline-block"
   :background (if (even? (+ (:row coordinate) (:column coordinate)))
                 "white"
                 "#ddd")})

(def cell-selected-style {:border        "4px solid rebeccapurple"
                          :box-sizing    "border-box"
                          :paddingBottom "calc(12.5% - 8px)"})

(defn cell-component [{cell          :cell
                       state         :state
                       trigger-event :trigger-event}]
  (let [piece (s/get-piece cell)
        selected-piece-coordinates (s/get-selected-piece-coordinates state)
        coordinates {:row    (:row cell)
                     :column (:column cell)}]
    [:div {:style   (merge (cell-style (s/get-coordinates cell))
                           (when (core/can-move? piece)
                             {:cursor "pointer"})
                           (if (= selected-piece-coordinates (s/get-coordinates cell))
                             cell-selected-style
                             {:paddingBottom "12.5%"}))
           :onClick (fn []
                      (trigger-event {:event :cell-click
                                      :data  coordinates}))}]))


(def row-style {:zIndex  0
                :display "flex"})

(defn row-component [{row           :row
                      key           :key
                      state         :state
                      trigger-event :trigger-event}]
  [:div {:style row-style
         :key   key}
   (map (fn [cell]
          [cell-component {:cell          cell
                           :key           (:column cell)
                           :state         state
                           :trigger-event trigger-event}])
        row)])

(def board-frame-style {:padding    "2%"
                        :background "#bbb"})

(def board-style {:position "relative"})

(defn board-component [{state         :state
                        trigger-event :trigger-event}]
  [:div {:style board-frame-style}
   [:div {:style board-style}
    (map-indexed (fn [index cell]
                   [piece-component {:key   index
                                     :cell  cell
                                     :state state}])
                 (s/get-cells-with-pieces state))
    (map-indexed (fn [index row]
                   [row-component {:row           row
                                   :key           index
                                   :state         state
                                   :trigger-event trigger-event}])
                 (s/get-board-rows state))]])


(defn app-component [{app-state-atom :app-state-atom
                      trigger-event  :trigger-event}]
  [board-component {:state         @app-state-atom
                    :trigger-event trigger-event}])