(ns chess-view-clojure.main
  (:require [reagent.core :as reagent :refer [atom]]
            [chess-view-clojure.state :refer [create-state]]
            [chess-view-clojure.app-component :as app-component]
            [chess-view-clojure.core :as core]
            [chess-view-clojure.service :as service]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state-atom (atom nil))

(add-watch app-state-atom :game-engine
           (fn [_ _ state _]
             (cond
               (core/should-call-create-game? state)
               (service/create-game! app-state-atom)

              (core/should-call-move-piece? state)
              (service/move-piece! app-state-atom)

               )))

(swap! app-state-atom
       (fn [state]
         (or state (create-state))))

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
