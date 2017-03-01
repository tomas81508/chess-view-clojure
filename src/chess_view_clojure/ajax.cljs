(ns chess-view-clojure.ajax)

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