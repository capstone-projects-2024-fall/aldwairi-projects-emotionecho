import os
import joblib
import numpy as np
from sklearn.model_selection import train_test_split
import torch
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import DataLoader, TensorDataset
import matplotlib.pyplot as plt


SAVE_DIR_PATH = '/Users/charlesmorgan/Desktop/MFCC_Features'


x = joblib.load(os.path.join(SAVE_DIR_PATH, 'x.joblib'))
y = joblib.load(os.path.join(SAVE_DIR_PATH, 'y.joblib'))


X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.3, random_state=42)


X_train = torch.tensor(X_train, dtype=torch.float32).unsqueeze(2)
X_test = torch.tensor(X_test, dtype=torch.float32).unsqueeze(2)
y_train = torch.tensor(y_train, dtype=torch.long)  y_test = torch.tensor(y_test, dtype=torch.long)


#print(f"Train shape: {X_train.shape}, Test shape: {X_test.shape}")


train_dataset = TensorDataset(X_train, y_train)
test_dataset = TensorDataset(X_test, y_test)

train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
test_loader = DataLoader(test_dataset, batch_size=32, shuffle=False)

class EmotionCNN(nn.Module):
    def __init__(self, input_size, num_classes):
        super(EmotionCNN, self).__init__()
        self.conv1 = nn.Conv1d(1, 64, kernel_size=5, padding=2)  # Conv1D Layer
        self.relu = nn.ReLU()  # Activation
        self.dropout = nn.Dropout(0.2)  # Dropout for regularization
        self.flatten = nn.Flatten()  # Flatten the output
        self.fc = nn.Linear(input_size * 64, num_classes)  # Fully connected layer

    def forward(self, x):
        x = x.permute(0, 2, 1)
        x = self.conv1(x)
        x = self.relu(x)
        x = self.dropout(x)
        x = self.flatten(x)
        x = self.fc(x)
        return x


input_size = X_train.shape[1]  # Number of MFCC features
num_classes = 8  # Number of output classes


model = EmotionCNN(input_size=input_size, num_classes=num_classes)
#print(model)


criterion = nn.CrossEntropyLoss()
optimizer = optim.RMSprop(model.parameters(), lr=0.001)


epochs = 70
train_losses = []
val_losses = []

for epoch in range(epochs):
    model.train()
    train_loss = 0.0
    for batch in train_loader:
        inputs, labels = batch
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()
        train_loss += loss.item()

    train_loss /= len(train_loader)
    train_losses.append(train_loss)


    model.eval()
    val_loss = 0.0
    with torch.no_grad():
        for batch in test_loader:
            inputs, labels = batch
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            val_loss += loss.item()

    val_loss /= len(test_loader)
    val_losses.append(val_loss)

    #print(f"Epoch {epoch+1}/{epochs}, Train Loss: {train_loss:.4f}, Val Loss: {val_loss:.4f}")


model.eval()
y_pred = []
with torch.no_grad():
    for batch in test_loader:
        inputs, _ = batch
        outputs = model(inputs)
        _, predicted = torch.max(outputs, 1)
        y_pred.extend(predicted.numpy())

'''
try:
    torch.save(model.state_dict(), '/Users/charlesmorgan/Desktop/EmotionEcho3/EmotionEchoCNNModel2.pth')
    print("Model saved successfully.")
except Exception as e:
    print(f"An error occurred: {e}")
'''


y_pred_classes = torch.tensor(y_pred, dtype=torch.long)


y_test = y_test.long()


correct_predictions = (y_pred_classes == y_test).sum().item()
total_predictions = y_test.size(0)


validation_accuracy = correct_predictions / total_predictions
#print(f"Validation Accuracy: {validation_accuracy:.2%}")



plt.plot(train_losses, label="Training Loss")
plt.plot(val_losses, label="Validation Loss")
plt.xlabel("Epochs")
plt.ylabel("Loss")
plt.legend()
plt.show()
