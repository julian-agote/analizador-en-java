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
	TElemIra getElemIra(String simb, int est) {
		return new TElemIra(simb,est);
	}
	void initablaira(){
		TVectorIra ira;
		TElemIra item;
		TElemIra[][] bindVars = new TElemIra[][]{{getElemIra("(",1),getElemIra("OPS5",2)},{getElemIra("vector_attribute",3),getElemIra("p",4),getElemIra("literalize",5),getElemIra("startup",6),getElemIra("orden",7)},{getElemIra("$",8)},{getElemIra("id",9),getElemIra("vector_name",10)},{getElemIra("id",11)},{getElemIra("id",12)},{getElemIra("(",13),getElemIra("emt",14)},{getElemIra(")",15)},{},{getElemIra("id",16),getElemIra("Rvector_name",17)},{},{getElemIra("Q4",18)},{getElemIra("Q0",19)},{getElemIra("make",20)},{},{getElemIra("(",21),getElemIra("Rorden",22)},{getElemIra("id",16),getElemIra("Rvector_name",23)},{},{getElemIra("{",24),getElemIra("-",25),getElemIra("menosu",26),getElemIra("cond",27),getElemIra("Q9",28),getElemIra("negar_cond",29)},{getElemIra("id",30),getElemIra("atributo_escalar",31)},{getElemIra("id",32)},{getElemIra("vector_attribute",3),getElemIra("p",4),getElemIra("literalize",5),getElemIra("startup",6),getElemIra("orden",33)},{},{},{getElemIra("variable",34)},{getElemIra("M",35)},{getElemIra("M",36)},{getElemIra("Q8",37)},{},{getElemIra("(",38)},{getElemIra("Q2",39)},{},{getElemIra("Q1",40)},{getElemIra(")",41)},{getElemIra("M",42)},{},{},{getElemIra("->",43)},{getElemIra("id",44)},{getElemIra("id",45),getElemIra("Ratributo_escalar",46)},{getElemIra("^",47),getElemIra("pares_atrib_valor",48)},{getElemIra("(",21),getElemIra("Rorden",49)},{getElemIra("(",50)},{getElemIra("Mponer_marca_lista",51)},{getElemIra("Mmarca",52)},{getElemIra("Q2",53)},{},{getElemIra("id",54)},{getElemIra(")",55)},{},{getElemIra("id",56)},{getElemIra("(",57),getElemIra("acc",58)},{getElemIra("^",59),getElemIra("pares_atrib_cond",60)},{getElemIra("id",45),getElemIra("Ratributo_escalar",61)},{getElemIra("M",62)},{getElemIra("Q11",63)},{getElemIra("Mmarca",64)},{getElemIra("make",65),getElemIra("modify",66),getElemIra("remove",67),getElemIra("bind",68),getElemIra("write",69),getElemIra("halt",70)},{getElemIra("(",57),getElemIra("acc",71),getElemIra("Racc",72)},{getElemIra("id",73)},{getElemIra(")",74)},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",82),getElemIra("T",83),getElemIra("F",84)},{getElemIra("(",85),getElemIra("Remt",86)},{getElemIra("^",59),getElemIra("pares_atrib_cond",87)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",88),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",89)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",88),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",90)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",88),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",91)},{getElemIra("id",92),getElemIra("num",93),getElemIra("(",94),getElemIra("cadena",95),getElemIra("variable",96),getElemIra("nil",97),getElemIra("menosu",98),
			getElemIra("F",99)},{getElemIra("Mponer_marca_lista",100)},{getElemIra(")",101)},{getElemIra("(",57),getElemIra("acc",71),getElemIra("Racc",102)},{},{getElemIra("M",103)},{getElemIra("Q6",104)},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("compute",105),getElemIra("acceptline",106),getElemIra("E",107),getElemIra("T",83),getElemIra("F",84)},{},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",108),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",109)},{getElemIra("+",110),getElemIra("-",111),getElemIra("Q3",112)},{getElemIra("*",113),getElemIra("/",114)},{},{getElemIra("make",115)},{},{getElemIra(")",116)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",107),getElemIra("T",83),getElemIra("F",84)},{getElemIra("Mponer_marca_lista",117)},{getElemIra("Mponer_marca_lista",118)},{getElemIra(")",119)},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",120),getElemIra("T",83),getElemIra("F",84)},{},{},{},{getElemIra("id",92),getElemIra("num",93),getElemIra("(",94),getElemIra("cadena",95),getElemIra("variable",96),getElemIra("nil",97),getElemIra("menosu",98),
			getElemIra("F",121)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",122),getElemIra("T",83),getElemIra("F",84)},{getElemIra("(",123),getElemIra("variable",124),getElemIra("texto",125),getElemIra("elem_write",126)},{},{},{getElemIra("{",127),getElemIra("op_rel",128),getElemIra("doble_angulo_ab",129),getElemIra("elemento_condicion",130),getElemIra("Q9",131),getElemIra("conjuncion",132),
			getElemIra("disyuncion",133),getElemIra("pdcha_cond",134)},{getElemIra("{",135),getElemIra("-",25),getElemIra("menosu",26),getElemIra("Rcond",136),getElemIra("Q9",28),getElemIra("negar_cond",137)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",138),getElemIra("T",83),getElemIra("F",84)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",88),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",139)},{getElemIra(")",140),getElemIra("+",110),getElemIra("-",111)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",107),getElemIra("T",83),getElemIra("F",84)},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",108),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("T",141),getElemIra("F",84)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",108),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("T",142),getElemIra("F",84)},{getElemIra("^",47),getElemIra("pares_atrib_valor",143)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",108),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",144)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",108),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",145)},{getElemIra("id",146)},{getElemIra("}",147)},{getElemIra("^",47),getElemIra("pares_atrib_valor",148)},{getElemIra("^",47),getElemIra("pares_atrib_valor",149)},{},{getElemIra(")",150),getElemIra("+",110),getElemIra("-",111)},{},{getElemIra(")",151),getElemIra("+",110),getElemIra("-",111)},{getElemIra("rjust",152),getElemIra("tabto",153),getElemIra("crlf",154)},{getElemIra("(",123),getElemIra("variable",124),getElemIra("texto",155),getElemIra("elem_write",126)},{getElemIra(")",156)},{getElemIra("(",123),getElemIra("variable",124),getElemIra("texto",157),getElemIra("elem_write",126)},{getElemIra("Mponer_marca_conj",158)},{getElemIra("M",159)},{getElemIra("Mponer_marca_dis",160)},{getElemIra("^",59),getElemIra("pares_atrib_cond",161)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",162),getElemIra("T",83),getElemIra("F",84)},{},{},{},{getElemIra("variable",163)},{},{getElemIra("(",164)},{getElemIra(")",165),getElemIra("+",110),getElemIra("-",111)},{getElemIra(")",166)},{},{getElemIra("*",113),getElemIra("/",114)},{getElemIra("*",113),getElemIra("/",114)},{},{},{},{getElemIra("Q1",167)},{getElemIra("Q5",168)},{getElemIra(")",169)},{getElemIra(")",170)},{},{},{getElemIra("num",171)},{getElemIra("num",172)},{getElemIra("M",173)},{},{},{},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",177),getElemIra("Q9",178),getElemIra("conjuncion",179),
			getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",182),getElemIra("T",83),getElemIra("F",84)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",186),getElemIra("Q9",187),getElemIra("conjuncion",188),
			getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{},{getElemIra("+",110),getElemIra("-",111)},{getElemIra("M",191)},{getElemIra("id",192)},{},{},{getElemIra("^",47),getElemIra("pares_atrib_valor",193)},{getElemIra("{",135),getElemIra("-",25),getElemIra("menosu",26),getElemIra("Rcond",194),getElemIra("Q9",28),getElemIra("negar_cond",137)},{},{},{getElemIra(")",195)},{getElemIra(")",196)},{getElemIra(")",197)},{getElemIra("Mponer_marca_conj",198)},{getElemIra("M",199)},{getElemIra("Mponer_marca_dis",200)},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",201),getElemIra("Relemento_cond",202),getElemIra("Q9",178),
			getElemIra("conjuncion",179),getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",205),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("E",210),getElemIra("T",211),getElemIra("F",212)},{},{},{},{getElemIra("+",110),getElemIra("-",111)},{getElemIra("Mponer_marca_conj",213)},{getElemIra("M",214)},{getElemIra("Mponer_marca_dis",215)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",216),getElemIra("Relemento_cond",217),getElemIra("Q9",187),
			getElemIra("conjuncion",188),getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",220),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("E",225),getElemIra("T",226),getElemIra("F",227)},{},{},{},{getElemIra("(",228)},{getElemIra("Mmarca",229)},{getElemIra(")",230)},{},{},{},{},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",231),getElemIra("Q9",178),getElemIra("conjuncion",179),
			getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",205),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("E",232),getElemIra("T",211),getElemIra("F",212)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",233),getElemIra("Q9",187),getElemIra("conjuncion",188),
			getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",201),getElemIra("Relemento_cond",234),getElemIra("Q9",178),
			getElemIra("conjuncion",179),getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("}",235)},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("compute",236),getElemIra("acceptline",237),getElemIra("E",238),getElemIra("T",83),getElemIra("F",84)},{},{},{},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",239),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("F",240)},{getElemIra("+",241),getElemIra("-",242)},{getElemIra("*",243),getElemIra("/",244)},{},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",245),getElemIra("Q9",178),getElemIra("conjuncion",179),
			getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",220),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("E",246),getElemIra("T",226),getElemIra("F",227)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",247),getElemIra("Q9",187),getElemIra("conjuncion",188),
			getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",216),getElemIra("Relemento_cond",248),getElemIra("Q9",187),
			getElemIra("conjuncion",188),getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{getElemIra("doble_angulo_ce",249)},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("compute",250),getElemIra("acceptline",251),getElemIra("E",252),getElemIra("T",83),getElemIra("F",84)},{},{},{},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",253),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("F",254)},{getElemIra("+",255),getElemIra("-",256)},{getElemIra("*",257),getElemIra("/",258)},{},{getElemIra("id",259)},{getElemIra("^",59),getElemIra("pares_atrib_cond",260)},{getElemIra("Q11",261)},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",201),getElemIra("Relemento_cond",262),getElemIra("Q9",178),
			getElemIra("conjuncion",179),getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("+",241),getElemIra("-",242)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",216),getElemIra("Relemento_cond",263),getElemIra("Q9",187),
			getElemIra("conjuncion",188),getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",264),getElemIra("T",83),getElemIra("F",84)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",88),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",265)},{getElemIra(")",266),getElemIra("+",110),getElemIra("-",111)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",238),getElemIra("T",83),getElemIra("F",84)},{},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",239),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("T",267),getElemIra("F",212)},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",239),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("T",268),getElemIra("F",212)},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",239),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("F",269)},{getElemIra("id",203),getElemIra("num",204),getElemIra("(",239),getElemIra("cadena",206),getElemIra("variable",207),getElemIra("nil",208),getElemIra("menosu",209),
			getElemIra("F",270)},{getElemIra("{",174),getElemIra("op_rel",175),getElemIra("doble_angulo_ab",176),getElemIra("elemento_condicion",201),getElemIra("Relemento_cond",271),getElemIra("Q9",178),
			getElemIra("conjuncion",179),getElemIra("disyuncion",180),getElemIra("pdcha_cond",181)},{getElemIra("+",255),getElemIra("-",256)},{getElemIra("{",183),getElemIra("op_rel",184),getElemIra("doble_angulo_ab",185),getElemIra("elemento_condicion",216),getElemIra("Relemento_cond",272),getElemIra("Q9",187),
			getElemIra("conjuncion",188),getElemIra("disyuncion",189),getElemIra("pdcha_cond",190)},{},{},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",273),getElemIra("T",83),getElemIra("F",84)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",88),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("F",274)},{getElemIra(")",275),getElemIra("+",110),getElemIra("-",111)},{getElemIra("id",75),getElemIra("num",76),getElemIra("(",77),getElemIra("cadena",78),getElemIra("variable",79),getElemIra("nil",80),getElemIra("menosu",81),
			getElemIra("E",252),getElemIra("T",83),getElemIra("F",84)},{},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",253),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("T",276),getElemIra("F",227)},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",253),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("T",277),getElemIra("F",227)},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",253),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("F",278)},{getElemIra("id",218),getElemIra("num",219),getElemIra("(",253),getElemIra("cadena",221),getElemIra("variable",222),getElemIra("nil",223),getElemIra("menosu",224),
			getElemIra("F",279)},{getElemIra("Mmarca",280)},{getElemIra(")",281)},{getElemIra("(",85),getElemIra("Remt",282)},{getElemIra("}",283)},{getElemIra("doble_angulo_ce",284)},{getElemIra(")",285),getElemIra("+",110),getElemIra("-",111)},{getElemIra(")",286)},{},{getElemIra("*",243),getElemIra("/",244)},{getElemIra("*",243),getElemIra("/",244)},{},{},{getElemIra("}",287)},{getElemIra("doble_angulo_ce",288)},{getElemIra(")",289),getElemIra("+",110),getElemIra("-",111)},{getElemIra(")",290)},{},{getElemIra("*",257),getElemIra("/",258)},{getElemIra("*",257),getElemIra("/",258)},{},{},{getElemIra("^",59),getElemIra("pares_atrib_cond",291)},{getElemIra("Q6",292)},{},{},{},{},{},{},{},{},{},{getElemIra(")",293)},{getElemIra("{",135),getElemIra("-",25),getElemIra("menosu",26),getElemIra("Rcond",294),getElemIra("Q9",28),getElemIra("negar_cond",137)},{getElemIra("}",295)},{},{getElemIra("Q5",296)},{getElemIra("{",135),getElemIra("-",25),getElemIra("menosu",26),getElemIra("Rcond",297),getElemIra("Q9",28),getElemIra("negar_cond",137)},{}};
		tablaira = new Vector<TVectorIra>();
		for(int z = 0;z<bindVars.length;z++){
			ira = new TVectorIra();
			for(int k=0;k<bindVars[z].length;k++)
				ira.addElement(bindVars[z][k]);
			tablaira.addElement(ira);
		}
	}
	TEltoAccion getEltoAccion(String ptipo,String psimbolo,TRegla pr){
		return new TEltoAccion(ptipo,psimbolo,pr);
	}
	TEltoAccion getEltoAccion(String ptipo,String psimbolo,int psig_estado){
		return new TEltoAccion(ptipo,psimbolo,psig_estado);
	}
	void initablaaccion(){
		TVectorAccion acc;
		TEltoAccion itema;
		TEltoAccion[][] bindVars = new TEltoAccion[][]{{getEltoAccion("Desplazar","(",1),getEltoAccion("Desplazar","OPS5",2)},{getEltoAccion("Desplazar","vector_attribute",3),getEltoAccion("Desplazar","p",4),getEltoAccion("Desplazar","literalize",5),getEltoAccion("Desplazar","startup",6),
			getEltoAccion("Desplazar","orden",7)},{getEltoAccion("Desplazar","$",8)},{getEltoAccion("Desplazar","id",9),getEltoAccion("Desplazar","vector_name",10)},{getEltoAccion("Desplazar","id",11)},{getEltoAccion("Desplazar","id",12)},{getEltoAccion("Desplazar","(",13),getEltoAccion("Desplazar","emt",14)},{getEltoAccion("Desplazar",")",15)},{getEltoAccion("Aceptar","$",0)},{getEltoAccion("Desplazar","id",16),getEltoAccion("Reducir",")",new TRegla("Rvector_name","",11)),getEltoAccion("Desplazar","Rvector_name",17)},{getEltoAccion("Reducir",")",new TRegla("orden","vector_attribute vector_name",3))},{getEltoAccion("Reducir","{",new TRegla("Q4","",74)),getEltoAccion("Reducir","(",new TRegla("Q4","",74)),getEltoAccion("Reducir","-",new TRegla("Q4","",74)),
			getEltoAccion("Reducir","menosu",new TRegla("Q4","",74)),getEltoAccion("Desplazar","Q4",18)},{getEltoAccion("Reducir","id",new TRegla("Q0","",70)),getEltoAccion("Desplazar","Q0",19)},{getEltoAccion("Desplazar","make",20)},{getEltoAccion("Reducir",")",new TRegla("orden","startup emt",12))},{getEltoAccion("Desplazar","(",21),getEltoAccion("Reducir","$",new TRegla("Rorden","",5)),getEltoAccion("Desplazar","Rorden",22)},{getEltoAccion("Desplazar","id",16),getEltoAccion("Reducir",")",new TRegla("Rvector_name","",11)),getEltoAccion("Desplazar","Rvector_name",23)},{getEltoAccion("Reducir",")",new TRegla("vector_name","id Rvector_name",9))},{getEltoAccion("Desplazar","{",24),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","-",25),getEltoAccion("Desplazar","menosu",26),
			getEltoAccion("Desplazar","cond",27),getEltoAccion("Desplazar","Q9",28),getEltoAccion("Desplazar","negar_cond",29)},{getEltoAccion("Desplazar","id",30),getEltoAccion("Desplazar","atributo_escalar",31)},{getEltoAccion("Desplazar","id",32)},{getEltoAccion("Desplazar","vector_attribute",3),getEltoAccion("Desplazar","p",4),getEltoAccion("Desplazar","literalize",5),getEltoAccion("Desplazar","startup",6),
			getEltoAccion("Desplazar","orden",33)},{getEltoAccion("Reducir","$",new TRegla("OPS5","( orden ) Rorden",1))},{getEltoAccion("Reducir",")",new TRegla("Rvector_name","id Rvector_name",10))},{getEltoAccion("Desplazar","variable",34)},{getEltoAccion("Reducir","(",new TRegla("M","",68)),getEltoAccion("Desplazar","M",35)},{getEltoAccion("Reducir","(",new TRegla("M","",68)),getEltoAccion("Desplazar","M",36)},{getEltoAccion("Reducir","->",new TRegla("Q8","",77)),getEltoAccion("Desplazar","Q8",37)},{getEltoAccion("Reducir","(",new TRegla("negar_cond","Q9",26))},{getEltoAccion("Desplazar","(",38)},{getEltoAccion("Reducir","id",new TRegla("Q2","",72)),getEltoAccion("Reducir",")",new TRegla("Q2","",72)),getEltoAccion("Desplazar","Q2",39)},{getEltoAccion("Reducir",")",new TRegla("orden","literalize id Q0 atributo_escalar",2))},{getEltoAccion("Reducir","^",new TRegla("Q1","",71)),getEltoAccion("Reducir",")",new TRegla("Q1","",71)),getEltoAccion("Desplazar","Q1",40)},{getEltoAccion("Desplazar",")",41)},{getEltoAccion("Reducir","(",new TRegla("M","",68)),getEltoAccion("Desplazar","M",42)},{getEltoAccion("Reducir","(",new TRegla("negar_cond","- M",24))},{getEltoAccion("Reducir","(",new TRegla("negar_cond","menosu M",25))},{getEltoAccion("Desplazar","->",43)},{getEltoAccion("Desplazar","id",44)},{getEltoAccion("Desplazar","id",45),getEltoAccion("Reducir",")",new TRegla("Ratributo_escalar","",8)),getEltoAccion("Desplazar","Ratributo_escalar",46)},{getEltoAccion("Desplazar","^",47),getEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),getEltoAccion("Desplazar","pares_atrib_valor",48)},{getEltoAccion("Desplazar","(",21),getEltoAccion("Reducir","$",new TRegla("Rorden","",5)),getEltoAccion("Desplazar","Rorden",49)},{getEltoAccion("Desplazar","(",50)},{getEltoAccion("Reducir","(",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Desplazar","Mponer_marca_lista",51)},{getEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),getEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),getEltoAccion("Desplazar","Mmarca",52)},{getEltoAccion("Reducir","id",new TRegla("Q2","",72)),getEltoAccion("Reducir",")",new TRegla("Q2","",72)),getEltoAccion("Desplazar","Q2",53)},{getEltoAccion("Reducir",")",new TRegla("atributo_escalar","id Q2 Ratributo_escalar",6))},{getEltoAccion("Desplazar","id",54)},{getEltoAccion("Desplazar",")",55)},{getEltoAccion("Reducir","$",new TRegla("Rorden","( orden ) Rorden",4))},{getEltoAccion("Desplazar","id",56)},{getEltoAccion("Desplazar","(",57),getEltoAccion("Desplazar","acc",58)},{getEltoAccion("Desplazar","^",59),getEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),getEltoAccion("Desplazar","pares_atrib_cond",60)},{getEltoAccion("Desplazar","id",45),getEltoAccion("Reducir",")",new TRegla("Ratributo_escalar","",8)),getEltoAccion("Desplazar","Ratributo_escalar",61)},{getEltoAccion("Reducir","id",new TRegla("M","",68)),getEltoAccion("Reducir","num",new TRegla("M","",68)),getEltoAccion("Reducir","(",new TRegla("M","",68)),
			getEltoAccion("Reducir","cadena",new TRegla("M","",68)),getEltoAccion("Reducir","variable",new TRegla("M","",68)),getEltoAccion("Reducir","nil",new TRegla("M","",68)),
			getEltoAccion("Reducir","menosu",new TRegla("M","",68)),getEltoAccion("Desplazar","M",62)},{getEltoAccion("Reducir","(",new TRegla("Q11","",79)),getEltoAccion("Reducir",")",new TRegla("Q11","",79)),getEltoAccion("Desplazar","Q11",63)},{getEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),getEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),getEltoAccion("Desplazar","Mmarca",64)},{getEltoAccion("Desplazar","make",65),getEltoAccion("Desplazar","modify",66),getEltoAccion("Desplazar","remove",67),getEltoAccion("Desplazar","bind",68),getEltoAccion("Desplazar","write",69),
			getEltoAccion("Desplazar","halt",70)},{getEltoAccion("Desplazar","(",57),getEltoAccion("Reducir",")",new TRegla("Racc","",46)),getEltoAccion("Desplazar","acc",71),getEltoAccion("Desplazar","Racc",72)},{getEltoAccion("Desplazar","id",73)},{getEltoAccion("Desplazar",")",74)},{getEltoAccion("Reducir",")",new TRegla("Ratributo_escalar","id Q2 Ratributo_escalar",7))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",82),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","(",85),getEltoAccion("Reducir",")",new TRegla("Remt","",15)),getEltoAccion("Desplazar","Remt",86)},{getEltoAccion("Desplazar","^",59),getEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),getEltoAccion("Desplazar","pares_atrib_cond",87)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",88),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",89)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",88),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",90)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",88),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",91)},{getEltoAccion("Desplazar","id",92),getEltoAccion("Desplazar","num",93),getEltoAccion("Desplazar","(",94),getEltoAccion("Desplazar","cadena",95),getEltoAccion("Desplazar","variable",96),
			getEltoAccion("Desplazar","nil",97),getEltoAccion("Desplazar","menosu",98),getEltoAccion("Desplazar","F",99)},{getEltoAccion("Reducir","(",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Reducir",")",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_lista","",82)),
			getEltoAccion("Desplazar","Mponer_marca_lista",100)},{getEltoAccion("Desplazar",")",101)},{getEltoAccion("Desplazar","(",57),getEltoAccion("Reducir",")",new TRegla("Racc","",46)),getEltoAccion("Desplazar","acc",71),getEltoAccion("Desplazar","Racc",102)},{getEltoAccion("Reducir",")",new TRegla("orden","p id Q4 cond Q8 -> Mponer_marca_lista acc Racc",18))},{getEltoAccion("Reducir","id",new TRegla("M","",68)),getEltoAccion("Reducir","num",new TRegla("M","",68)),getEltoAccion("Reducir","{",new TRegla("M","",68)),
			getEltoAccion("Reducir","(",new TRegla("M","",68)),getEltoAccion("Reducir","op_rel",new TRegla("M","",68)),getEltoAccion("Reducir","cadena",new TRegla("M","",68)),
			getEltoAccion("Reducir","variable",new TRegla("M","",68)),getEltoAccion("Reducir","nil",new TRegla("M","",68)),getEltoAccion("Reducir","menosu",new TRegla("M","",68)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("M","",68)),getEltoAccion("Desplazar","M",103)},{getEltoAccion("Reducir","{",new TRegla("Q6","",76)),getEltoAccion("Reducir","->",new TRegla("Q6","",76)),getEltoAccion("Reducir","(",new TRegla("Q6","",76)),
			getEltoAccion("Reducir","-",new TRegla("Q6","",76)),getEltoAccion("Reducir","menosu",new TRegla("Q6","",76)),getEltoAccion("Desplazar","Q6",104)},{getEltoAccion("Reducir","^",new TRegla("F","id",66)),getEltoAccion("Reducir",")",new TRegla("F","id",66)),getEltoAccion("Reducir","+",new TRegla("F","id",66)),
			getEltoAccion("Reducir","-",new TRegla("F","id",66)),getEltoAccion("Reducir","*",new TRegla("F","id",66)),getEltoAccion("Reducir","/",new TRegla("F","id",66))},{getEltoAccion("Reducir","^",new TRegla("F","num",64)),getEltoAccion("Reducir",")",new TRegla("F","num",64)),getEltoAccion("Reducir","+",new TRegla("F","num",64)),
			getEltoAccion("Reducir","-",new TRegla("F","num",64)),getEltoAccion("Reducir","*",new TRegla("F","num",64)),getEltoAccion("Reducir","/",new TRegla("F","num",64))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","compute",105),getEltoAccion("Desplazar","acceptline",106),
			getEltoAccion("Desplazar","E",107),getEltoAccion("Desplazar","T",83),getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","^",new TRegla("F","cadena",65)),getEltoAccion("Reducir",")",new TRegla("F","cadena",65)),getEltoAccion("Reducir","+",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","-",new TRegla("F","cadena",65)),getEltoAccion("Reducir","*",new TRegla("F","cadena",65)),getEltoAccion("Reducir","/",new TRegla("F","cadena",65))},{getEltoAccion("Reducir","^",new TRegla("F","variable",63)),getEltoAccion("Reducir",")",new TRegla("F","variable",63)),getEltoAccion("Reducir","+",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","-",new TRegla("F","variable",63)),getEltoAccion("Reducir","*",new TRegla("F","variable",63)),getEltoAccion("Reducir","/",new TRegla("F","variable",63))},{getEltoAccion("Reducir","^",new TRegla("F","nil",67)),getEltoAccion("Reducir",")",new TRegla("F","nil",67)),getEltoAccion("Reducir","+",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","-",new TRegla("F","nil",67)),getEltoAccion("Reducir","*",new TRegla("F","nil",67)),getEltoAccion("Reducir","/",new TRegla("F","nil",67))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",108),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",109)},{getEltoAccion("Reducir","^",new TRegla("Q3","",73)),getEltoAccion("Reducir",")",new TRegla("Q3","",73)),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111),
			getEltoAccion("Desplazar","Q3",112)},{getEltoAccion("Reducir","^",new TRegla("E","T",57)),getEltoAccion("Reducir",")",new TRegla("E","T",57)),getEltoAccion("Reducir","+",new TRegla("E","T",57)),
			getEltoAccion("Reducir","-",new TRegla("E","T",57)),getEltoAccion("Desplazar","*",113),getEltoAccion("Desplazar","/",114)},{getEltoAccion("Reducir","^",new TRegla("T","F",60)),getEltoAccion("Reducir",")",new TRegla("T","F",60)),getEltoAccion("Reducir","+",new TRegla("T","F",60)),
			getEltoAccion("Reducir","-",new TRegla("T","F",60)),getEltoAccion("Reducir","*",new TRegla("T","F",60)),getEltoAccion("Reducir","/",new TRegla("T","F",60))},{getEltoAccion("Desplazar","make",115)},{getEltoAccion("Reducir",")",new TRegla("emt","( make id Q1 pares_atrib_valor ) Q11 Remt",13))},{getEltoAccion("Desplazar",")",116)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",107),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","^",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Reducir",")",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Desplazar","Mponer_marca_lista",117)},{getEltoAccion("Reducir","^",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Reducir",")",new TRegla("Mponer_marca_lista","",82)),getEltoAccion("Desplazar","Mponer_marca_lista",118)},{getEltoAccion("Desplazar",")",119)},{getEltoAccion("Reducir","id",new TRegla("F","id",66)),getEltoAccion("Reducir","num",new TRegla("F","id",66)),getEltoAccion("Reducir","(",new TRegla("F","id",66)),
			getEltoAccion("Reducir","cadena",new TRegla("F","id",66)),getEltoAccion("Reducir","variable",new TRegla("F","id",66)),getEltoAccion("Reducir","nil",new TRegla("F","id",66)),
			getEltoAccion("Reducir","menosu",new TRegla("F","id",66))},{getEltoAccion("Reducir","id",new TRegla("F","num",64)),getEltoAccion("Reducir","num",new TRegla("F","num",64)),getEltoAccion("Reducir","(",new TRegla("F","num",64)),
			getEltoAccion("Reducir","cadena",new TRegla("F","num",64)),getEltoAccion("Reducir","variable",new TRegla("F","num",64)),getEltoAccion("Reducir","nil",new TRegla("F","num",64)),
			getEltoAccion("Reducir","menosu",new TRegla("F","num",64))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",120),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","id",new TRegla("F","cadena",65)),getEltoAccion("Reducir","num",new TRegla("F","cadena",65)),getEltoAccion("Reducir","(",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","cadena",new TRegla("F","cadena",65)),getEltoAccion("Reducir","variable",new TRegla("F","cadena",65)),getEltoAccion("Reducir","nil",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","menosu",new TRegla("F","cadena",65))},{getEltoAccion("Reducir","id",new TRegla("F","variable",63)),getEltoAccion("Reducir","num",new TRegla("F","variable",63)),getEltoAccion("Reducir","(",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","cadena",new TRegla("F","variable",63)),getEltoAccion("Reducir","variable",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","nil",new TRegla("F","variable",63)),getEltoAccion("Reducir","menosu",new TRegla("F","variable",63))},{getEltoAccion("Reducir","id",new TRegla("F","nil",67)),getEltoAccion("Reducir","num",new TRegla("F","nil",67)),getEltoAccion("Reducir","(",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","cadena",new TRegla("F","nil",67)),getEltoAccion("Reducir","variable",new TRegla("F","nil",67)),getEltoAccion("Reducir","nil",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","menosu",new TRegla("F","nil",67))},{getEltoAccion("Desplazar","id",92),getEltoAccion("Desplazar","num",93),getEltoAccion("Desplazar","(",94),getEltoAccion("Desplazar","cadena",95),getEltoAccion("Desplazar","variable",96),
			getEltoAccion("Desplazar","nil",97),getEltoAccion("Desplazar","menosu",98),getEltoAccion("Desplazar","F",121)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",122),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","(",123),getEltoAccion("Reducir",")",new TRegla("texto","",49)),getEltoAccion("Desplazar","variable",124),getEltoAccion("Desplazar","texto",125),
			getEltoAccion("Desplazar","elem_write",126)},{getEltoAccion("Reducir","(",new TRegla("acc","( halt )",44)),getEltoAccion("Reducir",")",new TRegla("acc","( halt )",44))},{getEltoAccion("Reducir",")",new TRegla("Racc","acc Racc",45))},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",127),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",128),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",129),getEltoAccion("Desplazar","elemento_condicion",130),getEltoAccion("Desplazar","Q9",131),getEltoAccion("Desplazar","conjuncion",132),
			getEltoAccion("Desplazar","disyuncion",133),getEltoAccion("Desplazar","pdcha_cond",134)},{getEltoAccion("Desplazar","{",135),getEltoAccion("Reducir","->",new TRegla("Rcond","",23)),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","-",25),
			getEltoAccion("Desplazar","menosu",26),getEltoAccion("Desplazar","Rcond",136),getEltoAccion("Desplazar","Q9",28),getEltoAccion("Desplazar","negar_cond",137)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",138),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",88),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",139)},{getEltoAccion("Desplazar",")",140),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",107),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","^",new TRegla("F","menosu F",61)),getEltoAccion("Reducir",")",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","+",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","-",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","*",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","/",new TRegla("F","menosu F",61))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",108),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","T",141),getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",108),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","T",142),getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","^",47),getEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),getEltoAccion("Desplazar","pares_atrib_valor",143)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",108),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",144)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",108),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",145)},{getEltoAccion("Desplazar","id",146)},{getEltoAccion("Desplazar","}",147)},{getEltoAccion("Desplazar","^",47),getEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),getEltoAccion("Desplazar","pares_atrib_valor",148)},{getEltoAccion("Desplazar","^",47),getEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),getEltoAccion("Desplazar","pares_atrib_valor",149)},{getEltoAccion("Reducir","(",new TRegla("acc","( remove F )",40)),getEltoAccion("Reducir",")",new TRegla("acc","( remove F )",40))},{getEltoAccion("Desplazar",")",150),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Reducir","id",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","num",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","(",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","cadena",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","variable",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","nil",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","menosu",new TRegla("F","menosu F",61))},{getEltoAccion("Desplazar",")",151),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar","rjust",152),getEltoAccion("Desplazar","tabto",153),getEltoAccion("Desplazar","crlf",154)},{getEltoAccion("Desplazar","(",123),getEltoAccion("Reducir",")",new TRegla("texto","",49)),getEltoAccion("Desplazar","variable",124),getEltoAccion("Desplazar","texto",155),
			getEltoAccion("Desplazar","elem_write",126)},{getEltoAccion("Desplazar",")",156)},{getEltoAccion("Desplazar","(",123),getEltoAccion("Reducir",")",new TRegla("texto","",49)),getEltoAccion("Desplazar","variable",124),getEltoAccion("Desplazar","texto",157),
			getEltoAccion("Desplazar","elem_write",126)},{getEltoAccion("Reducir","id",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","num",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","{",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","(",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","nil",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Desplazar","Mponer_marca_conj",158)},{getEltoAccion("Reducir","id",new TRegla("M","",68)),getEltoAccion("Reducir","num",new TRegla("M","",68)),getEltoAccion("Reducir","(",new TRegla("M","",68)),
			getEltoAccion("Reducir","cadena",new TRegla("M","",68)),getEltoAccion("Reducir","variable",new TRegla("M","",68)),getEltoAccion("Reducir","nil",new TRegla("M","",68)),
			getEltoAccion("Reducir","menosu",new TRegla("M","",68)),getEltoAccion("Desplazar","M",159)},{getEltoAccion("Reducir","id",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","num",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","{",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","(",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","nil",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Desplazar","Mponer_marca_dis",160)},{getEltoAccion("Desplazar","^",59),getEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),getEltoAccion("Desplazar","pares_atrib_cond",161)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",162),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","^",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir",")",new TRegla("elemento_condicion","conjuncion",31))},{getEltoAccion("Reducir","^",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir",")",new TRegla("elemento_condicion","disyuncion",32))},{getEltoAccion("Reducir","^",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir",")",new TRegla("elemento_condicion","pdcha_cond",30))},{getEltoAccion("Desplazar","variable",163)},{getEltoAccion("Reducir","->",new TRegla("cond","negar_cond ( id Mmarca pares_atrib_cond ) Q6 Rcond",20))},{getEltoAccion("Desplazar","(",164)},{getEltoAccion("Desplazar",")",165),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar",")",166)},{getEltoAccion("Reducir","^",new TRegla("F","( E )",62)),getEltoAccion("Reducir",")",new TRegla("F","( E )",62)),getEltoAccion("Reducir","+",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","-",new TRegla("F","( E )",62)),getEltoAccion("Reducir","*",new TRegla("F","( E )",62)),getEltoAccion("Reducir","/",new TRegla("F","( E )",62))},{getEltoAccion("Reducir","^",new TRegla("E","E + T",55)),getEltoAccion("Reducir",")",new TRegla("E","E + T",55)),getEltoAccion("Reducir","+",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","-",new TRegla("E","E + T",55)),getEltoAccion("Desplazar","*",113),getEltoAccion("Desplazar","/",114)},{getEltoAccion("Reducir","^",new TRegla("E","E - T",56)),getEltoAccion("Reducir",")",new TRegla("E","E - T",56)),getEltoAccion("Reducir","+",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","-",new TRegla("E","E - T",56)),getEltoAccion("Desplazar","*",113),getEltoAccion("Desplazar","/",114)},{getEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","^ id M E Q3 pares_atrib_valor",16))},{getEltoAccion("Reducir","^",new TRegla("T","T * F",58)),getEltoAccion("Reducir",")",new TRegla("T","T * F",58)),getEltoAccion("Reducir","+",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","-",new TRegla("T","T * F",58)),getEltoAccion("Reducir","*",new TRegla("T","T * F",58)),getEltoAccion("Reducir","/",new TRegla("T","T * F",58))},{getEltoAccion("Reducir","^",new TRegla("T","T / F",59)),getEltoAccion("Reducir",")",new TRegla("T","T / F",59)),getEltoAccion("Reducir","+",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","-",new TRegla("T","T / F",59)),getEltoAccion("Reducir","*",new TRegla("T","T / F",59)),getEltoAccion("Reducir","/",new TRegla("T","T / F",59))},{getEltoAccion("Reducir","^",new TRegla("Q1","",71)),getEltoAccion("Reducir",")",new TRegla("Q1","",71)),getEltoAccion("Desplazar","Q1",167)},{getEltoAccion("Reducir","{",new TRegla("Q5","",75)),getEltoAccion("Reducir","->",new TRegla("Q5","",75)),getEltoAccion("Reducir","(",new TRegla("Q5","",75)),
			getEltoAccion("Reducir","-",new TRegla("Q5","",75)),getEltoAccion("Reducir","menosu",new TRegla("Q5","",75)),getEltoAccion("Desplazar","Q5",168)},{getEltoAccion("Desplazar",")",169)},{getEltoAccion("Desplazar",")",170)},{getEltoAccion("Reducir","id",new TRegla("F","( E )",62)),getEltoAccion("Reducir","num",new TRegla("F","( E )",62)),getEltoAccion("Reducir","(",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","cadena",new TRegla("F","( E )",62)),getEltoAccion("Reducir","variable",new TRegla("F","( E )",62)),getEltoAccion("Reducir","nil",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","menosu",new TRegla("F","( E )",62))},{getEltoAccion("Reducir","(",new TRegla("acc","( bind F E )",41)),getEltoAccion("Reducir",")",new TRegla("acc","( bind F E )",41))},{getEltoAccion("Desplazar","num",171)},{getEltoAccion("Desplazar","num",172)},{getEltoAccion("Reducir",")",new TRegla("M","",68)),getEltoAccion("Desplazar","M",173)},{getEltoAccion("Reducir",")",new TRegla("texto","variable texto",48))},{getEltoAccion("Reducir","(",new TRegla("acc","( write Mponer_marca_lista texto )",43)),getEltoAccion("Reducir",")",new TRegla("acc","( write Mponer_marca_lista texto )",43))},{getEltoAccion("Reducir",")",new TRegla("texto","elem_write texto",47))},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",177),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),
			getEltoAccion("Desplazar","disyuncion",180),getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",182),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Desplazar","elemento_condicion",186),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","^ id M elemento_condicion pares_atrib_cond",27))},{getEltoAccion("Reducir","^",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir",")",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Desplazar","+",110),
			getEltoAccion("Desplazar","-",111)},{getEltoAccion("Reducir","(",new TRegla("M","",68)),getEltoAccion("Desplazar","M",191)},{getEltoAccion("Desplazar","id",192)},{getEltoAccion("Reducir","^",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir",")",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","+",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","-",new TRegla("E","( compute E )",53))},{getEltoAccion("Reducir","^",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir",")",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","+",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","-",new TRegla("E","( acceptline F )",54))},{getEltoAccion("Desplazar","^",47),getEltoAccion("Reducir",")",new TRegla("pares_atrib_valor","",17)),getEltoAccion("Desplazar","pares_atrib_valor",193)},{getEltoAccion("Desplazar","{",135),getEltoAccion("Reducir","->",new TRegla("Rcond","",23)),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","-",25),
			getEltoAccion("Desplazar","menosu",26),getEltoAccion("Desplazar","Rcond",194),getEltoAccion("Desplazar","Q9",28),getEltoAccion("Desplazar","negar_cond",137)},{getEltoAccion("Reducir","(",new TRegla("acc","( make F Mponer_marca_lista pares_atrib_valor )",38)),getEltoAccion("Reducir",")",new TRegla("acc","( make F Mponer_marca_lista pares_atrib_valor )",38))},{getEltoAccion("Reducir","(",new TRegla("acc","( modify F Mponer_marca_lista pares_atrib_valor )",39)),getEltoAccion("Reducir",")",new TRegla("acc","( modify F Mponer_marca_lista pares_atrib_valor )",39))},{getEltoAccion("Desplazar",")",195)},{getEltoAccion("Desplazar",")",196)},{getEltoAccion("Desplazar",")",197)},{getEltoAccion("Reducir","id",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","num",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","{",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","(",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","nil",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Desplazar","Mponer_marca_conj",198)},{getEltoAccion("Reducir","id",new TRegla("M","",68)),getEltoAccion("Reducir","num",new TRegla("M","",68)),getEltoAccion("Reducir","(",new TRegla("M","",68)),
			getEltoAccion("Reducir","cadena",new TRegla("M","",68)),getEltoAccion("Reducir","variable",new TRegla("M","",68)),getEltoAccion("Reducir","nil",new TRegla("M","",68)),
			getEltoAccion("Reducir","menosu",new TRegla("M","",68)),getEltoAccion("Desplazar","M",199)},{getEltoAccion("Reducir","id",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","num",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","{",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","(",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","nil",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Desplazar","Mponer_marca_dis",200)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),
			getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",201),
			getEltoAccion("Desplazar","Relemento_cond",202),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),getEltoAccion("Desplazar","disyuncion",180),
			getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",205),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","E",210),getEltoAccion("Desplazar","T",211),
			getEltoAccion("Desplazar","F",212)},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","{",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","}",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","(",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","conjuncion",31))},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","{",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","}",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","(",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","disyuncion",32))},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","{",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","}",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","(",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","pdcha_cond",30))},{getEltoAccion("Reducir","^",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir",")",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Desplazar","+",110),
			getEltoAccion("Desplazar","-",111)},{getEltoAccion("Reducir","id",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","num",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","{",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","(",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","nil",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_conj","",80)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_conj","",80)),getEltoAccion("Desplazar","Mponer_marca_conj",213)},{getEltoAccion("Reducir","id",new TRegla("M","",68)),getEltoAccion("Reducir","num",new TRegla("M","",68)),getEltoAccion("Reducir","(",new TRegla("M","",68)),
			getEltoAccion("Reducir","cadena",new TRegla("M","",68)),getEltoAccion("Reducir","variable",new TRegla("M","",68)),getEltoAccion("Reducir","nil",new TRegla("M","",68)),
			getEltoAccion("Reducir","menosu",new TRegla("M","",68)),getEltoAccion("Desplazar","M",214)},{getEltoAccion("Reducir","id",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","num",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","{",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","(",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","op_rel",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","cadena",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","variable",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","nil",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Reducir","menosu",new TRegla("Mponer_marca_dis","",81)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("Mponer_marca_dis","",81)),getEltoAccion("Desplazar","Mponer_marca_dis",215)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),getEltoAccion("Desplazar","elemento_condicion",216),
			getEltoAccion("Desplazar","Relemento_cond",217),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",220),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","E",225),getEltoAccion("Desplazar","T",226),
			getEltoAccion("Desplazar","F",227)},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","{",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","(",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","conjuncion",31)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","conjuncion",31)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","conjuncion",31))},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","{",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","(",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","disyuncion",32)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","disyuncion",32)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","disyuncion",32))},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","{",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","(",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","pdcha_cond",30)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","pdcha_cond",30)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","pdcha_cond",30))},{getEltoAccion("Desplazar","(",228)},{getEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),getEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),getEltoAccion("Desplazar","Mmarca",229)},{getEltoAccion("Desplazar",")",230)},{getEltoAccion("Reducir","->",new TRegla("cond","{ variable M ( id Mmarca pares_atrib_cond ) } Q5 Rcond",19))},{getEltoAccion("Reducir","(",new TRegla("elem_write","( rjust num )",50)),getEltoAccion("Reducir",")",new TRegla("elem_write","( rjust num )",50)),getEltoAccion("Reducir","variable",new TRegla("elem_write","( rjust num )",50))},{getEltoAccion("Reducir","(",new TRegla("elem_write","( tabto num )",51)),getEltoAccion("Reducir",")",new TRegla("elem_write","( tabto num )",51)),getEltoAccion("Reducir","variable",new TRegla("elem_write","( tabto num )",51))},{getEltoAccion("Reducir","(",new TRegla("elem_write","( crlf M )",52)),getEltoAccion("Reducir",")",new TRegla("elem_write","( crlf M )",52)),getEltoAccion("Reducir","variable",new TRegla("elem_write","( crlf M )",52))},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",231),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),
			getEltoAccion("Desplazar","disyuncion",180),getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",205),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","E",232),getEltoAccion("Desplazar","T",211),
			getEltoAccion("Desplazar","F",212)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Desplazar","elemento_condicion",233),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),
			getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",201),
			getEltoAccion("Desplazar","Relemento_cond",234),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),getEltoAccion("Desplazar","disyuncion",180),
			getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Desplazar","}",235)},{getEltoAccion("Reducir","id",new TRegla("F","id",66)),getEltoAccion("Reducir","num",new TRegla("F","id",66)),getEltoAccion("Reducir","{",new TRegla("F","id",66)),
			getEltoAccion("Reducir","}",new TRegla("F","id",66)),getEltoAccion("Reducir","(",new TRegla("F","id",66)),getEltoAccion("Reducir","+",new TRegla("F","id",66)),
			getEltoAccion("Reducir","-",new TRegla("F","id",66)),getEltoAccion("Reducir","*",new TRegla("F","id",66)),getEltoAccion("Reducir","/",new TRegla("F","id",66)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","id",66)),getEltoAccion("Reducir","cadena",new TRegla("F","id",66)),getEltoAccion("Reducir","variable",new TRegla("F","id",66)),
			getEltoAccion("Reducir","nil",new TRegla("F","id",66)),getEltoAccion("Reducir","menosu",new TRegla("F","id",66)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","id",66))},{getEltoAccion("Reducir","id",new TRegla("F","num",64)),getEltoAccion("Reducir","num",new TRegla("F","num",64)),getEltoAccion("Reducir","{",new TRegla("F","num",64)),
			getEltoAccion("Reducir","}",new TRegla("F","num",64)),getEltoAccion("Reducir","(",new TRegla("F","num",64)),getEltoAccion("Reducir","+",new TRegla("F","num",64)),
			getEltoAccion("Reducir","-",new TRegla("F","num",64)),getEltoAccion("Reducir","*",new TRegla("F","num",64)),getEltoAccion("Reducir","/",new TRegla("F","num",64)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","num",64)),getEltoAccion("Reducir","cadena",new TRegla("F","num",64)),getEltoAccion("Reducir","variable",new TRegla("F","num",64)),
			getEltoAccion("Reducir","nil",new TRegla("F","num",64)),getEltoAccion("Reducir","menosu",new TRegla("F","num",64)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","num",64))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","compute",236),getEltoAccion("Desplazar","acceptline",237),
			getEltoAccion("Desplazar","E",238),getEltoAccion("Desplazar","T",83),getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","id",new TRegla("F","cadena",65)),getEltoAccion("Reducir","num",new TRegla("F","cadena",65)),getEltoAccion("Reducir","{",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","}",new TRegla("F","cadena",65)),getEltoAccion("Reducir","(",new TRegla("F","cadena",65)),getEltoAccion("Reducir","+",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","-",new TRegla("F","cadena",65)),getEltoAccion("Reducir","*",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","/",new TRegla("F","cadena",65)),getEltoAccion("Reducir","op_rel",new TRegla("F","cadena",65)),getEltoAccion("Reducir","cadena",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","variable",new TRegla("F","cadena",65)),getEltoAccion("Reducir","nil",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","menosu",new TRegla("F","cadena",65)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","cadena",65))},{getEltoAccion("Reducir","id",new TRegla("F","variable",63)),getEltoAccion("Reducir","num",new TRegla("F","variable",63)),getEltoAccion("Reducir","{",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","}",new TRegla("F","variable",63)),getEltoAccion("Reducir","(",new TRegla("F","variable",63)),getEltoAccion("Reducir","+",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","-",new TRegla("F","variable",63)),getEltoAccion("Reducir","*",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","/",new TRegla("F","variable",63)),getEltoAccion("Reducir","op_rel",new TRegla("F","variable",63)),getEltoAccion("Reducir","cadena",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","variable",new TRegla("F","variable",63)),getEltoAccion("Reducir","nil",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","menosu",new TRegla("F","variable",63)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","variable",63))},{getEltoAccion("Reducir","id",new TRegla("F","nil",67)),getEltoAccion("Reducir","num",new TRegla("F","nil",67)),getEltoAccion("Reducir","{",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","}",new TRegla("F","nil",67)),getEltoAccion("Reducir","(",new TRegla("F","nil",67)),getEltoAccion("Reducir","+",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","-",new TRegla("F","nil",67)),getEltoAccion("Reducir","*",new TRegla("F","nil",67)),getEltoAccion("Reducir","/",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","nil",67)),getEltoAccion("Reducir","cadena",new TRegla("F","nil",67)),getEltoAccion("Reducir","variable",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","nil",new TRegla("F","nil",67)),getEltoAccion("Reducir","menosu",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","nil",67))},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",239),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","F",240)},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","{",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","}",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","(",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Desplazar","+",241),getEltoAccion("Desplazar","-",242),getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","Q9 E",29))},{getEltoAccion("Reducir","id",new TRegla("E","T",57)),getEltoAccion("Reducir","num",new TRegla("E","T",57)),getEltoAccion("Reducir","{",new TRegla("E","T",57)),
			getEltoAccion("Reducir","}",new TRegla("E","T",57)),getEltoAccion("Reducir","(",new TRegla("E","T",57)),getEltoAccion("Reducir","+",new TRegla("E","T",57)),
			getEltoAccion("Reducir","-",new TRegla("E","T",57)),getEltoAccion("Desplazar","*",243),getEltoAccion("Desplazar","/",244),getEltoAccion("Reducir","op_rel",new TRegla("E","T",57)),
			getEltoAccion("Reducir","cadena",new TRegla("E","T",57)),getEltoAccion("Reducir","variable",new TRegla("E","T",57)),
			getEltoAccion("Reducir","nil",new TRegla("E","T",57)),getEltoAccion("Reducir","menosu",new TRegla("E","T",57)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","T",57))},{getEltoAccion("Reducir","id",new TRegla("T","F",60)),getEltoAccion("Reducir","num",new TRegla("T","F",60)),getEltoAccion("Reducir","{",new TRegla("T","F",60)),
			getEltoAccion("Reducir","}",new TRegla("T","F",60)),getEltoAccion("Reducir","(",new TRegla("T","F",60)),getEltoAccion("Reducir","+",new TRegla("T","F",60)),
			getEltoAccion("Reducir","-",new TRegla("T","F",60)),getEltoAccion("Reducir","*",new TRegla("T","F",60)),getEltoAccion("Reducir","/",new TRegla("T","F",60)),
			getEltoAccion("Reducir","op_rel",new TRegla("T","F",60)),getEltoAccion("Reducir","cadena",new TRegla("T","F",60)),getEltoAccion("Reducir","variable",new TRegla("T","F",60)),
			getEltoAccion("Reducir","nil",new TRegla("T","F",60)),getEltoAccion("Reducir","menosu",new TRegla("T","F",60)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","F",60))},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",245),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),
			getEltoAccion("Desplazar","disyuncion",180),getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",220),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","E",246),getEltoAccion("Desplazar","T",226),
			getEltoAccion("Desplazar","F",227)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Desplazar","elemento_condicion",247),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),getEltoAccion("Desplazar","elemento_condicion",216),
			getEltoAccion("Desplazar","Relemento_cond",248),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Desplazar","doble_angulo_ce",249)},{getEltoAccion("Reducir","id",new TRegla("F","id",66)),getEltoAccion("Reducir","num",new TRegla("F","id",66)),getEltoAccion("Reducir","{",new TRegla("F","id",66)),
			getEltoAccion("Reducir","(",new TRegla("F","id",66)),getEltoAccion("Reducir","+",new TRegla("F","id",66)),getEltoAccion("Reducir","-",new TRegla("F","id",66)),
			getEltoAccion("Reducir","*",new TRegla("F","id",66)),getEltoAccion("Reducir","/",new TRegla("F","id",66)),getEltoAccion("Reducir","op_rel",new TRegla("F","id",66)),
			getEltoAccion("Reducir","cadena",new TRegla("F","id",66)),getEltoAccion("Reducir","variable",new TRegla("F","id",66)),getEltoAccion("Reducir","nil",new TRegla("F","id",66)),
			getEltoAccion("Reducir","menosu",new TRegla("F","id",66)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","id",66)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","id",66))},{getEltoAccion("Reducir","id",new TRegla("F","num",64)),getEltoAccion("Reducir","num",new TRegla("F","num",64)),getEltoAccion("Reducir","{",new TRegla("F","num",64)),
			getEltoAccion("Reducir","(",new TRegla("F","num",64)),getEltoAccion("Reducir","+",new TRegla("F","num",64)),getEltoAccion("Reducir","-",new TRegla("F","num",64)),
			getEltoAccion("Reducir","*",new TRegla("F","num",64)),getEltoAccion("Reducir","/",new TRegla("F","num",64)),getEltoAccion("Reducir","op_rel",new TRegla("F","num",64)),
			getEltoAccion("Reducir","cadena",new TRegla("F","num",64)),getEltoAccion("Reducir","variable",new TRegla("F","num",64)),
			getEltoAccion("Reducir","nil",new TRegla("F","num",64)),getEltoAccion("Reducir","menosu",new TRegla("F","num",64)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","num",64)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","num",64))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","compute",250),getEltoAccion("Desplazar","acceptline",251),
			getEltoAccion("Desplazar","E",252),getEltoAccion("Desplazar","T",83),getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","id",new TRegla("F","cadena",65)),getEltoAccion("Reducir","num",new TRegla("F","cadena",65)),getEltoAccion("Reducir","{",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","(",new TRegla("F","cadena",65)),getEltoAccion("Reducir","+",new TRegla("F","cadena",65)),getEltoAccion("Reducir","-",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","*",new TRegla("F","cadena",65)),getEltoAccion("Reducir","/",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","cadena",65)),getEltoAccion("Reducir","cadena",new TRegla("F","cadena",65)),getEltoAccion("Reducir","variable",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","nil",new TRegla("F","cadena",65)),getEltoAccion("Reducir","menosu",new TRegla("F","cadena",65)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","cadena",65)),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","cadena",65))},{getEltoAccion("Reducir","id",new TRegla("F","variable",63)),getEltoAccion("Reducir","num",new TRegla("F","variable",63)),getEltoAccion("Reducir","{",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","(",new TRegla("F","variable",63)),getEltoAccion("Reducir","+",new TRegla("F","variable",63)),getEltoAccion("Reducir","-",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","*",new TRegla("F","variable",63)),getEltoAccion("Reducir","/",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","variable",63)),getEltoAccion("Reducir","cadena",new TRegla("F","variable",63)),getEltoAccion("Reducir","variable",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","nil",new TRegla("F","variable",63)),getEltoAccion("Reducir","menosu",new TRegla("F","variable",63)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","variable",63)),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","variable",63))},{getEltoAccion("Reducir","id",new TRegla("F","nil",67)),getEltoAccion("Reducir","num",new TRegla("F","nil",67)),getEltoAccion("Reducir","{",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","(",new TRegla("F","nil",67)),getEltoAccion("Reducir","+",new TRegla("F","nil",67)),getEltoAccion("Reducir","-",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","*",new TRegla("F","nil",67)),getEltoAccion("Reducir","/",new TRegla("F","nil",67)),getEltoAccion("Reducir","op_rel",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","cadena",new TRegla("F","nil",67)),getEltoAccion("Reducir","variable",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","nil",new TRegla("F","nil",67)),getEltoAccion("Reducir","menosu",new TRegla("F","nil",67)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","nil",67)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","nil",67))},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",253),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","F",254)},{getEltoAccion("Reducir","id",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","num",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","{",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","(",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Desplazar","+",255),
			getEltoAccion("Desplazar","-",256),getEltoAccion("Reducir","op_rel",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","cadena",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","variable",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","nil",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","menosu",new TRegla("elemento_condicion","Q9 E",29)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("elemento_condicion","Q9 E",29)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("elemento_condicion","Q9 E",29))},{getEltoAccion("Reducir","id",new TRegla("E","T",57)),getEltoAccion("Reducir","num",new TRegla("E","T",57)),getEltoAccion("Reducir","{",new TRegla("E","T",57)),
			getEltoAccion("Reducir","(",new TRegla("E","T",57)),getEltoAccion("Reducir","+",new TRegla("E","T",57)),getEltoAccion("Reducir","-",new TRegla("E","T",57)),
			getEltoAccion("Desplazar","*",257),getEltoAccion("Desplazar","/",258),getEltoAccion("Reducir","op_rel",new TRegla("E","T",57)),getEltoAccion("Reducir","cadena",new TRegla("E","T",57)),
			getEltoAccion("Reducir","variable",new TRegla("E","T",57)),getEltoAccion("Reducir","nil",new TRegla("E","T",57)),
			getEltoAccion("Reducir","menosu",new TRegla("E","T",57)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","T",57)),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","T",57))},{getEltoAccion("Reducir","id",new TRegla("T","F",60)),getEltoAccion("Reducir","num",new TRegla("T","F",60)),getEltoAccion("Reducir","{",new TRegla("T","F",60)),
			getEltoAccion("Reducir","(",new TRegla("T","F",60)),getEltoAccion("Reducir","+",new TRegla("T","F",60)),getEltoAccion("Reducir","-",new TRegla("T","F",60)),
			getEltoAccion("Reducir","*",new TRegla("T","F",60)),getEltoAccion("Reducir","/",new TRegla("T","F",60)),getEltoAccion("Reducir","op_rel",new TRegla("T","F",60)),
			getEltoAccion("Reducir","cadena",new TRegla("T","F",60)),getEltoAccion("Reducir","variable",new TRegla("T","F",60)),getEltoAccion("Reducir","nil",new TRegla("T","F",60)),
			getEltoAccion("Reducir","menosu",new TRegla("T","F",60)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","F",60)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("T","F",60))},{getEltoAccion("Desplazar","id",259)},{getEltoAccion("Desplazar","^",59),getEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),getEltoAccion("Desplazar","pares_atrib_cond",260)},{getEltoAccion("Reducir","(",new TRegla("Q11","",79)),getEltoAccion("Reducir",")",new TRegla("Q11","",79)),getEltoAccion("Desplazar","Q11",261)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),
			getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",201),
			getEltoAccion("Desplazar","Relemento_cond",262),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),getEltoAccion("Desplazar","disyuncion",180),
			getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Reducir","id",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","num",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","{",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","}",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","(",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Desplazar","+",241),getEltoAccion("Desplazar","-",242),getEltoAccion("Reducir","op_rel",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","cadena",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","variable",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","nil",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","menosu",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("pdcha_cond","op_rel M E",33))},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),getEltoAccion("Desplazar","elemento_condicion",216),
			getEltoAccion("Desplazar","Relemento_cond",263),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Reducir","}",new TRegla("Relemento_cond","elemento_condicion Relemento_cond",36))},{getEltoAccion("Reducir","^",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir",")",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",264),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",88),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",265)},{getEltoAccion("Desplazar",")",266),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",238),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","id",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","num",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","{",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","}",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","(",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","+",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","-",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","*",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","/",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","op_rel",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","cadena",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","variable",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","nil",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","menosu",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","menosu F",61))},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",239),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","T",267),getEltoAccion("Desplazar","F",212)},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",239),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","T",268),getEltoAccion("Desplazar","F",212)},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",239),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","F",269)},{getEltoAccion("Desplazar","id",203),getEltoAccion("Desplazar","num",204),getEltoAccion("Desplazar","(",239),getEltoAccion("Desplazar","cadena",206),getEltoAccion("Desplazar","variable",207),
			getEltoAccion("Desplazar","nil",208),getEltoAccion("Desplazar","menosu",209),getEltoAccion("Desplazar","F",270)},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",174),getEltoAccion("Reducir","}",new TRegla("Relemento_cond","",37)),
			getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","op_rel",175),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),getEltoAccion("Desplazar","doble_angulo_ab",176),getEltoAccion("Desplazar","elemento_condicion",201),
			getEltoAccion("Desplazar","Relemento_cond",271),getEltoAccion("Desplazar","Q9",178),getEltoAccion("Desplazar","conjuncion",179),getEltoAccion("Desplazar","disyuncion",180),
			getEltoAccion("Desplazar","pdcha_cond",181)},{getEltoAccion("Reducir","id",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","num",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","{",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","(",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Desplazar","+",255),
			getEltoAccion("Desplazar","-",256),getEltoAccion("Reducir","op_rel",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","cadena",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","variable",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","nil",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","menosu",new TRegla("pdcha_cond","op_rel M E",33)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("pdcha_cond","op_rel M E",33)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("pdcha_cond","op_rel M E",33))},{getEltoAccion("Reducir","id",new TRegla("Q9","",78)),getEltoAccion("Reducir","num",new TRegla("Q9","",78)),getEltoAccion("Desplazar","{",183),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","op_rel",184),getEltoAccion("Reducir","cadena",new TRegla("Q9","",78)),getEltoAccion("Reducir","variable",new TRegla("Q9","",78)),
			getEltoAccion("Reducir","nil",new TRegla("Q9","",78)),getEltoAccion("Reducir","menosu",new TRegla("Q9","",78)),
			getEltoAccion("Desplazar","doble_angulo_ab",185),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","",37)),getEltoAccion("Desplazar","elemento_condicion",216),
			getEltoAccion("Desplazar","Relemento_cond",272),getEltoAccion("Desplazar","Q9",187),getEltoAccion("Desplazar","conjuncion",188),
			getEltoAccion("Desplazar","disyuncion",189),getEltoAccion("Desplazar","pdcha_cond",190)},{getEltoAccion("Reducir","doble_angulo_ce",new TRegla("Relemento_cond","elemento_condicion Relemento_cond",36))},{getEltoAccion("Reducir","^",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),getEltoAccion("Reducir",")",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35))},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",273),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",88),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","F",274)},{getEltoAccion("Desplazar",")",275),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar","id",75),getEltoAccion("Desplazar","num",76),getEltoAccion("Desplazar","(",77),getEltoAccion("Desplazar","cadena",78),getEltoAccion("Desplazar","variable",79),
			getEltoAccion("Desplazar","nil",80),getEltoAccion("Desplazar","menosu",81),getEltoAccion("Desplazar","E",252),getEltoAccion("Desplazar","T",83),
			getEltoAccion("Desplazar","F",84)},{getEltoAccion("Reducir","id",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","num",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","{",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","(",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","+",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","-",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","*",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","/",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","cadena",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","variable",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","nil",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","menosu",new TRegla("F","menosu F",61)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","menosu F",61)),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","menosu F",61))},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",253),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","T",276),getEltoAccion("Desplazar","F",227)},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",253),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","T",277),getEltoAccion("Desplazar","F",227)},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",253),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","F",278)},{getEltoAccion("Desplazar","id",218),getEltoAccion("Desplazar","num",219),getEltoAccion("Desplazar","(",253),getEltoAccion("Desplazar","cadena",221),getEltoAccion("Desplazar","variable",222),
			getEltoAccion("Desplazar","nil",223),getEltoAccion("Desplazar","menosu",224),getEltoAccion("Desplazar","F",279)},{getEltoAccion("Reducir","^",new TRegla("Mmarca","",69)),getEltoAccion("Reducir",")",new TRegla("Mmarca","",69)),getEltoAccion("Desplazar","Mmarca",280)},{getEltoAccion("Desplazar",")",281)},{getEltoAccion("Desplazar","(",85),getEltoAccion("Reducir",")",new TRegla("Remt","",15)),getEltoAccion("Desplazar","Remt",282)},{getEltoAccion("Desplazar","}",283)},{getEltoAccion("Desplazar","doble_angulo_ce",284)},{getEltoAccion("Desplazar",")",285),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar",")",286)},{getEltoAccion("Reducir","id",new TRegla("F","( E )",62)),getEltoAccion("Reducir","num",new TRegla("F","( E )",62)),getEltoAccion("Reducir","{",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","}",new TRegla("F","( E )",62)),getEltoAccion("Reducir","(",new TRegla("F","( E )",62)),getEltoAccion("Reducir","+",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","-",new TRegla("F","( E )",62)),getEltoAccion("Reducir","*",new TRegla("F","( E )",62)),getEltoAccion("Reducir","/",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","op_rel",new TRegla("F","( E )",62)),getEltoAccion("Reducir","cadena",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","variable",new TRegla("F","( E )",62)),getEltoAccion("Reducir","nil",new TRegla("F","( E )",62)),getEltoAccion("Reducir","menosu",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","( E )",62))},{getEltoAccion("Reducir","id",new TRegla("E","E + T",55)),getEltoAccion("Reducir","num",new TRegla("E","E + T",55)),getEltoAccion("Reducir","{",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","}",new TRegla("E","E + T",55)),getEltoAccion("Reducir","(",new TRegla("E","E + T",55)),getEltoAccion("Reducir","+",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","-",new TRegla("E","E + T",55)),getEltoAccion("Desplazar","*",243),getEltoAccion("Desplazar","/",244),
			getEltoAccion("Reducir","op_rel",new TRegla("E","E + T",55)),getEltoAccion("Reducir","cadena",new TRegla("E","E + T",55)),getEltoAccion("Reducir","variable",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","nil",new TRegla("E","E + T",55)),getEltoAccion("Reducir","menosu",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E + T",55))},{getEltoAccion("Reducir","id",new TRegla("E","E - T",56)),getEltoAccion("Reducir","num",new TRegla("E","E - T",56)),getEltoAccion("Reducir","{",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","}",new TRegla("E","E - T",56)),getEltoAccion("Reducir","(",new TRegla("E","E - T",56)),getEltoAccion("Reducir","+",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","-",new TRegla("E","E - T",56)),getEltoAccion("Desplazar","*",243),getEltoAccion("Desplazar","/",244),
			getEltoAccion("Reducir","op_rel",new TRegla("E","E - T",56)),getEltoAccion("Reducir","cadena",new TRegla("E","E - T",56)),getEltoAccion("Reducir","variable",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","nil",new TRegla("E","E - T",56)),getEltoAccion("Reducir","menosu",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E - T",56))},{getEltoAccion("Reducir","id",new TRegla("T","T * F",58)),getEltoAccion("Reducir","num",new TRegla("T","T * F",58)),getEltoAccion("Reducir","{",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","}",new TRegla("T","T * F",58)),getEltoAccion("Reducir","(",new TRegla("T","T * F",58)),getEltoAccion("Reducir","+",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","-",new TRegla("T","T * F",58)),getEltoAccion("Reducir","*",new TRegla("T","T * F",58)),getEltoAccion("Reducir","/",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","op_rel",new TRegla("T","T * F",58)),getEltoAccion("Reducir","cadena",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","variable",new TRegla("T","T * F",58)),getEltoAccion("Reducir","nil",new TRegla("T","T * F",58)),getEltoAccion("Reducir","menosu",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T * F",58))},{getEltoAccion("Reducir","id",new TRegla("T","T / F",59)),getEltoAccion("Reducir","num",new TRegla("T","T / F",59)),getEltoAccion("Reducir","{",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","}",new TRegla("T","T / F",59)),getEltoAccion("Reducir","(",new TRegla("T","T / F",59)),getEltoAccion("Reducir","+",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","-",new TRegla("T","T / F",59)),getEltoAccion("Reducir","*",new TRegla("T","T / F",59)),getEltoAccion("Reducir","/",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","op_rel",new TRegla("T","T / F",59)),getEltoAccion("Reducir","cadena",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","variable",new TRegla("T","T / F",59)),getEltoAccion("Reducir","nil",new TRegla("T","T / F",59)),getEltoAccion("Reducir","menosu",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T / F",59))},{getEltoAccion("Desplazar","}",287)},{getEltoAccion("Desplazar","doble_angulo_ce",288)},{getEltoAccion("Desplazar",")",289),getEltoAccion("Desplazar","+",110),getEltoAccion("Desplazar","-",111)},{getEltoAccion("Desplazar",")",290)},{getEltoAccion("Reducir","id",new TRegla("F","( E )",62)),getEltoAccion("Reducir","num",new TRegla("F","( E )",62)),getEltoAccion("Reducir","{",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","(",new TRegla("F","( E )",62)),getEltoAccion("Reducir","+",new TRegla("F","( E )",62)),getEltoAccion("Reducir","-",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","*",new TRegla("F","( E )",62)),getEltoAccion("Reducir","/",new TRegla("F","( E )",62)),getEltoAccion("Reducir","op_rel",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","cadena",new TRegla("F","( E )",62)),getEltoAccion("Reducir","variable",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","nil",new TRegla("F","( E )",62)),getEltoAccion("Reducir","menosu",new TRegla("F","( E )",62)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("F","( E )",62)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("F","( E )",62))},{getEltoAccion("Reducir","id",new TRegla("E","E + T",55)),getEltoAccion("Reducir","num",new TRegla("E","E + T",55)),getEltoAccion("Reducir","{",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","(",new TRegla("E","E + T",55)),getEltoAccion("Reducir","+",new TRegla("E","E + T",55)),getEltoAccion("Reducir","-",new TRegla("E","E + T",55)),
			getEltoAccion("Desplazar","*",257),getEltoAccion("Desplazar","/",258),getEltoAccion("Reducir","op_rel",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","cadena",new TRegla("E","E + T",55)),getEltoAccion("Reducir","variable",new TRegla("E","E + T",55)),getEltoAccion("Reducir","nil",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","menosu",new TRegla("E","E + T",55)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E + T",55)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","E + T",55))},{getEltoAccion("Reducir","id",new TRegla("E","E - T",56)),getEltoAccion("Reducir","num",new TRegla("E","E - T",56)),getEltoAccion("Reducir","{",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","(",new TRegla("E","E - T",56)),getEltoAccion("Reducir","+",new TRegla("E","E - T",56)),getEltoAccion("Reducir","-",new TRegla("E","E - T",56)),
			getEltoAccion("Desplazar","*",257),getEltoAccion("Desplazar","/",258),getEltoAccion("Reducir","op_rel",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","cadena",new TRegla("E","E - T",56)),getEltoAccion("Reducir","variable",new TRegla("E","E - T",56)),getEltoAccion("Reducir","nil",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","menosu",new TRegla("E","E - T",56)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","E - T",56)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","E - T",56))},{getEltoAccion("Reducir","id",new TRegla("T","T * F",58)),getEltoAccion("Reducir","num",new TRegla("T","T * F",58)),getEltoAccion("Reducir","{",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","(",new TRegla("T","T * F",58)),getEltoAccion("Reducir","+",new TRegla("T","T * F",58)),getEltoAccion("Reducir","-",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","*",new TRegla("T","T * F",58)),getEltoAccion("Reducir","/",new TRegla("T","T * F",58)),getEltoAccion("Reducir","op_rel",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","cadena",new TRegla("T","T * F",58)),getEltoAccion("Reducir","variable",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","nil",new TRegla("T","T * F",58)),getEltoAccion("Reducir","menosu",new TRegla("T","T * F",58)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T * F",58)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("T","T * F",58))},{getEltoAccion("Reducir","id",new TRegla("T","T / F",59)),getEltoAccion("Reducir","num",new TRegla("T","T / F",59)),getEltoAccion("Reducir","{",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","(",new TRegla("T","T / F",59)),getEltoAccion("Reducir","+",new TRegla("T","T / F",59)),getEltoAccion("Reducir","-",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","*",new TRegla("T","T / F",59)),getEltoAccion("Reducir","/",new TRegla("T","T / F",59)),getEltoAccion("Reducir","op_rel",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","cadena",new TRegla("T","T / F",59)),getEltoAccion("Reducir","variable",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","nil",new TRegla("T","T / F",59)),getEltoAccion("Reducir","menosu",new TRegla("T","T / F",59)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("T","T / F",59)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("T","T / F",59))},{getEltoAccion("Desplazar","^",59),getEltoAccion("Reducir",")",new TRegla("pares_atrib_cond","",28)),getEltoAccion("Desplazar","pares_atrib_cond",291)},{getEltoAccion("Reducir","{",new TRegla("Q6","",76)),getEltoAccion("Reducir","->",new TRegla("Q6","",76)),getEltoAccion("Reducir","(",new TRegla("Q6","",76)),
			getEltoAccion("Reducir","-",new TRegla("Q6","",76)),getEltoAccion("Reducir","menosu",new TRegla("Q6","",76)),getEltoAccion("Desplazar","Q6",292)},{getEltoAccion("Reducir",")",new TRegla("Remt","( make id Q1 pares_atrib_valor ) Q11 Remt",14))},{getEltoAccion("Reducir","id",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir","num",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","{",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","}",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","(",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir","op_rel",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","cadena",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","variable",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","nil",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir","menosu",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34))},{getEltoAccion("Reducir","id",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),getEltoAccion("Reducir","num",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","{",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","}",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","(",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","op_rel",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","cadena",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","variable",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","nil",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","menosu",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35))},{getEltoAccion("Reducir","id",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","num",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","{",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","}",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","(",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","+",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","-",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","op_rel",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","cadena",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","variable",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","nil",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","menosu",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( compute E )",53))},{getEltoAccion("Reducir","id",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","num",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","{",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","}",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","(",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","+",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","-",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","op_rel",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","cadena",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","variable",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","nil",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","menosu",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( acceptline F )",54))},{getEltoAccion("Reducir","id",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir","num",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","{",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","(",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","op_rel",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir","cadena",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","variable",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","nil",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","menosu",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34)),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("conjuncion","{ Mponer_marca_conj elemento_condicion Relemento_cond }",34))},{getEltoAccion("Reducir","id",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),getEltoAccion("Reducir","num",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","{",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","(",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","op_rel",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","cadena",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","variable",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","nil",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","menosu",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("disyuncion","doble_angulo_ab Mponer_marca_dis elemento_condicion Relemento_cond doble_angulo_ce",35))},{getEltoAccion("Reducir","id",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","num",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","{",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","(",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","+",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","-",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","op_rel",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","cadena",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","variable",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","nil",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","menosu",new TRegla("E","( compute E )",53)),getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( compute E )",53)),
			getEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","( compute E )",53))},{getEltoAccion("Reducir","id",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","num",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","{",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","(",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","+",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","-",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","op_rel",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","cadena",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","variable",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","nil",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","menosu",new TRegla("E","( acceptline F )",54)),
			getEltoAccion("Reducir","doble_angulo_ab",new TRegla("E","( acceptline F )",54)),getEltoAccion("Reducir","doble_angulo_ce",new TRegla("E","( acceptline F )",54))},{getEltoAccion("Desplazar",")",293)},{getEltoAccion("Desplazar","{",135),getEltoAccion("Reducir","->",new TRegla("Rcond","",23)),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","-",25),
			getEltoAccion("Desplazar","menosu",26),getEltoAccion("Desplazar","Rcond",294),getEltoAccion("Desplazar","Q9",28),getEltoAccion("Desplazar","negar_cond",137)},{getEltoAccion("Desplazar","}",295)},{getEltoAccion("Reducir","->",new TRegla("Rcond","negar_cond ( id Mmarca pares_atrib_cond ) Q6 Rcond",22))},{getEltoAccion("Reducir","{",new TRegla("Q5","",75)),getEltoAccion("Reducir","->",new TRegla("Q5","",75)),getEltoAccion("Reducir","(",new TRegla("Q5","",75)),
			getEltoAccion("Reducir","-",new TRegla("Q5","",75)),getEltoAccion("Reducir","menosu",new TRegla("Q5","",75)),getEltoAccion("Desplazar","Q5",296)},{getEltoAccion("Desplazar","{",135),getEltoAccion("Reducir","->",new TRegla("Rcond","",23)),getEltoAccion("Reducir","(",new TRegla("Q9","",78)),getEltoAccion("Desplazar","-",25),
			getEltoAccion("Desplazar","menosu",26),getEltoAccion("Desplazar","Rcond",297),getEltoAccion("Desplazar","Q9",28),getEltoAccion("Desplazar","negar_cond",137)},{getEltoAccion("Reducir","->",new TRegla("Rcond","{ variable M ( id Mmarca pares_atrib_cond ) } Q5 Rcond",21))}};
		tablaaccion = new Vector<TVectorAccion>();
		for(int z = 0;z<bindVars.length;z++){
			acc = new TVectorAccion();
			for(int k=0;k<bindVars[z].length;k++)
				acc.addElement(bindVars[z][k]);
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
