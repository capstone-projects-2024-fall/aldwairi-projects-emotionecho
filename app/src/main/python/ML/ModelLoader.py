import tensorflow as tf
import os

class ModelLoader:
    def __init__(self, model_path: str):
        self.model_path = model_path

    def load_model(self):
        if not os.path.exists(self.model_path):
            raise FileNotFoundError(f"The model path {self.model_path} does not exist.")

        try:
            model = tf.keras.models.load_model(self.model_path)
            print("Model loaded successfully.")
            return model
        except Exception as e:
            raise IOError(f"Failed to load model: {e}")