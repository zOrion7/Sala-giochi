<?php

header('Content-type: text/plain');
include "connessione.php";


$classificaGlobale = mysqli_query($link, "SELECT * FROM Scoreboard ORDER BY Globale DESC ");
   
 
   while ($row = mysqli_fetch_array($classificaGlobale)) {
  		 
    echo  $row['Nickname'],"-",$row['FlappyPlanet'],"-",$row['MeteorDodge'],"-",$row['SpaceShooter'],"-",$row['Breakout'],"-", $row['Globale'],"-";  
}    
?>