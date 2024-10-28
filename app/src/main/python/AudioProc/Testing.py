import sounddevice as sd
import numpy as np
from AudioManager import AudioManager

def recordAudio(duration, sampleRate = 44100):
    print('recording')

    audioData = sd.rec(int(duration * sampleRate), samplerate=sampleRate, channels=1, dtype='int16')
    sd.wait()
    print('recording complete')
    return audioData


def processRecording(data):
    m = AudioManager(44100,16,1,2)

    for i in range(0, len(data), 512):
        chunk = data[i:i+512]
        print("processing chunk: " + str(int(i / 512 + 1)) + f" - {len(chunk)}")
        m.processChunk(chunk)


processRecording(recordAudio(5))