#########################################################################################################################################################

# This file gets the image given to the user from the server and the image that the user makes and compares them pixel by pixel. 
# If the images are similar (up to a sertain percentage) then a function named "shapes" is called and it detects all the shapes from both images

#########################################################################################################################################################


import numpy as np 
import cv2
import PIL.Image
import os
import sys
import math 

#initialize paths
image_path = "images/" + sys.argv[1]
user_image_path = "userImages/" + sys.argv[1]

#algorithm for shape detection. Transforms shapes into grayscale and detects basic shapes
def shapes(path):
    
    #initialize shapes 
    image_shapes = {'triangle':0, 'rectangle':0, 'circle':0}

    #reads and converts image to greyscale
    img = cv2.imread(path)

    imgGrey = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    _, thrash = cv2.threshold(imgGrey, 240, 255, cv2.THRESH_BINARY)
    contours, _=cv2.findContours(thrash, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    #detects shapes based on the number of lines 
    for contour in contours:
        approx = cv2.approxPolyDP(contour, 0.01* cv2.arcLength(contour, True), True)
        cv2.drawContours(thrash, [approx], 0, (0, 0, 0), 5)

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

# print("App's Image:")
# print("Triangles:", math.floor(image_shapes['triangle'] /2) )
# print("Rectangles:", math.floor((image_shapes['rectangle'] -1) / 2))
# print("Circles:", math.floor(image_shapes['circle'] / 2 ))

# print("\n")

# print("User's Image")
# print("Triangles:", math.floor(user_image_shapes['triangle'] / 2))
# print("Rectangles:", math.floor((user_image_shapes['rectangle'] -1) / 2))
# print("Circles:", math.floor(user_image_shapes['circle'] /2) )

app_triangles = math.floor(image_shapes['triangle'] /2 )
app_rectangles = math.floor((image_shapes['rectangle'] -1) / 2)
app_circles = math.floor(image_shapes['circle'] / 2 )

user_triangles = math.floor(user_image_shapes['triangle'] / 2)
user_rectangles = math.floor((user_image_shapes['rectangle'] -1) / 2)
user_circles = math.floor(user_image_shapes['circle'] /2) 

def score(app_shapes, user_shapes):
    shape_score = 0 
    if (app_shapes == user_shapes):
        shape_score += app_shapes * 2
    else:
        shape_score += (app_shapes - (app_shapes - user_shapes)) * 2
    return shape_score

users_score = score(app_triangles, user_triangles) + score(app_rectangles, user_rectangles) + score(app_circles, user_circles)
max_score = app_triangles * 2 + app_rectangles * 2 + app_circles * 2
print("Score: " + str(users_score) + "/" + str(max_score))
print("Percentage score:" + str(users_score/max_score * 100))
# #user's score
# print("User's score", user_image_shapes)

# #full score
# print("full score", image_shapes)

# #percentage score
# percentage_score = score/full_score*100
# print("percentage score", percentage_score)

# #image given to the user
# i1 = PIL.Image.open(image_path)     

# #image made by the user
# i2 = PIL.Image.open(user_image_path)    

# #assert i1.mode == i2.mode, "Different kinds of images."
# #assert i1.size == i2.size, "Different sizes."
 
# #code for pixel by pixel comparison 
# pairs = zip(i1.getdata(), i2.getdata())
# if len(i1.getbands()) == 1:
#     # for gray-scale jpegs
#     dif = sum(abs(p1-p2) for p1,p2 in pairs)
# else:
#     dif = sum(abs(c1-c2) for p1,p2 in pairs for c1,c2 in zip(p1,p2))
 
# ncomponents = i1.size[0] * i1.size[1] * 3
# diff = (dif / 255.0 * 100) / ncomponents

# print("\n")

# print ("Difference (percentage):", (diff))

# if (diff <50):
#     print("High percentage of similarity, images match")

# else:
# 	print("Low percentage of similarity, images don't match")

#remove the image created from the user from the server
os.remove(user_image_path)

