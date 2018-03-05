(ns kafka-check.kafka-connect
  (:require [franzy.admin.serializers :as serializers]
            [franzy.admin.zookeeper.client :as client]
            [kafka-check.connection-data :refer [zk-connection-string]]
            [mount.core :refer [defstate]]))

(defstate kafka-connect
  :start (let [config {:servers (zk-connection-string)
                       :connection-timeout 30000
                       :session-timeout 30000              ;;from ZkConnection default
                       :operation-retry-timeout (long -1)  ;;from ZkClient default
                       :serializer (serializers/zk-string-serializer)}]
           (client/make-zk-utils config true))
  :stop (.close kafka-connect))
