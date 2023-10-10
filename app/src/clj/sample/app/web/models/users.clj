(ns sample.app.web.models.users
  (:require
    [buddy.hashers :as hashers]
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [query-fn]]))

(def trusted-algs #{:argon2id :bcrypt+blake2b-512})

(defn encrypt
  [password]
  (hashers/derive password {:alg :bcrypt+blake2b-512 :iterations 15}))

(defn create-user!
  [email password]
  (let [encrypted (encrypt password)]
    (query-fn :create-user! {:email email
                             :password encrypted})))

(defn get-users
  [where]
  (query-fn :get-users {:where where}))

(defn get-deleted-users
  [where]
  (query-fn :get-deleted-users {:where where}))

(defn update-user!
  [where set]
  (query-fn :update-user! {:set   set
                           :where where}))

(defn deactivate-user!
  [email]
  (query-fn :deactivate-user! {:email email}))

(defn delete-user!
  [email]
  (query-fn :delete-user! {:email email}))

(defn update-password!
  [new-password where]
  (update-user! where {:password (encrypt new-password)}))

(defn verify-password
  [attempt where]
  (let [encrypted (:password (first (get-users where)))
        result (hashers/verify attempt encrypted {:limit trusted-algs})]
    (when (:valid result)
      (when (:update result)
        (update-password! attempt where)))))
