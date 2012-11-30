(ns repl-server.views.home
  (:require [noir.core :refer [defpage]]
            [repl-server.clojure :as rsc])
  (:use [hiccup.page :only [include-css include-js html5]]))

(defn status-row [k] [:p k])

(defpage "/" [] 
  (html5 
    [:body
      [:h3 "Status"]
      (map status-row (rsc/sessions))
    ])
  )