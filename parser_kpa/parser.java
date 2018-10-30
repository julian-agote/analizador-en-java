package parser_kpa;
import java.util.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
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
	public parser(String s) throws IOException{
		TRegla regla;
		P = new Vector<TRegla>();
		regla = new TRegla("progP","prog $",0);
		P.addElement(regla);
		regla = new TRegla("prog","algoritmoa ID m0 dekl sentk",1);
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
		regla = new TRegla("objektu_mota","NUM osagai objektu_mota taula",9);
		P.addElement(regla);
		regla = new TRegla("id_zer","ID m0 id_zer_hond",10);
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
		regla = new TRegla("sent","var m5 ASIGN expr",16);
		P.addElement(regla);
		regla = new TRegla("sent","sentk",17);
		P.addElement(regla);
		regla = new TRegla("sent","irakur IRE_PAR ID m0 ITX_PAR",18);
		P.addElement(regla);
		regla = new TRegla("sent","idatz IRE_PAR expr ITX_PAR",19);
		P.addElement(regla);
		regla = new TRegla("sent","hemendik-hasita ID m5 ASIGN expr m6 heldu-arte expr m7 egin sent",20);
		P.addElement(regla);
		regla = new TRegla("sent","aukera baldintza_zer amaukera",21);
		P.addElement(regla);
		regla = new TRegla("baldintza_zer","expr BI_PUNTO mq sent m9 baldintza_zer",22);
		P.addElement(regla);
		regla = new TRegla("baldintza_zer","",23);
		P.addElement(regla);
		regla = new TRegla("sent","baldin expr orduan mq sent",24);
		P.addElement(regla);
		regla = new TRegla("sent","errepika mq sent_zer m10 harik-eta mq expr",25);
		P.addElement(regla);
		regla = new TRegla("sent","bitartean mq expr egin mq sent_zer m10 ambitartean",26);
		P.addElement(regla);
		regla = new TRegla("sent","sekuentzia-hasiera",27);
		P.addElement(regla);
		regla = new TRegla("expr","eand exprp",28);
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
		regla = new TRegla("var","lista ITX_MAKO",58);
		P.addElement(regla);
		regla = new TRegla("lista","lista COMA arit",59);
		P.addElement(regla);
		regla = new TRegla("lista","ID m0 IRE_MAKO arit",60);
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
		initablaira();
		initablaaccion();
		ent = new TEntrada();
		ent.gordeCad(s);
		try{
			gs = new TGestorSemantico();
		} catch (Exception e) {
			System.out.println("Error al inicializar el Gestor Semantico:"+e.getMessage());
		}	
	}
	TElemIra getElemIra(String simb, int est) {
		return new TElemIra(simb,est);
	}
	void initablaira(){
		TElemIra[][] bindVars = new TElemIra[][]{{getElemIra("algoritmoa",1),getElemIra("prog",2)},
			{getElemIra("ID",3)},
			{getElemIra("$",4)},
			{getElemIra("m0",5)},
			{},
			{getElemIra("aldagaiak",6),getElemIra("dekl",7)},
			{getElemIra("ID",8),getElemIra("id_zer",9)},
			{getElemIra("hasiera",10),getElemIra("sentk",11)},
			{getElemIra("m0",12)},
			{getElemIra("IRE_PAR",13)},
			{getElemIra("mq",14)},
			{},
			{getElemIra("COMA",15),getElemIra("id_zer_hond",16)},
			{getElemIra("NUM",17),getElemIra("karakterea",18),getElemIra("osoa",19),getElemIra("boolearra",20),getElemIra("objektu_mota",21)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent_zer",34),getElemIra("sent",35),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("ID",38)},
			{},
			{getElemIra("osagai",39)},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",40)},
			{getElemIra("m0",41)},
			{},
			{getElemIra("mq",42)},
			{getElemIra("IRE_PAR",43)},
			{getElemIra("IRE_PAR",44)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("OP_NOT",51),getElemIra("IRE_PAR",52),getElemIra("baldintza_zer",53),getElemIra("expr",54),getElemIra("eand",55),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("mq",63)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("OP_NOT",70),getElemIra("IRE_PAR",71),getElemIra("expr",72),getElemIra("eand",73),getElemIra("erel",74),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",81)},
			{getElemIra("mq",82)},
			{},
			{},
			{getElemIra("amaia",83)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent_zer",84),getElemIra("sent",35),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("m5",85)},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",87)},
			{getElemIra("m0",88)},
			{getElemIra("NUM",89),getElemIra("karakterea",90),getElemIra("osoa",91),getElemIra("boolearra",92),getElemIra("objektu_mota",93)},
			{getElemIra("m8",94)},
			{getElemIra("IRE_MAKO",95)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent_zer",96),getElemIra("sent",35),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("ID",97)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",106),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("fact",115),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("OP_NOT",51),getElemIra("IRE_PAR",52),getElemIra("expr",116),getElemIra("eand",55),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",117),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("amaukera",118)},
			{getElemIra("BI_PUNTO",119)},
			{getElemIra("OP_OR",120),getElemIra("exprp",121)},
			{getElemIra("OP_AND",122),getElemIra("eandp",123)},
			{getElemIra("OP_REL",124),getElemIra("erelp",125)},
			{getElemIra("OP_ADIT",126),getElemIra("aritp",127)},
			{getElemIra("OP_MULT",128),getElemIra("OP_DIV",129),getElemIra("OP_MOD",130),getElemIra("termp",131)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",132)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent_zer",144),getElemIra("sent",145),getElemIra("var",146),getElemIra("lista",37)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("fact",147),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("OP_NOT",70),getElemIra("IRE_PAR",71),getElemIra("expr",148),getElemIra("eand",73),getElemIra("erel",74),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",149),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("orduan",150)},
			{getElemIra("OP_OR",151),getElemIra("exprp",152)},
			{getElemIra("OP_AND",153),getElemIra("eandp",154)},
			{getElemIra("OP_REL",155),getElemIra("erelp",156)},
			{getElemIra("OP_ADIT",157),getElemIra("aritp",158)},
			{getElemIra("OP_MULT",159),getElemIra("OP_DIV",160),getElemIra("OP_MOD",161),getElemIra("termp",162)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",163)},
			{getElemIra("m5",164)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",173),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{},
			{},
			{getElemIra("ASIGN",182)},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("arit",190),getElemIra("term",191),getElemIra("fact",192),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{},
			{getElemIra("COMA",15),getElemIra("id_zer_hond",196)},
			{getElemIra("osagai",197)},
			{},
			{},
			{},
			{getElemIra("taula",198)},
			{getElemIra("ID",8),getElemIra("dekl_hond",199),getElemIra("id_zer",200)},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("arit",201),getElemIra("term",191),getElemIra("fact",192),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{getElemIra("amaia",202)},
			{getElemIra("m0",203)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("fact",204),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",205),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",206),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ITX_PAR",207)},
			{getElemIra("OP_OR",208),getElemIra("exprp",209)},
			{getElemIra("OP_AND",210),getElemIra("eandp",211)},
			{getElemIra("OP_REL",212),getElemIra("erelp",213)},
			{getElemIra("OP_ADIT",214),getElemIra("aritp",215)},
			{getElemIra("OP_MULT",216),getElemIra("OP_DIV",217),getElemIra("OP_MOD",218),getElemIra("termp",219)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",220)},
			{},
			{},
			{getElemIra("ITX_PAR",221)},
			{},
			{getElemIra("mq",222)},
			{getElemIra("mq",223)},
			{},
			{getElemIra("mq",224)},
			{},
			{getElemIra("m0",225)},
			{},
			{getElemIra("m0",226)},
			{},
			{getElemIra("m0",227)},
			{getElemIra("m0",228)},
			{getElemIra("m0",229)},
			{},
			{},
			{},
			{getElemIra("mq",230)},
			{getElemIra("IRE_PAR",231)},
			{getElemIra("IRE_PAR",232)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("OP_NOT",51),getElemIra("IRE_PAR",52),getElemIra("baldintza_zer",233),getElemIra("expr",54),getElemIra("eand",55),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("mq",234)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("OP_NOT",70),getElemIra("IRE_PAR",71),getElemIra("expr",235),getElemIra("eand",73),getElemIra("erel",74),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",236)},
			{getElemIra("mq",237)},
			{},
			{},
			{getElemIra("m10",238)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent_zer",239),getElemIra("sent",145),getElemIra("var",146),getElemIra("lista",37)},
			{getElemIra("m5",240)},
			{},
			{},
			{getElemIra("ITX_PAR",241)},
			{getElemIra("mq",242)},
			{getElemIra("mq",243)},
			{},
			{getElemIra("mq",244)},
			{},
			{getElemIra("m0",245)},
			{},
			{getElemIra("m0",246)},
			{},
			{getElemIra("m0",247)},
			{getElemIra("m0",248)},
			{getElemIra("m0",249)},
			{},
			{},
			{getElemIra("ASIGN",250)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("fact",251),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",252),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",253),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("egin",254)},
			{getElemIra("OP_OR",255),getElemIra("exprp",256)},
			{getElemIra("OP_AND",257),getElemIra("eandp",258)},
			{getElemIra("OP_REL",259),getElemIra("erelp",260)},
			{getElemIra("OP_ADIT",261),getElemIra("aritp",262)},
			{getElemIra("OP_MULT",263),getElemIra("OP_DIV",264),getElemIra("OP_MOD",265),getElemIra("termp",266)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",267)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("OP_NOT",274),getElemIra("IRE_PAR",275),getElemIra("expr",276),getElemIra("eand",277),getElemIra("erel",278),getElemIra("arit",279),getElemIra("term",280),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("fact",285),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",286),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{},
			{getElemIra("OP_ADIT",287),getElemIra("aritp",288)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",292)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",293)},
			{},
			{getElemIra("NUM",89),getElemIra("karakterea",90),getElemIra("osoa",91),getElemIra("boolearra",92),getElemIra("objektu_mota",294)},
			{},
			{},
			{getElemIra("IRE_PAR",295)},
			{},
			{},
			{getElemIra("ITX_PAR",296)},
			{},
			{},
			{getElemIra("ITX_PAR",297)},
			{},
			{getElemIra("mq",298)},
			{},
			{getElemIra("mq",299)},
			{},
			{getElemIra("m0",300)},
			{},
			{getElemIra("m0",301)},
			{},
			{getElemIra("m0",302)},
			{getElemIra("m0",303)},
			{getElemIra("m0",304)},
			{},
			{},
			{},
			{getElemIra("ID",22),getElemIra("P_COMA",305),getElemIra("hasiera",306),getElemIra("irakur",307),getElemIra("idatz",308),getElemIra("aukera",309),getElemIra("errepika",310),getElemIra("baldin",311),getElemIra("hemendik-hasita",312),getElemIra("bitartean",313),getElemIra("sekuentzia-hasiera",314),getElemIra("sentk",315),getElemIra("sent",316),getElemIra("var",317),getElemIra("lista",37)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("eand",318),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("erel",319),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("arit",320),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("term",321),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("fact",322),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("fact",323),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("IRE_PAR",52),getElemIra("fact",324),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent_zer",325),getElemIra("sent",35),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("ID",326)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",327),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("amaukera",328)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent_zer",329),getElemIra("sent",145),getElemIra("var",146),getElemIra("lista",37)},
			{getElemIra("orduan",330)},
			{getElemIra("m5",331)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",332),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("harik-eta",333)},
			{},
			{getElemIra("ASIGN",334)},
			{},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent",335),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("eand",336),getElemIra("erel",74),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("erel",337),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("arit",338),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("term",339),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("fact",340),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("fact",341),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("IRE_PAR",71),getElemIra("fact",342),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("OP_NOT",349),getElemIra("IRE_PAR",350),getElemIra("expr",351),getElemIra("eand",352),getElemIra("erel",353),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{},
			{},
			{getElemIra("ITX_PAR",360)},
			{getElemIra("mq",361)},
			{getElemIra("mq",362)},
			{},
			{getElemIra("mq",363)},
			{},
			{getElemIra("m0",364)},
			{},
			{getElemIra("m0",365)},
			{},
			{getElemIra("m0",366)},
			{getElemIra("m0",367)},
			{getElemIra("m0",368)},
			{},
			{},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("fact",369),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("OP_NOT",274),getElemIra("IRE_PAR",275),getElemIra("expr",370),getElemIra("eand",277),getElemIra("erel",278),getElemIra("arit",279),getElemIra("term",280),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",371),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{},
			{getElemIra("OP_OR",372),getElemIra("exprp",373)},
			{getElemIra("OP_AND",374),getElemIra("eandp",375)},
			{getElemIra("OP_REL",376),getElemIra("erelp",377)},
			{getElemIra("OP_ADIT",378),getElemIra("aritp",379)},
			{getElemIra("OP_MULT",380),getElemIra("OP_DIV",381),getElemIra("OP_MOD",382),getElemIra("termp",383)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",384)},
			{},
			{getElemIra("ITX_PAR",385)},
			{getElemIra("m0",386)},
			{},
			{getElemIra("m0",387)},
			{getElemIra("m0",388)},
			{getElemIra("m0",389)},
			{},
			{},
			{getElemIra("taula",390)},
			{getElemIra("NUM",17),getElemIra("karakterea",18),getElemIra("osoa",19),getElemIra("boolearra",20),getElemIra("objektu_mota",391)},
			{},
			{},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("eand",392),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("erel",393),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("arit",394),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("term",395),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("fact",396),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("fact",397),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("IRE_PAR",105),getElemIra("fact",398),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{},
			{getElemIra("mq",399)},
			{getElemIra("IRE_PAR",400)},
			{getElemIra("IRE_PAR",401)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("OP_NOT",51),getElemIra("IRE_PAR",52),getElemIra("baldintza_zer",402),getElemIra("expr",54),getElemIra("eand",55),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("mq",403)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("OP_NOT",70),getElemIra("IRE_PAR",71),getElemIra("expr",404),getElemIra("eand",73),getElemIra("erel",74),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",405)},
			{getElemIra("mq",406)},
			{},
			{},
			{getElemIra("m9",407)},
			{getElemIra("m5",408)},
			{getElemIra("m4",409)},
			{getElemIra("m3",410)},
			{getElemIra("m2",411)},
			{getElemIra("m1",412)},
			{getElemIra("m1",413)},
			{getElemIra("m1",414)},
			{getElemIra("m1",415)},
			{getElemIra("amaia",416)},
			{getElemIra("m0",417)},
			{getElemIra("ITX_PAR",418)},
			{},
			{getElemIra("m10",419)},
			{getElemIra("mq",420)},
			{getElemIra("ASIGN",421)},
			{getElemIra("egin",422)},
			{getElemIra("mq",423)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("OP_NOT",430),getElemIra("IRE_PAR",431),getElemIra("expr",432),getElemIra("eand",433),getElemIra("erel",434),getElemIra("arit",435),getElemIra("term",436),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{},
			{getElemIra("m4",441)},
			{getElemIra("m3",442)},
			{getElemIra("m2",443)},
			{getElemIra("m1",444)},
			{getElemIra("m1",445)},
			{getElemIra("m1",446)},
			{getElemIra("m1",447)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("fact",448),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("OP_NOT",349),getElemIra("IRE_PAR",350),getElemIra("expr",449),getElemIra("eand",352),getElemIra("erel",353),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",450),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("m6",451)},
			{getElemIra("OP_OR",452),getElemIra("exprp",453)},
			{getElemIra("OP_AND",454),getElemIra("eandp",455)},
			{getElemIra("OP_REL",456),getElemIra("erelp",457)},
			{getElemIra("OP_ADIT",458),getElemIra("aritp",459)},
			{getElemIra("OP_MULT",460),getElemIra("OP_DIV",461),getElemIra("OP_MOD",462),getElemIra("termp",463)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",464)},
			{},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent_zer",476),getElemIra("sent",477),getElemIra("var",478),getElemIra("lista",37)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("eand",479),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("erel",480),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("arit",481),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("term",482),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("fact",483),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("fact",484),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("IRE_PAR",172),getElemIra("fact",485),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{},
			{},
			{getElemIra("ITX_PAR",486)},
			{getElemIra("mq",487)},
			{},
			{getElemIra("mq",488)},
			{},
			{getElemIra("m0",489)},
			{},
			{getElemIra("m0",490)},
			{},
			{getElemIra("m0",491)},
			{getElemIra("m0",492)},
			{getElemIra("m0",493)},
			{},
			{},
			{},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("term",494),getElemIra("fact",192),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("fact",495),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("fact",496),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{getElemIra("ID",183),getElemIra("NUM",184),getElemIra("KAR",185),getElemIra("KATEA",186),getElemIra("BOOL",187),getElemIra("OP_ADIT",188),getElemIra("IRE_PAR",189),getElemIra("fact",497),getElemIra("rando",193),getElemIra("var",194),getElemIra("lista",195)},
			{},
			{getElemIra("ITX_PAR",498)},
			{getElemIra("m4",499)},
			{getElemIra("m3",500)},
			{getElemIra("m2",501)},
			{getElemIra("m1",502)},
			{getElemIra("m1",503)},
			{getElemIra("m1",504)},
			{getElemIra("m1",505)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent_zer",506),getElemIra("sent",35),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("ID",507)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",508),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("amaukera",509)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent_zer",510),getElemIra("sent",145),getElemIra("var",146),getElemIra("lista",37)},
			{getElemIra("orduan",511)},
			{getElemIra("m5",512)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",513),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("OP_NOT",51),getElemIra("IRE_PAR",52),getElemIra("baldintza_zer",514),getElemIra("expr",54),getElemIra("eand",55),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("ASIGN",515)},
			{getElemIra("OP_OR",120),getElemIra("exprp",516)},
			{getElemIra("OP_AND",122),getElemIra("eandp",517)},
			{getElemIra("OP_REL",124),getElemIra("erelp",518)},
			{getElemIra("OP_ADIT",126),getElemIra("aritp",519)},
			{getElemIra("OP_MULT",128),getElemIra("OP_DIV",129),getElemIra("OP_MOD",130),getElemIra("termp",520)},
			{getElemIra("OP_MULT",128),getElemIra("OP_DIV",129),getElemIra("OP_MOD",130),getElemIra("termp",521)},
			{getElemIra("OP_MULT",128),getElemIra("OP_DIV",129),getElemIra("OP_MOD",130),getElemIra("termp",522)},
			{},
			{getElemIra("ITX_PAR",523)},
			{},
			{getElemIra("harik-eta",524)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent",525),getElemIra("var",146),getElemIra("lista",37)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("OP_NOT",349),getElemIra("IRE_PAR",350),getElemIra("expr",526),getElemIra("eand",352),getElemIra("erel",353),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("mq",527)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("OP_NOT",274),getElemIra("IRE_PAR",275),getElemIra("expr",528),getElemIra("eand",277),getElemIra("erel",278),getElemIra("arit",279),getElemIra("term",280),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("fact",529),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("OP_NOT",430),getElemIra("IRE_PAR",431),getElemIra("expr",530),getElemIra("eand",433),getElemIra("erel",434),getElemIra("arit",435),getElemIra("term",436),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",531),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{},
			{getElemIra("OP_OR",532),getElemIra("exprp",533)},
			{getElemIra("OP_AND",534),getElemIra("eandp",535)},
			{getElemIra("OP_REL",536),getElemIra("erelp",537)},
			{getElemIra("OP_ADIT",538),getElemIra("aritp",539)},
			{getElemIra("OP_MULT",540),getElemIra("OP_DIV",541),getElemIra("OP_MOD",542),getElemIra("termp",543)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",544)},
			{getElemIra("OP_OR",151),getElemIra("exprp",545)},
			{getElemIra("OP_AND",153),getElemIra("eandp",546)},
			{getElemIra("OP_REL",155),getElemIra("erelp",547)},
			{getElemIra("OP_ADIT",157),getElemIra("aritp",548)},
			{getElemIra("OP_MULT",159),getElemIra("OP_DIV",160),getElemIra("OP_MOD",161),getElemIra("termp",549)},
			{getElemIra("OP_MULT",159),getElemIra("OP_DIV",160),getElemIra("OP_MOD",161),getElemIra("termp",550)},
			{getElemIra("OP_MULT",159),getElemIra("OP_DIV",160),getElemIra("OP_MOD",161),getElemIra("termp",551)},
			{},
			{},
			{getElemIra("ITX_PAR",552)},
			{getElemIra("heldu-arte",553)},
			{getElemIra("mq",554)},
			{},
			{getElemIra("mq",555)},
			{},
			{getElemIra("m0",556)},
			{},
			{getElemIra("m0",557)},
			{},
			{getElemIra("m0",558)},
			{getElemIra("m0",559)},
			{getElemIra("m0",560)},
			{},
			{},
			{},
			{getElemIra("mq",561)},
			{getElemIra("IRE_PAR",562)},
			{getElemIra("IRE_PAR",563)},
			{getElemIra("ID",45),getElemIra("NUM",46),getElemIra("KAR",47),getElemIra("KATEA",48),getElemIra("BOOL",49),getElemIra("OP_ADIT",50),getElemIra("OP_NOT",51),getElemIra("IRE_PAR",52),getElemIra("baldintza_zer",564),getElemIra("expr",54),getElemIra("eand",55),getElemIra("erel",56),getElemIra("arit",57),getElemIra("term",58),getElemIra("fact",59),getElemIra("rando",60),getElemIra("var",61),getElemIra("lista",62)},
			{getElemIra("mq",565)},
			{getElemIra("ID",64),getElemIra("NUM",65),getElemIra("KAR",66),getElemIra("KATEA",67),getElemIra("BOOL",68),getElemIra("OP_ADIT",69),getElemIra("OP_NOT",70),getElemIra("IRE_PAR",71),getElemIra("expr",566),getElemIra("eand",73),getElemIra("erel",74),getElemIra("arit",75),getElemIra("term",76),getElemIra("fact",77),getElemIra("rando",78),getElemIra("var",79),getElemIra("lista",80)},
			{getElemIra("ID",567)},
			{getElemIra("mq",568)},
			{},
			{},
			{getElemIra("m10",569)},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent_zer",570),getElemIra("sent",477),getElemIra("var",478),getElemIra("lista",37)},
			{getElemIra("m5",571)},
			{getElemIra("m4",572)},
			{getElemIra("m3",573)},
			{getElemIra("m2",574)},
			{getElemIra("m1",575)},
			{getElemIra("m1",576)},
			{getElemIra("m1",577)},
			{getElemIra("m1",578)},
			{},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("eand",579),getElemIra("erel",278),getElemIra("arit",279),getElemIra("term",280),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("erel",580),getElemIra("arit",279),getElemIra("term",280),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("arit",581),getElemIra("term",280),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("term",582),getElemIra("fact",281),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("fact",583),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("fact",584),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("ID",268),getElemIra("NUM",269),getElemIra("KAR",270),getElemIra("KATEA",271),getElemIra("BOOL",272),getElemIra("OP_ADIT",273),getElemIra("IRE_PAR",275),getElemIra("fact",585),getElemIra("rando",282),getElemIra("var",283),getElemIra("lista",284)},
			{getElemIra("m1",586)},
			{getElemIra("m1",587)},
			{getElemIra("m1",588)},
			{getElemIra("m1",589)},
			{getElemIra("m8",590)},
			{getElemIra("OP_OR",208),getElemIra("exprp",591)},
			{getElemIra("OP_AND",210),getElemIra("eandp",592)},
			{getElemIra("OP_REL",212),getElemIra("erelp",593)},
			{getElemIra("OP_ADIT",214),getElemIra("aritp",594)},
			{getElemIra("OP_MULT",216),getElemIra("OP_DIV",217),getElemIra("OP_MOD",218),getElemIra("termp",595)},
			{getElemIra("OP_MULT",216),getElemIra("OP_DIV",217),getElemIra("OP_MOD",218),getElemIra("termp",596)},
			{getElemIra("OP_MULT",216),getElemIra("OP_DIV",217),getElemIra("OP_MOD",218),getElemIra("termp",597)},
			{getElemIra("amaia",598)},
			{getElemIra("m0",599)},
			{getElemIra("ITX_PAR",600)},
			{},
			{getElemIra("m10",601)},
			{getElemIra("mq",602)},
			{getElemIra("ASIGN",603)},
			{getElemIra("egin",604)},
			{},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("OP_NOT",611),getElemIra("IRE_PAR",612),getElemIra("expr",613),getElemIra("eand",614),getElemIra("erel",615),getElemIra("arit",616),getElemIra("term",617),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("mq",622)},
			{},
			{getElemIra("m6",623)},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent_zer",624),getElemIra("sent",477),getElemIra("var",478),getElemIra("lista",37)},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",625)},
			{getElemIra("mq",626)},
			{},
			{getElemIra("mq",627)},
			{},
			{getElemIra("m0",628)},
			{},
			{getElemIra("m0",629)},
			{},
			{getElemIra("m0",630)},
			{getElemIra("m0",631)},
			{getElemIra("m0",632)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",633),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("eand",634),getElemIra("erel",353),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("erel",635),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("arit",636),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("term",637),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("fact",638),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("fact",639),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("IRE_PAR",350),getElemIra("fact",640),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent_zer",641),getElemIra("sent",35),getElemIra("var",36),getElemIra("lista",37)},
			{getElemIra("ID",642)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",643),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{getElemIra("amaukera",644)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent_zer",645),getElemIra("sent",145),getElemIra("var",146),getElemIra("lista",37)},
			{getElemIra("orduan",646)},
			{getElemIra("m5",647)},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",648),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ambitartean",649)},
			{},
			{getElemIra("ASIGN",650)},
			{getElemIra("OP_OR",255),getElemIra("exprp",651)},
			{getElemIra("OP_AND",257),getElemIra("eandp",652)},
			{getElemIra("OP_REL",259),getElemIra("erelp",653)},
			{getElemIra("OP_ADIT",261),getElemIra("aritp",654)},
			{getElemIra("OP_MULT",263),getElemIra("OP_DIV",264),getElemIra("OP_MOD",265),getElemIra("termp",655)},
			{getElemIra("OP_MULT",263),getElemIra("OP_DIV",264),getElemIra("OP_MOD",265),getElemIra("termp",656)},
			{getElemIra("OP_MULT",263),getElemIra("OP_DIV",264),getElemIra("OP_MOD",265),getElemIra("termp",657)},
			{getElemIra("m4",658)},
			{getElemIra("m3",659)},
			{getElemIra("m2",660)},
			{getElemIra("m1",661)},
			{getElemIra("m1",662)},
			{getElemIra("m1",663)},
			{getElemIra("m1",664)},
			{getElemIra("OP_ADIT",287),getElemIra("aritp",665)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",666)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",667)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",668)},
			{getElemIra("ID",8),getElemIra("dekl_hond",669),getElemIra("id_zer",200)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",670)},
			{},
			{getElemIra("harik-eta",671)},
			{getElemIra("ID",22),getElemIra("P_COMA",305),getElemIra("hasiera",306),getElemIra("irakur",307),getElemIra("idatz",308),getElemIra("aukera",309),getElemIra("errepika",310),getElemIra("baldin",311),getElemIra("hemendik-hasita",312),getElemIra("bitartean",313),getElemIra("sekuentzia-hasiera",314),getElemIra("sentk",315),getElemIra("sent",672),getElemIra("var",317),getElemIra("lista",37)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("OP_NOT",349),getElemIra("IRE_PAR",350),getElemIra("expr",673),getElemIra("eand",352),getElemIra("erel",353),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("mq",674)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("fact",675),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("OP_NOT",611),getElemIra("IRE_PAR",612),getElemIra("expr",676),getElemIra("eand",614),getElemIra("erel",615),getElemIra("arit",616),getElemIra("term",617),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",677),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{},
			{getElemIra("OP_OR",678),getElemIra("exprp",679)},
			{getElemIra("OP_AND",680),getElemIra("eandp",681)},
			{getElemIra("OP_REL",682),getElemIra("erelp",683)},
			{getElemIra("OP_ADIT",684),getElemIra("aritp",685)},
			{getElemIra("OP_MULT",686),getElemIra("OP_DIV",687),getElemIra("OP_MOD",688),getElemIra("termp",689)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",690)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("OP_NOT",430),getElemIra("IRE_PAR",431),getElemIra("expr",691),getElemIra("eand",433),getElemIra("erel",434),getElemIra("arit",435),getElemIra("term",436),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("heldu-arte",692)},
			{getElemIra("m10",693)},
			{},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("eand",694),getElemIra("erel",434),getElemIra("arit",435),getElemIra("term",436),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("erel",695),getElemIra("arit",435),getElemIra("term",436),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("arit",696),getElemIra("term",436),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("term",697),getElemIra("fact",437),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("fact",698),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("fact",699),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("ID",424),getElemIra("NUM",425),getElemIra("KAR",426),getElemIra("KATEA",427),getElemIra("BOOL",428),getElemIra("OP_ADIT",429),getElemIra("IRE_PAR",431),getElemIra("fact",700),getElemIra("rando",438),getElemIra("var",439),getElemIra("lista",440)},
			{getElemIra("m7",701)},
			{getElemIra("m4",702)},
			{getElemIra("m3",703)},
			{getElemIra("m2",704)},
			{getElemIra("m1",705)},
			{getElemIra("m1",706)},
			{getElemIra("m1",707)},
			{getElemIra("m1",708)},
			{getElemIra("amaia",709)},
			{getElemIra("m0",710)},
			{getElemIra("ITX_PAR",711)},
			{},
			{getElemIra("m10",712)},
			{getElemIra("mq",713)},
			{getElemIra("ASIGN",714)},
			{getElemIra("egin",715)},
			{},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("OP_NOT",722),getElemIra("IRE_PAR",723),getElemIra("expr",724),getElemIra("eand",725),getElemIra("erel",726),getElemIra("arit",727),getElemIra("term",728),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("OP_OR",372),getElemIra("exprp",733)},
			{getElemIra("OP_AND",374),getElemIra("eandp",734)},
			{getElemIra("OP_REL",376),getElemIra("erelp",735)},
			{getElemIra("OP_ADIT",378),getElemIra("aritp",736)},
			{getElemIra("OP_MULT",380),getElemIra("OP_DIV",381),getElemIra("OP_MOD",382),getElemIra("termp",737)},
			{getElemIra("OP_MULT",380),getElemIra("OP_DIV",381),getElemIra("OP_MOD",382),getElemIra("termp",738)},
			{getElemIra("OP_MULT",380),getElemIra("OP_DIV",381),getElemIra("OP_MOD",382),getElemIra("termp",739)},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("mq",740)},
			{},
			{getElemIra("m6",741)},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent_zer",742),getElemIra("sent",477),getElemIra("var",478),getElemIra("lista",37)},
			{},
			{},
			{getElemIra("ITX_PAR",743)},
			{getElemIra("mq",744)},
			{},
			{getElemIra("mq",745)},
			{},
			{getElemIra("m0",746)},
			{},
			{getElemIra("m0",747)},
			{},
			{getElemIra("m0",748)},
			{getElemIra("m0",749)},
			{getElemIra("m0",750)},
			{},
			{},
			{},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",751),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ambitartean",752)},
			{getElemIra("m4",753)},
			{getElemIra("m3",754)},
			{getElemIra("m2",755)},
			{getElemIra("m1",756)},
			{getElemIra("m1",757)},
			{getElemIra("m1",758)},
			{getElemIra("m1",759)},
			{getElemIra("egin",760)},
			{getElemIra("OP_OR",452),getElemIra("exprp",761)},
			{getElemIra("OP_AND",454),getElemIra("eandp",762)},
			{getElemIra("OP_REL",456),getElemIra("erelp",763)},
			{getElemIra("OP_ADIT",458),getElemIra("aritp",764)},
			{getElemIra("OP_MULT",460),getElemIra("OP_DIV",461),getElemIra("OP_MOD",462),getElemIra("termp",765)},
			{getElemIra("OP_MULT",460),getElemIra("OP_DIV",461),getElemIra("OP_MOD",462),getElemIra("termp",766)},
			{getElemIra("OP_MULT",460),getElemIra("OP_DIV",461),getElemIra("OP_MOD",462),getElemIra("termp",767)},
			{},
			{getElemIra("ITX_PAR",768)},
			{},
			{getElemIra("harik-eta",769)},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent",770),getElemIra("var",478),getElemIra("lista",37)},
			{getElemIra("ID",343),getElemIra("NUM",344),getElemIra("KAR",345),getElemIra("KATEA",346),getElemIra("BOOL",347),getElemIra("OP_ADIT",348),getElemIra("OP_NOT",349),getElemIra("IRE_PAR",350),getElemIra("expr",771),getElemIra("eand",352),getElemIra("erel",353),getElemIra("arit",354),getElemIra("term",355),getElemIra("fact",356),getElemIra("rando",357),getElemIra("var",358),getElemIra("lista",359)},
			{getElemIra("mq",772)},
			{getElemIra("m0",41)},
			{},
			{},
			{},
			{},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("fact",773),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("OP_NOT",722),getElemIra("IRE_PAR",723),getElemIra("expr",774),getElemIra("eand",725),getElemIra("erel",726),getElemIra("arit",727),getElemIra("term",728),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("KAR",100),getElemIra("KATEA",101),getElemIra("BOOL",102),getElemIra("OP_ADIT",103),getElemIra("OP_NOT",104),getElemIra("IRE_PAR",105),getElemIra("expr",775),getElemIra("eand",107),getElemIra("erel",108),getElemIra("arit",109),getElemIra("term",110),getElemIra("fact",111),getElemIra("rando",112),getElemIra("var",113),getElemIra("lista",114)},
			{},
			{getElemIra("OP_OR",776),getElemIra("exprp",777)},
			{getElemIra("OP_AND",778),getElemIra("eandp",779)},
			{getElemIra("OP_REL",780),getElemIra("erelp",781)},
			{getElemIra("OP_ADIT",782),getElemIra("aritp",783)},
			{getElemIra("OP_MULT",784),getElemIra("OP_DIV",785),getElemIra("OP_MOD",786),getElemIra("termp",787)},
			{},
			{},
			{getElemIra("COMA",86),getElemIra("ITX_MAKO",788)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("OP_NOT",611),getElemIra("IRE_PAR",612),getElemIra("expr",789),getElemIra("eand",614),getElemIra("erel",615),getElemIra("arit",616),getElemIra("term",617),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("heldu-arte",790)},
			{getElemIra("m10",791)},
			{},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("eand",792),getElemIra("erel",615),getElemIra("arit",616),getElemIra("term",617),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("erel",793),getElemIra("arit",616),getElemIra("term",617),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("arit",794),getElemIra("term",617),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("term",795),getElemIra("fact",618),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("fact",796),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("fact",797),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("ID",605),getElemIra("NUM",606),getElemIra("KAR",607),getElemIra("KATEA",608),getElemIra("BOOL",609),getElemIra("OP_ADIT",610),getElemIra("IRE_PAR",612),getElemIra("fact",798),getElemIra("rando",619),getElemIra("var",620),getElemIra("lista",621)},
			{getElemIra("m7",799)},
			{},
			{getElemIra("OP_OR",532),getElemIra("exprp",800)},
			{getElemIra("OP_AND",534),getElemIra("eandp",801)},
			{getElemIra("OP_REL",536),getElemIra("erelp",802)},
			{getElemIra("OP_ADIT",538),getElemIra("aritp",803)},
			{getElemIra("OP_MULT",540),getElemIra("OP_DIV",541),getElemIra("OP_MOD",542),getElemIra("termp",804)},
			{getElemIra("OP_MULT",540),getElemIra("OP_DIV",541),getElemIra("OP_MOD",542),getElemIra("termp",805)},
			{getElemIra("OP_MULT",540),getElemIra("OP_DIV",541),getElemIra("OP_MOD",542),getElemIra("termp",806)},
			{getElemIra("ID",22),getElemIra("P_COMA",23),getElemIra("hasiera",24),getElemIra("irakur",25),getElemIra("idatz",26),getElemIra("aukera",27),getElemIra("errepika",28),getElemIra("baldin",29),getElemIra("hemendik-hasita",30),getElemIra("bitartean",31),getElemIra("sekuentzia-hasiera",32),getElemIra("sentk",33),getElemIra("sent",807),getElemIra("var",36),getElemIra("lista",37)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("mq",808)},
			{},
			{getElemIra("m6",809)},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent_zer",810),getElemIra("sent",477),getElemIra("var",478),getElemIra("lista",37)},
			{},
			{},
			{getElemIra("ITX_PAR",811)},
			{getElemIra("mq",812)},
			{},
			{getElemIra("mq",813)},
			{},
			{getElemIra("m0",814)},
			{},
			{getElemIra("m0",815)},
			{},
			{getElemIra("m0",816)},
			{getElemIra("m0",817)},
			{getElemIra("m0",818)},
			{},
			{},
			{},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",819),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ambitartean",820)},
			{getElemIra("m4",821)},
			{getElemIra("m3",822)},
			{getElemIra("m2",823)},
			{getElemIra("m1",824)},
			{getElemIra("m1",825)},
			{getElemIra("m1",826)},
			{getElemIra("m1",827)},
			{getElemIra("egin",828)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("OP_NOT",722),getElemIra("IRE_PAR",723),getElemIra("expr",829),getElemIra("eand",725),getElemIra("erel",726),getElemIra("arit",727),getElemIra("term",728),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("heldu-arte",830)},
			{getElemIra("m10",831)},
			{},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("eand",832),getElemIra("erel",726),getElemIra("arit",727),getElemIra("term",728),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("erel",833),getElemIra("arit",727),getElemIra("term",728),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("arit",834),getElemIra("term",728),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("term",835),getElemIra("fact",729),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("fact",836),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("fact",837),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("ID",716),getElemIra("NUM",717),getElemIra("KAR",718),getElemIra("KATEA",719),getElemIra("BOOL",720),getElemIra("OP_ADIT",721),getElemIra("IRE_PAR",723),getElemIra("fact",838),getElemIra("rando",730),getElemIra("var",731),getElemIra("lista",732)},
			{getElemIra("m7",839)},
			{},
			{getElemIra("OP_OR",678),getElemIra("exprp",840)},
			{getElemIra("OP_AND",680),getElemIra("eandp",841)},
			{getElemIra("OP_REL",682),getElemIra("erelp",842)},
			{getElemIra("OP_ADIT",684),getElemIra("aritp",843)},
			{getElemIra("OP_MULT",686),getElemIra("OP_DIV",687),getElemIra("OP_MOD",688),getElemIra("termp",844)},
			{getElemIra("OP_MULT",686),getElemIra("OP_DIV",687),getElemIra("OP_MOD",688),getElemIra("termp",845)},
			{getElemIra("OP_MULT",686),getElemIra("OP_DIV",687),getElemIra("OP_MOD",688),getElemIra("termp",846)},
			{getElemIra("ID",22),getElemIra("P_COMA",133),getElemIra("hasiera",134),getElemIra("irakur",135),getElemIra("idatz",136),getElemIra("aukera",137),getElemIra("errepika",138),getElemIra("baldin",139),getElemIra("hemendik-hasita",140),getElemIra("bitartean",141),getElemIra("sekuentzia-hasiera",142),getElemIra("sentk",143),getElemIra("sent",847),getElemIra("var",146),getElemIra("lista",37)},
			{},
			{getElemIra("ID",165),getElemIra("NUM",166),getElemIra("KAR",167),getElemIra("KATEA",168),getElemIra("BOOL",169),getElemIra("OP_ADIT",170),getElemIra("OP_NOT",171),getElemIra("IRE_PAR",172),getElemIra("expr",848),getElemIra("eand",174),getElemIra("erel",175),getElemIra("arit",176),getElemIra("term",177),getElemIra("fact",178),getElemIra("rando",179),getElemIra("var",180),getElemIra("lista",181)},
			{getElemIra("ambitartean",849)},
			{getElemIra("m4",850)},
			{getElemIra("m3",851)},
			{getElemIra("m2",852)},
			{getElemIra("m1",853)},
			{getElemIra("m1",854)},
			{getElemIra("m1",855)},
			{getElemIra("m1",856)},
			{getElemIra("egin",857)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("m7",858)},
			{},
			{getElemIra("OP_OR",776),getElemIra("exprp",859)},
			{getElemIra("OP_AND",778),getElemIra("eandp",860)},
			{getElemIra("OP_REL",780),getElemIra("erelp",861)},
			{getElemIra("OP_ADIT",782),getElemIra("aritp",862)},
			{getElemIra("OP_MULT",784),getElemIra("OP_DIV",785),getElemIra("OP_MOD",786),getElemIra("termp",863)},
			{getElemIra("OP_MULT",784),getElemIra("OP_DIV",785),getElemIra("OP_MOD",786),getElemIra("termp",864)},
			{getElemIra("OP_MULT",784),getElemIra("OP_DIV",785),getElemIra("OP_MOD",786),getElemIra("termp",865)},
			{getElemIra("ID",22),getElemIra("P_COMA",305),getElemIra("hasiera",306),getElemIra("irakur",307),getElemIra("idatz",308),getElemIra("aukera",309),getElemIra("errepika",310),getElemIra("baldin",311),getElemIra("hemendik-hasita",312),getElemIra("bitartean",313),getElemIra("sekuentzia-hasiera",314),getElemIra("sentk",315),getElemIra("sent",866),getElemIra("var",317),getElemIra("lista",37)},
			{getElemIra("egin",867)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ID",22),getElemIra("P_COMA",465),getElemIra("hasiera",466),getElemIra("irakur",467),getElemIra("idatz",468),getElemIra("aukera",469),getElemIra("errepika",470),getElemIra("baldin",471),getElemIra("hemendik-hasita",472),getElemIra("bitartean",473),getElemIra("sekuentzia-hasiera",474),getElemIra("sentk",475),getElemIra("sent",868),getElemIra("var",478),getElemIra("lista",37)},
			{}};
		tablaira = new Vector<Hashtable>();
		for(int z = 0;z<bindVars.length;z++){
			Hashtable ira = new Hashtable();
			for(int k=0;k<bindVars[z].length;k++)
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
		TEltoAccion[][] bindVars = new TEltoAccion[869][];
		BufferedReader inputStream = null;
		String l;
		int fila = 0,ncol;
		try{
			inputStream = new BufferedReader(new FileReader("tabla_acc.txt"));
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
				System.out.println("Falta entrada en accion estado="+String.valueOf(est)+" con="+sim.token);
				error = true;
			}	
		} while (!error && !eltoacc.tipo.equalsIgnoreCase("Aceptar"));
		return error;
	}
}
