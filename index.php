<!DOCTYPE html>
<html>
<head>
<title>DRINKMASTER</title>
</head>
<body>
<h1>Hello</h1>
<?php 
echo "Current date: " . date("Y/m/d") . "<br>";
echo "Working directory: " . getcwd() . "<br>";
?>
<img src="https://placekitten.com/200/300" alt="x">

<form action="index.php?start=yes" method="GET">
<input type="submit" value="start">
</form>
<?php
if ($_GET["start"]){
	system("python3 /home/IT1G/drinkMaster/grbl_test.py")
}
?>
</body>
</html>
