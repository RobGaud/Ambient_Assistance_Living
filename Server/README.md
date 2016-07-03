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
Since we have not want to download every time the same maps, the database has a version, called dbVersion. When the app sends the reqeust it sends its dbVersion also. So the server checks if the app has the last data or not. In the positive case the server does not answer whit the maps, in negative case the server aswers whit the maps. The format used is JSON, like this:<br/>
"{<br/>
	\ "maps": [{<br/>
		"mapName": "DIAG",<br/>
		.."nodes": [{<br/>
			..."Major": "62887",<br/>
			"Minor": "4125",<br/>
			"Audio": "Exit",<br/>
			"Category": "OUTDOOR",<br/>
			"Steps": "0"<br/>
		}....],<br/>
		"edges": [{<br/>
			"From_Major": "62887",<br/>
			"From_Minor": "4558",<br/>
			"To_Major": "62887",<br/>
			"To_Minor": "53723",<br/>
			"Degree": "160",<br/>
			"Distance": "5"<br/>
		}.....]<br/>
	}],<br/>
	"success": 1,<br/>
	"message": "Maps returned successfully.",<br/>
	"dbVersion": "2"<br/>
}".<br/><br/>

This JSON is parsed by app through class DBHelper that it creates a database (all entities and relations) and save the current dbVersion.
The scheme ER is the follow:
<br><img src="https://github.com/RobGaud/Ambient_Assistance_Living/blob/master/images/Er.png" alt="Drawing"width="200" height="200" align="middle"/>
<br>
The server provides also this functions:

* deleteEdge, deleteMap, deleteNode,
* insertEdge, insertMap, insertNode;


