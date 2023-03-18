import subprocess
import unittest


class TestPanbyteIntegration(unittest.TestCase):
    def test_echo_bytes_to_bytes(self):
        input_str = "test"
        expected_output_str = "test"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "bytes", "-t", "bytes"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_bytes(self):
        input_str = "74657374"
        expected_output_str = "test"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "hex", "-t", "bytes"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bytes_to_hex(self):
        input_str = "test"
        expected_output_str = "74657374"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "bytes", "-t", "hex"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_bytes_interleaved(self):
        input_str = "74 65 73 74"
        expected_output_str = "test"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "hex", "-t", "bytes"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_int_to_hex(self):
        input_str = "1234567890"
        expected_output_str = "499602d2"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "int", "-t", "hex"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_int_to_hex_big(self):
        input_str = "1234567890"
        expected_output_str = "499602d2"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "int", "--from-options=big",
               "-t", "hex"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_int_to_hex_little(self):
        input_str = "1234567890"
        expected_output_str = "d2029649"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "int", "--from-options=little",
               "-t", "hex"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_int(self):
        input_str = "499602d2"
        expected_output_str = "1234567890"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "hex", "-t", "int"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_int_big(self):
        input_str = "499602d2"
        expected_output_str = "1234567890"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "hex", "-t", "int",
               "--to-options=big"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_hex_to_int_little(self):
        input_str = "d2029649"
        expected_output_str = "1234567890"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "hex", "-t", "int",
               "--to-options=little"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bits_to_bytes(self):
        input_str = "100 1111 0100 1011"
        expected_output_str = "OK"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "bits", "-t", "bytes"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bits_to_bytes_left(self):
        input_str = "100111101001011"
        expected_output_str = "OK"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "bits", "--from-options=left ",
               "-t", "bytes"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bits_to_hex_right(self):
        input_str = "100111101001011"
        expected_output_str = "9e96"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "bits", "--from-options=right",
               "-t", "hex"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_bytes_to_bits(self):
        input_str = "OK"
        expected_output_str = "0100111101001011"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "bytes", "-t", "bits"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)


if __name__ == '__main__':
    unittest.main()
