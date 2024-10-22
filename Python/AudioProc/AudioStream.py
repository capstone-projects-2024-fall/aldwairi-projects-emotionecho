import numpy as np
import threading

class AudioStream:
    """
    AudioStream class is used as an efficient circular buffer
    """
    
    def __init__(self, buffSize: int, chunkSize: int):
        """
        Constructor for AudioStream class
        Args:
            buffSize: The size of the circular biffer
            chunkSize: The size of the chunk to be returned
        """
        self.buffSize = buffSize
        self.chunkSize = chunkSize
        
        self.buffer = np.zeros(self.buffSize, dtype=np.int16)
        self.wPos = 0
        self.rPos = 0
        self.size = 0
        self.lock = threading.Lock()


    def write(self, data):
        """
        Writes to the buffer at given location
        Arg:
            data: data to be written
        """
        with self.lock:
            if len(data) + self.size > self.buffSize:
                raise ValueError("Buffer overflow")

            endPos = self.wPos + len(data)
            if endPos <= self.buffSize:
                self.buffer[self.wPos:endPos] = data
            else:
                endSplit = self.buffSize - self.wPos
                self.buffer[self.wPos:] = data[:endSplit]
                self.buffer[:len(data) - endSplit] = data[endSplit:]
            
            self.wPos = (self.wPos + len(data)) % self.buffSize
            self.size += len(data)

    def read(self):
        """
        Reads data from the buffer at given location
        Return:
            Data at front of buffer.
        """
        with self.lock:
            if self.size < self.chunkSize:
                raise ValueError("Not enough data in buffer")

            endPos = self.rPos + self.chunkSize
            if endPos <= self.buffSize:
                data = self.buffer[self.rPos:endPos]
            else:
                endSplit = self.buffSize - self.rPos
                data = np.concatenate((self.buffer[self.rPos:], self.buffer[:self.chunkSize - endSplit]))
            
            self.rPos = (self.rPos + self.chunkSize) % self.buffSize
            self.size -= self.chunkSize

        return data
    