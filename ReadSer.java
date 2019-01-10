import java.io.*;
import java.lang.*;
 

public class ReadSer {
	
	public static void main(String args[])
	{
		try
		{
			FileInputStream fis=new FileInputStream("tweets.ser");
			ObjectInputStream ois=new ObjectInputStream(fis);
			
			Tweet x=(Tweet)ois.readObject();
			x.printTweet();
			
			fis.close();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}

}
