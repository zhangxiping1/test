import de.jarnbjo.ogg.FileStream;
import de.jarnbjo.ogg.LogicalOggStream;
import org.jitsi.impl.neomedia.codec.audio.opus.Opus;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Collection;

public class OpusAudioPlayer {
  private static int BUFFER_SIZE = 1024 * 1024;
  private static int INPUT_BITRATE = 48000;
  private static int OUTPUT_BITRATE = 48000;

  private FileStream oggFile;
  private long opusState;

  private ByteBuffer decodeBuffer = ByteBuffer.allocate(BUFFER_SIZE);

  private AudioFormat audioFormat = new AudioFormat(OUTPUT_BITRATE, 16, 1, true, false);

  /**
   *   本代码取自 : https://qa.1r1g.com/sf/ask/1837571221/  样例代码  第二个案例.
   *   项目依赖的jar来自:
   *      http://www.java2s.com/Code/Jar/j/Downloadjnaplatform400jar.htm
   *      下载   jna-4.0.0.jar   jna-platform-4.0.0.jar
   *      https://github.com/stephengold/j-ogg-all
   *      下载   已经release的 j-ogg-all-1.0.1.jar
   *      下载   https://github.com/jitsi/libjitsi/tree/v1.0 ,导入 idea .
   *      编译    mvn clean package -Dmaven.test.skip=true  , 生成  libjitsi-1.0-SNAPSHOT.jar
   */

  public OpusAudioPlayer(File audioFile) throws IOException {
    oggFile = new FileStream(new RandomAccessFile(audioFile, "r"));
    opusState = Opus.decoder_create(INPUT_BITRATE, 1);
  }

  private byte[] decode(byte[] packetData) {
    int frameSize = Opus.decoder_get_nb_samples(opusState, packetData, 0, packetData.length);
    int decodedSamples = Opus.decode(opusState, packetData, 0, packetData.length, decodeBuffer.array(), 0, frameSize, 0);
    if (decodedSamples < 0) {
      decodeBuffer.clear();
      return null;
    }
    decodeBuffer.position(decodedSamples * 2); // 2 bytes per sample
    decodeBuffer.flip();

    byte[] decodedData = new byte[decodeBuffer.remaining()];
    decodeBuffer.get(decodedData);
    decodeBuffer.flip();
    return decodedData;
  }

  public void play() {
    int totalDecodedBytes = 0;
    try {
      SourceDataLine speaker = AudioSystem.getSourceDataLine(audioFormat);
      speaker.open();
      speaker.start();
      System.out.println(" 开始播放音乐 music is playing . . .");
      int i = 0 ;
      for (LogicalOggStream stream : (Collection<LogicalOggStream>) oggFile.getLogicalStreams()) {
        byte[] nextPacket = stream.getNextOggPacket();
        while (nextPacket != null) {
          byte[] decodedData = decode(nextPacket);
          if(decodedData != null) {
            // Write packet to SourceDataLine
            if (i % 100 == 1) {
              if (i % 1500 == 1){
                System.out.println(" ♪♫♩");
              } else {
                System.out.print(" ♪♫♩");
              }
            }
            speaker.write(decodedData, 0, decodedData.length);
            totalDecodedBytes += decodedData.length;
          }
          nextPacket = stream.getNextOggPacket();
          i++;
        }
      }

      speaker.drain();
      speaker.close();
      System.out.println(String.format("Decoded to %d bytes", totalDecodedBytes));
    } catch (Exception e) {
      System.out.println("\n music over!");
    }
  }

  public static void main(String[] args) {
    try {
      OpusAudioPlayer opusAudioPlayer;
      for (int  i=0; i<3 ;i++){
        opusAudioPlayer = new OpusAudioPlayer(new File("src/main/song/229159131239188141233131129230172162.opus"));
        opusAudioPlayer.play();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}