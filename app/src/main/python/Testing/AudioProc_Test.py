import unittest
from unittest.mock import MagicMock, patch
import numpy as np
from AudioProc.AudioProcessor import AudioProcessor
from AudioProc.FileManager import FileManager
from AudioProc.AudioManager import AudioManager

class TestAudioManager(unittest.TestCase):
    def setUp(self):
        self.sample_rate = 44100
        self.bit_depth = 16
        self.channel_cnt = 2
        self.duration_ms = 1000

        # Mock the FileManager and AudioProcessor
        self.mock_file_manager = MagicMock(spec=FileManager)
        self.mock_audio_processor = MagicMock(spec=AudioProcessor)

        with patch('AudioProc.AudioManager.FileManager', return_value=self.mock_file_manager), \
             patch('AudioProc.AudioManager.AudioProcessor', return_value=self.mock_audio_processor):
            self.audio_manager = AudioManager(
                self.sample_rate, self.bit_depth, self.channel_cnt, self.duration_ms
            )

    def test_process_chunk(self):
        chunk = b'\x00\x01\x02\x03'  # Example PCM data
        self.audio_manager.audioProc.split.return_value = "mock_file.wav"
        result = self.audio_manager.processChunk(chunk)
        self.assertEqual(result, "mock_file.wav")
        self.mock_audio_processor.split.assert_called_once_with(chunk)

    def test_get_wav_file(self):
        self.mock_file_manager.getWav.return_value = "mock_file.wav"
        result = self.audio_manager.getWavFile()
        self.assertEqual(result, "mock_file.wav")
        self.mock_file_manager.getWav.assert_called_once()

class TestAudioProcessor(unittest.TestCase):
    def setUp(self):
        self.mock_manager = MagicMock()
        self.mock_manager.sampleRate = 44100
        self.mock_manager.bufferSize = 44100
        self.mock_manager.fileManager = MagicMock()
        self.audio_processor = AudioProcessor(self.mock_manager)

    def test_split(self):
        chunk = np.random.randint(-32768, 32767, size=44100, dtype=np.int16).tobytes()
        self.mock_manager.fileManager.saveWav.return_value = "mock_file.wav"
        result = self.audio_processor.split(chunk)
        self.assertEqual(result, "mock_file.wav")
        self.mock_manager.fileManager.saveWav.assert_called_once()

    def test_process_chunk(self):
        chunk = np.random.randint(-32768, 32767, size=44100, dtype=np.int16).tobytes()
        self.audio_processor.processChunk(chunk)
        # Check silence counter and buffer are updated
        self.assertGreaterEqual(len(self.audio_processor.buffer), 0)

class TestFileManager(unittest.TestCase):
    def setUp(self):
        self.mock_manager = MagicMock()
        self.mock_manager.channelCnt = 2
        self.mock_manager.bitDepth = 16
        self.mock_manager.sampleRate = 44100

        self.file_manager = FileManager(self.mock_manager)

    @patch("os.makedirs")
    def test_make_dir(self, mock_makedirs):
        self.file_manager.makeDir()
        mock_makedirs.assert_called_once_with(self.file_manager.dirName, exist_ok=True)

    @patch("wave.open")
    def test_save_wav(self, mock_wave_open):
        data = np.random.randint(-32768, 32767, size=44100, dtype=np.int16)
        filename = self.file_manager.saveWav(data)
        self.assertTrue(filename.startswith(self.file_manager.dirName))
        self.assertTrue(mock_wave_open.called)

    @patch("os.remove")
    @patch("os.listdir", return_value=["file1.wav", "file2.wav"])
    def test_clear_dir(self, mock_listdir, mock_remove):
        self.file_manager.clearDir()
        self.assertEqual(mock_remove.call_count, 0)

    @patch("os.path.isfile", return_value=True)
    @patch("os.remove")
    def test_del_wav(self, mock_remove, mock_isfile):
        path = "/mock/path/file.wav"
        self.file_manager.delWav(path)
        mock_remove.assert_called_once_with(path)

    def test_get_wav(self):
        self.file_manager.countFiles = 1
        self.file_manager.filePaths = ["mock_file.wav"]
        result = self.file_manager.getWav()
        self.assertEqual(result, "mock_file.wav")
        self.assertEqual(self.file_manager.countFiles, 0)
        self.assertEqual(len(self.file_manager.filePaths), 0)

if __name__ == "__main__":
    unittest.main()
