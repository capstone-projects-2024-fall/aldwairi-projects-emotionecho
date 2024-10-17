import numpy as np
import matplotlib as plt
import AudioSegment


class AudioProcessor:
    """
    Creates and prepares AudioSegment for ML algorithm.
    """
    
    def cleanSeg(seg: AudioSegment) -> AudioSegment:
        """
        Removes segments that don't provide viable raw PCM data
        Args:
            seg: AudioSegment
        Returns:
            AudioSegment if viable, else None
        """
        pass

    def fourierTrans(seg: AudioSegment) -> AudioSegment:
        """
        Performs Fast Fourier Transform (FFT) on raw PCM audio data to convert data
        into the frequency domain
        Args:
            seg: AudioSegment
        Returns:
            AudioSegment with applied FFT
        """
        pass

    def store(seg: AudioSegment) -> None:
        """
        Stores processed segment in SegmentBuffer for use by the ML algorithm.
        Args:
            seg: AudioSegment
        Returns:
            None
        """
        pass

    
