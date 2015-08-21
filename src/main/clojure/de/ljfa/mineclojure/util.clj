(ns de.ljfa.mineclojure.util
  (:import (net.minecraft.command CommandBase ICommandSender CommandException)
           (net.minecraft.util ChatComponentText))
  (:require [clojure.string :as s]))

(defn send-chat
  "Sends an unlocalized chat message to the sender"
  [^ICommandSender sender msg]
  (.addChatMessage sender (ChatComponentText. msg)))

(defn send-chat-lines
  "Sends an unlocalized chat message which can contain multiple lines to the sender"
  [^ICommandSender sender msg]
  (when-not (empty? msg)
    (let [trmsg (s/trimr msg)]
      (doseq [line (s/split trmsg #"\r?\n")]
        (send-chat sender line)))))

(defn str-nil
  "Like str, but nil values get converted to \"nil\" rather than an empty string"
  [x]
  (if (nil? x)
    "nil"
    (str x)))

(gen-class
  :name de.ljfa.mineclojure.util.chat-writer
  :extends java.io.StringWriter
  :prefix chat-writer-
  :state sender
  :init init
  :constructors {[net.minecraft.command.ICommandSender] []})
(import de.ljfa.mineclojure.util.chat-writer)

(defn chat-writer-init
  [sender]
  [[] sender])

(defn chat-writer-flush
  [^chat-writer this]
  (send-chat-lines (.sender this) (.toString this))
  (-> this (.getBuffer) (.setLength 0)))

(defn chat-writer-close
  [^chat-writer this]
  (.flush this))
