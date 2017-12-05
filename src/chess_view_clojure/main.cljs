(ns chess-view-clojure.main
  (:require [reagent.core :as reagent :refer [atom]]
            [chess-view-clojure.state :refer [create-state] :as state]
            [chess-view-clojure.app-component :as app-component]
            [chess-view-clojure.core :as core]
            [chess-view-clojure.service-websocket :as service-websocket]
            [chess-view-clojure.websocket :as websocket]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state-atom (atom nil))

(defn handle-receive-data! [data]
  (println data)
  (swap! app-state-atom core/receive-move-piece-service-response data))

(when-not (websocket/connected?)
  (websocket/connect! (str "ws://" (aget js/window "location" "hostname") ":9876/")
                      handle-receive-data!
                      (fn handle-connected []
                        (swap! app-state-atom assoc :connected true))))

(add-watch app-state-atom :game-engine
           (fn [_ _ _ state]
             (cond
               (core/should-create-game? state)
               (service-websocket/create-game! app-state-atom)

               (core/should-move-piece? state)
               (service-websocket/move-piece! app-state-atom)

               (core/should-undo? state)
               (service-websocket/undo! app-state-atom)

               (core/should-redo? state)
               (service-websocket/redo! app-state-atom))))

(swap! app-state-atom
       (fn [state]
         (or state (create-state))))

(defn handle-event!
  [{event :event data :data}]
  (condp = event
    :cell-click
    (swap! app-state-atom core/handle-cell-click data)

    :movable-pieces-hint-opacity-change
    (swap! app-state-atom state/set-movable-pieces-hint-opacity (:value data))

    :redo
    (swap! app-state-atom core/handle-redo-click)

    :undo
    (swap! app-state-atom core/handle-undo-click)))

(reagent/render-component [app-component/app-component {:app-state-atom app-state-atom
                                                        :trigger-event  (fn [{event :event data :data :as params}]
                                                                          (handle-event! params))}]
                          (. js/document (getElementById "app")))
