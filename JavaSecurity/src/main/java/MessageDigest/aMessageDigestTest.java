package MessageDigest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 消息摘要（Message Digest）又称为数字摘要(Digital Digest)。它是一个唯一对应一个消息或文本的固定长度的值，
 * 它由一个单向Hash加密函数对消息进行作用而产生。如果消息在途中改变了，则接收者通过对收到消息的新产生的摘要与原摘要比较，
 * 就可知道消息是否被改变了。因此消息摘要保证了消息的完整性。 消息摘要采用单向Hash 函数将需加密的明文"摘要"成一串128bit的密文，
 * 这一串密文亦称为数字指纹(Finger Print)，它有固定的长度，且不同的明文摘要成密文，其结果总是不同的，而同样的明文其摘要必定一致。
 * 这样这串摘要便可成为验证明文是否是"真身"的"指纹"了。
 *
 * 消息摘要其实是我们日常开发中经常遇到的，比如MD5算法就是一种摘要。它是Java安全提供者体系中最简单的标准引擎。本文不会讨论算法本身，
 * 只关注Java语言体系中的实现和使用方法。具体算法原理，后续撰文学习并补充。
 *
 * 消息摘要通过MessageDigest类实现。消息摘要通过getInstance()方法获取算法实例。通过update()方法为消息摘要增加内容字节。
 * 根据digest()方法利用累计的内容字节计算最后的摘要。
 */
public class aMessageDigestTest {
  private static String TEST_DATA = "i am a test";
  public static void main(String[] args)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA");
    print(md);
    md = MessageDigest.getInstance("MD5");
    print(md);
    md = MessageDigest.getInstance("MD2");
    print(md);
    md = MessageDigest.getInstance("SHA-256");
    print(md);
    md = MessageDigest.getInstance("SHA-384");
    print(md);
    md = MessageDigest.getInstance("SHA-224");
    print(md);
    md = MessageDigest.getInstance("SHA-512");
    print(md);
  }
  private static void print(MessageDigest md) throws UnsupportedEncodingException {
    System.out.println("Algorithm:\t" + md.getAlgorithm());
    System.out.println("\tProvider:\t" + md.getProvider());
    long start = System.nanoTime();
    md.update(TEST_DATA.getBytes("UTF-8"));
    byte[] digest = md.digest();
    long time = System.nanoTime() - start;
    System.out.println("\tTime cost:\t" + time + "ns");
    System.out.println("\tByte length:\t" + digest.length);
    //Hex  16 进制  , 进制越大,越短
    String digestBase64Str = Base64.getEncoder().encodeToString(digest);
    System.out.println("\tBase64:\t" + digestBase64Str + "\tlen:\t" + digestBase64Str.length());
    String digestHexStr = Hex.encodeHexString(digest);
    System.out.println("\tHex:\t" + digestHexStr + "\tlen:\t" + digestHexStr.length());
  }
}
