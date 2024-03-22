package com.yanmastra.msSecurityBase.security;

import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SecretKeyAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.jsonwebtoken.Jwts.KEY.*;
import static io.jsonwebtoken.Jwts.SIG.*;

public class KeyUtils {
    private static final Logger log = LogManager.getLogger(KeyUtils.class);
    private static final String RSA = "RSA";
    private static final String AES = "AES";
    private static final String RS = "RS";
    private static final String EC = "EC";
    private static final String SIGNATURE_ALGORITHM = "RS256";
    protected static final Map<MacAlgorithm, Integer> SIGNATURE_ALGORITHM_BITS;
    protected static final Map<SecretKeyAlgorithm, Integer> KEY_ENCRYPTION_BITS = new HashMap<>();

    private KeyUtils() {
    }

    public static PublicKey readPublicKey(String pemResName) throws IOException, GeneralSecurityException {
        return readPublicKey(pemResName, SIGNATURE_ALGORITHM);
    }

    public static PublicKey readPublicKey(String pemResName, String algo) throws IOException, GeneralSecurityException {
        InputStream contentIS = new FileInputStream(ResourceUtils.getFile(pemResName));
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePublicKey(new String(tmp, 0, length), algo);
    }

    public static PrivateKey decodePrivateKey(String pemEncoded, String algo) throws GeneralSecurityException {
        return decodePrivateKeyInternal(pemEncoded, keyFactoryAlgorithm(algo));
    }

    static PrivateKey decodePrivateKeyInternal(String pemEncoded, String algo) throws GeneralSecurityException {
        pemEncoded = removePemKeyBeginEnd(pemEncoded);
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pemEncoded);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance(algo);
        return kf.generatePrivate(keySpec);
    }

    public static PublicKey decodePublicKey(String pemEncoded) throws GeneralSecurityException {
        return decodePublicKey(pemEncoded, SIGNATURE_ALGORITHM);
    }

    public static SecretKey createSecretKeyFromSecret(String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretBytes, AES);
    }

    public static SecretKey createSecretKeyFromEncodedSecret(String secret) {
        byte[] secretBytes = Base64.getUrlDecoder().decode(secret);
        return new SecretKeySpec(secretBytes, AES);
    }

    public static PublicKey decodePublicKey(String pemEncoded, String algo) throws GeneralSecurityException {
        pemEncoded = removePemKeyBeginEnd(pemEncoded);
        byte[] encodedBytes = Base64.getDecoder().decode(pemEncoded);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance(keyFactoryAlgorithm(algo));
        return kf.generatePublic(spec);
    }

    public static KeyStore loadKeyStore(String keyStorePath, String keyStorePassword, Optional<String> keyStoreType, Optional<String> keyStoreProvider) throws Exception {
        String theKeyStoreType = getKeyStoreType(keyStorePath, keyStoreType);
        Provider provider = null;
        if (keyStoreProvider.isPresent()) {
            provider = Security.getProvider(keyStoreProvider.get());
            if (provider == null) {
                throw new FileNotFoundException("Key store not found!");
            }
        }

        KeyStore keyStore = provider != null ? KeyStore.getInstance(theKeyStoreType, provider) : KeyStore.getInstance(theKeyStoreType);
        if (keyStorePath != null) {

            try (InputStream is = new FileInputStream(ResourceUtils.getFile(keyStorePath))) {
                keyStore.load(is, keyStorePassword.toCharArray());
            } catch (Throwable throwable) {
                log.error(throwable);
                throw throwable;
            }
        } else {
            keyStore.load((InputStream)null, keyStorePassword.toCharArray());
        }

        return keyStore;
    }

    private static String getKeyStoreType(String keyStorePath, Optional<String> keyStoreType) {
        if (keyStoreType.isPresent()) {
            return keyStoreType.get().toUpperCase();
        } else {
            String pathName = keyStorePath.toString();
            return !pathName.endsWith(".p12") && !pathName.endsWith(".pkcs12") && !pathName.endsWith(".pfx") ? "JKS" : "PKCS12";
        }
    }

    public static PublicKey decodeEncryptionPublicKey(String pemEncoded, String algo) throws GeneralSecurityException {
        pemEncoded = removePemKeyBeginEnd(pemEncoded);
        byte[] encodedBytes = Base64.getDecoder().decode(pemEncoded);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance(encryptionKeyFactoryAlgorithm(algo));
        return kf.generatePublic(spec);
    }

    static String keyFactoryAlgorithm(String algo) throws NoSuchAlgorithmException {
        if (!algo.startsWith("RS") && !algo.startsWith("PS")) {
            if (algo.startsWith("ES")) {
                return EC;
            } else {
                throw new NoSuchAlgorithmException("Not supported algorithm: "+algo);
            }
        } else {
            return RSA;
        }
    }

    static String encryptionKeyFactoryAlgorithm(String algo) throws NoSuchAlgorithmException {
        if (algo.startsWith(RS)) {
            return RSA;
        } else if (algo.startsWith(EC)) {
            return EC;
        } else {
            throw new NoSuchAlgorithmException("Not supported algorithm: "+algo);
        }
    }

    public static PublicKey decodeCertificate(String pemEncoded) throws GeneralSecurityException {
        return getCertificate(pemEncoded).getPublicKey();
    }

    public static X509Certificate getCertificate(String pemEncoded) throws GeneralSecurityException {
        pemEncoded = removeCertBeginEnd(pemEncoded);
        byte[] encodedBytes = Base64.getDecoder().decode(pemEncoded);
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(encodedBytes));
    }

    public static String removePemKeyBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN(.*?)KEY-----", "");
        pem = pem.replaceAll("-----END(.*?)KEY-----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        pem = pem.replaceAll("\\\\n", "");
        return pem.trim();
    }

    private static String removeCertBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN(.*?)CERTIFICATE-----", "");
        pem = pem.replaceAll("-----END(.*?)CERTIFICATE-----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        pem = pem.replaceAll("\\\\n", "");
        return pem.trim();
    }

    static {
        KEY_ENCRYPTION_BITS.put(A128KW, 128);
        KEY_ENCRYPTION_BITS.put(A192KW, 192);
        KEY_ENCRYPTION_BITS.put(A256KW, 256);
        SIGNATURE_ALGORITHM_BITS = new HashMap<>();
        SIGNATURE_ALGORITHM_BITS.put(HS256, 256);
        SIGNATURE_ALGORITHM_BITS.put(HS384, 384);
        SIGNATURE_ALGORITHM_BITS.put(HS512, 512);
    }
}
