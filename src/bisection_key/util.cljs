
(ns bisection-key.util (:require [bisection-key.core :refer [mid-id max-id min-id bisect]]))

(defn key-after [dict base-key]
  (assert (string? base-key) "base-key should be string")
  (assert (map? dict) "dict should be a map")
  (let [existing-keys (vec (sort (keys dict))), keys-set (set existing-keys)]
    (assert (contains? keys-set base-key) "base-key should be existed")
    (let [position (.indexOf existing-keys base-key)]
      (bisect
       base-key
       (if (= position (dec (count existing-keys)))
         max-id
         (get existing-keys (inc position)))))))

(defn assoc-after [dict base-key v]
  (let [new-key (key-after dict base-key)] (assoc dict new-key v)))

(defn has-nth? [x n] (< n (count x)))

(defn key-nth [x n] (if (has-nth? x n) (nth (sort (keys x)) n) nil))

(defn assoc-after-nth [x n v]
  (when-not (has-nth? x n) (throw (js/Error. "Succeeded map size")))
  (let [k (key-nth x n)] (assoc-after x k v)))

(defn key-append [dict]
  (assert (map? dict) "dict should be a map")
  (if (empty? dict)
    mid-id
    (let [last-key (last (sort (keys dict)))] (bisect last-key max-id))))

(defn assoc-append [dict v]
  (assert (map? dict) "dict should be a map")
  (let [k (key-append dict)] (assoc dict k v)))

(defn key-before [dict base-key]
  (assert (string? base-key) "base-key should be string")
  (assert (map? dict) "dict should be a map")
  (let [existing-keys (vec (sort (keys dict))), keys-set (set existing-keys)]
    (assert (contains? keys-set base-key) "base-key should be existed")
    (let [position (.indexOf existing-keys base-key)]
      (bisect (if (zero? position) min-id (get existing-keys (dec position))) base-key))))

(defn assoc-before [dict base-key v]
  (let [new-key (key-before dict base-key)] (assoc dict new-key v)))

(defn assoc-before-nth [x n v]
  (when-not (has-nth? x n) (throw (js/Error. "Succeeded map size")))
  (let [k (key-nth x n)] (assoc-before x k v)))

(defn assoc-nth [x n v]
  (when-not (has-nth? x n) (throw (js/Error. "Succeeded map size")))
  (let [k (key-nth x n)] (assoc x k v)))

(defn key-prepend [dict]
  (assert (map? dict) "dict should be a map")
  (if (empty? dict)
    mid-id
    (let [first-key (first (sort (keys dict)))] (bisect min-id first-key))))

(defn assoc-prepend [dict v]
  (assert (map? dict) "dict should be a map")
  (let [k (key-prepend dict)] (assoc dict k v)))

(defn get-max-key [x] (apply max (keys x)))

(defn get-min-key [x] (apply min (keys x)))

(defn val-nth [x n]
  (if (has-nth? x n) (get x (key-nth x n)) (do (js/console.warn "exceeded map size") nil)))
