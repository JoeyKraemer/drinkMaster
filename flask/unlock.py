#!/usr/bin/env python
"""\
Simple g-code streaming script for grbl

Provided as an illustration of the basic communication interface
for grbl. When grbl has finished parsing the g-code block, it will
return an 'ok' or 'error' response. When the planner buffer is full,
grbl will not send a response until the planner buffer clears space.

G02/03 arcs are special exceptions, where they inject short line 
segments directly into the planner. So there may not be a response 
from grbl for the duration of the arc.

"""
import serial
import time

def main():

# Open grbl serial port
	s = serial.Serial('/dev/ttyUSB0',115200)
	a = "\r\n\r\n"
	s.write(a.encode())
	time.sleep(2)   # Wait for grbl to initialize 
	s.flushInput()  # Flush startup text in serial input
	
	l = "$X"
    print("Unlocking GRBL...")
    s.write(l.encode())
    grbl_out = s.readline() # Wait for grbl response with carriage return
    print('Response: ') 
    print(grbl_out.strip())
    		

# Close file and serial port
    s.close()    

if __name__ == "__main__":
	main()