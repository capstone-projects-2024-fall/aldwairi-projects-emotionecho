import AudioStream
import SegmentStream
import AudioReceiver
import AudioProcessor
import threading

class Manager:

    def __init__(self):
        self.audioStream = AudioStream()
        self.segmentStream = SegmentStream()
        self.stopEvnt = threading.Event()

    def startAudioProc(self):
        self.audioReceiver = AudioReceiver()
        self.audioProcessor = AudioProcessor()

        self.audioReceiver.start()
        self.audioProcessor.start()

    def stopAudioProc(self):
        self.stopEvnt.set()

        self.audioReceiver.join()
        self.audioProcessor.join()

