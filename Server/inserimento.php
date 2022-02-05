<?php

include "connessione.php";


$nickname = $_POST["nickname"];
$gioco = $_POST["gioco"];
$score = $_POST["score"];

//@@@@@@@@@@@@@@@@@@ CODICE PER INSERIMENTO @@@@@@@@@@@@@@@@@@@
if(isset($nickname) && isset($gioco) && isset($score)){

print "==== POST DATA ==== 
nickname = $nickname
gioco = $gioco
score = $score
";




//controllo se c'è l'utente
$check = mysqli_query($link, "SELECT * FROM Scoreboard WHERE Nickname = '$nickname' ");

//se l'utente non c'è, faccio una insert.
$righeCheck = mysqli_num_rows($check);

if($righeCheck==0){
	print("n righe= ".$righeCheck);
    
	$insert = mysqli_query($link, "INSERT INTO Scoreboard (Nickname,$gioco) VALUES ('$nickname', '$score')" );
    if(!$insert)print("\n". mysqli_error($link));
    			
}

/*
 se c'è l'utente devo solo fare l'update dello score (anche se non ci ha mai giocato)
 SOLO nel caso in cui il nuovo punteggio è migliore.
*/
else{

	$riga = mysqli_fetch_assoc($check);
    print("score attuale = ".$riga[$gioco]);
	$scoreOld = $riga[$gioco];
    
	if($scoreOld < $score){
	$updateScore = mysqli_query($link, "UPDATE Scoreboard SET $gioco = $score WHERE Nickname = '$nickname' ");
    
     if(!$updateScore)print("\n". mysqli_error($link));
    			
	}
    
}
   
//aggiorno punteggio Globale con la somma dei punteggi massimi per ogni utente.
$calcolaGlobale = mysqli_query($link, "SELECT Nickname, (MAX(FlappyPlanet) + Max(MeteorDodge) + Max(SpaceShooter) + Max(Breakout)) as totale
 FROM Scoreboard
 WHERE Nickname ='$nickname'"  );        
 
$riga = mysqli_fetch_assoc($calcolaGlobale);
$tot = $riga["totale"];
 
 $globalUpdate = mysqli_query($link, "UPDATE Scoreboard SET Globale = $tot 
										WHERE Nickname = '$nickname' ");
 if(!$globalUpdate)print("\n". mysqli_error($link));
    		


}


?>