import numpy as np
from scipy.io import wavfile
from scipy.fftpack import dct
import torch

class EmotionClassifier:
    def __init__(self, model):
        self.model = model
        self.emotion_mapping = {
            0: "neutral",
            1: "calm",
            2: "happy",
            3: "sad",
            4: "angry",
            5: "fearful",
            6: "disgust",
            7: "surprised"
        }

    def extract_mfcc(self, file_path, samplerate=22050, n_mfcc=40):
        sample_rate, signal = wavfile.read(file_path)

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
        frames *= np.hamming(frame_length)

        NFFT = 512
        mag_frames = np.absolute(np.fft.rfft(frames, NFFT))
        pow_frames = (1.0 / NFFT) * (mag_frames ** 2)

        nfilt = 40
        low_freq_mel = 0
        high_freq_mel = 2595 * np.log10(1 + (sample_rate / 2) / 700)
        mel_points = np.linspace(low_freq_mel, high_freq_mel, nfilt + 2)
        hz_points = 700 * (10 ** (mel_points / 2595) - 1)
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

        mfccs = dct(filter_banks, type=2, axis=1, norm='ortho')[:, :n_mfcc]
        return np.mean(mfccs, axis=0)

    def classify_audio(self, audio_features):
        if not isinstance(audio_features, np.ndarray) or audio_features.shape[0] != 40:
            raise ValueError("Invalid audio features provided. Expected an array with 40 MFCC features.")

        audio_features = torch.tensor(audio_features, dtype=torch.float32).unsqueeze(0).unsqueeze(2)

        self.model.eval()

        with torch.no_grad():
            prediction = self.model(audio_features)
            predicted_class = torch.argmax(prediction, dim=1).item()

        return self.emotion_mapping[predicted_class]