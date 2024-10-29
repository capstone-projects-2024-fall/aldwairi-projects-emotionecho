import os
import wave
from datetime import datetime
import numpy as np
import sounddevice as sd
from time import sleep

class FileManager:
    
    def __init__(self, manager):
        self.manager = manager
        self.dirName = 'app/src/main/python/AudioProc/wav_files'
        self.countFiles = 0
        self.filePaths = []
    
    def makeDir(self):
        os.makedirs(self.dirName, exist_ok=True)

    def saveWav(self, data):
        self.countFiles += 1
        
        timestamp = datetime.now().strftime("%Y-%m-%d_%H:%M:%S")
        filename = self.dirName + f"/wav{self.countFiles}_{timestamp}.wav"
        self.filePaths.append(filename)

        with wave.open(filename, 'wb') as wf:
            wf.setnchannels(self.manager.channelCnt)
            wf.setsampwidth(int(self.manager.bitDepth / 8))
            wf.setframerate(self.manager.sampleRate)
            wf.writeframes(np.array(data, dtype=np.int16).tobytes())

    def clearDir(self):
        for filename in os.listdir(self.dirName):
            filePath = os.path.join(self.dirName, filename)
            if os.path.isfile(filePath):
                os.remove(filePath)

    def getWav(self):
        while self.countFiles == 0:
            sleep(1)

        path = self.filePaths[0]

        self.countFiles -= 1
        self.filePaths.pop(0)

        if os.path.isfile(filePath):
            os.remove(filePath)

        return path