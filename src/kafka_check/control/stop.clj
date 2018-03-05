(ns kafka-check.control.stop
  (:require [com.stuartsierra.component :as component]
            [me.raynes.conch
             :refer [programs
                     with-programs
                     let-programs]
             :as sh]
            [clojure.string :as str])
  (:gen-class
   :name kafka_check.control.stop
   :methods [[POST [String] java.util.HashMap]]))

(defrecord Command [cmd data]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (with-programs [bash]
      (try
        (let [data (bash "-c" (:cmd this))]
          (assoc this :data {:status 200 :body data}))
        (catch clojure.lang.ExceptionInfo e
          (assoc this :data
                 {:status 501
                  :body
                  (str "Command: '" (:cmd this)  "' failed with: '"
                       (.getMessage e) "'")})))))

  (stop [this]
    this))

(defn execute-command [cmd]
  (let [command (map->Command {:cmd cmd})
        data (:data (.start command))]
    (.stop command)
    data))

(defn- get-params
  "Takes a request object and extracts the parameters."
  [ctx]
  (:params (:request ctx)))

(defn POST
  [ctx]
  (let [parms (get-params ctx)
        status (atom 503)
        body (atom "Requested Service Unavailable")]
    (when (= (:confirm parms) "true")
      (let [result
            (execute-command "/usr/local/bin/supervisorctl stop kafka:kafka-0")]
        (reset! status (:status result))
        (reset! body (:body result))))
    {:status @status
     :header {"Content-Type" "application/json"}
     :body @body}))
