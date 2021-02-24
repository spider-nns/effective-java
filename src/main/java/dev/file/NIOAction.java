package dev.file;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author lgc
 * @date 2021/2/24
 */
public class NIOAction {

    public static final Logger log = LoggerFactory.getLogger(NIOAction.class);

    public static void main(String[] args) {
        mappedByteBuffer();
    }

    static void mappedByteBuffer() {
        String mbb = "/Users/spider/Desktop/jf/mbb.txt";
        try {
            File file = new File(mbb);
            if (!file.exists()) file.createNewFile();

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.write(Byte.MAX_VALUE);
            FileChannel channel = randomAccessFile.getChannel();
            FileDescriptor fd = randomAccessFile.getFD();
            long filePointer = randomAccessFile.getFilePointer();
            log.info("channel :{}", channel);
            log.info("fd:valid :{}", fd.valid());
            log.info("filePointer :{}", filePointer);
            log.info("channel is open :{}",channel.isOpen());

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] ftBytes = FileUtils.readFileToByteArray(file);
            log.info("Files readAllBytes :{}", Arrays.toString(fileBytes));
            log.info("FileUtils readFileToByteArray :{}", Arrays.toString(ftBytes));
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            mappedByteBuffer.force();
            mappedByteBuffer.put(fileBytes);
            mappedByteBuffer.force();
            channel.close();
            randomAccessFile.close();
            mappedByteBuffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
    }
}
