(ns repl-server.server
  (:require [noir.server :as server]))

(server/load-views-ns 'repl-server.views)

(defn to-port [s]
  (when-let [port s] (Long. port)))

(defn -main [& m]
  (let [mode (keyword (or (first m ) :dev))
        port (or (to-port (System/getenv "PORT")) 8080)
       ]
    (server/start port {:mode mode
                        :ns 'repl-server})))