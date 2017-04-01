AntiTimer 0.2.1
======


But you didn't make this?!?!
------
Correct. This was forked from https://bitbucket.org/WydD/antitimer/ so I could continue its development. All credit goes to WydD for figuring out how to get data from Antichamber and creating the bulk of this program. I'm just here to add some extra features.

Cali made a video tutorial on how to setup AntiTimer. Go check him out: http://www.twitch.tv/cali292292/c/3765832

AntiTimer DOES NOT WORK with the Standalone version
------
I'm working on finding a workaround. For now, it's working only on the Windows Steam version.

Get it!
------
Go the Download section here https://bitbucket.org/WydD/antitimer/downloads and download the latest binary

Configuring your Antichamber
------
Antichamber does not provide a trigger for clicking on the map (start timer) and for credits so we'll need to configure some hacks to workaround.

Making LiveSplit Work
------
- Download LiveSplit Server from https://github.com/LiveSplit/LiveSplit.Server/releases and extract the .zip file to the Components folder in your LiveSplit install
- LiveSplit -> Edit Layout... -> (+) -> Control -> LiveSplit Server
- Make sure LiveSplit and LiveSplit Server (Control -> Start Server) are running before you start Antitimer
- The "Init Llanfair" menu option will generate a splits file at `` C:\Users\{username}\antichamber.lss ``. You will need to manually import the splits file into LiveSplit whenever you update the splits for now.

#### Set logging in flush mode
Inside the launch options in Steam add the following command

``-FORCELOGFLUSH``

If you use a custom desktop/menu shortcut, be sure to configure the target as `` "[...]\Antichamber\Binaries\Win32\UDK.exe" -FORCELOGFLUSH ``
Alternatively, you can use the AntiTimer menu -> Launch Antichamber which does the trick as well.

### Add a fake input to trace sounds
This is necessary if you want to track the Credits trigger.
Go to the AntiTimer menu -> Configure -> Allow tracking in The End -> Click Apply

How to run this thing?
------
#### Environment
You need an up-to-date Java environment on your desktop (java8). If you don't, download it at http://java.com/getjava 

Now, just launch the jar file (double-clicking works if you have java installed).

#### Set Antichamber Path
AntiTimer will ask you to find ``Antichamber\Binaries\Win32\UDK.exe`` to be able to locate all the files it needs. This will be stored inside antitimer.cfg for future use.

#### Set a timer
By default, antitimer does not connect to a timer. To select a timer, go to the Timer menu and select the timer you want. Selecting Llanfair will automatically open the timer, while selecting LiveSplit requires a LiveSplit server instance to be running already (see above: Making LiveSplit Work). Your timer selection will be saved.

Configure your run
------
Add a split by clicking on the "+", select the trigger type and the goal. You can delete the split by clicking on the "x" or shift your it with "v" or "^". For each split, whenever AntiTimer detects the trigger it will launch a split command to Llanfair.

Do the same thing with the completion conditions, do remember that those are additive. For example, if you select:

* Sign / Complete
* Map / Complete

The game will stop the timer if and only if you have all signs and map is 100%.

#### Init Llanfair
The menu item Init Llanfair will replace your current run with a run that corresponds to your triggers. It may be useful for practice for instance.

#### File Management
You can load/save your trigger configuration for different runs. However, Llanfair is totally independent and won't react to your load/save actions. Use the Llanfair menu to load/save your runs. Smarter file management may happen in a future release.



Monitors
------
There are 6 monitors built-in AntiTimer which enable you to track the game state.

* Current Gun: displays the color of the gun you currently have.
* Map: displays the map as it can be seen in the hub.
* Map Completion: displays a percentage of map completion.
* Moral Wall: displays the moral wall aka the sign wall. Each box represents a sign with the same order as the moral wall inside the hub.
* Triggers: displays all the triggers that are happening inside antitimer.
* Pink Wall: displays the pink cubes you have. Their order is not important (actually it is displayed using the pink route of the current WR).

All window positions are saved each time they are moved so you will recover the same layout the next time you launch AntiTimer.

#### About extra pink cubes
By default, the pink wall and the pink completion is triggered when you have all the usual pink cubes. However, if you do get the extra pink cubes, they will show up on your pink wall. To set a real 100% pink completion goal, add both of the extra pink cubes in your completion conditions.


About Antitimer
------
Originally coded by Loic Petit (WydD), who you can find at http://twitch.tv/wyddgames. After WydD became inactive in the Antichamber community, I (Mathew Kirschbaum / mkirsch42) picked this up and added a few new features to it. You can find me on Reddit at http://reddit.com/u/mkirsch42, and I'm sometimes on the Antichamber Discord as mkirsch42.

The code has been released on Apache 2 Licence, don't hesitate to contribute and/or steal my code.

Changelog
------

#### 0.2.0
* Add support for LiveSplit and switching timers
* Add support for not using a timer
* Add configuration panel

#### 0.1.2
* Fixing error management during save reading. This problem could (and have) lead to a random reset. (reported by Murklan)

#### 0.1.1
* Fixing Issue#1: Restart level causes reset
* Fixing Issue#2: Log watcher sometimes not synced
* Fixing lag issues. File watchers are now updated every 5ms instead of 1ms and load latency has been augmented to 80ms instead of 10ms.

#### 0.1.0.3
Fixing various stability issues. Considered, first stable release.

#### 0.1
First release

What's to come in future release?
------
* Sync files between LiveSplit, Llanfair, and antitimer

Happy Running!


-- WydD --