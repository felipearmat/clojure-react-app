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
  ;; A password that must contain at least one uppercase letter, one lowercase
  ;; letter, one digit, one special character, and be at least 8 characters long.
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
  (spec/keys :req-un [:users.gen/where :users.gen/set]))

(defn encrypt
  "Encrypts a password using the bcrypt+blake2b-512 algorithm with 13 iterations."
  [password]
  (hashers/derive password {:alg :bcrypt+blake2b-512 :iterations 13}))

(defn normalize-email
  "Normalizes an email address by converting it to lowercase and validating its format."
  [email]
  (->> email
    (string/lower-case)
    (validate-spec :users/email)))

(defn normalize-where
  "Normalizes a 'where' map, especially normalizing email addresses within it."
  [{:keys [email] :as where}]
  (cond-> where
    (seq email) (merge {:email (normalize-email email)})
    true        (#(validate-spec :users.gen/where %))))

(defn get-users
  "Retrieves user records based on the 'where' conditions."
  [where]
  (-> where
    (normalize-where)
    (#(query-fn :get-users {:where %}))))

(defn create-user!
  "Creates a new user with the given email and password."
  [email password]
  (let [data {:email (string/lower-case email) :password password}]
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

(defn update-user!
  "Updates an user's information based on 'where' and 'set' conditions."
  [where set]
  (let [data {:where where :set set}]
    (validate-spec :users/update-user! data)
    (query-fn :update-user! data)))

(defn deactivate-user!
  "Deactivates an user by setting their status to 'inactive'."
  [email]
  (-> email
    (normalize-email)
    (#(update-user! {:email %}
                    {:status "inactive"}))))

(defn activate-user!
  "Activates an user by setting their status to 'active'."
  [email]
  (-> email
    (normalize-email)
    (#(update-user! {:email %}
                    {:status "active"}))))

(defn delete-user!
  "Deletes an user based on their email address."
  [email]
  (-> email
    (normalize-email)
    (#(query-fn :delete-user! {:email %}))))

(defn update-password!
  "Updates an user's password with a new one."
  [new-password email]
  (validate-spec :users/password new-password)
  (-> email
    (normalize-email)
    (#(update-user! {:email %}
                    {:password (encrypt new-password)}))))

(defn verify-password
  "Verifies a password attempt against the user's stored password hash."
  [attempt email]
  (normalize-email email)
  (when-let [user (first (get-users {:email email}))]
    (when (not (:inactive user))
      (-> (:password user)
        (#(hashers/verify attempt % {:limit trusted-algs}))
        (:valid)))))
