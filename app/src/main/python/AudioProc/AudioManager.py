from AudioProc.AudioProcessor import AudioProcessor
from AudioProc.FileManager import FileManager
from time import sleep

class AudioManager:

    def __init__(self, sampleRate: int, bitDepth: int, channelCnt: int, durationMS: int):
        self.sampleRate = sampleRate
        self.bitDepth = bitDepth
        self.channelCnt = channelCnt
        self.durationMS = durationMS
        self.bufferSize = self.durationMS * self.sampleRate

        self.audioProc = AudioProcessor(self)
        self.fileManager = FileManager(self)
        self.fileManager.makeDir()

    def processChunk(self, chunk):
        self.audioProc.split(chunk)

    def getWavFile(self):
        return self.fileManager.getWav()

