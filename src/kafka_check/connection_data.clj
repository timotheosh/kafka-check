(ns kafka-check.connection-data
  (:require [clojure.java.io :refer [file]]
            [clojurewerkz.propertied.properties :refer [load-from
                                                        properties->map]]))
(defn read-properties
  "Reads a Java properties file and loads it as Clojure data."
  [filename]
  (properties->map (load-from (file filename)) true))

(defn zk-connection-string
  "Return the zookeeper connection string from kafka's local server.properties file."
  []
  (:zookeeper.connect (read-properties "/etc/kafka/server.properties")))
