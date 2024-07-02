package MessageDigest;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * https://www.jianshu.com/p/7d72f09e8077
 */

public class cKeyTest {
  public static void main(String[] args) {
    try {
      generateKeyPair();
      generateKey();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
  public static void generateKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
    kpg.initialize(512);
    //生成非对称密钥对
    KeyPair kp = kpg.generateKeyPair();
    System.out.println(kpg.getProvider());
    System.out.println(kpg.getAlgorithm());
    KeyFactory kf = KeyFactory.getInstance("DSA");
    DSAPrivateKeySpec dsaPKS = kf.getKeySpec(kp.getPrivate(), DSAPrivateKeySpec.class);
//    Params:
//    x – the private key.
//    p – the prime.
//    q – the sub-prime.
//    g – the base.
    System.out.println("\tDSA param G:" + dsaPKS.getG());
    System.out.println("\tDSA param P:" + dsaPKS.getP());
    System.out.println("\tDSA param Q:" + dsaPKS.getQ());
    System.out.println("\tDSA param X:" + dsaPKS.getX());
  }
  public static void generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyGenerator kg = KeyGenerator.getInstance("DES");
    SecretKey key = kg.generateKey();
    System.out.println(kg.getProvider());
    System.out.println(kg.getAlgorithm());
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    DESKeySpec desKS = (DESKeySpec) skf.getKeySpec(key, DESKeySpec.class);
    System.out.println("\tDES key bytes size:" + desKS.getKey().length);
  }
}
