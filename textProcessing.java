import edu.stanford.nlp.tagger.maxent.*;
import java.util.*;

public class TextProcessing {
	
	static {
		tagger = new MaxentTagger("taggers/english-bidirectional-distsim.tagger");
	}
	
	String a;
	String b;
	static MaxentTagger tagger;
	String features="";
	
	TextProcessing(Tweet a, Tweet b)
	{
		this.a=a.content;
		this.b=b.content;
		
		
	}
	
	String getFeatures()
	{
		a=str_pre(a);
		b=str_pre(b);
		
		a=a.trim();
		b=b.trim();
		
		int length_a=a.length();
		int length_b=b.length();
		
		String a_final=a.charAt(0)+"";
		String b_final=b.charAt(0)+"";
		
		for(int i=1; i<length_a; i++)
		{
			if(a.charAt(i)==' ' && a.charAt(i-1)==' ')
			{
				
			}
			else
			{
				a_final+=a.charAt(i);
			}
		}
		
		for(int i=1; i<length_b; i++)
		{
			if(b.charAt(i)==' ' && b.charAt(i-1)==' ')
			{
				
			}
			else
			{
				b_final+=b.charAt(i);
			}
		}
		
		//System.out.println(a_final+"\n"+b_final);
		
		double trigram_feature=findWordTrigrams(a_final, b_final);
		double common_feature=findCommonWords(a_final, b_final);
		double word_bigram_feature=findBigrams(a_final, b_final);
		
		trigram_feature=Double.isNaN(trigram_feature)?0:trigram_feature;
		common_feature=Double.isNaN(common_feature)?0:common_feature;
		word_bigram_feature=Double.isNaN(word_bigram_feature)?0:word_bigram_feature;
		
		double[] f_array=new double[3];
		findNounsAdjectives(a_final, b_final, f_array);
		
		for(int i=0; i<3; i++)
		{
			f_array[i]=Double.isNaN(f_array[i])?0:f_array[i];
		}
		
		double proper_noun_feature=f_array[0];
		double common_noun_feature=f_array[1];
		double adjective_feature=f_array[2];
		
		return common_feature+" "+word_bigram_feature+" "+trigram_feature+" "+proper_noun_feature+" "+common_noun_feature+" "+adjective_feature;
	}
	
	public void findNounsAdjectives(String a, String b, double[] f_array)
	{
		
		String a_tagged=tagger.tagString(a);
		String b_tagged=tagger.tagString(b);
		
		String[] a_words=a.split(" ", 0);
		String[] b_words=b.split(" ", 0);
		String[] a_tagged_words=a_tagged.split(" ", 0);
		String[] b_tagged_words=b_tagged.split(" ", 0);
		
		Set<String> A_NP=new HashSet<String>();
		Set<String> B_NP=new HashSet<String>();
		Set<String> U_NP=new HashSet<String>();
		Set<String> I_NP=new HashSet<String>();
		
		Set<String> A_NC=new HashSet<String>();
		Set<String> B_NC=new HashSet<String>();
		Set<String> U_NC=new HashSet<String>();
		Set<String> I_NC=new HashSet<String>();
		
		Set<String> A_ADJ=new HashSet<String>();
		Set<String> B_ADJ=new HashSet<String>();
		Set<String> U_ADJ=new HashSet<String>();
		Set<String> I_ADJ=new HashSet<String>();
		
		int a_tagged_words_length=a_tagged_words.length;
		int b_tagged_words_length=b_tagged_words.length;
		for(int i=0; i<a_tagged_words_length; i++)
		{
			if(a_tagged_words[i].indexOf("_NNP")!=-1)
			{
				A_NP.add(a_words[i]);
			}
			else if(a_tagged_words[i].indexOf("_NN")!=-1)
			{
				A_NC.add(a_words[i]);
			}
			else if(a_tagged_words[i].indexOf("_JJ")!=-1)
			{
				A_ADJ.add(a_words[i]);
			}
			
		}
		for(int i=0; i<b_tagged_words_length; i++)
		{
			if(b_tagged_words[i].indexOf("_NNP")!=-1)
			{
				B_NP.add(b_words[i]);
			}
			else if(b_tagged_words[i].indexOf("_NN")!=-1)
			{
				B_NC.add(b_words[i]);
			}
			else if(b_tagged_words[i].indexOf("_JJ")!=-1)
			{
				B_ADJ.add(b_words[i]);
			}
			
		}
		/*
		
	    System.out.println(a_tagged);
	 	System.out.println(b_tagged);
		 
		*/
		
		union(A_NP, B_NP, U_NP);
		union(A_NC, B_NC, U_NC);
		union(A_ADJ, B_ADJ, U_ADJ);
		
		intersection(A_NP, B_NP, I_NP);
		intersection(A_NC, B_NC, I_NC);
		intersection(A_ADJ, B_ADJ, I_ADJ);
		
		f_array[0]=( ((double)I_NP.size()) / U_NP.size());
		f_array[1]=( ((double)I_NC.size()) / U_NC.size());
		f_array[2]=( ((double)I_ADJ.size()) / U_ADJ.size());
		
	}
	
	public double findBigrams(String a, String b)
	{
		int a_length=a.length();
		int b_length=b.length();
		
		String[] a_words=a.split(" ", 0);
		String[] b_words=b.split(" ", 0);
		
		int a_words_length=a_words.length;
		int b_words_length=b_words.length;
		
		Set<String> A=new HashSet<String>();
		Set<String> B=new HashSet<String>();
		
		for(int i=1; i<a_words_length; i++)
		{
			A.add(a_words[i-1]+a_words[i]);			
		}
		for(int i=1; i<b_words_length; i++)
		{
			B.add(b_words[i-1]+b_words[i]);			
		}
		
		Set<String> U=new HashSet<String>();
		Set<String> I=new HashSet<String>();
		
		union(A, B, U);
		intersection(A, B, I);
		
		return ( ((double)I.size()) / U.size());
		
	}
	
	public double findCommonWords(String a, String b)
	{
		int a_length=a.length();
		int b_length=b.length();
		
		Set<String> A=new HashSet<String>();
		Set<String> B=new HashSet<String>();
		
		int prev=-1;
		int i=0;
		for(i=0; i<a_length; i++)
		{
			if(a.charAt(i)==' ')
			{
				A.add(a.substring(prev+1, i));
				prev=i;
			}
		}
		A.add(a.substring(prev+1, i));
		//System.out.println(A);
		
		prev=-1;
		for(i=0; i<b_length; i++)
		{
			if(b.charAt(i)==' ')
			{
				B.add(b.substring(prev+1, i));
				prev=i;
			}
		}
		B.add(b.substring(prev+1, i));
		//System.out.println(B);
		
		Set<String> U=new HashSet<String>();
		Set<String> I=new HashSet<String>();
		
		union(A, B, U);
		intersection(A, B, I);
		
		return ( ((double)I.size()) / U.size());
	}
	
	
	public double findWordTrigrams(String a, String b)
	{
		int a_length=a.length();
		int b_length=b.length();
		
		Set<String> A=new HashSet<String>();
		Set<String> B=new HashSet<String>();
		
		for(int i=2; i<a_length; i++)
		{
			A.add(a.substring(i-2, i+1));
		}
		for(int i=2; i<b_length; i++)
		{
			B.add(b.substring(i-2, i+1));
		}
		
		Set<String> U=new HashSet<String>();
		Set<String> I=new HashSet<String>();
		
		union(A, B, U);
		intersection(A, B, I);
		
		return ( ((double)I.size()) / U.size());
		
	}

	public  String str_pre(String input_string) //removes delimiters and converts to lower case
	{
		String delimiters = "_#,.!?/&-:;@\\\'";
		
		String output_string="";
		int length=input_string.length();
		for(int i=0; i<length; i++)
		{
			if(delimiters.indexOf(input_string.charAt(i))==-1)
			{
				output_string+=input_string.charAt(i);
			}
			else
			{
				output_string+=" ";
			}
		}
		
		return output_string;
	}
	
	public void union(Set<String> A, Set<String> B, Set<String> U)
	{
		for(Iterator<String> iter=A.iterator(); iter.hasNext();)
		{
			U.add(iter.next());
		}
		for(Iterator<String> iter=B.iterator(); iter.hasNext();)
		{
			U.add(iter.next());
		}
	}
	
	public void intersection(Set<String> A, Set<String> B, Set<String> I)
	{
		for(Iterator<String> iter=A.iterator(); iter.hasNext();)
		{
			String temp=iter.next();
			if(B.contains(temp))
			{
				I.add(temp);
			}
		}
		
	}
}
	
