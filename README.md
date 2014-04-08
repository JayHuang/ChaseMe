ChaseMe - Android application for action sports
==========

Android application that allows a drone to chase you while you do action sports.

###Installing the ChaseMe Application

To load the application onto your Android phone from the original source, you will first need to compile the program in order to load it onto your phone. In order to compile it, use the following steps:

1. Download and install <a href="https://www.java.com/en/download/">Java</a> (dependency for Android).  
  a. Click on the red Install button  
  b. Click on the red Agree & Download button  
  c. Run the file after downloading and follow the installation steps

2. Download and install the <a href="http://developer.android.com/sdk/index.html">Android SDK</a> (includes Eclipse with built-in ADT)    
  a.	Click on the blue download button  
  b.	Agree to the terms and select 32-bit or 64-bit depending on your computer type  
  c.	Unzip the download to your preferred location  
  d.	Run Eclipse from the Eclipse subfolder  

3. Turn on USB debugging on your Android phone  
  a. Under System->Developer options  
  b. If Developer options is not visible:  
    * Go to Settings->More->System Manager->About Device  
    * Click on device model number 7 times  
    * Developer options will now be available from the System Manager section  

4. Download the project source code from <a href="https://github.com/JayHuang/ChaseMe">Github</a>  
5. Import project into Android Developer Tools  
  a. In Eclipse: File -> Import Project  
  b. Select Android->Existing Android Code Into Workspace and click on next  
<img src="/screenshots/import_eclipse.png" width="300px" height="280px">
<img src="/screenshots/import_eclipse2.png" width="300px" height="280px">  
**Make sure all the folders in the picture are imported together**  
  c. Click on Browse and navigate to the location of the code downloaded from GitHub  
  d. Connect your computer to your phone via USB and authorize the device when prompted  
  e. Click the “Run” button to compile the app on your phone. (We recommend not compiling on a virtual device)  

###Using the ChaseMe Application
The ChaseMe application has a simple and intuitive interface, providing a connect/disconnect toggle, a ChaseMe button, and connection settings for you to connect to your drone.  

*ChaseMe Settings*  
<img src="/screenshots/ui_settings.png" width="200px" height="350px">
<img src="/screenshots/ui_settings2.png" width="200px" height="350px">
<img src="/screenshots/ui_settings3.png" width="200px" height="350px">  

The settings can be configured for different types of connections. There are four different types of available connections as displayed above: USB, TCP, UDP, Bluetooth. You’ll have to select the connection type that best matches the communicaiton being used between your drone and the ChaseMe application.  

Under connection preferences, make sure to fill out the details for the connection types as necessary. For example, if using a TCP connection, set up the IP address and port being used by the drone.  

*ChaseMe Homescreen*  
<img src="/screenshots/homescreen.png" width="200px" height="350px">  
Once the connection settings have been set up, click on connect in the top right corner. The nearest drone that is available for pairing should connect automatically. Then tap “Chase Me!” to have the drone start following you.  
*ChaseMe disengaged*  
<img src="/screenshots/homescreen.png" width="200px" height="350px">  
Finally, press "Don’t Chase Me" to stop the drone from following you and land.  

###Testing and MAVLink modification:
Once a connection with the drone has been established using the “Connect” button in the ChaseMe application, testing ca begin. In order to test:  

  1. Some code needs to be uncommented in order to test the ChaseMe functionality. You can locate them in:  
    *	Package: org.chaseme.fragments       **FlightActionsFragment.java**: Lines 122-131
    * Package: org.chaseme.service         **MAVLinkClient.java**: Lines 294-296

  2. The actual ChaseMe functionality consists of 3 function calls: takeoff, followMe, and land. These 3 functions fire off MAVLink commands that are sent to the drone. If you wish to modify these commands, the code can be found in:  
    * Package: org.chaseme.drone	    **Drone.java**
      * takeOff function: Lines 103-124		MAVLink Command: Line 111
      *	followMe function: Lines 130-146	MAVLink Command: Line 132
      *	land function: Lines 152-161		MAVLink Command: Line 154

  3.	In order to edit the MAVlink commands:  
    a. A list of the MAVLink commands can be found <a href="https://pixhawk.ethz.ch/mavlink/">here</a>.  
    b. To create your own MAVLink message:  
      * Create and initialize a msg_mission_item object (eg. msg_mission_item item = new msg_mission_item())  
      * Look up the MAVLink command ID needed from the list above (enums of the commands are in MAVLink.Messages.enums in the Mavlink folder)
      * Assign the parameters for the message as necessary
      *	Required: Set commandID (eg.item.command = ….)
      * Send packet (eg. MavcClient.sendMavPacket(item.pack()))

###FAQ
**Q**: What happens if I forget to disengage ChaseMe when I press disconnect?  
**A**: No worries, in addition to having a button to disengage ChaseMe, it automatically disengages when you press the disconnect button!  

**Q**: How far away will the drone be from me?  
**A**: The drone is currently set at 20 feet off the ground, and will hover near your location. There may be options for you to customize the proximity of the drone in future releases.  

**Q**: The project is giving me errors after I import the project?  
**A**: Make sure you include all the folders as seen in the import picture. Also, wait for Eclipse/ADT to finish building the project.  


