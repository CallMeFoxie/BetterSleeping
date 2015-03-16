# Better Sleeping #
This mod enhances the way that the sleeping mechanic works in Minecraft. You can go to sleep any time of the day provided you are tired. You can also wake up any time of the day if you set an alarm clock right next to your bed.

More information is on [CurseForge](http://minecraft.curseforge.com/mc-mods/227114-better-sleeping).

## Modpacks ##
Feel free to use it any modpack as you wish.

The only thing I dislike is rehosting my mod on 3rd party websites. If you want to link my mod then link the Curse page, do NOT rehost it yourself. (Modpack rehosting is fine of course.)

## Developer Environment ##
If you want to do some work on this mod OR want just to load it in your development environment for any reason, you can either:

a) get precompiled versions from my [Jenkins](http://ondraster.cz:8080/job/Better%20Sleeping/) - note that versions here do not match versions on Curse, the ones on Curse do not include build number! (The versions themselves do though, as you can check in mcmod.info.)

b) set it in your dev environment yourself:

```
in command line:
gradlew setupDecompWorkspace

If you want to use IntelliJ Idea you can either import the project OR
gradlew idea
If you want to use Eclipse
gradlew eclipse

In order for the mod to properly run you also need to make sure that the coremod is loaded by adding a parameter to the launch parameters:
-Dfml.coreMods.load=cz.ondraster.bettersleeping.asm.BetterSleepingCoreLoader
```

Now your development environment should be ready to run my mod.

## API ##
The API should be as stable as possible, you can either grab it yourself from here or you can grab a separate JAR from the [Jenkins](http://ondraster.cz:8080/job/Better%20Sleeping/).

## Bug Reports and Feature Requests ##
Both should be done in Issues here on GitHub. When you are reporting a crash, DEFINITELY include a crashlog and preferably even the main log itself (so it includes the list of other mods you are running etc). If you did something special, DESCRIBE IT! If you are reporting a gameplay bug, DESCRIBE IT!

Feature Requests should be as descriptive as possible.

Language Pull Requests are no problem.

Code Pull Requests will be checked, make sure you know what you're doing!
