package com.ala.tool;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptServiceImpl {

	private static String key = null;
	private static String ivKey = null;
	private static boolean enc = true;
	static {
//		hashToKey(PropertyConfigurer.getString("encrypt.aes.key"));
		hashToKey("alaonion");
	}

	public static void hashToKey(String hash) {
		String md5Code = MD5Util.MD5(hash);
		key = md5Code.substring(0, 16);
		ivKey = md5Code.substring(16);
	}

	public static void main(String[] args){
		System.out.println(new EncryptServiceImpl().aes128Decrypt("qWfLRa/8K+KZVH7aLW14PA==") + "_1");
		System.out.println(new EncryptServiceImpl().aes128Encrypt("彭丁 "));
	}
	
	public String pbkdf2Encrypt(String plain) {
		if (!enc) {
			return plain;
		}
		if (plain == null) {
			return null;
		}
		try {
			return generateStorngPasswordHash(plain);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean pbkdf2Match(String plain, String encrypt) {
		if (!enc) {
			return encrypt!=null&&encrypt.equals(plain);
		}
		try {
			return validatePassword(plain, encrypt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String generateStorngPasswordHash(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	private static String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	private static boolean validatePassword(String originalPassword,
			String storedPassword) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt,
				iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
		return bytes;
	}

	public String aes128Encrypt(String plain) {
		if (!enc) {
			return plain;
		}
		return encrypt(plain);
	}

	public String aes128Decrypt(String encrypt) {
		if (!enc) {
			return encrypt;
		}
		return decrypt(encrypt);
	}

	private static String encrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			int blockSize = cipher.getBlockSize();

			byte[] dataBytes = data.getBytes();
			int plaintextLength = dataBytes.length;
			if (plaintextLength % blockSize != 0) {
				plaintextLength = plaintextLength
						+ (blockSize - (plaintextLength % blockSize));
			}

			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(ivKey.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);

			return Base64.encode(encrypted);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	private static String decrypt(String data) {
		try {
			String text = Base64.decode(data.getBytes());

			byte[] by = ConvertTobyte(text);
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(ivKey.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(by);
			String originalString = new String(original);
			return originalString.replace("\u0000", "");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	private static byte[] ConvertTobyte(String data) {
		int maxLength = data.length();
		byte[] by = new byte[maxLength];
		char[] chars = data.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			by[i] = (byte) chars[i];
		}
		return by;
	}

	static class Base64 {
		private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C',
				'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
				'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
				'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
		private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55,
				56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3,
				4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30,
				31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
				47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

		public static String encode(byte[] data) {
			StringBuffer sb = new StringBuffer();
			int len = data.length;
			int i = 0;
			int b1, b2, b3;
			while (i < len) {
				b1 = data[i++] & 0xff;
				if (i == len) {
					sb.append(base64EncodeChars[b1 >>> 2]);
					sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
					sb.append("==");
					break;
				}
				b2 = data[i++] & 0xff;
				if (i == len) {
					sb.append(base64EncodeChars[b1 >>> 2]);
					sb.append(base64EncodeChars[((b1 & 0x03) << 4)
							| ((b2 & 0xf0) >>> 4)]);
					sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
					sb.append("=");
					break;
				}
				b3 = data[i++] & 0xff;
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
						| ((b3 & 0xc0) >>> 6)]);
				sb.append(base64EncodeChars[b3 & 0x3f]);
			}
			return sb.toString();
		}

		public static String decode(byte[] data) {
			StringBuffer sb = new StringBuffer();
			int len = data.length;
			int i = 0;
			int b1, b2, b3, b4;
			while (i < len) {
				do {
					b1 = base64DecodeChars[data[i++]];
				} while (i < len && b1 == -1);
				if (b1 == -1)
					break;

				do {
					b2 = base64DecodeChars[data[i++]];
				} while (i < len && b2 == -1);
				if (b2 == -1)
					break;
				sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

				do {
					b3 = data[i++];
					if (b3 == 61)
						return sb.toString();
					b3 = base64DecodeChars[b3];
				} while (i < len && b3 == -1);
				if (b3 == -1)
					break;
				sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

				do {
					b4 = data[i++];
					if (b4 == 61)
						return sb.toString();
					b4 = base64DecodeChars[b4];
				} while (i < len && b4 == -1);
				if (b4 == -1)
					break;
				sb.append((char) (((b3 & 0x03) << 6) | b4));
			}
			return sb.toString();
		}

	}

	static class MD5Util {
		public final static String MD5(String s) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'a', 'b', 'c', 'd', 'e', 'f' };
			try {
				byte[] btInput = s.getBytes();
				MessageDigest mdInst = MessageDigest.getInstance("MD5");
				mdInst.update(btInput);
				byte[] md = mdInst.digest();
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return null;
		}
	}

}
