(ns de.ljfa.mineclojure.util
  (:import (net.minecraft.command CommandBase ICommandSender CommandException)
           (net.minecraft.util ChatComponentText)
           (org.apache.logging.log4j LogManager Logger Level)
           (java.io Writer StringWriter))
  (:require [clojure.string :as s]))

(def ^Logger logger
  (LogManager/getLogger "MineClojure"))

(defn log4jlvl
  "Converts a keyword (:error, :warn etc.) to a log4j logging level"
  [level]
  (case level
    :trace Level/TRACE
    :debug Level/DEBUG
    :info Level/INFO
    :warn Level/WARN
    :error Level/ERROR
    :fatal Level/FATAL
    Level/INFO))

(defn log
  "Logs a message with parameters, with the logging level given as keyword"
  ([level str]
    (.log logger (log4jlvl level) str))
  ([level str & args]
    (.log logger (log4jlvl level) str (into-array Object args))))

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

(defn ^Writer chat-writer
  "Creates a Writer that writes into the chat of the given command sender"
  [sender]
  (proxy [StringWriter] []
    (flush []
      (let [^StringWriter this this]
        (send-chat-lines sender (.toString this))
        (.setLength (.getBuffer this) 0)))
    (close []
      (.flush ^Writer this))))
