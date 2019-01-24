import parser_kpa.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class parser_KPA{
	public static void main(String[] args) throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader inputStream = null;
		String l,fichero,opcion;
		fichero = new String();
		opcion = new String();
		System.out.println("args="+String.valueOf(args.length));
		if(args.length==1){
			opcion = "TXORI";
			fichero = args[args.length-1];
		} else if(args.length==2){
			opcion = args[args.length-1];
			fichero = args[args.length-2];
		}  
		System.out.println("fichero="+fichero);
		// lee el contenido del fichero que contiene el algoritmo, para pasarselo al parser como un string
		try{
			inputStream = new BufferedReader(new FileReader(fichero));
			while((l=inputStream.readLine())!=null){		
			  sb.append(l+"\n");
			}	
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}	
		parser_kpa.parser g = new parser_kpa.parser(sb.toString().replace("\n"," "),opcion);// en el parametro opcion se pasa el lenguaje maquina/objeto
		g.parserascendente();
	}
}
