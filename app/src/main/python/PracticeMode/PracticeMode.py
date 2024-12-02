# implement practice mode functionality
import random
class PracticeMode:
    def __init__(self):
        self.audio_clips = [
            ## DATABASE audio clips
        ]
    # Pulls random audio clip from database
    def get_audio(self):
        return random.choice(self.audio_clips)
    # play clip in UI
    def play_audio(self, audio_path):
        # filler
        return
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
# Sample use
practice_mode = PracticeMode()
practice_mode.run_example()