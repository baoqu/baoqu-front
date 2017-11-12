(ns baoqu.tests.whatever
  (:require [cljs.test :as t :include-macros true]))

(enable-console-print!)

(t/deftest whatever-test
  (let [name "Miguel"]
    (t/is (= "Miguel" name))))
