import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

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
    TRegla(String pizda, String[] result, int n) {
        num = n;
        lhs = new String(pizda);
        rhs = new Vector<String>();
	    for (int x = 0; x < result.length; x++)
			rhs.addElement(new String(result[x]).trim());
    }
    TRegla() {
        num = -1;
        lhs = new String();
        rhs = new Vector<String>();
    }
}
class TElemento {
    String lhs;
    Vector<String> rhs;
    int pospunto;
    Vector<String> simant;

    TElemento(String pizda, Vector<String> pdcha, int posicion, Vector<String> santicipacion) {
        lhs = new String(pizda);
        rhs = new Vector<String>(pdcha);
        pospunto = posicion;
        simant = new Vector<String>(santicipacion);
    }

    TElemento() {
    }
    /***************************************
	* devuelve el simbolo gramatical a continuacion del punto en el elemento
	****************************************/
    String getsimdespuespunto() {
        int indmax = rhs.size();
        String sim;
        if (pospunto >= indmax)
            sim = new String("NULL");
        else
            sim = rhs.elementAt(pospunto);
        return sim;
    }
    /**********************************************
	* comparacion con otro elemento de la misma clase
	***********************************************/
    boolean igual(TElemento a) {
        boolean ok = true;
        String sim, sima;
        if (!lhs.equalsIgnoreCase(a.lhs))
            ok = false;
        int index = 0;
		if(rhs.size()!=a.rhs.size())
			ok = false;
        for (Enumeration e = rhs.elements(); e.hasMoreElements() && ok;) {
            sim = (String)e.nextElement();
			if(a.rhs.size()-1<index)
				ok = false;
			else{	
	            sima = a.rhs.elementAt(index);
	            if (!sim.equalsIgnoreCase(sima))
	                ok = false;
	            index++;
			}	
        }
        if (ok && pospunto != a.pospunto)
            ok = false;
		//if(ok && simant.size() != a.simant.size())
		//	ok = false;
        for (Enumeration e = simant.elements();
             e.hasMoreElements() && ok; ) {
            sim = (String)e.nextElement();
            ok = false;
            for (Enumeration enum2 = a.simant.elements();
                 enum2.hasMoreElements() && !ok; ) {
                sima = (String)enum2.nextElement();
                if (sim.equalsIgnoreCase(sima))
                    ok = true;
            }
        }
        return ok;
    }

    boolean igualexceptosim(TElemento a) {
        boolean ok = true;
        String sim, sima;
        if (!lhs.equalsIgnoreCase(a.lhs))
            ok = false;
        int index = 0;
		if(rhs.size()!=a.rhs.size())
			ok = false;
        for (Enumeration e = rhs.elements(); e.hasMoreElements() && ok;) {
            sim = (String)e.nextElement();
			if(a.rhs.size()-1<index)
				ok = false;
			else{	
	            sima = a.rhs.elementAt(index);
	            if (!sim.equalsIgnoreCase(sima))
	                ok = false;
	            index++;
			}	
        }
        if (ok && pospunto != a.pospunto)
            ok = false;
        return ok;
    }

    Vector<String> getbetaa() {
        Vector<String> beta = new Vector<String>();
        String sim;
        int n = rhs.size();
        for (int i = pospunto + 1; i < n; i++) {
            sim = rhs.elementAt(i);
            beta.addElement(sim);
        }
        // for (Enumeration e = simant.elements(); e.hasMoreElements(); ) {
            // sim = (String)e.nextElement();
            // beta.addElement(sim);
        // }
        return beta;
    }
    Vector<String> getsimAnt() {
        Vector<String> beta = new Vector<String>();
        String sim;
        for (Enumeration e = simant.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
            beta.addElement(sim);
        }
        return beta;
    }
}
class TPrimero {
    String el;
    Vector<String> valores;

    TPrimero(String nel, Vector<String> val) {
        el = new String(nel);
        valores = new Vector<String>();
        valores = val;
    }

    TPrimero() {
        el = new String();
        valores = new Vector<String>();
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
/*******************************
* una arista en el grafo, con que simbolo y a que vertice va
********************************/
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
    void inicializar(Vector<String> simbol) {
        String X;
        TElemIra elira;
		filaira = new Vector<TElemIra>();
        for (Enumeration e = simbol.elements(); e.hasMoreElements(); ) {
            X = (String)e.nextElement();
            elira = new TElemIra(X, -1);
            filaira.addElement(elira);
        }
    }

    void setestado(String X, int estado) {
        boolean enc = false;
        int index = 0;
        TElemIra elira = new TElemIra();
        for (Enumeration e = filaira.elements();
             e.hasMoreElements() && !enc; ) {
            elira = (TElemIra)e.nextElement();
            if (elira.simbolo.equalsIgnoreCase(X)) {
                enc = true;
                elira.nestado = estado;
            } else
                index++;
        }
        filaira.setElementAt(elira, index);
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
}
class Gramatica {
    String axioma;
    Vector<String> T, V;
    Vector<TRegla> P;
    Vector<TPrimero> primero;
    Vector<TVectorIra> tablaira;
    Vector<TVectorAccion> tablaaccion;
	TTablaSimbolos ts;    
	Stack<String> pilasem;
	String sufijo;

    void aumentargramatica() {
        TRegla regla = new TRegla();
        T.addElement("$");
        String str = new String(axioma + "P");
        regla.rhs.addElement(axioma);
        regla.rhs.addElement("$");
        regla.lhs = str;
        regla.num = 0;
        P.insertElementAt(regla, 0);
        axioma = str;
        V.addElement(str);
    }
	/*********************************************
	* A partir de una cadena donde va la regla, obtener el objeto TRegla y guardarlo en el cjto de producciones P
	**********************************************/
    void insertregla(String cadena, int num) {
        StringBuffer straux;
        String pizda = new String();
        String str = new String();
        String str2 = new String();
        Vector<String> pdcha = new Vector<String>();
        char nombre[] = new char[140];
        TRegla regla;
        char cant = ' ';
        char ch;
        int i, j, k, m;
        boolean enc = false;
        int longcad = cadena.length();
        i = 0;
        j = 0;
		ch = cadena.charAt(i);
		while (i < longcad && ch >= '0' && ch <= '9') {
			i++;
			if (i < longcad)
				ch = cadena.charAt(i);
		}
        while (!enc) {
            ch = cadena.charAt(i++);
            if (ch != ' ')
                if (ch == '-')
                    cant = ch;
                else if (ch == '>' && cant == '-')
                    enc = true;
                else
                    nombre[j++] = ch;
        }
        nombre[j] = 0;
        pizda = String.copyValueOf(nombre);
        straux = new StringBuffer(pizda);
        straux.setLength(j);
        pizda = new String(straux);
        j = 0;
        if (V.contains(pizda)) {
		    try {
            for (; i < longcad; i++) {
                ch = cadena.charAt(i);
                if (ch != ' ') {
                    nombre[j++] = ch;
                    nombre[j] = 0;
                    str = String.copyValueOf(nombre);
                    straux = new StringBuffer(str);
                    straux.setLength(j);
                    str = new String(straux);
					if (str.compareTo("lambda")==0){
						pdcha.addElement(str);
						j = 0;
                    }else					
                    if (V.contains(str) || T.contains(str)) { // con lo leido de la cadena se ha encontrado ya una vble. o terminal
                        enc = false;
                        k = i + 1;
                        m = j;
                        while (ch != ' ' && k < longcad && !enc) { //  ver si hay una vble. o terminal cuyo nombre englobe al encontrado
                            ch = cadena.charAt(k);
                            nombre[m] = ch;
                            nombre[m + 1] = 0;
                            str2 = String.copyValueOf(nombre);
                            straux = new StringBuffer(str2);
                            straux.setLength(m + 1);
                            str2 = new String(straux);
                            if (V.contains(str2) || T.contains(str2))
                                enc = true;
                            k++;
                            m++;
                        }
                        if (!enc) { // si no se ha encontrado una vble. o terminal cuyo nombre englobe al encontrado, añadir a la parte derecha de la regla
                            pdcha.addElement(str);
                            j = 0;
                        }
                    }
                }
            }
			}catch (ArrayIndexOutOfBoundsException e){
			  System.out.println("nombre="+new String(nombre));
			  throw e;
			}
            regla = new TRegla(pizda, pdcha, num);
            P.addElement(regla);
        }
    }
    /********************************************
	* muestra por pantalla todas las reglas de produccion de la gramatica
	*********************************************/
    void verreglas(PrintWriter outputStream,boolean pant) {
        StringBuffer cad = new StringBuffer();
        String sim;
        TRegla regla;
        for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
			sim = (String)e.nextElement();
			if(e.hasMoreElements()){
				cad.append(sim);
				cad.append(" ");
			}	
		}
		if(pant)
			System.out.println(String.valueOf(cad));
		else{
			outputStream.println(axioma.substring(0,axioma.length()-1));
			outputStream.println(String.valueOf(cad));
		}	
		cad = new StringBuffer();
        for (Enumeration e = V.elements(); e.hasMoreElements(); ) {
			sim = (String)e.nextElement();
			if(e.hasMoreElements()){
				cad.append(sim);
				cad.append(" ");
			}	
		}
		if(pant)
			System.out.println(String.valueOf(cad));
		else
			outputStream.println(String.valueOf(cad));
        for (Enumeration e = P.elements(); e.hasMoreElements(); ) {
            regla = (TRegla)e.nextElement();
			if(regla.num>0){
				cad = new StringBuffer(String.valueOf(regla.num));
				cad.append(" " + regla.lhs + " -> ");
				for (Enumeration enum2 = regla.rhs.elements();enum2.hasMoreElements(); ) {
					sim = (String)enum2.nextElement();
					cad.append(sim);
					if(enum2.hasMoreElements())
						cad.append(" ");
				}
				if(pant)
					System.out.println(String.valueOf(cad));
				else
					outputStream.println(String.valueOf(cad));
			}
        }
    }
    void escribereglas(PrintWriter outputStream) {
        StringBuffer cad = new StringBuffer();
        String sim;
        TRegla regla;
		boolean sig;
        for (Enumeration e = P.elements(); e.hasMoreElements(); ) {
            regla = (TRegla)e.nextElement();
            cad = new StringBuffer("\t\tregla = new TRegla(\""+regla.lhs+"\",\"");
			sig  = false;
            for (Enumeration enum2 = regla.rhs.elements(); enum2.hasMoreElements(); ) {
				if(sig)
					cad.append(" ");
				else
					sig = true;
                sim = (String)enum2.nextElement();
				cad.append(sim);
            }
			cad.append("\","+String.valueOf(regla.num)+");");
            outputStream.println(String.valueOf(cad));
			outputStream.println("\t\tP.addElement(regla);");
        }
    }
	/****************************************
	* devuelve la parte derecha de una regla con parte izda igual al parametro
	******************************************/
    Vector<String> getrhs(String a) {
        boolean yaesta = false;
        Vector<String> val = new Vector<String>();
        TRegla regla;
        for (Enumeration e = P.elements();
             e.hasMoreElements() && !yaesta; ) {
            regla = (TRegla)e.nextElement();
            if (regla.lhs.equalsIgnoreCase(a)) {
                yaesta = true;
                val = regla.rhs;
            }
        }
        return val;
    }
	/*****************************************
	* devuelve el cjto. primero del simbolo gramatical Y
	******************************************/
    Vector<String> getvaloresprimero(String Y) {
        TPrimero item;
        Vector<String> val = new Vector<String>();
        boolean enc = false;
        for (Enumeration e = primero.elements();
             e.hasMoreElements() && !enc; ) {
            item = (TPrimero)e.nextElement();
            if (item.el.equalsIgnoreCase(Y)) {
                enc = true;
                val = item.valores;
            }
        }
        return val;
    }	
	/***************************************
	* Devuelve los terminales que es posible alcanzar por derivaciones de la forma de frase del parametro
	***************************************/
    Vector<String> calcularprimerocad(Vector<String> alfa) {
        Vector<String> cadena, primcad, Z;
        String Y, el;
        boolean yaesta = false;
        cadena = alfa;
        primcad = new Vector<String>();
        for (Enumeration e = cadena.elements();
             e.hasMoreElements() && !yaesta; ) {
            Y = (String)e.nextElement();
            Z = getvaloresprimero(Y);
            yaesta = true;
            for (Enumeration enum2 = Z.elements(); enum2.hasMoreElements(); ) {
                el = (String)enum2.nextElement();
                if (!el.equalsIgnoreCase("lambda"))
                    primcad.addElement(el);
                else
                    yaesta = false;
            }
        }
        if (!yaesta)
            primcad.addElement("lambda");
        return primcad;
    }
	/*****************************************
	* Calcula los cjtos. primeros de todos los simbolos gramaticales
	******************************************/
    void calcularprimeros() {
        TPrimero el = new TPrimero();
        String sim, Y;
        Vector<String> val, Z, cadena;
        boolean yaesta, enc, marfinal;
        int index, longi;
        TRegla regla;
        primero = new Vector<TPrimero>();
        for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
            val = new Vector<String>();
            val.addElement(sim);
            el = new TPrimero(sim, val);
            primero.addElement(el);
        }
        for (Enumeration e = V.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
            val = new Vector<String>();
            el = new TPrimero(sim, val);
            primero.addElement(el);
        }
		// Para las reglas X->lambda añadir lambda a primero(X)
        for (Enumeration e = P.elements(); e.hasMoreElements(); ) {
            regla = (TRegla)e.nextElement();
            if (regla.vacia()) {
                enc = false;
                index = 0;
                for (Enumeration enum2 = primero.elements();
                     enum2.hasMoreElements() && !enc; ) {
                    el = (TPrimero)enum2.nextElement();
                    if (el.el.equalsIgnoreCase(regla.lhs)) {
                        enc = true;
                        el.valores.addElement("lambda");
                        primero.setElementAt(el, index);
                    }
                    index++;
                }
            }
        }
        marfinal = false;
        while (!marfinal) {
            marfinal = true;
			// Para cada prod. X->Y1 Y2 ... Yk añadir primero(Yi) a primero(X) si lambda esta en primero(Yj) para todo j<i
            for (Enumeration e = P.elements(); e.hasMoreElements(); ) {
                regla = (TRegla)e.nextElement();
                if (!regla.vacia()) {
                    enc = false;
                    index = 0;
                    for (Enumeration enum2 = primero.elements();
                         enum2.hasMoreElements() && !enc; ) {
                        el = (TPrimero)enum2.nextElement();
                        if (el.el.equalsIgnoreCase(regla.lhs))
                            enc = true;
                        else
                            index++;
                    }
                    cadena = new Vector<String>(regla.rhs);
                    do {
                        Y = new String(cadena.firstElement());
                        longi = el.valores.size();
                        Z = getvaloresprimero(Y);
                        yaesta = true;
                        for (Enumeration enum2 = Z.elements();
                             enum2.hasMoreElements(); ) {
                            sim = (String)enum2.nextElement();
                            if (!sim.equalsIgnoreCase("lambda")) {
                                if (!el.valores.contains(sim))
                                    el.valores.addElement(sim);
                            } else {
                                cadena.removeElementAt(0);
                                yaesta = false;
                            }
                        }
						if (cadena.isEmpty() && !el.valores.contains("lambda"))
                            el.valores.addElement("lambda");						
                        if (longi < el.valores.size())
                            marfinal = false;
                    } while (!cadena.isEmpty() && !yaesta);
					primero.setElementAt(el, index);                    
                }
            }
        }
    }
	/*****************************************
	* muestra por pantalla los cjtos. primeros
	******************************************/
    void verprimeros() {
        StringBuffer cad = new StringBuffer();
        String sim;
        TPrimero elem;
        for (Enumeration e = primero.elements(); e.hasMoreElements(); ) {
            elem = (TPrimero)e.nextElement();
            cad = new StringBuffer(elem.el + ":");
            for (Enumeration enum2 = elem.valores.elements();
                 enum2.hasMoreElements(); ) {
                sim = (String)enum2.nextElement();
                cad.append(sim + " ");
            }
            System.out.println(String.valueOf(cad));
        }
    }
    void setestadoira(int pos, String X, int estado) {
        TVectorIra ira;
        ira = tablaira.elementAt(pos);
        ira.setestado(X, estado);
        tablaira.setElementAt(ira, pos);
    }
    int getestadoira(int pos, String X) {
        TVectorIra ira;
        int estado;
        ira = tablaira.elementAt(pos);
        estado = ira.getestado(X);
        return estado;
    }
	/**********************************
	* Muestra en pantalla la tabla ira: formateada una fila por estado o nodo del grafo y debajo de los terminales, no-terminales
	* el numero del estado al que se va en el grafo por ese simbolo
	***********************************/
    void vertablaira() {
        Vector<String> simbolo = new Vector<String>();
        String sim, cad;
		StringBuffer espacios = new StringBuffer();
        TVectorIra ira;
        TElemIra item;
        int index2, x;
        int index = 0;
        int longcelda = 0;
        for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
            if (sim.length() > longcelda)
                longcelda = sim.length();
            simbolo.addElement(sim);
        }
        for (Enumeration e = V.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
            if (sim.length() > longcelda)
                longcelda = sim.length();
            simbolo.addElement(sim);
        }
        cad = new String("ir_a");
        if (cad.length() > longcelda)
            longcelda = cad.length();
        for (Enumeration e = simbolo.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
			espacios.delete(0,espacios.length());
            for(x =0;x<(longcelda - sim.length());x++){
			   espacios.append(" ");
			}   
            cad+=sim+espacios.toString();
        }
		System.out.println(cad);
        for (Enumeration e = tablaira.elements(); e.hasMoreElements(); ) {
            cad = String.valueOf(index);
			espacios.delete(0,espacios.length());
            for(x =0;x<(longcelda - cad.length());x++){
			   espacios.append(" ");
			}   
			cad += espacios.toString();
            ira = (TVectorIra)e.nextElement();
            for (Enumeration enum2 = ira.elements(); enum2.hasMoreElements();) {
                item = (TElemIra)enum2.nextElement();
                if (item.nestado >= 0) {
                    index2 = simbolo.indexOf(item.simbolo);
					espacios.delete(0,espacios.length());
		            for(x =0;x<(index2 * longcelda +longcelda - String.valueOf(item.nestado).length());x++){
					   espacios.append(" ");
					}   
                    cad+=espacios.toString()+String.valueOf(item.nestado);
                }
            }
			System.out.println(cad);
            index++;
        }
    }
	/******************************************
	*  Construccion de conjuntos de elementos LR(1) para la gramatica G
	*******************************************/
    Vector<TConjEltos> calcularcoleclr1() {
        // C := cerradura({[S' ->.S,$]})
        // repetir: para cada cjto. de elementos I en C y cada simbolo gramatical X tal que ir_a(I,X) no este vacio y no este en C hacer añadir ir_a(I,X) a C
        // hasta: no se pueden añadir mas conjuntos de elementos a C
        Vector<TConjEltos> cclr1 = new Vector<TConjEltos>();
        TConjEltos I = new TConjEltos();
        TConjEltos el2, nuevoconj;
        TElemento el;
        Vector<String> alfa, santicip, simbol;
        TElemIra elira;
        TVectorIra ira;
        String X;
        int posi;
        tablaira = new Vector<TVectorIra>();
        alfa = getrhs(axioma);
        santicip = new Vector<String>();
        santicip.addElement("$");
        el = new TElemento(axioma, alfa, 0, santicip);
        el2 = new TConjEltos();
        el2.addElement(el);
        el2.calcularcierre(this);
        cclr1.addElement(el2);
        ira = new TVectorIra();
        simbol = new Vector<String>(T);
        for (Enumeration e = V.elements(); e.hasMoreElements(); ) {
            X = (String)e.nextElement();
            if (!X.equalsIgnoreCase(axioma))
                simbol.addElement(X);
        }
        // simbol.addAll(V);
        ira.inicializar(simbol);
        tablaira.addElement(ira);
        int primeranyadido = 0;
        int ultanyadido = 0;
        do {
            for (int j = primeranyadido; j <= ultanyadido; j++) {
                I = (TConjEltos)cclr1.elementAt(j);
                for (Enumeration e = simbol.elements();
                     e.hasMoreElements(); ) {
                    X = (String)e.nextElement();
                    nuevoconj = I.calcularira(this, X);
                    if (!nuevoconj.isEmpty()) {
                        posi = posicion(cclr1,nuevoconj);
                        if (posi >= 0)
                            setestadoira(j, X, posi);
                        else {
                            cclr1.addElement(nuevoconj);
                            posi = cclr1.size() - 1;
                            setestadoira(j, X, posi);
                            ira = new TVectorIra();
                            ira.inicializar(simbol);
                            tablaira.addElement(ira);
                        }
                    }
                }
            }
            primeranyadido = ultanyadido + 1;
            ultanyadido = cclr1.size() - 1;
        } while (ultanyadido >= primeranyadido);
        return cclr1;
    }
    /*************************************
	* devuelve el numero correspondiente de una regla
	**************************************/
    int encnregla(String A, Vector<String> beta) {
        TRegla item;
        int nreg, index;
        boolean igual;
        String alfa, gama;
        nreg = -1;
        for (Enumeration e = P.elements(); e.hasMoreElements(); ) {
            item = (TRegla)e.nextElement();
            igual = true;
            index = 0;
            if (!item.lhs.equalsIgnoreCase(A))
                igual = false;
			if(igual)	
				if(beta.size()==0){
					if(item.rhs.size()>0)
						igual = false;
				}else		
		            for (Enumeration enum2 = beta.elements();enum2.hasMoreElements() && igual; ) {
		                alfa = (String)enum2.nextElement();
						if(item.rhs.size()-1<index)
							igual = false;
						else{	
			                gama = item.rhs.elementAt(index);
			                if (!alfa.equalsIgnoreCase(gama))
			                    igual = false;
			                index++;
						}	
		            }	
            if (igual){
                nreg = item.num;
				break;
			}	
        }
        return nreg;
    }
    /***************************************************
	* Muestra en pantalla la tabla de acciones para el analizador sintactico, en la primera fila van los terminales y a continuacion
	* una fila por estado y debajo de la columna del terminal la accion a realizar que puede estar vacia o ser una indicacion de reduccion por una regla  o una indicacion de desplazamiento
	****************************************************/
    void vertablaacc() {
        String sim, cad;
		String oper = new String();
		StringBuffer espacios = new StringBuffer();
        TVectorAccion acc;
        TEltoAccion item;
        int index2,x;
        int index = 0;
        int longcelda = 6;
        for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
            if (sim.length() > longcelda)
                longcelda = sim.length();
        }
        cad = new String("Estado");
        for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
            sim = (String)e.nextElement();
			espacios.delete(0,espacios.length());
            for(x=0;x<(longcelda - sim.length());x++){
  			   espacios.append(" ");
			}   
            cad+=sim+espacios.toString();
        }
		System.out.println(cad);
        for (Enumeration e = tablaaccion.elements(); e.hasMoreElements(); ) {
            cad=String.valueOf(index);
			espacios.delete(0,espacios.length());
            for(x=0;x<(longcelda - cad.length());x++){
  			   espacios.append(" ");
			}   
            acc = (TVectorAccion)e.nextElement();
            for (Enumeration enum2 = acc.elements(); enum2.hasMoreElements();) {
                item = (TEltoAccion)enum2.nextElement();
                if (!item.tipo.equalsIgnoreCase("error")) {
                    index2 = T.indexOf(item.simbolo);
                    if (item.tipo.equalsIgnoreCase("reducir")) {
                        oper = "red [" + String.valueOf(item.R.num) + ","+item.simbolo+"]";
                    }
                    if (item.tipo.equalsIgnoreCase("desplazar")) {
                        oper = "desp"+item.simbolo;
                    }
                    if (item.tipo.equalsIgnoreCase("aceptar")) {
                        oper = "acep";
                    }
					espacios.delete(0,espacios.length());
		            for(x=0;x<(index2 * longcelda + longcelda- oper.length());x++){
		  			   espacios.append(" ");
					}   
                    cad+= oper+espacios;
                }
            }
			System.out.println(cad);
            index++;
        }
    }
	/********************************************************
	* escribe en forma de programa la tabla de Accion
	*********************************************************/
	void escribetablaacc(){
        TVectorAccion acc;
        TEltoAccion item;
		String str = new String("TEltoAccion[][] bindVars = new TEltoAccion[][]{");
		String cad;
		StringBuffer sb = new StringBuffer(str);
		StringBuffer sb2;
		boolean sig;
		int i;
		for(Enumeration e = tablaaccion.elements(); e.hasMoreElements();){
			acc = (TVectorAccion)e.nextElement();
			if(sb.length()>str.length()) sb.append(",");
			sb2 = new StringBuffer("{"); i=1;
			for(Enumeration enum2 = acc.elements(); enum2.hasMoreElements();){
				item = (TEltoAccion)enum2.nextElement();
				if(!item.tipo.equalsIgnoreCase("error")){
					if(sb2.length()>1) sb2.append(",");
					if(sb2.length()>(i*155)){
						sb2.append("\n\t\t\t");
						i++;
					}	
					if(item.R != null){
						if(item.R.rhs.isEmpty())
							sb2.append("getEltoAccion(\""+item.tipo+"\",\""+item.simbolo+"\",new TRegla(\""+item.R.lhs+"\",\"\","+Integer.toString(item.R.num)+"))");
						else{
							cad = new String(); sig = false;
							for(Enumeration enumr=item.R.rhs.elements();enumr.hasMoreElements();){								
								if(sig)
									cad = cad.concat(" ");
								else
									sig = true;
								cad = cad.concat((String)enumr.nextElement());
							}	
							sb2.append("getEltoAccion(\""+item.tipo+"\",\""+item.simbolo+"\",new TRegla(\""+item.R.lhs+"\",\""+cad+"\","+Integer.toString(item.R.num)+"))");	
						}		
					}else
						sb2.append("getEltoAccion(\""+item.tipo+"\",\""+item.simbolo+"\","+Integer.toString(item.sig_estado)+")");
				}
			}
			sb.append(sb2.toString()+"}");
		}
		System.out.println("\t\t"+sb.toString()+"};");
		System.out.println("\t\ttablaaccion = new Vector<TVectorAccion>();");
		System.out.println("\t\tfor(int z = 0;z<bindVars.length;z++){");
		System.out.println("\t\t	acc = new TVectorAccion();");
		System.out.println("\t\t	for(int k=0;k<bindVars[z].length;k++)");
		System.out.println("\t\t		acc.addElement(bindVars[z][k]);");
		System.out.println("\t\t	tablaaccion.addElement(acc);");
		System.out.println("\t\t}");
	}
	void escribetablaacc2(PrintWriter outputStream) throws IOException{
        TVectorAccion acc;
        TEltoAccion item;
		String cad;
		StringBuffer sb2;
		boolean sig;
		int n=0;
		PrintWriter outputStream3 = new PrintWriter(new FileWriter("tabla_acc"+this.sufijo+".txt"));
		for(Enumeration e = tablaaccion.elements(); e.hasMoreElements();){
			n++;
			acc = (TVectorAccion)e.nextElement();
			sb2 = new StringBuffer(""); 
			for(Enumeration enum2 = acc.elements(); enum2.hasMoreElements();){
				item = (TEltoAccion)enum2.nextElement();
				if(!item.tipo.equalsIgnoreCase("error")){
					if(sb2.length()>1) sb2.append(",");
					if(item.R != null){
						if(item.R.rhs.isEmpty())
							sb2.append(item.tipo+","+item.simbolo+","+item.R.lhs+",,"+Integer.toString(item.R.num)+",");
						else{
							cad = new String(); sig = false;
							for(Enumeration enumr=item.R.rhs.elements();enumr.hasMoreElements();){								
								if(sig)
									cad = cad.concat(" ");
								else
									sig = true;
								cad = cad.concat((String)enumr.nextElement());
							}	
							sb2.append(item.tipo+","+item.simbolo+","+item.R.lhs+","+cad+","+Integer.toString(item.R.num)+",");	
						}		
					}else
						sb2.append(item.tipo+","+item.simbolo+",,,,"+Integer.toString(item.sig_estado));
				}
			}
			outputStream3.println(sb2.toString());
		}
		outputStream3.close();
		outputStream.println("\t\tTEltoAccion[][] bindVars = new TEltoAccion["+n+"][];");
		outputStream.println("\t\tBufferedReader inputStream = null;");
		outputStream.println("\t\tString l;");
		outputStream.println("\t\tint fila = 0,ncol;");
		outputStream.println("\t\ttry{");
		outputStream.println("\t\t	inputStream = new BufferedReader(new FileReader(\"tabla_acc"+this.sufijo+".txt\"));");
		outputStream.println("\t\t	while((l=inputStream.readLine())!=null){		");
		outputStream.println("\t\t	  String[] vars= l.split(\",\");");
		outputStream.println("\t\t	  if(vars.length%6==0)");
		outputStream.println("\t\t	  	ncol =vars.length/6;");
		outputStream.println("\t\t	  else");
		outputStream.println("\t\t		ncol =(vars.length+1)/6;  ");
		outputStream.println("\t\t	  bindVars[fila] = new TEltoAccion[ncol];");
		outputStream.println("\t\t	  for(int i=0;i<ncol;i++)");
		outputStream.println("\t\t		if(!vars[i*6+2].isEmpty())");
		outputStream.println("\t\t			bindVars[fila][i] = getEltoAccion(vars[i*6],vars[i*6+1],new TRegla(vars[i*6+2],new String(vars[i*6+3]),Integer.parseInt(vars[i*6+4])));");
		outputStream.println("\t\t		else");
		outputStream.println("\t\t			bindVars[fila][i] = getEltoAccion(vars[i*6],vars[i*6+1],Integer.parseInt(vars[i*6+5]));");
		outputStream.println("\t\t	  fila++;");
		outputStream.println("\t\t	}	");
		outputStream.println("\t\t}finally{");
		outputStream.println("\t\t	if(inputStream!=null)");
		outputStream.println("\t\t		inputStream.close();");
		outputStream.println("\t\t}");
		outputStream.println("\t\ttablaaccion = new Vector<Hashtable>();");
		outputStream.println("\t\tfor(int z = 0;z<bindVars.length;z++){");
		outputStream.println("\t\t	Hashtable acc = new Hashtable();");
		outputStream.println("\t\t	for(int k=0;k<bindVars[z].length;k++)");
		outputStream.println("\t\t		acc.put(bindVars[z][k].simbolo,new EltoAcc(bindVars[z][k].tipo,bindVars[z][k].R,bindVars[z][k].sig_estado));");
		outputStream.println("\t\t	tablaaccion.addElement(acc);");
		outputStream.println("\t\t}");
	}
	/********************************************************
	* escribe en forma de programa la tabla de ira
	*********************************************************/	
	void escribetablaira() {
        String cad = new String("TElemIra[][] bindVars = new TElemIra[][]{");
        TVectorIra ira;
        TElemIra item;
		StringBuffer sb = new StringBuffer(cad);
		StringBuffer sb2;
		int i,index2;
        for (Enumeration e = tablaira.elements(); e.hasMoreElements(); ) {
            ira = (TVectorIra)e.nextElement();
			if(sb.length()>cad.length()) sb.append(",");
			sb2 = new StringBuffer("{"); i=1;
            for (Enumeration enum2 = ira.elements(); enum2.hasMoreElements();) {
                item = (TElemIra)enum2.nextElement();
                if (item.nestado >= 0) {
					if(sb2.length()>1) sb2.append(",");
					if(sb2.length()>(i*155)){
						sb2.append("\n\t\t\t");
						i++;
					}	
                    //index2 = V.indexOf(item.simbolo);
					//if(index2!=-1)
					sb2.append("getElemIra(\""+item.simbolo+"\","+item.nestado+")");
                }
            }
			sb.append(sb2.toString()+"}");
        }
		System.out.println("\t\t"+sb.toString()+"};");
		System.out.println("\t\ttablaira = new Vector<TVectorIra>();");
		System.out.println("\t\tfor(int z = 0;z<bindVars.length;z++){");
		System.out.println("\t\t	ira = new TVectorIra();");
		System.out.println("\t\t	for(int k=0;k<bindVars[z].length;k++)");
		System.out.println("\t\t		ira.addElement(bindVars[z][k]);");
		System.out.println("\t\t	tablaira.addElement(ira);");
		System.out.println("\t\t}");
    }
	void escribetablaira2(PrintWriter outputStream) {
        String cad = new String("TElemIra[][] bindVars = new TElemIra[][]{");
        TVectorIra ira;
        TElemIra item;
		StringBuffer sb = new StringBuffer(cad);
		StringBuffer sb2;
		int index2;
		Boolean prim_linea=true;
        for (Enumeration e = tablaira.elements(); e.hasMoreElements(); ) {
            ira = (TVectorIra)e.nextElement();
			if(!prim_linea){
				sb.append(",");
				outputStream.println("\t\t"+sb.toString());
				sb = new StringBuffer("\t");
			}else prim_linea = false;	
			sb2 = new StringBuffer("{"); 
            for (Enumeration enum2 = ira.elements(); enum2.hasMoreElements();) {
                item = (TElemIra)enum2.nextElement();
                if (item.nestado >= 0) {
					if(sb2.length()>1) sb2.append(",");
					sb2.append("getElemIra(\""+item.simbolo+"\","+item.nestado+")");
                }
            }
			sb.append(sb2.toString()+"}");
        }
		outputStream.println("\t\t"+sb.toString()+"};");
		outputStream.println("\t\ttablaira = new Vector<Hashtable>();");
		outputStream.println("\t\tfor(int z = 0;z<bindVars.length;z++){");
		outputStream.println("\t\t	Hashtable ira = new Hashtable();");
		outputStream.println("\t\t	for(int k=0;k<bindVars[z].length;k++)");
		outputStream.println("\t\t		ira.put(bindVars[z][k].simbolo,new Integer(bindVars[z][k].nestado));");
		outputStream.println("\t\t	tablaira.addElement(ira);");
		outputStream.println("\t\t}");
    }
    /********************************************
	* Busca en la coleccion de elementos, devuelve -1 si no se encuentra, la posicion en el vector de conjuntos de elementos, en caso contrario
	*********************************************/
    int posicion(Vector<TConjEltos> tablaConjEltos,TConjEltos conj) {
        TConjEltos iconj;
        TElemento ii, elem;
        boolean yaesta, igual, enc;
        int index = 0;
        yaesta = false;
        for (Enumeration e = tablaConjEltos.elements();
             e.hasMoreElements() && !yaesta; ) {
            iconj = (TConjEltos)e.nextElement();
            igual = true;
            for (Enumeration enum2 = iconj.elements();
                 enum2.hasMoreElements() && igual; ) {
                ii = (TElemento)enum2.nextElement();
                enc = false;
                for (Enumeration enum3 = conj.elements();
                     enum3.hasMoreElements() && !enc; ) {
                    elem = (TElemento)enum3.nextElement();
                    if (elem.igual(ii))
                        enc = true;
                }
                if (!enc)
                    igual = false;
            }
            if (!igual || iconj.size()!=conj.size())
                index++;
            else
                yaesta = true;
        }
        if (yaesta)
            return index;
        else
            return -1;
    }
	/***************************************************
	* Muestra en pantalla la coleccion de elementos
	****************************************************/
    void vercoleclr1(Vector<TConjEltos> tablaConjEltos) {
        int index, index2;
        String cad, simbolo;
        TConjEltos elem;
        TElemento item;
        index = 0;
        for (Enumeration e = tablaConjEltos.elements(); e.hasMoreElements(); ) {
            elem = (TConjEltos)e.nextElement();
            System.out.println("I" + String.valueOf(index));
            for (Enumeration enum2 = elem.elements(); enum2.hasMoreElements();
            ) {
                item = (TElemento)enum2.nextElement();
                cad = new String(item.lhs + "->");
                index2 = 0;
                for (Enumeration enum3 = item.rhs.elements();
                     enum3.hasMoreElements(); ) {
                    simbolo = (String)enum3.nextElement();
                    if (item.pospunto == index2)
                        cad += "*";
                    cad += simbolo;
                    index2++;
                }
                cad += ",";
                for (Enumeration enum3 = item.simant.elements();
                     enum3.hasMoreElements(); ) {
                    simbolo = (String)enum3.nextElement();
                    cad += simbolo;
                }
                System.out.println(cad);
            }
            index++;
        }
    }
    /***************************************************
	* Calcular la tabla de acciones: recorrer el grafo de los cjtos. de elementos buscando las acciones: 
	* si en un elemento el punto esta delante de un terminal la accion sera: desplazar (en la tabla ir_a hay una arista por ese terminal desde el estado en curso
	* y si el punto esta al final de una regla, la accion será reducir por dicha regla
	****************************************************/
    boolean calcularlr1() {
        TConjEltos conj;
        TElemento item;
        TEltoAccion elacc;
        TVectorAccion acc;
        Vector<TConjEltos> cclr1;
        boolean eslr1 = true;
        int estado, nreg;
        String t, tipo;
        t = new String();
        cclr1 = calcularcoleclr1();
		System.out.println("Coleccion elementos=");
		vercoleclr1(cclr1);
		System.out.println("Terminales=");
		String l;
		for (Enumeration e = this.T.elements();e.hasMoreElements(); ) {
                l = (String)e.nextElement();
				System.out.println(l);
		}		
		System.out.println("Variables=");
		for (Enumeration e = this.V.elements();e.hasMoreElements(); ) {
                l = (String)e.nextElement();
				System.out.println(l);
		}		

        int ultestado = tablaira.size();
        tablaaccion = new Vector<TVectorAccion>();
		// inicializar la tabla de acciones para el analizador sintactico ascendente
        for (int i = 0; i < ultestado; i++) {
            acc = new TVectorAccion();
            for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
                t = (String)e.nextElement();
                elacc = new TEltoAccion();
                elacc.simbolo = new String(t);
                elacc.tipo = new String("error");
                acc.addElement(elacc);
            }
            tablaaccion.addElement(acc);
        }
		// obtener primero las acciones de desplazar: para todas las aristas por terminales en ir_a, añadir la accion de desplazar
        for (int i = 0; i < ultestado; i++) {
            acc = tablaaccion.elementAt(i);
            for (Enumeration e = T.elements(); e.hasMoreElements(); ) {
                t = (String)e.nextElement();
                estado = getestadoira(i, t);
                if (estado >= 0)
                    acc.settipo("Desplazar",t,estado);
            }
            tablaaccion.setElementAt(acc, i);
        }
        for (int i = 0; i < ultestado; i++) {
            acc = tablaaccion.elementAt(i);
            conj = (TConjEltos)cclr1.elementAt(i);
            for (Enumeration e = conj.elements(); e.hasMoreElements();) {
                item = (TElemento)e.nextElement();
                if (item.pospunto == item.rhs.size()) // hay un elemento con el punto al final
                    if (!item.lhs.equalsIgnoreCase(axioma))
                        for (Enumeration enum2 = item.simant.elements();
                             enum2.hasMoreElements(); ) {
                            t = (String)enum2.nextElement();
                            tipo = acc.gettipo(t);
                            if (tipo.equalsIgnoreCase("error")) {
                                acc.settipo("Reducir", t,0);
                                nreg = encnregla(item.lhs, item.rhs);
                                acc.setregla(item.lhs, item.rhs, nreg, t);
                            } else
                                eslr1 = false;
                        }
                    else {
                        tipo = acc.gettipo("$");
                        if (tipo.equalsIgnoreCase("error"))
                            acc.settipo("Aceptar", "$",0);
                        else
                            eslr1 = false;
                    }
            }
        }
        return eslr1;
    }
	/**********************************************
	* dar de alta  los terminales, variables y reglas que forman la gramatica en estudio, el nombre del fichero normalmente será de la forma grama_ 
	* y a continuacion el nombre que se utilizara para nombrar los paquetes
	**********************************************/
	void inicializarGramatica(String nomFich) throws IOException {
		BufferedReader inputStream = null;
		String l;
		try{
			this.sufijo = nomFich.substring(nomFich.indexOf("_"),nomFich.indexOf("."));
			inputStream = new BufferedReader(new FileReader(nomFich));
			l = inputStream.readLine();
			this.axioma = l;
			l = inputStream.readLine();
			this.T = new Vector<String>();
			String[] result;
			result = new String[l.split(" ").length];
			result = l.split(" ");
	        for (int x = 0; x < result.length; x++)
	            this.T.addElement(result[x]);
			l = inputStream.readLine();	
			result = new String[l.split(" ").length];
			result = l.split(" ");
			this.V = new Vector<String>();
	        for (int x = 0; x < result.length; x++)
	            this.V.addElement(result[x]);
			this.P = new Vector<TRegla>();
			int i= 1;
			while((l=inputStream.readLine())!=null){	
				if(!l.contains("COM=")){
					this.insertregla(l, i);
					i++;
				}
			}	
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}	
        this.aumentargramatica();
        this.calcularprimeros();	  
	    this.verprimeros();
		pilasem = new Stack<String>();
	}

}
class TConjEltos {
    private Vector<TElemento> conjEltos;
	TConjEltos () {
		conjEltos = new Vector<TElemento>();
	}
	TConjEltos (Vector<TElemento> pconj) {
		conjEltos = pconj;
	}
	Enumeration elements(){
		return conjEltos.elements();
	}	
	void addElement(TElemento el){
		conjEltos.addElement(el);
	}
	int size(){
		return conjEltos.size();
	}
	boolean isEmpty(){
		return conjEltos.isEmpty();
	}	
	void removeAllElements(){
		conjEltos.removeAllElements();
	}
	void setElementAt(TElemento el, int index){
		conjEltos.setElementAt(el,index);
	}	
    void calcularcierre(Gramatica G) {
        // repetir: Para cada elemento [A->alfa .B beta,a] en I,  cada produccion B->gamma en G y cada terminal b en PRIMERO(beta a) tal que [B->.gamma,b] no este en I hacer añadir [B->.gamma,b]  a I
        // hasta no se pueden añadir mas elementos a I
        Vector<TElemento> nuevoseltoscierre = new Vector<TElemento>(this.conjEltos);
        Vector<TElemento> anyadidos = new Vector<TElemento>();
        String B,sim; 
        TElemento item, nuevoitem;
        Vector<String> termb, betaa;
        TRegla regla;
        do {
            anyadidos.removeAllElements();
            for (Enumeration e = nuevoseltoscierre.elements();
                 e.hasMoreElements(); ) {
                item = (TElemento)e.nextElement();
                B = item.getsimdespuespunto();
                if (G.V.contains(B))
                    for (Enumeration enum2 = G.P.elements();
                         enum2.hasMoreElements(); ) {
                        regla = (TRegla)enum2.nextElement();
                        if (B.equalsIgnoreCase(regla.lhs)) {
                            betaa = item.getbetaa();
                            termb = G.calcularprimerocad(betaa);
							if(termb.contains("lambda")){
								betaa = item.getsimAnt();
								for(Enumeration esim = betaa.elements();esim.hasMoreElements();){
									sim = (String)esim.nextElement();
									if(!termb.contains(sim))
										termb.add(sim);
								}	
								termb.remove("lambda");
							}
                            nuevoitem = new TElemento(B, regla.rhs, 0, termb);
                            anyadidos.addElement(nuevoitem);
                        }
                    }
            }
            nuevoseltoscierre = quitar(anyadidos);
            anyadir(anyadidos);
        } while (!nuevoseltoscierre.isEmpty()); // no se pueden añadir mas elementos a I
    }
    /*********************************************************
	* devuelve un vector con los elementos de anyadidos que no estan ya en el cierre
	**********************************************************/
    Vector<TElemento> quitar(Vector<TElemento> conjini) {
        Vector<TElemento>  res = new Vector<TElemento>();
        TElemento item, item2;
        boolean yaesta;
        for (Enumeration e = conjini.elements(); e.hasMoreElements(); ) {
            item = (TElemento)e.nextElement();
            yaesta = false;
            for (Enumeration enum2 = conjEltos.elements();
                 enum2.hasMoreElements() && !yaesta; ) {
                item2 = (TElemento)enum2.nextElement();
                if (item.igual(item2))
                    yaesta = true;
            }
            if (!yaesta)
                res.addElement(item);
        }
        return res;
    }

    void anyadir(Vector<TElemento> a) {
        TElemento item, item2;
        String sim, sim2;
        boolean yaesta;
        int index;
        item2 = new TElemento();
        for (Enumeration e = a.elements(); e.hasMoreElements(); ) {
            item = (TElemento)e.nextElement();
            yaesta = false;
            index = 0;
            for (Enumeration enum2 = this.conjEltos.elements();
                 enum2.hasMoreElements() && !yaesta; ) {
                item2 = (TElemento)enum2.nextElement();
                if (item2.igualexceptosim(item))
                    yaesta = true;
                else
                    index++;
            }
            if (!yaesta)
                this.conjEltos.addElement(item);
            else {
                for (Enumeration enum2 = item.simant.elements();
                     enum2.hasMoreElements(); ) {
                    sim = (String)enum2.nextElement();
                    yaesta = false;
                    for (Enumeration enum3 = item2.simant.elements();
                         enum3.hasMoreElements() && !yaesta; ) {
                        sim2 = (String)enum3.nextElement();
                        if (sim2.equalsIgnoreCase(sim))
                            yaesta = true;
                    }
                    if (!yaesta) {
                        item2.simant.addElement(sim);
                        this.conjEltos.setElementAt(item2, index);
                    }
                }
            }
        }
    }

    TConjEltos calcularira(Gramatica G, String X) {
        // Sea J el conjunto de elementos [A->alfa X . beta,a] tal que [A->alfa . X beta,a] este en I
        // return cerradura(J)
        TConjEltos nuevoconj = new TConjEltos();
        TElemento item, item2;
        String sim;
        for (Enumeration e = conjEltos.elements(); e.hasMoreElements(); ) {
            item = (TElemento)e.nextElement();
            sim = item.getsimdespuespunto();
            if (sim.equalsIgnoreCase(X)) {
                item2 = new TElemento(item.lhs, item.rhs, item.pospunto + 1, item.simant);
                nuevoconj.addElement(item2);
            }
        }
        nuevoconj.calcularcierre(G);
        return nuevoconj;
    }
	
}
class TEltoAccion {
    String simbolo;
    String tipo;
    TRegla R;
	int sig_estado;
	TEltoAccion(String psimbolo,String ptipo,TRegla pr){
		simbolo = new String(psimbolo);
		tipo = new String(ptipo);
		R = pr;
	}	
	TEltoAccion(String psimbolo,String ptipo,int psig_estado){
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
	void addElement(TEltoAccion el){
		filaAccion.addElement(el);
	}	
	Enumeration elements(){
		return filaAccion.elements();
	}	
	/**************************************
	* poner la accion a realizar cuando llega desde la entrada simbol
	***************************************/
    void settipo(String tipo, String simbol, int psig_estado) {
        TEltoAccion item;
        boolean enc = false;
        int index = 0;
        for (Enumeration e = filaAccion.elements();
             e.hasMoreElements() && !enc; ) {
            item = (TEltoAccion)e.nextElement();
            if (item.simbolo.equalsIgnoreCase(simbol)) {
                enc = true;
                item.tipo = tipo;
				item.sig_estado = psig_estado;
                filaAccion.setElementAt(item, index);
            } else
                index++;
        }
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
    /***************************************
	* cuando la accion a realizar es una reduccion, poner con que regla se reduce
	****************************************/
    void setregla(String lhs, Vector<String> rhs, int num, String simbol) {
        TEltoAccion item;
        boolean enc = false;
        int index = 0;
        for (Enumeration e = filaAccion.elements();
             e.hasMoreElements() && !enc; ) {
            item = (TEltoAccion)e.nextElement();
            if (item.simbolo.equalsIgnoreCase(simbol)) {
                enc = true;
                item.R = new TRegla(lhs, rhs, num);
                filaAccion.setElementAt(item, index);
            } else
                index++;
        }
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
public class geLRco_tablas {
	static void escribeparser(Gramatica g)  throws IOException {
		PrintWriter outputStream = null;
		try {
            outputStream = new PrintWriter(new FileWriter("parser"+g.sufijo+"/parser.java"));
			outputStream.println("package parser"+g.sufijo+";");
			outputStream.println("import java.util.*;");
			outputStream.println("import java.io.IOException;");
			outputStream.println("import java.io.FileReader;");
			outputStream.println("import java.io.BufferedReader;");
			outputStream.println("/* ---------------- Gramatica: grama"+g.sufijo+".txt ------------------");
			g.verreglas(outputStream,false);
			outputStream.println("---------------------------------------------------------------- */");
			outputStream.println("class TRegla {");
			outputStream.println("    int num;");
			outputStream.println("    String lhs;");
			outputStream.println("    Vector<String> rhs;");
			outputStream.println("    boolean vacia() {");
			outputStream.println("        return (rhs.contains(\"lambda\")||rhs.size()==0);");
			outputStream.println("    }");
		    outputStream.println("    TRegla(String pizda, Vector<String> pdcha, int n) {");
		    outputStream.println("        num = n;");
		    outputStream.println("        lhs = new String(pizda);");
			outputStream.println("        if(!pdcha.isEmpty())");
		    outputStream.println("        	rhs = new Vector<String>(pdcha);");
			outputStream.println("        else");
			outputStream.println("        	rhs = new Vector<String>();");
		    outputStream.println("    }");
		    outputStream.println("    TRegla() {");
		    outputStream.println("        num = -1;");
		    outputStream.println("        lhs = new String();");
		    outputStream.println("        rhs = new Vector<String>();");
		    outputStream.println("    }");
			outputStream.println("    TRegla(String pizda, String pdcha, int n) {");
			outputStream.println("        num = n;");
			outputStream.println("        lhs = new String(pizda);");
			outputStream.println("        rhs = new Vector<String>();");
			outputStream.println("        if(!pdcha.equals(\"\")){");
			outputStream.println("        	String[] result = pdcha.split(\" \");");
			outputStream.println("        	for (int x = 0; x < result.length; x++)");
			outputStream.println("        		rhs.addElement(new String(result[x]).trim());");
			outputStream.println("    	}");
			outputStream.println("    }");
			outputStream.println("}");
			outputStream.println("class TElemIra {");
			outputStream.println("    String simbolo;");
			outputStream.println("    int nestado;");
			outputStream.println("    TElemIra(String simb, int est) {");
			outputStream.println("        simbolo = new String(simb);");
			outputStream.println("        nestado = est;");
			outputStream.println("    }");
			outputStream.println("    TElemIra() {");
			outputStream.println("        simbolo = new String();");
			outputStream.println("        nestado = -1;");
			outputStream.println("    }");
			outputStream.println("}");
			outputStream.println("class TEltoAccion {");
			outputStream.println("    String simbolo;");
			outputStream.println("    String tipo;");
			outputStream.println("    TRegla R;");
			outputStream.println("    int sig_estado;");
			outputStream.println("	TEltoAccion(String ptipo,String psimbolo,TRegla pr){");
			outputStream.println("		simbolo = new String(psimbolo);");
			outputStream.println("		tipo = new String(ptipo);");
			outputStream.println("		R = pr;");
			outputStream.println("	}	");
			outputStream.println("	TEltoAccion(String ptipo,String psimbolo,int psig_estado){");
			outputStream.println("		simbolo = new String(psimbolo);");
			outputStream.println("		tipo = new String(ptipo);");
			outputStream.println("		R = null;");
			outputStream.println("		sig_estado = psig_estado;");
			outputStream.println("	}	");
			outputStream.println("	TEltoAccion(){		");
			outputStream.println("		simbolo = new String();");
			outputStream.println("		tipo = new String();");
			outputStream.println("		R = null;");
			outputStream.println("	}");
			outputStream.println("}");
			outputStream.println("class EltoAcc {");
			outputStream.println("    String tipo;");
			outputStream.println("    TRegla R;");
			outputStream.println("    int sig_estado;");
			outputStream.println("	EltoAcc(String ptipo,TRegla pr,int psig_estado){");
			outputStream.println("		tipo = new String(ptipo);");
			outputStream.println("		R = pr;");
			outputStream.println("		sig_estado = psig_estado;");
			outputStream.println("	}	");
			outputStream.println("	EltoAcc(){");
			outputStream.println("		tipo = new String();");
			outputStream.println("		R = null;");
			outputStream.println("		sig_estado = -1;");
			outputStream.println("	}");
			outputStream.println("}");
			outputStream.println("public class parser{");
			outputStream.println("\tVector<TRegla> P;");
			outputStream.println("\tVector<Hashtable> tablaira;");
			outputStream.println("\tVector<Hashtable> tablaaccion;");
			outputStream.println("\tTTablaSimbolos ts;");
			outputStream.println("\tTEntrada ent;");
			outputStream.println("\tTGestorSemantico gs;");
			outputStream.println("\tpublic parser(String s) throws IOException{");
			outputStream.println("\t\tTRegla regla;");
			outputStream.println("\t\tP = new Vector<TRegla>();");
			g.escribereglas(outputStream);
			outputStream.println("\t\tinitablaira();");
			outputStream.println("\t\tinitablaaccion();");
			outputStream.println("\t\tent = new TEntrada();");
			outputStream.println("\t\tent.gordeCad(s);");
			outputStream.println("\t\ttry{");
			outputStream.println("\t\t	gs = new TGestorSemantico();");
			outputStream.println("\t\t} catch (Exception e) {");
            outputStream.println("\t\t	System.out.println(\"Error al inicializar el Gestor Semantico:\"+e.getMessage());");
			outputStream.println("\t\t}	");
			outputStream.println("\t}");
			outputStream.println("\tTElemIra getElemIra(String simb, int est) {");
			outputStream.println("\t	return new TElemIra(simb,est);");
			outputStream.println("\t}");
			outputStream.println("\tvoid initablaira(){");
			g.escribetablaira2(outputStream);
			outputStream.println("\t}");
			outputStream.println("\tTEltoAccion getEltoAccion(String ptipo,String psimbolo,TRegla pr){");
			outputStream.println("\t	return new TEltoAccion(ptipo,psimbolo,pr);");
			outputStream.println("\t}");
			outputStream.println("\tTEltoAccion getEltoAccion(String ptipo,String psimbolo,int psig_estado){");
			outputStream.println("\t	return new TEltoAccion(ptipo,psimbolo,psig_estado);");
			outputStream.println("\t}");
			outputStream.println("\tvoid initablaaccion() throws IOException {");
			g.escribetablaacc2(outputStream);
			outputStream.println("\t}");
			outputStream.println("\tpublic boolean parserascendente() throws IOException  {");
	        outputStream.println("\t\tStack<String> pila = new Stack<String>();");
	        outputStream.println("\t\tString tipo, strest;");
	        outputStream.println("\t\tTElemEnt sim, sim_ant;");
	        outputStream.println("\t\tHashtable ira;");
	        outputStream.println("\t\tHashtable acc;");
			outputStream.println("\t\tEltoAcc eltoacc;");
	        outputStream.println("\t\tint est, nest, longi;");
	        outputStream.println("\t\tTRegla regla;");
	        outputStream.println("\t\tstrest = \"\";");
		    outputStream.println("\t\tboolean error;");
			outputStream.println("\t\tthis.ts = new TTablaSimbolos();");
			outputStream.println("\t\terror = false;");
			outputStream.println("\t\tpila.push(\"0\");");
			outputStream.println("\t\tsim = ent.leersigterm(new TElemEnt(),this.ts);");
			outputStream.println("\t\tsim_ant = sim;");
			outputStream.println("\t\tdo {");
			outputStream.println("\t\t\test = Integer.parseInt(pila.peek());");
			outputStream.println("\t\t\tacc = this.tablaaccion.elementAt(est);");
			outputStream.println("\t\t\teltoacc = (EltoAcc)acc.get(sim.token);");
			outputStream.println("\t\t\tif (eltoacc !=null){");
			outputStream.println("\t\t\t\tif (eltoacc.tipo.equalsIgnoreCase(\"Desplazar\")) {");
			outputStream.println("\t\t\t\t\tnest = eltoacc.sig_estado;");
			outputStream.println("\t\t\t\t\tpila.push(String.valueOf(nest));");
			outputStream.println("\t\t\t\t\tsim_ant = sim;");
			outputStream.println("\t\t\t\t\tsim = ent.leersigterm(sim,this.ts);");
			outputStream.println("\t\t\t\t\t//System.out.println(pila.toString()+\"|\"+ent.faltaLeer()+\"|\");");
			outputStream.println("\t\t\t\t} else if (eltoacc.tipo.equalsIgnoreCase(\"Reducir\")) {");
			outputStream.println("\t\t\t\t\tregla = eltoacc.R;");
			outputStream.println("\t\t\t\t\tlongi = regla.rhs.size();");
			outputStream.println("\t\t\t\t\tfor (int i = 0; i < longi; i++)");
			outputStream.println("\t\t\t\t\t\tstrest = pila.pop();");
			outputStream.println("\t\t\t\t\tnest = Integer.parseInt(pila.peek());");
			outputStream.println("\t\t\t\t\tira = this.tablaira.elementAt(nest);");
			outputStream.println("\t\t\t\t\tif(ira.containsKey(regla.lhs)){");
			outputStream.println("\t\t\t\t\t\tnest = ((Integer)ira.get(regla.lhs)).intValue();");
			outputStream.println("\t\t\t\t\t\tpila.push(String.valueOf(nest));");
			outputStream.println("\t\t\t\t\t\t//System.out.println(pila.toString()+\"|\"+ent.faltaLeer()+\"| regla=\" + String.valueOf(regla.num));");
			outputStream.println("\t\t\t\t\t\tthis.gs.ejecutarAccionSemantica(regla.num,(sim_ant.get_atributo()>=0)?this.ts.leer(sim_ant.get_atributo()):sim_ant.token);");
			outputStream.println("\t\t\t\t\t}else{");
			outputStream.println("\t\t\t\t\t\tSystem.out.println(\"Falta ira en estado=\"+String.valueOf(nest)+\" con vble=\"+regla.lhs+\" despues reducir regla=\"+ String.valueOf(regla.num));");
			outputStream.println("\t\t\t\t\t\terror = true;");
			outputStream.println("\t\t\t\t\t}");
			outputStream.println("\t\t\t\t}");
			outputStream.println("\t\t\t} else {");
			outputStream.println("\t\t\t\tSystem.out.println(\"Falta entrada en accion estado=\"+String.valueOf(est)+\" con=\"+sim.token);");
			outputStream.println("\t\t\t\terror = true;");
			outputStream.println("\t\t\t}	");
			outputStream.println("\t\t} while (!error && !eltoacc.tipo.equalsIgnoreCase(\"Aceptar\"));");
			outputStream.println("\t\treturn error;");
			outputStream.println("\t}");
			outputStream.println("}");
		}finally{
			if (outputStream != null) {
                outputStream.close();
            }
		}	
	}
    public static void main(String[] args) throws IOException {
		boolean error;
		Gramatica g = new Gramatica();			
		g.inicializarGramatica(args[args.length-1]);
        g.calcularlr1();
		g.vertablaacc();
		escribeparser(g);
    }
}
