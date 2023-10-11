(ns sample.app.web.models.users
  (:require
    [buddy.hashers :as hashers]
    [clojure.string :as string]
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [query-fn validate-spec db-error]]))

(def trusted-algs #{:bcrypt+blake2b-512})

(def password-regex
  #"^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}")

(def email-regex #"^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$")

(spec/def ::email (spec/and string? #(re-matches email-regex %)))

(spec/def ::password (spec/and string? #(re-matches password-regex %)))

(spec/def ::create-user (spec/keys :req-un [::email ::password]))

(defn encrypt
  [password]
  (hashers/derive password {:alg :bcrypt+blake2b-512 :iterations 13}))

(defn get-users
  [where]
  (query-fn :get-users {:where where}))

(defn create-user!
  [email password]
  (let [data {:email (string/lower-case email) :password password}]
    (if (empty? (get-users {:email email}))
      (->> data
        (validate-spec ::create-user)
        (#(merge % {:password (encrypt password)}))
        (query-fn :create-user!))
      (throw (db-error "User already exists.")))))

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
  ([attempt where {:keys [update]}]
  (let [encrypted (:password (first (get-users where)))
        result (hashers/verify attempt encrypted {:limit trusted-algs})]
    ;; TODO: Verify why (:update result) always returning 'true'
    ;; (when (and update (:valid result))
    ;;   (when (:update result)
    ;;     (println "Password updated!")
    ;;     (update-password! attempt where)))
    (:valid result)))
  ([attempt where]
  (verify-password attempt where {:update false})))
