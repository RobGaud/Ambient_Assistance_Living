<?php
	require_once '../config.php';
	
	if (!$_REQUEST['From_Major'] || !$_REQUEST['From_Minor'] || 
		!$_REQUEST['To_Major'] || !$_REQUEST['To_Minor']) {
		echo "error";
	}else{
	// Create connection 
	$conn = new mysqli($hostname, $username, $password, $database);
	
	// Check connection
	if ( $conn -> connect_error) {
		die ("Connection failed: " . $conn -> connect_error);
	}
			
	// i campi obbligatori ci sono
	$From_Major         = $_REQUEST['From_Major'];
	$From_Minor     = $_REQUEST['From_Minor'];
	$To_Major         = $_REQUEST['To_Major'];
	$To_Minor     = $_REQUEST['To_Minor'];	
	
	$conn->query("SET NAMES 'utf8'");
	$sql = "DELETE FROM edge WHERE  From_Major =".$From_Major." AND From_Minor=".$From_Minor." AND To_Major=".$To_Major." AND To_Minor=".$To_Minor;
	
	if ( $conn -> query($sql) === TRUE ) {
		echo "ok";
		require_once '../updateDbVersion.php';
	} else {
		echo "error".$conn -> connect_error;
	}
	$conn -> close();
	
	}
?>