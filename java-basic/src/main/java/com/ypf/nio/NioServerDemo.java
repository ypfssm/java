package com.ypf.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author shuaifei
 */
public class NioServerDemo {

    public static void main(String[] args) throws IOException {
        // 1. 创建一个 Selector
        Selector selector = Selector.open();
        // 2. 要监听的每一个端口都需要有一个 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        // 3. 绑定一个端口
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(12345));

        /**
         * SelectionKey 代表这个通道（Channel）在该 Selector 上的这个注册事件发生时的返回值。
         * 我们可以通过 SelectionKey 获得之前注册的 Channel，以及设置的 KeyAttachment。
         * SelectionKey 还可以用于取消通道的注册。
         */
        // 4. 注册 accept 事件
        SelectionKey selectionKey = serverSocketChannel.register(
                selector, SelectionKey.OP_ACCEPT);

        // 5. 申请一块缓存区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(true){
            // 6. 调用 select 轮询注册的事件。
            // select 方法会阻塞，直到至少有一个已注册的事件发生
            selector.select();
            // 7. 处理已注册的事件，比如：可读事件、可写事件。
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            SelectionKey key;
            while(iterator.hasNext()){
                key = iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    // 相当于从 ServerSocket 中 accept 一个 socket
                    SocketChannel socketChannel = channel.accept();
                    // 设为非阻塞
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    buffer.clear();
                    channel.read(buffer);
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        System.out.print((char)buffer.get());
                    }
                    // attach 一个对象来保存写入进度
                    channel.register(selector, SelectionKey.OP_WRITE)
                            .attach(new Task());
                } else if(key.isWritable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    Task task = (Task) key.attachment();
                    ByteBuffer buf = task.getBuffer();
                    if(buf == null){
                        // cancel 会把 channel 也关掉
                        key.cancel();
                        // 取消掉 OP_WRITE ??? 如果不取消 OP_WRITE ，Server、Client 都会阻塞，为什么？？
                        key.interestOps(SelectionKey.OP_READ);
                    } else {
                        channel.write(buf);
                        // buf.hasRemaining() 最好判断一下，为了不阻塞可能一次没写完 ???
                    }
                }
                iterator.remove();
            }

        }

    }

    static class Task{
        String[] testList = {
                "测试1\r\n",
                "测试2\r\n",
                "测试3\r\n"
        };
        ByteBuffer buf;
        int pos = -1;

        public Task(){
            buf = ByteBuffer.allocate(1024);
        }

        public ByteBuffer getBuffer(){
            buf.clear();
            pos++;
            if(pos >= testList.length) {
                return null;
            }

            buf.put(testList[pos].getBytes());
            buf.flip();
            return buf;
        }
    }

}
