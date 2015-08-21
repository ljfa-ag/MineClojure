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
  "clj <clojure form>")

(defn send-chat
  "Sends an unlocalized chat message to the sender"
  [^ICommandSender sender msg]
  (.addChatMessage sender (ChatComponentText. msg)))

(defn send-chat-lines
  "Sends an unlocalized chat message which can contain multiple lines to the sender"
  [^ICommandSender sender msg]
  (doseq [line (clojure.string/split msg #"\r?\n")]
    (send-chat sender line)))

(defn str-nil
  "Like str, but nil values get converted to \"nil\" rather than an empty string"
  [x]
  (if (nil? x)
    "nil"
    (str x)))
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
      (read-string x)
      (binding [*ns* repl-ns, mineclj/me sender]
        (eval x))
      (send-chat-lines sender (str-nil x)))
    (catch Throwable e
      (throw (CommandException. (str e) (into-array []))))))
