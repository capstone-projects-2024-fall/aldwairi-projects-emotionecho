import threading
import time
import json
from AudioManager import AudioManager
from ModelLoader import ModelLoader
from EmotionClassifier import EmotionClassifier
import matplotlib.pyplot as plt
import sounddevice as sd
from time import sleep

emotion_mapping = {
    "neutral": 0,
    "calm": 1,
    "happy": 2,
    "sad": 3,
    "angry": 4,
    "fearful": 5,
    "disgust": 6,
    "surprised": 7
}

sample_rate = 44100
bit_depth = 16
channel_count = 1
duration_ms = 3  # 3 or 3000 idk

audio_manager = AudioManager(sampleRate=sample_rate, bitDepth=bit_depth, channelCnt=channel_count, durationMS=duration_ms)

#load the model
model_path = '/Users/charlesmorgan/Desktop/EmotionEcho/EmotionEchoCNNModel.h5'
model_loader = ModelLoader(model_path)
model = model_loader.load_model()

#initialize EmotionClassifier with the loaded model
emotion_classifier = EmotionClassifier(model)

def recordAudio(duration, sampleRate = 44100): #testing
    print('recording')

    audioData = sd.rec(int(duration * sampleRate), samplerate=sampleRate, channels=1, dtype='int16')
    sd.wait()
    print('recording complete')
    return audioData


def processRecording(data): #testing
    audio_manager.fileManager.clearDir()
    #m.fileManager.saveWav(data)

    for i in range(0, len(data), 512):
        chunk = data[i:i+512]
        #print(f"processing chunk: {int(i / 512 + 1)} of {int(len(data)/512 + 1)}")
        audio_manager.processChunk(chunk)

processRecording(recordAudio(10))
#sleep(2)

emotions_list = [] #records the emotion for each .wav file for an individual audio clip

def process_audio_with_ml():
    global emotions_list

    while True: #thread
        #get .wav file
        wav_file_path = audio_manager.getWavFile()


        if wav_file_path is None:
            if emotions_list: #has emotions in them
                get_emotions_percentage(emotions_list)
                emotions_list = []  #make it empty
            time.sleep(1)
            continue

        try:
            #Process the audio file if found
            audio_features = emotion_classifier.extract_mfcc(wav_file_path)
            predicted_emotion = emotion_classifier.classify_audio(audio_features)
            emotions_list.append(predicted_emotion)

            #Delete the file after processing
            audio_manager.deleteWavFile(wav_file_path)

        except Exception as e:
            print("File Error")

        #Short sleep to prevent tight looping
        time.sleep(1)

def get_emotions_percentage(list_emotion):
    list_emotion = [emotion_mapping[emotion] for emotion in list_emotion]

    emotions_percentage = [0.0] * 8

    for num in list_emotion:
        emotions_percentage[num] += 1

    for index, count in enumerate(emotions_percentage):
        emotions_percentage[index] = (count / len(list_emotion)) * 100 if list_emotion else 0

    return emotions_percentage

audio_thread = threading.Thread(target=process_audio_with_ml)
audio_thread.start()

audio_thread.join()

    
