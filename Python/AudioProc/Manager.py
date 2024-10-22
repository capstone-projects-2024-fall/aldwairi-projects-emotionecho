from AudioStream import AudioStream
from AudioProcessor import AudioProcessor
from AudioSegment import AudioSegment
import threading

BUFFER_SIZE = 8192 # 8 KB buffer: ~185ms at 44.1kHz 16bit mono

class Manager:

    def __init__(self, sampleRatePerSec: int, bitDepth: int, channelCount: int, segDurationMS: int):
        self.stopEvnt = threading.Event()
        self.sampleRatePerSec = sampleRatePerSec
        self.bitDepth = bitDepth
        self.channelCount = channelCount
        self.segDurationMS = segDurationMS

        self.audioStream = AudioStream(BUFFER_SIZE, self.calcChunkSize())
        #self.segmentStream = SegmentStream

    def startAudioProc(self):
        self.audioProcessor = AudioProcessor(self, self.stopEvnt)
        self.audioProcessor.start()

    def stopAudioProc(self):
        self.stopEvnt.set()
        self.audioProcessor.join()

    def addPCMData(self):
        pass

    def getProcessedSegment(self) -> AudioSegment:
        #This method will need updating
        if self.segmentStream.isEmpty():
            return None
        else:
            return self.segmentStream.get()
        
    def calcChunkSize(self) -> int:
        samples = self.sampleRatePerSec * self.segDurationMS * .001
        return samples * self.channelCount * (self.bitDepth / 8)
