(ns de.ljfa.mineclojure.command-clj
  (:require [de.ljfa.mineclojure.util :refer :all])
  (:import (net.minecraft.command CommandBase ICommandSender CommandException)
           (net.minecraft.util ChatComponentText)))

(gen-class
  :name de.ljfa.mineclojure.command-clj
  :extends net.minecraft.command.CommandBase)

(defn -getCommandName
  [this]
  "clj")

(defn -getCommandUsage
  [this sender]
  "clj <clojure form>")

;Create a custom namespace for the REPL
(def repl-ns
  (create-ns 'mineclj))

;and make clojure.core accessible in there
(binding [*ns* repl-ns]
  (use 'clojure.core))

(.setDynamic (intern repl-ns 'me)) ;gets bound to the command sender

;get bound to *1, *2, *3 and *e
(def r1 (ref nil))
(def r2 (ref nil))
(def r3 (ref nil))
(def re (ref nil))

(defn -processCommand
  [this sender args]
  (try
    (let [in-str (clojure.string/join " " args)
          output (with-open [wr (chat-writer sender)]
                   (binding [*ns* repl-ns, *out* wr, mineclj/me sender,
                             *1 @r1, *2 @r2, *3 @r3, *e @re]
                     (eval (read-string in-str))))]
      (send-chat-lines sender (str-nil output))
      (dosync
        (ref-set r3 @r2)
        (ref-set r2 @r1)
        (ref-set r1 output)))
    (catch Throwable e
      (dosync (ref-set re e))
      (throw (CommandException. (str e) (into-array []))))))
