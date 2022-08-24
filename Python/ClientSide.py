import socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("localhost", 420))
msg = "HELLO WORLD"
msg = msg.encode("utf-8")
s.send(msg)
s.recv(4096)
s.close()

