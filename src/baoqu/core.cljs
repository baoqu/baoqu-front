(ns baoqu.core
  (:require [rum.core :as rum]
            [goog.dom :as dom]
            [bidi.router :as bidi]
            [baoqu.components.base :as base-c]))

(rum/mount (base-c/main) (dom/getElement "app"))
