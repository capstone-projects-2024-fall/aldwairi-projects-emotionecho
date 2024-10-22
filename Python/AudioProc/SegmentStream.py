from AudioSegment import AudioSegment
from collections import deque

class SegmentStream:
    """
    SegmentStream class stores AudioSegment objects in buffer
    """

    def __init__(self, buffSize: int):
        """
        Constructor for SegmentStream class
        Arg:
            buffSize: The size of buffer
        """
        self.buffSize = buffSize
        self.buffer = deque(maxlen=self.buffSize)
    
    def addSegment(self, segment: AudioSegment):
        """
        Appends segment to end of buffer
        Arg:
            segment: Segment to store
        """
        self.buffer.append(segment)

    def getSegment(self) -> AudioSegment:
        """
        Gets segment from buffer
        Return:
            AudioSegment object
        """
        if self.buffer:
            return self.buffer.popleft()
        else:
            return None
        