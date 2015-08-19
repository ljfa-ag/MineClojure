(ns de.ljfa.mineclojure.command-clj
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
  "clj <form>")

(defn send-chat
  [^ICommandSender sender msg]
  (.addChatMessage sender (ChatComponentText. (str msg))))

;Create a custom namespace for the REPL
(def repl-ns
  (create-ns 'mineclj))

;and make clojure.core accessible in there
(binding [*ns* repl-ns]
  (use 'clojure.core))

(defn -processCommand
  [this sender args]
  (try
    (as-> args x
      (clojure.string/join " " x)
      (read-string x)
      (binding [*ns* repl-ns] (eval x))
      (send-chat sender x))
    (catch Throwable e
      (throw (CommandException. (str e) (into-array []))))))
