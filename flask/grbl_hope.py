import serial
import time
import os

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
                action("calibration")
                return render_template("index.html")
            elif v == "goToUser":
                action("goToUser")
                return render_template("index.html")
            elif v == "DRINK1":
                action('drink1')
                return render_template("index.html")
            elif v == "DRINK2":
                action("drink2")
                return render_template("index.html")   
            elif v == "DRINK3":
                action("DRINKNAME")
                return render_template("index.html")
            elif v == "DRINK4":
                action("DRINKNAME")
                return render_template("index.html")
            elif v == "rebootPi":
                restartPi()
                return render_template("index.html")
            elif v == "pushUp":
                action("pushUp")
                return render_template("index.html")
            elif v == "pushDown":
                action("pushDown")
                return render_template("index.html")     
        else:
            return render_template("index.html")
        
def action (drink):
    #gcode commands stored in arrays
    #helper movements
    pushUp = ["$X","G92 Z0","G0 F15000", "G0 Z-0800"]
    pushDown = ["$X", "G92 Z0","G0 F15000", "G0 Z0800"]
    goToUser = ["$X","G0 F10000","G90","G0 Z0X0Y0 ;BUFFER",";BUFFER"]

    #calibration movements
    goToUserHome = ["$X","G91","G0 F15000","G92 Y0 X0 Z0","G0 X0150","G0 Z-2200","G0 Y-0350","$#","G0010"]
    homeY = ["$X","G91","G92 X0 Y0 Z0","G0 F15000","G0 Y2000"]
    homeX = ["$X","G91","G0 F15000","G0 X-2000"]
    homeZ = ["$X","G91","G0 F15000","G0 Z1000","G0 Z1000","G0 Z9999"]
    pushY = ["$X","G91","G0 F15000","G0 Y-0010"]
    pushX = ["$X","G91","G0 F15000","G0 X0010"]
    pushZ = ["$X","G91","G92 Z0","G0 F15000","G0 Z-0700"]

    #drinks
    drink1 = [] # Drink 1
    drink2 = ["$X","G91","G92 X0 Y0 Z0","G0 F9000","G0 Z1900","G0 F15000","G0 Y0083","G0 X0115","G0 F6000","G0 Z-0900  ;Buffer","G0 Z0900"] # Drink 2
    drink3 = [] # Drink 3
    drink4 = [] # Drink 4
   
    if drink == "calibration":
        calibration = [homeY,homeZ,homeX,pushY,pushX,pushZ,goToUserHome]
        sendToGRBL(calibration)
        print(drink+": Perfectly executed")
    elif drink == "goToUser":
        sendToGRBL([goToUser])
        print(drink+": Perfectly executed")
    elif drink == "drink1":
        sendToGRBL([drink1])
    elif drink == "drink2":
        sendToGRBL([drink2])
    elif drink == "drink3":
        sendToGRBL([drink3])
    elif drink == "drink4":
        sendToGRBL([drink4])
    elif drink == "pushUp":
        sendToGRBL([pushUp])
    elif drink == "pushDown":
        sendToGRBL([pushDown])


def sendToGRBL(gcodeArray):
    for movement in gcodeArray:
        s = serial.Serial('/dev/ttyUSB0',115200)
        a = "\r\n\r\n"
        s.write(a.encode())
        time.sleep(2)
        s.flushInput()
        s.flushOutput()

        for command in movement:
            print('Sending: ' + command)
            substring_whatever = command[-4:]
            command += '\n'
            s.write(command.encode())
            response = s.readline()
            print('Response: ' + response.decode())
            time.sleep(0.8)
            
        print("BUFFER MUFFER ",substring_whatever)
        
        if command.find(";") > 0: 
            time.sleep(1.2)
            print("EXTRA BUFFER TRIGGER")
        else:
            time.sleep(abs(int(substring_whatever)) / 1000)
        # time.sleep(5)
        s.close()

def restartPi():
    os.system("sudo reboot")
    exit()

def main():
    app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
    main()
