import java.io.*;
import java.util.*; 

public class ReadSer {
	
	public static void main(String args[])
	{
		ArrayList<Tweet> tweet_list=new ArrayList<Tweet>();
		try
		{
			FileInputStream fis=new FileInputStream("tweets.ser");
			ObjectInputStream ois=new ObjectInputStream(fis);
			
			ois.readObject(); //Throwing the Dummy Object Away
				
			Tweet x;
			
			while((x=(Tweet)ois.readObject())!=null)
			{
				tweet_list.add(x);
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
		
		int number_of_tweets=tweet_list.size();
		
		for(int i=0; i<number_of_tweets; i++)
		{
			tweet_list.get(i).printTweet();
		}
		
		for(int i=0; i<number_of_tweets; i++)
		{
			for(int j=i+1; j<number_of_tweets; j++)
			{
				TextProcessing tps=new TextProcessing(tweet_list.get(i), tweet_list.get(j));
				String features_of_pair=(tps.getFeatures());

			}
		}
		
		
		
	}

}
