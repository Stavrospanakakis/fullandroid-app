<?php
/************************************************************
This code finds the name of the uploaded image, passes it as 
a parameter to the python script and then runs the python 
script. Finally it echoes the output of the script, so the 
android app could get it and show it on screen.
*************************************************************/

/*Find the name of the uploaded image. Repeat while the image 
name is empty. I used the loop because python script could run
faster than the variable set and display no output*/
$image_file = shell_exec("ls userImages");
while ($image_file == ""){
    $image_file = shell_exec("ls userImages");
}
//run the python script for comparing images and save it to output variable
$output = shell_exec("python ./compare.py ${image_file} ");
echo $output;

?>
