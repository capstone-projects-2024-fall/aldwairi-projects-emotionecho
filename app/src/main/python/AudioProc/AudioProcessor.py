import numpy as np

MIN_SILENCE_DURATION = .3
SILENCE_THRESHOLD = 300

class AudioProcessor:

    def __init__(self, manager):
        self.manager = manager
        self.silenceCounter = 0
        self.buffer = []

    def processChunk(self, chunk):
        # Convert the incoming chunk to PCM data
        pcmData = np.frombuffer(chunk, dtype=np.int16)

        for sample in pcmData:
            # Sample below silence threshold
            if abs(sample) < SILENCE_THRESHOLD:
                self.silenceCounter += 1
            else:
                # Sample is not silent
                if self.silenceCounter >= MIN_SILENCE_DURATION * self.manager.sampleRate and len(self.buffer) != 0:
                    # Save the buffered data if the silence duration threshold is reached
                    self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
                    self.buffer.clear()

                # Reset the silence counter and append the sample to the buffer
                self.silenceCounter = 0
                self.buffer.append(sample)

                # Save the buffer if it reaches the specified size
                if len(self.buffer) >= self.manager.bufferSize:
                    self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
                    self.buffer.clear()

        # Handle the case where the chunk ends and there's remaining non-silent data
        if len(self.buffer) > 0 and self.silenceCounter >= MIN_SILENCE_DURATION * self.manager.sampleRate:
            self.manager.fileManager.saveWav(np.array(self.buffer, dtype=np.int16))
            self.buffer.clear()
                