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
        
def calibration():



def action (drink):
    s = serial.Serial('/dev/ttyUSB0',115200)
    if drink == "calibration":
        homingY = ["$X","G92 X0 Y0 Z0","G0 F15000","G0 Y2000"]
        homingX = ["$x","G0 F15000","G0 X-2000"]
        homingZ = ["$x","G0 F15000","G0 Z7000"]
        pushY = ["$x","G0 F15000","G0 Y-10"]
        pushX = ["$x","G0 F15000","G0 X10"]
        pushZ = ["$x","G0 F15000","G0 Z-700"]

def sendToGRBL()
