(ns baoqu.tests.core
  (:require [cljs.test :as t :include-macros true]
            [baoqu.tests.whatever]))

(enable-console-print!)

(defn main
  []
  (t/run-tests
   (t/empty-env)
   'baoqu.tests.whatever))

(defmethod t/report [:cljs.test/default :end-run-tests]
  [m]
  (if (t/successful? m)
    (set! (.-exitCode js/process) 0)
    (set! (.-exitCode js/process) 1)))

(set! *main-cli-fn* main)
