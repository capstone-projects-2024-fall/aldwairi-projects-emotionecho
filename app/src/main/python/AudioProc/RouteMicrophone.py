import sounddevice as sd

# Class to route microphone to user selection (internal/external)
class RouteMicrophone:

    def __init__(self, manager):
        self.mic_id = None

    # Lists available microphones 
    def mic_options(self):
        devices = sd.query_devices()
        print("Available audio input devices:")
        for idx, device in enumerate(devices):
            if device['max_input_channels'] > 0:
                print(f"ID {idx}: {device['name']}")

    # Sets perferred device (mic)
    def set_device(self, mic_id):
        try:
            device_info = sd.query_devices(mic_id)
            if device_info['max_input_channels'] > 0:
                self.mic_id = mic_id
                print(f"Selected device ID {mic_id}: {device_info['name']}")
            else:
                print("Selected device does not support audio input.")
        except ValueError:
            print("Invalid device ID. Please select a valid input device.")

    # Returns current device in use 
    def get_selected_device(self):
        return self.mic_id