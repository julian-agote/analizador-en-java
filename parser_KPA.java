import parser_kpa.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class parser_KPA{
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
		parser_kpa.parser g = new parser_kpa.parser(sb.toString().replace("\n"," "));
		g.parserascendente();
	}
}
