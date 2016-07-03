<?php
	require_once '../config.php';
	//echo "updateDbVersion";
	
	// query for retrieving the DB version
	$sql = "SELECT Version FROM dbversion"; 
    $result = $conn -> query($sql) or die ($conn -> error);
    while ($row = mysqli_fetch_array($result) ) {
       	$VersionDb = $row["Version"];
    }
    $newVersionDb = $VersionDb + 1;
    $sql = "UPDATE dbversion SET Version=$newVersionDb WHERE Version=$VersionDb";
        $conn -> query($sql) or die ($conn -> error);

	
?>
	
	