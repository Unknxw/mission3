<?php
include 'function.php';

function connect()
{
    $dbhost = "w1.webstrator.fr";
    $dbuser = "bts";
    $dbpass = "XXXXX";
    $db = "bts";


    $conn = new PDO("mysql:host=$dbhost;dbname=$db", $dbuser, $dbpass);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $credentials = json_decode($_POST['Auth']);
    $username = (string)$credentials[0];
    $password = (string)$credentials[1];
    authentification($conn, $username, $password);

    return $conn;
}

/**
 * @param $conn
 */
function synchronisation($conn)
{
    echo " Fonction de synchro en cours ";

    Sync($conn);
}

/**
 * @param $conn
 */
function disconnect($conn)
{
    $conn->close();
}

