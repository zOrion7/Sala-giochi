<?php

header('Content-type: text/plain');
include "connessione.php";

$gioco = $_POST["nomeGioco"];
if(isset($gioco)){

$classificaGlobale = mysqli_query($link, "SELECT Nickname,$gioco FROM Scoreboard ORDER BY $gioco DESC ");

  		while ($row = mysqli_fetch_array($classificaGlobale)) {
            echo $row['Nickname'],"-",$row[$gioco],"-";     
    	}
  	}
    
?>
