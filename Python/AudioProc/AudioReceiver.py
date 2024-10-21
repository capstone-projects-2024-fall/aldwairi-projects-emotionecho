import AudioStream
import socket
import threading
from time import sleep

class AudioReceiver(threading.Thread):
    """
    Uses socket communication between kotlin application and script to retrieve real-time
    mic audio.
    """
    
    def __init__(self, manager, stopEvnt: threading.Event):
        """
        Constructor for AudioReceiver
        """
        self.manager = manager
        self.stopEvnt = stopEvnt
        self.socketAddr = ('0.0.0.0', 8888)


    def run(self):
        """
        Start receiving and routing Audio data to the AudioStream from the microphone
        """
        serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serverSocket.bind(self.socketAddr)
        serverSocket.listen(1)

        while True:
            conn, addr = serverSocket.accept()
            self.clientHandler(conn)

    def clientHandler(self, conn: socket.socket):
        """
        Handle connection Android app connection
        """
        try:
            while True:
                data = conn.recv(1024)

                if not data:
                    continue

                if self.stopEvnt.is_set():
                    break
                
                #This might need updating
                self.manager.audioStream.add()
                #Sleep time might need to be updated
                sleep(1)
        finally:
            conn.close()

