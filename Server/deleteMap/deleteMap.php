<?php
	require_once '../config.php';
	
		
	if (!$_REQUEST['Major'] ) {
		echo "error";
	}else{// Create connection 
	$conn = new mysqli($hostname, $username, $password, $database);
	
	// Check connection
	if ( $conn -> connect_error) {
		die ("Connection failed: " . $conn -> connect_error);
	}
			
	//mandatory fields
	$Major         = $_REQUEST['Major'];

	
	$conn->query("SET NAMES 'utf8'");
	$sql = "DELETE FROM map WHERE  Major =".$Major;
	
	if ( $conn -> query($sql) === TRUE ) {
		echo "ok";
		require_once '../updateDbVersion.php';
	} else {
		echo "error".$conn -> connect_error;
	}
	$conn -> close();
	
	}
?>