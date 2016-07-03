<?php
	require_once '../config.php';
	
	
	if (!$_REQUEST['From_Major'] || !$_REQUEST['From_Minor'] || 
		!$_REQUEST['To_Major'] || !$_REQUEST['To_Minor'] || 
		!$_REQUEST['Degree'] || !$_REQUEST['Distance']) {
		echo "error param in input";
	}else{
			// Create connection 
			$conn = new mysqli($hostname, $username, $password, $database);
		
			// Check connection
			if ( $conn -> connect_error) {
				die ("Connection failed: " . $conn -> connect_error);
			}
				
			//mandatory fields
			$From_Major         = $_REQUEST['From_Major'];
			$From_Minor     = $_REQUEST['From_Minor'];
			$To_Major         = $_REQUEST['To_Major'];
			$To_Minor     = $_REQUEST['To_Minor'];			
			$Degree = $_REQUEST['Degree'];
			$Distance = $_REQUEST['Distance'];
			
			// First query, insert into edge
			//$conn->query("SET NAMES 'utf8'");
			$sql = "INSERT INTO edge (From_Major,From_Minor,To_Major,To_Minor,Degree,Distance)
					VALUES ('$From_Major', '$From_Minor','$To_Major','$To_Minor','$Degree','$Distance');";
			
				if ( $conn -> query($sql) === TRUE ) {
					// Get last query id. NOTE: It works only with AUTO_INCREMENT attribute.
					//$last_id = $conn ->insert_id;
					echo "done";
					require_once '../updateDbVersion.php';
				} else {
					echo "error query";
				}
		
				$conn -> close();
				
			}
?>