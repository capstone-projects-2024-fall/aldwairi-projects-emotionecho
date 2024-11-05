from ModelLoader import ModelLoader
from EmotionClassifier import EmotionClassifier

class TestingMlBlock:
    def __init__(self, model_path, audio_file_path):
        self.model_path = model_path
        self.audio_file_path = audio_file_path

    def run_test(self):

        loader = ModelLoader(self.model_path)
        model = loader.load_model()

        classifier = EmotionClassifier(model)
        audio_features = classifier.extract_mfcc(self.audio_file_path)
        predicted_emotion = classifier.classify_audio(audio_features)

        print("Predicted Emotion:", predicted_emotion)

# Set up paths
model_path = 'ML/EmotionEchoCNNModel.h5'
audio_file_path = 'ML/03-01-05-01-01-02-02.wav'

# Run the test
if __name__ == "__main__":
    test_block = TestingMlBlock(model_path, audio_file_path)
    test_block.run_test()
