(ns chess-view-clojure.app-component
  (:require [chess-view-clojure.state :as s]
            [chess-view-clojure.core :as core]))

(defn app-component [{app-state-atom :app-state-atom}]
  (let [state @app-state-atom]
    [:div {:className "board"}
     (map-indexed (fn [index row]
                    [:div {:className "row" :key index}
                     (map (fn [cell]
                            [:div {:className "cell" :key (:column cell)}
                             [:div {:className "cell__content"}
                              (when-let [piece (s/get-piece cell)]
                                [:img {:className "piece" :src (core/get-piece-image-url state piece)}])]])
                          row)])
                  (s/get-board-rows state))]))