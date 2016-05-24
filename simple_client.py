import socket
import sys


s = socket.create_connection((sys.argv[1], 1337))

try:
    while True:
        buf = s.recv(6)
        print(buf)
        if len(buf) == 0:
            break
finally:
    s.close()

