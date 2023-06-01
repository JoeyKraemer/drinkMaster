import serial
import time
from flask import render_template
from flask import Flask
from flask import request
from grbl_test import main as grbl

app = Flask(__name__)

@app.route("/", methods=["GET","POST"])
def index():
	if request.method == "POST":
		grbl()
		return render_template("index.html")
	
	elif request.method == "GET":
		arg = request.args["hello"];
		print(f"<p> {arg} </p>");

	 

def main():
	app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
	main()
