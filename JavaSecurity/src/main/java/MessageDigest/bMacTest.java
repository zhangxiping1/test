package MessageDigest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Hex;

/**
 * 安全(加密的)消息摘要
 * 考虑到消息摘要的不安全性——拿到原文并知道算法，就可以得到正确的消息摘要。人们又加入了密钥元素，
 * 得到了安全消息摘要——MAC（Message Authentication Code）。MAC的特点是仅通过原文是无法计算出安全消息摘要的。
 * 还需要一个双方都知道的消息密钥。如果有人修改了消息本体，又修改了消息摘要，但是因为没有使用密钥，那么将被发现破坏了数据。
 * 计算MAC的方法有很多，但是核心Java API没有相关实现。Java的实现主要是通过JCE提供者来实现的。Mac类对应消息摘要的MessageDigest类。
 * 计算消息摘要时需要一个密钥。密钥的管理是另一个话题了。这里主要讲MAC的算法，JCE实现主要基于HmacSHA1和HmacMD5。
 * 具体provider提供了哪些MAC相关的算法，它们比较如何，见下面的代码：
 * 这里使用的key是通过KeyGenerator生成的。在实际应用中，应该有一方来生成并导出到可管理的KeyStore，使用方载入Keystore再进行摘要生成和校验。
 */

public class bMacTest {
  public static String TEST_DATA = "I am test";
  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
    // TODO Auto-generated method stub
    Mac mac = Mac.getInstance("HmacSHA1");
    print(mac);
    mac = Mac.getInstance("HmacMD5");
    print(mac);
    mac = Mac.getInstance("SslMacMD5");
    print(mac);
    mac = Mac.getInstance("HmacSHA384");
    print(mac);
    mac = Mac.getInstance("HmacSHA256");
    print(mac);
    mac = Mac.getInstance("HmacSHA224");
    print(mac);
    mac = Mac.getInstance("SslMacSHA1");
    print(mac);
    mac = Mac.getInstance("HmacSHA512");
    print(mac);
  }
  private static void print(Mac mac) throws NoSuchAlgorithmException, InvalidKeyException {
    System.out.println("Algorithm:\t" + mac.getAlgorithm());
    System.out.println("\tProvider:\t" + mac.getProvider());
    KeyGenerator kg = KeyGenerator.getInstance("DES");
    SecretKey key = kg.generateKey();
    long start = System.nanoTime();
    mac.init(key);
    mac.update(TEST_DATA.getBytes());
    byte[] digest = mac.doFinal();
    long time = System.nanoTime() - start;
    System.out.println("\tTime cost:\t" + time + "ns");
    System.out.println("\tByte length:\t" + digest.length);
    String digestBase64Str = Base64.getEncoder().encodeToString(digest);
    System.out.println("\tBase64:\t" + digestBase64Str + "\tlen:\t" + digestBase64Str.length());
    String digestHexStr = Hex.encodeHexString(digest);
    System.out.println("\tHex:\t" + digestHexStr + "\tlen:\t" + digestHexStr.length());

    //检验摘要
    SecretKey key2 = kg.generateKey();
    Mac mac2 = Mac.getInstance(mac.getAlgorithm());
    //检验摘要通过
    //mac2.init(key);
    mac2.init(key2);
    mac2.update(TEST_DATA.getBytes());
    byte[] digest2 = mac2.doFinal();
    System.out.println("\tByte length digest2:\t" + digest2.length+"\t"+Hex.encodeHexString(digest2));
    System.out.println(Hex.encodeHexString(digest).equals(Hex.encodeHexString(digest2)));

  }
}
