#!/usr/bin/env python
# coding: utf-8

# In[3]:


import os
import numpy as np
import librosa
from tensorflow.keras.layers import Conv1D, Activation, Dropout, Flatten, Dense
from tensorflow.keras.models import Sequential
from tensorflow.keras.optimizers import Adam
from sklearn.model_selection import train_test_split
from tensorflow.keras.callbacks import EarlyStopping
import kagglehub

dataset_path = kagglehub.dataset_download("uwrfkaggler/ravdess-emotional-speech-audio")


def extract_mel_spectrogram(input_data, sr=22050, n_mels=40, max_pad_len=100, from_file=True):
    if from_file:
        y, sr = librosa.load(input_data, sr=sr, res_type='kaiser_fast')
    else:
        y = input_data  
    mel = librosa.feature.melspectrogram(y=y, sr=sr, n_mels=n_mels)
    mel_db = librosa.power_to_db(mel, ref=np.max)
    if mel_db.shape[1] < max_pad_len:
        pad_width = max_pad_len - mel_db.shape[1]
        mel_db = np.pad(mel_db, ((0, 0), (0, pad_width)), mode='constant')
    else:
        mel_db = mel_db[:, :max_pad_len]
    return mel_db.T

def add_noise(y, noise_factor=0.005):
    noise = np.random.randn(len(y))
    return y + noise_factor * noise

def shift_time(y, shift_max=5, shift_direction='both'):
    shift = np.random.randint(-shift_max, shift_max)
    if shift_direction == 'right':
        shift = abs(shift)
    elif shift_direction == 'left':
        shift = -abs(shift)
    y_shifted = np.roll(y, shift)
    return y_shifted

mfcc_features = []
for root, dirs, files in os.walk(dataset_path):
    for file in files:
        if file.endswith('.wav') and file.startswith('03-01'):
            try:
                file_path = os.path.join(root, file)
                
                mel_spect = extract_mel_spectrogram(file_path, from_file=True)
                file_class = int(file[7:8]) - 1  
                mfcc_features.append((mel_spect, file_class))
                
                y, sr = librosa.load(file_path, sr=22050, res_type='kaiser_fast')
                for _ in range(2):
                    noisy_y = add_noise(y)
                    noisy_mel_spect = extract_mel_spectrogram(noisy_y, from_file=False)
                    mfcc_features.append((noisy_mel_spect, file_class))
                    
                    shifted_y = shift_time(y)
                    shifted_mel_spect = extract_mel_spectrogram(shifted_y, from_file=False)
                    mfcc_features.append((shifted_mel_spect, file_class))
                    
            except ValueError as err:
                print(f"Error processing file {file}: {err}")
                continue


x, y = zip(*mfcc_features)
x, y = np.asarray(x), np.asarray(y)
x = np.expand_dims(x, -1)
print(x.shape, y.shape)

X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.3, random_state=42)

model = Sequential()
model.add(Conv1D(64, 5, padding='same', input_shape=(X_train.shape[1], X_train.shape[2])))
model.add(Activation('relu'))
model.add(Dropout(0.2))
model.add(Conv1D(128, 3, padding='same'))
model.add(Activation('relu'))
model.add(Dropout(0.2))
model.add(Flatten())
model.add(Dense(8, activation='softmax'))

model.summary()

model.compile(optimizer=Adam(learning_rate=0.0001), loss='sparse_categorical_crossentropy', metrics=['accuracy'])

early_stopping = EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)

history = model.fit(X_train, y_train, epochs=70, batch_size=32, validation_data=(X_test, y_test), callbacks=[early_stopping])

plt.figure(figsize=(12, 4))
plt.subplot(1, 2, 1)
plt.plot(history.history['accuracy'], label='Training Accuracy')
plt.plot(history.history['val_accuracy'], label='Validation Accuracy')
plt.xlabel('Epoch')
plt.ylabel('Accuracy')
plt.legend()
plt.title('Training and Validation Accuracy')

plt.subplot(1, 2, 2)
plt.plot(history.history['loss'], label='Training Loss')
plt.plot(history.history['val_loss'], label='Validation Loss')
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.legend()
plt.title('Training and Validation Loss')

plt.show()

test_loss, test_accuracy = model.evaluate(X_test, y_test, verbose=0)
print(f"Test Accuracy: {test_accuracy * 100:.2f}%")

os.makedirs('/Users/charlesmorgan/Desktop/MLComponent', exist_ok=True)
try:
    model.save('/Users/charlesmorgan/Desktop/MLComponent/EmotionEchoCNNModel.h5')
    print("Model saved successfully.")
except Exception as e:
    print(f"An error occurred: {e}")


# In[ ]:




