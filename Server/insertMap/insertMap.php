<?php
	require_once '../config.php';
	
		
	if (!$_REQUEST['Major'] || !$_REQUEST['Name']) {
		echo "error";
	}else{
			// Create connection 
			$conn = new mysqli($hostname, $username, $password, $database);
		
			// Check connection
			if ( $conn -> connect_error) {
				die ("Connection failed: " . $conn -> connect_error);
			}
				
			//i campi obbligatori ci sono
			$Major         = $_REQUEST['Major'];
			$Name     = $_REQUEST['Name'];
		
			
			// First query, insert into PLANT
			//$conn->query("SET NAMES 'utf8'");
			$sql = "INSERT INTO map (Major,Name)
					VALUES ('$Major', '$Name');";
			
				if ( $conn -> query($sql) === TRUE ) {
					// Get last query id. NOTE: It works only with AUTO_INCREMENT attribute.
					$last_id = $conn ->insert_id;
					echo $last_id;
					require_once '../updateDbVersion.php';
				} else {
					echo "error query";
				}
		
				$conn -> close();
				
			}
?>