package secure.message.transaction.crypto;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.X509V3CertificateGenerator;


public class GenerateCertificate {
	
	public X509Certificate generateV3Certificate(String x509String, PublicKey publicKey, PrivateKey privateKey)
			throws InvalidKeyException, NoSuchProviderException, SignatureException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(new X500Principal(x509String));
		certGen.setNotBefore(new Date(System.currentTimeMillis() - 10000));
		certGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10)));
		certGen.setSubjectDN(new X500Principal(x509String));
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

		certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
		certGen.addExtension(X509Extensions.KeyUsage, true,
				new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
		certGen.addExtension(X509Extensions.ExtendedKeyUsage, true,
				new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));

		certGen.addExtension(X509Extensions.SubjectAlternativeName, false,
				new GeneralNames(new GeneralName(GeneralName.rfc822Name, "test@test.test")));

		return certGen.generateX509Certificate(privateKey, "BC");
	}
	
	public Certificate getByteArrayToCert(byte[] byteArrayValue) {
		Certificate cert = null;
		try {
			cert = CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(byteArrayValue));
		} catch (Exception e) {
		}
		return cert;
	}
	
	public boolean exportCertificateIntoFile(X509Certificate cert, String filePath) {
		boolean status = false;
		try {

			//X509Certificate cert = (X509Certificate) getByteArrayToCert(byteArrayValue);
			if (cert != null) {
				if ((!filePath.contains(".crt")) && (!filePath.contains(".cer"))) {
					filePath = filePath + ".crt";
				}
				File file = new File(filePath);
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
				
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				PrintStream printStream = new PrintStream(byteStream);

				//BASE64Encoder encoder = new BASE64Encoder();
				//Base64.encode(cert.getEncoded(), printStream);

				printStream.println("-----BEGIN CERTIFICATE-----\n");
				Base64.encode(cert.getEncoded(), printStream);
				//encoder.encodeBuffer(cert.getEncoded(), printStream);
				printStream.println("\n-----END CERTIFICATE-----");
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(new String(byteStream.toByteArray()));
				writer.close();
				status = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
		return status;
	}

}
