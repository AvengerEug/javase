package io.masterslavereactor;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException {
        MainReactor reactor = new MainReactor("127.0.0.1", 7788);
        // 简单的执行run方法
        new Thread(reactor).start();
    }
}
