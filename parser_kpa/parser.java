package parser_kpa;
import java.util.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
/* ---------------- Gramatica: grama_kpa.txt ------------------
prog
algoritmoa ID BI_PUNTO P_COMA NUM KAR KATEA aldagaiak hasiera amaia ASIGN irakur idatz aukera amaukera errepika harik-eta baldin orduan hemendik-hasita heldu-arte bitartean egin ambitartean deitu karakterea osoa boolearra taula osagai BOOL OP_OR OP_AND OP_REL OP_ADIT OP_MULT OP_NOT NUM IRE_PAR ITX_PAR BOOL COMA OP_DIV OP_MOD bestela ekintza karakteretako osotako itzuli 
prog dekl dekl_hond sentk sent_zer sent objektu_mota id_zer id_zer_hond baldintza_zer expr exprp eand eandp erel erelp arit aritp term termp fact rando m0 m1 m2 m3 mq m4 m5 m6 var lista m7 m8 m9 m10 m11 bestelakoa beste_ekintzak itzulketa m12 m13 parametroak m14 
1 prog -> algoritmoa ID m12 dekl sentk m13 beste_ekintzak
2 dekl -> 
3 dekl -> aldagaiak id_zer IRE_PAR objektu_mota ITX_PAR m8 dekl_hond
4 dekl_hond -> id_zer IRE_PAR objektu_mota ITX_PAR m8 dekl_hond
5 dekl_hond -> 
6 objektu_mota -> karakterea
7 objektu_mota -> osoa
8 objektu_mota -> boolearra
9 objektu_mota -> NUM m0 osagai objektu_mota taula
10 id_zer -> ID m14 id_zer_hond
11 id_zer_hond -> COMA ID m0 id_zer_hond
12 id_zer_hond -> 
13 sentk -> hasiera mq sent_zer amaia
14 sent_zer -> sent sent_zer
15 sent_zer -> 
16 sent -> var m5 ASIGN arit
17 sent -> sentk
18 sent -> irakur IRE_PAR ID m0 ITX_PAR
19 sent -> idatz IRE_PAR arit ITX_PAR
20 sent -> hemendik-hasita ID m5 ASIGN arit m6 heldu-arte expr m7 egin sent
21 sent -> aukera mq baldintza_zer mq amaukera
22 baldintza_zer -> expr BI_PUNTO mq sent m9 baldintza_zer
23 baldintza_zer -> 
24 sent -> baldin expr orduan mq sent bestelakoa
25 sent -> errepika mq sent_zer m10 harik-eta mq expr
26 sent -> bitartean mq expr egin mq sent_zer m10 ambitartean
27 sent -> deitu var
28 expr -> eand m11 exprp
29 exprp -> 
30 exprp -> OP_OR mq eand m4 exprp
31 eand -> erel eandp
32 eandp -> 
33 eandp -> OP_AND mq erel m3 eandp
34 erel -> arit erelp
35 erelp -> 
36 erelp -> OP_REL m0 arit m2 erelp
37 arit -> term aritp
38 aritp -> 
39 aritp -> OP_ADIT m0 term m1 aritp
40 term -> fact termp
41 termp -> 
42 termp -> OP_MULT m0 fact m1 termp
43 expr -> OP_NOT expr
44 fact -> OP_ADIT fact
45 fact -> rando
46 rando -> NUM
47 rando -> var
48 rando -> IRE_PAR expr ITX_PAR
49 m0 -> 
50 m1 -> 
51 m2 -> 
52 m3 -> 
53 mq -> 
54 m4 -> 
55 rando -> BOOL
56 m5 -> 
57 var -> ID
58 var -> lista ITX_PAR
59 lista -> lista COMA arit
60 lista -> ID m14 IRE_PAR arit
61 termp -> OP_DIV m0 fact m1 termp
62 termp -> OP_MOD m0 fact m1 termp
63 m6 -> 
64 m7 -> 
65 m8 -> 
66 m9 -> 
67 rando -> KAR
68 rando -> KATEA
69 sent -> P_COMA
70 m10 -> 
71 m11 -> 
72 bestelakoa -> bestela mq sent
73 bestelakoa -> 
74 beste_ekintzak -> ekintza ID m12 parametroak itzulketa dekl sentk m13 beste_ekintzak
75 beste_ekintzak -> 
76 objektu_mota -> karakteretako
77 itzulketa -> itzuli dekl_hond
78 parametroak -> IRE_PAR dekl_hond ITX_PAR
79 m12 -> 
80 m13 -> 
81 objektu_mota -> osotako
82 m14 -> 
83 var -> id_zer
84 parametroak -> 
85 itzulketa -> 
---------------------------------------------------------------- */
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
class EltoAcc {
    String tipo;
    TRegla R;
    int sig_estado;
	EltoAcc(String ptipo,TRegla pr,int psig_estado){
		tipo = new String(ptipo);
		R = pr;
		sig_estado = psig_estado;
	}	
	EltoAcc(){
		tipo = new String();
		R = null;
		sig_estado = -1;
	}
}
public class parser{
	Vector<TRegla> P;
	Vector<Hashtable> tablaira;
	Vector<Hashtable> tablaaccion;
	TTablaSimbolos ts;
	TEntrada ent;
	TGestorSemantico gs;
	public parser(String s,String _opcion) throws IOException{
		TRegla regla;
		P = new Vector<TRegla>();
		regla = new TRegla("progP","prog $",0);
		P.addElement(regla);
		regla = new TRegla("prog","algoritmoa ID m12 dekl sentk m13 beste_ekintzak",1);
		P.addElement(regla);
		regla = new TRegla("dekl","",2);
		P.addElement(regla);
		regla = new TRegla("dekl","aldagaiak id_zer IRE_PAR objektu_mota ITX_PAR m8 dekl_hond",3);
		P.addElement(regla);
		regla = new TRegla("dekl_hond","id_zer IRE_PAR objektu_mota ITX_PAR m8 dekl_hond",4);
		P.addElement(regla);
		regla = new TRegla("dekl_hond","",5);
		P.addElement(regla);
		regla = new TRegla("objektu_mota","karakterea",6);
		P.addElement(regla);
		regla = new TRegla("objektu_mota","osoa",7);
		P.addElement(regla);
		regla = new TRegla("objektu_mota","boolearra",8);
		P.addElement(regla);
		regla = new TRegla("objektu_mota","NUM m0 osagai objektu_mota taula",9);
		P.addElement(regla);
		regla = new TRegla("id_zer","ID m14 id_zer_hond",10);
		P.addElement(regla);
		regla = new TRegla("id_zer_hond","COMA ID m0 id_zer_hond",11);
		P.addElement(regla);
		regla = new TRegla("id_zer_hond","",12);
		P.addElement(regla);
		regla = new TRegla("sentk","hasiera mq sent_zer amaia",13);
		P.addElement(regla);
		regla = new TRegla("sent_zer","sent sent_zer",14);
		P.addElement(regla);
		regla = new TRegla("sent_zer","",15);
		P.addElement(regla);
		regla = new TRegla("sent","var m5 ASIGN arit",16);
		P.addElement(regla);
		regla = new TRegla("sent","sentk",17);
		P.addElement(regla);
		regla = new TRegla("sent","irakur IRE_PAR ID m0 ITX_PAR",18);
		P.addElement(regla);
		regla = new TRegla("sent","idatz IRE_PAR arit ITX_PAR",19);
		P.addElement(regla);
		regla = new TRegla("sent","hemendik-hasita ID m5 ASIGN arit m6 heldu-arte expr m7 egin sent",20);
		P.addElement(regla);
		regla = new TRegla("sent","aukera mq baldintza_zer mq amaukera",21);
		P.addElement(regla);
		regla = new TRegla("baldintza_zer","expr BI_PUNTO mq sent m9 baldintza_zer",22);
		P.addElement(regla);
		regla = new TRegla("baldintza_zer","",23);
		P.addElement(regla);
		regla = new TRegla("sent","baldin expr orduan mq sent bestelakoa",24);
		P.addElement(regla);
		regla = new TRegla("sent","errepika mq sent_zer m10 harik-eta mq expr",25);
		P.addElement(regla);
		regla = new TRegla("sent","bitartean mq expr egin mq sent_zer m10 ambitartean",26);
		P.addElement(regla);
		regla = new TRegla("sent","deitu var",27);
		P.addElement(regla);
		regla = new TRegla("expr","eand m11 exprp",28);
		P.addElement(regla);
		regla = new TRegla("exprp","",29);
		P.addElement(regla);
		regla = new TRegla("exprp","OP_OR mq eand m4 exprp",30);
		P.addElement(regla);
		regla = new TRegla("eand","erel eandp",31);
		P.addElement(regla);
		regla = new TRegla("eandp","",32);
		P.addElement(regla);
		regla = new TRegla("eandp","OP_AND mq erel m3 eandp",33);
		P.addElement(regla);
		regla = new TRegla("erel","arit erelp",34);
		P.addElement(regla);
		regla = new TRegla("erelp","",35);
		P.addElement(regla);
		regla = new TRegla("erelp","OP_REL m0 arit m2 erelp",36);
		P.addElement(regla);
		regla = new TRegla("arit","term aritp",37);
		P.addElement(regla);
		regla = new TRegla("aritp","",38);
		P.addElement(regla);
		regla = new TRegla("aritp","OP_ADIT m0 term m1 aritp",39);
		P.addElement(regla);
		regla = new TRegla("term","fact termp",40);
		P.addElement(regla);
		regla = new TRegla("termp","",41);
		P.addElement(regla);
		regla = new TRegla("termp","OP_MULT m0 fact m1 termp",42);
		P.addElement(regla);
		regla = new TRegla("expr","OP_NOT expr",43);
		P.addElement(regla);
		regla = new TRegla("fact","OP_ADIT fact",44);
		P.addElement(regla);
		regla = new TRegla("fact","rando",45);
		P.addElement(regla);
		regla = new TRegla("rando","NUM",46);
		P.addElement(regla);
		regla = new TRegla("rando","var",47);
		P.addElement(regla);
		regla = new TRegla("rando","IRE_PAR expr ITX_PAR",48);
		P.addElement(regla);
		regla = new TRegla("m0","",49);
		P.addElement(regla);
		regla = new TRegla("m1","",50);
		P.addElement(regla);
		regla = new TRegla("m2","",51);
		P.addElement(regla);
		regla = new TRegla("m3","",52);
		P.addElement(regla);
		regla = new TRegla("mq","",53);
		P.addElement(regla);
		regla = new TRegla("m4","",54);
		P.addElement(regla);
		regla = new TRegla("rando","BOOL",55);
		P.addElement(regla);
		regla = new TRegla("m5","",56);
		P.addElement(regla);
		regla = new TRegla("var","ID",57);
		P.addElement(regla);
		regla = new TRegla("var","lista ITX_PAR",58);
		P.addElement(regla);
		regla = new TRegla("lista","lista COMA arit",59);
		P.addElement(regla);
		regla = new TRegla("lista","ID m14 IRE_PAR arit",60);
		P.addElement(regla);
		regla = new TRegla("termp","OP_DIV m0 fact m1 termp",61);
		P.addElement(regla);
		regla = new TRegla("termp","OP_MOD m0 fact m1 termp",62);
		P.addElement(regla);
		regla = new TRegla("m6","",63);
		P.addElement(regla);
		regla = new TRegla("m7","",64);
		P.addElement(regla);
		regla = new TRegla("m8","",65);
		P.addElement(regla);
		regla = new TRegla("m9","",66);
		P.addElement(regla);
		regla = new TRegla("rando","KAR",67);
		P.addElement(regla);
		regla = new TRegla("rando","KATEA",68);
		P.addElement(regla);
		regla = new TRegla("sent","P_COMA",69);
		P.addElement(regla);
		regla = new TRegla("m10","",70);
		P.addElement(regla);
		regla = new TRegla("m11","",71);
		P.addElement(regla);
		regla = new TRegla("bestelakoa","bestela mq sent",72);
		P.addElement(regla);
		regla = new TRegla("bestelakoa","",73);
		P.addElement(regla);
		regla = new TRegla("beste_ekintzak","ekintza ID m12 parametroak itzulketa dekl sentk m13 beste_ekintzak",74);
		P.addElement(regla);
		regla = new TRegla("beste_ekintzak","",75);
		P.addElement(regla);
		regla = new TRegla("objektu_mota","karakteretako",76);
		P.addElement(regla);
		regla = new TRegla("itzulketa","itzuli dekl_hond",77);
		P.addElement(regla);
		regla = new TRegla("parametroak","IRE_PAR dekl_hond ITX_PAR",78);
		P.addElement(regla);
		regla = new TRegla("m12","",79);
		P.addElement(regla);
		regla = new TRegla("m13","",80);
		P.addElement(regla);
		regla = new TRegla("objektu_mota","osotako",81);
		P.addElement(regla);
		regla = new TRegla("m14","",82);
		P.addElement(regla);
		regla = new TRegla("var","id_zer",83);
		P.addElement(regla);
		regla = new TRegla("parametroak","",84);
		P.addElement(regla);
		regla = new TRegla("itzulketa","",85);
		P.addElement(regla);
		initablaira();
		initablaaccion();
		ent = new TEntrada();
		ent.gordeCad(s);
		try{
			gs = new TGestorSemantico(_opcion,ent);
		} catch (Exception e) {
			System.out.println("Error al inicializar el Gestor Semantico:"+e.getMessage());
		}	
	}
	TElemIra getElemIra(String simb, int est) {
		return new TElemIra(simb,est);
	}
	void initablaira()  throws IOException {
		TElemIra[][] bindVars = new TElemIra[1997][];
		BufferedReader inputStream = null;
		String l;
		int fila = 0,ncol;
		try{
		  inputStream = new BufferedReader(new FileReader("tabla_ira_kpa.txt"));
		  while((l=inputStream.readLine())!=null){        
		    if(!l.equals("")){ 
		      String[] vars= l.split(",");
		      ncol =vars.length/2;
		      bindVars[fila] = new TElemIra[ncol];
		      for(int i=0;i<ncol;i++)
		          bindVars[fila][i] = getElemIra(vars[i*2],Integer.parseInt(vars[i*2+1]));
		    }else{
		          bindVars[fila] = new TElemIra[1];
		          bindVars[fila][0] = getElemIra("",0);
		    }
		    fila++;
		  }   
		}finally{
		  if(inputStream!=null)
		      inputStream.close();
		}
		tablaira = new Vector<Hashtable>();
		for(int z = 0;z<bindVars.length;z++){
		  Hashtable ira = new Hashtable();
		  for(int k=0;k<bindVars[z].length;k++)
		      if(!bindVars[z][k].simbolo.equals(""))
		          ira.put(bindVars[z][k].simbolo,new Integer(bindVars[z][k].nestado));
		  tablaira.addElement(ira);
		}
	}
	TEltoAccion getEltoAccion(String ptipo,String psimbolo,TRegla pr){
		return new TEltoAccion(ptipo,psimbolo,pr);
	}
	TEltoAccion getEltoAccion(String ptipo,String psimbolo,int psig_estado){
		return new TEltoAccion(ptipo,psimbolo,psig_estado);
	}
	void initablaaccion() throws IOException {
		TEltoAccion[][] bindVars = new TEltoAccion[1997][];
		BufferedReader inputStream = null;
		String l;
		int fila = 0,ncol;
		try{
			inputStream = new BufferedReader(new FileReader("tabla_acc_kpa.txt"));
			while((l=inputStream.readLine())!=null){		
			  String[] vars= l.split(",");
			  if(vars.length%6==0)
			  	ncol =vars.length/6;
			  else
				ncol =(vars.length+1)/6;  
			  bindVars[fila] = new TEltoAccion[ncol];
			  for(int i=0;i<ncol;i++)
				if(!vars[i*6+2].isEmpty())
					bindVars[fila][i] = getEltoAccion(vars[i*6],vars[i*6+1],new TRegla(vars[i*6+2],new String(vars[i*6+3]),Integer.parseInt(vars[i*6+4])));
				else
					bindVars[fila][i] = getEltoAccion(vars[i*6],vars[i*6+1],Integer.parseInt(vars[i*6+5]));
			  fila++;
			}	
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}
		tablaaccion = new Vector<Hashtable>();
		for(int z = 0;z<bindVars.length;z++){
			Hashtable acc = new Hashtable();
			for(int k=0;k<bindVars[z].length;k++)
				acc.put(bindVars[z][k].simbolo,new EltoAcc(bindVars[z][k].tipo,bindVars[z][k].R,bindVars[z][k].sig_estado));
			tablaaccion.addElement(acc);
		}
	}
	public boolean parserascendente() throws IOException  {
		Stack<String> pila = new Stack<String>();
		String tipo, strest;
		TElemEnt sim, sim_ant;
		Hashtable ira;
		Hashtable acc;
		EltoAcc eltoacc;
		int est, nest, longi;
		TRegla regla;
		strest = "";
		boolean error;
		this.ts = new TTablaSimbolos();
		error = false;
		pila.push("0");
		sim = ent.leersigterm(new TElemEnt(),this.ts);
		sim_ant = sim;
		do {
			est = Integer.parseInt(pila.peek());
			acc = this.tablaaccion.elementAt(est);
			eltoacc = (EltoAcc)acc.get(sim.token);
			if (eltoacc !=null){
				if (eltoacc.tipo.equalsIgnoreCase("Desplazar")) {
					nest = eltoacc.sig_estado;
					pila.push(String.valueOf(nest));
					sim_ant = sim;
					sim = ent.leersigterm(sim,this.ts);
					//System.out.println(pila.toString()+"|"+ent.faltaLeer()+"|");
				} else if (eltoacc.tipo.equalsIgnoreCase("Reducir")) {
					regla = eltoacc.R;
					longi = regla.rhs.size();
					for (int i = 0; i < longi; i++)
						strest = pila.pop();
					nest = Integer.parseInt(pila.peek());
					ira = this.tablaira.elementAt(nest);
					if(ira.containsKey(regla.lhs)){
						nest = ((Integer)ira.get(regla.lhs)).intValue();
						pila.push(String.valueOf(nest));
						//System.out.println(pila.toString()+"|"+ent.faltaLeer()+"| regla=" + String.valueOf(regla.num));
						this.gs.ejecutarAccionSemantica(regla.num,(sim_ant.get_atributo()>=0)?this.ts.leer(sim_ant.get_atributo()):sim_ant.token);
					}else{
						System.out.println("Falta ira en estado="+String.valueOf(nest)+" con vble="+regla.lhs+" despues reducir regla="+ String.valueOf(regla.num));
						error = true;
					}
				}
			} else {
				System.out.println("Falta entrada en accion estado="+String.valueOf(est)+" con="+sim.token+" "+ent.faltaLeer());
				error = true;
			}	
		} while (!error && !eltoacc.tipo.equalsIgnoreCase("Aceptar"));
		return error;
	}
}
