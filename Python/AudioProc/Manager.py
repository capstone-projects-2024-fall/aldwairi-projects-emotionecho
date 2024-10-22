from AudioStream import AudioStream
from SegmentStream import SegmentStream
from AudioProcessor import AudioProcessor
from AudioSegment import AudioSegment
import threading
import math

BUFFER_SIZE = 8192 # 8 KB buffer: ~185ms at 44.1kHz 16bit mono

class Manager:

    def __init__(self, sampleRatePerSec: int, bitDepth: int, channelCount: int, segDurationMS: int):
        self.stopEvnt = threading.Event()
        self.sampleRatePerSec = sampleRatePerSec
        self.bitDepth = bitDepth
        self.channelCount = channelCount
        self.segDurationMS = segDurationMS

        #Buffer size for AudioStream is ~185ms of data and Chunk size is ~20ms
        self.audioStream = AudioStream(BUFFER_SIZE, self.calcChunkSize())
        #Buffer size for SegmentStream is ~1s, 5 times buffer size, dependent on ML speed
        self.segmentStream = SegmentStream(5 * math.ceil(BUFFER_SIZE / self.calcChunkSize()))

    def startAudioProc(self):
        self.audioProcessor = AudioProcessor(self, self.stopEvnt)
        self.audioProcessor.start()

    def stopAudioProc(self):
        self.stopEvnt.set()
        self.audioProcessor.join()

    def addPCMData(self, data):
        self.audioStream.write(data)

    def getProcessedSegment(self) -> AudioSegment:
        return self.segmentStream.getSegment()
        
    def calcChunkSize(self) -> int:
        samples = self.sampleRatePerSec * self.segDurationMS * .001
        return samples * self.channelCount * (self.bitDepth / 8)
