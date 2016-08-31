package secure.message.transaction.utility;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.spongycastle.asn1.x509.X509Name;
import org.spongycastle.asn1.x509.X509NameTokenizer;
import org.spongycastle.jce.PrincipalUtil;
import org.spongycastle.jce.X509Principal;

public class CertificateUtility {
	Utility utility = new Utility();

	public static final String EMAIL = "rfc822name";
	public static final String EMAIL1 = "email";
	public static final String EMAIL2 = "EmailAddress";
	public static final String EMAIL3 = "E";
	private static final String[] EMAILIDS = { EMAIL, EMAIL1, EMAIL2, EMAIL3 };

	String password = "12345";

	// public PublicKey getPublicKeyFromPFX(String certificatePath) {
	// PublicKey publicKey = null;
	// try {
	// InputStream certificateStream = new FileInputStream(certificatePath);
	// Certificate cert = utility.getByteArrayToCert(utility
	// .getPfxToCertByteArray(certificateStream, password));
	//
	// publicKey = cert.getPublicKey();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return publicKey;
	// }

	public PublicKey getPublicKey(String certificatePath) {
		PublicKey pubkey = null;
		try {
			InputStream inStream = new FileInputStream(certificatePath);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);

			pubkey = cert.getPublicKey();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pubkey;
	}

	public static X509Certificate getCertificate(String certificatePath) {
		X509Certificate cert = null;
		try {
			InputStream inStream = new FileInputStream(certificatePath);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) cf.generateCertificate(inStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cert;
	}

	public static String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10)
				buffer.append("0");
			buffer.append(Long.toString(bytes[i] & 0xff, 16) + " ");
		}
		return buffer.toString();
	}

	public PrivateKey getPrivateKey(String certificatePath) {
		PrivateKey privateKey = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream fileName = new FileInputStream(certificatePath);
			ks.load(fileName, password.toCharArray());
			Enumeration aliases = ks.aliases();
			String alias = (String) aliases.nextElement();
			privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static String getEmailAddressFromCertificate(X509Certificate certificate) {
		try {
			final X509Principal principal = PrincipalUtil.getSubjectX509Principal(certificate);
			final Vector<?> values = principal.getValues(X509Name.E);

			if (values != null || values.size() != 0) {
				return (String) values.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static String getCommonNameFromCertificate(X509Certificate certificate) {
		try {
			final X509Principal principal = PrincipalUtil.getSubjectX509Principal(certificate);
			final Vector<?> values = principal.getValues(X509Name.CN);

			if (values != null || values.size() != 0) {
				return (String) values.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
