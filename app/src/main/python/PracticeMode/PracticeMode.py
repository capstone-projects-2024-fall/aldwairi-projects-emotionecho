# implement practice mode functionality
import random
import os
import pandas as pd
from playsound import playsound

class PracticeMode:
    def __init__(self, data_path):
        self.audio_clips = self.get_metadata(data_path)

    def get_metadata(self, data_path):
        if not os.path.exists(data_path):
            raise FileNotFoundError(f"Metadata file not found at {data_path}")

        # Load metadata using pandas
        metadata = pd.read_csv(data_path)
        return [
            {
                "clip_id": row["FileName"],
                "audio_path": os.path.join(data_path, row["FileName"]),
                "emotion_label": row["Emotion"].lower()
            }
            for _, row in metadata.iterrows()
        ]


    # Pulls random audio clip from database
    def get_audio(self):
        randomaudio = random.choice(self.audio_clips)
        return randomaudio
    # play clip in UI
    def play_audio(self, audio_path):
        # filler
        print("Playing Random Audio...")
        playsound(audio_path)
    def get_user_emotion_input(self):
        # filler prompt for UI
        return input("Enter the emotion you hear: ").strip().lower()
    # Provides feedback for user (correct/incorrect)
    def provide_feedback(self, correct_emotion, user_selection):
        if user_selection == correct_emotion:
            print("Correct!")
        else:
            print(f"Incorrect. The correct emotion was '{correct_emotion}'.")
    # To be replaced, user will initiate this in UI
    def run_example(self):
        clip = self.get_audio()
        self.play_audio(clip["audio_path"])
        # Get the user's guessed emotion
        user_emotion = self.get_user_emotion_input()
        # Provide feedback
        self.provide_feedback(clip["emotion_label"], user_emotion)

# # Sample use
# practice_mode = PracticeMode()
# practice_mode.run_example()

# Example usage
# if __name__ == "__main__":
#     dataset_path = "app/src/main/python/PracticeMode/practicemode_dataset"  # Update with actual dataset path
#     practice_mode = PracticeMode(dataset_path)
#     practice_mode.run_example()

prac = PracticeMode("/data/data/com.temple.aldwairi_projects_emotionecho/files/chaquopy/AssetFinder/app/PracticeMode/practicemode_dataset/metadata.csv")