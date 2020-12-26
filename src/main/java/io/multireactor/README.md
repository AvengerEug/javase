## Reactor 之 多线程模型
* 多线程（**服务器多线程处理业务逻辑**）模型中，一个线程总共要做如下事情：

  > 1、接收客户端的链接
  >
  > 2、读取已连接上的客户端发来的数据
  >
  > 3、读到数据后要解码  ----- 异步（将channel的数据读取到buffer中，最终转成一个字符串）
  >
  > 4、处理业务逻辑  ----- 异步
  >
  > 5、编码  -----  异步（将字符串）
  >
  > 6、响应客户端（将要给客户端响应数据的字符串解码成一个byte数组）

  它能**分心**，将读取客户端发来的消息和发送消息给客户端的这两个操作都可以变成异步（使用线程池来代替）

* 实现核心：与nio实现的聊天室差不多，只不过它利用了selectedKey的attach附加对象方法来传递一些对象，而这些对象携带了当前selectedKey绑定的channel、socker对象，我们可以根据attach附加对象里面的内容做实际的业务逻辑处理，只不过这个业务逻辑处理是异步的方式。但这里要注意一点：就是一定要保证**解码、执行业务逻辑、编码**这三个步骤异步处理，不能将从channel中读取数据和使用channel写数据到客户端的过程中也使用异步的方式，这就有可能出现多个线程同时操作同一个channel，出现并发问题，最终数据会错乱（比如线程1在往客户端写数据过程中，线程2一直在往channel中读数据，造成线程2写的数据被线程1给写给客户端了）。总而言之就是：要避免多个线程同时使用一个channel
* 缺点：只有一个selector来处理事件，若并发量特别高时，必须要等到下一个轮训才能被执行