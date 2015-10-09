(ns de.ljfa.mineclojure.core
  (:require de.ljfa.mineclojure.command-clj
            [de.ljfa.mineclojure.util :refer [log]])
  (:import (cpw.mods.fml.common Mod Mod$EventHandler)
           (cpw.mods.fml.common.event FMLInitializationEvent FMLServerStartedEvent)
           (net.minecraft.server MinecraftServer)
           (net.minecraft.command ServerCommandManager)
           (de.ljfa.mineclojure command-clj)))

(def ^:const modid "mineclojure")
(def ^:const modversion "${version}")
(def ^:const modname "MineClojure")

(gen-class :name
  ^{Mod {:modid "mineclojure", :version "${version}", :acceptableRemoteVersions "*"}}
  de.ljfa.mineclojure.core
  :methods [[^{Mod$EventHandler {}} init [cpw.mods.fml.common.event.FMLInitializationEvent] void]
            [^{Mod$EventHandler {}} serverStarted [cpw.mods.fml.common.event.FMLServerStartedEvent] void]])

(defn -init
  [this event]
  (log :info "Oh, Vic is going to hate this :P"))

(defn -serverStarted
  [this event]
  (let [^ServerCommandManager cmgr (.getCommandManager (MinecraftServer/getServer))]
    (.registerCommand cmgr (command-clj.))))
