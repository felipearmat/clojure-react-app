(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [clojure.pprint]
    [clojure.spec.alpha :as s]
    [clojure.tools.namespace.repl :as repl]
    [criterium.core :as c]                                  ;; benchmarking
    [expound.alpha :as expound]
    [integrant.core :as ig]
    [integrant.repl :refer [clear go halt prep init reset reset-all]]
    [integrant.repl.state :as state]
    [kit.api :as kit]
    [lambdaisland.classpath.watch-deps :as watch-deps]      ;; hot loading for deps
    [sample.app.core :refer [start-app]]))

;; uncomment to enable hot loading for deps
(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (sample.app.config/system-config {:profile :dev})
                                  (ig/prep)))))

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (sample.app.config/system-config {:profile :test})
                                  (ig/prep)))))

;; Can change this to test-prep! if want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)

(defn reset-db []
  (migratus.core/reset (:db.sql/migrations state/system)))

(defn rollback []
  (migratus.core/rollback (:db.sql/migrations state/system)))

(defn migrate []
  (migratus.core/migrate (:db.sql/migrations state/system)))

(defn query-fn [& vars]
  (apply (:db.sql/query-fn state/system) vars))

(defn create-migration
  [migration-name]
  (migratus.core/create (:db.sql/migrations state/system) migration-name))

(defn reset-config
  ([env]
    (let [cfg (sample.app.config/system-config {:profile env})]
      (alter-var-root #'state/config (constantly cfg))))
  ([]
    (reset-config :dev)))

(defn reset-system
  [keys-vector]
    (reset-config)
    (init keys-vector))

(defn start-repl
  ([_]
    (reset-system [:db.sql/query-fn]))
  ([]
    (start-repl nil)))

(defn start-dev [_]
  (go))

(comment
  (go)
  (reset))
