import serial
import time
from flask import render_template
from flask import Flask
from flask import request
from grbl_test import main as fuck

app = Flask(__name__)

@app.route("/", methods=["GET","POST"])
def index():
	if request.method == "POST":
		fuck()
	return render_template("index.html")

def main():
	app.run(debug=True, host="0.0.0.0", use_reloader=False)

if __name__ == "__main__":
	main()
