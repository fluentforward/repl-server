(ns repl-server.views.clojure
  (:require [noir.core :refer [defpage]]
            [repl-server.clojure :refer [eval-request]]
            [noir.response :as resp]))

(defpage "/clojure.json" {:keys [expr jsonp sid]}
  (let [{:keys [expr result error message] :as res} (eval-request sid expr)
        data (if error
               res
               (let [[out res] result]
                 {:expr (pr-str expr)
                  :result (pr-str res)
                  :out (str out)}))]
    
    (if jsonp
      (resp/jsonp jsonp data)
      (resp/json data))))