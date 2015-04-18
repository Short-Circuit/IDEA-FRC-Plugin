package com.shortcircuit.frc.build.utils;

import com.intellij.ide.passwordSafe.impl.providers.EncryptionUtil;

/**
 * @author ShortCircuit908
 */
public class Encrypt {
	public static final String APP_PASS = "3@j94CFN8#jexP07c";
	private static final byte[] PASSKEY;

	static {
		PASSKEY = EncryptionUtil.genPasswordKey(APP_PASS);
	}

	public static byte[] encrypt(String text) {
		return EncryptionUtil.encryptText(PASSKEY, text);
	}

	public static String decrypt(byte[] data) {
		if (data == null) {
			return null;
		}
		return EncryptionUtil.decryptText(PASSKEY, data);
	}
}
