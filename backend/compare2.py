#########################################################################################################################################################

# This file gets the image given to the user from the server and the image that the user makes and compares them pixel by pixel. 
# If the images are similar (up to a sertain percentage) then a function named "shapes" is called and it detects all the shapes from both images

#########################################################################################################################################################


import numpy as np 
import cv2
import os
import sys

#initialize paths
image_path = "images/" + sys.argv[1]
user_image_path = "userImages/" + sys.argv[1]

def shapes(path):
    image_shapes = [0,0,0,0]

    # read image
    image = cv2.imread(path)

    # convert to grayscale
    gray_scaled_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # convert to edge
    edged = cv2.Canny(gray_scaled_image, 0, 255)

    # find contours to edged image
    contours, _ = cv2.findContours(edged,
                                    cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # draw contours to original image
    for contour in contours:
        cv2.drawContours(image, contours, -1, (255,0,0), 3)

    def detect_shape(contour):
        # initialize shape
        shape = "none"

        # calculate perimeter
        perimeter = cv2.arcLength(contour, True)

        # apply contour approximation and store the result in vertices
        vertices = cv2.approxPolyDP(contour, 0.01 * perimeter, True)

        # find shapes by vertices
        if len(vertices) == 3:
            image_shapes[0] += 1
            shape = "triangle"
        elif len(vertices) == 4:
            image_shapes[1] += 1
            shape = "rectangle"
        elif len(vertices) == 5:
            image_shapes[2] += 1
            shape = "pentagon"
        elif len(vertices) > 10:
            image_shapes[3] += 1
            shape = "circle"
        return shape


    for contour in contours:
        # find the circle of the image
        M = cv2.moments(contour)
        cX = int(M['m10'] / M['m00'])
        cY = int(M['m01'] / M['m00'])

        # find shape for each contour
        shape = detect_shape(contour)

        # Outline the contours
        cv2.drawContours(image, [contour], -1, (255, 0, 0), 2)

        # Write the name of shape on the center of shapes
        cv2.putText(image, shape, (cX, cY), cv2.FONT_HERSHEY_SIMPLEX,
                    0.5, (0, 0, 0), 2)

    arr = np.array(image_shapes)

    arr = arr / 4

    return arr

image_shapes = shapes(image_path)
user_image_shapes = shapes(user_image_path)

print("App's Image:")
print("Triangles:", image_shapes[0])
print("Rectangles:", image_shapes[1])
print("Pentagons:", image_shapes[2])
print("Circles:", image_shapes[3])

print("\n")

print("User's Image")
print("Triangles:", user_image_shapes[0])
print("Rectangles:", user_image_shapes[1])
print("Pentagons:", user_image_shapes[2])
print("Circles:", user_image_shapes[3])

#count score
score = 0 
for i in range(len(image_shapes)):
    if (image_shapes[i] <= user_image_shapes[i]):
        score += user_image_shapes[i]
    else:
        score += (image_shapes[i] - (image_shapes[i] - (user_image_shapes[i])))

print("\n")

#user's score
print("User's score", user_image_shapes)

#full score
full_score = image_shapes
print("full score", full_score)

#percentage score
# percentage_score = score/full_score*100
# print("percentage score", percentage_score)

#remove the image created from the user from the server
os.remove(user_image_path)

