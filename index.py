import serial
import time
from flask import render_template
from flask import Flask
from flask import request

app = Flask(__name__)

@app.route("/", methods=["GET","POST"])
def index():
	return render_template("index.html")
	if request.method == "POST":
		return "yes"

with app.app_context():
	index()
