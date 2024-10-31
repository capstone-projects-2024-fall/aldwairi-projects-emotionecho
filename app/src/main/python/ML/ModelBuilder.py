import os
import joblib
import numpy as np
from sklearn.model_selection import train_test_split
from tensorflow.keras.layers import Conv1D, Activation, Dropout, Flatten, Dense
from tensorflow.keras.models import Sequential
from tensorflow.keras.optimizers import RMSprop
import matplotlib.pyplot as plt


SAVE_DIR_PATH = '/Users/charlesmorgan/Desktop/MFCC_Features'

x = joblib.load(os.path.join(SAVE_DIR_PATH, 'x.joblib'))
y = joblib.load(os.path.join(SAVE_DIR_PATH, 'y.joblib'))

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


os.makedirs('/Users/charlesmorgan/Desktop/MLComponent', exist_ok=True)
try:
    model.save('/Users/charlesmorgan/Desktop/MLComponent/EmotionEchoCNNModel.h5')
    print("Model saved successfully.")
except Exception as e:
    print(f"An error occurred: {e}")

