import os
import torch

class ModelLoader:
    def __init__(self, model_path: str):
        self.model_path = model_path

    def load_model(self, model_class):
        if not os.path.exists(self.model_path):
            raise FileNotFoundError(f"The model path {self.model_path} does not exist.")

        try:
            model = model_class()

            model.load_state_dict(torch.load(self.model_path))
            model.eval()
            print("Model loaded successfully.")
            return model
        except Exception as e:
            raise IOError(f"Failed to load model: {e}")

