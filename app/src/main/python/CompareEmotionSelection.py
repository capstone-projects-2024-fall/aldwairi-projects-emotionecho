import random

class CompareEmotionSelection:
    EmotionOptions = ["Neutral", "Calm", "Happy", "Sad", "Angry", "Fearful", "Disgust"]

    def __init__(self, user_selection):
        self.user_selection = user_selection
        self.model_prediction = self.model_output()

    # Function for the model output
    def model_output(self):
        # Placeholder for model output
        return random.choice(self.EmotionOptions)

    # Method to compare user-selected emotion with model-prediction
    def compare_emotions(self):
        if self.user_selection == self.model_prediction:
            return "Correct!"
        else:
            return f"Not Quite: Your Selection - {self.user_selection}, Predicted Emotion - {self.model_prediction}"

    # Method to update model's prediction
    def set_model_prediction(self, detected_emotion):
        self.model_prediction = detected_emotion

    # # Getters for user-selected and model-predicted emotions (if needed)
    # def get_user_selection(self):
    #     return self.user_selection
    #
    # def get_model_prediction(self):
    #     return self.model_prediction


# Example where user selects "fearful" as predicted emotion
if __name__ == "__main__":
    check = CompareEmotionSelection("fearful")
    result = check.compare_emotions()
    print(result)