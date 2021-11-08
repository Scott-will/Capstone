
import bluetooth
server_sock=bluetooth.BluetoothSocket(bluetooth.RFCOMM)
port=17
server_sock.bind(("",port))
server_sock.listen(1)
client_sock,address=server_sock.accept()
print("Acccept")
while(True):
    data=client_sock.recv(1024)
    print("recevied [%s]"% data)


client_sock.close()
server_sock.close()