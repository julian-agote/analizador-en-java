package parser_kpa;
import java.util.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
class Util {
    public static boolean stringEmpty(String v) {
        return (v == null) || v.equals("");
    }
    public static String getString(String s, String def) {
        if (stringEmpty(s)) {
            return def;
        }
        return s;
    }
}   
class Var {
    private String nombre;
    private Float valf;
    private Integer vali;
	private char valc;
    private String tipo;
    ArrayList<Integer> registros;
    int[] anArray;
    private Hashtable<String, Integer> sig_uso;
	private Hashtable<String, Integer> nodo;
	int nodo;
    Var(String _nombre){
        this.nombre = _nombre;
        this.tipo = "osoa";
		this.sig_uso = new Hashtable<String, Integer>();
		this.nodo = new Hashtable<String, Integer>();
		this.registros = new ArrayList<Integer>();
    }   
    Var(String _nombre, String _tipo){
        this.nombre = _nombre;
        this.tipo = _tipo;
		this.sig_uso = new Hashtable<String, Integer>();
		this.nodo = new Hashtable<String, Integer>();
		this.registros = new ArrayList<Integer>();
    }   
    Var(String _nombre,float pval){
        this.nombre = _nombre;
        this.tipo = "erreala";
        this.valf = new Float(pval);
		this.sig_uso = new Hashtable<String, Integer>();
		this.nodo = new Hashtable<String, Integer>();
		this.registros = new ArrayList<Integer>();
    }   
    Var(String _nombre,int pval){
        this.nombre = _nombre;
        this.tipo = "osoa";
        this.vali = new Integer(pval);
		this.sig_uso = new Hashtable<String, Integer>();
		this.nodo = new Hashtable<String, Integer>();
		this.registros = new ArrayList<Integer>();
    }   
    Var(String _nombre,char pval){
        this.nombre = _nombre;
        this.tipo = "karakterea";
        this.valc = pval;
		this.sig_uso = new Hashtable<String, Integer>();
		this.nodo = new Hashtable<String, Integer>();
		this.registros = new ArrayList<Integer>();
    }   
    public String nombre() {
        return nombre;
    }
    public String tipo() {
        return tipo;
    }
    public Number valor() {
        if(tipo.equals("erreala"))
            return valf;
        else
            return vali;
    }
    public char devuelve_caracter() {
		return valc;
    }
    public void set_valor(float pvalor){
        this.valf = new Float(pvalor);
        if(this.tipo==null)
            this.tipo = "erreala";
    }   
    public void set_valor(int pvalor){
        this.vali = new Integer(pvalor);
        if(this.tipo==null)
            this.tipo = "osoa";
    }   
    public void set_valor(char pvalor){
        this.valc = pvalor;
        if(this.tipo==null)
            this.tipo = "karakterea";
    }   
    public void set_valor(int pos, int pvalor){
        this.anArray[pos-1] = pvalor;
    }   
    public Integer get_valor(int pos){
        return new Integer(this.anArray[pos-1]);
    }
    public void set_activo(int _i,int nbloque){
        this.sig_uso.put("B"+Integer.toString(nbloque),new Integer(_i));      
    }
    public void set_inactivo(int nbloque){
        this.sig_uso.put("B"+Integer.toString(nbloque),new Integer(-1));
    }
    public int activo(int nbloque){
        Integer n = sig_uso.get("B"+Integer.toString(nbloque));
		if(n==null)
			return -1;
		return n.intValue();
    }
    public int get_nodo(int nbloque){
        Integer n = nodo.get("B"+Integer.toString(nbloque));
		if(n==null)
			return -1;
		return n.intValue();
    }
    public void set_nodo(int nbloque,int _i){
        this.nodo.put("B"+Integer.toString(nbloque),new Integer(_i));      
    }
    public int devolver_registro(String reg_pref){
        if(registros.size()==0)
            return 0;
		if(Util.stringEmpty(reg_pref))
			return registros.get(0).intValue();
		else{
			if(registros.indexOf(Integer.valueOf(reg_pref.substring(1)))>=0)
				return Integer.valueOf(reg_pref.substring(1)).intValue();
		}
		return 0;
    }
    public void borrar_registro(String ldir){ // la vble. ya no esta en Rpos
        int arrunta = 0;    
        if (ldir.substring(0,1).equals("r")){
            try {
                arrunta = Integer.parseInt(ldir.substring(1));
                registros.remove(new Integer(arrunta));
            }   catch(NumberFormatException e) {
                return;
                }
        }
    }
    public void anadir_registro(Integer pos){
		if(this.registros.isEmpty())
			this.registros.add(pos);
		else if(this.registros.indexOf(pos)==-1)
            this.registros.add(pos);
    }
}
class Tvariables { // tabla de simbolos para la ejecucion del programa
    private ArrayList<Var> ts;
    Tvariables(){
        ts = new ArrayList<Var>();
    }
    int insertar(String p_lex) {
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(p_lex)){
                pos = i;
                break;
            }   
        if (pos == -1) {
            pos = ts.size();
            if(p_lex.charAt(0)>='0' && p_lex.charAt(0)<='9')
                if(p_lex.indexOf(".")>=0)
                    ts.add(new Var(p_lex,Float.valueOf(p_lex).floatValue()));
                else
                    ts.add(new Var(p_lex,Integer.valueOf(p_lex).intValue()));
            else    
                ts.add(new Var(p_lex));
        }
        return pos;
    }
    int insertar(String p_lex,String p_tipo) {
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(p_lex)){
                pos = i;
                break;
            }   
        if (pos == -1) {
            pos = ts.size();
            ts.add(new Var(p_lex,p_tipo));
        }
        return pos;
    }
    String leer(int pos){
        if(pos<ts.size())
            return (String)ts.get(pos).nombre();
        return null;    
    }
    String leer_valor(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
			if(ts.get(pos).tipo().equals("karakterea")){
				char data[]={'a'};
				data[0]= ts.get(pos).devuelve_caracter();
				return new String(data);
			}else
				return ts.get(pos).valor().toString();
        return nombre;  
    }
    String leer_tipo(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            return ts.get(pos).tipo();
        return nombre;  
    }
    String leer_valor(String nombre,int ind){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            return ts.get(pos).get_valor(ind).toString();
        return nombre;  
    }
	void activar(String nombre, int nbloque,int cuad){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).set_activo(cuad,nbloque);
	}	
    void desactivar(String nombre, int nbloque){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).set_inactivo(nbloque);
    }
    boolean esta_activo(String nombre,int nbloque){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            return (ts.get(pos).activo(nbloque)>=0?true:false);
        return false;
    }   
    boolean esta_indefinido(String nombre,int nbloque){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            return (ts.get(pos).get_nodo(nbloque)>=0?true:false);
        return false;
    }   
    void apuntar_nodo(String nombre,int nbloque,int indice){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).set_nodo(nbloque,indice);
    }   
    int obtener_nodo(String nombre,int nbloque){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            return ts.get(pos).get_nodo(nbloque);
		return -1;
    }   
    void guardar_valor(String nombre, String valor){
        int pos = -1;
        Var v;
        Integer a;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1){
            v = ts.get(pos);
            if(valor.charAt(0)>='0' && valor.charAt(0)<='9')
                if(valor.indexOf(".")>=0)
                    v.set_valor(Float.valueOf(valor).floatValue());
                else    
                    v.set_valor(Integer.valueOf(valor).intValue());
			else
				v.set_valor(valor.charAt(0));	
            ts.set(pos,v);
        }else
            if(valor.charAt(0)>='0' && valor.charAt(0)<='9')
                if(valor.indexOf(".")>=0)
                    ts.add(new Var(nombre, Float.valueOf(valor).floatValue()));
                else    
                    ts.add(new Var(nombre, Integer.valueOf(valor).intValue()));
			else
				ts.add(new Var(nombre, valor.charAt(0)));
    }
    void guardar_valor(String nombre, int ind, String valor){
        int pos = -1;
        Var v;
        Integer a;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1){
            v = ts.get(pos);
            v.set_valor(ind,Integer.valueOf(valor).intValue());
            ts.set(pos,v);
        }
    }
    String obtener_pos_en_curso(String nombre,String reg_pref){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            if(ts.get(pos).devolver_registro("")>0)
				if(Util.stringEmpty(reg_pref))
					return new String("r"+Integer.toString(ts.get(pos).devolver_registro("")));
				else{
					int rbuscado = ts.get(pos).devolver_registro(reg_pref);
					if(rbuscado>0)
						return reg_pref;
					else
						return new String("r"+Integer.toString(ts.get(pos).devolver_registro("")));
				}
            else
                return nombre;
        return nombre;    
    }  
    boolean esta_en_reg(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            if(ts.get(pos).devolver_registro("")>0)
                return true;
        return false;              
    }    
    void quitar_desc_direcc(String nombre, String ldir){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).borrar_registro(ldir);
    }
    void actualiza_descrip_direcc(String nombre, String ldir){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        int arrunta = 0;    
        if (pos != -1 && ldir.substring(0,1).equals("r")){
            try {
                arrunta = Integer.parseInt(ldir.substring(1));
                ts.get(pos).anadir_registro(new Integer(arrunta));
            }   catch(NumberFormatException e) {
                return;
                }
        }
    }
    Iterator<Var> devuelve_vbles(){
		return ts.iterator();
	}    
}
class Reg { // para la pila semantica
    private String nombre,lugar,codigo,quad,cierto,falso;
    Reg(String _nombre, String _lugar, String _codigo, String _quad, String _cierto, String _falso) {
        this.nombre = _nombre;
        this.lugar = _lugar;
        this.codigo = _codigo;
        this.quad = _quad;
        this.cierto = _cierto;
        this.falso = _falso;
    }
    Reg(String _nombre) {
        this.nombre = _nombre;
        this.lugar = null;
        this.codigo = "";
        this.quad = null;
        this.cierto = null;
        this.falso = null;
    }
    public String nombre() {
        return nombre;
    }
    public String lugar() {
        return lugar;
    }
    public String codigo() {
        return Util.getString(codigo, "");
    }
    public String quad() {
        return quad;
    }
    public String cierto() {
        return Util.getString(cierto, "");
    }
    public String falso() {
        return Util.getString(falso, "");
    }
}
class Cuad { // para la tabla de cuadruplos
    private String op,arg1,arg2,res;
    Cuad(String _op, String _arg1, String _arg2, String _res) {
        this.op = _op;
        this.arg1 = _arg1;
        this.arg2 = _arg2;
        this.res = _res;
    }
    public String op() {
        return op;
    }
    public String arg1() {
        return arg1;
    }
    public String arg2() {
        return arg2;
    }
    public String res() {
        return res;
    }
    public void set_res(String _res) {
        this.res = _res;
    }
    public void set_op(String _op) {
        this.op = _op;
    }
}
class NodoGDA{
	String etiqueta;
	ArrayList<String> identificadores;
	int hijo_izdo, hijo_dcho;
	NodoGDA(String _etiq){
		this.etiqueta = _etiq;
		this.identificadores = new ArrayList<String>();
		this.hijo_izdo = -1;
		this.hijo_dcho = -1;
	}
}
class Bloque_basico{
    int nbloque,lider;
    ArrayList<String> cuadruplos,succ,pred;
	ArrayList<NodoGDA> gda;
    Bloque_basico(int _b,int _lider){
        this.nbloque = _b;
        this.lider = _lider;
        this.cuadruplos = new ArrayList<String>();
        this.succ = new ArrayList<String>();
        this.pred = new ArrayList<String>();
		this.gda = new ArrayList<NodoGDA>();
    }
    Bloque_basico(){
        this.cuadruplos = new ArrayList<String>();
        this.succ = new ArrayList<String>();
        this.pred = new ArrayList<String>();
		this.gda = new ArrayList<NodoGDA>();
    }
    void aniadir_cuad(String _n){
        this.cuadruplos.add(_n);
    }
    void aniadir_pred(int _n){
        this.pred.add(String.valueOf(_n));
    }
    void aniadir_succ(int _n){
        this.succ.add(String.valueOf(_n));
    }
	int aniadir_hoja(String _etiq){
		this.gda.add(new NodoGDA(_etiq));
		return (this.gda.size()-1);
	}
	boolean esta_indeterminado(String _op,int hi,int hd){
		return gda.contains(new NodoGDA(_op,hi,hd));
	}
	int encontrar_nodo(String _op,int hi,int hd){
		return gda.indexOf(new NodoGDA(_op,hi,hd));
	}
	int aniadir_nodo(String _op,int hi,int hd){
		gda.add(new NodoGDA(_op,hi,hd));
		return (gda.size()-1);
	}	
}
class Descriptor_reg{
    int posicion;
    ArrayList<String> contiene;
    Descriptor_reg(int _pos){
        this.posicion = _pos;
        this.contiene = new ArrayList<String>();
    }
    boolean vacio(){
        return this.contiene.isEmpty();
    }
}
public class TGestorSemantico{
    PrintWriter outputStream = null;
    Stack<Reg> pila_sem = null;
    ArrayList<Cuad> tcuad;
    int indice = 0;
    Tvariables variables; // de momento hay un solo algoritmo, en el momento en que sean mas, habra que utilizar una pila
    ArrayList<Descriptor_reg> treg;
    String opcion;
	Var vble;
    TGestorSemantico(String _opc) throws IOException {
        pila_sem = new Stack<Reg>();
        outputStream = new PrintWriter(new FileWriter("a.out"));
        tcuad = new ArrayList<Cuad>();
        variables = new Tvariables();
        treg = new ArrayList<Descriptor_reg>();
        for(int i=1;i<32;i++){
            treg.add(new Descriptor_reg(i));
        }
		opcion = new String(_opc);
    }
    String gen(String op, String arg1, String arg2, String res){
        tcuad.add(new Cuad(op,arg1,arg2,res));
        return String.valueOf(tcuad.size()-1);
    }   
    String nuevotemp(){
        indice++;
        return ("t"+String.valueOf(indice));
    }
    String primera_pos(String lista){
        String indices[];
        if(!Util.stringEmpty(lista)){
            indices = lista.split(",");
            for (int j = 0; j < indices.length; j++)
                if(!Util.stringEmpty(indices[j]))
                    return indices[j];
        }
        return null;    
    }
    void asocia(String lista,String pos){
        Cuad q;
        String ind_cuad[];
        if(!Util.stringEmpty(lista)){
            ind_cuad = lista.split(",");
            for (int j = 0; j < ind_cuad.length; j++)
                if(!Util.stringEmpty(ind_cuad[j])){
                    q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[j]).intValue());
                    q.set_res(pos);
                    tcuad.set(Integer.valueOf(ind_cuad[j]).intValue(),q);
                }   
        }   
    }
    void encontrar_lazos(ArrayList<Bloque_basico> tbloques,int[] lazo, int n, ArrayList<String> listaLazos){ // n marca la ultima posicion del camino recorrido
        Bloque_basico bloque = new Bloque_basico();
        String sucesor,cad;
        boolean lazo_interno;
        for(int k=0;k<tbloques.size();k++){ // encontrar el bloque en la ultima posicion del camino recorrido
            bloque = tbloques.get(k);
            if(bloque.nbloque == lazo[n])
                break;
        }
        for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){ // para los sucesores de ese bloque
            sucesor = (String)iter.next();
            if(lazo[0]==Integer.valueOf(sucesor).intValue()){ // hay un lazo,se añade a listaLazos
                cad = new Integer(lazo[0]).toString();
                for(int k=1;k<=n;k++) cad+="->"+(new Integer(lazo[k]).toString());
                listaLazos.add(cad+"->"+new Integer(lazo[0]).toString());
            }else if(!Util.stringEmpty(sucesor)){
                // comprobar si hay un lazo interno en cuyo caso se abandona la busqueda
                lazo_interno = false;
                for(int i=0;i<n;i++)
					if(lazo[i]==Integer.valueOf(sucesor).intValue()){
						lazo_interno = true;
						break;
					}
                if(!lazo_interno){
					lazo[n+1]=Integer.valueOf(sucesor).intValue(); // se van añadiendo nodos segun se recorre el camino entre bloques
					encontrar_lazos(tbloques,lazo,n+1,listaLazos);
                }
            }
        }
    }
    boolean no_esta(String c, ArrayList<String> b){ // identifica si dos caminos representan el mismo lazo
        String aux[],cad;
        HashSet<String> asec,bsec;
        aux=c.split("->");
        asec = new HashSet<String>();
        for(int k=0;k<aux.length;k++)
            asec.add(aux[k]);
        for(Iterator<String> iter=b.iterator();iter.hasNext();){
            cad = (String)iter.next();
            aux = cad.split("->");
            bsec = new HashSet<String>();
            for(int k=0;k<aux.length;k++)
                bsec.add(aux[k]);
            if(asec.equals(bsec))
                return false;
        }
        return true;
    }
	boolean hay_volcado_registro(){
		// si no hay ningun registro vacio, hay que elegir uno ocupado y volcar previamente su contenido a memoria
		boolean hay_uno_vacio = false;
        for(int i=1;i<treg.size();i++)
            if(treg.get(i).vacio())
				hay_uno_vacio = true;
		return (!hay_uno_vacio);
	}	
    String obtenreg(String nombre, int nbloque, String etiq){
		String vble; 
		boolean primera_inst = true;
		String etiqueta = new String(etiq);
		char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
        // devolver un registro vacio si hay alguno
        for(int i=1;i<treg.size();i++)
            if(treg.get(i).vacio()){
				treg.get(i).contiene.add(nombre);
                return ("r"+String.valueOf(i));
			}	
        // si no se puede, encuentrese un registro ocupado R, almacenese el valor de R en una posicion de memoria
        // MOV R,M actualicese el descriptor de direcciones de M y devuelvase R
        for(int i=1;i<treg.size();i++){
			boolean enc = false;
            for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
                vble = (String)iter.next();
                if(variables.esta_activo(vble,nbloque))
                    enc = true;
			}
			if(!enc){
				for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
					vble = (String)iter.next();
					outputStream.println(etiqueta+"st r"+String.valueOf(i)+", "+vble);
					if(primera_inst){
						primera_inst = false;
						etiqueta = new String(tabulador);
					}											
					variables.quitar_desc_direcc(vble,"r"+String.valueOf(i)); // la vble. ya no esta en Ri
				}    
				treg.get(i).contiene.clear(); // se pone al registro que ya no contiene a ninguna vble, excepto al que se pasa por parametro
				treg.get(i).contiene.add(nombre);				
				// quitar a nombre de todos los otros registros	
				for(int k=1;k<treg.size();k++)
					if(k!=i){
						int j = treg.get(k).contiene.indexOf(nombre);
						if (j>=0)
							treg.get(k).contiene.remove(j);
					}	
				return ("r"+String.valueOf(i));
			}
        } 
        if(!treg.get(1).vacio()){
			for(Iterator<String> iter=treg.get(1).contiene.iterator();iter.hasNext();){ // si no se ha encontrado ningun registro ocupado con vbles sin uso en el bloque se coge el primer registro
				vble = (String)iter.next();
				outputStream.println(etiqueta+"st r1, "+vble);
				if(primera_inst){
					primera_inst = false;
					etiqueta = new String(tabulador);
				}											
				variables.quitar_desc_direcc(vble,"r1"); // la vble. ya no esta en R1
			}  		
			treg.get(1).contiene.clear();	
		}
		treg.get(1).contiene.add(nombre);
		// quitar a nombre de todos los otros registros	
        for(int i=2;i<treg.size();i++){
			int j = treg.get(i).contiene.indexOf(nombre);
			if (j>=0)
				treg.get(i).contiene.remove(j);
		}	
		return ("r1");
    }
	boolean hay_volcado_r1(){
		return (!treg.get(1).vacio());
	}	
	void almacenar_reg_r1(String nombre,String etiq){
		String vble; 
		boolean primera_inst = true;
		String etiqueta = new String(etiq);
		char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
		if(!treg.get(1).vacio()){
			for(Iterator<String> iter=treg.get(1).contiene.iterator();iter.hasNext();){ 
				vble = (String)iter.next();
				outputStream.println(etiqueta+"st r1, "+vble);
				if(primera_inst){
					primera_inst = false;
					etiqueta = new String(tabulador);
				}											
				System.out.println("st r1, "+vble);
				variables.quitar_desc_direcc(vble,"r1"); // la vble. ya no esta en R1
			}  
			treg.get(1).contiene.clear();	
		}
		treg.get(1).contiene.add(nombre);
		// quitar a nombre de todos los otros registros	
        for(int i=2;i<treg.size();i++){
			if(!treg.get(i).vacio()){
				int j = treg.get(i).contiene.indexOf(nombre);
				if (j>=0)
					treg.get(i).contiene.remove(j);
			}
		}			
	}
	
    void ejecutarAccionSemantica(int numRegla,String ps)  throws IOException {
        Reg arg1,arg2,op,ini,arg3,op2;
        StringBuffer sb;
        String t,cierto,falso,cod;
        Cuad q;
        String ind_cuad[],aux_cuad[],ultimo_quad;
        ArrayList<Bloque_basico> tbloques;
        int[] lideres; int n=0,i;
        Bloque_basico bloque, bbloque, bloque_pred;
        Iterator<String> iter_bl;                
        try {
            switch (numRegla) {
                case 1: arg1 = pila_sem.pop();
                        cod = gen("RET",null,null,null);
                        aux_cuad = arg1.codigo().split(",");
                        ind_cuad = new String[aux_cuad.length+1];
                        System.arraycopy(aux_cuad,0,ind_cuad,0,aux_cuad.length);
                        ind_cuad[aux_cuad.length] = cod;
                        for (int j = 0; j < ind_cuad.length; j++)
                            if(!Util.stringEmpty(ind_cuad[j])){
                                q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[j]).intValue());
                                if((q.op().equals("GOTO") || (!Util.stringEmpty(q.op()) && q.op().length()>=2 && q.op().substring(0,2).equals("IF"))) && Util.stringEmpty(q.res())){
                                    q.set_res(cod);
                                    tcuad.set(Integer.valueOf(ind_cuad[j]).intValue(),q);
                                }   
                            }
                        // escribir el programa en lenguaje intermedio de cuadruplos    
                        for (int j = 0; j < ind_cuad.length; j++)
                            if(!Util.stringEmpty(ind_cuad[j])){
                                q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[j]).intValue());
                                if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/"))
                                    outputStream.println(ind_cuad[j]+": "+q.res()+":="+q.arg1()+" "+q.op()+" "+q.arg2());
                                else if(q.op().equals(":=")) 
                                    outputStream.println(ind_cuad[j]+": "+q.res()+":="+q.arg1());
                                else if(q.op().substring(0,2).equals("IF")) 
                                    outputStream.println(ind_cuad[j]+": IF "+q.arg1()+" "+q.op().substring(2)+" "+q.arg2()+" GOTO "+q.res());   
                                else if(q.op().equals("GOTO")) 
                                    outputStream.println(ind_cuad[j]+": GOTO "+q.res());
                                else if(q.op().equals("INPUT")) 
                                    outputStream.println(ind_cuad[j]+": INPUT "+q.arg1());
                                else if(q.op().equals("WRITE")) 
                                    outputStream.println(ind_cuad[j]+": WRITE "+q.arg1());
                                else if(q.op().equals("WRITEC")) 
                                    outputStream.println(ind_cuad[j]+": WRITE \""+q.arg1()+"\"");
                                else if(q.op().equals("RET")) 
                                    outputStream.println(ind_cuad[j]+": RET");
                                else if(q.op().equals("[]=")) 
                                    outputStream.println(ind_cuad[j]+": "+q.arg1()+"["+q.arg2()+"]:="+q.res());
                                else if(q.op().equals("=[]")) 
                                    outputStream.println(ind_cuad[j]+": "+q.res()+":="+q.arg1()+"["+q.arg2()+"]");
                                else outputStream.println(ind_cuad[j]+": op desc "+q.op()+" arg1="+Util.getString(q.arg1(),"")+" arg2="+Util.getString(q.arg2(),"")+" res="+Util.getString(q.res(),""));
                            }   
                        outputStream.close();
                        // ejecutar el programa en lenguaje intermedio
                        int j = 0;
                        do{
                            if(!Util.stringEmpty(ind_cuad[j])){
                                //System.out.println("Ejecutar instr. "+ind_cuad[j]+" "+String.valueOf(j));
                                q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[j]).intValue());
                                if(q.op().equals("+")){
                                    if(variables.leer_valor(q.arg1()).indexOf(".")>=0){
                                        float a = Integer.valueOf(variables.leer_valor(q.arg1())).floatValue();
                                        float b = Integer.valueOf(variables.leer_valor(q.arg2())).floatValue();
                                        variables.guardar_valor(q.res(),String.valueOf(a+b));
                                    }else{
                                        int a = Integer.valueOf(variables.leer_valor(q.arg1())).intValue();
                                        int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
                                        variables.guardar_valor(q.res(),String.valueOf(a+b));
                                    }
                                    j++;
                                }else if(q.op().equals("-")){
                                    if(variables.leer_valor(q.arg1()).indexOf(".")>=0){
                                        float a = Integer.valueOf(variables.leer_valor(q.arg1())).floatValue();
                                        float b = Integer.valueOf(variables.leer_valor(q.arg2())).floatValue();
                                        variables.guardar_valor(q.res(),String.valueOf(a-b));
                                    }else{
                                        int a = Integer.valueOf(variables.leer_valor(q.arg1())).intValue();
                                        int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
                                        variables.guardar_valor(q.res(),String.valueOf(a-b));
                                    }
                                    j++;
                                }else if(q.op().equals("/")){
                                    if(variables.leer_valor(q.arg1()).indexOf(".")>=0){
                                        float a = Float.valueOf(variables.leer_valor(q.arg1())).floatValue();
                                        float b = Float.valueOf(variables.leer_valor(q.arg2())).floatValue();
                                        variables.guardar_valor(q.res(),new Float(a/b).toString());
                                    }else{
                                        int a = Integer.valueOf(variables.leer_valor(q.arg1())).intValue();
                                        int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
                                        variables.guardar_valor(q.res(),new Integer(a/b).toString());
                                    }
                                    j++;
                                }else if(q.op().equals("*")){
                                    if(variables.leer_valor(q.arg1()).indexOf(".")>=0){
                                        float a = Float.valueOf(variables.leer_valor(q.arg1())).floatValue();
                                        float b = Float.valueOf(variables.leer_valor(q.arg2())).floatValue();
                                        variables.guardar_valor(q.res(),new Float(a*b).toString());
                                    }else{
                                        int a = Integer.valueOf(variables.leer_valor(q.arg1())).intValue();
                                        int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
                                        variables.guardar_valor(q.res(),new Integer(a*b).toString());
                                    }
                                    j++;
                                }else if(q.op().equals("[]=")){
                                    int a = Integer.valueOf(variables.leer_valor(q.res())).intValue();
                                    int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
                                    variables.guardar_valor(q.arg1(),b,new Integer(a).toString());
                                    j++;
                                }else if(q.op().equals("=[]")){
                                    int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
                                    System.out.println("indice="+q.arg2()+" valor="+b);
                                    int a = Integer.valueOf(variables.leer_valor(q.arg1(),b)).intValue();
                                    variables.guardar_valor(q.res(),new Integer(a).toString());
                                    j++;                                    
                                }else if(q.op().equals("INPUT")){
									char[] tmp = new char[10];
									int len=0;
									do{
										tmp[len] = (char) System.in.read();
										len++;
									}while(tmp[len-1]!='\n');
                                    variables.guardar_valor(q.arg1(),new String(tmp).trim());
                                    j++;
                                }else if(q.op().equals("WRITEC")){
                                    System.out.println(q.arg1());
                                    j++;
                                }else if(q.op().equals("WRITE")){
                                    System.out.println(variables.leer_valor(q.arg1()));
                                    j++;
                                }else if(q.op().equals(":=")){
                                    variables.guardar_valor(q.res(),variables.leer_valor(q.arg1()));
                                    j++;
                                }else if(q.op().substring(0,2).equals("IF")){
                                    if(variables.leer_valor(q.arg1()).indexOf(".")<0 || variables.leer_tipo(q.arg1()).equals("karakterea")){ // en principio contendra un entero o un caracter, se puede preguntar por el tipo de la variable
									// leer_valor devuelve siempre un String, luego hacemos las conversiones de tipo, para las comparaciones
										if (variables.leer_tipo(q.arg1()).equals("karakterea")){
											char a = variables.leer_valor(q.arg1()).charAt(0);
											char b = variables.leer_valor(q.arg2()).charAt(0);
											//System.out.println("entero a="+String.valueOf(a)+" b="+String.valueOf(b)+" op"+q.op().substring(2)+" goto="+q.res());
											if(q.op().substring(2).trim().equalsIgnoreCase(">")){
												if(a>b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].trim().equals(q.res().trim())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase(">=")){
												if(a>=b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].trim().equals(q.res().trim())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().compareToIgnoreCase("<")==0){
												if(a<b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(!Util.stringEmpty(ind_cuad[k]) && ind_cuad[k].trim().equals(q.res().trim())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase("<=")) {
												if(a<=b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].equals(q.res())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase("=")) {
												if(a==b) {
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].equals(q.res())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase("<>")) {
												if(a!=b) {
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].equals(q.res())){
															j=k;
															break;
														}   
												}else j++;
											} 											
										}else{
											int a = Integer.valueOf(variables.leer_valor(q.arg1())).intValue();
											int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
											//System.out.println("entero a="+String.valueOf(a)+" b="+String.valueOf(b)+" op"+q.op().substring(2)+" goto="+q.res());
											if(q.op().substring(2).trim().equalsIgnoreCase(">")){
												if(a>b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].trim().equals(q.res().trim())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase(">=")){
												if(a>=b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].trim().equals(q.res().trim())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().compareToIgnoreCase("<")==0){
												if(a<b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(!Util.stringEmpty(ind_cuad[k]) && ind_cuad[k].trim().equals(q.res().trim())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase("<=")) {
												if(a<=b){ 
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].equals(q.res())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase("=")) {
												if(a==b) {
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].equals(q.res())){
															j=k;
															break;
														}   
												}else j++;
											}else if(q.op().substring(2).trim().equalsIgnoreCase("<>")) {
												if(a!=b) {
													for(int k=0;k<ind_cuad.length;k++)
														if(ind_cuad[k].equals(q.res())){
															j=k;
															break;
														}   
												}else j++;
											} 
										}																	
									}else{
                                        float a = Float.valueOf(variables.leer_valor(q.arg1())).floatValue();
                                        float b = Float.valueOf(variables.leer_valor(q.arg2())).floatValue();
                                        //System.out.println("a="+String.valueOf(a)+" b="+String.valueOf(b)+" op"+q.op().substring(2)+" goto="+q.res());
                                        if(q.op().substring(2).equals(">")) 
                                            if(a>b){ 
                                                for(int k=0;k<ind_cuad.length;k++)
                                                    if(ind_cuad[k].equals(q.res())){
                                                        j=k;
                                                        break;
                                                    }   
                                            }else j++;
                                        else if(q.op().substring(2).equals(">=")) 
                                            if(a>=b){ 
                                                for(int k=0;k<ind_cuad.length;k++)
                                                    if(ind_cuad[k].equals(q.res())){
                                                        j=k;
                                                        break;
                                                    }   
                                            }else j++;
                                        else if(q.op().substring(2).equals("<")) 
                                            if(a<b){ 
                                                for(int k=0;k<ind_cuad.length;k++)
                                                    if(ind_cuad[k].equals(q.res())){
                                                        j=k;
                                                        break;
                                                    }   
                                            }else j++;
                                        else if(q.op().substring(2).equals("<=")) 
                                            if(a<=b) {
                                                for(int k=0;k<ind_cuad.length;k++)
                                                    if(ind_cuad[k].equals(q.res())){
                                                        j=k;
                                                        break;
                                                    }   
                                            }else j++;
                                        else if(q.op().substring(2).equals("=")) 
                                            if(a==b) {
                                                for(int k=0;k<ind_cuad.length;k++)
                                                    if(ind_cuad[k].equals(q.res())){
                                                        j=k;
                                                        break;
                                                    }   
                                            }else j++;
                                        else if(q.op().substring(2).equals("<>")) 
                                            if(a!=b) {
                                                for(int k=0;k<ind_cuad.length;k++)
                                                    if(ind_cuad[k].equals(q.res())){
                                                        j=k;
                                                        break;
                                                    }   
                                            }else j++;                                      
                                    }       
                                }else if(q.op().trim().equalsIgnoreCase("GOTO")){                                    
                                        for(int k=0;k<ind_cuad.length;k++)
                                            if(ind_cuad[k].equals(q.res())){
                                                j=k;
                                                break;
                                            }   
                                }else if(q.op().equals("RET")){
                                    j++;
                                    break;      
                                }else{ 
                                    System.out.println("Operador sin identificar ="+q.op());    
                                    break;
                                }   
                            }else j++;      
                        }while(j<ind_cuad.length);
                        //Particionar en bloques basicos
                        tbloques = new ArrayList<Bloque_basico>();
                        // obtener la lista de lideres, la 1a. instr., los destinos de un salto, y los que van despues de un salto
                        lideres=new int[ind_cuad.length];
                        for (j = 0; j < ind_cuad.length; j++)
                            if(!Util.stringEmpty(ind_cuad[j])){
                                lideres[n++]= Integer.valueOf(ind_cuad[j]).intValue(); // la 1a. instr. 
                                break;
                            }   
                        for(;j<ind_cuad.length;)
                            if(!Util.stringEmpty(ind_cuad[j])){
                                i = Integer.valueOf(ind_cuad[j]).intValue();
                                q = (Cuad)tcuad.get(i); 
                                if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF"))){
                                    boolean enc = false;
                                    for(int k=0;k<n && !enc;k++)
                                        if(lideres[k]==Integer.valueOf(q.res()).intValue())
                                            enc=true;
                                    if(!enc)
                                        lideres[n++]=Integer.valueOf(q.res()).intValue(); // los destinos de un salto
                                    for(j=j+1;j<ind_cuad.length;j++)
                                        if(!Util.stringEmpty(ind_cuad[j])){
                                            enc = false;
                                            for(int k=0;k<n && !enc;k++)
                                                if(lideres[k]==Integer.valueOf(ind_cuad[j]).intValue())
                                                    enc=true;
                                            if(!enc)                                            
                                                lideres[n++]=Integer.valueOf(ind_cuad[j]).intValue(); // los que van despues de un salto
                                            break;
                                        }   
                                }else j++;
                            }else j++;
                        //lideres = java.util.Arrays.sort(lideres); 
                        String cad = new String("lideres=");
                        for(int k=0;k<n;k++) cad+=","+String.valueOf(lideres[k]);
                        System.out.println(cad);
                        // cada bloque basico esta formado por un lider hasta el siguiente lider sin incluirlo
                        int b=0,lider=lideres[0],n_cuad = 1;
                        bloque = new Bloque_basico(b,lider);
                        for(j=0;j<ind_cuad.length;j++)
                            if(!Util.stringEmpty(ind_cuad[j])&& lider==Integer.valueOf(ind_cuad[j]).intValue())
                                break;
                        for(j=j+1;j<ind_cuad.length;)
                            if(!Util.stringEmpty(ind_cuad[j])){
                                // recorrer los cuadruplos hasta el siguiente lider
                                boolean esta = false;
                                for(int k=0;k<n && !esta;k++)
                                    if(lideres[k]==Integer.valueOf(ind_cuad[j]).intValue())
                                        esta = true;
                                if(!esta){
                                    n_cuad++;
                                    bloque.aniadir_cuad(ind_cuad[j]);
                                    j++;
                                }else{
                                    System.out.println("B"+b+" "+lider+" "+n_cuad);
                                    tbloques.add(bloque);
                                    b++;n_cuad=1;
                                    lider = Integer.valueOf(ind_cuad[j]).intValue();
                                    bloque = new Bloque_basico(b,lider);
                                    j++;
                                }                               
                            }else j++;
                        System.out.println("B"+b+" "+lider+" "+n_cuad);
                        tbloques.add(bloque);                           
                        // para saber los sucesores de un bloque, se va al ultimo cuadruplo y se mira a que cuadruplo va el salto y 
                        // el bloque al que pertenece el destino del salto
                        for(j=0;j<tbloques.size();j++){
                            bloque = tbloques.get(j);
                            ultimo_quad = String.valueOf(bloque.lider);
                            for(Iterator<String> iter=bloque.cuadruplos.iterator();iter.hasNext();)
                                ultimo_quad=(String)iter.next();
                            q=(Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                            if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF"))){
                                // obtener el bloque destino del salto
                                for(int k=0;k<tbloques.size();k++){
                                    bbloque = tbloques.get(k);
                                    if(bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                        bbloque.aniadir_pred(bloque.nbloque);
                                        bloque.aniadir_succ(bbloque.nbloque);
                                        break;
                                    }
                                }
                                if(q.op().substring(0,2).equals("IF")){
                                    // ver cual es el cuadruplo a continuacion de ultimo_quad
                                    for (int k = 0; k < ind_cuad.length; k++)
                                        if(!Util.stringEmpty(ind_cuad[k]) && ind_cuad[k].equals(ultimo_quad)){
                                            do{k++;}while(k<ind_cuad.length && Util.stringEmpty(ind_cuad[k]));
                                            ultimo_quad=ind_cuad[k];
                                            break;
                                        }
                                    // repetir buscar el bloque, en este caso en lugar de q.res() con ultimo_quad
                                    for(int k=0;k<tbloques.size();k++){
                                        bbloque = tbloques.get(k);
                                        if(bbloque.lider==Integer.valueOf(ultimo_quad).intValue()){
                                            bbloque.aniadir_pred(bloque.nbloque);
                                            bloque.aniadir_succ(bbloque.nbloque);
                                            break;
                                        }
                                    }                                   
                                }
                            }else if(j<tbloques.size()-1){
                                    // ver cual es el cuadruplo a continuacion de ultimo_quad
                                    for (int k = 0; k < ind_cuad.length; k++)
                                        if(!Util.stringEmpty(ind_cuad[k]) && ind_cuad[k].equals(ultimo_quad)){
                                            do{k++;}while(k<ind_cuad.length && Util.stringEmpty(ind_cuad[k]));
                                            ultimo_quad=ind_cuad[k];
                                            break;
                                        }
                                    // repetir buscar el bloque, en este caso en lugar de q.res() con ultimo_quad
                                    for(int k=0;k<tbloques.size();k++){
                                        bbloque = tbloques.get(k);
                                        if(bbloque.lider==Integer.valueOf(ultimo_quad).intValue()){
                                            bbloque.aniadir_pred(bloque.nbloque);
                                            bloque.aniadir_succ(bbloque.nbloque);
                                            break;
                                        }
                                    }                                                                   
                            }
                        }
                        // mostrar la inf. de los bloques basicos y el grafo de flujo
                        String grafo = new String("Graph[{");
                        for(j=0;j<tbloques.size();j++){
                            bloque = tbloques.get(j);
                            String lcuad = String.valueOf(bloque.lider);
                            for(Iterator<String> iter=bloque.cuadruplos.iterator();iter.hasNext();)
                                lcuad+=","+(String)iter.next();
                            for(ListIterator<String> iter=bloque.cuadruplos.listIterator();iter.hasPrevious();){
                                ultimo_quad = (String)iter.previous();
                                q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                if(q.op().equals(":=") || q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/") || q.op().equals("=[]")){
                                    variables.desactivar(q.res(),bloque.nbloque);
									if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
										variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
									if(!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))
										variables.activar(q.arg2(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                }else if(q.op().equals("IF")){
									if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
										variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
									if(!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))
										variables.activar(q.arg2(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());									
								}else if(q.op().equals("[]=")){
									variables.desactivar(q.arg1(),bloque.nbloque);
									if(!(q.res().charAt(0)>='0' && q.res().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.res())))
										variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
									if(!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))
										variables.activar(q.arg2(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());									
								}else if(q.op().equals("WRITE")){
									variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
								}else if(q.op().equals("INPUT"))
									variables.desactivar(q.arg1(),bloque.nbloque);
                            }
                            String lpred = new String();
                            for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext();)
                                lpred+=","+(String)iter.next();
                            String lsucc = new String();
                            for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
                                String sucesor = (String)iter.next();
                                if(!Util.stringEmpty(lsucc))
                                    lsucc+=","+sucesor;
                                else
                                    lsucc=sucesor;
                                if(grafo.length()>7)
                                    grafo+=","+bloque.nbloque+"->"+sucesor;
                                else
                                    grafo+=bloque.nbloque+"->"+sucesor;
                            }   
                            System.out.println("B"+bloque.nbloque+" cuad="+lcuad+" pred="+lpred+" succ="+lsucc);
							// Construccion de un GDA
							ultimo_quad = String.valueOf(bloque.lider);
							iter_bl = bloque.cuadruplos.iterator();
							int n1,n2;
							while(!Util.stringEmpty(ultimo_quad)){
								q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
								if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/")){
									if(variables.esta_indefinido(q.arg1(),bloque.nbloque)){
										// creese una hoja etiquetada
										n1 = bloque.aniadir_hoja(q.arg1());
										variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);
									}else
										n1 = variables.obtener_nodo(q.arg1(),bloque.nbloque);
									if(variables.esta_indefinido(q.arg2(),bloque.nbloque)){
										// creese una hoja etiquetada
										n2 = bloque.aniadir_hoja(q.arg2());
										variables.apuntar_nodo(q.arg2(),bloque.nbloque,n2);
									}else
										n2 = variables.obtener_nodo(q.arg2(),bloque.nbloque);
									if(bloque.esta_indeterminado(q.op(),n1,n2)){
										n1 = bloque.aniadir_nodo(q.op(),n1,n2);
									}else n1 = bloque.encontrar_nodo(q.op(),n1,n2);	
									// Borrese x de la lista de identificadores asociados a nodo(x)
									
									// Añadase x a la lista de identificadores asociados al nodo n e igualase nodo(x) a n
								}	
								if(iter_bl.hasNext())
									ultimo_quad=(String)iter_bl.next();
								else
									ultimo_quad="";
							}								
						}
                        grafo+="},VertexLabels->All]";
                        System.out.println(grafo);
                        // generar codigo objeto en el lenguaje ensamblador TXORI
						// obtener de la pila el nombre del algoritmo
						arg1 = pila_sem.pop();
						int bdestino = 0;
						if(opcion.equalsIgnoreCase("TXORI")){
							outputStream = new PrintWriter(new FileWriter(arg1.nombre().toLowerCase()+".asm"));
							outputStream.println(".title "+arg1.nombre().toLowerCase());
							System.out.println(".title "+arg1.nombre().toLowerCase());
							// declaracion de las variables
							for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
								vble = (Var)iter.next(); 
								if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
								    !Util.stringEmpty(vble.tipo()))
									if(vble.tipo().substring(0,4).equals("arra"))
										outputStream.println(vble.nombre()+": .word "+vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")"))+";");
									else{
										outputStream.println(vble.nombre()+": .word 1;");
										System.out.println(vble.nombre()+": .word 1;");										
										}
							}
							outputStream.println(".proc main");
							System.out.println(".proc main");
							char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
							String ldir,yp,zp,etiqueta,espacios;
							boolean poner_etiqueta,saltar,primera_inst,limpiar_etiq;
							for(j=0;j<tbloques.size();j++){
								bloque = tbloques.get(j);
								// ver si a la primera instruccion hay que ponerle etiqueta
								poner_etiqueta = false;
								for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext() && !poner_etiqueta;){
									String nbpred=(String)iter.next();
									bbloque = tbloques.get(Integer.valueOf(nbpred).intValue());
									// ir al ultimo cuadruplo del bloque
									ultimo_quad = String.valueOf(bbloque.lider);
									q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
									if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF")))
										if(bloque.lider==Integer.valueOf(q.res()).intValue())
											poner_etiqueta=true;
									for(Iterator<String> iter_cuad=bbloque.cuadruplos.iterator();iter_cuad.hasNext() && !poner_etiqueta;){
										ultimo_quad = (String)iter_cuad.next();
										q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
										if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF")))
											if(bloque.lider==Integer.valueOf(q.res()).intValue())
												poner_etiqueta=true;
									}
								}
								if(poner_etiqueta){
									etiqueta = "etiq"+bloque.nbloque+":";
									espacios = new String(tabulador,etiqueta.length(),7 - etiqueta.length());
									etiqueta += espacios;
									System.out.println("etiq"+bloque.nbloque+":");
								}else
									etiqueta = new String(tabulador);
								primera_inst = true;
								ultimo_quad = String.valueOf(bloque.lider);
								iter_bl = bloque.cuadruplos.iterator();
								while(!Util.stringEmpty(ultimo_quad)){
									q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
									if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/")){
										saltar = false;ldir = new String();
										// invocar obtenreg para determinar donde se guarda el resultado
										// falta ver si alguno de los operandos es una cte.
										if(variables.esta_en_reg(q.arg1())){
											yp = variables.obtener_pos_en_curso(q.arg1(),"");
										}else{
											limpiar_etiq = false;
											if(primera_inst)
												if(hay_volcado_registro())
													limpiar_etiq = true;
											yp = obtenreg(q.arg1(),bloque.nbloque,etiqueta);
											if(limpiar_etiq){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}											
											if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1())))
												outputStream.println(etiqueta+"movi "+yp+", #"+q.arg1());
											else{
												outputStream.println(etiqueta+"ld "+yp+", "+q.arg1());
												System.out.println("ld "+yp+", "+q.arg1());
												variables.actualiza_descrip_direcc(q.arg1(),yp);
											}
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}											
										}    
										if(variables.esta_en_reg(q.arg2())){
											zp = variables.obtener_pos_en_curso(q.arg2(),"");
										}else{
						                    limpiar_etiq = false; 
											if(primera_inst)
												if(hay_volcado_registro())
													limpiar_etiq = true;
											zp = obtenreg(q.arg2(),bloque.nbloque,etiqueta);
											if(limpiar_etiq){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}											
											if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2()))){
												saltar = true;
												limpiar_etiq = false;
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												ldir = obtenreg(q.res(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}																						
												if(q.op().equals("+"))
													outputStream.println(etiqueta+"addi "+ldir+", #"+q.arg2());
												else if(q.op().equals("-"))
													outputStream.println(etiqueta+"subi "+ldir+", #"+q.arg2());
												else if(q.op().equals("*"))
													outputStream.println(etiqueta+"muli "+ldir+", #"+q.arg2());
												else if(q.op().equals("/"))
													outputStream.println(etiqueta+"divi "+ldir+", #"+q.arg2());
											}else{
												outputStream.println(etiqueta+"ld "+zp+", "+q.arg2());
												variables.actualiza_descrip_direcc(q.arg1(),yp);
											}
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
										} 
										if(!saltar){
											limpiar_etiq = false; 
											if(primera_inst)
												if(hay_volcado_registro())
													limpiar_etiq = true;
											ldir = obtenreg(q.res(),bloque.nbloque,etiqueta);
											if(limpiar_etiq){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}											
											if(q.op().equals("+"))
												outputStream.println(etiqueta+"add "+ldir+", "+yp+", "+zp);
											else if(q.op().equals("-"))
												outputStream.println(etiqueta+"sub "+ldir+", "+yp+", "+zp);
											else if(q.op().equals("*"))
												outputStream.println(etiqueta+"mul "+ldir+", "+yp+", "+zp);
											else if(q.op().equals("/"))
												outputStream.println(etiqueta+"div "+ldir+", "+yp+", "+zp);
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
										}
										// se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
										variables.actualiza_descrip_direcc(q.res(),ldir);
									}else if(q.op().equals(":=")){
											if(variables.esta_en_reg(q.res()))
												ldir = variables.obtener_pos_en_curso(q.res(),"");
											else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												ldir = obtenreg(q.res(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												variables.actualiza_descrip_direcc(q.res(),ldir);
											}
											if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1())))
												outputStream.println(etiqueta+"movi "+ldir+", #"+q.arg1());
											else
												if(!variables.esta_en_reg(q.arg1())){
													outputStream.println(etiqueta+"ld "+ldir+", "+q.arg1());
													variables.actualiza_descrip_direcc(q.arg1(),ldir);
												}else{
													yp = variables.obtener_pos_en_curso(q.arg1(),"");
													outputStream.println(etiqueta+"mov "+ldir+", "+yp);
												}
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
									}else if(q.op().substring(0,2).equals("IF")){
											if(variables.esta_en_reg(q.arg1())){
												yp = variables.obtener_pos_en_curso(q.arg1(),"");
											}else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												yp = obtenreg(q.arg1(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1())))
													outputStream.println(etiqueta+"movi "+yp+", #"+q.arg1());
												else{
													outputStream.println(etiqueta+"ld "+yp+", "+q.arg1());
													variables.actualiza_descrip_direcc(q.arg1(),yp);
												}
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}    
											if(variables.esta_en_reg(q.arg2())){
												zp = variables.obtener_pos_en_curso(q.arg2(),"");
											}else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												zp = obtenreg(q.arg2(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2())))
													outputStream.println(etiqueta+"movi "+zp+", #"+q.arg2());
												else{
													outputStream.println(etiqueta+"ld "+zp+", "+q.arg2());
													variables.actualiza_descrip_direcc(q.arg2(),zp);
												}
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}  
											limpiar_etiq = false; 
											if(primera_inst)
												if(hay_volcado_registro())
													limpiar_etiq = true;
											ldir = obtenreg("tmpif",bloque.nbloque,etiqueta);
											if(limpiar_etiq){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}											
											outputStream.println(etiqueta+"sub "+ldir+", "+yp+", "+zp);
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
											// averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
											for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
												String sucesor = (String)iter.next();
												bbloque = tbloques.get(Integer.valueOf(sucesor).intValue());
												if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
													bdestino = bbloque.nbloque;
													break;
												}
											}			
											if(q.op().substring(2).trim().equals(">")){
												outputStream.println(etiqueta+"bgt "+ldir+", etiq"+Integer.toString(bdestino));
											}else if(q.op().substring(2).trim().equals(">=")){	
												outputStream.println(etiqueta+"bge "+ldir+", etiq"+Integer.toString(bdestino));
											}else if(q.op().substring(2).trim().equals("<")){
												outputStream.println(etiqueta+"bls "+ldir+", etiq"+Integer.toString(bdestino));
											}else if(q.op().substring(2).trim().equals("<=")){
												outputStream.println(etiqueta+"ble "+ldir+", etiq"+Integer.toString(bdestino));
											}else if(q.op().substring(2).trim().equals("<>")){
												outputStream.println(etiqueta+"bne "+ldir+", etiq"+Integer.toString(bdestino));
											}else if(q.op().substring(2).trim().equals("=")){
												outputStream.println(etiqueta+"beq "+ldir+", etiq"+Integer.toString(bdestino));	
											}	
									}else if(q.op().equals("GOTO")){
											// averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
											for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
												String sucesor = (String)iter.next();
												bbloque = tbloques.get(Integer.valueOf(sucesor).intValue());
												if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
													bdestino = bbloque.nbloque;
													break;
												}
											}
											outputStream.println(etiqueta+"jmp etiq"+Integer.toString(bdestino));
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
									}else if(q.op().equals("=[]")){
											if(variables.esta_en_reg(q.res())){
												yp = variables.obtener_pos_en_curso(q.res(),"");
											}else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												yp = obtenreg(q.res(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												outputStream.println(etiqueta+"ld "+yp+", "+q.res());
												// se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
										        variables.actualiza_descrip_direcc(q.res(),yp);
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}    
											if(variables.esta_en_reg(q.arg2())){
												zp = variables.obtener_pos_en_curso(q.arg2(),"");
											}else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												zp = obtenreg(q.arg2(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2())))
													outputStream.println(etiqueta+"movi "+zp+", #"+q.arg2());
												else{
													outputStream.println(etiqueta+"ld "+zp+", "+q.arg2());
													// se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
										            variables.actualiza_descrip_direcc(q.arg2(),zp);
												}	
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}  
											outputStream.println(etiqueta+"ldx "+yp+", "+q.arg1()+"["+zp+"]");
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
									}else if(q.op().equals("[]=")){
											if(variables.esta_en_reg(q.res())){
												yp = variables.obtener_pos_en_curso(q.res(),"");
											}else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												yp = obtenreg(q.res(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												outputStream.println(etiqueta+"ld "+yp+", "+q.res());
												// se actualiza el descriptor de direcciones de q.res para indicar que esta en yp
										        variables.actualiza_descrip_direcc(q.res(),yp);
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}    
											if(variables.esta_en_reg(q.arg2())){
												zp = variables.obtener_pos_en_curso(q.arg2(),"");
											}else{
												limpiar_etiq = false; 
												if(primera_inst)
													if(hay_volcado_registro())
														limpiar_etiq = true;
												zp = obtenreg(q.arg2(),bloque.nbloque,etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2())))
													outputStream.println(etiqueta+"movi "+zp+", #"+q.arg2());
												else{
													outputStream.println(etiqueta+"ld "+zp+", "+q.arg2());
													 variables.actualiza_descrip_direcc(q.arg2(),zp);
												}	
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}  
											outputStream.println(etiqueta+"stx "+yp+", "+q.arg1()+"["+zp+"]");
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
									}else if(q.op().equals("WRITE")){
											if(variables.esta_en_reg(q.arg1())){
												yp = variables.obtener_pos_en_curso(q.arg1(),"r1");
												if(!yp.equals("r1")){
													limpiar_etiq = false;
													if(primera_inst)
														if(hay_volcado_r1())
															limpiar_etiq = true;
													almacenar_reg_r1(q.arg1(),etiqueta);
													if(limpiar_etiq){
														primera_inst = false;
														etiqueta = new String(tabulador);
													}											
													outputStream.println(etiqueta+"mov r1, "+yp);
													variables.actualiza_descrip_direcc(q.arg1(),"r1");
													if(primera_inst){
														primera_inst = false;
														etiqueta = new String(tabulador);
													}
												}
											}else{
												limpiar_etiq = false;
												if(primera_inst)
													if(hay_volcado_r1())
														limpiar_etiq = true;
												almacenar_reg_r1(q.arg1(),etiqueta);
												if(limpiar_etiq){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}											
												if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1())))
													outputStream.println(etiqueta+"movi r1, #"+q.arg1());
												else{
													outputStream.println(etiqueta+"ld r1, "+q.arg1());
													variables.actualiza_descrip_direcc(q.arg1(),"r1");
												}	
												if(primera_inst){
													primera_inst = false;
													etiqueta = new String(tabulador);
												}
											}
											outputStream.println(etiqueta+"out");// visualiza el contenido del registro r1
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
                                 	}else if(q.op().equals("INPUT")){
											limpiar_etiq = false;
											if(primera_inst)
												if(hay_volcado_r1())
													limpiar_etiq = true;
											almacenar_reg_r1(q.arg1(),etiqueta);
											if(limpiar_etiq){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}											
											outputStream.println(etiqueta+"in"); // guarda el entero tecleado en el registro r1
											System.out.println("in");
											variables.actualiza_descrip_direcc(q.arg1(),"r1");
											if(primera_inst){
												primera_inst = false;
												etiqueta = new String(tabulador);
											}
									}	
									if(iter_bl.hasNext())
										ultimo_quad=(String)iter_bl.next();
									else
										ultimo_quad="";
								}								
							}  
							outputStream.println("       retm");
							outputStream.println(".endp main");
							outputStream.println(".end");
							outputStream.close();
						}else if(opcion.equalsIgnoreCase("UDMPs99")){
							outputStream = new PrintWriter(new FileWriter(arg1.nombre().toLowerCase()+".mpv"));
							// declaracion de las variables
							for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
								vble = (Var)iter.next(); 
								if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
								    !Util.stringEmpty(vble.tipo()))
									if(vble.tipo().equals("osoa"))
										outputStream.println("globali "+vble.nombre());
									else if(vble.tipo().equals("erreala"))
											outputStream.println("globalr "+vble.nombre());
										 else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter())
											 outputStream.println("globalb "+vble.nombre());
							}
							// empieza el programa principal de la maquina de pila
							outputStream.println("inicio");
							String ldir,yp,zp,etiqueta;
							boolean poner_etiqueta;
							for(j=0;j<tbloques.size();j++){
								bloque = tbloques.get(j);
								// ver si a la primera instruccion hay que ponerle etiqueta
								poner_etiqueta = false;
								for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext() && !poner_etiqueta;){
									String nbpred=(String)iter.next();
									bbloque = tbloques.get(Integer.valueOf(nbpred).intValue());
									ultimo_quad = String.valueOf(bbloque.lider);
									q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
									if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF")))
										if(bloque.lider==Integer.valueOf(q.res()).intValue())
											poner_etiqueta=true;
									for(Iterator<String> iter_cuad=bbloque.cuadruplos.iterator();iter_cuad.hasNext() && !poner_etiqueta;){
										ultimo_quad = (String)iter_cuad.next();
										q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
										if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF")))
											if(bloque.lider==Integer.valueOf(q.res()).intValue())
												poner_etiqueta=true;
									}
								}
								if(poner_etiqueta)
									outputStream.println("etiq #B"+Integer.toString(bloque.nbloque));
								ultimo_quad = String.valueOf(bloque.lider);
								iter_bl = bloque.cuadruplos.iterator();
								while(!Util.stringEmpty(ultimo_quad)){
									q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
									if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/")){
										outputStream.println("valori "+q.res());
										if(variables.leer_tipo(q.res()).equals("osoa")){
											if(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')
												outputStream.println("insi "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
											if(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')
												outputStream.println("insi "+q.arg2());
											else 
												outputStream.println("valord "+q.arg2());
											if(q.op().equals("+"))
												outputStream.println("sumai");
											else if(q.op().equals("-"))
												outputStream.println("restai");
											else if(q.op().equals("*")) 
												outputStream.println("multi");
											else if(q.op().equals("/"))
												outputStream.println("divi");
											outputStream.println("asigna");
										}else if(variables.leer_tipo(q.res()).equals("erreala")){
											if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')|| q.arg1().charAt(0)=='.')
												outputStream.println("insr "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
											if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')|| q.arg2().charAt(0)=='.')
												outputStream.println("insr "+q.arg2());
											else 
												outputStream.println("valord "+q.arg2());
											if(q.op().equals("+"))
												outputStream.println("sumar");
											else if(q.op().equals("-"))
												outputStream.println("restar");
											else if(q.op().equals("*")) 
												outputStream.println("multr");
											else if(q.op().equals("/"))
												outputStream.println("divr");
											outputStream.println("asignar");
										}else{
											if(q.arg1().length()==1 && q.arg1().equals(variables.leer_valor(q.arg1())))
												outputStream.println("insb "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
											if(q.arg2().length()==1 && q.arg2().equals(variables.leer_valor(q.arg2())))
												outputStream.println("insb "+q.arg2());
											else 
												outputStream.println("valord "+q.arg2());
											if(q.op().equals("+"))
												outputStream.println("sumab");
											else if(q.op().equals("-"))
												outputStream.println("restab");
											outputStream.println("asignab");
										}	
									}else if(q.op().equals(":=")){
										outputStream.println("valori "+q.res());
										if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")<0) || variables.leer_tipo(q.arg1()).equals("osoa")){
											if(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')
												outputStream.println("insi "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
											outputStream.println("asigna");
										}else if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")>=0)||variables.leer_tipo(q.arg1()).equals("erreala")){
											if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')|| q.arg1().charAt(0)=='.')
												outputStream.println("insr "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
											outputStream.println("asignar");	
										}else{
											if(q.arg1().length()==1 && q.arg1().equals(variables.leer_valor(q.arg1())))
												outputStream.println("insb "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
											outputStream.println("asignab");
										}		
									}else if(q.op().substring(0,2).equals("IF")){
										if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")<0) || variables.leer_tipo(q.arg1()).equals("osoa"))
											if(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')
												outputStream.println("insi "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
										else if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")>=0) || variables.leer_tipo(q.arg1()).equals("erreala"))
											if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||q.arg1().charAt(0)=='.')
												outputStream.println("insr "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());
										else{
											if(q.arg1().length()==1 && q.arg1().equals(variables.leer_valor(q.arg1())))
												outputStream.println("insb "+q.arg1());
											else 
												outputStream.println("valord "+q.arg1());											
										}
										if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9' && q.arg2().indexOf(".")<0) || variables.leer_tipo(q.arg2()).equals("osoa"))
											if(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')
												outputStream.println("insi "+q.arg2());
											else 
												outputStream.println("valord "+q.arg2());
										else if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9' && q.arg2().indexOf(".")>=0) || variables.leer_tipo(q.arg2()).equals("erreala"))
											if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||q.arg2().charAt(0)=='.')
												outputStream.println("insr "+q.arg2());
											else 
												outputStream.println("valord "+q.arg2());
										else{
											if(q.arg2().length()==1 && q.arg2().equals(variables.leer_valor(q.arg2())))
												outputStream.println("insb "+q.arg2());
											else 
												outputStream.println("valord "+q.arg2());											
										}
										// averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
										for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
											String sucesor = (String)iter.next();
											bbloque = tbloques.get(Integer.valueOf(sucesor).intValue());
											if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
												bdestino = bbloque.nbloque;
												break;
											}
										}	
										if(variables.leer_tipo(q.arg1()).equals("osoa")||variables.leer_tipo(q.arg2()).equals("osoa"))
											if(q.op().substring(2).trim().equals("="))
												outputStream.println("==i");
											else if(q.op().substring(2).trim().equals("<>"))
												outputStream.println("!=i");
											else
												outputStream.println(q.op().substring(2).trim()+"i");
										else if(variables.leer_tipo(q.arg1()).equals("erreala")||variables.leer_tipo(q.arg2()).equals("erreala"))
											if(q.op().substring(2).trim().equalsIgnoreCase("="))
												outputStream.println("==r");
											else if(q.op().substring(2).trim().equals("<>"))
												outputStream.println("!=r");
											else
												outputStream.println(q.op().substring(2).trim()+"r");
										else
											if(q.op().substring(2).trim().equals("="))
												outputStream.println("==b");
											else if(q.op().substring(2).trim().equals("<>"))
												outputStream.println("!=b");
											else
												outputStream.println(q.op().substring(2).trim()+"b");
										outputStream.println("si-cierto-ir-a #B"+Integer.toString(bdestino));									
									}else if(q.op().equals("GOTO")){
										// averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
										for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
											String sucesor = (String)iter.next();
											bbloque = tbloques.get(Integer.valueOf(sucesor).intValue());
											if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
												bdestino = bbloque.nbloque;
												break;
											}
										}	
                                        outputStream.println("ir–a #B"+Integer.toString(bdestino));
									}else if(q.op().equals("INPUT")){
										outputStream.println("valori "+q.arg1());
										if(variables.leer_tipo(q.arg1()).equals("osoa")){
											outputStream.println("leeri");
											outputStream.println(":=");
										}else if(variables.leer_tipo(q.arg1()).equals("erreala")){
											outputStream.println("leerr");
											outputStream.println(":=r");
										}else{
											outputStream.println("leerb");
											outputStream.println(":=b");
											}
									}else if(q.op().equals("WRITE")){
										outputStream.println("valord "+q.arg1());
										if(variables.leer_tipo(q.arg1()).equals("osoa"))
											outputStream.println("escribiri");
										else if(variables.leer_tipo(q.arg1()).equals("erreala"))
											outputStream.println("escribirr");
										else
											outputStream.println("escribirb");
										outputStream.println("escribirln");
									}else if(q.op().equals("RET")){	
										outputStream.println("fin");
									}
									if(iter_bl.hasNext())
										ultimo_quad=(String)iter_bl.next();
									else
										ultimo_quad="";
								}								
							}  							
							outputStream.close();
						}	
                        // ver si hay lazos: empezando en cada bloque ver si siguiendo sus sucesores se llega al mismo bloque
                        ArrayList<String> bucles = new ArrayList<String>();
                        int[] lazo;
                        for(j=0;j<tbloques.size();j++){
                            ArrayList<String> llazos = new ArrayList<String>();
                            lazo = new int[tbloques.size()];
                            bloque = tbloques.get(j);
                            lazo[0]=bloque.nbloque;
                            encontrar_lazos(tbloques,lazo,0,llazos);
                            for(Iterator<String> iter=llazos.iterator();iter.hasNext();){
                                grafo = (String)iter.next();
                                if(no_esta(grafo,bucles))
                                    bucles.add(grafo);
                            }
                        }   
                        for(Iterator<String> iter=bucles.iterator();iter.hasNext();){ // faltaria para cada lazo, calcular la cuenta de uso de las variables que aparecen en el lazo
                            grafo = (String)iter.next();
                            System.out.println("lazo:"+grafo);
                        } 
                        break;
                case 6: pila_sem.push(new Reg("karakterea"));
                        break;						
                case 7: pila_sem.push(new Reg("osoa"));
                        break;		
				case 9: arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();
						pila_sem.push(new Reg("array("+arg2.nombre()+","+arg1.nombre()+")"));
                        break;
                case 13:arg1 = pila_sem.pop();
                        cod = arg1.codigo();
						cierto = arg1.cierto();
						falso = arg1.falso();
                        while(!pila_sem.empty() && Util.stringEmpty(pila_sem.peek().quad())){
                            arg1 = pila_sem.pop();
                            asocia(arg1.cierto(),primera_pos(cod));
                            asocia(arg1.falso(),primera_pos(cod));
                            cod = arg1.codigo()+","+cod;
                        }   
                        if(!pila_sem.empty() && !Util.stringEmpty(pila_sem.peek().quad()))
                           arg1 = pila_sem.pop();
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;  
                case 16:arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();
                        cod = gen(":=", arg2.nombre(), null, arg1.nombre());
                        if(arg2.codigo()!=null) cod = arg2.codigo()+","+cod;
                        if(arg1.codigo()!=null) cod = cod+","+arg1.codigo();
                        pila_sem.push(new Reg(null,null,cod,null,null,null));
                        break;
                case 18:arg1 = pila_sem.pop();
                        cod = gen("INPUT", arg1.nombre(), null, null);
                        pila_sem.push(new Reg(null,null,cod,null,null,null));
                        break;
                case 19:arg1 = pila_sem.pop();
                        cod = gen("WRITE", variables.leer(Integer.valueOf(arg1.lugar()).intValue()), null, null);
                        pila_sem.push(new Reg(arg1.nombre(),arg1.lugar(),arg1.codigo()+","+cod,null,null,null));
                        break;  
				case 21:op=pila_sem.pop();
						arg1=pila_sem.pop();
						asocia(arg1.cierto(),op.quad());
						asocia(arg1.falso(),op.quad());
						op=pila_sem.pop();
						pila_sem.push(new Reg(null,null,arg1.codigo(),null,null,null));
						break;
                case 24:arg3 = pila_sem.pop();
						op2 = pila_sem.pop();
						arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop(); 
						if(Util.stringEmpty(arg3.codigo())){		// ver si existe la parte else				
							cod = arg1.codigo()+","+arg2.codigo();
							asocia(arg1.cierto(),op.quad());
							cierto = arg2.cierto();
							falso = arg1.falso()+","+arg2.falso();
						}else{
							cod = arg1.codigo()+","+arg2.codigo()+","+arg3.codigo();
							asocia(arg1.cierto(),op.quad());
							asocia(arg1.falso(),op2.quad());
							cierto = arg2.cierto()+","+arg3.cierto();
							falso = arg2.falso()+","+arg3.falso();
						}						
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;
                case 25:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        ini = pila_sem.pop();   
                        asocia(arg1.falso(),op.quad());
                        asocia(arg1.cierto(),op.quad());
                        asocia(arg2.falso(),ini.quad());
                        cod = arg1.codigo()+","+arg2.codigo();
                        cierto = arg2.cierto();
                        pila_sem.push(new Reg(null,null,cod,null,cierto,null));
                        break;
                case 26:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        ini = pila_sem.pop();   
                        asocia(arg1.cierto(),op.quad());
                        asocia(arg2.cierto(),ini.quad());
                        asocia(arg2.falso(),ini.quad());
                        cod = arg1.codigo()+","+arg2.codigo()+","+gen("GOTO",null,null,ini.quad());
                        falso = arg1.falso();
                        pila_sem.push(new Reg(null,null,cod,null,null,falso));
                        break;
                case 20:arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        asocia(arg1.cierto(),primera_pos(arg2.codigo()));   
                        asocia(arg2.cierto(),String.valueOf(tcuad.size()));
                        asocia(arg2.falso(),String.valueOf(tcuad.size()));
                        cod = arg1.codigo()+","+arg2.codigo()+","+gen("+",arg1.nombre(),"1",arg1.nombre())+","+gen("GOTO",null,null,arg1.quad());
                        falso = arg1.falso();
                        pila_sem.push(new Reg(null,null,cod,null,null,falso));
                        break;
                case 43:arg1 = pila_sem.pop();
                        pila_sem.push(new Reg(null,null,null,null,arg1.falso(),arg1.cierto()));
                        break;              
                case 46:pila_sem.push(new Reg(ps));
                        break; 
                case 49:pila_sem.push(new Reg(ps));
                        break;
                case 50:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();
                        t = nuevotemp();
                        cod = gen(op.nombre(), arg1.nombre(), arg2.nombre(),t);
                        pila_sem.push(new Reg(t,String.valueOf(variables.insertar(t)),arg1.codigo()+","+arg2.codigo()+","+cod,null,null,null));
                        break;  
                case 51:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        cierto = String.valueOf(tcuad.size());
                        cod = arg1.codigo();
                        if(arg2.codigo()!=null)
                            cod += ","+arg2.codigo();
                        cod += ","+gen("IF"+op.nombre(),arg1.nombre(),arg2.nombre(),null);
                        falso = String.valueOf(tcuad.size());
                        cod += ","+gen("GOTO",null,null,null);
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;
                case 52:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        falso = arg1.falso()+","+arg2.falso();
                        asocia(arg1.cierto(),op.quad());
                        cierto = arg2.cierto();
                        pila_sem.push(new Reg(null,null,arg1.codigo()+","+arg2.codigo(),null,cierto,falso));
                        break;
                case 53:pila_sem.push(new Reg(null,null,null,String.valueOf(tcuad.size()),null,null));
                        break;      
                case 54:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        cierto = arg1.cierto()+","+arg2.cierto();
                        asocia(arg1.falso(),op.quad());
                        falso = arg2.falso();
                        pila_sem.push(new Reg(null,null,arg1.codigo()+","+arg2.codigo(),null,cierto,falso));
                        break;
                case 56:arg1 = pila_sem.peek();
                        cod = arg1.codigo();
                        if(cod!=null && !cod.equals("")){
                            int ind = Integer.valueOf(cod).intValue();
                            q = (Cuad)tcuad.get(ind);
                            q.set_op("[]=");
                            tcuad.set(ind,q);
                        }
                        break; 
                case 57:pila_sem.push(new Reg(ps,String.valueOf(variables.insertar(ps)),null,null,null,null));
                        break;
                case 60:arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();
                        t = nuevotemp();
                        cod = gen("=[]",arg1.nombre(),arg2.nombre(),t);
                        if(arg2.codigo()!=null && !arg2.codigo().equals("")) cod = arg2.codigo()+","+cod;
                        pila_sem.push(new Reg(t,String.valueOf(variables.insertar(t)),cod,null,null,null));
                        break;
                case 63:arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();
                        cod = gen(":=", arg2.nombre(), null, arg1.nombre());
                        if(arg2.codigo()!=null) cod = arg2.codigo()+","+cod;
                        pila_sem.push(new Reg(arg1.nombre(),null,cod,String.valueOf(tcuad.size()),null,null));
                        break;
                case 64:arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        cod = arg1.codigo();
                        if(arg2.codigo()!=null)
                            cod += ","+arg2.codigo();
                        cierto = String.valueOf(tcuad.size());
                        cod += ","+gen("IF<=",arg1.nombre(),arg2.nombre(),null);
                        falso = String.valueOf(tcuad.size());
                        cod += ","+gen("GOTO",null,null,null);
                        pila_sem.push(new Reg(arg1.nombre(),null,cod,arg1.quad(),cierto,falso));
                        break;
                case 65:arg2 = pila_sem.pop(); // leer el tipo
						// obtener los ID asociados a ese tipo, para darlos de alta en la TS del registro de activacion
						arg1 = pila_sem.pop();
						do {
							variables.insertar(arg1.nombre(),arg2.nombre());
							arg1 = pila_sem.pop();
						}while(!pila_sem.empty());
						// se vuelve a insertar en la pila, lo que sería el nombre del algoritmo
						pila_sem.push(arg1);
                        break;
                case 66:arg2 = pila_sem.pop(); // cada una de las condiciones de aukera se trata como si fuera una instruccion de baldin expr orduan
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        cod = arg1.codigo()+","+arg2.codigo();
                        asocia(arg1.cierto(),op.quad());
                        cierto = arg2.cierto()+","+arg2.falso();
                        falso = arg1.falso(); 
						if(!pila_sem.empty() && Util.stringEmpty(pila_sem.peek().quad())){
							arg1 = pila_sem.pop();
							asocia(arg1.falso(),primera_pos(cod));
							cod = arg1.codigo()+","+cod;
							cierto = arg1.cierto()+","+cierto;
						}
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;
                case 67:pila_sem.push(new Reg(ps));
                        break; 
                case 70:arg1 = pila_sem.pop();
                        cod = arg1.codigo();
						cierto = arg1.cierto();
						falso = arg1.falso();
                        while(!pila_sem.empty() && Util.stringEmpty(pila_sem.peek().quad())){
                            arg1 = pila_sem.pop();
                            asocia(arg1.cierto(),primera_pos(cod));
                            asocia(arg1.falso(),primera_pos(cod));
                            cod = arg1.codigo()+","+cod;
                        }   
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;
                case 72:pila_sem.push(new Reg(null,null,null,null,null,null));
						pila_sem.push(new Reg(null,null,null,null,null,null));
						break;				
			}
        } finally{
        }
    }
}   
