(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]))


(defn piece-component [{key   :key
                        cell  :cell
                        state :state}]
  (let [piece (s/get-piece cell)
        amplitude 120]
    [:img {:key   key
           :style {:left          "2.9%"
                   :top           "2.9%"
                   :height        "10%"
                   :width         "10%"
                   :zIndex        1
                   :position      "absolute"
                   :transition    "all 400ms"
                   :pointerEvents "none"
                   :transform     (str "translate("
                                       (* amplitude (:column cell))
                                       "%,"
                                       (* amplitude (:row cell))
                                       "%)")}
           :src   (core/get-piece-image-url state (:piece cell))}]))



(defn app-component [{app-state-atom :app-state-atom
                      trigger-event  :trigger-event}]
  (let [state @app-state-atom
        selected-piece-coordinates (s/get-selected-piece-coordinates state)]
    [:div {:style {:padding    "1rem"
                   :position "relative"
                   :background "#bbb"}}
     (map-indexed (fn [index cell]
                    [piece-component {:key   index
                                      :cell  cell
                                      :state state}])
                  (s/get-cells-with-pieces state))
     (map-indexed (fn [index row]
                    [:div {:style     {:zIndex 0
                                       :display "flex"}
                           :key       index}
                     (map (fn [cell]
                            (let [piece (s/get-piece cell)
                                  coordinates {:row    (:row cell)
                                               :column (:column cell)}]
                              [:div {:style   (merge {:width         "12.5%"
                                                      :display       "inline-block"
                                                      :background    (if (even? (+ (:row cell) (:column cell)))
                                                                       "white"
                                                                       "#ddd")}
                                                     (when (core/can-move? piece)
                                                       {:cursor "pointer"})
                                                     (if (= selected-piece-coordinates (s/get-coordinates cell))
                                                       {:border "4px solid rebeccapurple"
                                                        :box-sizing "border-box"
                                                        :paddingBottom "calc(12.5% - 8px)"}
                                                       {:paddingBottom "12.5%"}))
                                     :key     (:column cell)
                                     :onClick (fn []
                                                (trigger-event {:event :cell-click
                                                                :data  coordinates}))}]))
                          row)])
                  (s/get-board-rows state))]))