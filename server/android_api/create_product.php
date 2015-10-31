<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['type']) && isset($_POST['street']) && isset($_POST['imei'])  && isset($_POST['detail'])) {
    
    $type = $_POST['type'];
    $street = $_POST['street'];
    $detail = $_POST['detail'];
    $imei = $_POST['imei'];
   // $phone = $_POST['phone'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();
    
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO products(type, street, detail,imei) VALUES('$type', '$street', '$detail','$imei' )");
    $row = mysql_insert_id();
    // check if row inserted or not
    if ($result) {
    
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = $row;

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>