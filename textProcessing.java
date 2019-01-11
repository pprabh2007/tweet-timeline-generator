import java.util.*;

public class textProcessing {
	
	public static void main(String args[])
	{
		/*		
		System.out.println(findCommonWords("thou shall perish", "excuse shall hhey"));
		System.exit(0);
		*/
		
		/*
		Set<String> U=new HashSet<String>();
		Set<String> A=new HashSet<String>();
		A.add("hello");
		Set<String> B=new HashSet<String>();
		B.add("mello");
		
		//union(A, B, U);
		//intersection(A, B, U);
		System.out.println(U);
		System.exit(0);
		*/
		
		
		String a="   !@     how     are     you            ";
		String b="how   are  !@    you      ";
		

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
		
		
		double trigram_feature=findWordTrigrams(a_final, b_final);
		
		System.out.println(a_final);
		System.out.println(b_final);
		System.out.println(trigram_feature);
		
	}
	
	public static double findCommonWords(String a, String b)
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
	
	
	public static double findWordTrigrams(String a, String b)
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
	
	public static String str_pre(String input_string) //removes delimiters and converts to lower case
	{
		String delimiters = ",.!?/&-:;@\\\'";
		
		String output_string="";
		int length=input_string.length();
		for(int i=0; i<length; i++)
		{
			if(delimiters.indexOf(input_string.charAt(i))==-1)
			{
				output_string+=input_string.charAt(i);
			}
		}
		
		return output_string.toLowerCase();
	}
	
	public static void union(Set<String> A, Set<String> B, Set<String> U)
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
	
	public static void intersection(Set<String> A, Set<String> B, Set<String> I)
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
/*
def str_pre(input_string): #removes delimiters and converts to lower case
	

	delimiters = [",", ".", "!", "?", "/", "&", "-", ":", ";", "@", "'"]	
	output_string = ""

	for ch in input_string:
		
		if(ch in delimiters):
			output_string = output_string +" "
		else:
			output_string= output_string+ch


	return (output_string.lower())


def word_comp(wordArr1, wordArr2): 

	'''
	1. finds proportion of similar words 
	2. pass word array of the string obtained from delimiter-free lower case string 

	'''

	A=set(wordArr1)
	B=set(wordArr2)

	I=set.intersection(A, B)
	U=set.union(A, B)

	return (len(I)/len(U))


def word_bigram(wordArr1, wordArr2):

	'''
	1. finds proportion of similar word-bigrams 
	2. pass word array of the string obtained from delimiter-free lower case string 

	'''
	bigramArr1=[]
	bigramArr2=[]

	length_1=len(wordArr1)
	length_2=len(wordArr2)

	for i in range(length_1-1):
		bigramArr1.append(wordArr1[i]+wordArr1[i+1])

	for i in range(length_2-1):
		bigramArr2.append(wordArr2[i]+wordArr2[i+1])

	A=set(bigramArr1)
	B=set(bigramArr2)

	I=set.intersection(A, B)
	U=set.union(A, B)

	return (len(I)/ len (U))


def char_trigram(wordArr1, wordArr2):

	'''
	1. finds proportion of similar character-trigrams 
	2. pass word array of the string obtained from delimiter-free lower case string 

	'''

	string_1=" ".join(wordArr1)
	string_2=" ".join(wordArr2)

	trigramArr1=[]
	trigramArr2=[]

	length_1=len(string_1)
	length_2=len(string_2)


	for i in range(length_1-2):
		trigramArr1.append(string_1[i]+string_1[i+1]+string_1[i+2])

	for i in range(length_2-2):
		trigramArr2.append(string_2[i]+string_2[i+1]+string_2[i+2])

	A=set(trigramArr1)
	B=set(trigramArr2)

	I=set.intersection(A, B)
	U=set.union(A, B)

	return (len(I)/len(U))
}
*/