<html>
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Phone Control</title>
</head>
<body>
<p>Control your phone..</p>

<form name="send_gcm" method="get" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
gcm_id: <input type"text" name="gcm_id"/><br/>
<input type="button" name="up" value="up" onclick="DoSubmit(1)"/>
<input type="button" name="down" value="down" onclick="DoSubmit(2)"/>
<input type="button" name="select" value="select" onclick="DoSubmit(3)"/>
<input type="button" name="back" value="back" onclick="DoSubmit(4)"/>
<input type="hidden" name="control">
</form>
<script>
function DoSubmit(control) {

   var form = document.forms['send_gcm'];
   form.control.value = control;
   form.submit();
}
</script>
</html>
<?php
//------------------------------
// Payload data you want to send 
// to Android device (will be
// accessible via intent extras)
//------------------------------
$control = '-1';

if ($_SERVER["REQUEST_METHOD"] == "GET") {
if (empty($_GET["control"])) {
     echo "control required";
   } else {
     $control = $_GET["control"];
      
   }
$gcm_id = $_GET["gcm_id"];


$data = array( "message" => $control );

//------------------------------
// The recipient registration IDs
// that will receive the push
// (Should be stored in your DB)
// 
// Read about it here:
// http://developer.android.com/google/gcm/
//------------------------------

$ids = array( 'APA91bEAlnWM45lx-xZ1MTFctwL9hiwg-68kJMbp73yUXsNSE86-oqqN3tAWvHFsqURrTRLcchFekMpgWNDir7GuGKGKriorv4vZTbEKCGHwd1MJe-aWb2g5oDIDpaVJhDBZhilkEuN0');

if ($gcm_id != "") {
$ids = array( $gcm_id );
}

//------------------------------
// Call our custom GCM function
//------------------------------

sendGoogleCloudMessage(  $data, $ids );
}
//------------------------------
// Define custom GCM function
//------------------------------

function sendGoogleCloudMessage( $data, $ids )
{
    //------------------------------
    // Replace with real GCM API 
    // key from Google APIs Console
    // 
    // https://code.google.com/apis/console/
    //------------------------------

    $apiKey = 'AIzaSyCF-Ummr6O7nW0KP10znmXjQVC94d7nH6s';

    //------------------------------
    // Define URL to GCM endpoint
    //------------------------------

    $url = 'https://android.googleapis.com/gcm/send';

    //------------------------------
    // Set GCM post variables
    // (Device IDs and push payload)
    //------------------------------

    $post = array(
                    'registration_ids'  => $ids,
                    'data'              => $data,
                    );

    //------------------------------
    // Set CURL request headers
    // (Authentication and type)
    //------------------------------

    $headers = array( 
                        'Authorization: key=' . $apiKey,
                        'Content-Type: application/json'
                    );

    //------------------------------
    // Initialize curl handle
    //------------------------------

    $ch = curl_init();

    //------------------------------
    // Set URL to GCM endpoint
    //------------------------------

    curl_setopt( $ch, CURLOPT_URL, $url );

    //------------------------------
    // Set request method to POST
    //------------------------------

    curl_setopt( $ch, CURLOPT_POST, true );

    //------------------------------
    // Set our custom headers
    //------------------------------

    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers );

    //------------------------------
    // Get the response back as 
    // string instead of printing it
    //------------------------------

    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    // Disabling SSL Certificate support temporarly
    curl_setopt( $ch, CURLOPT_SSL_VERIFYHOST, 0 );
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_IPRESOLVE, CURL_IPRESOLVE_V4 );
    //------------------------------
    // Set post data as JSON
    //------------------------------

    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $post ) );

    //------------------------------
    // Actually send the push!
    //------------------------------
   
    $result = curl_exec( $ch );
    
    //------------------------------
    // Error? Display it!
    //------------------------------

    if ( curl_errno( $ch ) )
    {
        echo 'GCM error: ' . curl_error( $ch );
    }

    //------------------------------
    // Close curl handle
    //------------------------------

    curl_close( $ch );

    //------------------------------
    // Debug GCM response
    //------------------------------

    echo $result;
}
?>

