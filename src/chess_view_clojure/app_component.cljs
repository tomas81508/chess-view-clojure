(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]))

(defn app-component [{app-state-atom :app-state-atom
                      trigger-event  :trigger-event}]
  (let [state @app-state-atom]
    [:div {:className "board"}
     (map-indexed (fn [index row]
                    [:div {:className "row" :key index}
                     (map (fn [cell]
                            (let [piece (s/get-piece cell)
                                  coordinates {:row    (:row cell)
                                               :column (:column cell)}]
                              [:div {:className (str "cell"
                                                     (when (core/can-move? piece)
                                                       " cell--selectable")
                                                     (when (core/selected? state coordinates)
                                                       " cell--selected"))
                                     :key       (:column cell)
                                     :onClick   (fn [] (trigger-event {:event :cell-click
                                                                       :data  coordinates}))}
                               [:div {:className "cell__content"}
                                (when piece
                                  [:img {:className "piece" :src (core/get-piece-image-url state piece)}])]]))
                          row)])
                  (s/get-board-rows state))]))