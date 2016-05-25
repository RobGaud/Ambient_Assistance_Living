<?php
/**
 * Database config variables
 */

$hostname = "localhost";
$username = "beaconcontrolflow";
$password = "pobvimabsu49";
$database = "my_beaconcontrolflow";


if (!$_REQUEST['distance'] || !$_REQUEST['mobile']) {
	echo "error";
}else{
		// Create connection 
		$conn = new mysqli($hostname, $username, $password, $database);
	
		// Check connection
		if ( $conn -> connect_error) {
			die ("Connection failed: " . $conn -> connect_error);
		}
			
		//i campi obbligatori ci sono
        $distance         = $_REQUEST['distance'];
		$mobile     = $_REQUEST['mobile'];
		
		// First query, insert into PLANT
        //$conn->query("SET NAMES 'utf8'");
		$sql = "INSERT INTO CONTROLFLOW (distance, mobilemodel)
				VALUES ('$distance', '$mobile');";
		
			if ( $conn -> query($sql) === TRUE ) {
				// Get last query id. NOTE: It works only with AUTO_INCREMENT attribute.
				$last_id = $conn ->insert_id;
				echo $last_id
			} else {
				echo "error query";
			}
	
			$conn -> close();
		}
?>