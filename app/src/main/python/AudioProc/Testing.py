import sounddevice as sd
import wave
import numpy as np
from AudioManager import AudioManager
from FileManager import FileManager

def recordAudio(duration, sampleRate = 44100):
    print('recording')

    audioData = sd.rec(int(duration * sampleRate), samplerate=sampleRate, channels=1, dtype='int16')
    sd.wait()
    print('recording complete')
    return audioData


def processRecording(data):
    m = AudioManager(44100,16,1,2)
    m.fileManager.clearDir()
    m.fileManager.saveWav(data)

    for i in range(0, len(data), 512):
        chunk = data[i:i+512]
        print(f"processing chunk: {int(i / 512 + 1)} of {int(len(data)/512 + 1)}")
        m.processChunk(chunk)

def playWav(filename):
    with wave.open(filename, 'rb') as wf:
        data = wf.readframes(wf.getnframes())
        audioData = np.frombuffer(data, dtype=np.int16)
        sd.play(audioData, samplerate=wf.getframerate())
        sd.wait()

processRecording(recordAudio(5))
#playWav('app/src/main/python/AudioProc/wav_files/wav1_2024-10-28_15:13:17.wav')