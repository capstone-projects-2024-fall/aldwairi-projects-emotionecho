import unittest
from unittest.mock import patch, MagicMock
import os
import torch
from ML.ModelLoader import ModelLoader  # Replace with the actual import path

class TestModelLoader(unittest.TestCase):
    
    @patch("os.path.exists")
    def test_model_path_not_exist(self, mock_exists):
        # Test case where the model path does not exist
        mock_exists.return_value = False
        model_loader = ModelLoader("invalid_path/model.pth")
        
        with self.assertRaises(FileNotFoundError):
            model_loader.load_model(MagicMock())  # Passing a dummy model class
    
    @patch("os.path.exists")
    @patch("torch.load")
    def test_load_model_failed(self, mock_torch_load, mock_exists):
        # Test case where loading the model fails
        mock_exists.return_value = True
        mock_torch_load.side_effect = Exception("Failed to load model weights.")
        
        model_loader = ModelLoader("ML/EmotionEchoCNNModel2.pth")
        
        with self.assertRaises(IOError):
            model_loader.load_model(MagicMock())  # Passing a dummy model class
    
    @patch("os.path.exists")
    @patch("torch.load")
    def test_load_model_success(self, mock_torch_load, mock_exists):
        # Test case where the model is loaded successfully
        mock_exists.return_value = True
        mock_torch_load.return_value = MagicMock()  # Simulate successful model load
        
        model_loader = ModelLoader("ML/EmotionEchoCNNModel2.pth")
        
        model_class_mock = MagicMock()
        model_class_mock.return_value = MagicMock()  # Simulate a dummy model
        
        model = model_loader.load_model(model_class_mock)
        
        model_class_mock.assert_called_once()  # Ensure the model class was initialized
        mock_torch_load.assert_called_once_with("ML/EmotionEchoCNNModel2.pth")  # Ensure model loading was called
        self.assertTrue(model)  # Ensure the model is returned
        
    @patch("os.path.exists")
    def test_model_loader_invalid_model_class(self, mock_exists):
        # Test case where an invalid model class is passed (mocking a class that doesn't have load_state_dict method)
        mock_exists.return_value = True
        model_loader = ModelLoader("ML/EmotionEchoCNNModel2.pth")
        
        class InvalidModel:
            pass  # Doesn't have load_state_dict
        
        with self.assertRaises(OSError):
            model_loader.load_model(InvalidModel)  # Pass invalid model class

if __name__ == "__main__":
    unittest.main()
