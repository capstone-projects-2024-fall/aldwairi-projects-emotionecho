import numpy as np
import librosa


class EmotionClassifier:
    def __init__(self, model):
        self.model = model
        self.emotion_mapping = {
            0: "neutral",
            1: "calm",
            2: "happy",
            3: "sad",
            4: "angry",
            5: "fearful",
            6: "disgust",
            7: "surprised"
        }

    def extract_mfcc(self, file_path, sr=22050, n_mfcc=40):
        y, sr = librosa.load(file_path, sr=sr, res_type='kaiser_fast')
        mfccs = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=n_mfcc)
        mfccs_mean = np.mean(mfccs.T, axis=0)
        return mfccs_mean

    def classify_audio(self, audio_features):
        if not isinstance(audio_features, np.ndarray) or audio_features.shape[0] != 40:
            raise ValueError("Invalid audio features provided. Expected an array with 40 MFCC features.")
        
        audio_features = audio_features.reshape(1, 40, 1)
        prediction = self.model.predict(audio_features)
        predicted_class = np.argmax(prediction)
        return self.emotion_mapping[predicted_class]
