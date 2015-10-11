# MineClojure
This is a small proof-of-concept mod that adds a `/clj` command, which executes
its argument as Clojure code. Basically, it is an ingame Clojure REPL
(read-eval-print loop), and you have full access to all Minecraft and mod
classes from within the game. Be careful, as this can be used to execute
arbitrary code on the server.

Unfortunately though, Minecraft's obfuscation makes it basically useless
outside of a dev environment, unless you want to type names such as
"field_150348_b" or "func_149715_a".

Well, you can try the following from within a dev environment for fun
(the easiest way to start one is with `gradlew runClient`):
```clojure
/clj (import net.minecraft.init.Blocks)
/clj (doto Blocks/stone (.setHardness 0.1) (.setLightLevel 0.9))
/clj (.setFire me 3)  ;"me" is bound to the command sender
```

Either way, interfacing with Java code from Clojure is pretty cumbersome,
let alone that not many people know the language at all.

Ultimately, this is nothing more than a toy and is probably not going to
become anything serious or useful.
