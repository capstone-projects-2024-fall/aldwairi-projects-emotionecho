import os

class ModelLoader:
    """
    ModelLoader class.

    Purpose: This class is responsible for loading the pre-trained model when needed.

    Attributes:
    - model_path: The path to the saved model file in our database.

    Methods:
    - __init__(model_path): Constructor that initializes the loader with the model's file path.
    - load_model(): Loads and returns the pre-trained model.
    """

    def __init__(self, model_path):
        """
        Constructor for ModelLoader.

        Args:
            model_path (str): The path to the saved model file.

        Pre-condition: The provided model_path must point to an existing model file.
        Post-condition: A ModelLoader instance is created with the provided model path.
        """
        self.model_path = model_path

    def load_model(self):
        """
        Loads and returns the pre-trained model.

        Pre-condition: The model file exists at the specified path.
        Post-condition: The model is loaded into memory and returned.

        Returns:
            object: The pre-trained model object.

        Raises:
            FileNotFoundError: If the file doesn't exist.
            IOError: If there is an error reading the model file.
        """
        if not os.path.exists(self.model_path):
            raise FileNotFoundError(f"Error: The file '{self.model_path}' doesn't exist.")
        
        try:
            # Replace the following with actual model loading logic
            # For example, using TensorFlow or PyTorch
            model = self._mock_load_model()  # Replace with actual model loading function
            return model
        except IOError:
            raise IOError(f"Error: File found but can't be opened at '{self.model_path}'.")
        except Exception as e:
            raise IOError(f"Error: An issue occurred while loading the model. Details: {str(e)}")

    def _mock_load_model(self):
        """
        Mock method to simulate model loading. Replace this with actual model loading logic.
        """
        # Simulate loading a model
        return "Loaded pre-trained model"

# Example usage:
# loader = ModelLoader("path_to_model.h5")
# model = loader.load_model()
