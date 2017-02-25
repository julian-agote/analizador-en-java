import java.util.*;
class TRegla {
    int num;
    String lhs;
    Vector<String> rhs;
    boolean vacia() {
        return (rhs.contains("lambda")||rhs.size()==0);
    }
    TRegla(String pizda, Vector<String> pdcha, int n) {
        num = n;
        lhs = new String(pizda);
        if(!pdcha.isEmpty())
        	rhs = new Vector<String>(pdcha);
        else
        	rhs = new Vector<String>();
    }
    TRegla() {
        num = -1;
        lhs = new String();
        rhs = new Vector<String>();
    }
    TRegla(String pizda, String pdcha, int n) {
        num = n;
        lhs = new String(pizda);
        rhs = new Vector<String>();
        if(!pdcha.equals("")){
        	String[] result = pdcha.split(" ");
        	for (int x = 0; x < result.length; x++)
        		rhs.addElement(new String(result[x]).trim());
    	}
    }
}
class TElemIra {
    String simbolo;
    int nestado;
    TElemIra(String simb, int est) {
        simbolo = new String(simb);
        nestado = est;
    }
    TElemIra() {
        simbolo = new String();
        nestado = -1;
    }
}
class TVectorIra  {
    private Vector<TElemIra> filaira;
	TVectorIra(){
		filaira = new Vector<TElemIra>();
	}
    int getestado(String X) {
        boolean enc = false;
        TElemIra elira;
        int estado = -1;
        for (Enumeration e = filaira.elements();
             e.hasMoreElements() && !enc; ) {
            elira = (TElemIra)e.nextElement();
            if (elira.simbolo.equalsIgnoreCase(X)) {
                enc = true;
                estado = elira.nestado;
            }
        }
        return estado;
    }
	Enumeration elements(){
	    return filaira.elements();
	}   
	void addElement(TElemIra e){
		filaira.addElement(e);
	}
}
class TEltoAccion {
    String simbolo;
    String tipo;
    TRegla R;
    int sig_estado;
	TEltoAccion(String ptipo,String psimbolo,TRegla pr){
		simbolo = new String(psimbolo);
		tipo = new String(ptipo);
		R = pr;
	}	
	TEltoAccion(String ptipo,String psimbolo,int psig_estado){
		simbolo = new String(psimbolo);
		tipo = new String(ptipo);
		R = null;
		sig_estado = psig_estado;
	}	
	TEltoAccion(){		
		simbolo = new String();
		tipo = new String();
		R = null;
	}
}
class TVectorAccion  {
	private Vector<TEltoAccion> filaAccion;
	TVectorAccion (){
		filaAccion = new Vector<TEltoAccion>();
	}	
	Enumeration elements(){
		return filaAccion.elements();
	}	
	void addElement(TEltoAccion e){
		filaAccion.addElement(e);
	}
	/**************************************
	* devuevle la accion a realizar cuando llega desde la entrada simbol
	***************************************/
    String gettipo(String simbol) {
        TEltoAccion item;
        boolean enc = false;
        String tipo = new String();
        for (Enumeration e = filaAccion.elements();
             e.hasMoreElements() && !enc; ) {
            item = (TEltoAccion)e.nextElement();
            if (item.simbolo.equalsIgnoreCase(simbol)) {
                enc = true;
                tipo = new String(item.tipo);
            }
        }
        return tipo;
    }
	/**************************************
	* cuando la accion a realizar es Desplazar, devuevle el siguiente estado llega desde la entrada simbol
	***************************************/
    int getSigEstado(String simbol) {
        TEltoAccion item;
        boolean enc = false;
        int siguiente = 0;
        for (Enumeration e = filaAccion.elements();
             e.hasMoreElements() && !enc; ) {
            item = (TEltoAccion)e.nextElement();
            if (item.simbolo.equalsIgnoreCase(simbol)) {
                enc = true;
                siguiente = item.sig_estado;
            }
        }
        return siguiente;
    }
    /***************************************
	* cuando la accion a realizar es una reduccion, devuelve la regla por la que reducir
	****************************************/
    TRegla getregla(String simbol) {
        TEltoAccion item;
        boolean enc = false;
        TRegla regla = new TRegla();
        for (Enumeration e = filaAccion.elements();
             e.hasMoreElements() && !enc; ) {
            item = (TEltoAccion)e.nextElement();
            if (item.simbolo.equalsIgnoreCase(simbol)) {
                enc = true;
                regla = new TRegla(item.R.lhs, item.R.rhs, item.R.num);
            }
        }
        return regla;
    }
}
class TTablaSimbolos {
    private Vector<String> ts;
	TTablaSimbolos(){
		ts = new Vector<String>();
	}
    int insertar(String p_lex) {
        int pos = ts.indexOf(p_lex);
        if (pos == -1) {
            pos = ts.size();
            ts.add(p_lex);
        }
        return pos;
    }
}
/*******************************************************
* Representa una unidad lexica, atributo puede ser un apuntador a la tabla de simbolos
********************************************************/
class TElemEnt {
    String token;
    int atributo;
}
/*************************************************
* Representa la cadena de entrada, indice es un apuntador al caracter en curso
* leersigterm devuelve la siguiente unidad lexica
**************************************************/
class TEntrada {
    int indice;
    String cad;
    TEntrada() {
        indice = 0;
		cad = new String();
    }
    void gordeCad(String pcad){
		cad = new String(pcad);
	}	
    TElemEnt leersigterm(TElemEnt p_ant, TTablaSimbolos ts) {
    	char ch;
        int val = 0;
        int i, longcad;
        char car[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        String[] pal_clave =
            "p,make,modify,remove,bind,call,write,halt,rjust,tabto,crlf,literalize,vector_attribute,nil,compute,acceptline,startup".split(",");
        StringBuffer lexema = new StringBuffer();
        TElemEnt UdLex = new TElemEnt();
        if (indice == cad.length()) { // se ha llegado al final, devolver $
            UdLex.token = new String("$");
            UdLex.atributo = -1;
            return UdLex;
        }
        // saltar espacios, tab, cr, lf
        ch = cad.charAt(indice);
        while (ch == ' ' || ch == '\n' || ch == '\t') {
            indice++;
            if (indice == cad.length()) { // se ha llegado al final, devolver $
                UdLex.token = new String("$");
                UdLex.atributo = -1;
                return UdLex;
            }
            ch = cad.charAt(indice);
        }
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
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
            UdLex.token = new String("id");
            UdLex.atributo = ts.insertar(lexema.toString());
        } else {
            switch (ch) {
            case '+':
            case '*':
            case '(':
            case ')':
            case '{':
            case '}':
            case '/':
            case '^':
                UdLex.token = new String(String.valueOf(ch));
                UdLex.atributo = -1;
                indice++;
                break;
            case '-':
                indice++;
                if (cad.charAt(indice) == '-' &&
                    cad.charAt(indice + 1) == '>') {
                    UdLex.token = new String("->");
                    UdLex.atributo = -1;
                    indice += 2;
                } else if (p_ant.token != "id" && p_ant.token != "(" &&
                           p_ant.token != "num" && p_ant.token != "variable") {
                    UdLex.token = new String("menosu");
                    UdLex.atributo = -1;
                } else {
                    UdLex.token = new String("-");
                    UdLex.atributo = -1;
                }
                break;
            case '=':
                UdLex.token = new String("op_rel");
                UdLex.atributo = 1;
                indice++;
                break;
            case '<':
                indice++;
                ch = cad.charAt(indice);
                if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                    while ((ch >= 'a' && ch <= 'z') ||
                           (ch >= 'A' && ch <= 'Z') || ch == '-' ||
                           ch == '_') {
                        lexema.append(ch);
                        indice++;
                        ch = cad.charAt(indice);
                    }
                    if (ch == '>') {
                        UdLex.token = new String("variable");
                        UdLex.atributo = ts.insertar(lexema.toString());
                        indice++;
                    }
                } else if (ch == '>') {
                    UdLex.token = new String("op_rel");
                    UdLex.atributo = 4;
                    indice++;
                } else if (ch == '=') {
                    UdLex.token = new String("op_rel");
                    UdLex.atributo = 3;
                    indice++;
                } else if (ch == '<') {
                    UdLex.token = new String("doble_angulo_ab");
                    UdLex.atributo = -1;
                    indice++;
                } else {
                    UdLex.token = new String("op_rel");
                    UdLex.atributo = 2;
                }
                break;
            case '>':
                indice++;
                ch = cad.charAt(indice);
                if (ch == '>') {
                    UdLex.token = new String("doble_angulo_ce");
                    UdLex.atributo = -1;
                    indice++;
                } else if (ch == '=') {
                    UdLex.token = new String("ope_rel");
                    UdLex.atributo = 6;
                    indice++;
                } else {
                    UdLex.token = new String("ope_rel");
                    UdLex.atributo = 5;
                }
                break;
            case '|':
                indice++;
                ch = cad.charAt(indice);
                while (ch != '|') {
                    lexema.append(ch);
                    indice++;
                    ch = cad.charAt(indice);
                }
                UdLex.token = new String("cadena");
                UdLex.atributo = ts.insertar(lexema.toString());
                indice++;
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
                UdLex.token = new String("num");
                longcad = cad.length();
                while (indice < longcad && ch >= '0' && ch <= '9') {
                    for (i = 0; car[i] < ch; i++)
                        ;
                    val = val * 10 + i;
                    indice++;
                    if (indice < longcad)
                        ch = cad.charAt(indice);
                }
                UdLex.atributo = val;
                break;
            }
        }
        return UdLex;
	}
}	
class parser_OPS5P{
	Vector<TRegla> P;
	Vector<TVectorIra> tablaira;
	Vector<TVectorAccion> tablaaccion;
	TTablaSimbolos ts;
	TEntrada ent;
	parser_OPS5P(String s){
		TRegla regla;
		P = new Vector<TRegla>();
		regla = new TRegla("OPS5P","OPS5 $",0);
		P.addElement(regla);
		regla = new TRegla("OPS5","( orden ) Rorden",1);
		P.addElement(regla);
		regla = new TRegla("orden","literalize id Q0 atributo_escalar",2);
		P.addElement(regla);
		regla = new TRegla("orden","vector_attribute vector_name",3);
		P.addElement(regla);
		regla = new TRegla("Rorden","( orden ) Rorden",4);
		P.addElement(regla);
		regla = new TRegla("Rorden","",5);
		P.addElement(regla);
		regla = new TRegla("atributo_escalar","id Q2 Ratributo_escalar",6);
		P.addElement(regla);
		regla = new TRegla("Ratributo_escalar","id Q2 Ratributo_escalar",7);
		P.addElement(regla);
		regla = new TRegla("Ratributo_escalar","",8);
		P.addElement(regla);
		regla = new TRegla("vector_name","id Rvector_name",9);
		P.addElement(regla);
		regla = new TRegla("Rvector_name","id Rvector_name",10);
		P.addElement(regla);
		regla = new TRegla("Rvector_name","",11);
		P.addElement(regla);
		regla = new TRegla("orden","startup emt",12);
		P.addElement(regla);
		regla = new TRegla("emt","( make id Q1 pares_atrib_valor ) Q11 Remt",13);
		P.addElement(regla);
		regla = new TRegla("Remt","( make id Q1 pares_atrib_valor ) Q11 Remt",14);
		P.addElement(regla);
		regla = new TRegla("Remt","",15);
		P.addElement(regla);
		regla = new TRegla("pares_atrib_valor","^ id M E Q3 pares_atrib_valor",16);
		P.addElement(regla);
		regla = new TRegla("pares_atrib_valor","",17);
		P.addElement(regla);
		regla = new TRegla("orden","p id Q4 cond Q8 -> Mponer_marca_lista acc Racc",18);
		P.addElement(regla);
		regla = new TRegla("cond","{ variable M ( id Mmarca pares_atrib_cond ) } Q5 Rcond",19);
		P.addElement(regla);
		regla = new TRegla("cond","negar_cond ( id Mmarca pares_atrib_cond ) Q6 Rcond",20);
		P.addElement(regla);
		regla = new TRegla("Rcond","{ variable M ( id Mmarca pares_atrib_cond ) } Q5 Rcond",21);
		P.addElement(regla);
		regla = new TRegla("Rcond","negar_cond ( id Mmarca pares_atrib_cond ) Q6 Rcond",22);
		P.addElement(regla);
		regla = new TRegla("Rcond","",23);
		P.addElement(regla);
		regla = new TRegla("negar_cond","- M",24);
		P.addElement(regla);
		regla = new TRegla("negar_cond","menosu M",25);
		P.addElement(regla);
		regla = new TRegla("negar_cond","Q9",26);
		P.addElement(regla);
		regla = new TRegla("pares_atrib_cond","^ id M elemento_condicion pares_atrib_cond",27);
		P.addElement(regla);
		regla = new TRegla("pares_atrib_cond","",28);
		P.addElement(regla);
		regla = new TRegla("elemento_condicion","Q9 E",29);
		P.addElement(regla);
		regla = new TRegla("elemento_condicion","pdcha_cond",30);
		P.addElement(regla);
		regla = new TRegla("elemento_condicion","conjuncion",31);
		P.addElement(regla);
		regla = new TRegla("elemento_condicion","disyuncion",32);
		P.addElement(regla);
		regla = new TRegla("pdcha_cond","op_rel M E",33);
		P.addElement(regla);
		regla = new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34);
		P.addElement(regla);
		regla = new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35);
		P.addElement(regla);
		regla = new TRegla("Relemento_cond","elemento_condicion Relemento_cond",36);
		P.addElement(regla);
		regla = new TRegla("Relemento_cond","",37);
		P.addElement(regla);
		regla = new TRegla("acc","( make F Mponer_marca_lista pares_atrib_valor )",38);
		P.addElement(regla);
		regla = new TRegla("acc","( modify F Mponer_marca_lista pares_atrib_valor )",39);
		P.addElement(regla);
		regla = new TRegla("acc","( remove F )",40);
		P.addElement(regla);
		regla = new TRegla("acc","( bind F E )",41);
		P.addElement(regla);
		regla = new TRegla("acc","( write Mponer_marca_lista texto )",43);
		P.addElement(regla);
		regla = new TRegla("acc","( halt )",44);
		P.addElement(regla);
		regla = new TRegla("Racc","acc Racc",45);
		P.addElement(regla);
		regla = new TRegla("Racc","",46);
		P.addElement(regla);
		regla = new TRegla("texto","elem_write texto",47);
		P.addElement(regla);
		regla = new TRegla("texto","variable texto",48);
		P.addElement(regla);
		regla = new TRegla("texto","",49);
		P.addElement(regla);
		regla = new TRegla("elem_write","( rjust num )",50);
		P.addElement(regla);
		regla = new TRegla("elem_write","( tabto num )",51);
		P.addElement(regla);
		regla = new TRegla("elem_write","( crlf M )",52);
		P.addElement(regla);
		regla = new TRegla("E","( compute E )",53);
		P.addElement(regla);
		regla = new TRegla("E","( acceptline F )",54);
		P.addElement(regla);
		regla = new TRegla("E","E + T",55);
		P.addElement(regla);
		regla = new TRegla("E","E - T",56);
		P.addElement(regla);
		regla = new TRegla("E","T",57);
		P.addElement(regla);
		regla = new TRegla("T","T * F",58);
		P.addElement(regla);
		regla = new TRegla("T","T / F",59);
		P.addElement(regla);
		regla = new TRegla("T","F",60);
		P.addElement(regla);
		regla = new TRegla("F","menosu F",61);
		P.addElement(regla);
		regla = new TRegla("F","( E )",62);
		P.addElement(regla);
		regla = new TRegla("F","variable",63);
		P.addElement(regla);
		regla = new TRegla("F","num",64);
		P.addElement(regla);
		regla = new TRegla("F","cadena",65);
		P.addElement(regla);
		regla = new TRegla("F","id",66);
		P.addElement(regla);
		regla = new TRegla("F","nil",67);
		P.addElement(regla);
		regla = new TRegla("M","",68);
		P.addElement(regla);
		regla = new TRegla("Mmarca","",69);
		P.addElement(regla);
		regla = new TRegla("Q0","",70);
		P.addElement(regla);
		regla = new TRegla("Q1","",71);
		P.addElement(regla);
		regla = new TRegla("Q2","",72);
		P.addElement(regla);
		regla = new TRegla("Q3","",73);
		P.addElement(regla);
		regla = new TRegla("Q4","",74);
		P.addElement(regla);
		regla = new TRegla("Q5","",75);
		P.addElement(regla);
		regla = new TRegla("Q6","",76);
		P.addElement(regla);
		regla = new TRegla("Q8","",77);
		P.addElement(regla);
		regla = new TRegla("Q9","",78);
		P.addElement(regla);
		regla = new TRegla("Q11","",79);
		P.addElement(regla);
		regla = new TRegla("Mponer_marca_conj","",80);
		P.addElement(regla);
		regla = new TRegla("Mponer_marca_dis","",81);
		P.addElement(regla);
		regla = new TRegla("Mponer_marca_lista","",82);
		P.addElement(regla);
		initablaira();
		initablaaccion();
		ent = new TEntrada();
		ent.gordeCad(s);
	}
	void initablaira(){
		TVectorIra ira;
		TElemIra item;
		tablaira = new Vector<TVectorIra>();
		ira = new TVectorIra();
		item = new TElemIra("(",1);
		ira.addElement(item);
		item = new TElemIra("OPS5",2);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("vector_attribute",3);
		ira.addElement(item);
		item = new TElemIra("p",4);
		ira.addElement(item);
		item = new TElemIra("literalize",5);
		ira.addElement(item);
		item = new TElemIra("startup",6);
		ira.addElement(item);
		item = new TElemIra("orden",7);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("$",8);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",9);
		ira.addElement(item);
		item = new TElemIra("vector_name",10);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",11);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",12);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",13);
		ira.addElement(item);
		item = new TElemIra("emt",14);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",15);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",16);
		ira.addElement(item);
		item = new TElemIra("Rvector_name",17);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q4",18);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q0",19);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("make",20);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",21);
		ira.addElement(item);
		item = new TElemIra("Rorden",22);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",16);
		ira.addElement(item);
		item = new TElemIra("Rvector_name",23);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",24);
		ira.addElement(item);
		item = new TElemIra("-",25);
		ira.addElement(item);
		item = new TElemIra("menosu",26);
		ira.addElement(item);
		item = new TElemIra("cond",27);
		ira.addElement(item);
		item = new TElemIra("Q9",28);
		ira.addElement(item);
		item = new TElemIra("negar_cond",29);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",30);
		ira.addElement(item);
		item = new TElemIra("atributo_escalar",31);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",32);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("vector_attribute",3);
		ira.addElement(item);
		item = new TElemIra("p",4);
		ira.addElement(item);
		item = new TElemIra("literalize",5);
		ira.addElement(item);
		item = new TElemIra("startup",6);
		ira.addElement(item);
		item = new TElemIra("orden",33);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("variable",34);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",35);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",36);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q8",37);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",38);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q2",39);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q1",40);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",41);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",42);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("->",43);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",44);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",45);
		ira.addElement(item);
		item = new TElemIra("Ratributo_escalar",46);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",47);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_valor",48);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",21);
		ira.addElement(item);
		item = new TElemIra("Rorden",49);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",50);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_lista",51);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mmarca",52);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q2",53);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",54);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",55);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",56);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",57);
		ira.addElement(item);
		item = new TElemIra("acc",58);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",59);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_cond",60);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",45);
		ira.addElement(item);
		item = new TElemIra("Ratributo_escalar",61);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",62);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q11",63);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mmarca",64);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("make",65);
		ira.addElement(item);
		item = new TElemIra("modify",66);
		ira.addElement(item);
		item = new TElemIra("remove",67);
		ira.addElement(item);
		item = new TElemIra("bind",68);
		ira.addElement(item);
		item = new TElemIra("write",69);
		ira.addElement(item);
		item = new TElemIra("halt",70);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",57);
		ira.addElement(item);
		item = new TElemIra("acc",71);
		ira.addElement(item);
		item = new TElemIra("Racc",72);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",73);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",74);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",82);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",85);
		ira.addElement(item);
		item = new TElemIra("Remt",86);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",59);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_cond",87);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",88);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",89);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",88);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",90);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",88);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",91);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",92);
		ira.addElement(item);
		item = new TElemIra("num",93);
		ira.addElement(item);
		item = new TElemIra("(",94);
		ira.addElement(item);
		item = new TElemIra("cadena",95);
		ira.addElement(item);
		item = new TElemIra("variable",96);
		ira.addElement(item);
		item = new TElemIra("nil",97);
		ira.addElement(item);
		item = new TElemIra("menosu",98);
		ira.addElement(item);
		item = new TElemIra("F",99);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_lista",100);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",101);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",57);
		ira.addElement(item);
		item = new TElemIra("acc",71);
		ira.addElement(item);
		item = new TElemIra("Racc",102);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",103);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q6",104);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("compute",105);
		ira.addElement(item);
		item = new TElemIra("acceptline",106);
		ira.addElement(item);
		item = new TElemIra("E",107);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",108);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",109);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		item = new TElemIra("Q3",112);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",113);
		ira.addElement(item);
		item = new TElemIra("/",114);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("make",115);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",116);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",107);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_lista",117);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_lista",118);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",119);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",120);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",92);
		ira.addElement(item);
		item = new TElemIra("num",93);
		ira.addElement(item);
		item = new TElemIra("(",94);
		ira.addElement(item);
		item = new TElemIra("cadena",95);
		ira.addElement(item);
		item = new TElemIra("variable",96);
		ira.addElement(item);
		item = new TElemIra("nil",97);
		ira.addElement(item);
		item = new TElemIra("menosu",98);
		ira.addElement(item);
		item = new TElemIra("F",121);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",122);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",123);
		ira.addElement(item);
		item = new TElemIra("variable",124);
		ira.addElement(item);
		item = new TElemIra("texto",125);
		ira.addElement(item);
		item = new TElemIra("elem_write",126);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",127);
		ira.addElement(item);
		item = new TElemIra("op_rel",128);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",129);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",130);
		ira.addElement(item);
		item = new TElemIra("Q9",131);
		ira.addElement(item);
		item = new TElemIra("conjuncion",132);
		ira.addElement(item);
		item = new TElemIra("disyuncion",133);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",134);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",135);
		ira.addElement(item);
		item = new TElemIra("-",25);
		ira.addElement(item);
		item = new TElemIra("menosu",26);
		ira.addElement(item);
		item = new TElemIra("Rcond",136);
		ira.addElement(item);
		item = new TElemIra("Q9",28);
		ira.addElement(item);
		item = new TElemIra("negar_cond",137);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",138);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",88);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",139);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",140);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",107);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",108);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("T",141);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",108);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("T",142);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",47);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_valor",143);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",108);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",144);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",108);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",145);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",146);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("}",147);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",47);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_valor",148);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",47);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_valor",149);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",150);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",151);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("rjust",152);
		ira.addElement(item);
		item = new TElemIra("tabto",153);
		ira.addElement(item);
		item = new TElemIra("crlf",154);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",123);
		ira.addElement(item);
		item = new TElemIra("variable",124);
		ira.addElement(item);
		item = new TElemIra("texto",155);
		ira.addElement(item);
		item = new TElemIra("elem_write",126);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",156);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",123);
		ira.addElement(item);
		item = new TElemIra("variable",124);
		ira.addElement(item);
		item = new TElemIra("texto",157);
		ira.addElement(item);
		item = new TElemIra("elem_write",126);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_conj",158);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",159);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_dis",160);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",59);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_cond",161);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",162);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("variable",163);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",164);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",165);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",166);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",113);
		ira.addElement(item);
		item = new TElemIra("/",114);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",113);
		ira.addElement(item);
		item = new TElemIra("/",114);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q1",167);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q5",168);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",169);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",170);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("num",171);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("num",172);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",173);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",177);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",182);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",186);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",191);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",192);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",47);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_valor",193);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",135);
		ira.addElement(item);
		item = new TElemIra("-",25);
		ira.addElement(item);
		item = new TElemIra("menosu",26);
		ira.addElement(item);
		item = new TElemIra("Rcond",194);
		ira.addElement(item);
		item = new TElemIra("Q9",28);
		ira.addElement(item);
		item = new TElemIra("negar_cond",137);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",195);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",196);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",197);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_conj",198);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",199);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_dis",200);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",201);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",202);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",205);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("E",210);
		ira.addElement(item);
		item = new TElemIra("T",211);
		ira.addElement(item);
		item = new TElemIra("F",212);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_conj",213);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("M",214);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mponer_marca_dis",215);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",216);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",217);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",220);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("E",225);
		ira.addElement(item);
		item = new TElemIra("T",226);
		ira.addElement(item);
		item = new TElemIra("F",227);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",228);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mmarca",229);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",230);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",231);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",205);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("E",232);
		ira.addElement(item);
		item = new TElemIra("T",211);
		ira.addElement(item);
		item = new TElemIra("F",212);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",233);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",201);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",234);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("}",235);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("compute",236);
		ira.addElement(item);
		item = new TElemIra("acceptline",237);
		ira.addElement(item);
		item = new TElemIra("E",238);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",239);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("F",240);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",241);
		ira.addElement(item);
		item = new TElemIra("-",242);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",243);
		ira.addElement(item);
		item = new TElemIra("/",244);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",245);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",220);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("E",246);
		ira.addElement(item);
		item = new TElemIra("T",226);
		ira.addElement(item);
		item = new TElemIra("F",227);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",247);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",216);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",248);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("doble_angulo_ce",249);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("compute",250);
		ira.addElement(item);
		item = new TElemIra("acceptline",251);
		ira.addElement(item);
		item = new TElemIra("E",252);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",253);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("F",254);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",255);
		ira.addElement(item);
		item = new TElemIra("-",256);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",257);
		ira.addElement(item);
		item = new TElemIra("/",258);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",259);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",59);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_cond",260);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q11",261);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",201);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",262);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",241);
		ira.addElement(item);
		item = new TElemIra("-",242);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",216);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",263);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",264);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",88);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",265);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",266);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",238);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",239);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("T",267);
		ira.addElement(item);
		item = new TElemIra("F",212);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",239);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("T",268);
		ira.addElement(item);
		item = new TElemIra("F",212);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",239);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("F",269);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",203);
		ira.addElement(item);
		item = new TElemIra("num",204);
		ira.addElement(item);
		item = new TElemIra("(",239);
		ira.addElement(item);
		item = new TElemIra("cadena",206);
		ira.addElement(item);
		item = new TElemIra("variable",207);
		ira.addElement(item);
		item = new TElemIra("nil",208);
		ira.addElement(item);
		item = new TElemIra("menosu",209);
		ira.addElement(item);
		item = new TElemIra("F",270);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",174);
		ira.addElement(item);
		item = new TElemIra("op_rel",175);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",176);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",201);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",271);
		ira.addElement(item);
		item = new TElemIra("Q9",178);
		ira.addElement(item);
		item = new TElemIra("conjuncion",179);
		ira.addElement(item);
		item = new TElemIra("disyuncion",180);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",181);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("+",255);
		ira.addElement(item);
		item = new TElemIra("-",256);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",183);
		ira.addElement(item);
		item = new TElemIra("op_rel",184);
		ira.addElement(item);
		item = new TElemIra("doble_angulo_ab",185);
		ira.addElement(item);
		item = new TElemIra("elemento_condicion",216);
		ira.addElement(item);
		item = new TElemIra("Relemento_cond",272);
		ira.addElement(item);
		item = new TElemIra("Q9",187);
		ira.addElement(item);
		item = new TElemIra("conjuncion",188);
		ira.addElement(item);
		item = new TElemIra("disyuncion",189);
		ira.addElement(item);
		item = new TElemIra("pdcha_cond",190);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",273);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",88);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("F",274);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",275);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",75);
		ira.addElement(item);
		item = new TElemIra("num",76);
		ira.addElement(item);
		item = new TElemIra("(",77);
		ira.addElement(item);
		item = new TElemIra("cadena",78);
		ira.addElement(item);
		item = new TElemIra("variable",79);
		ira.addElement(item);
		item = new TElemIra("nil",80);
		ira.addElement(item);
		item = new TElemIra("menosu",81);
		ira.addElement(item);
		item = new TElemIra("E",252);
		ira.addElement(item);
		item = new TElemIra("T",83);
		ira.addElement(item);
		item = new TElemIra("F",84);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",253);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("T",276);
		ira.addElement(item);
		item = new TElemIra("F",227);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",253);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("T",277);
		ira.addElement(item);
		item = new TElemIra("F",227);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",253);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("F",278);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("id",218);
		ira.addElement(item);
		item = new TElemIra("num",219);
		ira.addElement(item);
		item = new TElemIra("(",253);
		ira.addElement(item);
		item = new TElemIra("cadena",221);
		ira.addElement(item);
		item = new TElemIra("variable",222);
		ira.addElement(item);
		item = new TElemIra("nil",223);
		ira.addElement(item);
		item = new TElemIra("menosu",224);
		ira.addElement(item);
		item = new TElemIra("F",279);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Mmarca",280);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",281);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("(",85);
		ira.addElement(item);
		item = new TElemIra("Remt",282);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("}",283);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("doble_angulo_ce",284);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",285);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",286);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",243);
		ira.addElement(item);
		item = new TElemIra("/",244);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",243);
		ira.addElement(item);
		item = new TElemIra("/",244);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("}",287);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("doble_angulo_ce",288);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",289);
		ira.addElement(item);
		item = new TElemIra("+",110);
		ira.addElement(item);
		item = new TElemIra("-",111);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",290);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",257);
		ira.addElement(item);
		item = new TElemIra("/",258);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("*",257);
		ira.addElement(item);
		item = new TElemIra("/",258);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("^",59);
		ira.addElement(item);
		item = new TElemIra("pares_atrib_cond",291);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q6",292);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra(")",293);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",135);
		ira.addElement(item);
		item = new TElemIra("-",25);
		ira.addElement(item);
		item = new TElemIra("menosu",26);
		ira.addElement(item);
		item = new TElemIra("Rcond",294);
		ira.addElement(item);
		item = new TElemIra("Q9",28);
		ira.addElement(item);
		item = new TElemIra("negar_cond",137);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("}",295);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("Q5",296);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		item = new TElemIra("{",135);
		ira.addElement(item);
		item = new TElemIra("-",25);
		ira.addElement(item);
		item = new TElemIra("menosu",26);
		ira.addElement(item);
		item = new TElemIra("Rcond",297);
		ira.addElement(item);
		item = new TElemIra("Q9",28);
		ira.addElement(item);
		item = new TElemIra("negar_cond",137);
		ira.addElement(item);
		tablaira.addElement(ira);
		ira = new TVectorIra();
		tablaira.addElement(ira);
	}
	void initablaaccion(){
		TVectorAccion acc;
		TEltoAccion itema;
		Object[] bindVars = new Object[]{new Object[]{new TEltoAccion("Desplazar","(",1),,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","OPS5",2),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","vector_attribute",3),,,,,new TEltoAccion("Desplazar","p",4),,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","literalize",5),
,,,,,,,new TEltoAccion("Desplazar","startup",6),,,new TEltoAccion("Desplazar","orden",7),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","$",8),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",9),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","vector_name",10),,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",11),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",12),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",13),,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","emt",14),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",15),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Aceptar","$",0),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",16),,,,,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Rvector_name","",11)),,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Rvector_name",17),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("orden","vector_attribute vector_name",3)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,},new Object[]{new TEltoAccion("Reducir","{",new TRegla("Q4","",74)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q4","",74)),,,,new TEltoAccion("Reducir","-",new TRegla("Q4","",74)),
,,,,,,,,,,new TEltoAccion("Reducir","menosu",new TRegla("Q4","",74)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q4",18),
,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q0","",70)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q0",19),
,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","make",20),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("orden","startup emt",12)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",21),,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Reducir","$",new TRegla("Rorden","",5)),,,new TEltoAccion("Desplazar","Rorden",22),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",16),,,,,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Rvector_name","",11)),,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Rvector_name",23),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("vector_name","id Rvector_name",9)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","{",24),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,new TEltoAccion("Desplazar","-",25),,,,,,
,,,,,new TEltoAccion("Desplazar","menosu",26),,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","cond",27),,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",28),
,,,,,,,,,new TEltoAccion("Desplazar","negar_cond",29),},new Object[]{new TEltoAccion("Desplazar","id",30),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","atributo_escalar",31),,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",32),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","vector_attribute",3),,,,,new TEltoAccion("Desplazar","p",4),,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","literalize",5),
,,,,,,,new TEltoAccion("Desplazar","startup",6),,,new TEltoAccion("Desplazar","orden",33),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","$",new TRegla("OPS5","( orden ) Rorden",1)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("Rvector_name","id Rvector_name",10)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","variable",34),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",35),,,,,,,,,,,,,,
,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",36),,,,,,,,,,,,,,
,,,,,,},new Object[]{new TEltoAccion("Reducir","->",new TRegla("Q8","",77)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q8",37),
,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("negar_cond","Q9",26)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",38),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q2","",72)),,,,,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Q2","",72)),,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q2",39),,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("orden","literalize id Q0 atributo_escalar",2)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Q1","",71)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Q1","",71)),,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q1",40),,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",41),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",42),,,,,,,,,,,,,,
,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("negar_cond","- M",24)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("negar_cond","menosu M",25)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","->",43),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",44),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",45),,,,,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Ratributo_escalar","",8)),,,,,,,,,,,,,,,,,,,,,,,,,
new TEltoAccion("Desplazar","Ratributo_escalar",46),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",47),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,new TEltoAccion("Desplazar","pares_atrib_valor",48),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",21),,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Reducir","$",new TRegla("Rorden","",5)),,,new TEltoAccion("Desplazar","Rorden",49),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",50),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_lista","",82)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_lista",51),
,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mmarca",52),,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q2","",72)),,,,,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Q2","",72)),,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q2",53),,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("atributo_escalar","id Q2 Ratributo_escalar",6)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",54),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",55),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","$",new TRegla("Rorden","( orden ) Rorden",4)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",56),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",57),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","acc",58),,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",59),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,new TEltoAccion("Desplazar","pares_atrib_cond",60),,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",45),,,,,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Ratributo_escalar","",8)),,,,,,,,,,,,,,,,,,,,,,,,,
new TEltoAccion("Desplazar","Ratributo_escalar",61),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("M","",68)),new TEltoAccion("Reducir","num",new TRegla("M","",68)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("M","",68)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("M","",68)),,,,,new TEltoAccion("Reducir","variable",new TRegla("M","",68)),
new TEltoAccion("Reducir","nil",new TRegla("M","",68)),new TEltoAccion("Reducir","menosu",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",62),
,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("Q11","",79)),new TEltoAccion("Reducir",")",new TRegla("Q11","",79)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q11",63),,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mmarca",64),,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","make",65),new TEltoAccion("Desplazar","modify",66),new TEltoAccion("Desplazar","remove",67),new TEltoAccion("Desplazar","bind",68),
new TEltoAccion("Desplazar","write",69),new TEltoAccion("Desplazar","halt",70),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,},new Object[]{new TEltoAccion("Desplazar","(",57),new TEltoAccion("Reducir",")",new TRegla("Racc","",46)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","acc",71),
new TEltoAccion("Desplazar","Racc",72),,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",73),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",74),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("Ratributo_escalar","id Q2 Ratributo_escalar",7)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",82),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","(",85),new TEltoAccion("Reducir",")",new TRegla("Remt","",15)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Remt",86),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",59),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,new TEltoAccion("Desplazar","pares_atrib_cond",87),,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",88),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",89),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",88),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",90),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",88),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",91),,},new Object[]{new TEltoAccion("Desplazar","id",92),new TEltoAccion("Desplazar","num",93),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",94),,,,,,,,new TEltoAccion("Desplazar","cadena",95),
,,,,new TEltoAccion("Desplazar","variable",96),new TEltoAccion("Desplazar","nil",97),new TEltoAccion("Desplazar","menosu",98),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",99),,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_lista","",82)),new TEltoAccion("Reducir",")",new TRegla("Mponer_marca_lista","",82)),,,,
,,,,,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_lista","",82)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_lista",100),
,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",101),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",57),new TEltoAccion("Reducir",")",new TRegla("Racc","",46)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","acc",71),
new TEltoAccion("Desplazar","Racc",102),,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("orden","p id Q4 cond Q8 -> Mponer_marca_lista acc Racc",18)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("M","",68)),new TEltoAccion("Reducir","num",new TRegla("M","",68)),,,new TEltoAccion("Reducir","{",new TRegla("M","",68)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("M","",68)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("M","",68)),new TEltoAccion("Reducir","cadena",new TRegla("M","",68)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("M","",68)),new TEltoAccion("Reducir","nil",new TRegla("M","",68)),
new TEltoAccion("Reducir","menosu",new TRegla("M","",68)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("M","",68)),,,,,,,,,,,,,,
,,,,,,,,,,,,new TEltoAccion("Desplazar","M",103),,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","{",new TRegla("Q6","",76)),,new TEltoAccion("Reducir","->",new TRegla("Q6","",76)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q6","",76)),
,,,new TEltoAccion("Reducir","-",new TRegla("Q6","",76)),,,,,,,,,,,new TEltoAccion("Reducir","menosu",new TRegla("Q6","",76)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q6",104),,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","id",66)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","id",66)),,new TEltoAccion("Reducir","+",new TRegla("F","id",66)),
new TEltoAccion("Reducir","-",new TRegla("F","id",66)),new TEltoAccion("Reducir","*",new TRegla("F","id",66)),new TEltoAccion("Reducir","/",new TRegla("F","id",66)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","num",64)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","num",64)),,new TEltoAccion("Reducir","+",new TRegla("F","num",64)),
new TEltoAccion("Reducir","-",new TRegla("F","num",64)),new TEltoAccion("Reducir","*",new TRegla("F","num",64)),new TEltoAccion("Reducir","/",new TRegla("F","num",64)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,new TEltoAccion("Desplazar","compute",105),new TEltoAccion("Desplazar","acceptline",106),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",107),
new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","cadena",65)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","cadena",65)),,new TEltoAccion("Reducir","+",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","-",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","*",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","/",new TRegla("F","cadena",65)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","variable",63)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","variable",63)),,new TEltoAccion("Reducir","+",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","-",new TRegla("F","variable",63)),new TEltoAccion("Reducir","*",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","/",new TRegla("F","variable",63)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","nil",67)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","nil",67)),,new TEltoAccion("Reducir","+",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","-",new TRegla("F","nil",67)),new TEltoAccion("Reducir","*",new TRegla("F","nil",67)),new TEltoAccion("Reducir","/",new TRegla("F","nil",67)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",108),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",109),,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Q3","",73)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Q3","",73)),,new TEltoAccion("Desplazar","+",110),
new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q3",112),,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("E","T",57)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("E","T",57)),,new TEltoAccion("Reducir","+",new TRegla("E","T",57)),
new TEltoAccion("Reducir","-",new TRegla("E","T",57)),new TEltoAccion("Desplazar","*",113),new TEltoAccion("Desplazar","/",114),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("T","F",60)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("T","F",60)),,new TEltoAccion("Reducir","+",new TRegla("T","F",60)),
new TEltoAccion("Reducir","-",new TRegla("T","F",60)),new TEltoAccion("Reducir","*",new TRegla("T","F",60)),new TEltoAccion("Reducir","/",new TRegla("T","F",60)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","make",115),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("emt","( make id Q1 pares_atrib_valor ) Q11 Remt",13)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",116),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",107),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Mponer_marca_lista","",82)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Mponer_marca_lista","",82)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_lista",117),,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Mponer_marca_lista","",82)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Mponer_marca_lista","",82)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_lista",118),,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",119),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","id",66)),new TEltoAccion("Reducir","num",new TRegla("F","id",66)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","id",66)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","id",66)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","id",66)),
new TEltoAccion("Reducir","nil",new TRegla("F","id",66)),new TEltoAccion("Reducir","menosu",new TRegla("F","id",66)),,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","num",64)),new TEltoAccion("Reducir","num",new TRegla("F","num",64)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","num",64)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","num",64)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","num",64)),
new TEltoAccion("Reducir","nil",new TRegla("F","num",64)),new TEltoAccion("Reducir","menosu",new TRegla("F","num",64)),,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",120),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","num",new TRegla("F","cadena",65)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","cadena",65)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","cadena",65)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","nil",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","menosu",new TRegla("F","cadena",65)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","variable",63)),new TEltoAccion("Reducir","num",new TRegla("F","variable",63)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","variable",63)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","variable",63)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","nil",new TRegla("F","variable",63)),new TEltoAccion("Reducir","menosu",new TRegla("F","variable",63)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","nil",67)),new TEltoAccion("Reducir","num",new TRegla("F","nil",67)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","nil",67)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","nil",67)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","nil",new TRegla("F","nil",67)),new TEltoAccion("Reducir","menosu",new TRegla("F","nil",67)),,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",92),new TEltoAccion("Desplazar","num",93),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",94),,,,,,,,new TEltoAccion("Desplazar","cadena",95),
,,,,new TEltoAccion("Desplazar","variable",96),new TEltoAccion("Desplazar","nil",97),new TEltoAccion("Desplazar","menosu",98),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",121),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",122),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","(",123),new TEltoAccion("Reducir",")",new TRegla("texto","",49)),,,,,,,,,,,,new TEltoAccion("Desplazar","variable",124),
,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","texto",125),new TEltoAccion("Desplazar","elem_write",126),,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("acc","( halt )",44)),new TEltoAccion("Reducir",")",new TRegla("acc","( halt )",44)),,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("Racc","acc Racc",45)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",127),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",128),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",129),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",130),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",131),,,,,new TEltoAccion("Desplazar","conjuncion",132),new TEltoAccion("Desplazar","disyuncion",133),
,,,,new TEltoAccion("Desplazar","pdcha_cond",134)},new Object[]{new TEltoAccion("Desplazar","{",135),,new TEltoAccion("Reducir","->",new TRegla("Rcond","",23)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),
,,,new TEltoAccion("Desplazar","-",25),,,,,,,,,,,new TEltoAccion("Desplazar","menosu",26),,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Rcond",136),
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",28),,,,,,,,,,new TEltoAccion("Desplazar","negar_cond",137),},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",138),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",88),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",139),,},new Object[]{new TEltoAccion("Desplazar",")",140),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",107),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","menosu F",61)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","menosu F",61)),,new TEltoAccion("Reducir","+",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","-",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","*",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","/",new TRegla("F","menosu F",61)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",108),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","T",141),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",108),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","T",142),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","^",47),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,new TEltoAccion("Desplazar","pares_atrib_valor",143),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",108),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",144),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",108),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",145),,},new Object[]{new TEltoAccion("Desplazar","id",146),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","}",147),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",47),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,new TEltoAccion("Desplazar","pares_atrib_valor",148),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",47),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,new TEltoAccion("Desplazar","pares_atrib_valor",149),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("acc","( remove F )",40)),new TEltoAccion("Reducir",")",new TRegla("acc","( remove F )",40)),,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",150),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","num",new TRegla("F","menosu F",61)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","menosu F",61)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","menosu F",61)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","nil",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","menosu",new TRegla("F","menosu F",61)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",151),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","rjust",152),new TEltoAccion("Desplazar","tabto",153),new TEltoAccion("Desplazar","crlf",154),,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",123),new TEltoAccion("Reducir",")",new TRegla("texto","",49)),,,,,,,,,,,,new TEltoAccion("Desplazar","variable",124),
,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","texto",155),new TEltoAccion("Desplazar","elem_write",126),,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",156),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",123),new TEltoAccion("Reducir",")",new TRegla("texto","",49)),,,,,,,,,,,,new TEltoAccion("Desplazar","variable",124),
,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","texto",157),new TEltoAccion("Desplazar","elem_write",126),,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Mponer_marca_conj","",80)),new TEltoAccion("Reducir","num",new TRegla("Mponer_marca_conj","",80)),,,
new TEltoAccion("Reducir","{",new TRegla("Mponer_marca_conj","",80)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_conj","",80)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_conj","",80)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","nil",new TRegla("Mponer_marca_conj","",80)),new TEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_conj","",80)),,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_conj",158),,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("M","",68)),new TEltoAccion("Reducir","num",new TRegla("M","",68)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("M","",68)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("M","",68)),,,,,new TEltoAccion("Reducir","variable",new TRegla("M","",68)),
new TEltoAccion("Reducir","nil",new TRegla("M","",68)),new TEltoAccion("Reducir","menosu",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",159),
,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Mponer_marca_dis","",81)),new TEltoAccion("Reducir","num",new TRegla("Mponer_marca_dis","",81)),,,new TEltoAccion("Reducir","{",new TRegla("Mponer_marca_dis","",81)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_dis","",81)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_dis","",81)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","nil",new TRegla("Mponer_marca_dis","",81)),new TEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_dis","",81)),,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_dis",160),,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",59),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,new TEltoAccion("Desplazar","pares_atrib_cond",161),,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",162),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("elemento_condicion","conjuncion",31)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("elemento_condicion","conjuncion",31)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("elemento_condicion","disyuncion",32)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("elemento_condicion","disyuncion",32)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("elemento_condicion","pdcha_cond",30)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("elemento_condicion","pdcha_cond",30)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","variable",163),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","->",new TRegla("cond","negar_cond ( id Mmarca pares_atrib_cond ) Q6 Rcond",20)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",164),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",165),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",166),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("F","( E )",62)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("F","( E )",62)),,new TEltoAccion("Reducir","+",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","-",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","*",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","/",new TRegla("F","( E )",62)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("E","E + T",55)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("E","E + T",55)),,new TEltoAccion("Reducir","+",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","-",new TRegla("E","E + T",55)),new TEltoAccion("Desplazar","*",113),new TEltoAccion("Desplazar","/",114),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("E","E - T",56)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("E","E - T",56)),,new TEltoAccion("Reducir","+",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","-",new TRegla("E","E - T",56)),new TEltoAccion("Desplazar","*",113),new TEltoAccion("Desplazar","/",114),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","^ id M E Q3 pares_atrib_valor",16)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("T","T * F",58)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("T","T * F",58)),,new TEltoAccion("Reducir","+",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","-",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","*",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","/",new TRegla("T","T * F",58)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("T","T / F",59)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("T","T / F",59)),,new TEltoAccion("Reducir","+",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","-",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","*",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","/",new TRegla("T","T / F",59)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Q1","",71)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Q1","",71)),,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q1",167),,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","{",new TRegla("Q5","",75)),,new TEltoAccion("Reducir","->",new TRegla("Q5","",75)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q5","",75)),
,,,new TEltoAccion("Reducir","-",new TRegla("Q5","",75)),,,,,,,,,,,new TEltoAccion("Reducir","menosu",new TRegla("Q5","",75)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q5",168),,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",169),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",170),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","num",new TRegla("F","( E )",62)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","( E )",62)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("F","( E )",62)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","nil",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","menosu",new TRegla("F","( E )",62)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("acc","( bind F E )",41)),new TEltoAccion("Reducir",")",new TRegla("acc","( bind F E )",41)),,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","num",171),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","num",172),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",173),,,,,,,,,,,,,,
,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("texto","variable texto",48)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("acc","( write Mponer_marca_lista texto )",43)),new TEltoAccion("Reducir",")",new TRegla("acc","( write Mponer_marca_lista texto )",43)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("texto","elem_write texto",47)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",176),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",177),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),
,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",182),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",186),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),
,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","^ id M elemento_condicion pares_atrib_cond",27)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("elemento_condicion","Q9 E",29)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("elemento_condicion","Q9 E",29)),
,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",191),,,,,,,,,,,,,
,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",192),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("E","( compute E )",53)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("E","( compute E )",53)),
,new TEltoAccion("Reducir","+",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","-",new TRegla("E","( compute E )",53)),,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("E","( acceptline F )",54)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("E","( acceptline F )",54)),
,new TEltoAccion("Reducir","+",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","-",new TRegla("E","( acceptline F )",54)),,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",47),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,new TEltoAccion("Desplazar","pares_atrib_valor",193),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","{",135),,new TEltoAccion("Reducir","->",new TRegla("Rcond","",23)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),
,,,new TEltoAccion("Desplazar","-",25),,,,,,,,,,,new TEltoAccion("Desplazar","menosu",26),,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Rcond",194),
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",28),,,,,,,,,,new TEltoAccion("Desplazar","negar_cond",137),},new Object[]{new TEltoAccion("Reducir","(",new TRegla("acc","( make F Mponer_marca_lista pares_atrib_valor )",38)),new TEltoAccion("Reducir",")",new TRegla("acc","( make F Mponer_marca_lista pares_atrib_valor )",38)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("acc","( modify F Mponer_marca_lista pares_atrib_valor )",39)),new TEltoAccion("Reducir",")",new TRegla("acc","( modify F Mponer_marca_lista pares_atrib_valor )",39)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",195),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",196),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",197),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Mponer_marca_conj","",80)),new TEltoAccion("Reducir","num",new TRegla("Mponer_marca_conj","",80)),,,
new TEltoAccion("Reducir","{",new TRegla("Mponer_marca_conj","",80)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_conj","",80)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_conj","",80)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","nil",new TRegla("Mponer_marca_conj","",80)),new TEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_conj","",80)),,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_conj",198),,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("M","",68)),new TEltoAccion("Reducir","num",new TRegla("M","",68)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("M","",68)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("M","",68)),,,,,new TEltoAccion("Reducir","variable",new TRegla("M","",68)),
new TEltoAccion("Reducir","nil",new TRegla("M","",68)),new TEltoAccion("Reducir","menosu",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",199),
,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Mponer_marca_dis","",81)),new TEltoAccion("Reducir","num",new TRegla("Mponer_marca_dis","",81)),,,new TEltoAccion("Reducir","{",new TRegla("Mponer_marca_dis","",81)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_dis","",81)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_dis","",81)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","nil",new TRegla("Mponer_marca_dis","",81)),new TEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_dis","",81)),,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_dis",200),,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
new TEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),
new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),new TEltoAccion("Desplazar","doble_angulo_ab",176),
,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",201),new TEltoAccion("Desplazar","Relemento_cond",202),,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),
,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",205),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",210),new TEltoAccion("Desplazar","T",211),new TEltoAccion("Desplazar","F",212),
,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","conjuncion",31)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","}",new TRegla("elemento_condicion","conjuncion",31)),
,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","conjuncion",31)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","conjuncion",31)),
new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","conjuncion",31)),,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","conjuncion",31)),
new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","conjuncion",31)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","conjuncion",31)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","disyuncion",32)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","}",new TRegla("elemento_condicion","disyuncion",32)),
,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","disyuncion",32)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","disyuncion",32)),
new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","disyuncion",32)),,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","disyuncion",32)),
new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","disyuncion",32)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","disyuncion",32)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","pdcha_cond",30)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","}",new TRegla("elemento_condicion","pdcha_cond",30)),
,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","pdcha_cond",30)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","pdcha_cond",30)),
new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","pdcha_cond",30)),,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","pdcha_cond",30)),
new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","pdcha_cond",30)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","pdcha_cond",30)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("pdcha_cond","op_rel M E",33)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pdcha_cond","op_rel M E",33)),
,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Mponer_marca_conj","",80)),new TEltoAccion("Reducir","num",new TRegla("Mponer_marca_conj","",80)),,,
new TEltoAccion("Reducir","{",new TRegla("Mponer_marca_conj","",80)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_conj","",80)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_conj","",80)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","nil",new TRegla("Mponer_marca_conj","",80)),new TEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_conj","",80)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_conj","",80)),,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_conj",213),,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("M","",68)),new TEltoAccion("Reducir","num",new TRegla("M","",68)),,,,,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("M","",68)),
,,,,,,,new TEltoAccion("Reducir","cadena",new TRegla("M","",68)),,,,,new TEltoAccion("Reducir","variable",new TRegla("M","",68)),
new TEltoAccion("Reducir","nil",new TRegla("M","",68)),new TEltoAccion("Reducir","menosu",new TRegla("M","",68)),,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","M",214),
,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Mponer_marca_dis","",81)),new TEltoAccion("Reducir","num",new TRegla("Mponer_marca_dis","",81)),,,new TEltoAccion("Reducir","{",new TRegla("Mponer_marca_dis","",81)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Mponer_marca_dis","",81)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_dis","",81)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","nil",new TRegla("Mponer_marca_dis","",81)),new TEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_dis","",81)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_dis","",81)),,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mponer_marca_dis",215),,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),
,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",216),new TEltoAccion("Desplazar","Relemento_cond",217),,,,,,,
,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",220),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",225),new TEltoAccion("Desplazar","T",226),new TEltoAccion("Desplazar","F",227),
,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","conjuncion",31)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","conjuncion",31)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","conjuncion",31)),
,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","conjuncion",31)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","conjuncion",31)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","conjuncion",31)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","conjuncion",31)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","conjuncion",31)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","disyuncion",32)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","disyuncion",32)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","disyuncion",32)),
,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","disyuncion",32)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","disyuncion",32)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","disyuncion",32)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","disyuncion",32)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","disyuncion",32)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","pdcha_cond",30)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","pdcha_cond",30)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","pdcha_cond",30)),
,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","pdcha_cond",30)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","pdcha_cond",30)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","pdcha_cond",30)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","pdcha_cond",30)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","pdcha_cond",30)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",228),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mmarca",229),,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",230),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","->",new TRegla("cond","{ variable M ( id Mmarca pares_atrib_cond ) } Q5 Rcond",19)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("elem_write","( rjust num )",50)),new TEltoAccion("Reducir",")",new TRegla("elem_write","( rjust num )",50)),
,,,,,,,,,,,new TEltoAccion("Reducir","variable",new TRegla("elem_write","( rjust num )",50)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("elem_write","( tabto num )",51)),new TEltoAccion("Reducir",")",new TRegla("elem_write","( tabto num )",51)),
,,,,,,,,,,,new TEltoAccion("Reducir","variable",new TRegla("elem_write","( tabto num )",51)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("elem_write","( crlf M )",52)),new TEltoAccion("Reducir",")",new TRegla("elem_write","( crlf M )",52)),
,,,,,,,,,,,new TEltoAccion("Reducir","variable",new TRegla("elem_write","( crlf M )",52)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",176),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",231),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),
,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",205),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",232),new TEltoAccion("Desplazar","T",211),new TEltoAccion("Desplazar","F",212),
,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",233),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),
,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
new TEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),
new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),new TEltoAccion("Desplazar","doble_angulo_ab",176),
,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",201),new TEltoAccion("Desplazar","Relemento_cond",234),,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),
,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Desplazar","}",235),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","id",66)),new TEltoAccion("Reducir","num",new TRegla("F","id",66)),,,new TEltoAccion("Reducir","{",new TRegla("F","id",66)),
new TEltoAccion("Reducir","}",new TRegla("F","id",66)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","id",66)),,,new TEltoAccion("Reducir","+",new TRegla("F","id",66)),
new TEltoAccion("Reducir","-",new TRegla("F","id",66)),new TEltoAccion("Reducir","*",new TRegla("F","id",66)),
new TEltoAccion("Reducir","/",new TRegla("F","id",66)),new TEltoAccion("Reducir","op_rel",new TRegla("F","id",66)),new TEltoAccion("Reducir","cadena",new TRegla("F","id",66)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","id",66)),new TEltoAccion("Reducir","nil",new TRegla("F","id",66)),new TEltoAccion("Reducir","menosu",new TRegla("F","id",66)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","id",66)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","num",64)),new TEltoAccion("Reducir","num",new TRegla("F","num",64)),,,new TEltoAccion("Reducir","{",new TRegla("F","num",64)),
new TEltoAccion("Reducir","}",new TRegla("F","num",64)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","num",64)),,,new TEltoAccion("Reducir","+",new TRegla("F","num",64)),
new TEltoAccion("Reducir","-",new TRegla("F","num",64)),new TEltoAccion("Reducir","*",new TRegla("F","num",64)),
new TEltoAccion("Reducir","/",new TRegla("F","num",64)),new TEltoAccion("Reducir","op_rel",new TRegla("F","num",64)),new TEltoAccion("Reducir","cadena",new TRegla("F","num",64)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","num",64)),new TEltoAccion("Reducir","nil",new TRegla("F","num",64)),
new TEltoAccion("Reducir","menosu",new TRegla("F","num",64)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","num",64)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,new TEltoAccion("Desplazar","compute",236),new TEltoAccion("Desplazar","acceptline",237),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",238),
new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","num",new TRegla("F","cadena",65)),,,new TEltoAccion("Reducir","{",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","}",new TRegla("F","cadena",65)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","cadena",65)),
,,new TEltoAccion("Reducir","+",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","-",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","*",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","/",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","op_rel",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","cadena",new TRegla("F","cadena",65)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","nil",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","menosu",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","cadena",65)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","variable",63)),new TEltoAccion("Reducir","num",new TRegla("F","variable",63)),,,new TEltoAccion("Reducir","{",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","}",new TRegla("F","variable",63)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","variable",63)),
,,new TEltoAccion("Reducir","+",new TRegla("F","variable",63)),new TEltoAccion("Reducir","-",new TRegla("F","variable",63)),new TEltoAccion("Reducir","*",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","/",new TRegla("F","variable",63)),new TEltoAccion("Reducir","op_rel",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","cadena",new TRegla("F","variable",63)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","nil",new TRegla("F","variable",63)),new TEltoAccion("Reducir","menosu",new TRegla("F","variable",63)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","variable",63)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","nil",67)),new TEltoAccion("Reducir","num",new TRegla("F","nil",67)),,,new TEltoAccion("Reducir","{",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","}",new TRegla("F","nil",67)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","nil",67)),,,new TEltoAccion("Reducir","+",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","-",new TRegla("F","nil",67)),new TEltoAccion("Reducir","*",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","/",new TRegla("F","nil",67)),new TEltoAccion("Reducir","op_rel",new TRegla("F","nil",67)),new TEltoAccion("Reducir","cadena",new TRegla("F","nil",67)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","nil",67)),new TEltoAccion("Reducir","nil",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","menosu",new TRegla("F","nil",67)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","nil",67)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",239),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",240),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","Q9 E",29)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","}",new TRegla("elemento_condicion","Q9 E",29)),
,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","Q9 E",29)),,,new TEltoAccion("Desplazar","+",241),new TEltoAccion("Desplazar","-",242),
,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","Q9 E",29)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","Q9 E",29)),
new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","Q9 E",29)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","T",57)),new TEltoAccion("Reducir","num",new TRegla("E","T",57)),,,new TEltoAccion("Reducir","{",new TRegla("E","T",57)),
new TEltoAccion("Reducir","}",new TRegla("E","T",57)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","T",57)),,,new TEltoAccion("Reducir","+",new TRegla("E","T",57)),
new TEltoAccion("Reducir","-",new TRegla("E","T",57)),new TEltoAccion("Desplazar","*",243),new TEltoAccion("Desplazar","/",244),
new TEltoAccion("Reducir","op_rel",new TRegla("E","T",57)),new TEltoAccion("Reducir","cadena",new TRegla("E","T",57)),,,,,new TEltoAccion("Reducir","variable",new TRegla("E","T",57)),
new TEltoAccion("Reducir","nil",new TRegla("E","T",57)),new TEltoAccion("Reducir","menosu",new TRegla("E","T",57)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","T",57)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("T","F",60)),new TEltoAccion("Reducir","num",new TRegla("T","F",60)),,,new TEltoAccion("Reducir","{",new TRegla("T","F",60)),
new TEltoAccion("Reducir","}",new TRegla("T","F",60)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("T","F",60)),,,new TEltoAccion("Reducir","+",new TRegla("T","F",60)),
new TEltoAccion("Reducir","-",new TRegla("T","F",60)),new TEltoAccion("Reducir","*",new TRegla("T","F",60)),new TEltoAccion("Reducir","/",new TRegla("T","F",60)),
new TEltoAccion("Reducir","op_rel",new TRegla("T","F",60)),new TEltoAccion("Reducir","cadena",new TRegla("T","F",60)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("T","F",60)),new TEltoAccion("Reducir","nil",new TRegla("T","F",60)),new TEltoAccion("Reducir","menosu",new TRegla("T","F",60)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","F",60)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",176),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",245),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),
,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",220),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",246),new TEltoAccion("Desplazar","T",226),new TEltoAccion("Desplazar","F",227),
,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",247),
,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),
,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),
,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",216),new TEltoAccion("Desplazar","Relemento_cond",248),,,,,,,
,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Desplazar","doble_angulo_ce",249),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","id",66)),new TEltoAccion("Reducir","num",new TRegla("F","id",66)),,,new TEltoAccion("Reducir","{",new TRegla("F","id",66)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","id",66)),,,new TEltoAccion("Reducir","+",new TRegla("F","id",66)),new TEltoAccion("Reducir","-",new TRegla("F","id",66)),
new TEltoAccion("Reducir","*",new TRegla("F","id",66)),new TEltoAccion("Reducir","/",new TRegla("F","id",66)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","id",66)),new TEltoAccion("Reducir","cadena",new TRegla("F","id",66)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","id",66)),
new TEltoAccion("Reducir","nil",new TRegla("F","id",66)),new TEltoAccion("Reducir","menosu",new TRegla("F","id",66)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","id",66)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","id",66)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","num",64)),new TEltoAccion("Reducir","num",new TRegla("F","num",64)),,,new TEltoAccion("Reducir","{",new TRegla("F","num",64)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","num",64)),,,new TEltoAccion("Reducir","+",new TRegla("F","num",64)),new TEltoAccion("Reducir","-",new TRegla("F","num",64)),
new TEltoAccion("Reducir","*",new TRegla("F","num",64)),new TEltoAccion("Reducir","/",new TRegla("F","num",64)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","num",64)),new TEltoAccion("Reducir","cadena",new TRegla("F","num",64)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","num",64)),
new TEltoAccion("Reducir","nil",new TRegla("F","num",64)),new TEltoAccion("Reducir","menosu",new TRegla("F","num",64)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","num",64)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","num",64)),,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,new TEltoAccion("Desplazar","compute",250),new TEltoAccion("Desplazar","acceptline",251),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",252),
new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","num",new TRegla("F","cadena",65)),,,new TEltoAccion("Reducir","{",new TRegla("F","cadena",65)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","cadena",65)),,,new TEltoAccion("Reducir","+",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","-",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","*",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","/",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","cadena",new TRegla("F","cadena",65)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","nil",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","menosu",new TRegla("F","cadena",65)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","cadena",65)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","cadena",65)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","variable",63)),new TEltoAccion("Reducir","num",new TRegla("F","variable",63)),,,new TEltoAccion("Reducir","{",new TRegla("F","variable",63)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","variable",63)),,,new TEltoAccion("Reducir","+",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","-",new TRegla("F","variable",63)),new TEltoAccion("Reducir","*",new TRegla("F","variable",63)),new TEltoAccion("Reducir","/",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","variable",63)),new TEltoAccion("Reducir","cadena",new TRegla("F","variable",63)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","variable",63)),new TEltoAccion("Reducir","nil",new TRegla("F","variable",63)),
new TEltoAccion("Reducir","menosu",new TRegla("F","variable",63)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","variable",63)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","variable",63)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","nil",67)),new TEltoAccion("Reducir","num",new TRegla("F","nil",67)),,,new TEltoAccion("Reducir","{",new TRegla("F","nil",67)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","nil",67)),,,new TEltoAccion("Reducir","+",new TRegla("F","nil",67)),new TEltoAccion("Reducir","-",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","*",new TRegla("F","nil",67)),new TEltoAccion("Reducir","/",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","nil",67)),new TEltoAccion("Reducir","cadena",new TRegla("F","nil",67)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","nil",new TRegla("F","nil",67)),new TEltoAccion("Reducir","menosu",new TRegla("F","nil",67)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","nil",67)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","nil",67)),,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",253),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",254),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","num",new TRegla("elemento_condicion","Q9 E",29)),
,,new TEltoAccion("Reducir","{",new TRegla("elemento_condicion","Q9 E",29)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("elemento_condicion","Q9 E",29)),
,,new TEltoAccion("Desplazar","+",255),new TEltoAccion("Desplazar","-",256),,,new TEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","Q9 E",29)),
new TEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","Q9 E",29)),,,,,new TEltoAccion("Reducir","variable",new TRegla("elemento_condicion","Q9 E",29)),
new TEltoAccion("Reducir","nil",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","Q9 E",29)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","Q9 E",29)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","Q9 E",29)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","T",57)),new TEltoAccion("Reducir","num",new TRegla("E","T",57)),,,new TEltoAccion("Reducir","{",new TRegla("E","T",57)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","T",57)),,,new TEltoAccion("Reducir","+",new TRegla("E","T",57)),new TEltoAccion("Reducir","-",new TRegla("E","T",57)),
new TEltoAccion("Desplazar","*",257),new TEltoAccion("Desplazar","/",258),new TEltoAccion("Reducir","op_rel",new TRegla("E","T",57)),
new TEltoAccion("Reducir","cadena",new TRegla("E","T",57)),,,,,new TEltoAccion("Reducir","variable",new TRegla("E","T",57)),new TEltoAccion("Reducir","nil",new TRegla("E","T",57)),
new TEltoAccion("Reducir","menosu",new TRegla("E","T",57)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","T",57)),
new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","T",57)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("T","F",60)),new TEltoAccion("Reducir","num",new TRegla("T","F",60)),,,new TEltoAccion("Reducir","{",new TRegla("T","F",60)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("T","F",60)),,,new TEltoAccion("Reducir","+",new TRegla("T","F",60)),new TEltoAccion("Reducir","-",new TRegla("T","F",60)),
new TEltoAccion("Reducir","*",new TRegla("T","F",60)),new TEltoAccion("Reducir","/",new TRegla("T","F",60)),new TEltoAccion("Reducir","op_rel",new TRegla("T","F",60)),
new TEltoAccion("Reducir","cadena",new TRegla("T","F",60)),,,,,new TEltoAccion("Reducir","variable",new TRegla("T","F",60)),
new TEltoAccion("Reducir","nil",new TRegla("T","F",60)),new TEltoAccion("Reducir","menosu",new TRegla("T","F",60)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","F",60)),
new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("T","F",60)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",259),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",59),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,new TEltoAccion("Desplazar","pares_atrib_cond",260),,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","(",new TRegla("Q11","",79)),new TEltoAccion("Reducir",")",new TRegla("Q11","",79)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q11",261),,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
new TEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),
new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),new TEltoAccion("Desplazar","doble_angulo_ab",176),
,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",201),new TEltoAccion("Desplazar","Relemento_cond",262),,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),
,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Reducir","id",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","num",new TRegla("pdcha_cond","op_rel M E",33)),
,,new TEltoAccion("Reducir","{",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","}",new TRegla("pdcha_cond","op_rel M E",33)),,,,,
,,,,new TEltoAccion("Reducir","(",new TRegla("pdcha_cond","op_rel M E",33)),,,new TEltoAccion("Desplazar","+",241),new TEltoAccion("Desplazar","-",242),,,
new TEltoAccion("Reducir","op_rel",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","cadena",new TRegla("pdcha_cond","op_rel M E",33)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","nil",new TRegla("pdcha_cond","op_rel M E",33)),
new TEltoAccion("Reducir","menosu",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("pdcha_cond","op_rel M E",33)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),
,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",216),new TEltoAccion("Desplazar","Relemento_cond",263),,,,,,,
,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Reducir","}",new TRegla("Relemento_cond","elemento_condicion Relemento_cond",36)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",264),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",88),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",265),,},new Object[]{new TEltoAccion("Desplazar",")",266),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",238),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","num",new TRegla("F","menosu F",61)),,,new TEltoAccion("Reducir","{",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","}",new TRegla("F","menosu F",61)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","menosu F",61)),
,,new TEltoAccion("Reducir","+",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","-",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","*",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","/",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","op_rel",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","cadena",new TRegla("F","menosu F",61)),,,,,new TEltoAccion("Reducir","variable",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","nil",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","menosu",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","menosu F",61)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",239),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","T",267),new TEltoAccion("Desplazar","F",212),,},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",239),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","T",268),new TEltoAccion("Desplazar","F",212),,},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",239),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",269),,},new Object[]{new TEltoAccion("Desplazar","id",203),new TEltoAccion("Desplazar","num",204),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",239),,,,,,,,new TEltoAccion("Desplazar","cadena",206),
,,,,new TEltoAccion("Desplazar","variable",207),new TEltoAccion("Desplazar","nil",208),new TEltoAccion("Desplazar","menosu",209),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",270),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",174),
new TEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",175),
new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),new TEltoAccion("Desplazar","doble_angulo_ab",176),
,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",201),new TEltoAccion("Desplazar","Relemento_cond",271),,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",178),
,,,,new TEltoAccion("Desplazar","conjuncion",179),new TEltoAccion("Desplazar","disyuncion",180),,,,,new TEltoAccion("Desplazar","pdcha_cond",181)},new Object[]{new TEltoAccion("Reducir","id",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","num",new TRegla("pdcha_cond","op_rel M E",33)),
,,new TEltoAccion("Reducir","{",new TRegla("pdcha_cond","op_rel M E",33)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("pdcha_cond","op_rel M E",33)),
,,new TEltoAccion("Desplazar","+",255),new TEltoAccion("Desplazar","-",256),,,new TEltoAccion("Reducir","op_rel",new TRegla("pdcha_cond","op_rel M E",33)),
new TEltoAccion("Reducir","cadena",new TRegla("pdcha_cond","op_rel M E",33)),,,,,new TEltoAccion("Reducir","variable",new TRegla("pdcha_cond","op_rel M E",33)),
new TEltoAccion("Reducir","nil",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","menosu",new TRegla("pdcha_cond","op_rel M E",33)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("pdcha_cond","op_rel M E",33)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("pdcha_cond","op_rel M E",33)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("Q9","",78)),new TEltoAccion("Reducir","num",new TRegla("Q9","",78)),,,new TEltoAccion("Desplazar","{",183),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),,,,,,,new TEltoAccion("Desplazar","op_rel",184),new TEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("Q9","",78)),new TEltoAccion("Reducir","nil",new TRegla("Q9","",78)),new TEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
new TEltoAccion("Desplazar","doble_angulo_ab",185),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),
,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","elemento_condicion",216),new TEltoAccion("Desplazar","Relemento_cond",272),,,,,,,
,,,,,,,new TEltoAccion("Desplazar","Q9",187),,,,,new TEltoAccion("Desplazar","conjuncion",188),new TEltoAccion("Desplazar","disyuncion",189),,,,,new TEltoAccion("Desplazar","pdcha_cond",190)},new Object[]{new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","elemento_condicion Relemento_cond",36)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",273),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",88),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",274),,},new Object[]{new TEltoAccion("Desplazar",")",275),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",75),new TEltoAccion("Desplazar","num",76),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",77),,,,,,,,new TEltoAccion("Desplazar","cadena",78),
,,,,new TEltoAccion("Desplazar","variable",79),new TEltoAccion("Desplazar","nil",80),new TEltoAccion("Desplazar","menosu",81),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","E",252),new TEltoAccion("Desplazar","T",83),new TEltoAccion("Desplazar","F",84),,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","num",new TRegla("F","menosu F",61)),,,new TEltoAccion("Reducir","{",new TRegla("F","menosu F",61)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","menosu F",61)),,,new TEltoAccion("Reducir","+",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","-",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","*",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","/",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","cadena",new TRegla("F","menosu F",61)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","nil",new TRegla("F","menosu F",61)),
new TEltoAccion("Reducir","menosu",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","menosu F",61)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","menosu F",61)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",253),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","T",276),new TEltoAccion("Desplazar","F",227),,},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",253),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","T",277),new TEltoAccion("Desplazar","F",227),,},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",253),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",278),,},new Object[]{new TEltoAccion("Desplazar","id",218),new TEltoAccion("Desplazar","num",219),,,,,,,,,,,,,new TEltoAccion("Desplazar","(",253),,,,,,,,new TEltoAccion("Desplazar","cadena",221),
,,,,new TEltoAccion("Desplazar","variable",222),new TEltoAccion("Desplazar","nil",223),new TEltoAccion("Desplazar","menosu",224),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","F",279),,},new Object[]{new TEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Mmarca",280),,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",281),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","(",85),new TEltoAccion("Reducir",")",new TRegla("Remt","",15)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Remt",282),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","}",283),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","doble_angulo_ce",284),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",285),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",286),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","num",new TRegla("F","( E )",62)),,,new TEltoAccion("Reducir","{",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","}",new TRegla("F","( E )",62)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","( E )",62)),
,,new TEltoAccion("Reducir","+",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","-",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","*",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","/",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","op_rel",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","cadena",new TRegla("F","( E )",62)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","nil",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","menosu",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","( E )",62)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","num",new TRegla("E","E + T",55)),,,new TEltoAccion("Reducir","{",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","}",new TRegla("E","E + T",55)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","E + T",55)),
,,new TEltoAccion("Reducir","+",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","-",new TRegla("E","E + T",55)),new TEltoAccion("Desplazar","*",243),
new TEltoAccion("Desplazar","/",244),new TEltoAccion("Reducir","op_rel",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","cadena",new TRegla("E","E + T",55)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","nil",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","menosu",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E + T",55)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","num",new TRegla("E","E - T",56)),,,new TEltoAccion("Reducir","{",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","}",new TRegla("E","E - T",56)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","E - T",56)),
,,new TEltoAccion("Reducir","+",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","-",new TRegla("E","E - T",56)),new TEltoAccion("Desplazar","*",243),
new TEltoAccion("Desplazar","/",244),new TEltoAccion("Reducir","op_rel",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","cadena",new TRegla("E","E - T",56)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","nil",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","menosu",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E - T",56)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","num",new TRegla("T","T * F",58)),,,new TEltoAccion("Reducir","{",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","}",new TRegla("T","T * F",58)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("T","T * F",58)),
,,new TEltoAccion("Reducir","+",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","-",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","*",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","/",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","op_rel",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","cadena",new TRegla("T","T * F",58)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","nil",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","menosu",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T * F",58)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","num",new TRegla("T","T / F",59)),,,new TEltoAccion("Reducir","{",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","}",new TRegla("T","T / F",59)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("T","T / F",59)),
,,new TEltoAccion("Reducir","+",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","-",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","*",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","/",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","op_rel",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","cadena",new TRegla("T","T / F",59)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","nil",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","menosu",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T / F",59)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","}",287),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","doble_angulo_ce",288),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",289),,new TEltoAccion("Desplazar","+",110),new TEltoAccion("Desplazar","-",111),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",290),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","num",new TRegla("F","( E )",62)),,,new TEltoAccion("Reducir","{",new TRegla("F","( E )",62)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("F","( E )",62)),,,new TEltoAccion("Reducir","+",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","-",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","*",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","/",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","op_rel",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","cadena",new TRegla("F","( E )",62)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","nil",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","menosu",new TRegla("F","( E )",62)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","( E )",62)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","( E )",62)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","num",new TRegla("E","E + T",55)),,,new TEltoAccion("Reducir","{",new TRegla("E","E + T",55)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","E + T",55)),,,new TEltoAccion("Reducir","+",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","-",new TRegla("E","E + T",55)),new TEltoAccion("Desplazar","*",257),new TEltoAccion("Desplazar","/",258),new TEltoAccion("Reducir","op_rel",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","cadena",new TRegla("E","E + T",55)),,,,,new TEltoAccion("Reducir","variable",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","nil",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","menosu",new TRegla("E","E + T",55)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E + T",55)),
new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","E + T",55)),,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","num",new TRegla("E","E - T",56)),,,new TEltoAccion("Reducir","{",new TRegla("E","E - T",56)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","E - T",56)),,,new TEltoAccion("Reducir","+",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","-",new TRegla("E","E - T",56)),new TEltoAccion("Desplazar","*",257),new TEltoAccion("Desplazar","/",258),new TEltoAccion("Reducir","op_rel",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","cadena",new TRegla("E","E - T",56)),,,,,new TEltoAccion("Reducir","variable",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","nil",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","menosu",new TRegla("E","E - T",56)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E - T",56)),
new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","E - T",56)),,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","num",new TRegla("T","T * F",58)),,,new TEltoAccion("Reducir","{",new TRegla("T","T * F",58)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("T","T * F",58)),,,new TEltoAccion("Reducir","+",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","-",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","*",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","/",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","op_rel",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","cadena",new TRegla("T","T * F",58)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","nil",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","menosu",new TRegla("T","T * F",58)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T * F",58)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("T","T * F",58)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","num",new TRegla("T","T / F",59)),,,new TEltoAccion("Reducir","{",new TRegla("T","T / F",59)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("T","T / F",59)),,,new TEltoAccion("Reducir","+",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","-",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","*",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","/",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","op_rel",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","cadena",new TRegla("T","T / F",59)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","nil",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","menosu",new TRegla("T","T / F",59)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T / F",59)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("T","T / F",59)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","^",59),,,,,,,,,,,,new TEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,new TEltoAccion("Desplazar","pares_atrib_cond",291),,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","{",new TRegla("Q6","",76)),,new TEltoAccion("Reducir","->",new TRegla("Q6","",76)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q6","",76)),
,,,new TEltoAccion("Reducir","-",new TRegla("Q6","",76)),,,,,,,,,,,new TEltoAccion("Reducir","menosu",new TRegla("Q6","",76)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q6",292),,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir",")",new TRegla("Remt","( make id Q1 pares_atrib_valor ) Q11 Remt",14)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),new TEltoAccion("Reducir","num",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,new TEltoAccion("Reducir","{",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","}",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","cadena",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","nil",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","menosu",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","num",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),,,new TEltoAccion("Reducir","{",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","}",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","cadena",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","nil",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","menosu",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","num",new TRegla("E","( compute E )",53)),,,new TEltoAccion("Reducir","{",new TRegla("E","( compute E )",53)),
new TEltoAccion("Reducir","}",new TRegla("E","( compute E )",53)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","( compute E )",53)),
,,new TEltoAccion("Reducir","+",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","-",new TRegla("E","( compute E )",53)),
,,new TEltoAccion("Reducir","op_rel",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","cadena",new TRegla("E","( compute E )",53)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","nil",new TRegla("E","( compute E )",53)),
new TEltoAccion("Reducir","menosu",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( compute E )",53)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","num",new TRegla("E","( acceptline F )",54)),,,
new TEltoAccion("Reducir","{",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","}",new TRegla("E","( acceptline F )",54)),,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","( acceptline F )",54)),
,,new TEltoAccion("Reducir","+",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","-",new TRegla("E","( acceptline F )",54)),
,,new TEltoAccion("Reducir","op_rel",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","cadena",new TRegla("E","( acceptline F )",54)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","nil",new TRegla("E","( acceptline F )",54)),
new TEltoAccion("Reducir","menosu",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( acceptline F )",54)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),new TEltoAccion("Reducir","num",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,new TEltoAccion("Reducir","{",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","cadena",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),,,,,new TEltoAccion("Reducir","variable",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","nil",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","menosu",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","num",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),,,new TEltoAccion("Reducir","{",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,new TEltoAccion("Reducir","op_rel",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","cadena",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,new TEltoAccion("Reducir","variable",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","nil",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","menosu",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","num",new TRegla("E","( compute E )",53)),,,new TEltoAccion("Reducir","{",new TRegla("E","( compute E )",53)),
,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","( compute E )",53)),,,new TEltoAccion("Reducir","+",new TRegla("E","( compute E )",53)),
new TEltoAccion("Reducir","-",new TRegla("E","( compute E )",53)),,,new TEltoAccion("Reducir","op_rel",new TRegla("E","( compute E )",53)),
new TEltoAccion("Reducir","cadena",new TRegla("E","( compute E )",53)),,,,,new TEltoAccion("Reducir","variable",new TRegla("E","( compute E )",53)),
new TEltoAccion("Reducir","nil",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","menosu",new TRegla("E","( compute E )",53)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( compute E )",53)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","( compute E )",53)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","id",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","num",new TRegla("E","( acceptline F )",54)),,,
new TEltoAccion("Reducir","{",new TRegla("E","( acceptline F )",54)),,,,,,,,,,new TEltoAccion("Reducir","(",new TRegla("E","( acceptline F )",54)),,,new TEltoAccion("Reducir","+",new TRegla("E","( acceptline F )",54)),
new TEltoAccion("Reducir","-",new TRegla("E","( acceptline F )",54)),,,new TEltoAccion("Reducir","op_rel",new TRegla("E","( acceptline F )",54)),
new TEltoAccion("Reducir","cadena",new TRegla("E","( acceptline F )",54)),,,,,new TEltoAccion("Reducir","variable",new TRegla("E","( acceptline F )",54)),
new TEltoAccion("Reducir","nil",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","menosu",new TRegla("E","( acceptline F )",54)),
new TEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( acceptline F )",54)),new TEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","( acceptline F )",54)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar",")",293),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","{",135),,new TEltoAccion("Reducir","->",new TRegla("Rcond","",23)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),
,,,new TEltoAccion("Desplazar","-",25),,,,,,,,,,,new TEltoAccion("Desplazar","menosu",26),,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Rcond",294),
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",28),,,,,,,,,,new TEltoAccion("Desplazar","negar_cond",137),},new Object[]{new TEltoAccion("Desplazar","}",295),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","->",new TRegla("Rcond","negar_cond ( id Mmarca pares_atrib_cond ) Q6 Rcond",22)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Reducir","{",new TRegla("Q5","",75)),,new TEltoAccion("Reducir","->",new TRegla("Q5","",75)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q5","",75)),
,,,new TEltoAccion("Reducir","-",new TRegla("Q5","",75)),,,,,,,,,,,new TEltoAccion("Reducir","menosu",new TRegla("Q5","",75)),
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q5",296),,,,,,,,,,,,,,},new Object[]{new TEltoAccion("Desplazar","{",135),,new TEltoAccion("Reducir","->",new TRegla("Rcond","",23)),,,,,,,,new TEltoAccion("Reducir","(",new TRegla("Q9","",78)),
,,,new TEltoAccion("Desplazar","-",25),,,,,,,,,,,new TEltoAccion("Desplazar","menosu",26),,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Rcond",297),
,,,,,,,,,,,,,,,,,new TEltoAccion("Desplazar","Q9",28),,,,,,,,,,new TEltoAccion("Desplazar","negar_cond",137),},new Object[]{new TEltoAccion("Reducir","->",new TRegla("Rcond","{ variable M ( id Mmarca pares_atrib_cond ) } Q5 Rcond",21)),,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,}};
		tablaaccion = new Vector<TVectorAccion>();
		for(int z = 0;z<bindVars.length;z++){
			acc = new TVectorAccion();
			for(int k=0;k<bindVars[z].length;k++)
				acc.addElement((TEltoAccion)bindVars[z][k]);
			tablaaccion.addElement(acc);
		}
	}
	boolean parserascendente() {
		Stack<String> pila = new Stack<String>();
		String tipo, strest;
		TElemEnt sim, sim_ant;
		TVectorIra ira;
		TVectorAccion acc;
		int est, nest, longi;
		TRegla regla;
		strest = "";
		boolean error;
		this.ts = new TTablaSimbolos();
		error = false;
		pila.push("0");
		sim = ent.leersigterm(new TElemEnt(),this.ts);
		do {
			est = Integer.parseInt(pila.peek());
			acc = this.tablaaccion.elementAt(est);
			tipo = acc.gettipo(sim.token);
			if (tipo.equalsIgnoreCase("Desplazar")) {
				nest = acc.getSigEstado(sim.token);
				pila.push(String.valueOf(nest));
				sim_ant = sim;
				sim = ent.leersigterm(sim,this.ts);
			} else if (tipo.equalsIgnoreCase("Reducir")) {
				regla = acc.getregla(sim.token);
				longi = regla.rhs.size();
				for (int i = 0; i < longi; i++)
					strest = pila.pop();
				nest = Integer.parseInt(pila.peek());
				ira = (TVectorIra)this.tablaira.elementAt(nest);
				nest = ira.getestado(regla.lhs);
				pila.push(String.valueOf(nest));
				System.out.println("Ejecutar acc.sem. regla=" + String.valueOf(regla.num));
			} else if (tipo.equalsIgnoreCase("")){
				System.out.println("Falta entrada en accion estado="+String.valueOf(est)+" con="+sim.token);
				error = true;
			}	
		} while (!tipo.equalsIgnoreCase("Aceptar") && !error);
		return error;
	}
}
public class pruebaops5{
	public static void main(String[] args) {
		String s = new String("(literalize student\n" +
			                    "        name\n" +
			                    "        id\n" +
			                    "        maj-code)\n" +
			                    "\n" +
			                    "(literalize majors\n" +
			                    "        major-code\n" +
			                    "        major-name)\n" +
			                    "\n" +
			                    "(startup\n" +
			                    "	(make student ^name Laura ^id 2233 ^maj-code 10)\n" +
			                    "	(make student ^name Laura ^id 1234 ^maj-code 11)\n" +
			                    "	(make student ^name John ^id 4321 ^maj-code 11)\n" +
			                    "	(make student ^name Heather ^id 3214 ^maj-code 10)\n" +
			                    "	(make student ^name Becca ^id 9876 ^maj-code 10)\n" +
			                    "	(make student ^name Kathy ^id 6543 ^maj-code 11)\n" +
			                    "	(make majors ^major-code 10 ^major-name biology)\n" +
			                    "	(make majors ^major-code 11 ^major-name chemistry)\n" +
			                    ")\n" +
			                    "\n" +
			                    "(p select-major\n" +
			                    "        (student ^name <name> ^maj-code <mc> ^id <id>)\n" +
			                    "        (majors ^major-code <mc> ^major-name <major>)\n" +
			                    "      - (student ^maj-code < <mc>)\n" +
			                    "-->\n" +
			                    "        (write <name> (tabto 20) <major> (tabto 30) <id> (crlf))\n" +
			                    "        (remove 1)\n" +
			                    ")");
		parser_OPS5P g = new parser_OPS5P(s);
		g.parserascendente();
	}
}
