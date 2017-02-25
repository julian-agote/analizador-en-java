import parser_jkl.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class parser_JKL{
	public static void main(String[] args) throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader inputStream = null;
		String l;
		try{
			inputStream = new BufferedReader(new FileReader(args[args.length-1]));
			while((l=inputStream.readLine())!=null){		
			  sb.append(l+"\n");
			}	
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}	
		parser_jkl.parser g = new parser_jkl.parser(sb.toString().replace("\n"," "));
		g.parserascendente();
	}
}
