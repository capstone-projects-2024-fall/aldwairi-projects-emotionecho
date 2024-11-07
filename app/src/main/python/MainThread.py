import threading
import time
from AudioManager import AudioManager
from ModelLoader import ModelLoader
from EmotionClassifier import EmotionClassifier

#model_path = '/Users/charlesmorgan/Desktop/EmotionEcho/EmotionEchoCNNModel.h5'
model_path = 'app/src/main/python/ML/EmotionEchoCNNModel.h5'
sample_rate = 44100
bit_depth = 16
channel_count = 1
duration_ms = 3

emotions_list = []

#called first from Kotlin
def initialize_components():
    global model, emotion_classifier, audio_manager
    audio_manager = AudioManager(sampleRate=sample_rate, bitDepth=bit_depth, channelCnt=channel_count, durationMS=duration_ms)
    model_loader = ModelLoader(model_path)
    model = model_loader.load_model()
    emotion_classifier = EmotionClassifier(model)


def process_audio_with_ml():
    global emotions_list
    emotions_list.clear()

    def audio_processing_loop():
        while True:
            wav_file_path = audio_manager.getWavFile()

            if wav_file_path is None:
                if emotions_list:
                    return emotions_list
                time.sleep(1)
                continue

            try:
                audio_features = emotion_classifier.extract_mfcc(wav_file_path)
                predicted_emotion = emotion_classifier.classify_audio(audio_features)
                emotions_list.append(predicted_emotion)

                audio_manager.deleteWavFile(wav_file_path)

            except Exception as e:
                print(f"Error processing audio file {wav_file_path}: {e}")

            time.sleep(1)

    audio_thread = threading.Thread(target=audio_processing_loop)
    audio_thread.start()
    audio_thread.join()

    
