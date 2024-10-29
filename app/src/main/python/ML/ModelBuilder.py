from IPython.display import Audio, display
from tensorflow.keras.layers import Conv1D, Activation, Dropout, Flatten, Dense
from tensorflow.keras.models import Sequential
from sklearn.model_selection import train_test_split
from tensorflow.keras.optimizers import RMSprop
import matplotlib.pyplot as plt
import librosa
import librosa.display
import matplotlib.pyplot as plt
import os
import numpy as np
import librosa
import joblib

# Define the path to your audio file
audio_file_path = "/Users/charlesmorgan/Desktop/MLComponent/RAVDESS_data/Actor_01/03-01-01-01-01-01-01.wav"


# Set the sample rate
sr = 22050

# Display the audio
display(Audio(filename=audio_file_path, rate=sr))

# Load the audio file
y, sr = librosa.load(audio_file_path, sr=sr)

# Plot the waveform
plt.figure(figsize=(14, 5))
librosa.display.waveshow(y, sr=sr)
plt.title('Waveform')
plt.show()

# Function to extract MFCC features
def extract_mfcc(file_path, sr=22050, n_mfcc=40):
    y, sr = librosa.load(file_path, sr=sr, res_type='kaiser_fast')
    mfccs = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=n_mfcc)
    mfccs_mean = np.mean(mfccs.T, axis=0)
    return mfccs_mean

# Path to dataset
dataset_path = 'C:/Users/USER HP/Downloads/RAVDESS/'

mfcc_features = []

# Iterate through the dataset and extract MFCC features
for root, dirs, files in os.walk(dataset_path):
    for file in files:
        if file.endswith('.wav'):
            try:
                file_path = os.path.join(root, file)
                mfccs = extract_mfcc(file_path)
                file_class = int(file[7:8]) - 1  # Extract class from filename
                mfcc_features.append((mfccs, file_class))
            except ValueError as err:
                print(f"Error processing file {file}: {err}")
                continue

# Check the extraction worked
print(f"Extracted {len(mfcc_features)} files")
for i in range(5):  # Print the first 5 entries
    print(mfcc_features[i])

# Unzipping the list and converting to NumPy arrays
x, y = zip(*mfcc_features)
x, y = np.asarray(x), np.asarray(y)
print(x.shape, y.shape)

# Define the directory path to save the arrays
SAVE_DIR_PATH = 'C:/Users/USER HP/Downloads/MFCC_Features'

# Check if the directory exists, if not, create it
if not os.path.isdir(SAVE_DIR_PATH):
    os.makedirs(SAVE_DIR_PATH)

# Save the arrays to files using joblib
joblib.dump(x, os.path.join(SAVE_DIR_PATH, 'x.joblib'))
joblib.dump(y, os.path.join(SAVE_DIR_PATH, 'y.joblib'))

# Define the CNN model
model = Sequential()

model.add(Conv1D(64, 5, padding='same', input_shape=(40, 1)))
model.add(Activation('relu'))
model.add(Dropout(0.2))
model.add(Flatten())
model.add(Dense(8))
model.add(Activation('softmax'))

model.summary()

# Split the data into training and test sets
X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.3, random_state=42)

X_train = np.expand_dims(X_train, axis=2)
X_test = np.expand_dims(X_test, axis=2)

print(X_train.shape, X_test.shape)

# Compile the model with the RMSProp optimizer
model.compile(optimizer=RMSprop(), loss='sparse_categorical_crossentropy', metrics=['accuracy'])

# Train the model for 70 epochs
history = model.fit(X_train, y_train, epochs=70, batch_size=32, validation_data=(X_test, y_test))

# Evaluate the model
y_pred = model.predict(X_test)

# Plot accuracy
plt.plot(history.history['accuracy'], label='accuracy')
plt.plot(history.history['val_accuracy'], label='val_accuracy')
plt.xlabel('Epoch')
plt.ylabel('Accuracy')
plt.ylim([0, 1])
plt.legend(loc='lower right')
plt.show()

# Plot loss
plt.plot(history.history['loss'], label='loss')
plt.plot(history.history['val_loss'], label='val_loss')
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.legend(loc='upper right')
plt.show()





def save_model_to_database(model, db_connection):
    """
    Saves the trained model to a database.
    
    Parameters:
    model: The trained machine learning model.
    db_connection: The database connection to save the model to.
    """
