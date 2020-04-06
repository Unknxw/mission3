<?php

/**
 * @param $conn
 * @param $username
 * @param $password
 */
function authentification($conn, $username, $password)
{

    $sql = "SELECT * FROM user WHERE name = '$username' AND password = '$password'";
    $stmt = $conn->prepare($sql);
    $stmt->execute();
    $nb_result = $stmt->rowCount();
    if ($nb_result > 0) {
        $req = $stmt->fetchAll();
        $userID = $req[0]['id'];
        $result = "Authentification réussie%" . (string)$userID . "%";
    } else {
        $result = 'Erreur d\'authentification';
    }
    echo $result;


}

/**
 * @param $conn
 */
function Sync($conn)
{
    date_default_timezone_set('UTC');
    $currentDate = date("Y") . date("m");
    $datas = json_decode($_POST["Sync"]);
    $user = $datas[0]->UserID;
    $date = $datas[0]->date;
    $km = $datas[0]->KM;
    $rep = $datas[0]->REP;
    $etp = $datas[0]->ETP;
    $nuit = $datas[0]->NUI;
    addFrais($conn, $currentDate, $user, $date, $km, $rep, $etp, $nuit);
    $hfRestant = true;
    $index = 0;
    do {
        $HFindex = "HF" . $index;
        if (!empty($datas[0]->$HFindex)) {
            $hf = $datas[0]->$HFindex;
            $hfMontant = $hf->Montant;
            $hfMotif = $hf->Motif;
            $hfJour = $hf->Jour;
            addFraisHorsForfait($conn, $user, $date, $currentDate, $hfMotif, $hfMontant, $hfJour);
            $index++;
        } else {
            $hfRestant = false;
        }

    } while ($hfRestant);

}

/**
 * @param $conn
 * @param $user
 * @param $date
 * @param $currentDate
 * @param $hfMotif
 * @param $hfMontant
 * @param $hfJour
 */
function addFraisHorsForfait($conn, $user, $date, $currentDate, $hfMotif, $hfMontant, $hfJour)
{
    if (!(strval($currentDate) === strval($date))) {
        try {
            $req = $conn->prepare("INSERT INTO lignefraishorsforfait(idvisiteur,mois,libelle,montant) VALUES (:idvisiteur,:mois,:libelle,:montant)");
            $req->bindParam(':idvisiteur', $user, PDO::PARAM_STR, 255);
            $req->bindParam(':mois', $date, PDO::PARAM_STR, 255);
            $req->bindParam(':libelle', $hfMotif, PDO::PARAM_STR, 255);
            $req->bindParam(':montant', $hfMontant, PDO::PARAM_INT);

            $req->execute();
        } catch (PDOException $e) {
            echo "Probleme d'insertion des frais hors forfait : " . $e;
            echo "%error%";
        }

    }

}

/**
 * @param $conn
 * @param $currentDate
 * @param $user
 * @param $date
 * @param $km
 * @param $rep
 * @param $etp
 * @param $nuit
 */
function addFrais($conn, $currentDate, $user, $date, $km, $rep, $etp, $nuit)
{
    $assoc = array
    (
        array("KM", $km),
        array("ETP", $etp),
        array("NUI", $nuit),
        array("REP", $rep)
    );
    if (!(strval($currentDate) === strval($date))) {
        for ($i = 0; $i < 4; $i++) {
            $index = $i;
            $quantity = $assoc[$index][1];
            $idfraisforfait = $assoc[$index][0];

            try {
                $req = $conn->prepare("INSERT INTO lignefraisforfait(idvisiteur,mois,idfraisforfait,quantite) VALUES (:idvisiteur,:mois,:idfraisforfait,:quantite)");
                $req->bindParam(':idvisiteur', $user, PDO::PARAM_STR, 255);
                $req->bindParam(':mois', $date, PDO::PARAM_STR, 255);
                $req->bindParam(':idfraisforfait', $idfraisforfait, PDO::PARAM_STR, 255);
                $req->bindParam(':quantite', $quantity, PDO::PARAM_INT);

                $req->execute();
            } catch (PDOException $e) {
                echo "Un problème d'insertion dans la base de donnée est survenue : Duplication -> " . $e;
                echo "%error%";
            }
        }
    }
}