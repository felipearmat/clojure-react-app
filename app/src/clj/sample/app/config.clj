(ns sample.app.config
  (:require
    [kit.config :as config]))

(def ^:const system-filename "system.edn")

(defn- list-files
  "Returns a seq of java.io.Files inside a path and all of it's subfolders"
  [path]
  (filter #(.isFile %) (file-seq (clojure.java.io/file path))))

(defn- filenames-by-folder
  "Returns a vector of filename paths relative to 'resources'
   folder to be used on system-file configuration"
  [folder]
  (let [fullpath  (str "./resources/" folder)
        all-files (list-files fullpath)
        filenames (filter #(re-find #".sql" (.getPath %)) all-files)]
    (mapv #(clojure.string/replace (str %) #"./resources/" "") filenames)))

(defn handle-folder-key-config
  "If system-config has a [:db.sql/query-fn :folder] key, change
   it to a [:db.sql/query-fn :filenames] key and updates its values
   with a vector of sql files found on './resources/<folder-path>'"
  [config]
  (let [query-config     (:db.sql/query-fn config)
        folder-path      (:folder query-config)]
    (if (seq folder-path)
      (->> (filenames-by-folder folder-path)
           (assoc query-config :filenames)
           (assoc config :db.sql/query-fn))
      config)))

(defn system-config
  [options]
  (handle-folder-key-config
    (config/read-config system-filename options)))
