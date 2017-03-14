(ns chess-view-clojure.main
  (:require [reagent.core :as reagent :refer [atom]]
            [chess-view-clojure.state :refer [create-state]]
            [chess-view-clojure.app-component :as app-component]
            [chess-view-clojure.ajax :refer [ajax]]
            [chess-view-clojure.core :as core]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state-atom (atom (create-state)))

(ajax {:route      "/createGame"
       :data       nil
       :on-success (fn [{data :data}]
                     (swap! app-state-atom (fn [state]
                                             (assoc state :game-state data))))
       :on-error   (fn [_]
                     (println "error"))})

(defn handle-event!
  [{event :event data :data}]
  (condp = event
    :cell-click
    (swap! app-state-atom core/handle-cell-click data)))

(reagent/render-component [app-component/app-component {:app-state-atom app-state-atom
                                                        :trigger-event  (fn [{event :event data :data :as params}]
                                                                          (println event data)
                                                                          (handle-event! params))}]
                          (. js/document (getElementById "app")))

