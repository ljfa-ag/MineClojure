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

(.setDynamic (intern repl-ns 'me)) ;this gets bound to the command sender

(defn -processCommand
  [this sender args]
  (try
    (let [in-str (clojure.string/join " " args)
          output (with-open [wr (chat-writer sender)]
                   (binding [*ns* repl-ns, *out* wr, mineclj/me sender]
                     (eval (read-string in-str))))]
      (send-chat-lines sender (str-nil output)))
    (catch Throwable e
      (throw (CommandException. (str e) (into-array []))))))
