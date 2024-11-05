import os
import numpy as np
import librosa

# Function to extract MFCC features
def extract_mfcc(file_path, sr=22050, n_mfcc=40):
    y, sr = librosa.load(file_path, sr=sr, res_type='kaiser_fast')
    mfccs = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=n_mfcc)
    mfccs_mean = np.mean(mfccs.T, axis=0)
    return mfccs_mean

import kagglehub

dataset_path = kagglehub.dataset_download("uwrfkaggler/ravdess-emotional-speech-audio")

mfcc_features = []

# Iterate through the dataset and extract MFCC features
for root, dirs, files in os.walk(dataset_path):
    for file in files:
        if file.endswith('.wav') and file.startswith('03-01'):
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


import joblib

# Unzipping the list and converting to NumPy arrays
x, y = zip(*mfcc_features)
x, y = np.asarray(x), np.asarray(y)
print(x.shape, y.shape)

# Define the directory path to save the arrays
SAVE_DIR_PATH = '/Users/charlesmorgan/Desktop/MFCC_Features'

# Check if the directory exists, if not, create it
if not os.path.isdir(SAVE_DIR_PATH):
    os.makedirs(SAVE_DIR_PATH)


# Save the arrays to files using joblib
joblib.dump(x, os.path.join(SAVE_DIR_PATH, 'x.joblib'))
joblib.dump(y, os.path.join(SAVE_DIR_PATH, 'y.joblib'))
