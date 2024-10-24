from AudioStream import AudioStream
from SegmentStream import SegmentStream
from AudioProcessor import AudioProcessor
from AudioSegment import AudioSegment
import threading
import math

BUFFER_SIZE = 8192 # 8 KB buffer: ~185ms at 44.1kHz 16bit mono

class Manager:
    """
    Creates and manages instances of AudioProc objects.
    """

    def __init__(self, sampleRatePerSec: int, bitDepth: int, channelCount: int, segDurationMS: int):
        """
        Constructor for Manager class
        Args:
            sampleRatePerSec: sample rate of mic, Ex. 44.1kHz = 44,100
            bitDepth: Bit Depth of mic, Ex. 16, 24, 32
            channelCount: Channel Count, Ex. mono = 1, stereo = 2
            segDurationMS: Segment duration in ms
        """
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
        """
        Starts Audio Processing Thread
        """
        self.audioProcessor = AudioProcessor(self, self.stopEvnt)
        self.audioProcessor.start()

    def stopAudioProc(self):
        """
        Kills Audio Processing Thread
        """
        self.stopEvnt.set()
        self.audioProcessor.join()

    def addPCMData(self, data):
        """
        Method for Kotlin implementation, adds kotlin microphone data to AudioStream circular buffer.
        Kotlin local buffer size is smallest possible.
        """
        self.audioStream.write(data)

    def getProcessedSegment(self) -> AudioSegment:
        """
        Method for ML implementation, gets processed AudioSegment from SegmentStream
        """
        return self.segmentStream.getSegment()
        
    def calcChunkSize(self) -> int:
        """
        Calculates the chunk size in bytes given the microphone settings and requested duration.
        """
        samples = self.sampleRatePerSec * self.segDurationMS * .001
        return samples * self.channelCount * (self.bitDepth / 8)
