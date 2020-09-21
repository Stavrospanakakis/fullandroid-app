<?php
/************************************************************
This code gets the image from the android app and saves it to
/userImages.
*************************************************************/

//get image from app and save it to /userImages
$image_name = $_POST["IMAGE_NAME"];
$image_value = $_POST["IMAGE_VALUE"];

$path = "userImages/$image_name.png";

$url = "http://192.168.2.9:8000/backend/$path";

file_put_contents($path,base64_decode($image_value));
?>
