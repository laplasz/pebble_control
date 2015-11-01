<html>
<body>
<p>hali</p>
<?php
//------------------------------
// Payload data you want to send 
// to Android device (will be
// accessible via intent extras)
//------------------------------
$control = '-1';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
if (empty($_POST["control"])) {
     echo "control required";
   } else {
     $control = $_POST["control"];
      
   }
$data = array( "message" => $control );

//------------------------------
// The recipient registration IDs
// that will receive the push
// (Should be stored in your DB)
// 
// Read about it here:
// http://developer.android.com/google/gcm/
//------------------------------

$ids = array( 'APA91bEnqaHJARYKiXfDiijHXZRIxZImTro8LtKUa55YCKnN9y-1WXLlFt6mwBk8YsaHHJxdkBHdEkgpgGIyEZ1HRbxyD73H2Cf611gRi8c6raZjnr-4dgGePKUpU2D7diaq0q1NCs6C');

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
    //------------------------------
    // Set post data as JSON
    //------------------------------

    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $post ) );

    //------------------------------
    // Actually send the push!
    //------------------------------

    echo "Sending:";
    $result = curl_exec( $ch );
    echo $result;
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
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<input type="hidden" name="control" value="1">
<input type="submit" name="submit" value="Up">
</form>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<input type="hidden" name="control" value="2">
<input type="submit" name="submit" value="Select">
</form>
</body>
</html>

