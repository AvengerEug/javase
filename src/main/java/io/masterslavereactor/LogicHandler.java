package io.masterslavereactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogicHandler {

    private static final String SPECIAL_COMMAND = "avengerEug";

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5,
            10,
            10,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(30),
            Executors.defaultThreadFactory(),
            (r, executor) -> System.out.println("自定义拒绝策略")
    );

    public LogicHandler() {
    }

    public void run(SelectionKey registerKey) {
        System.out.println("读事件发生了");
        if (registerKey.isReadable()) {
            try {
                readData(registerKey);
            } catch (IOException e) {
                // 客户端强制关闭连接了，此处catch住异常，由服务器断开与客户端的连接
                try {
                    closeClientConnection(registerKey);
                } catch (IOException e1) {
                }
            }
        } else {
            System.out.println("register key 绑定的事件未知");
        }
    }

    private void writeData(SelectionKey registerKey) throws IOException {
        SocketChannel sc = (SocketChannel) registerKey.channel();
        String content = "我收到你的消息了";
        System.out.println(Thread.currentThread().getName() + "开始向客户端发送数据, 内容：" + content);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.put(content.getBytes());
        byteBuffer.flip();
        sc.write(byteBuffer);
//        registerKey.interestOps(SelectionKey.OP_READ);
//        registerKey.selector().wakeup();
    }

    private String readData(SelectionKey registerKey) throws IOException {
        System.out.println(Thread.currentThread().getName() + " 开始读取客户端发送给服务端的数据");
        SocketChannel sc = (SocketChannel) registerKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
        StringBuffer stringBuffer = new StringBuffer();
        int length;
        while ((length = sc.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.limit() - byteBuffer.position()];
            byteBuffer.get(bytes);
            stringBuffer.append(new String(bytes));
            byteBuffer.clear();
        }

        if (length == -1) {
            closeClientConnection(registerKey);
            return null;
        } else {
            threadPoolExecutor.execute(() -> {
                System.out.println("接收到数据，执行业务逻辑");
                if (SPECIAL_COMMAND.equals(stringBuffer.toString())) {
                    // 如果命令匹配，则执行对应的特殊操作
                    execLogic();
                } else {
                    System.out.println("接收到客户端发送的消息：" + stringBuffer.toString());
//                registerKey.interestOps(SelectionKey.OP_WRITE);
//                registerKey.selector().wakeup();
                }
                try {
                    writeData(registerKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }

        return stringBuffer.toString();
    }

    private void closeClientConnection(SelectionKey registerKey) throws IOException {
        System.out.println("客户端断开了连接, 取消对应的事件, 并关闭对应的channel");
        SocketChannel sc = (SocketChannel) registerKey.channel();
        registerKey.cancel();
        sc.close();
    }

    private void execLogic() {
        System.out.println("客户端发送了avengerEug指令，我要输出一段sql：SELECT * FROM user");
    }
}
