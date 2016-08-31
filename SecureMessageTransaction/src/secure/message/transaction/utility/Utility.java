package secure.message.transaction.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import javax.naming.Context;
import javax.swing.text.View;



public class Utility {
	public static String getRandomValue(){
		Random ran = new Random();
		int x = ran.nextInt(1000) + 5;
		return x+"";
	}
	
	public static String getFileSeparator() {
		String fileSeparator = System.getProperty("file.separator");
		return fileSeparator;
	}
	
	public static byte[] append(byte[] prefix, byte[] suffix) {
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++) {
			toReturn[i] = prefix[i];
		}
		for (int i = 0; i < suffix.length; i++) {
			toReturn[i + prefix.length] = suffix[i];
		}
		return toReturn;
	}

	public static String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10)
				buffer.append("0");
			buffer.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}

	public static byte[] hexToByteArray(String str) {
		int i = 0;
		byte[] results = new byte[str.length() / 2];
		for (int k = 0; k < str.length();) {
			results[i] = (byte) (Character.digit(str.charAt(k++), 16) << 4);
			results[i] += (byte) (Character.digit(str.charAt(k++), 16));
			i++;
		}
		return results;
	}

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	public static byte[] decodeHex(char[] data) {
		int len = data.length;
		if ((len & 0x01) != 0) {
		}
		byte[] out = new byte[len >> 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

	protected static int toDigit(char ch, int index) {
		int digit = 0;
		try {
			digit = Character.digit(ch, 16);
			if (digit == -1) {
			}
		} catch (Exception e) {

		}
		return digit;

	}
	
	public static void convertToHex(PrintStream out, File file)
			throws IOException {
		InputStream is = new FileInputStream(file);

		int bytesCounter = 0;
		int value = 0;
		StringBuilder sbHex = new StringBuilder();
		StringBuilder sbText = new StringBuilder();
		StringBuilder sbResult = new StringBuilder();

		while ((value = is.read()) != -1) {
			// convert to hex value with "X" formatter
			sbHex.append(String.format("%02X ", value));

			// If the chracater is not convertable, just print a dot symbol "."
			if (!Character.isISOControl(value)) {
				sbText.append((char) value);
			} else {
				sbText.append(".");
			}

			// if 16 bytes are read, reset the counter,
			// clear the StringBuilder for formatting purpose only.
			if (bytesCounter == 15) {
				// sbResult.append(sbHex).append("      ").append(sbText)
				// .append("\n");
				sbResult.append(sbHex).append("\n");
				sbHex.setLength(0);
				sbText.setLength(0);
				bytesCounter = 0;
			} else {
				bytesCounter++;
			}
		}

		// if still got content
		if (bytesCounter != 0) {
			// add spaces more formatting purpose only
			for (; bytesCounter < 16; bytesCounter++) {
				// 1 character 3 spaces
				sbHex.append("   ");
			}
			// sbResult.append(sbHex).append("      ").append(sbText).append("\n");
			sbResult.append(sbHex).append("\n");
		}

		out.print(sbResult);
		is.close();
	}
	
	
	public byte[] getPfxToCertByteArray(InputStream fileUrl, String password) {
		byte[] certByteArray = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			// FileInputStream fileName = new FileInputStream(fileUrl);
			InputStream fileName = fileUrl;
			char[] passwordKey = password.toCharArray();
			ks.load(fileName, passwordKey);
			Enumeration<String> aliases = ks.aliases();
			String alias = aliases.nextElement();
			byte[] vertBytes = ks.getCertificate(alias).getEncoded();
			Certificate cert = CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(vertBytes));
			certByteArray = cert.getEncoded();
		} catch (Exception e) {
		}
		return certByteArray;
	}
	
	
	public Certificate getByteArrayToCert(byte[] byteArrayValue) {
		Certificate cert = null;
		try {
			cert = CertificateFactory.getInstance("X.509").generateCertificate(
					new ByteArrayInputStream(byteArrayValue));
		} catch (Exception e) {
		}
		return cert;
	}
	
	
	public static Long currentDataTime() {
//		DateFormat dateFormate = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		return date.getTime()/1000;
//		return /dateFormate.format(date);
	}
	
	
	
	
	public static String showListData(String data){
		int displayChar = 15;
		return showListData(data, displayChar);
	}
	
	public static String showListData(String data, int displayChar){
		if (data.length() == 0) return null;
		if(data.length() > displayChar){
			data = data.substring(0, displayChar);
			data = data + "...";
		}
		return data;
	}
}
