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
 * <p>Description:JMS�ͻ�����Ϣ������ </p>
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
			// ���������ĵ�JNDI����
			System.out.println("����JNDI���ʻ�����ϢҲ��������Ӧ�÷���������������Ϣ!");
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);// ��KEY��ֵΪ��ʼ��Context�Ĺ�����,JNDI����������
			env.put(Context.PROVIDER_URL,  PROVIDER_URL);// ��KEY��ֵΪContext�����ṩ�ߵ�URL.���������ṩ�ߵ�URL
			env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
			env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);//Ӧ���û��ĵ�¼��,����.
			// ��ȡ��InitialContext����.
			context = new InitialContext(env);
			System.out.println ("��ȡ���ӹ���!");
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(DEFAULT_CONNECTION_FACTORY);
			System.out.println ("��ȡĿ�ĵ�!");
			Destination destination = (Destination) context.lookup(DEFAULT_DESTINATION);

			// ����JMS���ӡ��Ự�������ߺ�������
			connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(destination);
			connection.start();

//			int count = Integer.parseInt(DEFAULT_MESSAGE_COUNT);
			// �����ض���Ŀ����Ϣ
			
//			Date date = new Date();
//			System.out.println(date);

//			TextMessage message = null;
//			for (int i = 0; i < count; i++) {
//				Date date = new Date();
//				System.out.println(date);
//				message = session.createTextMessage("���ǵ�"+i+"����Ϣ ------"+DEFAULT_MESSAGE+"-----"+date);
//				producer.send(message);
//				System.out.println ("message:"+message);
//				System.out.println ("message:"+DEFAULT_MESSAGE);
//			}
//			// �ȴ�30���˳�
			TextMessage message = null;
			int flag = 1;
			do{
			long t= System.currentTimeMillis();
		    long end = t+5000; //����5��
		    int count = 1;
		    while(true){
		    
		    if(System.currentTimeMillis() < end) {
		    	Date date = new Date();
		    	System.out.println(date);
		    	message = session.createTextMessage("���ǵ�"+count+"����Ϣ ------"+DEFAULT_MESSAGE+"-----ʱ��Ϊ��"+date);
				producer.send(message);
				count++;
				try {
		            Thread.sleep(100);
		        } catch(InterruptedException e) {
		          
		        }
				System.out.println("���ǵ�"+count+"����Ϣ ------"+DEFAULT_MESSAGE+"-----ʱ��Ϊ��"+date);
		     }else{
		    	 System.out.println("��5������--------------");
		    	 break;		    	 
		     }
		    
		    }
		    System.out.println("jeishu................"); 
		    System.out.println("�������Ƿ������1��ʾ������0��ʾ�˳�"); 
			Scanner input = new Scanner(System.in); 
			flag = input.nextInt();
			
			}while(flag==1);
			System.out.println("�˳�"); 

			CountDownLatch latch = new CountDownLatch(1);
		
			latch.await(30, TimeUnit.SECONDS);			
		} catch (Exception e) {
			log.severe(e.getMessage());
			throw e;
		} finally {
			if (context != null) {
				context.close();
			}
			// �ر����Ӹ���Ự,�����̺�������
			if (connection != null) {
				connection.close();
			}
		}
	}
}

