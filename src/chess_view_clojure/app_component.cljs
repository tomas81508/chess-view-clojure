(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]
            [chess-view-clojure.styles :as styles]))

(defn cell-component [state {coordinates :coordinates trigger-event :trigger-event}]
  (let [piece (s/get-piece state coordinates)
        selected (core/selected? state coordinates)]
    [:div {
           :style   (styles/get-cell-style {:selectable  (core/can-move? piece)
                                            :coordinates coordinates})
           :key     (:column coordinates)
           :onClick (fn [] (trigger-event {:event :cell-click
                                           :data  coordinates}))}
     [:div {:style {
                    :display        "flex"
                    :justifyContent "center"
                    :alignItems     "center"
                    :position       "absolute"
                    :width          "100%"
                    :height         "100%"
                    :top            0
                    :left           0
                    }}
      (when piece
        [:img {:style (styles/get-piece-style) :src (core/get-piece-image-url state piece)}])]
     (when selected [:div {:style {
                                   :position  "absolute"
                                   :width     "100%"
                                   :height    "100%"
                                   :boxSizing "border-box"
                                   :border    "4px solid rebeccapurple"
                                   }}])]))


(defn app-component [{app-state-atom :app-state-atom
                      trigger-event  :trigger-event}]
  (let [state @app-state-atom]
    [:div {:style (styles/get-board-style)}
     (map-indexed (fn [index row]
                    [:div {:style (styles/get-row-style) :key index}
                     (map (fn [cell]
                            (let [piece (s/get-piece cell)
                                  coordinates {:row    (:row cell)
                                               :column (:column cell)}]
                              (cell-component state {:coordinates coordinates :trigger-event trigger-event})))
                          row)])
                  (s/get-board-rows state))]))