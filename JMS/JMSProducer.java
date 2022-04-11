package javaee.jms.queue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.*;
/**
 * <p>Description:JMS客户端消息生产者 </p>
 */

class SayHello extends TimerTask {
    public void run() {
       
       for (int i = 0; i < 5; i++) {
       
       System.out.println("Hello World!"+i); 
       }
       
       System.out.println("---------------------------------"); 
    }
}




public class JMSProducer {
	private static final Logger log = Logger.getLogger(JMSProducer.class.getName());

	private static final String DEFAULT_MESSAGE = "zhang jin he nb";
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/queue/test";
	private static final String DEFAULT_MESSAGE_COUNT = "10";

	private static final String DEFAULT_USERNAME = "test7777";
	private static final String DEFAULT_PASSWORD = "123456";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "remote://localhost:4447";
	
	 
	public static void main(String[] args) throws Exception {
		Context context=null;
		Connection connection=null;
		try {
			// 设置上下文的JNDI查找
			System.out.println("设置JNDI访问环境信息也就是设置应用服务器的上下文信息!");
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);// 该KEY的值为初始化Context的工厂类,JNDI驱动的类名
			env.put(Context.PROVIDER_URL,  PROVIDER_URL);// 该KEY的值为Context服务提供者的URL.命名服务提供者的URL
			env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
			env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);//应用用户的登录名,密码.
			// 获取到InitialContext对象.
			context = new InitialContext(env);
			System.out.println ("获取连接工厂!");
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(DEFAULT_CONNECTION_FACTORY);
			System.out.println ("获取目的地!");
			Destination destination = (Destination) context.lookup(DEFAULT_DESTINATION);

			// 创建JMS连接、会话、生产者和消费者
			connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(destination);
			connection.start();

//			int count = Integer.parseInt(DEFAULT_MESSAGE_COUNT);
			// 发送特定数目的消息
			
//			Date date = new Date();
//			System.out.println(date);

//			TextMessage message = null;
//			for (int i = 0; i < count; i++) {
//				Date date = new Date();
//				System.out.println(date);
//				message = session.createTextMessage("这是第"+i+"条消息 ------"+DEFAULT_MESSAGE+"-----"+date);
//				producer.send(message);
//				System.out.println ("message:"+message);
//				System.out.println ("message:"+DEFAULT_MESSAGE);
//			}
//			// 等待30秒退出
			TextMessage message = null;
			int flag = 1;
			do{
			long t= System.currentTimeMillis();
		    long end = t+5000; //运行5秒
		    int count = 1;
		    while(true){
		    
		    if(System.currentTimeMillis() < end) {
		    	Date date = new Date();
		    	System.out.println(date);
		    	message = session.createTextMessage("这是第"+count+"条消息 ------"+DEFAULT_MESSAGE+"-----时间为："+date);
				producer.send(message);
				count++;
				try {
		            Thread.sleep(100);
		        } catch(InterruptedException e) {
		          
		        }
				System.out.println("这是第"+count+"条消息 ------"+DEFAULT_MESSAGE+"-----时间为："+date);
		     }else{
		    	 System.out.println("到5秒钟了--------------");
		    	 break;		    	 
		     }
		    
		    }
		    System.out.println("jeishu................"); 
		    System.out.println("请输入是否继续，1表示继续，0表示退出"); 
			Scanner input = new Scanner(System.in); 
			flag = input.nextInt();
			
			}while(flag==1);
			System.out.println("退出"); 

			CountDownLatch latch = new CountDownLatch(1);
		
			latch.await(30, TimeUnit.SECONDS);			
		} catch (Exception e) {
			log.severe(e.getMessage());
			throw e;
		} finally {
			if (context != null) {
				context.close();
			}
			// 关闭连接负责会话,生产商和消费者
			if (connection != null) {
				connection.close();
			}
		}
	}
}

