import subprocess
import unittest

class TestPanbyteIntegration(unittest.TestCase):
    def test_echo_bytes_to_bytes(self):
        input_str = "test"
        expected_output_str = "test"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bytes", "-t", "bytes", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_bytes(self):
        input_str = "74657374"
        expected_output_str = "test"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "hex", "-t", "bytes", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bytes_to_hex(self):
        input_str = "test"
        expected_output_str = "74657374"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bytes", "-t", "hex", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_int_big(self):
        input_str = "1234567890"
        expected_output_str = "499602d2"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "int", "--from-options=big", "-t", "hex", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_int_little(self):
        input_str = "d2029649"
        expected_output_str = "1234567890"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "hex", "-t", "int", "--to-options=little", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bits_to_bytes(self):
        input_str = "100 1111 0100 1011"
        expected_output_str = "OK"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bits", "-t", "bytes", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bits_left_to_bytes(self):
        input_str = "100111101001011"
        expected_output_str = "OK"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bits", "--from-options=left", "-t", "bytes", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bits_right_to_hex(self):
        input_str = "100111101001011"
        expected_output_str = "9e96"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bits", "--from-options=right", "-t", "hex", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bytes_to_bits(self):
        input_str = "OK"
        expected_output_str = "0100111101001011"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bytes", "-t", "bits", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_hex_to_bits_conversion(self):
        # Test converting hex input to bits output
        input_str = "DEADBEEF"
        expected_output_str = "11011110101011011011111011101111"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "hex", "-t", "bits", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_bits_to_hex_conversion(self):
        # Test converting bits input to hex output
        input_str = "11011110101011011011111011101111"
        expected_output_str = "DEADBEEF"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bits", "-t", "hex", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_int_to_bits_conversion(self):
        # Test converting int input to bits output
        input_str = "1234567890"
        expected_output_str = "01001001100101100000011000110010"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "int", "-t", "bits", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_bits_to_int_conversion(self):
        # Test converting bits input to int output
        input_str = "01001001100101100000011000110010"
        expected_output_str = "1234567890"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "bits", "-t", "int", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_hex_to_int_conversion(self):
        # Test converting hex input to int output
        input_str = "DEADBEEF"
        expected_output_str = "3735928559"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "hex", "-t", "int", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_int_to_hex_conversion(self):
        # Test converting int input to hex output
        input_str = "3735928559"
        expected_output_str = "DEADBEEF"
        cmd = ["echo", input_str, "|", "./panbyte", "-f", "int", "-t", "hex", "-"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)


if __name__ == '__main__':
    TestPanbyteIntegration.main()