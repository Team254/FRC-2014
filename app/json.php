<?php
header('Content-type: text/json');
$rand = rand(0, 100);
$array = array(
	Shooter => array(
		array(
			y => 5
		),
		array(
			y => 10
		)
	),
	Intake => array(
		array(
			y => 5
		),
		array(
			y => 10
		)
	),
	Drivebase => array(
		array(
			y => $rand
		),
		array(
			y => 10
		)
	),
	Arm => array(
		array(
			y => 5
		),
		array(
			y => 10
		)
	)
);
echo json_encode($array);
?>
