<?php
header('Content-type: text/json');
$rand = rand(0, 100);
echo json_encode($rand);
?>
