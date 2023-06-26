import serial
import time
import os

from flask import render_template
from flask import Flask
from flask import request

x = 0
y = 0
z = 0

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
                goToUser(x,y,z)
                return render_template("index.html")
            elif v == "DRINK1":
                action('drink1')
                return render_template("index.html")
            elif v == "DRINK2":
                action("drink2")
                return render_template("index.html")   
            elif v == "DRINK3":
                action("drink3")
                return render_template("index.html")
            elif v == "DRINK4":
                action("drink4")
                return render_template("index.html")
            elif v == "rebootPi":
                restartPi()
                return render_template("index.html")
            elif v == "rebootPlatform":
                restartGRBL()
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
    pushUp = ["$x","G92 Z0","G0 F15000", "G0 Z-0900"]
    pushDown = ["$x", "G92 Z0","G0 F15000", "G0 Z0900"]

    #calibration movements
    goToUserHome = ["$X","G91","G0 F15000","G92 Y0 X0 Z0","G0 X0150","G0 Z-2200","G0 Y-0350"]
    homeY = ["$X","G91","G0 F15000","G0 Y2000"]
    homeX = ["$X","G91","G0 F15000","G0 X-2000"]
    homeZ = ["$X","G91","G0 F15000","G0 Z9999"]
    pushY = ["$X","G91","G0 F15000","G0 Y-0010"]
    pushX = ["$X","G91","G0 F15000","G0 X0010"]
    pushZ = ["$X","G91","G92 Z0","G0 F15000","G0 Z-0700"]

    #drinks
    drink1 = [
        "$x","G90","G0 F9000", "G0 Z1900", "G0 F15000", "G0 Y0210", "G0 X0120",
        "G0 F6000","G92 z0", "G0 Z-1000", "G04 P5","G92 z0", "G0 Z0900", 
        "G0 F12000","G92 x0", "G0 X-220","G92 z0", "G0 Z-900", "G04 P5","G92 z0", "G0 Z0800"
        ] # Drink 1
    drink2 = [
        "$x","G90","G0 F9000","G0 Z1900","G0 F15000","G0 Y0105","G0 X0115",
        "G0 F6000","G92 z0"," G0 Z-1000","G04 P5","G92 z0 ","G0 Z780",
        "G0 F12000","G92 x0","G0 X-220","G92 y0","G0 Y-5","G0 F6000", "G92 z0","G0 Z-900","G04 P5","G92 z0","G0 Z600"] # Drink 2
    drink3 = [
        "$x", "G90", "G0 F9000", "G0 Z1900", "G0 F15000", "G0 Y0315", "G0 X0110", "G92 y0","G0 Y-5",
        "G0 F6000", "G92 z0", "G0 Z-1000", "G04 P5", "G92 z0", "G0 Z0900",
        "G0 F12000", "G92 x0", "G0 X-210", "G92 y0","G0 Y-5","G0 F6000", "G92 z0", "G0 Z-900", "G04 P5", "G92 z0", "G0 Z0800"
    ] # Drink 3
    drink4 = [] # Drink 4
   
    if drink == "calibration":
        home = [homeY,homeZ,homeX,pushY,pushX,pushZ,goToUserHome]
        calibration(home)
        print(drink+": Perfectly executed")
    elif drink == "goToUser":
        goToUser(x,y,z)
        print(drink+": Perfectly executed")
    elif drink == "drink1":
        sendToGRBL(drink1)
        time.sleep(5)
        goToUser(x,y,z)
    elif drink == "drink2":
        sendToGRBL(drink2)
        time.sleep(5)
        goToUser(x,y,z)
    elif drink == "drink3":
        sendToGRBL(drink3)
        time.sleep(5)
        goToUser(x,y,z)
    elif drink == "drink4":
        sendToGRBL(drink4)
        time.sleep(5)
        goToUser(x,y,z)
    elif drink == "pushUp":
        sendToGRBL(pushUp)
    elif drink == "pushDown":
        sendToGRBL(pushDown)


def calibration(gcodeArray):
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
        time.sleep(abs(int(substring_whatever)) / 1000)
        s.close()
    global x,y,z
    x = 0
    y = 0
    z = 0


def sendToGRBL(drink):
    s = serial.Serial('/dev/ttyUSB0',115200)
    a = "\r\n\r\n"
    s.write(a.encode())
    time.sleep(2)
    s.flushInput()
    s.flushOutput()
    global x, y, z
     
    for command in drink:
        print('Sending: ' + command)
        if "X" in command:
            substring_whatever = command.split("X")
            print(substring_whatever[1])
            x += int(substring_whatever[1])
        elif "Y" in command:
            substring_whatever = command.split("Y")
            print(substring_whatever[1])
            y += int(substring_whatever[1]) 
        elif "Z" in command:
            substring_whatever = command.split("Z")
            print(substring_whatever[1])
            z += int(substring_whatever[1])
        command += '\n'
        s.write(command.encode())
        response = s.readline()
        print('Response: ' + response.decode())
    
    print("Current position: X", str(x), " Y", str(y), " Z", str(z))
    s.close()

def goToUser(x,y,z):
    currentPosition = "G92 x" + str(x) +  "G92 y" + str(y) + "G92 z" + str(z)
    #currentPositionY = "G92 y" + str(y)
    #currentPositionZ = "G92 z" + str(z)
    
    x = int(x) * -1
    y = int(y) * -1
    z = int(z) * -1

    Xaxis = "G0 X" + str(x)
    Yaxis = "G0 Y" + str(y)
    Zaxis = "G0 Z" + str(z-100)

    ar = ["$x",currentPosition,"G91","G0 F10000",Xaxis,Zaxis,Yaxis]

    s = serial.Serial('/dev/ttyUSB0',115200)
    a = "\r\n\r\n"
    s.write(a.encode())
    time.sleep(2)
    s.flushInput()
    s.flushOutput()

    for command in ar:
        print('Sending: ' + command)
        if "X" in command:
                substring_whatever = command.split("X")
                print(substring_whatever[1])
                x +=int(substring_whatever[1])
        elif "Y" in command:
            substring_whatever = command.split("Y")
            print(substring_whatever[1])
            y +=int(substring_whatever[1]) 
        elif "Z" in command:
            substring_whatever = command.split("Z")
            print(substring_whatever[1])
            z +=int(substring_whatever[1])
        command += '\n'
        s.write(command.encode())
        response = s.readline()
        print('Response: ' + response.decode())
    print("Current position: X",x," Y",y," Z",z)


def restartPi():
    os.system("sudo reboot")
    exit()


def restartGRBL():
    s = serial.Serial('/dev/ttyUSB0',115200)
    a = "\r\n\r\n"
    s.write(a.encode())
    time.sleep(2)
    s.flushInput()
    s.flushOutput()
    d = ";RESTART"
    s.write(d.encode())
    output = s.readline()
    print("Response",output.decode())
    s.close()


def main():
    app.run(debug=True, host="0.0.0.0", use_reloader=False)


if __name__ == "__main__":
    main()
