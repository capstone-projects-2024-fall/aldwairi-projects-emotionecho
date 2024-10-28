import os
import wave
from datetime import datetime
import numpy as np

class FileManager:
    
    def __init__(self, manager):
        self.manager = manager
        self.dirName = 'app/src/main/python/AudioProc/wav_files'
        self.countFiles = 0
    
    def makeDir(self):
        os.makedirs(self.dirName, exist_ok=True)

    def saveWav(self, data):
        self.countFiles += 1
        
        timestamp = datetime.now().strftime("%Y-%m-%d_%H:%M:%S")
        filename = self.dirName + f"/wav{self.countFiles}_{timestamp}"

        with wave.open(filename, 'wb') as wf:
            wf.setnchannels(self.manager.channelCnt)
            wf.setsampwidth(int(self.manager.bitDepth / 8))
            wf.setframerate(self.manager.sampleRate)
            wf.writeframes(np.array(data, dtype=np.int16).tobytes())
