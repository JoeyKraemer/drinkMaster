import serial
import time

# Open grbl serial port
s = serial.Serial('/dev/ttyUSB0',115200)

# Wake up grbl
a = "\r\n\r\n"
s.write(a.encode())
time.sleep(2)   # Wait for grbl to initialize 
s.flushInput()  # Flush startup text in serial input


gcode = ""

while gcode != "q":
    gcode = input("Input gcode, q to quit: ")

    if gcode == "q":
        break
    else:
        print ('Sending: ') 
        print (gcode)
        gcode = gcode + '\n'
        s.write(gcode.encode())
        grbl_out = s.readline() # Wait for grbl response with carriage return
        print(' : ') 
        print(grbl_out.strip())

# Close serial port
s.close()  
  