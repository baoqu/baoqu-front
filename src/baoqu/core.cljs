(ns baoqu.core
  (:require [rum.core :as rum]
            [goog.dom :as dom]
            [baoqu.routes]
            [baoqu.components.base :as base-c]))

(rum/mount (base-c/main) (dom/getElement "app"))
