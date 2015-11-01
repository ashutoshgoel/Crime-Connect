 
<?php  

// if (isset($_GET['SmsSid']) && isset($_GET['From']) && isset($_GET['To']) && isset($_GET['Body']) && isset($_GET['Date'])) {
 
    $smsid = $_GET['SmsSid'];
    $from = $_GET['From'];
    $to = $_GET['To'];
    $body = $_GET['Body'];
    $date = $_GET['Date'];
  

$myArray = explode(',', $body);
$type = $myArray[0];
$detail = $myArray[1];
$lat = $myArray[2];
$lon = $myArray[3];
$imei = $myArray[4];
 //echo $smsid,$from,$to,$body; 
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "inout";
//python caliing
$data = array($lat,$lon);
$result = shell_exec('python /var/www/html/hack/AndroidConnectingToPhpMySQL/exotel/location.py ' . escapeshellarg(json_encode($data)));
// Decode the result
$resultData = json_decode($result, true);

// This will contain: array('status' => 'Yes!')
var_dump($resultData);
$address = $resultData['address'];
//echo $address;
//Create connection
$con = mysqli_connect($servername, $username, $password, $dbname);
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
// Check connection
$a=mysqli_query($con,"INSERT INTO products (type,street,imei,detail)
VALUES ('$type','$address','$imei','$detail')");
$row = mysqli_insert_id($con);
mysqli_close($con);
echo $row;

// header('content-type:text/plain');
/*
$msg = "Complain id : ".$row." emergency @".$address." Phone number : ".$from;
$post_data = array(
    // 'From' doesn't matter; For transactional, this will be replaced with your SenderId;
    // For promotional, this will be ignored by the SMS gateway
    'From'   => '8808891988',
    'To'    => '9879170829',
    'Body'  => $msg//Message body(PCR)
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
 
require_once __DIR__ . '/police.php';
call();*/


//}
?>