<?php
include 'server.php';

$conn = null;
if (isset($_POST['Auth'])) {
    $conn = connect();
    echo " Connected Successfully to Database ";

}
if (isset($_POST['Sync'])) {
    $conn = connect();
    synchronisation($conn);

} else {
    echo "<center>An internal error occurred during the request</center>";

}
if (isset($conn)) {
    disconnect($conn);
}


