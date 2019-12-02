package com.ypf.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * https://www.cnblogs.com/woshijpf/articles/3723364.html
 *
 *  Buffer 中的 Capacity,Position 和 Limit 三个概念：
 *      1. Capacity 在读写模式下都是固定的，就是我们分配的缓冲大小,
 *      2. Position 类似于读写指针，表示当前读(写)到什么位置,
 *      3. Limit 在写模式下表示最多能写入多少数据，此时和Capacity相同，在读模式下表示最多能读多少数据，此时和缓存中的实际数据大小相同。
 *
 * @author shuaifei
 */
public class BufferForFlipDemo {

    private static final int SIZE = 20;

    public static void main(String[] args) throws Exception {
        // 获取通道，该通道允许写操作
        FileChannel fc = new FileOutputStream("data.txt").getChannel();
        // 将字节数组包装到缓冲区中
        fc.write(ByteBuffer.wrap("Some text".getBytes()));
        // 关闭通道
        fc.close();

        // 随机读写文件流创建的管道
        fc = new RandomAccessFile("data.txt", "rw").getChannel();
        // fc.position()计算从文件的开始到当前位置之间的字节数
        System.out.println("此通道的文件位置：" + fc.position());
        // 设置此通道的文件位置,fc.size()此通道的文件的当前大小,该条语句执行后，通道位置处于文件的末尾
        fc.position(fc.size());
        // 在文件末尾写入字节
        fc.write(ByteBuffer.wrap("Some more".getBytes()));
        fc.close();

        // 用通道读取文件
        fc = new FileInputStream("data.txt").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);
        // 将文件内容读到指定的缓冲区中
        fc.read(buffer);
        /**
         * buffer.flip() 一定得有，如果没有，就是从文件最后开始读取的，当然读出来的都是系统设置的默认值字符( char = 0)。
         * 通过 buffer.flip() 这个方法，就能把 buffer 的当前位置（position）更改为 buffer 缓冲区的第一个位置（position = 0）。
         */
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char)buffer.get());
        }
        fc.close();

    }

}
