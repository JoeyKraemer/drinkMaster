import network
import socket
from time import *
from picozero import pico_led, pico_temp_sensor
import machine

ssid = "DRINKMASTER"
password = "projectdrinkmaster"

def connectWlan():
    #Connect to WiFi
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(ssid,password)
    
    while wlan.isconnected() == False:
        print("Waiting for connection...")
        sleep(1)
    ip = wlan.ifconfig()[0]
    print(f"Connected on {ip}")
    return ip
    
def openSocket(ip):
    #Open a socket
    address = (ip,80)
    connection = socket.socket()
    connection.bind(address)
    connection.listen(1)
    return connection

def webpage(temperature, state):
    #Simple HTML page
    html = f"""
            <!DOCTYPE html>
            <html>
            <head>
            <title>Hello World!</title>
            </head>
            <body>
            <h1>Hello World!</h1><br>
            <h2>Light control</h2>
            <form action="./lighton">
            <input type="submit" value="Light on">
            </form>
            <form action="./lightoff">
            <input type="submit" value="Light off">
            </form>
            <p>LED is {state}</p>
            <p>Temperature is 0</p>
            </body>
            </html>
            """
    return str(html)

def serve(connection):
    #Start a web server
    state = 'OFF'
    pico_led.off()
    temperature = 0
    while True:
        client = connection.accept()[0]
        request = client.recv(1024)
        request = str(request)
        try:
            request = request.split()[1]
        except IndexError:
            pass
        if request == '/lighton?':
            pico_led.on()
            state = 'ON'
        elif request =='/lightoff?':
            pico_led.off()
            state = 'OFF'
        temperature = pico_temp_sensor.temp
        html = webpage(temperature, state)
        client.send(html)
        client.close()

try:
    ip = connectWlan()
    connection = openSocket(ip)
    serve(connection)
except:
    KeyboardInterrupt
    
    

    

