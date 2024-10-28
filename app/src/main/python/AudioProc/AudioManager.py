from AudioProcessor import AudioProcessor
from FileManager import FileManager

class AudioManager:

    def __init__(self, sampleRate: int, bitDepth: int, channelCnt: int, durationMS: int):
        self.sampleRate = sampleRate
        self.bitDepth = bitDepth
        self.channelCnt = channelCnt
        self.durationMS = durationMS
        self.bufferSize = self.durationMS * self.sampleRate

        self.procDataBuff = []

        self.audioProc = AudioProcessor(self)
        self.fileManager = FileManager(self)
        self.fileManager.makeDir()

    def processChunk(self, chunk):
        self.audioProc.processChunk(chunk)
    

m = AudioManager(44100, 16, 1, 2000)
m.processChunk(1)