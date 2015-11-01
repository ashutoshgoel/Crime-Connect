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
    require_once __DIR__ . '/police.php';
    // connecting to db
    $db = new DB_CONNECT();
    
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO products(type, street, detail,imei) VALUES('$type', '$street', '$detail','$imei' )");
    $row = mysql_insert_id();
    // check if row inserted or not
    if ($result) {
    
        // successfully inserted into database
        $response [""]= $row;
        // echoing JSON response
        echo json_encode($response);
        
        $msg = "Complain id : ".$row."emergency @".$street;
$post_data = array(
    // 'From' doesn't matter; For transactional, this will be replaced with your SenderId;
    // For promotional, this will be ignored by the SMS gateway
    'From'   => '8808891988',
    'To'    => '9879170829',
    'Body'  => $msg //Message body(PCR)
);
 
$exotel_sid = "inout1"; // Your Exotel SID - Get it from here: http://my.exotel.in/Exotel/settings/site#api-settings
$exotel_token = "10f1bf6abc98873fd22925ddc57886d2985a0481"; // Your exotel token - Get it from here: http://my.exotel.in/Exotel/settings/site#api-settings
 
$url = "https://".$exotel_sid.":".$exotel_token."@twilix.exotel.in/v1/Accounts/".$exotel_sid."/Sms/send";
 
$ch = curl_init();
curl_setopt($ch, CURLOPT_VERBOSE, 1);
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_FAILONERROR, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($post_data));
 
$http_result = curl_exec($ch);
$error = curl_error($ch);
$http_code = curl_getinfo($ch ,CURLINFO_HTTP_CODE);
 
curl_close($ch);
 
call();      
        
        

        
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