<?php echo'{
	"success" : "1",
	"message" : "Maps returned successfully.",
	"maps" : 
	[
		{
			"mapName": "DIAG",
			"nodes": [{
				"audio": "the Hall",
				"major": "9406",
				"minor": "48775",
				"category": "ROOM",
				"steps": "0"
			}, {
				"audio": "Stairs A",
				"major": "62887",
				"minor": "44680",
				"category": "ROOM",
				"steps": "14"
			}, {
				"audio": "Stairs B",
				"major": "61272",
				"minor": "53723",
				"category": "ROOM",
				"steps": "14"
			}, {
				"audio": "Wing B",
				"major": "25261",
				"minor": "6695",
				"category": "ROOM",
				"steps": "0"
			}, {
				"audio": "the Garden gazebos",
				"major": "null",
				"minor": "null",
				"category": "ROOM",
				"steps": "0"
			}, {
				"audio": "Exit",
				"major": "null",
				"minor": "null",
				"category": "ROOM",
				"steps": "0"
			}],
			"edges": [{
				"nodeFrom": "9406 48775",
				"nodeTo": "62887 44680",
				"direction": "160",
				"distance": "5"
			}, {
				"nodeFrom": "9406 48775",
				"nodeTo": "61272 53723",
				"direction": "340",
				"distance": "5"
			}, {
				"nodeFrom": "9406 48775",
				"nodeTo": "null null",
				"direction": "250",
				"distance": "5"
			}, {
				"nodeFrom": "9406 48775",
				"nodeTo": "null null",
				"direction": "70",
				"distance": "5"
			}, {
				"nodeFrom": "62887 44680",
				"nodeTo": "9406 48775",
				"direction": "340",
				"distance": "5"
			}, {
				"nodeFrom": "61272 53723",
				"nodeTo": "9406 48775",
				"direction": "160",
				"distance": "5"
			}, {
				"nodeFrom": "61272 53723",
				"nodeTo": "25261 6695",
				"direction": "340",
				"distance": "5"
			}, {
				"nodeFrom": "25261 6695",
				"nodeTo": "61272 53723",
				"direction": "160",
				"distance": "5"
			}]
		},
		{
			"mapName": "Casa di Rob",
			"nodes": [
			{
				"audio": "Stanza di Rob",
				"major": "666",
				"minor": "666",
				"category": "ROOM",
				"steps": "0"
			}, {
				"audio": "Scrivania di Rob",
				"major": "666",
				"minor": "667",
				"category": "ROOM",
				"steps": "0"
			}],
			"edges": [
				{
					"From_Major": "666",
					"From_Minor": "666",
					"To_Major": "666",
					"To_Minor": "667",
					"direction": "0",
					"distance": "5"
				}
			]  
		}
	]
}'
?>
