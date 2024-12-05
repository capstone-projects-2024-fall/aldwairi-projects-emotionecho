import os
import numpy as np
import joblib
from scipy.fftpack import dct
from scipy.io.wavfile import read as wavread


def extract_mfcc(file_path, samplerate=22050, n_mfcc=40):

    sample_rate, signal = wavread(file_path)
    
  
    signal = signal.astype(np.float32)
    signal /= np.max(np.abs(signal))

    
    pre_emphasis = 0.97
    emphasized_signal = np.append(signal[0], signal[1:] - pre_emphasis * signal[:-1])


    frame_size = 0.025
    frame_stride = 0.01
    frame_length, frame_step = int(frame_size * sample_rate), int(frame_stride * sample_rate)
    signal_length = len(emphasized_signal)
    num_frames = int(np.ceil(float(np.abs(signal_length - frame_length)) / frame_step)) + 1

   
    pad_signal_length = num_frames * frame_step + frame_length
    z = np.zeros((pad_signal_length - signal_length))
    padded_signal = np.append(emphasized_signal, z)


    indices = np.tile(np.arange(0, frame_length), (num_frames, 1)) + np.tile(
        np.arange(0, num_frames * frame_step, frame_step), (frame_length, 1)
    ).T
    frames = padded_signal[indices.astype(np.int32, copy=False)]
    frames *= np.hamming(frame_length)  # Apply a Hamming window


    NFFT = 512  #number of FFT points
    mag_frames = np.absolute(np.fft.rfft(frames, NFFT))
    pow_frames = (1.0 / NFFT) * (mag_frames ** 2)

 
    nfilt = 40  # Number of Mel filters
    low_freq_mel = 0
    high_freq_mel = 2595 * np.log10(1 + (sample_rate / 2) / 700)  # Convert Hz to Mel
    mel_points = np.linspace(low_freq_mel, high_freq_mel, nfilt + 2)  # Mel scale
    hz_points = 700 * (10 ** (mel_points / 2595) - 1)  # Convert Mel to Hz
    bin = np.floor((NFFT + 1) * hz_points / sample_rate)

    fbank = np.zeros((nfilt, int(np.floor(NFFT / 2 + 1))))
    for m in range(1, nfilt + 1):
        f_m_minus = int(bin[m - 1])
        f_m = int(bin[m])
        f_m_plus = int(bin[m + 1])

        for k in range(f_m_minus, f_m):
            fbank[m - 1, k] = (k - bin[m - 1]) / (bin[m] - bin[m - 1])
        for k in range(f_m, f_m_plus):
            fbank[m - 1, k] = (bin[m + 1] - k) / (bin[m + 1] - bin[m])

    filter_banks = np.dot(pow_frames, fbank.T)
    filter_banks = np.where(filter_banks == 0, np.finfo(float).eps, filter_banks)
    filter_banks = 20 * np.log10(filter_banks)


    mfcc = dct(filter_banks, type=2, axis=1, norm='ortho')[:, :n_mfcc]
    return np.mean(mfcc, axis=0)



DATASET_PATH = '/Users/charlesmorgan/Desktop/RAVDESS_data'

mfcc_features = []


for root, dirs, files in os.walk(DATASET_PATH):
    for file in files:
        if file.endswith('.wav'):
            try:
                file_path = os.path.join(root, file)
                mfccs = extract_mfcc(file_path)
                file_class = int(file[7:8]) - 1  
                mfcc_features.append((mfccs, file_class))
            except Exception as e:
                print(f"Error processing file {file}: {e}")
                continue


print(f"Extracted {len(mfcc_features)} files")

x, y = zip(*mfcc_features)
x, y = np.asarray(x), np.asarray(y)


SAVE_DIR_PATH = '/Users/charlesmorgan/Desktop/MFCC_Features'
if not os.path.exists(SAVE_DIR_PATH):
    os.makedirs(SAVE_DIR_PATH)

joblib.dump(x, os.path.join(SAVE_DIR_PATH, 'x.joblib'))
joblib.dump(y, os.path.join(SAVE_DIR_PATH, 'y.joblib'))
