(ns beer.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]))

;; Temperature sensors 1 and 2
(defn temperature-one []
  (let [t1 (re-frame/subscribe [:temp-one])]
    (fn []
      [re-com/box
       :child [:p (str @t1 "\u00B0")]])))

(defn temperature-two []
  (let [t2 (re-frame/subscribe [:temp-two])]
    (fn []
      [re-com/box
       :child [:p (str @t2 "\u00B0")]])))

;; Target temperatures 1 and 2
(defn target-one []
  (let [t1 (re-frame/subscribe [:target-one])]
    (fn []
      [re-com/box
       :child [re-com/input-text
               :model @t1
               :change-on-blur? true
               :on-change #(re-frame/dispatch [:set-target-1 %1])]])))

(defn target-two []
  (let [t2 (re-frame/subscribe [:target-two])]
    (fn []
      [re-com/box
       :child [re-com/input-text
               :model @t2
               :change-on-blur? true
               :on-change #(re-frame/dispatch [:set-target-2 %1])]])))

;; Pumps 1 and 2
(defn pump-one []
  (let [p1 (re-frame/subscribe [:pump-one])]
    (fn []
      [re-com/box
       :class (:class @p1)
       :attr {:on-click #(re-frame/dispatch [:toggle-relay :pump-1])}
       :child [:p "Pump One"]])))

(defn pump-two []
  (let [p2 (re-frame/subscribe [:pump-two])]
    (fn []
      [re-com/box
       :class (:class @p2)
       :attr {:on-click #(re-frame/dispatch [:toggle-relay :pump-2])}
       :child [:p "Pump Two"]])))

;; Solenoids 1 and 2
(defn solenoid-one []
  (let [s1 (re-frame/subscribe [:pump-one])]
    (fn []
      [re-com/box
       :class (:class @s1)
       :attr {:on-click #(re-frame/dispatch [:toggle-relay :solenoid-1])}
       :child [:p "Solenoid One"]])))

(defn solenoid-two []
  (let [s2 (re-frame/subscribe [:pump-two])]
    (fn []
      [re-com/box
       :class (:class @s2)
       :attr {:on-click #(re-frame/dispatch [:toggle-relay :pump-2])}
       :child [:p "Solenoid Two"]])))

;; Putting all the components together here
(defn main-panel []
  (fn []
    [re-com/v-box
     :height "100%"
     :children [[re-com/h-box
                 :children [[temperature-one]
                            [temperature-two]]]
                [re-com/h-box
                 :children [[target-one]
                            [target-two]]]
                [re-com/h-box
                 :children [[pump-one]
                            [pump-two]]]]]))
