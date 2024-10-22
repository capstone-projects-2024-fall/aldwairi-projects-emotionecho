from AudioSegment import AudioSegment
from collections import deque

class SegmentStream:

    def __init__(self, buffSize: int):
        self.buffSize = buffSize
        self.buffer = deque(maxlen=self.buffSize)
    
    def addSegment(self, segment: AudioSegment):
        self.buffer.append(segment)

    def getSegment(self) -> AudioSegment:
        if self.buffer:
            return self.buffer.popleft()
        else:
            return None
        