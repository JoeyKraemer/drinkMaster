import serial
import time

# Open grbl serial port
s = serial.Serial('/dev/ttyUSB0',115200)

# Wake up grbl
s.write("\r\n\r\n")
time.sleep(2)   # Wait for grbl to initialize 
s.flushInput()  # Flush startup text in serial input

gcode  = ""

while gcode != "q":
    gcode = input("Enter gcode, press q to quit: ")

    if gcode == "q":
        break
    else:
        s.write(gcode);