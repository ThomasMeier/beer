(ns beer.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :temp-one
 (fn [db]
   (reaction (:temp-1 @db))))

(re-frame/register-sub
 :temp-two
 (fn [db]
   (reaction (:temp-2 @db))))

(re-frame/register-sub
 :target-one
 (fn [db]
   (reaction (:target-1 @db))))

(re-frame/register-sub
 :target-two
 (fn [db]
   (reaction (:target-2 @db))))

(re-frame/register-sub
 :pump-one
 (fn [db]
   (reaction (:pump-1 @db))))

(re-frame/register-sub
 :pump-two
 (fn [db]
   (reaction (:pump-2 @db))))

(re-frame/register-sub
 :solenoid-one
 (fn [db]
   (reaction (:solenoid-1 @db))))

(re-frame/register-sub
 :solenoid-two
 (fn [db]
   (reaction (:solenoid-2 @db))))
