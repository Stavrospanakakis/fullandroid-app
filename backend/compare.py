#########################################################################################################################################################

# This file gets the image given to the user from the server and the image that the user makes and compares them pixel by pixel. 
# If the images are similar (up to a sertain percentage) then a function named "shapes" is called and it detects all the shapes from both images

#########################################################################################################################################################


import numpy as np 
import cv2
import PIL.Image
import os
import sys

#initialize paths
image_path = "images/" + sys.argv[1]
user_image_path = "userImages/" + sys.argv[1]

#initialize score. It is -8 due to a bug. It recongises one more rectangle by default
score = -8

#algorithm for shape detection. Transforms shapes into grayscale and detects basic shapes
def shapes(path):
    
    #initialize shapes 
    image_shapes = {'triangle':0, 'rectangle':0, 'circle':0}

    #reads and converts image to greyscale
    img = cv2.imread(path, cv2.IMREAD_GRAYSCALE)    
    _, threshold = cv2.threshold(img, 200, 300, cv2.THRESH_BINARY)  
    contours, _=cv2.findContours(threshold, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    #detects shapes based on the number of lines 
    for cnt in contours:
        approx = cv2.approxPolyDP(cnt, 0.01*cv2.arcLength(cnt, True), True)
        cv2.drawContours(img, [approx], 0, (0), 5)
        x = approx.ravel()[0]
        y = approx.ravel()[0]

        if len(approx) == 3:
            image_shapes['triangle'] += 1

        elif len(approx) == 4:
            image_shapes['rectangle'] += 1

        else:
            image_shapes['circle'] += 1

    return image_shapes

#app's image shapes
image_shapes = shapes(image_path)

#user's image shapes
user_image_shapes = shapes(user_image_path)

print("App's Image:")
print("Triangles:", image_shapes['triangle'])
print("Rectangles:", image_shapes['rectangle'] -1)
print("Circles:", image_shapes['circle'])

print("\n")

print("User's Image")
print("Triangles:", user_image_shapes['triangle'])
print("Rectangles:", user_image_shapes['rectangle'] -1)
print("Circles:", user_image_shapes['circle'])

#score for triangles
if (image_shapes['triangle'] == user_image_shapes['triangle']):
    score += image_shapes['triangle'] * 6
elif (image_shapes['triangle'] < user_image_shapes['triangle']):
    score += image_shapes['triangle'] * 6
else:
    score += (image_shapes['triangle'] - (image_shapes['triangle'] - (user_image_shapes['triangle']))) * 6

#score for rectangles
if (image_shapes['rectangle'] == user_image_shapes['rectangle']):
    score += image_shapes['rectangle'] * 8
elif (image_shapes['rectangle'] < user_image_shapes['rectangle']):
    score += image_shapes['rectangle'] * 8
else:
    score += (image_shapes['rectangle'] - (image_shapes['rectangle'] - (user_image_shapes['rectangle']))) * 8

#score for circles
if (image_shapes['circle'] == user_image_shapes['circle']):
    score += image_shapes['circle'] * 2
elif (image_shapes['circle'] < user_image_shapes['circle']):
    score += image_shapes['circle'] * 2
else:
    score += (image_shapes['circle'] - (image_shapes['circle'] - (user_image_shapes['circle']))) * 2

print("\n")

#user's score
print("User's score", score)

#full score
full_score = image_shapes['triangle'] * 6 + image_shapes['rectangle'] * 8 + image_shapes['circle'] * 2 - 8
print("full score", full_score)

#percentage score
percentage_score = score/full_score*100
print("percentage score", percentage_score)

#image given to the user
i1 = PIL.Image.open(image_path)     

#image made by the user
i2 = PIL.Image.open(user_image_path)    

#assert i1.mode == i2.mode, "Different kinds of images."
#assert i1.size == i2.size, "Different sizes."
 
#code for pixel by pixel comparison 
pairs = zip(i1.getdata(), i2.getdata())
if len(i1.getbands()) == 1:
    # for gray-scale jpegs
    dif = sum(abs(p1-p2) for p1,p2 in pairs)
else:
    dif = sum(abs(c1-c2) for p1,p2 in pairs for c1,c2 in zip(p1,p2))
 
ncomponents = i1.size[0] * i1.size[1] * 3
diff = (dif / 255.0 * 100) / ncomponents

print("\n")

print ("Difference (percentage):", (diff))

if (diff <50):
    print("High percentage of similarity, images match")

else:
	print("Low percentage of similarity, images don't match")

#remove the image created from the user from the server
os.remove(user_image_path)

