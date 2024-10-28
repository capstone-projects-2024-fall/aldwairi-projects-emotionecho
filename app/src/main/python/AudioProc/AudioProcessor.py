import numpy as np

MIN_SILENCE_DURATION = .3
SILENCE_THRESHOLD = 500

class AudioProcessor:

    def __init__(self, manager):
        self.manager = manager
        self.silenceCounter = 0

    def processChunk(self, chunk):        
        pcmData = np.frombuffer(chunk, dtype=np.int16)

        for sample in pcmData:
            #sample below silence threshold
            if abs(sample) < SILENCE_THRESHOLD:
                self.silence_counter += 1
            
            #sample not below silence threshold
            else:
                self.silenceCounter = 0
                self.manager.procDataBuff.append(sample)
            
                if len(self.manager.procDataBuff) >= self.manager.bufferSize:
                    self.maanger.fileManager.saveWav(sample)
                    self.manager.procDataBuff.clear()

            if self.silenceCounter >= MIN_SILENCE_DURATION * self.maanger.sampleRate and len(self.manager.procDataBuff) != 0:
                self.maanger.fileManager.saveWav(sample)
                self.manager.procDataBuff.clear()
                