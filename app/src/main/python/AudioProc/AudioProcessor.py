import numpy as np

MIN_SILENCE_DURATION = .3
SILENCE_THRESHOLD = 250

class AudioProcessor:

    def __init__(self, manager):
        self.manager = manager
        self.silenceCounter = 0
        self.buffer = []

    def processChunk(self, chunk):        
        pcmData = np.frombuffer(chunk, dtype=np.int16)

        for sample in pcmData:
            #sample below silence threshold
            if abs(sample) < SILENCE_THRESHOLD:
                #print(f'silence {sample}')
                self.silenceCounter += 1
            
            #sample not below silence threshold
            else:
                #print(f'speech {sample}')
                self.silenceCounter = 0
                self.buffer.append(sample)
            
                if len(self.buffer) >= self.manager.bufferSize:
                    #self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16).tobytes())
                    self.manager.fileManager.saveWav(self.buffer)
                    self.buffer.clear()

            if self.silenceCounter >= MIN_SILENCE_DURATION * self.manager.sampleRate and len(self.buffer) != 0:
                #self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16).tobytes())
                self.manager.fileManager.saveWav(self.buffer)
                self.buffer.clear()
                