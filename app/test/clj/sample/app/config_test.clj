;; (ns sample.app.config-test
;;   (:require
;;     [clojure.test :refer :all]
;;     [sample.app.config :as config]))

;; ;; Define a test resource path for testing purposes
;; (def test-resources "./resources/")
;; (def test-folder "sample-folder")
;; (def test-files ["sample-file1.sql" "sample-file2.sql"])
;; (def sample-system-config {:db.sql/query-fn {:folder test-folder}})

;; (defn delete-file [path]
;;   (try
;;     (let [file (clojure.java.io/file path)]
;;       (when (.exists file)
;;         (.delete file)))))

;; (defn with-test-files
;;   [path files]
;;   (fn [f]
;;   (let [file-paths (map #(str path "/" %) files)]
;;     (try
;;       (doseq [file-path file-paths]
;;         (spit file-path "test content"))
;;       (f)
;;       (finally
;;         (doseq [file-path file-paths]
;;           (delete-file file-path)))))))

;; (use-fixtures :once (with-test-files (str test-resources test-folder) test-files))

;; (deftest test-filenames-by-folder
;;   (testing "It returns a vector of filename paths relative to the resources folder"
;;     (with-redefs [config/resources-path test-resources]
;;       (is (= test-files (config/filenames-by-folder test-folder))))))

;; (deftest test-handle-folder-key-config
;;   (testing "It replaces [:db.sql/query-fn :folder] with [:db.sql/query-fn :filenames] and updates values"
;;     (with-redefs [config/resources-path test-resources]
;;       (let [expected-config {:db.sql/query-fn {:filenames test-files}}]
;;         (is (= expected-config (config/handle-folder-key-config sample-system-config)))))))

;; (deftest test-system-config
;;   (testing "It reads and handles the system configuration from the file"
;;     (let [expected-config {:db.sql/query-fn {:filenames test-files}}
;;           system-filename "test-system-config.edn"]
;;       (with-redefs [config/system-filename system-filename
;;                     config/resources-path  test-resources]
;;         (try
;;           (spit system-filename (pr-str sample-system-config))
;;           (is (= (config/system-config {}) expected-config))
;;           (finally
;;             (delete-file system-filename)))))))
