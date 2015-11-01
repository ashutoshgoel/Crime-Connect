<?php
function call(){
$post_data = array(
    'From' => "9879170829",
    'CallerId' => "01139595486",
    'Url' => "http://my.exotel.in/exoml/start/56825",
    'Priority' => "high", 
    'CallType' => "trans" //Can be "trans" for transactional and "promo" for promotional content
);
 
$exotel_sid = "inout1"; // Your Exotel SID - Get it from here: http://my.exotel.in/settings/site#api-settings
$exotel_token = "10f1bf6abc98873fd22925ddc57886d2985a0481"; // Your exotel token - Get it from here: http://my.exotel.in/settings/site#api-settings
 
$url = "https://".$exotel_sid.":".$exotel_token."@twilix.exotel.in/v1/Accounts/".$exotel_sid."/Calls/connect";

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
 
print "Response = ".print_r($http_result);
}?>