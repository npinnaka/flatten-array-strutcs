(ns flatten-array-strutcs.core
  (:require [flambo.session :as session]
            [flambo.sql :as sql]
            [clojure.string :as strg])
  (:import [org.apache.spark.sql functions
            SaveMode
            Column])
  (:gen-class))

(defn build-spark-session[app-name]
  (->
   (session/session-builder)
   (session/master "local")
   (session/get-or-create)))

(def spark (build-spark-session "app"))
(def sql-ctx ())

(def department-fields
  {["IT" "SALES" "MARKETING" "OPERATIONS" "FINANCE" "HR"] ["department" "emp_count" "expenses"]})

(defn fy-max[key-name value]
  (str "max(" (str key-name "_" value) ") as " (str key-name "_" value) ""))

(defn fy-case[key-name value]
  (str "case when depts.department = '" key-name "' then depts." value " end as " (str key-name "_" value) ""))

(defn fx[key-name bool-value]
  (if (true? bool-value)
    (map #(fy-case key-name %) (val (first department-fields)))
    (map #(fy-max key-name %) (val (first department-fields)))))

(defn get-case-max-statemets [bool-value]
  (flatten (map #(fx % bool-value) (key (first department-fields)))))


(defn -main
  "I don't do a whole lot ... yet. "
  [& args]
  (let [data-set                                       (->
                                                        spark
                                                        (.read)
                                                        (.json "resources/company_departments.json"))
        _ (.printSchema data-set)
        _                                              (.createOrReplaceTempView data-set "json_view")
        exploded-data-set                              (.sql spark "select id, explode(dept) as depts from json_view")
        _                                              (.createOrReplaceTempView exploded-data-set "comp_dept")
        cased-data-frame                               (.sql spark (str "select id, " (strg/join "," (get-case-max-statemets true)) " from comp_dept "))
        _                                              (.createOrReplaceTempView  cased-data-frame "comp_temp_case")
        flattened-data-frame                           (.sql spark
                                                             (str "select id, " (strg/join "," (get-case-max-statemets false)) " from comp_temp_case group by id"))]
    (->
      flattened-data-frame
     (.coalesce 1)
     (.write)
     (.mode SaveMode/Overwrite )
     (.json "output.json"))))
