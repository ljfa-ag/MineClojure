(ns de.ljfa.mineclojure.core
  (:import (cpw.mods.fml.common Mod Mod$EventHandler)
           (cpw.mods.fml.common.event FMLInitializationEvent FMLServerStartedEvent)
           (net.minecraft.server MinecraftServer)
           (net.minecraft.command ServerCommandManager)
           (org.apache.logging.log4j Logger Level))
  (:require de.ljfa.mineclojure.command-clj))

(def ^:const modid "mineclojure")
(def ^:const modversion "${version}")
(def ^:const modname "MineClojure")

(gen-class :name
  ^{Mod {:modid "mineclojure", :version "${version}"}}
  de.ljfa.mineclojure.core
  :methods [[^{Mod$EventHandler {}} init [cpw.mods.fml.common.event.FMLInitializationEvent] void]
            [^{Mod$EventHandler {}} serverStarted [cpw.mods.fml.common.event.FMLServerStartedEvent] void]])

(def ^Logger logger
  (org.apache.logging.log4j.LogManager/getLogger modname))

(defn log
  "Logs a message. The logging level is given as keyword."
  [level str]
  (let [log4jlvl (case level
                   :trace Level/TRACE
                   :debug Level/DEBUG
                   :info Level/INFO
                   :warn Level/WARN
                   :error Level/ERROR
                   :fatal Level/FATAL)]
    (.log logger log4jlvl str)))

(defn -init
  [this event]
  (log :info "Oh, Vic is going to hate this :P"))

(defn -serverStarted
  [this event]
  (let [^ServerCommandManager cmgr (.getCommandManager (MinecraftServer/getServer))]
    (.registerCommand cmgr (de.ljfa.mineclojure.command-clj.))))
