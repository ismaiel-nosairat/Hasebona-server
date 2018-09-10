/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.tools;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public class JwtClientsFactory {

    public static AsymFac buildAsym() {

        return new AsymFac();

    }

    public static SymFac buildSym() {

        return new SymFac();

    }

    public static class AsymFac {

        JwtClient proxy;
        String keyStorefilePath;
        String keyStorePassword;
        String keyName;
        String keyPassword;

        public AsymFac() {
            proxy = new JwtClient();
        }

        public String getKeyStorefilePath() {
            return keyStorefilePath;
        }

        public AsymFac setKeyStorefilePath(String keyStorefilePath) {
            this.keyStorefilePath = keyStorefilePath;
            return this;
        }

        public String getKeyStorePassword() {
            return keyStorePassword;
        }

        public AsymFac setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
            return this;
        }

        public String getKeyName() {
            return keyName;
        }

        public AsymFac setKeyName(String keyName) {
            this.keyName = keyName;
            return this;
        }

        public String getKeyPassword() {
            return keyPassword;
        }

        public AsymFac setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
            return this;
        }

        public AsymFac setTtlMillis(long ttlMillis) {
            this.proxy.setTtlMillis(ttlMillis);
            return this;
        }

        public AsymFac setSignatureAlgorithm(AsymmetricAlgo algo) {
            this.proxy.setSignatureAlgorithm(algo.getAlgo());
            return this;
        }

        public JwtClient build() {
            // validate that all mandatory input are presented   

            //
            FileInputStream is = null;
            try {
                is = new FileInputStream(keyStorefilePath);
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(is, keyStorePassword.toCharArray());
                Key key = keystore.getKey(keyName, keyPassword.toCharArray());
                Certificate cert = keystore.getCertificate(keyName);
                PublicKey publicKey = cert.getPublicKey();
                proxy.setIsAsymmetric(true);
                proxy.setPrivateKey(key);
                proxy.setPublicKey(publicKey);
                return proxy;
            } catch (Exception ex) {
                Logger.getLogger(JwtClientsFactory.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(JwtClientsFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;

        }

    }

    public static class SymFac {

        JwtClient proxy;
        String secreteKeyAlgorithmName;
        byte[] keySeed;

        public SymFac() {
            proxy = new JwtClient();
        }

        public byte[] getKeySeed() {
            return keySeed;
        }

        public SymFac setKeySeed(byte[] keySeed) {
            this.keySeed = keySeed;
            return this;
        }

        public String getSecreteKeyAlgorithmName() {
            return secreteKeyAlgorithmName;
        }

        public SymFac setSecreteKeyAlgorithmName(String secreteKeyAlgorithmName) {
            this.secreteKeyAlgorithmName = secreteKeyAlgorithmName;
            return this;
        }

        public SymFac setTtlMillis(long ttlMillis) {
            this.proxy.setTtlMillis(ttlMillis);
            return this;
        }

        public SymFac setSignatureAlgorithm(SymmetricAlgo algo) {
            this.proxy.setSignatureAlgorithm(algo.getAlgo());
            return this;
        }

        public JwtClient build() {

            SecretKey secretKey = new SecretKeySpec(keySeed, secreteKeyAlgorithmName);
            this.proxy.setSecretKey(secretKey);
            this.proxy.setIsAsymmetric(false);
            return proxy;

        }

    }

}
