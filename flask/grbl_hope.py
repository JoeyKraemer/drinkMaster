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
                action("calibration")
                return render_template("index.html")
            elif v == "goToUser":
                action("goToUser")
                return render_template("index.html")
            elif v == "DRINK1":
                action('freeCup')
                return render_template("index.html")
            elif v == "DRINK2":
                action("DRINK2")
                return render_template("index.html")   
            elif v == "DRINK3":
                action("DRINKNAME")
                return render_template("index.html")
            elif v == "DRINK4":
                action("DRINKNAME")
                return render_template("index.html")
            elif v == "freeCup":
                action("freeCup")
                return render_template("index.html")
        else:
            return render_template("index.html")
        
def action (drink):
    #gcode commands stored in arrays
    goToUser = ["$X","G0 F15000","G92 Y0 X0 Z0","G0 X150","G0 Z-2500","G0 Y-320"]
    homeY = ["$X","G92 X0 Y0 Z0","G0 F15000","G0 Y2000"]
    homeX = ["$X","G0 F15000","G0 X-2000"]
    homeZ = ["$X","G0 F15000","G0 Z7000"]
    pushY = ["$X","G0 F15000","G0 Y-10"]
    pushX = ["$X","G0 F15000","G0 X10"]
    pushZ = ["$X","G0 F15000","G0 Z-700"]

   
    if drink == "calibration":
        calibration = [homeY,homeZ,homeX,pushY,pushX,pushZ,goToUser]
        sendToGRBL(calibration)
        print(drink+": Perfectly executed")
    elif drink == "goToUser":
        sendToGRBL(goToUser)
        print(drink+": Perfectly executed")
    # elif drink == "freeCup":
    #     sendToGRBL(freeCup)
    # elif drink == "drink1":
    #     sendToGRBL(drink1)
    # elif drink == "drink2":
    #     sendToGRBL(drink2)
    # elif drink == "drink3":
    #     sendToGRBL(drink3)
    # elif drink == "drink4":
    #     sendToGRBL(drink4)

def sendToGRBL(gcodeArray):
    s = serial.Serial('/dev/ttyUSB0',115200)
    a = "\r\n\r\n"
    s.write(a.encode())
    time.sleep(2)
    s.flushInput()

    for movement in gcodeArray:
        for command in movement:
            print('Sending: ' + command)
            s.write(command.encode())
            print("write works")
            # response = s.read()
            # print("read works")
            # print('Response: ' + response)
            time.sleep(0.8)

    s.close()
        
    
def main():
    app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
    main()
