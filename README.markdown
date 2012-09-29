A [Minecraft Pocket Edition](https://play.google.com/store/apps/details?id=com.mojang.minecraftpe) inventory editor for Android.

This project uses the [SpoutNBT](https://github.com/SpoutDev/SpoutNBT) library.

### Compiling ###

PocketInvEditor is structured so that code common to both the free and the Pro version
is included in the PocketInvEditor library project, while code for only the free version
goes into the PocketInvEditor_app project.

Building with the Ant buildscript from the Android SDK:

1: git clone https://github.com/zhuowei/PocketInvEditor.git

2: git clone https://github.com/zhuowei/PocketInvEditor_app.git

3: cd PocketInvEditor_app

4: (optional) ./builtfor_amazon.sh or ./builtfor_play.sh # this changes the app market link in the About menu - the default goes to Google Play

5: ant clean debug install

6: ???

7: Profit!

PocketInvEditor depends on an old version of SpoutNBT, which is included with this repository in the libs folder. 

I have not tested building with Eclipse; however, if you open PocketInvEditor_app,
add PocketInvEditor as a library project, and add the spoutnbt-1.0.2-SNAPSHOT.jar to
your classpath, it probably would build in Eclipse.

