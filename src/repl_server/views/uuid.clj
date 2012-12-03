(ns repl-server.views.uuid
  (:require [noir.core :refer [defpage]]            
            [noir.response :as resp])
  (:import [java.util.UUID]))

(defpage "/uuid.json" {:keys [jsonp]}
  (let [data {:uuid (str (java.util.UUID/randomUUID))}]    
    (if jsonp
      (resp/jsonp jsonp data)
      (resp/json data))))