import os
import sys
if len(sys.argv) < 2:
    touch = open("redirect.log","w")
    touch.write("Not enough arguments - no command to redirect")
    touch.close()
else:
    command = sys.argv[1]
    touch = open("redirect.log","w")
    touch.write(command)
    touch.close()
    os.system(command)
