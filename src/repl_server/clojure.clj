(ns repl-server.clojure
  (:require [clojail.testers :refer [secure-tester-without-def blanket]]
            [clojail.core :refer [sandbox]]
            [clojure.stacktrace :refer [root-cause]]
            [clojure.core.cache :as cache])
  (:import java.io.StringWriter
     java.util.concurrent.TimeoutException))

(def sandbox-cache (atom (cache/ttl-cache-factory {} :ttl (* 2 60 60 1000)))) ; 2 hour cache ttl

(defn eval-form [form sbox]
  (with-open [out (StringWriter.)]
    (let [result (sbox form {#'*out* out})]
      {:expr form
       :result [out result]})))

(defn eval-string [expr sbox]
  (let [form (binding [*read-eval* false] (read-string expr))]
    (eval-form form sbox)))

(def try-clojure-tester
  (conj secure-tester-without-def (blanket "tryclojure" "noir")))

(defn make-sandbox []
  (sandbox try-clojure-tester
           :timeout 2000
           :init '(do (require '[clojure.repl :refer [doc source]])
                      (future (Thread/sleep 600000)
                              (-> *ns* .getName remove-ns)))))

;Note we aren't using the guidelines for cache usage for 2 reasons
; 1. We want cache item ttl to be updated on each usage
; 2. The ttl cache has a race whereby cache/has? returns true, but the lookup can fail due to timing issues

(defn cache-with-sandbox [scache sid]
  (let [sb (cache/lookup scache sid)]
    (if sb 
      (cache/miss scache sid sb) 
      (cache/miss scache sid (make-sandbox)))))

(defn get-sandbox [sid]
  (cache/lookup (swap! sandbox-cache cache-with-sandbox sid) sid))

(defn eval-request [sid expr]
  (try
    (eval-string expr (get-sandbox sid))
    (catch TimeoutException _
      {:error true :message "Execution Timed Out!"})
    (catch Exception e
      {:error true :message (str (root-cause e))})))