package parser_jkl;
/*************************************************
* Representa la cadena de entrada, indice es un apuntador al caracter en curso
* leersigterm devuelve la siguiente unidad lexica
**************************************************/
public class TEntrada {
    int indice;
    String cad;
    TEntrada() {
        indice = 0;
		cad = new String();
    }
    void gordeCad(String pcad){
		cad = new String(pcad);
	}	
	String faltaLeer(){
		return cad.substring(indice);
	}	
    TElemEnt leersigterm(TElemEnt p_ant, TTablaSimbolos ts) {
    	char ch;
        int val = 0;
		double valf;
        int i, longcad;
        char car[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        String[] pal_clave = "programa,aldagaia,hasi,nahiko,irakurri,erakutzi,idatzi,lerro-berria,gertatu-ezkero,egin,gertatzen-den-bitartian,hemendik-hasita,heldu-arte".split(",");
        StringBuffer lexema = new StringBuffer();
        TElemEnt UdLex = new TElemEnt();
        if (indice == cad.length()) { // se ha llegado al final, devolver $
            UdLex.token = new String("$");
            UdLex.atributo = -1;
            return UdLex;
        }
        // saltar espacios, tab, cr, lf
        ch = cad.charAt(indice);
        while (ch == ' ' || ch == Character.LINE_SEPARATOR || ch == '\t') {
            indice++;
            if (indice == cad.length()) { // se ha llegado al final, devolver $
                UdLex.token = new String("$");
                UdLex.atributo = -1;
                return UdLex;
            }
            ch = cad.charAt(indice);
        }
		//saltar comentarios
		if(ch=='/' && cad.charAt(indice+1)=='*'){
			indice+=2;
			ch = cad.charAt(indice);
			while(!(ch=='*'&& cad.charAt(indice+1)=='/')){
				indice++;
				ch = cad.charAt(indice);
	        }
			indice+=2;	
			if (indice == cad.length()) { // se ha llegado al final, devolver $
				UdLex.token = new String("$");
				UdLex.atributo = -1;
				return UdLex;
			}
	        ch = cad.charAt(indice);
	        while (ch == ' ' || ch == Character.LINE_SEPARATOR || ch == '\t') {
	            indice++;
	            if (indice == cad.length()) { // se ha llegado al final, devolver $
	                UdLex.token = new String("$");
	                UdLex.atributo = -1;
	                return UdLex;
	            }
	            ch = cad.charAt(indice);
	        }
		}			
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||(ch >= '0' && ch <= '9') ||
                   ch == '-' || ch == '_') {
                lexema.append(ch);
                indice++;
                ch = cad.charAt(indice);
            }
            for (int x = 0; x < pal_clave.length; x++) // ver si el identificador es una palabra clave
                if (lexema.toString().compareToIgnoreCase(pal_clave[x]) == 0) {
                    UdLex.token = new String(pal_clave[x]);
                    UdLex.atributo = -1;
                    return UdLex;
                }
			if (lexema.toString().compareToIgnoreCase("egia") == 0 || lexema.toString().compareToIgnoreCase("gezurra") == 0){ 	
				UdLex.token = new String("BOOL");
			}else if (lexema.toString().compareToIgnoreCase("edo") == 0){ 	
				UdLex.token = new String("OP_OR");
			}else if (lexema.toString().compareToIgnoreCase("eta") == 0){ 	
				UdLex.token = new String("OP_AND");
			}else if (lexema.toString().compareToIgnoreCase("ez-da") == 0){ 	
				UdLex.token = new String("OP_NOT");
            }else{
				UdLex.token = new String("ID");
			}
            UdLex.atributo = ts.insertar(lexema.toString());			
        } else {
            switch (ch) {
            case '+':
				UdLex.token = new String("OP_ADIT");
				UdLex.atributo = ts.insertar("+");
				indice++;
				break;
            case '-':
				UdLex.token = new String("OP_ADIT");
				UdLex.atributo = ts.insertar("-");
				indice++;
				break;
            case '*':
				UdLex.token = new String("OP_MULT");
				UdLex.atributo = ts.insertar("*");
				indice++;
				break;
            case '/':
				UdLex.token = new String("OP_MULT");
				UdLex.atributo = ts.insertar("/");
				indice++;
				break;
            case ')':
				UdLex.token = new String("PAR_CER");
				UdLex.atributo = -1;
				indice++;
				break;
            case ';':
				UdLex.token = new String("P_COMA");
				UdLex.atributo = -1;
				indice++;
				break;
            case '.':
				UdLex.token = new String("PUNTO");
				UdLex.atributo = -1;
				indice++;
				break;
            case ',':
				UdLex.token = new String("COMA");
				UdLex.atributo = -1;
				indice++;
				break;
            case '[':
				UdLex.token = new String("COR_ABR");
				UdLex.atributo = -1;
				indice++;
				break;
            case ']':
				UdLex.token = new String("COR_CER");
				UdLex.atributo = -1;
				indice++;
				break;
            case '{':
				UdLex.token = new String("LLAVE_ABR");
				UdLex.atributo = -1;
				indice++;
				break;
            case '}':
				UdLex.token = new String("LLAVE_CER");
				UdLex.atributo = -1;
				indice++;
				break;
            case '$':
				UdLex.token = new String("OP_C");
				UdLex.atributo = -1;
				indice++;
				break;
            case '^':
				UdLex.token = new String("OP_EXP");
				UdLex.atributo = -1;
				indice++;
				break;
            case ':':
				UdLex.token = new String("ASIGN");
				UdLex.atributo = -1;
				indice++;
				if(cad.charAt(indice)=='=') indice++;
				break;
            case '<':
				UdLex.token = new String("OP_REL");
				UdLex.atributo = ts.insertar("<");
				indice++;
				if(cad.charAt(indice)=='='){
					indice++;
					UdLex.atributo = ts.insertar("<=");
				}else if(cad.charAt(indice)=='>'){
					indice++;
					UdLex.atributo = ts.insertar("<>");
				}	
				break;
            case '>':
				UdLex.token = new String("OP_REL");
				UdLex.atributo = ts.insertar(">");
				indice++;
				if(cad.charAt(indice)=='='){
					indice++;
					UdLex.atributo = ts.insertar(">=");
				}	
				break;
            case '=':
				UdLex.token = new String("OP_REL");
				UdLex.atributo = ts.insertar("=");
				indice++;
				if(cad.charAt(indice)=='=')	indice++;
				break;
            case '|':
				UdLex.token = new String("OP_OR");
				UdLex.atributo = -1;
				indice++;
				if(cad.charAt(indice)=='|')	indice++;
				break;
            case '&':
				UdLex.token = new String("OP_AND");
				UdLex.atributo = -1;
				indice++;
				if(cad.charAt(indice)=='&')	indice++;
				break;
            case '"':
                indice++;
                ch = cad.charAt(indice);
				while (ch != '"') {
					lexema.append(ch);
					indice++;
					if (indice == cad.length()) break;
					ch = cad.charAt(indice);
				}
				UdLex.token = new String("CAD");
				UdLex.atributo = ts.insertar(lexema.toString());
				indice++;
                break;
            case '(':
				if(cad.charAt(indice+1)=='-' && cad.charAt(indice+2)==')'){
					indice+=3;
					ch = cad.charAt(indice);
	                UdLex.token = new String("NUM");
	                longcad = cad.length();
	                while (indice < longcad && ch >= '0' && ch <= '9') {
	                    for (i = 0; car[i] < ch; i++);
	                    val = val * 10 + i;
	                    indice++;
	                    if (indice < longcad)
	                        ch = cad.charAt(indice);
	                }
					if(ch=='.'){
	                    indice++;
	                    if (indice < longcad)
	                        ch = cad.charAt(indice);
					    valf = val; int k=1;
		                while (indice < longcad && ch >= '0' && ch <= '9') {
		                    for (i = 0; car[i] < ch; i++);
							valf = valf + i * Math.pow(10,-k);
		                    indice++;
		                    if (indice < longcad)
		                        ch = cad.charAt(indice);
							k++;	
		                }
						UdLex.atributo = ts.insertar((new Double(-1*valf)).toString());
					}else
						UdLex.atributo = ts.insertar((new Integer(-1*val)).toString());					
				}else{
					UdLex.token = new String("PAR_ABR");
					UdLex.atributo = -1;
					indice++;
				}	
				break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                UdLex.token = new String("NUM");
                longcad = cad.length();
                while (indice < longcad && ch >= '0' && ch <= '9') {
                    for (i = 0; car[i] < ch; i++);
                    val = val * 10 + i;
                    indice++;
                    if (indice < longcad)
                        ch = cad.charAt(indice);
                }
				if(ch=='.'){
                    indice++;
                    if (indice < longcad)
                        ch = cad.charAt(indice);
				    valf = val; int k=1;
	                while (indice < longcad && ch >= '0' && ch <= '9') {
	                    for (i = 0; car[i] < ch; i++);
	                    valf = valf + i * Math.pow(10,-k);
	                    indice++;
	                    if (indice < longcad)
	                        ch = cad.charAt(indice);
						k++;	
	                }
					UdLex.atributo = ts.insertar((new Double(valf)).toString());
				}else
					UdLex.atributo = ts.insertar((new Integer(val)).toString());
                break;
            }
        }
        return UdLex;
	}
}	
