package javaee.jms.queue;

import javax.jms.Message;
import javax.jms.MessageListener;

import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
public class AsyncMesConsumer{



		
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/queue/test";
	
	private static final String DEFAULT_USERNAME = "test7777";
	private static final String DEFAULT_PASSWORD = "123456";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "remote://localhost:4447";
      
    public static class AsynMesListener implements MessageListener
{
    public void onMessage(Message msg)
    {
        TextMessage tm = (TextMessage) msg;
        try {
            System.out.println("onMessage, recv text=" + tm.getText());
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}

        
  public static void main(String[] args) throws Exception 
  {
	  Context context=null;
	  Connection connection=null;
	  TextMessage msg=null;
	  AsynMesListener l=null;
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
			MessageConsumer consumer = session.createConsumer(destination);
			l=new AsynMesListener();
			consumer.setMessageListener(l);
			connection.start();
			// 等待30秒退出
			CountDownLatch latch = new CountDownLatch(1);	
			while (msg == null) {		
				System.out.println("开始从JBOSS端接收信息-----");
		    	latch.await(5, TimeUnit.SECONDS);
			 }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}


 


