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
        return render_template("index.html")
    elif request.method == "GET":
        if request.args.get("action"):
            v = request.args.get("action")
            if v == "calibration":
                calibration()
                return render_template("index.html")
            elif v == "goToUser":
                action("goToUser")
                return render_template("index.html")
            elif v == "DRINK1":
                action('freeCup')
                return render_template("index.html")
            elif v == "DRINK2":
                action("DRINKNAME")
                return render_template("index.html")
            elif v == "DRINK3":
                action("DRINKNAME")
                return render_template("index.html")
            elif v == "DRINK4":
                action("DRINKNAME")
                return render_template("index.html")
        else:
            return render_template("index.html")

def showError(error, sp):
    if error.find("Alarm"):
        sp.close()
        return render_template("error.html", error=error)

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
        time.sleep(1)
        grbl_out = serial.readline().strip() # Wait for grbl response with carriage return
        #showError(grbl_out,serial)
        print('Response: ') 
        print(grbl_out)
    time.sleep(delay)
    file.close()
    serial.close()

def action(drink):
    s = serial.Serial('/dev/ttyUSB0',115200)
    f = open('GCODE/' + drink + '.gcode','r')
    sendToGRBL(s,f)

def calibration():
    i = 0
    if i == 0:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/homingY.gcode','r')
        sendToGRBL(s,f,2)
        i = 1

    if i == 1:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/homingZ.gcode','r')
        sendToGRBL(s,f,4)
        i = 2

    if i == 2:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/homingX.gcode','r')
        sendToGRBL(s,f,1)
        i = 3

    if i == 3:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/pushX.gcode','r')
        sendToGRBL(s,f,1)
        i = 4

    if i == 4:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/pushZ.gcode','r')
        sendToGRBL(s,f,1)
        i = 5

    if i == 5:
        s = serial.Serial('/dev/ttyUSB0',115200)
        f = open('GCODE/goToUser.gcode','r')
        sendToGRBL(s,f,10)

def main():
    app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
    main()


