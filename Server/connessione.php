<?php

$user   = "root";
$password = "";
$database = "my_sms1920salagiochi";
$host   = "localhost";
$link = mysqli_connect($host,$user,$password);
mysqli_select_db($link,$database) or die( "Errore di connessione.");


?>
