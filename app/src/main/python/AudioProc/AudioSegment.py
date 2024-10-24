

class AudioSegment:
    """
    Defines an AudioSegment object
    """

    def __init__(self, seg_id: int, byte_arr: bytearray, duration: float):
        """
        AudioSegment Constructor
        """
        self.seg_id = seg_id
        self.byte_arr = byte_arr
        self.duration = duration
    
    def getData(self) -> bytearray:
        """
        Returns:
            bytearray data stored in the segment
        """
        return self.byte_arr
    

    
