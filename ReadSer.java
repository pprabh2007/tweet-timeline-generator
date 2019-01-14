import java.io.*;
import java.util.*; 

import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook; 

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
		
		try
		{
			Workbook workbook = new HSSFWorkbook();
			OutputStream writer = new FileOutputStream("Datasheet.xls");
			Sheet sheet = workbook.createSheet("Features");
			
			int number_of_tweets=tweet_list.size();
			Row[] rows=new Row[number_of_tweets];
			
			for(int i=0; i<number_of_tweets; i++)
			{
				rows[i] = sheet.createRow(i);
				Cell itself=rows[i].createCell(i);
			}
			
			for(int i=0; i<number_of_tweets; i++)
			{
				for(int j=i; j<number_of_tweets; j++)
				{
					TextProcessing tps=new TextProcessing(tweet_list.get(i), tweet_list.get(j));
					String features_of_pair=(tps.getFeatures());
					
					 Cell cell_one = rows[i].createCell(j);
					 cell_one.setCellValue(features_of_pair); 
					 	
					 Cell cell_two = rows[j].createCell(i);
					 cell_two.setCellValue(features_of_pair); 
					 System.out.println(i*number_of_tweets+j);
				}
			}
			workbook.write(writer);
			writer.close();
			workbook.close();
		}
		catch(Exception  e)
		{
			e.printStackTrace();
		}
		
	}

}
