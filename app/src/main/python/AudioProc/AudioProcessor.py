import numpy as np

MIN_SILENCE_DURATION = .3
SILENCE_THRESHOLD = 250
MIN_BUFFER_SIZE = 2

class AudioProcessor:

    def __init__(self, manager):
        self.manager = manager
        self.silenceCounter = 0
        self.buffer = []

    def processChunk(self, chunk):
        # Convert the incoming chunk to PCM data
        pcmData = np.frombuffer(chunk, dtype=np.int16)

        for sample in pcmData:
            #Sample is below threshold
            if abs(sample) < SILENCE_THRESHOLD:
                self.silenceCounter += 1

                if self.silenceCounter == self.manager.sampleRate * MIN_SILENCE_DURATION:
                    if len(self.buffer) >= MIN_BUFFER_SIZE * self.manager.sampleRate:
                        self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
                    self.buffer.clear()
                
                elif self.silenceCounter < self.manager.sampleRate * MIN_SILENCE_DURATION:
                    self.buffer.append(sample)
                    if len(self.buffer) == self.manager.bufferSize:
                        self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
                        self.buffer.clear()


            #Sample is above threshold
            else:

                if self.silenceCounter < self.manager.sampleRate * MIN_SILENCE_DURATION and self.silenceCounter != 0:
                    self.buffer = self.buffer[:len(self.buffer) - self.silenceCounter]

                self.silenceCounter = 0
                self.buffer.append(sample)
                if len(self.buffer) == self.manager.bufferSize:
                    self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
                    self.buffer.clear()

            

        #     if abs(sample) < SILENCE_THRESHOLD:
        #         self.silenceCounter += 1
        #     else:
        #         # Sample is not silent
        #         if self.silenceCounter >= MIN_SILENCE_DURATION * self.manager.sampleRate and len(self.buffer) != 0:
        #             # Save the buffered data if the silence duration threshold is reached
        #             self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
        #             self.buffer.clear()

        #         # Reset the silence counter and append the sample to the buffer
        #         self.silenceCounter = 0
        #         self.buffer.append(sample)

        #         # Save the buffer if it reaches the specified size
        #         if len(self.buffer) >= self.manager.bufferSize:
        #             self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
        #             self.buffer.clear()

        # # Handle the case where the chunk ends and there's remaining non-silent data
        # if len(self.buffer) > 0 and self.silenceCounter >= MIN_SILENCE_DURATION * self.manager.sampleRate:
        #     self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
        #     self.buffer.clear()
                