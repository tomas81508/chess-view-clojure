(ns chess-view-clojure.main
  (:require [reagent.core :as reagent :refer [atom]]
            [chess-view-clojure.state :refer [create-state
                                              get-board-rows]]))

(enable-console-print!)

(println "korv")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state-atom (atom (create-state)))

(defn hello-world []
  (let [state @app-state-atom]
    [:div {:className "board"}
     (map (fn [row]
            [:div {:className "row"}
             (map (fn [cell]
                    [:div {:className "cell"}
                     [:div {:className "cell__content"}
                      (str (:row cell) (:column cell))]])
                  row)])
          (get-board-rows state))]))

(defn ajax [{route      :route
             data       :data
             on-success :on-success
             on-error   :on-error}]
  (let [domain (or (.-domain js/document) "localhost")
        url (str "http://" domain ":8001" route)
        xml-http-request (js/XMLHttpRequest.)
        js-data (clj->js data)]
    (set! (.-onreadystatechange xml-http-request)
          (fn []
            (when (= (.-readyState xml-http-request) 4)     ; 4 = The fetch operation is complete.
              (let [status (.-status xml-http-request)]
                (if (= status 200)
                  (on-success {:data (-> (.parse js/JSON (.-responseText xml-http-request))
                                         (js->clj :keywordize-keys true))})
                  (on-error {:status status :data (.-status xml-http-request)}))))))
    (.open xml-http-request "POST" url)
    (.send xml-http-request (.stringify js/JSON js-data))))

(ajax {:route      "/createGame"
       :data       nil
       :on-success (fn [{data :data}]
                     (swap! app-state-atom (fn [state]
                                             (assoc state :game-state data)))
                     (println data))
       :on-error   (fn [_]
                     (println "error"))})

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state-atom to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
