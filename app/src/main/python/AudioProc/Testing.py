
from AudioProc.AudioManager import AudioManager
from AudioProc.FileManager import FileManager

m = AudioManager(44100,16,1,3)

def accept_audio(audio):
    m.processChunk(audio)