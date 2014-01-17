<?php
header('Content-type: text/json');
$rand = rand(0, 100);
$array = array(
	Shooter => array(
		array(
			y => $rand
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
			y => $rand
		)
	),
	Drivebase => array(
		array(
			y => $rand
		),
		array(
			y => $rand * 2
		),
		array(
			y => $rand * 1.5
		),
		array(
			y => $rand / 3
		),
		array(
			y => $rand / 2
		),
		array(
			y => $rand / 1.5
		)
	),
	Arm => array(
		array(
			y => $rand
		),
		array(
			y => 10
		)
	)
);
echo json_encode($array);
?>
