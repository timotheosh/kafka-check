(ns kafka-check.core
  (:require [clojure.data.json :refer [write-str]]
            [franzy.admin.topics :as t]
            [kafka-check.connection-data :as connection-data]
            [kafka-check.kafka-connect :refer [kafka-connect]]
            [kafka-check.topics :as topics]
            [mount.core :refer [start stop]])
  (:gen-class
   :name kafka_check.core
   :methods [[GET [String] java.util.HashMap]]))

(defn- get-params
  "Takes a request object and extracts the parameters."
  [ctx]
  (:params (:request ctx)))

(defn- under-replicated-topics
  "Reports the number of underreplicated topics."
  []
  (let [zkconn (start #'kafka-connect)
        data
        (let [alltopics (topics/get-all-topics kafka-connect)
              total-topics (count alltopics)
              under-replicated-topics
              (for [topic alltopics
                    :when (pos?
                           (count
                            (for [mdata (t/topics-metadata kafka-connect topic)
                                  pdata (:partitions-metadata mdata)
                                  :when (= (:error pdata)
                                           :replica-unavailable)]
                              topic)))]
                topic)]
          {:data
           {:under-replicated-topics
            (count under-replicated-topics)
            :total-topics total-topics}})]
    (stop #'kafka-connect)
    data))

(defn GET
  [ctx]
  (let [body (under-replicated-topics)]
    (if-not (pos? (:under-replicated-topics (:data body)))
      {:status 200
       :header {"Content-Type" "application/json"}
       :body (write-str body)}
      {:status 503
       :header {"Content-Type" "application/json"}
       :body (write-str body)})))

(defn -main
  "List all topics"
  [& args]
  (GET ""))
