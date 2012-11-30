(defproject repl-server "0.1.0"
  :description "repl server"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir "1.3.0-beta10"]
                 [clojail "1.0.1"]
                 [org.clojure/core.cache "0.6.2"]]
  :jvm-opts ["-Djava.security.policy=example.policy"]
  :main repl-server.server)
