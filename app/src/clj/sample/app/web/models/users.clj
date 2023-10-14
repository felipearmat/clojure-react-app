(ns sample.app.web.models.users
  (:require
    [buddy.hashers :as hashers]
    [clojure.string :as str]
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [db-error query-fn validate-spec]]))

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
  ;; A password that must contain at least one uppercase letter, one lowercase
  ;; letter, one digit, one special character, and be at least 8 characters long.
  (spec/and string? #(re-matches password-regex %)))

(spec/def :users/create-user!
  (spec/keys :req-un [:users/email :users/password]))

(spec/def :users.password/where
  (spec/keys :req-un [(or :users/email :users/uuid)]))

(spec/def :users/update-users!
  (spec/keys :req-un [:general/where :general.update/set]))

(defn encrypt
  "Encrypts a password using the bcrypt+blake2b-512 algorithm with 13 iterations."
  [password]
  (hashers/derive password {:alg :bcrypt+blake2b-512 :iterations 13}))

(defn normalize-email
  "Normalizes an email address by converting it to lowercase and validating its format."
  [email]
  (->> email
    (str/lower-case)
    (validate-spec :users/email)))

(defn normalize-where
  "Normalizes a 'where' map, especially normalizing email addresses within it."
  [{:keys [email] :as where}]
  (cond-> where
    (seq email) (merge {:email (normalize-email email)})
    true        (#(validate-spec :general/where %))))

(defn get-users
  "Retrieves user records based on the 'where' conditions."
  [where]
  (-> where
    (normalize-where)
    (#(query-fn :get-users {:where %}))))

(defn create-user!
  "Creates a new user with the given email and password.
  Returns 1 on success or throws an exception if the user already exists."
  [email password]
  (let [data {:email (str/lower-case email) :password password}]
    (validate-spec :users/create-user! data)
    (if (empty? (get-users (select-keys data [:email])))
      (-> data
        (merge {:password (encrypt password)})
        (#(query-fn :create-user! %)))
      (throw (db-error "User already exists.")))))

(defn get-deleted-users
  "Retrieves deleted user records based on the 'where' conditions."
  [where]
  (-> where
    (normalize-where)
    (#(query-fn :get-deleted-users {:where %}))))

(defn update-users!
  "Updates a user's information based on 'where' and 'set' conditions. Returns the number of rows affected"
  [where set]
  (let [data {:where where :set set}]
    (validate-spec :users/update-users! data)
    (query-fn :update-users! data)))

(defn deactivate-user!
  "Deactivates a user by setting their status to 'inactive'. Returns 1 on success"
  [email]
  (-> email
    (normalize-email)
    (#(update-users! {:email %} {:status "inactive"}))))

(defn activate-user!
  "Activates a user by setting their status to 'active'. Returns 1 on success"
  [email]
  (-> email
    (normalize-email)
    (#(update-users! {:email %} {:status "active"}))))

(defn delete-user!
  "Deletes a user based on their email address. Returns 1 on success"
  [email]
  (-> email
    (normalize-email)
    (#(query-fn :delete-user! {:email %}))))

(defn update-password!
  "Updates a user's password with a new one. Returns 1 on success"
  [new-password email]
  (validate-spec :users/password new-password)
  (-> email
    (normalize-email)
    (#(update-users! {:email %} {:password (encrypt new-password)}))))

(defn verify-password
  "Verifies a password attempt against the user's stored password hash.
  Returns nil if the user doesn't exist, is inactive, or the password is wrong."
  [attempt email]
  (when-let [user (last (get-users {:email email}))]
    (when (not (:inactive user))
      (-> (:password user)
        (#(hashers/verify attempt % {:limit trusted-algs}))
        (:valid)))))
