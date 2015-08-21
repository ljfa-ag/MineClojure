(ns de.ljfa.mineclojure.command-clj
  (:require [de.ljfa.mineclojure.util :refer :all])
  (:import (net.minecraft.command CommandBase ICommandSender CommandException)
           (net.minecraft.util ChatComponentText)
           (de.ljfa.mineclojure.util chat-writer)))

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

(.setDynamic (intern repl-ns 'me)) ;this gets bound to the command sender

(defn -processCommand
  [this sender args]
  (try
    (as-> args x
      (clojure.string/join " " x)
      (with-open [wr (chat-writer. sender)]
        (binding [*ns* repl-ns, *out* wr, mineclj/me sender]
         (eval (read-string x))))
      (send-chat-lines sender (str-nil x)))
    (catch Throwable e
      (throw (CommandException. (str e) (into-array []))))))
