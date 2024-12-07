import unittest
import numpy as np
from unittest.mock import patch, MagicMock
from scipy.io import wavfile
import torch

from ML.EmotionClassifier import EmotionClassifier

# Test the MFCC extraction method
class TestEmotionClassifier(unittest.TestCase):

    @patch('scipy.io.wavfile.read')
    def test_extract_mfcc(self, mock_wavfile_read):
        # Mock audio data as if it was read from a WAV file
        sample_rate = 22050
        signal = np.random.randn(sample_rate * 3)  # 3 seconds of audio
        mock_wavfile_read.return_value = (sample_rate, signal)

        model = MagicMock()  # Mock the model, not used in MFCC extraction
        classifier = EmotionClassifier(model)

        # Extract MFCC features
        mfcc_features = classifier.extract_mfcc('mock_audio.wav')

        # Assert that the MFCC features are of the expected shape (40,)
        self.assertEqual(mfcc_features.shape, (40,))
        self.assertIsInstance(mfcc_features, np.ndarray)

    @patch('torch.Tensor')  # Patching torch.Tensor to mock its behavior
    def test_classify_audio(self, mock_torch_tensor):
        # Test classify_audio with a mocked model and random MFCC features
        model = MagicMock()
        classifier = EmotionClassifier(model)

        # Create mock MFCC features (e.g., from a real audio file)
        mock_features = np.random.rand(40)

        # Create a mock tensor output (this simulates the prediction)
        mock_output = torch.tensor([[0.1, 0.2, 0.3, 0.1, 0.05, 0.1, 0.1, 0.05]])

        # Mock the model to return the mock tensor as output
        model.return_value = mock_output

        # Classify the features
        predicted_emotion = classifier.classify_audio(mock_features)

        # Assert the predicted emotion is one of the valid emotions
        self.assertIn(predicted_emotion, classifier.emotion_mapping.values())

        # Check that the model was called correctly
        model.eval.assert_called_once()

    def test_classify_audio_invalid_input(self):
        model = MagicMock()
        classifier = EmotionClassifier(model)

        # Invalid input: wrong shape of audio features (e.g., not 40)
        with self.assertRaises(ValueError):
            classifier.classify_audio(np.random.rand(30))  # Less than 40 MFCC features

        with self.assertRaises(ValueError):
            classifier.classify_audio(np.random.rand(50))  # More than 40 MFCC features


if __name__ == '__main__':
    unittest.main()
