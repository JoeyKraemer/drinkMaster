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
            elif v == "goToUser":
                action("goToUser")
            elif v == "DRINK1":
                action("DRINKNAME")
            elif v == "DRINK2":
                action("DRINKNAME")
            elif v == "DRINK3":
                action("DRINKNAME")
            elif v == "DRINK4":
                action("DRINKNAME")
        else:
            return render_template("index.html")

def showError(error : str,sp):
    foo = "Lalala"
    foo = error
    if foo.find("Alarm"):
        sp.close()
        return render_template("error.html", error=error)

def sendToGRBL(serial,file):
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
        grbl_out = serial.readline().strip # Wait for grbl response with carriage return
        showError(grbl_out,serial)
        print('Response: ') 
        print(grbl_out)
    serial.close()

def action(drink):
    s = serial.Serial('/dev/ttyUSB0',115200)
    f = open('GCODE/'+ drink +'.gcode','r')
    sendToGRBL(s,f)

def calibration():
    s = serial.Serial('/dev/ttyUSB0',115200)
    f = open('GCODE/homingY.gcode','r')
    sendToGRBL(s,f)
    f = open('GCODE/homingX.gcode','r')
    sendToGRBL(s,f)
    f = open('GCODE/homingZ.gcode','r')
    sendToGRBL(s,f)
    f = open('GCODE/goToUser.grbl.gcode','r')
    sendToGRBL(s,f)

def main():
    app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
    main()


