# Creates html page with error message and soft resets grbl 
import serial
import time

from flask import render_template
from flask import Flask
from flask import request

app = Flask(__name__)

@app.route("/", methods=["GET","POST"])
def index():
    if request.method == "POST":
        main()
        return render_template("index.html", error = "")
    elif request.method == "GET":
        if request.args.get("action"):
            v = request.args.get("action")
            if v == "calibration":
                calibration()
            elif v == "goToUser":
                action("goToUser")
            elif v == "DRINK1":
                action('freeCup')
            elif v == "DRINK2":
                action("DRINK2")   
            elif v == "DRINK3":
                action("DRINKNAME")
            elif v == "DRINK4":
                action("DRINKNAME")
            elif v == "freeCup":
                action("freeCup")
            return render_template("index.html", error = "")
        else:
            return render_template("index.html", error = "")

def sendToGRBL(serial,file,delay):
    a = "\r\n\r\n"
    serial.write(a.encode())
    time.sleep(2)   # Wait for grbl to initialize 
    serial.flushInput()  # Flush startup text in serial input

    for line in file:
        l = line.strip() # Strip all EOL characters for consistency
        print ('Sending: ') 
        print (l)
        l = l + '\n'
        serial.write(l.encode())
        grbl_out = serial.readline().strip() # Wait for grbl response with carriage return
        print('Response: ') 
        print(grbl_out)
        if "ALARM" in grbl_out.decode('UTF-8'):
            return render_template("index.html", error = grbl_out)
        time.sleep(0.75)

    time.sleep(delay)
    print(grbl_out)
    file.close()
    serial.close()

def action(drink):
    s = serial.Serial('/dev/ttyUSB0',115200)
    f = open('GCODE/' + drink + '.gcode','r')
    sendToGRBL(s,f,2)
    
    return render_template("index.html", error = "")

def calibration():
    i = 0
    if i == 0:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/homingY.gcode','r')
        sendToGRBL(s,f,3)
        i = 1

    if i == 1:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/homingZ.gcode','r')
        sendToGRBL(s,f,12)
        i = 2

    if i == 2:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/homingX.gcode','r')
        sendToGRBL(s,f,2)
        i = 3

    if i == 3:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/pushX.gcode','r')
        sendToGRBL(s,f,1)
        i = 4
    
    if i == 4:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/pushY.gcode','r')
        sendToGRBL(s,f,1)
        i = 5
    
    # if i == 5:
    #     s = serial.Serial('/dev/ttyUSB0',115200)
    #     f = open('GCODE/pushZ.gcode','r')
    #     sendToGRBL(s,f,6)
    #     i = 6
    
    if i == 5:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/pushZ.gcode','r')
        sendToGRBL(s,f,6)
        i = 7

    if i == 7:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/goToUser.gcode','r')
        sendToGRBL(s,f,10)

    return render_template("index.html")  

def main():
    app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
    main()
