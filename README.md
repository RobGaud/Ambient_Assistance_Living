**Sapienza - Università di Roma** <br/>
*Master of Science in Engineering in Computer Science* <br/>
*Pervasive Systems, a.y. 2015-16* <br/>
Group project realized by Andrea Bissoli and Roberto Gaudenzi for Pervasive Systems class from MS in Computer Engineering at Sapienza - Università di Roma.<br/>

# WayFinder
<img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/WayFinder_logo.png" alt="Drawing"width="200" height="200" align="middle"/>

*WayFinder* is an Android app specifically designed to help blind people to move around buildings. It uses beacons to locate the user and the magnetometer inside the phone to know how he is oriented and tell him what is facing.<br/>

The app is composed by four main components:
* The *BeaconSearch* activity implements the monitoring mechanism, i.e., it checks whether the user entered into the building or not by detecting the beacons inside it. Once a beacon is detected, the app switches to the Navigation activity.
* The *Navigation* activity is the main part of the app and implements all the navigation stuff. It offers a minimal user interface composed by a Textbox that keeps track of the current position inside the building, and a big button that is used to communicate with the user. This activity deals with the ranging mechanism, that is used to know which is the nearest beacon to the user, and works with the Compass class to navigate him.
* The *Compass* class uses the magnetometer inside the phone to know what the user is facing by computing the azimuth value (i.e., the clockwise degree with respect to the North).
* Finally, a *background service* was realized to keep executing the monitoring activities also when the app is not in foreground. In this way, the user doesn't need to manually open the app, but he can simply tap on the notification we send to him to open WayFinder and start the navigation.<br/>
Some screenshot of the app are shown below.<br/> <br/>

**Mockups** <br/>

The BeaconSearch activity  |  The Navigation activity
:-------------------------:|:-------------------------:
<img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/BeaconSearch.png" alt="Drawing"width="180" height="350" align="middle"/> <br/><br/>  |  <img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/Navigation_Activity.png" alt="Drawing"width="180" height="350" align="middle"/> <br/><br/>

**A note about accuracy** <br/>
The critical point in this kind of projects is related to the accuracy of users' location within the building. In particular, if their location is not guaranteed to be precisely determined during the navigation, user could lost themselves (remember WayFinder is oriented to blind users), or worse, they could hurt themselves (e.g., because of non-detected walls/stairs). For this reason, we decided to do the following:
* We increased the frequency of the signal for our beacons, in order to reduce as much as possible the delay related to beacon discovery;
* We reduced the range of beacons, in order to have a more accurate idea of users' position. Clearly, this could increase the number of beacons within the building.
Another important issue is related to users' direction during the navigation from one checkpoint to another. In particular, when an user is walking toward a given beacon, we compare his direction with a given range related to the path he's going through. Therefore, the width of this range is critical for the accuracy. <br/>

**Contacts and Links**
* You can find us at robgaudenzi@gmail.com and abissoli.ab@gmail.com. 
* You can also find us on Linkedin [here](https://www.linkedin.com/in/andrea-bissoli-537768116) and [here](https://www.linkedin.com/in/roberto-gaudenzi-4b0422116).
* [Link to the Pervasive Systems class page](http://ichatz.me/index.php/Site/PervasiveSystems2016).<br/>
* You can find a more detailed description of the concept on [Slideshare](http://www.slideshare.net/RobertoGaudenzi1/ambient-assistance-living).<br/>
* A more detailed description of how the app works is available on [Slideshare](http://www.slideshare.net/RobertoGaudenzi1/wayfinder-final-presentation).<br/>
* Finally, you can also find a short video demo on [Youtube](https://youtu.be/kZthXlnu1hE).
