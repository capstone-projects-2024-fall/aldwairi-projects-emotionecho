import numpy as np
import threading

class AudioStream:
    
    def __init__(self, buffSize: int, chunkSize: int):
        self.buffSize = buffSize
        self.chunkSize = chunkSize
        
        self.buffer = np.zeros(self.buffSize, dtype=np.int16)
        self.wPos = 0
        self.rPos = 0
        self.size = 0
        self.lock = threading.Lock()


    def write(self, data):
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
    