import threading
import time
import torch.nn as nn
from AudioProc.AudioManager import AudioManager
from ML.ModelLoader import ModelLoader
from ML.EmotionClassifier import EmotionClassifier
model_path = "/data/data/com.temple.aldwairi_projects_emotionecho/files/chaquopy/AssetFinder/app/ML/EmotionEchoCNNModel2.pth"

sample_rate = 44100
bit_depth = 16
channel_count = 1
duration_ms = 3

emotions_list = []

class EmotionCNN(nn.Module):
    def __init__(self, input_size=40, num_classes=8):
        super(EmotionCNN, self).__init__()
        self.conv1 = nn.Conv1d(1, 64, kernel_size=5, padding=2)
        self.relu = nn.ReLU()
        self.dropout = nn.Dropout(0.2)
        self.flatten = nn.Flatten()
        self.fc = nn.Linear(input_size * 64, num_classes)

    def forward(self, x):
        x = self.conv1(x.permute(0, 2, 1))
        x = self.relu(x)
        x = self.dropout(x)
        x = self.flatten(x)
        x = self.fc(x)
        return x

audio_manager = AudioManager(sampleRate=sample_rate, bitDepth=bit_depth, channelCnt=channel_count, durationMS=duration_ms)
model_loader = ModelLoader(model_path)
model = model_loader.load_model(EmotionCNN)
emotion_classifier = EmotionClassifier(model)

#called first from Kotlin to initialize components
# def initialize_components():
#     global model, emotion_classifier, audio_manager
#     audio_manager = AudioManager(sampleRate=sample_rate, bitDepth=bit_depth, channelCnt=channel_count, durationMS=duration_ms)
#
#     model_loader = ModelLoader(model_path)
#     model = model_loader.load_model(EmotionCNN)
#
#     emotion_classifier = EmotionClassifier(model)

def testing(data):
    wav_file_path = audio_manager.processChunk(data)
    audio_features = emotion_classifier.extract_mfcc(wav_file_path)
    predicted_emotion = emotion_classifier.classify_audio(audio_features)
    print(f"Predicted Emotion: {predicted_emotion}")


# def process_audio_with_ml():
#     global emotions_list
#     emotions_list.clear()
#
#     def audio_processing_loop():
#         while True:
#             wav_file_path = audio_manager.getWavFile()
#
#             if wav_file_path is None:
#                 if emotions_list:
#                     return emotions_list
#                 time.sleep(1)
#                 continue
#
#             try:
#                 # Extract MFCC features and classify the audio
#                 audio_features = emotion_classifier.extract_mfcc(wav_file_path)
#                 predicted_emotion = emotion_classifier.classify_audio(audio_features)
#                 emotions_list.append(predicted_emotion)
#                 print(f"Predicted Emotion: {predicted_emotion}")
#
#                 audio_manager.deleteWavFile(wav_file_path)
#
#             except Exception as e:
#                 print(f"Error processing audio file {wav_file_path}: {e}")
#
#             time.sleep(1)
#
#     audio_thread = threading.Thread(target=audio_processing_loop)
#     audio_thread.start()
#     audio_thread.join()

