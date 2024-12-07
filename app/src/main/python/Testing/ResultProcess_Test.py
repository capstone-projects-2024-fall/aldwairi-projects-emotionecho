import unittest
import json
from resultProcess import get_emotions_percentage  # Replace <module_name> with your file name

class TestGetEmotionsPercentage(unittest.TestCase):

    def test_all_emotions_equal_distribution(self):
        """Test with a balanced distribution of all emotions."""
        input_emotions = json.dumps([
            "neutral", "calm", "happy", "sad", "angry", "fearful", "disgust", "surprised"
        ])
        expected_output = [12.5] * 8  # Each emotion appears once in the list
        self.assertEqual(get_emotions_percentage(input_emotions), expected_output)

    def test_single_emotion(self):
        """Test with a list containing only one type of emotion."""
        input_emotions = json.dumps(["happy", "happy", "happy", "happy"])
        expected_output = [0.0, 0.0, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0]  # Only 'happy' has 100%
        self.assertEqual(get_emotions_percentage(input_emotions), expected_output)

    def test_empty_input(self):
        """Test with an empty list of emotions."""
        input_emotions = json.dumps([])
        expected_output = [0.0] * 8  # No emotions should result in all zeros
        self.assertEqual(get_emotions_percentage(input_emotions), expected_output)

    def test_unbalanced_emotions(self):
        """Test with an unbalanced distribution of emotions."""
        input_emotions = json.dumps([
            "neutral", "neutral", "happy", "happy", "happy", "angry", "fearful"
        ])
        expected_output = [28.57, 0.0, 42.86, 0.0, 14.29, 14.29, 0.0, 0.0]  # Rounded percentages
        result = get_emotions_percentage(input_emotions)
        rounded_result = [round(val, 2) for val in result]
        self.assertEqual(rounded_result, expected_output)

    def test_missing_emotions(self):
        """Test with a list missing some emotions."""
        input_emotions = json.dumps(["neutral", "neutral", "calm", "calm"])
        expected_output = [50.0, 50.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]  # Only 'neutral' and 'calm' are present
        self.assertEqual(get_emotions_percentage(input_emotions), expected_output)

if __name__ == "__main__":
    unittest.main()
