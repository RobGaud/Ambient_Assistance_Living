<?php
require_once '../config.php';

	if (!$_REQUEST['Major'] || !$_REQUEST['Minor'] ) {
		echo "error";
	}else{
	// Create connection 
	$conn = new mysqli($hostname, $username, $password, $database);
	
	// Check connection
	if ( $conn -> connect_error) {
		die ("Connection failed: " . $conn -> connect_error);
	}
			
	//mandatory fields
	$Major         = $_REQUEST['Major'];
	$Minor     = $_REQUEST['Minor'];
	$conn->query("SET NAMES 'utf8'");
	$sql = "DELETE FROM node WHERE Major =".$Major." AND Minor=".$Minor;
	
	if ( $conn -> query($sql) === TRUE ) {
		echo "done";
		require_once '../updateDbVersion.php';
	} else {
		echo "error".$conn -> connect_error;
	}
	$conn -> close();	
	}
?>