(ns repl-server.server
  (:require [noir.server :as server]))

(server/load-views-ns 'repl-server.views)

(defn to-port [s]
  (when-let [port s] (Long. port)))

(defn enable-cors [handler]
  (fn [request]
    (let [resp (handler request)]
      (assoc-in (assoc-in resp [:headers "Access-Control-Allow-Origin"] "*")
        [:headers "Access-Control-Allow-Headers"] "X-Requested-With")
      )))

(defn -main [& m]
  (let [mode (keyword (or (first m ) :dev))
        port (or (to-port (System/getenv "PORT")) 8080)
       ]
    (server/wrap-route :resources enable-cors)       
    (server/start port {:mode mode
                        :ns 'repl-server
                        :jetty-options {
                          :ssl? true
                          :ssl-port 443
                          :keystore (System/getenv "KEYSTORE")
                          :key-password (System/getenv "KEYPASS")
                        }
                        })))