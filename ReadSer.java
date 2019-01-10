import java.io.*;
import java.lang.*;
 

public class ReadSer {
	
	public static void main(String args[])
	{
		try
		{
			FileInputStream fis=new FileInputStream("tweets.ser");
			ObjectInputStream ois=new ObjectInputStream(fis);
			
			ois.readObject(); //Throwing the Dummy Object Away
			
			Tweet x;
			while((x=(Tweet)ois.readObject())!=null)
			{
				x.printTweet();
			}
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
