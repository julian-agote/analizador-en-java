package parser_jkl;
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
	private String tipo;
	int[] anArray;
	private int sig_uso;
	Var(String _nombre){
		this.nombre = _nombre;
		this.tipo = "OSOA";
	}	
	Var(String _nombre,float pval){
		this.nombre = _nombre;
		this.tipo = "ERREALA";
		this.valf = new Float(pval);
	}	
	Var(String _nombre,int pval){
		this.nombre = _nombre;
		this.tipo = "OSOA";
		this.vali = new Integer(pval);
	}	
	Var(String _nombre,int val_ini,int longitud){
		this.nombre = _nombre;
		this.tipo = "OSOEN-ARRAYA";
		this.anArray = new int[longitud];
	}	
	public String nombre() {
		return nombre;
	}
	public Number valor() {
		if(tipo.equals("ERREALA"))
			return valf;
		else
			return vali;
	}
	public void set_valor(float pvalor){
		this.valf = new Float(pvalor);
		if(this.tipo==null)
			this.tipo = "ERREALA";
	}	
	public void set_valor(int pvalor){
		this.vali = new Integer(pvalor);
		if(this.tipo==null)
			this.tipo = "OSOA";
	}	
	public void set_valor(int pos, int pvalor){
		this.anArray[pos-1] = pvalor;
	}	
	public Integer get_valor(int pos){
		return new Integer(this.anArray[pos-1]);
	}
	public void set_activo(int _i){
		this.sig_uso = _i;		
	}
	public void set_inactivo(){
		this.sig_uso = -1;
	}
	public int activo(){
		return sig_uso;
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
    int insertar(String p_lex,int longitud) {
	    int pos = -1;
		for(int i=0;i<ts.size();i++)
			if(ts.get(i).nombre().equals(p_lex)){
				pos = i;
				break;
			}	
        if (pos == -1) {
            pos = ts.size();
       		ts.add(new Var(p_lex,0,longitud));
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
			return ts.get(pos).valor().toString();
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
	void sgte_uso(String nombre, int ind){
	    int pos = -1;
		for(int i=0;i<ts.size();i++)
			if(ts.get(i).nombre().equals(nombre)){
				pos = i;
				break;
			}	
        if (pos != -1){
			ts.get(pos).set_activo(ind);
		}
	}	
	void desactivar(String nombre){
	    int pos = -1;
		for(int i=0;i<ts.size();i++)
			if(ts.get(i).nombre().equals(nombre)){
				pos = i;
				break;
			}	
        if (pos != -1)
			ts.get(pos).set_inactivo();
	}
	boolean esta_activo(String nombre){
	    int pos = -1;
		for(int i=0;i<ts.size();i++)
			if(ts.get(i).nombre().equals(nombre)){
				pos = i;
				break;
			}	
        if (pos != -1)
			return (ts.get(pos).activo()>=0?true:false);
		return false;
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
			ts.set(pos,v);
		}else
            if(valor.charAt(0)>='0' && valor.charAt(0)<='9')
            	if(valor.indexOf(".")>=0)
					ts.add(new Var(nombre, Float.valueOf(valor).floatValue()));
				else	
	            	ts.add(new Var(nombre, Integer.valueOf(valor).intValue()));
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
class Bloque_basico{
	int nbloque,lider;
	ArrayList<String> cuadruplos,succ,pred;
	Bloque_basico(int _b,int _lider){
		this.nbloque = _b;
		this.lider = _lider;
		this.cuadruplos = new ArrayList<String>();
		this.succ = new ArrayList<String>();
		this.pred = new ArrayList<String>();
	}
	Bloque_basico(){
		this.cuadruplos = new ArrayList<String>();
		this.succ = new ArrayList<String>();
		this.pred = new ArrayList<String>();
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
}
public class TGestorSemantico{
    PrintWriter outputStream = null;
	Stack<Reg> pila_sem = null;
	ArrayList<Cuad> tcuad;
	int indice = 0;
	Tvariables variables;

	TGestorSemantico() throws IOException {
		pila_sem = new Stack<Reg>();
		outputStream = new PrintWriter(new FileWriter("a.out"));
		tcuad = new ArrayList<Cuad>();
		variables = new Tvariables();
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
	void encontrar_lazos(ArrayList<Bloque_basico> tbloques,int[] lazo, int n, ArrayList<String> listaLazos){
		Bloque_basico bloque = new Bloque_basico();
		String sucesor,cad;
		for(int k=0;k<tbloques.size();k++){
			bloque = tbloques.get(k);
			if(bloque.nbloque == lazo[n])
				break;
		}
		for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
			sucesor = (String)iter.next();
			if(lazo[0]==Integer.valueOf(sucesor).intValue()){
				cad = new Integer(lazo[0]).toString();
				for(int k=1;k<n;k++) cad+="->"+(new Integer(lazo[k]).toString());
				listaLazos.add(cad+"->"+new Integer(lazo[0]).toString());
			}else{
				lazo[n+1]=Integer.valueOf(sucesor).intValue();
				encontrar_lazos(tbloques,lazo,n+1,listaLazos);
			}
		}
	}
	boolean no_esta(String c, ArrayList<String> b){
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
	void ejecutarAccionSemantica(int numRegla,String ps)  throws IOException {
		Reg arg1,arg2,op,ini;
		StringBuffer sb;
		String t,cierto,falso,cod;
	    Cuad q;
		String ind_cuad[],aux_cuad[],ultimo_quad;
		ArrayList<Bloque_basico> tbloques;
		int[] lideres; int n=0,i;
		Bloque_basico bloque, bbloque;
						
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
										float a = Integer.valueOf(variables.leer_valor(q.arg1())).floatValue();
										float b = Integer.valueOf(variables.leer_valor(q.arg2())).floatValue();
										variables.guardar_valor(q.res(),new Float(a/b).toString());
									}else{
										int a = Integer.valueOf(variables.leer_valor(q.arg1())).intValue();
										int b = Integer.valueOf(variables.leer_valor(q.arg2())).intValue();
										variables.guardar_valor(q.res(),new Integer(a/b).toString());
									}
									j++;
								}else if(q.op().equals("*")){
									if(variables.leer_valor(q.arg1()).indexOf(".")>=0){
										float a = Integer.valueOf(variables.leer_valor(q.arg1())).floatValue();
										float b = Integer.valueOf(variables.leer_valor(q.arg2())).floatValue();
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
									if(variables.leer_valor(q.arg1()).indexOf(".")<0){
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
									}else{
										float a = Integer.valueOf(variables.leer_valor(q.arg1())).floatValue();
										float b = Integer.valueOf(variables.leer_valor(q.arg2())).floatValue();
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
								lideres[n++]= Integer.valueOf(ind_cuad[j]).intValue();
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
										lideres[n++]=Integer.valueOf(q.res()).intValue();
									for(j=j+1;j<ind_cuad.length;j++)
										if(!Util.stringEmpty(ind_cuad[j])){
											enc = false;
											for(int k=0;k<n && !enc;k++)
												if(lideres[k]==Integer.valueOf(ind_cuad[j]).intValue())
													enc=true;
											if(!enc)											
												lideres[n++]=Integer.valueOf(ind_cuad[j]).intValue();
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
						// mostrar la inf. de los bloques basicos
						String grafo = new String("Graph[{");
						for(j=0;j<tbloques.size();j++){
							bloque = tbloques.get(j);
							String lcuad = String.valueOf(bloque.lider);
							for(Iterator<String> iter=bloque.cuadruplos.iterator();iter.hasNext();)
								lcuad+=","+(String)iter.next();
							for(ListIterator<String> iter=bloque.cuadruplos.listIterator();iter.hasPrevious();){
								ultimo_quad = (String)iter.previous();
								q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
								if(q.op().equals(":=")){
									variables.desactivar(q.res());
									variables.sgte_uso(q.arg1(),Integer.valueOf(ultimo_quad).intValue());
									variables.sgte_uso(q.arg2(),Integer.valueOf(ultimo_quad).intValue());
								}
							}
							String lpred = new String();
							for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext();)
								lpred+=","+(String)iter.next();
							String lsucc = new String();
							for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
								String sucesor = (String)iter.next();
								if(lsucc!=null)
									lsucc+=","+sucesor;
								else
									lsucc=sucesor;
								if(grafo.length()>7)
									grafo+=","+bloque.nbloque+"->"+sucesor;
								else
									grafo+=bloque.nbloque+"->"+sucesor;
							}	
							System.out.println("B"+bloque.nbloque+" cuad="+lcuad+" pred="+lpred+" succ="+lsucc);
						}
						grafo+="},VertexLabels->All]";
						System.out.println(grafo);
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
						for(Iterator<String> iter=bucles.iterator();iter.hasNext();){
							grafo = (String)iter.next();
							System.out.println("lazo:"+grafo);
						}	
						break;
				case 4:	arg1 = pila_sem.pop();
						cod = arg1.codigo();
						while(!pila_sem.empty() && Util.stringEmpty(pila_sem.peek().quad())){
							arg1 = pila_sem.pop();
							asocia(arg1.cierto(),primera_pos(cod));
							asocia(arg1.falso(),primera_pos(cod));
							cod = arg1.codigo()+","+cod;
						}	
						if(!pila_sem.empty() && !Util.stringEmpty(pila_sem.peek().quad()))
						   arg1 = pila_sem.pop();
						pila_sem.push(new Reg(null,null,cod,null,null,null));
						break;	
			    case 8:	arg2 = pila_sem.pop();
						arg1 = pila_sem.pop();
						cod = gen(":=", arg2.nombre(), null, arg1.nombre());
						if(arg2.codigo()!=null) cod = arg2.codigo()+","+cod;
						if(arg1.codigo()!=null) cod = cod+","+arg1.codigo();
						pila_sem.push(new Reg(null,null,cod,null,null,null));
						break;
				case 11:arg1 = pila_sem.pop();
						cod = gen("WRITE", variables.leer(Integer.valueOf(arg1.lugar()).intValue()), null, null);
						pila_sem.push(new Reg(arg1.nombre(),arg1.lugar(),arg1.codigo()+","+cod,null,null,null));
						break;		
				case 12:arg1 = pila_sem.pop();
						cod = gen("WRITEC", arg1.nombre(), null, null);
						pila_sem.push(new Reg(null,null,cod,null,null,null));
						break;		
				case 14:arg2 = pila_sem.pop();
						op = pila_sem.pop();
						arg1 = pila_sem.pop();	
						cod = arg1.codigo()+","+arg2.codigo();
						asocia(arg1.cierto(),op.quad());
						cierto = arg2.cierto();
						falso = arg1.falso()+","+arg2.falso();	
						pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
						break;
				case 15:arg2 = pila_sem.pop();
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
				case 17:arg2 = pila_sem.pop();
						arg1 = pila_sem.pop();	
						asocia(arg1.cierto(),primera_pos(arg2.codigo()));	
						asocia(arg2.cierto(),String.valueOf(tcuad.size()));
						asocia(arg2.falso(),String.valueOf(tcuad.size()));
						cod = arg1.codigo()+","+arg2.codigo()+","+gen("+",arg1.nombre(),"1",arg1.nombre())+","+gen("GOTO",null,null,arg1.quad());
						falso = arg1.falso();
						pila_sem.push(new Reg(null,null,cod,null,null,falso));
						break;
				case 33:arg1 = pila_sem.pop();
						pila_sem.push(new Reg(null,null,null,null,arg1.falso(),arg1.cierto()));
						break;				
				case 36:pila_sem.push(new Reg(ps));
						break; 
				case 39:pila_sem.push(new Reg(ps));
						break;
				case 40:arg2 = pila_sem.pop();
						op = pila_sem.pop();
						arg1 = pila_sem.pop();
						t = nuevotemp();
						cod = gen(op.nombre(), arg1.nombre(), arg2.nombre(),t);
						pila_sem.push(new Reg(t,String.valueOf(variables.insertar(t)),arg1.codigo()+","+arg2.codigo()+","+cod,null,null,null));
						break;	
				case 41:arg2 = pila_sem.pop();
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
				case 42:arg2 = pila_sem.pop();
						op = pila_sem.pop();
						arg1 = pila_sem.pop();	
						falso = arg1.falso()+","+arg2.falso();
						asocia(arg1.cierto(),op.quad());
						cierto = arg2.cierto();
						pila_sem.push(new Reg(null,null,arg1.codigo()+","+arg2.codigo(),null,cierto,falso));
						break;
				case 43:pila_sem.push(new Reg(null,null,null,String.valueOf(tcuad.size()),null,null));
						break;		
				case 44:arg2 = pila_sem.pop();
						op = pila_sem.pop();
						arg1 = pila_sem.pop();	
						cierto = arg1.cierto()+","+arg2.cierto();
						asocia(arg1.falso(),op.quad());
						falso = arg2.falso();
						pila_sem.push(new Reg(null,null,arg1.codigo()+","+arg2.codigo(),null,cierto,falso));
						break;
				case 46:arg1 = pila_sem.peek();
						cod = arg1.codigo();
						if(cod!=null && !cod.equals("")){
							int ind = Integer.valueOf(cod).intValue();
							q = (Cuad)tcuad.get(ind);
							q.set_op("[]=");
							tcuad.set(ind,q);
						}
						break; 
				case 47:pila_sem.push(new Reg(ps,String.valueOf(variables.insertar(ps)),null,null,null,null));
						break;
				case 50:arg2 = pila_sem.pop();
						arg1 = pila_sem.pop();
						t = nuevotemp();
						cod = gen("=[]",arg1.nombre(),arg2.nombre(),t);
						if(arg2.codigo()!=null && !arg2.codigo().equals("")) cod = arg2.codigo()+","+cod;
						pila_sem.push(new Reg(t,String.valueOf(variables.insertar(t)),cod,null,null,null));
						break;
				case 53:arg2 = pila_sem.pop();
						arg1 = pila_sem.pop();
						cod = gen(":=", arg2.nombre(), null, arg1.nombre());
						if(arg2.codigo()!=null) cod = arg2.codigo()+","+cod;
						pila_sem.push(new Reg(arg1.nombre(),null,cod,String.valueOf(tcuad.size()),null,null));
						break;
                case 54:arg2 = pila_sem.pop();
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
                case 55:arg2 = pila_sem.pop();
						arg1 = pila_sem.pop();
						variables.insertar(arg1.nombre(),Integer.valueOf(arg2.nombre()).intValue());
						break;
            }
        } finally{
		}
	}
}	
