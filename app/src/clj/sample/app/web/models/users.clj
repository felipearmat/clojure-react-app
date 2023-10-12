(ns sample.app.web.models.users
  (:require
    [buddy.hashers :as hashers]
    [clojure.string :as string]
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [query-fn validate-spec db-error]]))

(def trusted-algs #{:bcrypt+blake2b-512})

(def uuid-regex
  #"^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$")

(def email-regex
  #"^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$")

(def password-regex
  #"^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}")

(spec/def :users/uuid
  (spec/and string? #(re-matches uuid-regex %)))

(spec/def :users/email
  (spec/and string? #(re-matches email-regex %)))

(spec/def :users/password
  (spec/and string? #(re-matches password-regex %)))

(spec/def :users/create-user!
  (spec/keys :req-un [:users/email :users/password]))

(spec/def :users.gen/all #(= :all %))

(spec/def :users.gen/where (spec/or :where map?
                                    :all   :users.gen/all))

(spec/def :users.gen/set map?)

(spec/def :users.password/where
  (spec/keys :req-un [(or :users/email :users/uuid)]))

(spec/def :users/update-user!
  (spec/keys :req-un [:users.update/where :users.update/set]))

(defn encrypt
  [password]
  (hashers/derive password {:alg :bcrypt+blake2b-512 :iterations 13}))

(defn normalize-email
  [email]
  (->> email
    (string/lower-case)
    (validate-spec :users/email)))

(defn normalize-where
  [where]
  (let [email (:email where)]
    (cond-> where
      (seq email) (merge {:email (normalize-email email)})
      true        (#(validate-spec :users.gen/where %)))))

(defn get-users
  [where]
  (-> where
    (normalize-where)
    (#(query-fn :get-users {:where %}))))

(defn create-user!
  [email password]
  (let [data {:email (string/lower-case email) :password password}]
    (validate-spec :users/create-user! data)
    (if (empty? (get-users (select-keys data [:email])))
      (-> data
        (merge {:password (encrypt password)})
        (#(query-fn :create-user! %)))
      (throw (db-error "User already exists.")))))

(defn get-deleted-users
  [where]
  (-> where
    (normalize-where)
    (#(query-fn :get-deleted-users {:where %}))))

(defn update-user!
  [where set]
  (let [data {:where where :set set}]
    (validate-spec :users/update-user! data)
    (query-fn :update-user! data)))

(defn deactivate-user!
  [email]
  (-> email
    (normalize-email)
    (#(update-user! {:email %}
                    {:status "inactive"}))))

(defn activate-user!
  [email]
  (-> email
    (normalize-email)
    (#(update-user! {:email %}
                    {:status "active"}))))

(defn delete-user!
  [email]
  (-> email
    (normalize-email)
    (#(query-fn :delete-user! {:email %}))))

(defn update-password!
  [new-password where]
  (validate-spec :users/password new-password)
  (-> where
    (normalize-where)
    (update-user! {:password (encrypt new-password)})))

(defn verify-password
  [attempt where]
  (when-let [user (first (get-users where))]
    (when (not (:inactive user))
      (-> (:password user)
        (#(hashers/verify attempt % {:limit trusted-algs}))
        (:valid)))))
