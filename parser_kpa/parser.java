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
		regla = new TRegla("prog","algoritmoa ID dekl sentk",1);
		P.addElement(regla);
		regla = new TRegla("dekl","",2);
		P.addElement(regla);
		regla = new TRegla("dekl","aldagaiak id_zer m0 IRE_PAR objektu_mota m0 ITX_PAR dekl_hond",3);
		P.addElement(regla);
		regla = new TRegla("dekl_hond","id_zer m0 IRE_PAR objektu_mota m0 ITX_PAR dekl_hond",4);
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
		regla = new TRegla("id_zer","ID id_zer_hond",10);
		P.addElement(regla);
		regla = new TRegla("id_zer_hond","COMA ID id_zer_hond",11);
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
		regla = new TRegla("sent","irakur IRE_PAR ID ITX_PAR",18);
		P.addElement(regla);
		regla = new TRegla("sent","idatz IRE_PAR expr ITX_PAR",19);
		P.addElement(regla);
		regla = new TRegla("sent","hemendik-hasita ID m5 ASIGN expr m6 heldu-arte expr m7 egin sent",20);
		P.addElement(regla);
		regla = new TRegla("sent","aukera baldintza_zer amaukera",21);
		P.addElement(regla);
		regla = new TRegla("baldintza_zer","expr BI_PUNTO sent baldintza_zer",22);
		P.addElement(regla);
		regla = new TRegla("baldintza_zer","",23);
		P.addElement(regla);
		regla = new TRegla("sent","baldin expr orduan mq sent",24);
		P.addElement(regla);
		regla = new TRegla("sent","errepika sent_zer hariketa expr",25);
		P.addElement(regla);
		regla = new TRegla("sent","bitartean mq expr egin mq sent_zer ambitartean",26);
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
			{getElemIra("aldagaiak",5),getElemIra("dekl",6)},
			{},
			{getElemIra("ID",7),getElemIra("id_zer",8)},
			{getElemIra("hasiera",9),getElemIra("sentk",10)},
			{getElemIra("COMA",11),getElemIra("id_zer_hond",12)},
			{getElemIra("m0",13)},
			{getElemIra("mq",14)},
			{},
			{getElemIra("ID",15)},
			{},
			{getElemIra("IRE_PAR",16)},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent_zer",28),getElemIra("sent",29),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("COMA",11),getElemIra("id_zer_hond",32)},
			{getElemIra("NUM",33),getElemIra("karakterea",34),getElemIra("osoa",35),getElemIra("boolearra",36),getElemIra("objektu_mota",37)},
			{getElemIra("m0",38)},
			{getElemIra("mq",39)},
			{getElemIra("IRE_PAR",40)},
			{getElemIra("IRE_PAR",41)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("OP_NOT",46),getElemIra("IRE_PAR",47),getElemIra("baldintza_zer",48),getElemIra("expr",49),getElemIra("eand",50),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent_zer",68),getElemIra("sent",69),getElemIra("var",70),getElemIra("lista",31)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("OP_NOT",75),getElemIra("IRE_PAR",76),getElemIra("expr",77),getElemIra("eand",78),getElemIra("erel",79),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",86)},
			{getElemIra("mq",87)},
			{},
			{},
			{getElemIra("amaia",88)},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent_zer",89),getElemIra("sent",29),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("m5",90)},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",92)},
			{},
			{getElemIra("osagai",93)},
			{},
			{},
			{},
			{getElemIra("m0",94)},
			{getElemIra("IRE_MAKO",95)},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent_zer",96),getElemIra("sent",29),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("ID",97)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",104),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("fact",113),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("OP_NOT",46),getElemIra("IRE_PAR",47),getElemIra("expr",114),getElemIra("eand",50),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",115),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("amaukera",116)},
			{getElemIra("BI_PUNTO",117)},
			{getElemIra("OP_OR",118),getElemIra("exprp",119)},
			{getElemIra("OP_AND",120),getElemIra("eandp",121)},
			{getElemIra("OP_REL",122),getElemIra("erelp",123)},
			{getElemIra("OP_ADIT",124),getElemIra("aritp",125)},
			{getElemIra("OP_MULT",126),getElemIra("OP_DIV",127),getElemIra("OP_MOD",128),getElemIra("termp",129)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",130)},
			{getElemIra("mq",131)},
			{getElemIra("IRE_PAR",132)},
			{getElemIra("IRE_PAR",133)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("OP_NOT",46),getElemIra("IRE_PAR",47),getElemIra("baldintza_zer",134),getElemIra("expr",49),getElemIra("eand",50),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent_zer",135),getElemIra("sent",69),getElemIra("var",70),getElemIra("lista",31)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("OP_NOT",75),getElemIra("IRE_PAR",76),getElemIra("expr",136),getElemIra("eand",78),getElemIra("erel",79),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",137)},
			{getElemIra("mq",138)},
			{},
			{},
			{getElemIra("hariketa",139)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent_zer",140),getElemIra("sent",69),getElemIra("var",70),getElemIra("lista",31)},
			{getElemIra("m5",141)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("fact",142),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("OP_NOT",75),getElemIra("IRE_PAR",76),getElemIra("expr",143),getElemIra("eand",78),getElemIra("erel",79),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",144),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("orduan",145)},
			{getElemIra("OP_OR",146),getElemIra("exprp",147)},
			{getElemIra("OP_AND",148),getElemIra("eandp",149)},
			{getElemIra("OP_REL",150),getElemIra("erelp",151)},
			{getElemIra("OP_ADIT",152),getElemIra("aritp",153)},
			{getElemIra("OP_MULT",154),getElemIra("OP_DIV",155),getElemIra("OP_MOD",156),getElemIra("termp",157)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",158)},
			{getElemIra("m5",159)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",166),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{},
			{getElemIra("ASIGN",175)},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("arit",181),getElemIra("term",182),getElemIra("fact",183),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{},
			{getElemIra("NUM",187),getElemIra("karakterea",188),getElemIra("osoa",189),getElemIra("boolearra",190),getElemIra("objektu_mota",191)},
			{getElemIra("ITX_PAR",192)},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("arit",193),getElemIra("term",182),getElemIra("fact",183),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{getElemIra("amaia",194)},
			{getElemIra("ITX_PAR",195)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("fact",196),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",197),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",198),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ITX_PAR",199)},
			{getElemIra("OP_OR",200),getElemIra("exprp",201)},
			{getElemIra("OP_AND",202),getElemIra("eandp",203)},
			{getElemIra("OP_REL",204),getElemIra("erelp",205)},
			{getElemIra("OP_ADIT",206),getElemIra("aritp",207)},
			{getElemIra("OP_MULT",208),getElemIra("OP_DIV",209),getElemIra("OP_MOD",210),getElemIra("termp",211)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",212)},
			{},
			{},
			{getElemIra("ITX_PAR",213)},
			{},
			{getElemIra("ID",17),getElemIra("hasiera",214),getElemIra("irakur",215),getElemIra("idatz",216),getElemIra("aukera",217),getElemIra("errepika",218),getElemIra("baldin",219),getElemIra("hemendik-hasita",220),getElemIra("bitartean",221),getElemIra("sekuentzia-hasiera",222),getElemIra("sentk",223),getElemIra("sent",224),getElemIra("var",225),getElemIra("lista",31)},
			{getElemIra("mq",226)},
			{},
			{getElemIra("mq",227)},
			{},
			{getElemIra("m0",228)},
			{},
			{getElemIra("m0",229)},
			{},
			{getElemIra("m0",230)},
			{getElemIra("m0",231)},
			{getElemIra("m0",232)},
			{},
			{},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent_zer",233),getElemIra("sent",29),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("ID",234)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",235),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("amaukera",236)},
			{getElemIra("hariketa",237)},
			{getElemIra("orduan",238)},
			{getElemIra("m5",239)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",240),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("OP_NOT",245),getElemIra("IRE_PAR",246),getElemIra("expr",247),getElemIra("eand",248),getElemIra("erel",249),getElemIra("arit",250),getElemIra("term",251),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{},
			{getElemIra("ASIGN",256)},
			{},
			{},
			{getElemIra("ITX_PAR",257)},
			{getElemIra("mq",258)},
			{getElemIra("mq",259)},
			{},
			{getElemIra("mq",260)},
			{},
			{getElemIra("m0",261)},
			{},
			{getElemIra("m0",262)},
			{},
			{getElemIra("m0",263)},
			{getElemIra("m0",264)},
			{getElemIra("m0",265)},
			{},
			{},
			{getElemIra("ASIGN",266)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("fact",267),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",268),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",269),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("egin",270)},
			{getElemIra("OP_OR",271),getElemIra("exprp",272)},
			{getElemIra("OP_AND",273),getElemIra("eandp",274)},
			{getElemIra("OP_REL",275),getElemIra("erelp",276)},
			{getElemIra("OP_ADIT",277),getElemIra("aritp",278)},
			{getElemIra("OP_MULT",279),getElemIra("OP_DIV",280),getElemIra("OP_MOD",281),getElemIra("termp",282)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",283)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("OP_NOT",245),getElemIra("IRE_PAR",246),getElemIra("expr",284),getElemIra("eand",248),getElemIra("erel",249),getElemIra("arit",250),getElemIra("term",251),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("fact",285),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",286),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{},
			{getElemIra("OP_ADIT",287),getElemIra("aritp",288)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",292)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",293)},
			{getElemIra("osagai",294)},
			{},
			{},
			{},
			{getElemIra("taula",295)},
			{getElemIra("ID",7),getElemIra("dekl_hond",296),getElemIra("id_zer",297)},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",298)},
			{},
			{getElemIra("mq",299)},
			{},
			{getElemIra("mq",300)},
			{},
			{getElemIra("m0",301)},
			{},
			{getElemIra("m0",302)},
			{},
			{getElemIra("m0",303)},
			{getElemIra("m0",304)},
			{getElemIra("m0",305)},
			{},
			{},
			{},
			{getElemIra("mq",306)},
			{getElemIra("IRE_PAR",307)},
			{getElemIra("IRE_PAR",308)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("OP_NOT",46),getElemIra("IRE_PAR",47),getElemIra("baldintza_zer",309),getElemIra("expr",49),getElemIra("eand",50),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent_zer",310),getElemIra("sent",69),getElemIra("var",70),getElemIra("lista",31)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("OP_NOT",75),getElemIra("IRE_PAR",76),getElemIra("expr",311),getElemIra("eand",78),getElemIra("erel",79),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",312)},
			{getElemIra("mq",313)},
			{},
			{},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("OP_NOT",46),getElemIra("IRE_PAR",47),getElemIra("baldintza_zer",314),getElemIra("expr",49),getElemIra("eand",50),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("m5",315)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("eand",316),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("erel",317),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("arit",318),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("term",319),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("fact",320),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("fact",321),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("IRE_PAR",47),getElemIra("fact",322),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("amaia",323)},
			{getElemIra("ITX_PAR",324)},
			{getElemIra("ITX_PAR",325)},
			{},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("OP_NOT",330),getElemIra("IRE_PAR",331),getElemIra("expr",332),getElemIra("eand",333),getElemIra("erel",334),getElemIra("arit",335),getElemIra("term",336),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("mq",341)},
			{getElemIra("ASIGN",342)},
			{getElemIra("egin",343)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("fact",344),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("OP_NOT",245),getElemIra("IRE_PAR",246),getElemIra("expr",345),getElemIra("eand",248),getElemIra("erel",249),getElemIra("arit",250),getElemIra("term",251),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",346),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{},
			{getElemIra("OP_OR",347),getElemIra("exprp",348)},
			{getElemIra("OP_AND",349),getElemIra("eandp",350)},
			{getElemIra("OP_REL",351),getElemIra("erelp",352)},
			{getElemIra("OP_ADIT",353),getElemIra("aritp",354)},
			{getElemIra("OP_MULT",355),getElemIra("OP_DIV",356),getElemIra("OP_MOD",357),getElemIra("termp",358)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",359)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("OP_NOT",330),getElemIra("IRE_PAR",331),getElemIra("expr",360),getElemIra("eand",333),getElemIra("erel",334),getElemIra("arit",335),getElemIra("term",336),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent",361),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("eand",362),getElemIra("erel",79),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("erel",363),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("arit",364),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("term",365),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("fact",366),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("fact",367),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("IRE_PAR",76),getElemIra("fact",368),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("OP_NOT",373),getElemIra("IRE_PAR",374),getElemIra("expr",375),getElemIra("eand",376),getElemIra("erel",377),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{},
			{},
			{getElemIra("ITX_PAR",384)},
			{getElemIra("mq",385)},
			{getElemIra("mq",386)},
			{},
			{getElemIra("mq",387)},
			{},
			{getElemIra("m0",388)},
			{},
			{getElemIra("m0",389)},
			{},
			{getElemIra("m0",390)},
			{getElemIra("m0",391)},
			{getElemIra("m0",392)},
			{},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",393)},
			{getElemIra("m0",394)},
			{},
			{getElemIra("m0",395)},
			{getElemIra("m0",396)},
			{getElemIra("m0",397)},
			{},
			{},
			{getElemIra("NUM",187),getElemIra("karakterea",188),getElemIra("osoa",189),getElemIra("boolearra",190),getElemIra("objektu_mota",398)},
			{},
			{},
			{getElemIra("m0",399)},
			{},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("eand",400),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("erel",401),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("arit",402),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("term",403),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("fact",404),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("fact",405),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("IRE_PAR",103),getElemIra("fact",406),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent_zer",407),getElemIra("sent",29),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("ID",408)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",409),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("amaukera",410)},
			{getElemIra("hariketa",411)},
			{getElemIra("orduan",412)},
			{getElemIra("m5",413)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",414),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{getElemIra("ASIGN",415)},
			{getElemIra("m4",416)},
			{getElemIra("m3",417)},
			{getElemIra("m2",418)},
			{getElemIra("m1",419)},
			{getElemIra("m1",420)},
			{getElemIra("m1",421)},
			{getElemIra("m1",422)},
			{},
			{},
			{},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("fact",423),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("OP_NOT",330),getElemIra("IRE_PAR",331),getElemIra("expr",424),getElemIra("eand",333),getElemIra("erel",334),getElemIra("arit",335),getElemIra("term",336),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",425),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{},
			{getElemIra("OP_OR",426),getElemIra("exprp",427)},
			{getElemIra("OP_AND",428),getElemIra("eandp",429)},
			{getElemIra("OP_REL",430),getElemIra("erelp",431)},
			{getElemIra("OP_ADIT",432),getElemIra("aritp",433)},
			{getElemIra("OP_MULT",434),getElemIra("OP_DIV",435),getElemIra("OP_MOD",436),getElemIra("termp",437)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",438)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent",439),getElemIra("var",70),getElemIra("lista",31)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("OP_NOT",373),getElemIra("IRE_PAR",374),getElemIra("expr",440),getElemIra("eand",376),getElemIra("erel",377),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("mq",441)},
			{},
			{},
			{getElemIra("ITX_PAR",442)},
			{getElemIra("mq",443)},
			{},
			{getElemIra("mq",444)},
			{},
			{getElemIra("m0",445)},
			{},
			{getElemIra("m0",446)},
			{},
			{getElemIra("m0",447)},
			{getElemIra("m0",448)},
			{getElemIra("m0",449)},
			{},
			{},
			{},
			{},
			{getElemIra("m4",450)},
			{getElemIra("m3",451)},
			{getElemIra("m2",452)},
			{getElemIra("m1",453)},
			{getElemIra("m1",454)},
			{getElemIra("m1",455)},
			{getElemIra("m1",456)},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("fact",457),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("OP_NOT",373),getElemIra("IRE_PAR",374),getElemIra("expr",458),getElemIra("eand",376),getElemIra("erel",377),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",459),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("m6",460)},
			{getElemIra("OP_OR",461),getElemIra("exprp",462)},
			{getElemIra("OP_AND",463),getElemIra("eandp",464)},
			{getElemIra("OP_REL",465),getElemIra("erelp",466)},
			{getElemIra("OP_ADIT",467),getElemIra("aritp",468)},
			{getElemIra("OP_MULT",469),getElemIra("OP_DIV",470),getElemIra("OP_MOD",471),getElemIra("termp",472)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",473)},
			{},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent_zer",484),getElemIra("sent",485),getElemIra("var",486),getElemIra("lista",31)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("eand",487),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("erel",488),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("arit",489),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("term",490),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("fact",491),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("fact",492),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("IRE_PAR",165),getElemIra("fact",493),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("term",494),getElemIra("fact",183),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("fact",495),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("fact",496),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{getElemIra("ID",176),getElemIra("NUM",177),getElemIra("BOOL",178),getElemIra("OP_ADIT",179),getElemIra("IRE_PAR",180),getElemIra("fact",497),getElemIra("rando",184),getElemIra("var",185),getElemIra("lista",186)},
			{getElemIra("taula",498)},
			{getElemIra("IRE_PAR",499)},
			{getElemIra("m4",500)},
			{getElemIra("m3",501)},
			{getElemIra("m2",502)},
			{getElemIra("m1",503)},
			{getElemIra("m1",504)},
			{getElemIra("m1",505)},
			{getElemIra("m1",506)},
			{getElemIra("amaia",507)},
			{getElemIra("ITX_PAR",508)},
			{getElemIra("ITX_PAR",509)},
			{},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("OP_NOT",514),getElemIra("IRE_PAR",515),getElemIra("expr",516),getElemIra("eand",517),getElemIra("erel",518),getElemIra("arit",519),getElemIra("term",520),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("mq",525)},
			{getElemIra("ASIGN",526)},
			{getElemIra("egin",527)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("OP_NOT",514),getElemIra("IRE_PAR",515),getElemIra("expr",528),getElemIra("eand",517),getElemIra("erel",518),getElemIra("arit",519),getElemIra("term",520),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("OP_OR",118),getElemIra("exprp",529)},
			{getElemIra("OP_AND",120),getElemIra("eandp",530)},
			{getElemIra("OP_REL",122),getElemIra("erelp",531)},
			{getElemIra("OP_ADIT",124),getElemIra("aritp",532)},
			{getElemIra("OP_MULT",126),getElemIra("OP_DIV",127),getElemIra("OP_MOD",128),getElemIra("termp",533)},
			{getElemIra("OP_MULT",126),getElemIra("OP_DIV",127),getElemIra("OP_MOD",128),getElemIra("termp",534)},
			{getElemIra("OP_MULT",126),getElemIra("OP_DIV",127),getElemIra("OP_MOD",128),getElemIra("termp",535)},
			{},
			{},
			{getElemIra("ITX_PAR",536)},
			{getElemIra("mq",537)},
			{},
			{getElemIra("mq",538)},
			{},
			{getElemIra("m0",539)},
			{},
			{getElemIra("m0",540)},
			{},
			{getElemIra("m0",541)},
			{getElemIra("m0",542)},
			{getElemIra("m0",543)},
			{},
			{},
			{},
			{getElemIra("m6",544)},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent_zer",545),getElemIra("sent",485),getElemIra("var",486),getElemIra("lista",31)},
			{},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("eand",546),getElemIra("erel",249),getElemIra("arit",250),getElemIra("term",251),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("erel",547),getElemIra("arit",250),getElemIra("term",251),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("arit",548),getElemIra("term",251),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("term",549),getElemIra("fact",252),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("fact",550),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("fact",551),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("ID",241),getElemIra("NUM",242),getElemIra("BOOL",243),getElemIra("OP_ADIT",244),getElemIra("IRE_PAR",246),getElemIra("fact",552),getElemIra("rando",253),getElemIra("var",254),getElemIra("lista",255)},
			{getElemIra("OP_OR",146),getElemIra("exprp",553)},
			{getElemIra("OP_AND",148),getElemIra("eandp",554)},
			{getElemIra("OP_REL",150),getElemIra("erelp",555)},
			{getElemIra("OP_ADIT",152),getElemIra("aritp",556)},
			{getElemIra("OP_MULT",154),getElemIra("OP_DIV",155),getElemIra("OP_MOD",156),getElemIra("termp",557)},
			{getElemIra("OP_MULT",154),getElemIra("OP_DIV",155),getElemIra("OP_MOD",156),getElemIra("termp",558)},
			{getElemIra("OP_MULT",154),getElemIra("OP_DIV",155),getElemIra("OP_MOD",156),getElemIra("termp",559)},
			{},
			{},
			{getElemIra("ITX_PAR",560)},
			{getElemIra("heldu-arte",561)},
			{getElemIra("mq",562)},
			{},
			{getElemIra("mq",563)},
			{},
			{getElemIra("m0",564)},
			{},
			{getElemIra("m0",565)},
			{},
			{getElemIra("m0",566)},
			{getElemIra("m0",567)},
			{getElemIra("m0",568)},
			{},
			{},
			{getElemIra("mq",569)},
			{getElemIra("IRE_PAR",570)},
			{getElemIra("IRE_PAR",571)},
			{getElemIra("ID",42),getElemIra("NUM",43),getElemIra("BOOL",44),getElemIra("OP_ADIT",45),getElemIra("OP_NOT",46),getElemIra("IRE_PAR",47),getElemIra("baldintza_zer",572),getElemIra("expr",49),getElemIra("eand",50),getElemIra("erel",51),getElemIra("arit",52),getElemIra("term",53),getElemIra("fact",54),getElemIra("rando",55),getElemIra("var",56),getElemIra("lista",57)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent_zer",573),getElemIra("sent",69),getElemIra("var",70),getElemIra("lista",31)},
			{getElemIra("ID",71),getElemIra("NUM",72),getElemIra("BOOL",73),getElemIra("OP_ADIT",74),getElemIra("OP_NOT",75),getElemIra("IRE_PAR",76),getElemIra("expr",574),getElemIra("eand",78),getElemIra("erel",79),getElemIra("arit",80),getElemIra("term",81),getElemIra("fact",82),getElemIra("rando",83),getElemIra("var",84),getElemIra("lista",85)},
			{getElemIra("ID",575)},
			{getElemIra("mq",576)},
			{},
			{},
			{getElemIra("ambitartean",577)},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent_zer",578),getElemIra("sent",485),getElemIra("var",486),getElemIra("lista",31)},
			{getElemIra("m5",579)},
			{getElemIra("m4",580)},
			{getElemIra("m3",581)},
			{getElemIra("m2",582)},
			{getElemIra("m1",583)},
			{getElemIra("m1",584)},
			{getElemIra("m1",585)},
			{getElemIra("m1",586)},
			{getElemIra("m1",587)},
			{getElemIra("m1",588)},
			{getElemIra("m1",589)},
			{getElemIra("m1",590)},
			{},
			{getElemIra("NUM",33),getElemIra("karakterea",34),getElemIra("osoa",35),getElemIra("boolearra",36),getElemIra("objektu_mota",591)},
			{getElemIra("OP_OR",200),getElemIra("exprp",592)},
			{getElemIra("OP_AND",202),getElemIra("eandp",593)},
			{getElemIra("OP_REL",204),getElemIra("erelp",594)},
			{getElemIra("OP_ADIT",206),getElemIra("aritp",595)},
			{getElemIra("OP_MULT",208),getElemIra("OP_DIV",209),getElemIra("OP_MOD",210),getElemIra("termp",596)},
			{getElemIra("OP_MULT",208),getElemIra("OP_DIV",209),getElemIra("OP_MOD",210),getElemIra("termp",597)},
			{getElemIra("OP_MULT",208),getElemIra("OP_DIV",209),getElemIra("OP_MOD",210),getElemIra("termp",598)},
			{},
			{},
			{},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("fact",599),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("OP_NOT",514),getElemIra("IRE_PAR",515),getElemIra("expr",600),getElemIra("eand",517),getElemIra("erel",518),getElemIra("arit",519),getElemIra("term",520),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",601),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{},
			{getElemIra("OP_OR",602),getElemIra("exprp",603)},
			{getElemIra("OP_AND",604),getElemIra("eandp",605)},
			{getElemIra("OP_REL",606),getElemIra("erelp",607)},
			{getElemIra("OP_ADIT",608),getElemIra("aritp",609)},
			{getElemIra("OP_MULT",610),getElemIra("OP_DIV",611),getElemIra("OP_MOD",612),getElemIra("termp",613)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",614)},
			{getElemIra("ID",17),getElemIra("hasiera",214),getElemIra("irakur",215),getElemIra("idatz",216),getElemIra("aukera",217),getElemIra("errepika",218),getElemIra("baldin",219),getElemIra("hemendik-hasita",220),getElemIra("bitartean",221),getElemIra("sekuentzia-hasiera",222),getElemIra("sentk",223),getElemIra("sent",615),getElemIra("var",225),getElemIra("lista",31)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("OP_NOT",373),getElemIra("IRE_PAR",374),getElemIra("expr",616),getElemIra("eand",376),getElemIra("erel",377),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("mq",617)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("eand",618),getElemIra("erel",334),getElemIra("arit",335),getElemIra("term",336),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("erel",619),getElemIra("arit",335),getElemIra("term",336),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("arit",620),getElemIra("term",336),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("term",621),getElemIra("fact",337),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("fact",622),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("fact",623),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("ID",326),getElemIra("NUM",327),getElemIra("BOOL",328),getElemIra("OP_ADIT",329),getElemIra("IRE_PAR",331),getElemIra("fact",624),getElemIra("rando",338),getElemIra("var",339),getElemIra("lista",340)},
			{getElemIra("heldu-arte",625)},
			{getElemIra("ambitartean",626)},
			{getElemIra("m4",627)},
			{getElemIra("m3",628)},
			{getElemIra("m2",629)},
			{getElemIra("m1",630)},
			{getElemIra("m1",631)},
			{getElemIra("m1",632)},
			{getElemIra("m1",633)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",634),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("eand",635),getElemIra("erel",377),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("erel",636),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("arit",637),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("term",638),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("fact",639),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("fact",640),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("IRE_PAR",374),getElemIra("fact",641),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent_zer",642),getElemIra("sent",29),getElemIra("var",30),getElemIra("lista",31)},
			{getElemIra("ID",643)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",644),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{getElemIra("amaukera",645)},
			{getElemIra("hariketa",646)},
			{getElemIra("orduan",647)},
			{getElemIra("m5",648)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",649),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{},
			{getElemIra("ASIGN",650)},
			{getElemIra("OP_OR",271),getElemIra("exprp",651)},
			{getElemIra("OP_AND",273),getElemIra("eandp",652)},
			{getElemIra("OP_REL",275),getElemIra("erelp",653)},
			{getElemIra("OP_ADIT",277),getElemIra("aritp",654)},
			{getElemIra("OP_MULT",279),getElemIra("OP_DIV",280),getElemIra("OP_MOD",281),getElemIra("termp",655)},
			{getElemIra("OP_MULT",279),getElemIra("OP_DIV",280),getElemIra("OP_MOD",281),getElemIra("termp",656)},
			{getElemIra("OP_MULT",279),getElemIra("OP_DIV",280),getElemIra("OP_MOD",281),getElemIra("termp",657)},
			{getElemIra("OP_ADIT",287),getElemIra("aritp",658)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",659)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",660)},
			{getElemIra("OP_MULT",289),getElemIra("OP_DIV",290),getElemIra("OP_MOD",291),getElemIra("termp",661)},
			{getElemIra("m0",662)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",663)},
			{getElemIra("mq",664)},
			{},
			{getElemIra("mq",665)},
			{},
			{getElemIra("m0",666)},
			{},
			{getElemIra("m0",667)},
			{},
			{getElemIra("m0",668)},
			{getElemIra("m0",669)},
			{getElemIra("m0",670)},
			{},
			{},
			{},
			{getElemIra("m6",671)},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent_zer",672),getElemIra("sent",485),getElemIra("var",486),getElemIra("lista",31)},
			{getElemIra("m4",673)},
			{getElemIra("m3",674)},
			{getElemIra("m2",675)},
			{getElemIra("m1",676)},
			{getElemIra("m1",677)},
			{getElemIra("m1",678)},
			{getElemIra("m1",679)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",680),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{getElemIra("OP_OR",347),getElemIra("exprp",681)},
			{getElemIra("OP_AND",349),getElemIra("eandp",682)},
			{getElemIra("OP_REL",351),getElemIra("erelp",683)},
			{getElemIra("OP_ADIT",353),getElemIra("aritp",684)},
			{getElemIra("OP_MULT",355),getElemIra("OP_DIV",356),getElemIra("OP_MOD",357),getElemIra("termp",685)},
			{getElemIra("OP_MULT",355),getElemIra("OP_DIV",356),getElemIra("OP_MOD",357),getElemIra("termp",686)},
			{getElemIra("OP_MULT",355),getElemIra("OP_DIV",356),getElemIra("OP_MOD",357),getElemIra("termp",687)},
			{getElemIra("m7",688)},
			{getElemIra("m4",689)},
			{getElemIra("m3",690)},
			{getElemIra("m2",691)},
			{getElemIra("m1",692)},
			{getElemIra("m1",693)},
			{getElemIra("m1",694)},
			{getElemIra("m1",695)},
			{getElemIra("amaia",696)},
			{getElemIra("ITX_PAR",697)},
			{getElemIra("ITX_PAR",698)},
			{},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("OP_NOT",703),getElemIra("IRE_PAR",704),getElemIra("expr",705),getElemIra("eand",706),getElemIra("erel",707),getElemIra("arit",708),getElemIra("term",709),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("mq",714)},
			{getElemIra("ASIGN",715)},
			{getElemIra("egin",716)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("OP_NOT",703),getElemIra("IRE_PAR",704),getElemIra("expr",717),getElemIra("eand",706),getElemIra("erel",707),getElemIra("arit",708),getElemIra("term",709),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
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
			{},
			{getElemIra("ITX_PAR",718)},
			{},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("eand",719),getElemIra("erel",518),getElemIra("arit",519),getElemIra("term",520),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("erel",720),getElemIra("arit",519),getElemIra("term",520),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("arit",721),getElemIra("term",520),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("term",722),getElemIra("fact",521),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("fact",723),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("fact",724),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("ID",510),getElemIra("NUM",511),getElemIra("BOOL",512),getElemIra("OP_ADIT",513),getElemIra("IRE_PAR",515),getElemIra("fact",725),getElemIra("rando",522),getElemIra("var",523),getElemIra("lista",524)},
			{getElemIra("heldu-arte",726)},
			{getElemIra("ambitartean",727)},
			{getElemIra("OP_OR",426),getElemIra("exprp",728)},
			{getElemIra("OP_AND",428),getElemIra("eandp",729)},
			{getElemIra("OP_REL",430),getElemIra("erelp",730)},
			{getElemIra("OP_ADIT",432),getElemIra("aritp",731)},
			{getElemIra("OP_MULT",434),getElemIra("OP_DIV",435),getElemIra("OP_MOD",436),getElemIra("termp",732)},
			{getElemIra("OP_MULT",434),getElemIra("OP_DIV",435),getElemIra("OP_MOD",436),getElemIra("termp",733)},
			{getElemIra("OP_MULT",434),getElemIra("OP_DIV",435),getElemIra("OP_MOD",436),getElemIra("termp",734)},
			{getElemIra("m7",735)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("egin",736)},
			{getElemIra("OP_OR",461),getElemIra("exprp",737)},
			{getElemIra("OP_AND",463),getElemIra("eandp",738)},
			{getElemIra("OP_REL",465),getElemIra("erelp",739)},
			{getElemIra("OP_ADIT",467),getElemIra("aritp",740)},
			{getElemIra("OP_MULT",469),getElemIra("OP_DIV",470),getElemIra("OP_MOD",471),getElemIra("termp",741)},
			{getElemIra("OP_MULT",469),getElemIra("OP_DIV",470),getElemIra("OP_MOD",471),getElemIra("termp",742)},
			{getElemIra("OP_MULT",469),getElemIra("OP_DIV",470),getElemIra("OP_MOD",471),getElemIra("termp",743)},
			{},
			{},
			{},
			{getElemIra("m0",38)},
			{},
			{},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("fact",744),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("OP_NOT",703),getElemIra("IRE_PAR",704),getElemIra("expr",745),getElemIra("eand",706),getElemIra("erel",707),getElemIra("arit",708),getElemIra("term",709),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",98),getElemIra("NUM",99),getElemIra("BOOL",100),getElemIra("OP_ADIT",101),getElemIra("OP_NOT",102),getElemIra("IRE_PAR",103),getElemIra("expr",746),getElemIra("eand",105),getElemIra("erel",106),getElemIra("arit",107),getElemIra("term",108),getElemIra("fact",109),getElemIra("rando",110),getElemIra("var",111),getElemIra("lista",112)},
			{},
			{getElemIra("OP_OR",747),getElemIra("exprp",748)},
			{getElemIra("OP_AND",749),getElemIra("eandp",750)},
			{getElemIra("OP_REL",751),getElemIra("erelp",752)},
			{getElemIra("OP_ADIT",753),getElemIra("aritp",754)},
			{getElemIra("OP_MULT",755),getElemIra("OP_DIV",756),getElemIra("OP_MOD",757),getElemIra("termp",758)},
			{},
			{},
			{getElemIra("COMA",91),getElemIra("ITX_MAKO",759)},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent",760),getElemIra("var",486),getElemIra("lista",31)},
			{getElemIra("ID",369),getElemIra("NUM",370),getElemIra("BOOL",371),getElemIra("OP_ADIT",372),getElemIra("OP_NOT",373),getElemIra("IRE_PAR",374),getElemIra("expr",761),getElemIra("eand",376),getElemIra("erel",377),getElemIra("arit",378),getElemIra("term",379),getElemIra("fact",380),getElemIra("rando",381),getElemIra("var",382),getElemIra("lista",383)},
			{getElemIra("mq",762)},
			{},
			{getElemIra("ID",7),getElemIra("dekl_hond",763),getElemIra("id_zer",297)},
			{getElemIra("m4",764)},
			{getElemIra("m3",765)},
			{getElemIra("m2",766)},
			{getElemIra("m1",767)},
			{getElemIra("m1",768)},
			{getElemIra("m1",769)},
			{getElemIra("m1",770)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",771),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("egin",772)},
			{getElemIra("ID",17),getElemIra("hasiera",18),getElemIra("irakur",19),getElemIra("idatz",20),getElemIra("aukera",21),getElemIra("errepika",22),getElemIra("baldin",23),getElemIra("hemendik-hasita",24),getElemIra("bitartean",25),getElemIra("sekuentzia-hasiera",26),getElemIra("sentk",27),getElemIra("sent",773),getElemIra("var",30),getElemIra("lista",31)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("ITX_PAR",774)},
			{getElemIra("mq",775)},
			{},
			{getElemIra("mq",776)},
			{},
			{getElemIra("m0",777)},
			{},
			{getElemIra("m0",778)},
			{},
			{getElemIra("m0",779)},
			{getElemIra("m0",780)},
			{getElemIra("m0",781)},
			{},
			{},
			{},
			{getElemIra("m6",782)},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent_zer",783),getElemIra("sent",485),getElemIra("var",486),getElemIra("lista",31)},
			{},
			{getElemIra("OP_OR",602),getElemIra("exprp",784)},
			{getElemIra("OP_AND",604),getElemIra("eandp",785)},
			{getElemIra("OP_REL",606),getElemIra("erelp",786)},
			{getElemIra("OP_ADIT",608),getElemIra("aritp",787)},
			{getElemIra("OP_MULT",610),getElemIra("OP_DIV",611),getElemIra("OP_MOD",612),getElemIra("termp",788)},
			{getElemIra("OP_MULT",610),getElemIra("OP_DIV",611),getElemIra("OP_MOD",612),getElemIra("termp",789)},
			{getElemIra("OP_MULT",610),getElemIra("OP_DIV",611),getElemIra("OP_MOD",612),getElemIra("termp",790)},
			{getElemIra("m7",791)},
			{getElemIra("ID",17),getElemIra("hasiera",58),getElemIra("irakur",59),getElemIra("idatz",60),getElemIra("aukera",61),getElemIra("errepika",62),getElemIra("baldin",63),getElemIra("hemendik-hasita",64),getElemIra("bitartean",65),getElemIra("sekuentzia-hasiera",66),getElemIra("sentk",67),getElemIra("sent",792),getElemIra("var",70),getElemIra("lista",31)},
			{},
			{},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("eand",793),getElemIra("erel",707),getElemIra("arit",708),getElemIra("term",709),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("erel",794),getElemIra("arit",708),getElemIra("term",709),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("arit",795),getElemIra("term",709),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("term",796),getElemIra("fact",710),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("fact",797),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("fact",798),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("ID",699),getElemIra("NUM",700),getElemIra("BOOL",701),getElemIra("OP_ADIT",702),getElemIra("IRE_PAR",704),getElemIra("fact",799),getElemIra("rando",711),getElemIra("var",712),getElemIra("lista",713)},
			{getElemIra("heldu-arte",800)},
			{getElemIra("ambitartean",801)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("egin",802)},
			{},
			{getElemIra("m4",803)},
			{getElemIra("m3",804)},
			{getElemIra("m2",805)},
			{getElemIra("m1",806)},
			{getElemIra("m1",807)},
			{getElemIra("m1",808)},
			{getElemIra("m1",809)},
			{getElemIra("ID",160),getElemIra("NUM",161),getElemIra("BOOL",162),getElemIra("OP_ADIT",163),getElemIra("OP_NOT",164),getElemIra("IRE_PAR",165),getElemIra("expr",810),getElemIra("eand",167),getElemIra("erel",168),getElemIra("arit",169),getElemIra("term",170),getElemIra("fact",171),getElemIra("rando",172),getElemIra("var",173),getElemIra("lista",174)},
			{},
			{getElemIra("ID",17),getElemIra("hasiera",214),getElemIra("irakur",215),getElemIra("idatz",216),getElemIra("aukera",217),getElemIra("errepika",218),getElemIra("baldin",219),getElemIra("hemendik-hasita",220),getElemIra("bitartean",221),getElemIra("sekuentzia-hasiera",222),getElemIra("sentk",223),getElemIra("sent",811),getElemIra("var",225),getElemIra("lista",31)},
			{getElemIra("OP_OR",747),getElemIra("exprp",812)},
			{getElemIra("OP_AND",749),getElemIra("eandp",813)},
			{getElemIra("OP_REL",751),getElemIra("erelp",814)},
			{getElemIra("OP_ADIT",753),getElemIra("aritp",815)},
			{getElemIra("OP_MULT",755),getElemIra("OP_DIV",756),getElemIra("OP_MOD",757),getElemIra("termp",816)},
			{getElemIra("OP_MULT",755),getElemIra("OP_DIV",756),getElemIra("OP_MOD",757),getElemIra("termp",817)},
			{getElemIra("OP_MULT",755),getElemIra("OP_DIV",756),getElemIra("OP_MOD",757),getElemIra("termp",818)},
			{getElemIra("m7",819)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("egin",820)},
			{getElemIra("ID",17),getElemIra("hasiera",474),getElemIra("irakur",475),getElemIra("idatz",476),getElemIra("aukera",477),getElemIra("errepika",478),getElemIra("baldin",479),getElemIra("hemendik-hasita",480),getElemIra("bitartean",481),getElemIra("sekuentzia-hasiera",482),getElemIra("sentk",483),getElemIra("sent",821),getElemIra("var",486),getElemIra("lista",31)},
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
		TEltoAccion[][] bindVars = new TEltoAccion[822][];
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
