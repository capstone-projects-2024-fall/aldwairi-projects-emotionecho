import AudioStream
import socket

class AudioReceiver:
    """
    Uses socket communication between kotlin application and script to retrieve real-time
    mic audio.
    """
    
    def __init__(self):
        """
        Constructor for AudioReceiver
        """
        self.socketAddr = ('0.0.0.0', 8888)
        self.isReceiving = False
        #Might manage streams another way, pass through constructor
        self.audioStream = AudioStream()

    def startReceiving(self) -> None:
        """
        Start receiving and routing Audio data to the AudioStream from the microphone
        """
        serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serverSocket.bind(self.socketAddr)
        serverSocket.listen(1)

        self.isReceiving = True
        while True:
            conn, addr = serverSocket.accept()

            while True:
                #Buffer size will need adjusting
                audio_data = conn.recv(1024)
                if audio_data:
                    #Add to AudioStream
                    pass


    def stopReceiving(self) -> None:
        self.isReceiving = False

