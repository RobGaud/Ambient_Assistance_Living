<?php
	require_once '../config.php';
	
		
	if (!$_REQUEST['Major'] || !$_REQUEST['Minor'] || !$_REQUEST['Audio'] || !$_REQUEST['Type']) {
		echo "error param input<BR>";
        echo $_REQUEST['Major']."<BR>".$_REQUEST['Minor']."<BR>".$_REQUEST['Audio']."<BR>".$_REQUEST['Type']."<BR>".$_REQUEST['Steps'];
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
		$Audio = $_REQUEST['Audio'];
		$Type = $_REQUEST['Type'];
		$Steps =$_REQUEST['Steps'];
			
		// First query, insert into node
		//$conn->query("SET NAMES 'utf8'");
		$sql = "INSERT INTO node (Major,Minor,Audio,Type, Steps)
				VALUES ('$Major', '$Minor','$Audio','$Type','$Steps');";
			
		if ( $conn -> query($sql) === TRUE ) {
			echo "done";
			require_once '../updateDbVersion.php';
		} else {
			echo "error query";
		}
		$conn -> close();
		
	}
?>