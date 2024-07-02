import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestZstd {

  public static String zstdString(String unzip) {
    byte[] compress = Zstd.compress(unzip.getBytes());
    return new sun.misc.BASE64Encoder().encodeBuffer(compress);
  }

  public static String unZstdString(String zip) throws IOException {
    byte[] decode = new sun.misc.BASE64Decoder().decodeBuffer(zip);
    Long size = Zstd.decompressedSize(decode);
    byte[] decompress = Zstd.decompress(decode, size.intValue());
    return new String(decompress);
  }


  public static void main(String[] args) throws IOException {
    String compressedFile = "D:/application_1686216965745_0011_1.zstd";
    String decompressedFile = "D:/application_1686216965745_0011_1";

    try (FileInputStream fis = new FileInputStream(compressedFile);
         FileOutputStream fos = new FileOutputStream(decompressedFile);
         ZstdInputStream zis = new ZstdInputStream(fis)) {
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = zis.read(buffer)) != -1) {
        fos.write(buffer, 0, bytesRead);
      }
    }
  }


}
