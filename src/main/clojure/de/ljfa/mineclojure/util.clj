(ns de.ljfa.mineclojure.util
  (:import (net.minecraft.command CommandBase ICommandSender CommandException)
           (net.minecraft.util ChatComponentText)))

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