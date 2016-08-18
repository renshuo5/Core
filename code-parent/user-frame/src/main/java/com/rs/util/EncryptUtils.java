package com.rs.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.crypto.hash.Md5Hash;

public class EncryptUtils {
	public static final String encryptMD5(String source) {
		if (source == null) {
			source = "";
		}
		Md5Hash md5 = new Md5Hash(source);
		return md5.toString();
	}
	
	public static final String encryptSHA1(String salt,String tagStr){
		String sha1Token = "";
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA1");
			byte[] slatby = Hex.decodeHex(salt.toCharArray());
			sh.update(slatby);
			byte[] hashPass = sh.digest(tagStr.getBytes());
			sha1Token = Hex.encodeHexString(hashPass);
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sha1Token;
	}
	
	public static void main(String [] args){
		System.out.println(encryptMD5("renshuo"));
		
		System.out.println(encryptSHA1("978a409b4d643627", "renshuo"));
	}
}
