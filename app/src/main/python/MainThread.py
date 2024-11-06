import threading
import time
import json
from AudioManager import AudioManager
from ModelLoader import ModelLoader
from EmotionClassifier import EmotionClassifier
import matplotlib.pyplot as plt
import sounddevice as sd
from time import sleep

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

emotions_list = [] #records the emotion for each .wav file for an individual audio clip

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

def process_audio_with_ml():
    global emotions_list

    while True: #thread
        #get .wav file
        wav_file_path = audio_manager.getWavFile()

        '''
        #FOR TESTING IF YOU WANT THREAD TO STOP. Have to uncomment if statement in file manager.
        if wav_file_path is None:  
            if emotions_list:
                generate_emotion_pie_chart(emotions_list)
                emotions_list = []  
            print("No more files to process. Exiting.")
            break 
        '''


        try:
            #Process the audio file if found
            audio_features = emotion_classifier.extract_mfcc(wav_file_path)
            predicted_emotion = emotion_classifier.classify_audio(audio_features)
            emotions_list.append(predicted_emotion)
            print(f"Predicted Emotion: {predicted_emotion}")

            #Delete the file after processing
            audio_manager.deleteWavFile(wav_file_path)

        except Exception as e:
            print(f"Error processing audio file {wav_file_path}: {e}")

        #Short sleep to prevent tight looping
        time.sleep(1)


#for display      
def get_emotions_percentage(list_emotion):
    emotions_percentage = [0.0] * 8
    emotion_map = { "neutral": 0, "calm": 1,  "happy": 2, "sad": 3, "angry": 4, "fearful": 5, "disgust": 6,  "surprised": 7}
    for emotion in list_emotion:
        if emotion in emotion_map: 
            index = emotion_map[emotion]
            emotions_percentage[index] += 1

    total = len(list_emotion)
    if total > 0:
        emotions_percentage = [count / total * 100 for count in emotions_percentage]

    return emotions_percentage

#for display
def generate_emotion_pie_chart(emotions_list):
    percentages = get_emotions_percentage(emotions_list)
    labels = ["Neutral", "Calm", "Happy", "Sad", "Angry", "Fearful", "Disgust", "Surprised"]
    
    plt.figure(figsize=(8, 6))
    plt.pie(percentages, labels=labels, autopct='%1.1f%%', startangle=140)
    plt.title("Emotion Distribution for Audio Clip")
    plt.show()
    
#process_audio_with_ml() #for testing


audio_thread = threading.Thread(target=process_audio_with_ml)
audio_thread.start()
 
audio_thread.join()

    
