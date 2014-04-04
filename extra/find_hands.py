#!/usr/bin/env python
import numpy as np
import cv2 as cv
import socket
import sys
import datetime, time
import random

hashtags = [
        "#65pts",
        "#3ubertubs",
        "#7discauto",
        "#hewent2jared",
        "#loltimber",
        "#ejswag",
        "#caprisunluvr",
        "#suchshoot",
        "#travus",
        "#patfairbanks",
        "#qualiteeaward",
        "#makuhdashots",
        "wow",
        "#blubannarz",
        "#pewpew",
        "#vrooooom",
        "FFFFFFFUUUUUUUUUUUUU",
        "#saftiefrist",
        "#kanagasabapathy",
        "#einstineorbust",
        "#trusskids",
        "#inspirashun",
        "#miked #heFTGtoo",
        "#wuhdieflwrz",
        "#cubington",
        "#sonasa"
        ]

HOST, PORT = "10.2.54.2", 1180

WIDTH_PX = 1000
WEBCAM_WIDTH_PX = 640
WEBCAM_HEIGHT_PX = 360
X_OFFSET = (WIDTH_PX - WEBCAM_WIDTH_PX)/2

CAL_UL = (X_OFFSET + WEBCAM_WIDTH_PX/2 - 20, 180)
CAL_LR = (X_OFFSET + WEBCAM_WIDTH_PX/2 + 20, 220)

LEFT_UL = (240 + X_OFFSET, 250)
LEFT_LR = (310 + X_OFFSET, 300)

RIGHT_UL = (WEBCAM_WIDTH_PX - 310 + X_OFFSET, 250)
RIGHT_LR = (WEBCAM_WIDTH_PX - 240 + X_OFFSET, 300)

MAX_COLOR_DISTANCE = 100

UPDATE_RATE_HZ = 40.0
PERIOD = (1.0 / UPDATE_RATE_HZ) * 1000.0

connected = False

def getTimeMillis():
    return int(round(time.time() * 1000))

def color_distance(c1, c2):
    total_diff = 0
    for i in (0, 1, 2):
        diff = (c1[i]-c2[i])
        # Wrap hue angle
        if i == 0:
            while diff < -180:
                diff += 360
            while diff > 180:
                diff -= 360
        total_diff += abs(diff)
    #print total_diff
    return total_diff

def choose_swag():
    return random.choice(hashtags)

def apply_swag(img, swag, location):
    cv.putText(img, swag, location, cv.FONT_HERSHEY_PLAIN, 3, (0, 128, 255), 3)

def color_if_far(img, distance, ul, lr):
    if distance < MAX_COLOR_DISTANCE:
        cv.rectangle(img, ul, lr, (0, 255, 255), -1)
    return distance < MAX_COLOR_DISTANCE

def draw_static(img):
    bg = np.zeros((img.shape[0], WIDTH_PX, 3), dtype=np.uint8)
    bg[:,X_OFFSET:X_OFFSET+WEBCAM_WIDTH_PX,:] = img
    cv.rectangle(bg, LEFT_UL, LEFT_LR, (0, 255, 255), 3)
    cv.rectangle(bg, RIGHT_UL, RIGHT_LR, (0, 255, 255), 3)
    cv.rectangle(bg, CAL_UL, CAL_LR, (255, 255, 255), 3)
    if connected:
        cv.rectangle(bg, (0, 0), (bg.shape[1]-1, bg.shape[0]-1), (0, 255, 0), 15)
    else:
        cv.rectangle(bg, (0, 0), (bg.shape[1]-1, bg.shape[0]-1), (0, 0, 255), 15)
    return bg

def detect_color(img, box):
    h = np.mean(img[box[0][1]+3:box[1][1]-3, box[0][0]+3:box[1][0]-3, 0])
    s = np.mean(img[box[0][1]+3:box[1][1]-3, box[0][0]+3:box[1][0]-3, 1])
    v = np.mean(img[box[0][1]+3:box[1][1]-3, box[0][0]+3:box[1][0]-3, 2])
    return (h,s,v)

def detect_colors(img):
    cal = detect_color(img, (CAL_UL, CAL_LR))
    left = detect_color(img, (LEFT_UL, LEFT_LR))
    right = detect_color(img, (RIGHT_UL, RIGHT_LR))

    return cal, left, right

if __name__ == '__main__':
    cv.namedWindow("HotChez",1)
    capture = cv.VideoCapture(0)


    do_swag = "--swag" in sys.argv
    swag = None
    swag_min = (100, 50)
    swag_max = (700, 180)
    swag_loc = (WIDTH_PX/2-100, 100)
    left_last = False
    right_last = False
    exposure = -4
    last_exposure = exposure
    capture.set(15,exposure)

    last_t = getTimeMillis()
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(.05)
    try:
        s.connect((HOST, PORT))
    except:
        print "Could not connect at boot"

    while 1:
        _, img = capture.read()
        small_img = cv.flip(cv.resize(img, (WEBCAM_WIDTH_PX, WEBCAM_HEIGHT_PX)), 1)
        bg = draw_static(small_img)
        cal, left, right = detect_colors(cv.cvtColor(bg, cv.COLOR_BGR2HSV))

        #print "(%f, %f, %f) (%f, %f, %f)" % (left[0],left[1],left[2],right[0],right[1],right[2])
        left_dist = color_distance(left, cal)
        right_dist = color_distance(right, cal)

        left_on = color_if_far(bg, left_dist, (6, 6), ((WIDTH_PX-WEBCAM_WIDTH_PX)/2-6, WEBCAM_HEIGHT_PX-6))
        right_on = color_if_far(bg, right_dist, ((WIDTH_PX+WEBCAM_WIDTH_PX)/2+6, 6), (WIDTH_PX-6, WEBCAM_HEIGHT_PX-6))
        if do_swag:
            if left_on and not left_last:
                swag = choose_swag()
                swag_loc = (random.randint(swag_min[0], swag_max[0]), random.randint(swag_min[1], swag_max[1]))
            if right_on and not right_last:
                swag = choose_swag()
                swag_loc = (random.randint(swag_min[0], swag_max[0]), random.randint(swag_min[1], swag_max[1]))
            if left_on or right_on:
                apply_swag(bg, swag, swag_loc)
            left_last = left_on
            right_last = right_on
        cur_time = getTimeMillis()
        # Throttle the output
        if last_t + PERIOD <= cur_time: 
            try:
                bytes = bytearray()
                v = (left_on << 1) | (right_on << 0)
                bytes.append(v)
                s.send(bytes)
                #print "dleft: %f | %d, dright: %f | %d" % (left_dist, left_on, right_dist, right_on)
                last_t = cur_time
                connected = True
            except:
                print "Could not connect"
                connected = False
                try:
                    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    s.settimeout(.05)
                    s.connect((HOST, PORT))
                except:
                    print "failed to reconnect"
                    last_t = cur_time + 1000    

        cv.imshow("HotChez", bg)
        key = cv.waitKey(10) & 255
        if key == 27:
            break
        elif key == 119:
            exposure += 1
        elif key == 115:
            exposure -= 1

        if exposure < -7:
            exposure = -7
        elif exposure > -1:
            exposure = -1

        if exposure != last_exposure:
            print "Changing exposure to "
            print exposure
            capture.set(15,exposure)

        last_exposure = exposure

        

    s.close()
