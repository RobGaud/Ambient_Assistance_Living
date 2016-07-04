**Sapienza - Università di Roma** <br/>
*Master of Science in Engineering in Computer Science* <br/>
*Pervasive Systems, a.y. 2015-16* <br/>
Group project realized by Andrea Bissoli and Roberto Gaudenzi for Pervasive Systems class from MS in Computer Engineering at Sapienza - Università di Roma.<br/>
[Link to the Pervasive Systems class page](http://ichatz.me/index.php/Site/PervasiveSystems2016).<br/>
You can find us at robgaudenzi@gmail.com and abissoli.ab@gmail.com. 
You can also find us on Linkedin [here](https://www.linkedin.com/in/andrea-bissoli-537768116) and [here](https://www.linkedin.com/in/roberto-gaudenzi-4b0422116).


# WayFinder - Server Side
<img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/WayFinder_logo.png" alt="Drawing"width="200" height="200" align="middle"/>

*WayFinder Server Side* is a Server used by Android app specifically designed to help the store of the infomrmations concern maps supported by our system <br>
In this way, the application has not to download every time the informations when one beacon is detected. But it downloads the data at the start(one time), through request to the server. This is done in the splashscreen activity.<br/>
Whit this scheme we can add, remove and modify the maps dynamically whitout download the app again. <br/>
Since we do not want to download every time the same maps, the database has a version, called dbVersion. When the app sends the request it sends its dbVersion also. So the server checks if the app has the last data or not. In the positive case the server does not answer whit the maps, in negative case the server aswers whit the maps. The format used is JSON, like this:
<br><br><img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/Json.png" alt="Drawing"width="300" height="450" align="middle"/>
<br><br>

This JSON is parsed by app through class DBHelper that it creates a database (all entities and relations) and save the current dbVersion.
The scheme ER is the follow:
<br><br><img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/Er-scheme.png" alt="Drawing"width="550" height="400" align="middle"/>
<br><br>

We have four entities: Map, Node, Edge, dbVersion. The first rapresents the building covered by our system. The Node rapresent the checkpoint (beacon) in the every map. The Edge stores the data about the link between each checkpoints.
 <br><br>The server provides also this functions:

* deleteEdge, deleteMap, deleteNode,
* insertEdge, insertMap, insertNode;


