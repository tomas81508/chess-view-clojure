(ns chess-view-clojure.styles
  (:require [ysera.test #?(:clj :refer :cljs :refer-macros) [is= is is-not]]))

;/* some style */
;

;/* rebeccapurple */
;
;
;.cell--selected::after {
;                        content: "";
;                        position: absolute;
;                        width: 100%;
;                        height: 100%;
;                        box-sizing: border-box;
;                        border: 4px solid rebeccapurple;
;                        }
;
;
;                                           .cell__content {
;                                                           display: flex;
;                                                           justify-content: center;
;                                                           align-items: center;
;                                                           position: absolute;
;                                                           width: 100%;
;                                                           height: 100%;
;                                                           top: 0;
;                                                           left: 0;
;                                                           }
;

(defn get-board-style
  {:test (fn []
           (is= (get-board-style) {
                                   :padding    "1rem"
                                   :background "#bbb"
                                   :userSelect "none"
                                   }))}
  []
  {
   :padding    "1rem"
   :background "#bbb"
   :userSelect "none"
   })

(defn get-row-style []
  {
   :display "flex"
   }
  )

(defn get-cell-style
  {:test (fn []
           (is= (get-cell-style) {
                                  :background     "white"
                                  :position       "relative"
                                  :width          "12.5%"
                                  :padding-bottom "12.5%"
                                  })
           (is= (get-cell-style {}) (get-cell-style))
           (is= (get-cell-style {:selectable false}) (get-cell-style))
           (is= (-> (get-cell-style {:selectable true})
                    (:cursor))
                "pointer")
           (is= (-> (get-cell-style {:coordinates {:row 0 :column 1}})
                    (:background))
                "#ddd"))}
  ([{selectable  :selectable
     coordinates :coordinates}]
   (merge {
           :background     "white"
           :position       "relative"
           :width          "12.5%"
           :padding-bottom "12.5%"
           }
          (when selectable {:cursor "pointer"})
          (when (and coordinates (odd? (+ (:row coordinates) (:column coordinates))))
            {:background "#ddd"})
          ))
  ([]
   (get-cell-style {})))

(defn get-piece-style []
  {
   :width "70%"
   })