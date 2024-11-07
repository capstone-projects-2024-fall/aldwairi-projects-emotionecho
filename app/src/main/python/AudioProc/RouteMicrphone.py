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
    
    # Test 
def test():
    mic_router = RouteMicrophone(None)
    mic_router.mic_options()
    
    # Ask the user to select a device
    try:
        mic_id = int(input("Enter the ID of the microphone you want to use: "))
        mic_router.set_device(mic_id)
    except ValueError:
        print("Please enter a valid integer for the device ID.")
    
    # Confirm the selected microphone
    selected_device = mic_router.get_selected_device()
    if selected_device is not None:
        print(f"Microphone with ID {selected_device} is currently selected.")
    else:
        print("No microphone selected.")

test()