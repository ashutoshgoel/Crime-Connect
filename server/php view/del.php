<?php
include "config/connect.php";

mysql_query("DELETE FROM productions WHERE pid = '".$_GET['id']."'");
echo "<script language=javascript>parent.location.href='rekap.php';</script>";
?>