(ns kafka-check.topics
  (:require
   [franzy.admin.topics :as topics]))

(defn get-all-topics
  [conn]
  (topics/all-topics conn))

(defn alltopics
  [zkconn]
  (topics/topic-replica-assignments
   zkconn
   (get-all-topics zkconn)))

(defn underreplicated
  [zkconn]
  (for [topic (alltopics zkconn)
        :when (< (count (second topic)) 3)]
    topic))
