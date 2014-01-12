<?php
header('Content-type: text/json');
$rand = rand(0, 100);
$array = array(
	array(
		y => $rand
	),
	array(
		y => $rand * 2
	)
);
echo json_encode($array);
?>
