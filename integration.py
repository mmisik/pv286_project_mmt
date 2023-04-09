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

    def test_echo_hex_to_array(self):
        input_str = "01020304"
        expected_output_str = "{0x1, 0x2, 0x3, 0x4}"
        cmd = ["echo", "-n", input_str, "|", "java", "-jar", "target/panbyte.jar", "-f", "hex", "-t", "array"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_hex(self):
        input_str = "{0x01, 2, 0b11, '\\x04'}"
        expected_output_str = "01020304"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "hex"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array(self):
        input_str = "{0x01, 2, 0b11, '\\x04'}"
        expected_output_str = "{0x1, 0x2, 0x3, 0x4}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_hex(self):
        input_str = "[0x01, 2, 0b11, '\\x04']"
        expected_output_str = "{0x1, 0x2, 0x3, 0x4}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=0x"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_decimal(self):
        input_str = "(0x01, 2, 0b11, '\\x04')"
        expected_output_str = "{1, 2, 3, 4}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=0"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_characters(self):
        input_str = "{0x01, 2, 0b11, '\\x04'}"
        expected_output_str = "{'\\x01', '\\x02', '\\x03', '\\x04'}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=a"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_binary(self):
        input_str = "[0x01, 2, 0b11, '\\x04']"
        expected_output_str = "{0b1, 0b10, 0b11, 0b100}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=0b"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_regular_bracket(self):
        input_str = "(0x01, 2, 0b11, '\\x04')"
        expected_output_str = "(0x1, 0x2, 0x3, 0x4)"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=\"(\""]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_square_bracket_decimal(self):
        input_str = "(0x01, 2, 0b11, '\\x04')"
        expected_output_str = "[1, 2, 3, 4]"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=0", "--to-options=\"[\""]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_square_to_curly_bracket_nested(self):
        input_str = "[[1, 2], [3, 4], [5, 6]]"
        expected_output_str = "{{0x1, 0x2}, {0x3, 0x4}, {0x5, 0x6}}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_square_to_curly_bracket_nested_decimal(self):
        input_str = "[[1, 2], [3, 4], [5, 6]]"
        expected_output_str = "{{1, 2}, {3, 4}, {5, 6}}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=0", "--to-options=\"{\""]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_curly_to_square_bracket_nested_decimal(self):
        input_str = "{{0x01, (2), [3, 0b100, 0x05], '\\x06'}}"
        expected_output_str = "[[1, [2], [3, 4, 5], 6]]"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=0", "--to-options=\"[\""]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_regular_to_curly_bracket_empty(self):
        input_str = "()"
        expected_output_str = "{}"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array"]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)

    def test_echo_array_to_array_brackets_to_square_empty_nested(self):
        input_str = "([], {})"
        expected_output_str = "[[], []]"
        cmd = ["echo", "-n", f'"{input_str}"', "|", "java", "-jar", "target/panbyte.jar", "-f", "array", "-t", "array",
               "--to-options=\"[\""]
        output = subprocess.check_output(" ".join(cmd), shell=True).decode().strip()
        self.assertEqual(output, expected_output_str)


if __name__ == '__main__':
    unittest.main()
