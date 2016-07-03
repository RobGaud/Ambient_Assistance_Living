<?php
	require_once 'config.php';
	
	// Create connection 
	$conn = new mysqli($hostname, $username, $password, $database);
	
	// Check connection
	if ( $conn -> connect_error) {
		die ("Connection failed: " . $conn -> connect_error);
	}
	
	// Array for JSON
	$response = array();
	
	// queries
	
	$sql_maps= "SELECT * FROM map";
    $conn->query("SET NAMES 'utf8'");
	
	//take all major,name in map
	$result_map = $conn -> query ($sql_maps) or die ($conn -> error);
	if( $result_map -> num_rows > 0){
		$response["maps"] = array();
		$map = array();
		
		while ($row_map = mysqli_fetch_array($result_map)){
			$sql_node= "SELECT * FROM node WHERE major='";
			$sql_edge= "SELECT * FROM edge WHERE From_Major='";
			
			//add name
			$map_name        = $row_map['Name'] ;
			$map["mapName"]=$map_name;
		
			//for each row we do the query on nodes and edges
			$Major = $row_map['Major'];			
			
			//for NODEs
			$sql_node= $sql_node.$Major."'";	
			//echo $Major."<BR>";
			//echo $sql_node."<BR>";
			$map['nodes'] = array();
			$result_node = $conn -> query ($sql_node) or die ($conn -> error);
			if( $result_node -> num_rows > 0){
				while ($row_node = mysqli_fetch_array($result_node)){
					$nodes = array();			
					$nodes["Major"]         = $row_node["Major"];
					$nodes["Minor"]         = $row_node["Minor"];
					$nodes["Audio"]         = $row_node["Audio"];
                    $nodes["Category"]         = $row_node["Type"];
					$nodes["Steps"]         = $row_node["Steps"];
					array_push($map['nodes'],$nodes);
				}
			}			
			//for EDGEs
			$sql_edge = $sql_edge.$Major."'";
			//echo $sql_edge."<BR>";
			
			$map['edges'] = array();
			$result_edge = $conn -> query ($sql_edge) or die ($conn -> error);
			if( $result_edge->num_rows > 0) {
				while ($row_edge = mysqli_fetch_array($result_edge)){
					$edges = array();
					$edges["From_Major"]         = $row_edge["From_Major"];
					$edges["From_Minor"]         = $row_edge["From_Minor"];
					$edges["To_Major"]         = $row_edge["To_Major"];
					$edges["To_Minor"]         = $row_edge["To_Minor"];
					$edges["Degree"]         = $row_edge["Degree"];
					$edges["Distance"]         = $row_edge["Distance"];
					array_push($map["edges"],$edges);
				}
			}		
			array_push($response['maps'],$map);
		}			
		
		// success
		$response["success"] = 1;
		$response["message"] = "Maps returned successfully.";
			
		// get version db
		$sql = "SELECT * FROM dbversion"; 
		$result = $conn -> query($sql) or die ($conn -> error);
		if( $result -> num_rows > 0){
			while ($row = mysqli_fetch_array($result) ) {
				$versione = $row["Version"];
			}
			$response["dbVersion"] = $versione;
		}else{
			$response["dbVersion"] = "error";
		}
		// echoing JSON response
		echo json_encode($response);	
	} else {
		$response["success"] = 0;
		$response["message"] = "No maps found";		
		// echoing JSON response
		echo json_encode($response);
	}
    $conn -> close();
?>
	