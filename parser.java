package parser_jkl;
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
		regla = new TRegla("prog","programa ID P_COMA decl sentc PUNTO",1);
		P.addElement(regla);
		regla = new TRegla("decl","",2);
		P.addElement(regla);
		regla = new TRegla("decl","aldagaia ID m0 COR_ABR NUM m0 COR_CER P_COMA decl",3);
		P.addElement(regla);
		regla = new TRegla("sentc","hasi mq lsent nahiko",4);
		P.addElement(regla);
		regla = new TRegla("lsent","sent lsent",5);
		P.addElement(regla);
		regla = new TRegla("lsent","",6);
		P.addElement(regla);
		regla = new TRegla("sent","P_COMA",7);
		P.addElement(regla);
		regla = new TRegla("sent","var m5 ASIGN expr P_COMA",8);
		P.addElement(regla);
		regla = new TRegla("sent","sentc",9);
		P.addElement(regla);
		regla = new TRegla("sent","irakurri ID P_COMA",10);
		P.addElement(regla);
		regla = new TRegla("sent","erakutzi expr P_COMA",11);
		P.addElement(regla);
		regla = new TRegla("sent","idatzi CAD m0 P_COMA",12);
		P.addElement(regla);
		regla = new TRegla("sent","lerro-berria P_COMA",13);
		P.addElement(regla);
		regla = new TRegla("sent","gertatu-ezkero expr egin mq sent",14);
		P.addElement(regla);
		regla = new TRegla("sent","gertatzen-den-bitartian mq expr egin mq sent",15);
		P.addElement(regla);
		regla = new TRegla("sent","egin sent gertatzen-den-bitartian expr P_COMA",16);
		P.addElement(regla);
		regla = new TRegla("sent","hemendik-hasita ID m5 ASIGN expr m6 heldu-arte expr m7 egin sent",17);
		P.addElement(regla);
		regla = new TRegla("expr","eand exprp",18);
		P.addElement(regla);
		regla = new TRegla("exprp","",19);
		P.addElement(regla);
		regla = new TRegla("exprp","OP_OR mq eand m4 exprp",20);
		P.addElement(regla);
		regla = new TRegla("eand","erel eandp",21);
		P.addElement(regla);
		regla = new TRegla("eandp","",22);
		P.addElement(regla);
		regla = new TRegla("eandp","OP_AND mq erel m3 eandp",23);
		P.addElement(regla);
		regla = new TRegla("erel","arit erelp",24);
		P.addElement(regla);
		regla = new TRegla("erelp","",25);
		P.addElement(regla);
		regla = new TRegla("erelp","OP_REL m0 arit m2 erelp",26);
		P.addElement(regla);
		regla = new TRegla("arit","term aritp",27);
		P.addElement(regla);
		regla = new TRegla("aritp","",28);
		P.addElement(regla);
		regla = new TRegla("aritp","OP_ADIT m0 term m1 aritp",29);
		P.addElement(regla);
		regla = new TRegla("term","fact termp",30);
		P.addElement(regla);
		regla = new TRegla("termp","",31);
		P.addElement(regla);
		regla = new TRegla("termp","OP_MULT m0 fact m1 termp",32);
		P.addElement(regla);
		regla = new TRegla("expr","OP_NOT expr",33);
		P.addElement(regla);
		regla = new TRegla("fact","OP_ADIT fact",34);
		P.addElement(regla);
		regla = new TRegla("fact","rando",35);
		P.addElement(regla);
		regla = new TRegla("rando","NUM",36);
		P.addElement(regla);
		regla = new TRegla("rando","var",37);
		P.addElement(regla);
		regla = new TRegla("rando","PAR_ABR expr PAR_CER",38);
		P.addElement(regla);
		regla = new TRegla("m0","",39);
		P.addElement(regla);
		regla = new TRegla("m1","",40);
		P.addElement(regla);
		regla = new TRegla("m2","",41);
		P.addElement(regla);
		regla = new TRegla("m3","",42);
		P.addElement(regla);
		regla = new TRegla("mq","",43);
		P.addElement(regla);
		regla = new TRegla("m4","",44);
		P.addElement(regla);
		regla = new TRegla("rando","BOOL",45);
		P.addElement(regla);
		regla = new TRegla("m5","",46);
		P.addElement(regla);
		regla = new TRegla("var","ID",47);
		P.addElement(regla);
		regla = new TRegla("var","lista COR_CER",48);
		P.addElement(regla);
		regla = new TRegla("lista","lista COMA arit",49);
		P.addElement(regla);
		regla = new TRegla("lista","ID m0 COR_ABR arit",50);
		P.addElement(regla);
		regla = new TRegla("termp","OP_C m0 fact m1 termp",51);
		P.addElement(regla);
		regla = new TRegla("termp","OP_EXP m0 fact m1 termp",52);
		P.addElement(regla);
		regla = new TRegla("m6","",53);
		P.addElement(regla);
		regla = new TRegla("m7","",54);
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
		TElemIra[][] bindVars = new TElemIra[][]{{getElemIra("programa",1),getElemIra("prog",2)},
			{getElemIra("ID",3)},
			{getElemIra("$",4)},
			{getElemIra("P_COMA",5)},
			{},
			{getElemIra("aldagaia",6),getElemIra("decl",7)},
			{getElemIra("ID",8)},
			{getElemIra("hasi",9),getElemIra("sentc",10)},
			{getElemIra("m0",11)},
			{getElemIra("mq",12)},
			{getElemIra("PUNTO",13)},
			{getElemIra("COR_ABR",14)},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("lsent",27),getElemIra("sent",28),getElemIra("var",29),getElemIra("lista",30)},
			{},
			{getElemIra("NUM",31)},
			{getElemIra("m0",32)},
			{},
			{getElemIra("mq",33)},
			{getElemIra("ID",34)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("OP_NOT",37),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("expr",41),getElemIra("eand",42),getElemIra("erel",43),getElemIra("arit",44),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("CAD",50)},
			{getElemIra("P_COMA",51)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("OP_NOT",54),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("expr",58),getElemIra("eand",59),getElemIra("erel",60),getElemIra("arit",61),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("sent",67),getElemIra("var",29),getElemIra("lista",30)},
			{getElemIra("mq",68)},
			{getElemIra("ID",69)},
			{},
			{getElemIra("nahiko",70)},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("lsent",71),getElemIra("sent",28),getElemIra("var",29),getElemIra("lista",30)},
			{getElemIra("m5",72)},
			{getElemIra("COMA",73),getElemIra("COR_CER",74)},
			{getElemIra("m0",75)},
			{getElemIra("COR_ABR",76)},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("lsent",77),getElemIra("sent",28),getElemIra("var",29),getElemIra("lista",30)},
			{getElemIra("P_COMA",78)},
			{getElemIra("m0",32)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("fact",79),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("OP_NOT",37),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("expr",80),getElemIra("eand",42),getElemIra("erel",43),getElemIra("arit",44),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("OP_NOT",83),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("expr",87),getElemIra("eand",88),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{},
			{getElemIra("P_COMA",96)},
			{getElemIra("OP_OR",97),getElemIra("exprp",98)},
			{getElemIra("OP_AND",99),getElemIra("eandp",100)},
			{getElemIra("OP_REL",101),getElemIra("erelp",102)},
			{getElemIra("OP_ADIT",103),getElemIra("aritp",104)},
			{getElemIra("OP_MULT",105),getElemIra("OP_C",106),getElemIra("OP_EXP",107),getElemIra("termp",108)},
			{},
			{},
			{getElemIra("COMA",73),getElemIra("COR_CER",109)},
			{getElemIra("m0",110)},
			{},
			{getElemIra("m0",32)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("fact",111),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("OP_NOT",54),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("expr",112),getElemIra("eand",59),getElemIra("erel",60),getElemIra("arit",61),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("OP_NOT",83),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("expr",113),getElemIra("eand",88),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{},
			{getElemIra("egin",114)},
			{getElemIra("OP_OR",115),getElemIra("exprp",116)},
			{getElemIra("OP_AND",117),getElemIra("eandp",118)},
			{getElemIra("OP_REL",119),getElemIra("erelp",120)},
			{getElemIra("OP_ADIT",121),getElemIra("aritp",122)},
			{getElemIra("OP_MULT",123),getElemIra("OP_C",124),getElemIra("OP_EXP",125),getElemIra("termp",126)},
			{},
			{},
			{getElemIra("COMA",73),getElemIra("COR_CER",127)},
			{getElemIra("gertatzen-den-bitartian",128)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("OP_NOT",54),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("expr",129),getElemIra("eand",59),getElemIra("erel",60),getElemIra("arit",61),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("m5",130)},
			{},
			{},
			{getElemIra("ASIGN",131)},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("arit",137),getElemIra("term",138),getElemIra("fact",139),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{},
			{getElemIra("COR_CER",143)},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("arit",144),getElemIra("term",138),getElemIra("fact",139),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{getElemIra("nahiko",145)},
			{},
			{},
			{},
			{getElemIra("m0",32)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("fact",146),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("OP_NOT",83),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("expr",147),getElemIra("eand",88),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("OP_NOT",83),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("expr",148),getElemIra("eand",88),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{},
			{getElemIra("PAR_CER",149)},
			{getElemIra("OP_OR",150),getElemIra("exprp",151)},
			{getElemIra("OP_AND",152),getElemIra("eandp",153)},
			{getElemIra("OP_REL",154),getElemIra("erelp",155)},
			{getElemIra("OP_ADIT",156),getElemIra("aritp",157)},
			{getElemIra("OP_MULT",158),getElemIra("OP_C",159),getElemIra("OP_EXP",160),getElemIra("termp",161)},
			{},
			{},
			{getElemIra("COMA",73),getElemIra("COR_CER",162)},
			{},
			{getElemIra("mq",163)},
			{},
			{getElemIra("mq",164)},
			{},
			{getElemIra("m0",165)},
			{},
			{getElemIra("m0",166)},
			{},
			{getElemIra("m0",167)},
			{getElemIra("m0",168)},
			{getElemIra("m0",169)},
			{},
			{},
			{getElemIra("P_COMA",170)},
			{},
			{},
			{getElemIra("PAR_CER",171)},
			{getElemIra("mq",172)},
			{getElemIra("mq",173)},
			{},
			{getElemIra("mq",174)},
			{},
			{getElemIra("m0",175)},
			{},
			{getElemIra("m0",176)},
			{},
			{getElemIra("m0",177)},
			{getElemIra("m0",178)},
			{getElemIra("m0",179)},
			{},
			{},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("OP_NOT",37),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("expr",180),getElemIra("eand",42),getElemIra("erel",43),getElemIra("arit",44),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("egin",181)},
			{getElemIra("ASIGN",182)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("OP_NOT",37),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("expr",183),getElemIra("eand",42),getElemIra("erel",43),getElemIra("arit",44),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("m0",32)},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("fact",184),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("OP_NOT",83),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("expr",185),getElemIra("eand",88),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{},
			{},
			{getElemIra("OP_ADIT",186),getElemIra("aritp",187)},
			{getElemIra("OP_MULT",188),getElemIra("OP_C",189),getElemIra("OP_EXP",190),getElemIra("termp",191)},
			{},
			{},
			{getElemIra("COMA",73),getElemIra("COR_CER",192)},
			{getElemIra("P_COMA",193)},
			{},
			{},
			{},
			{},
			{getElemIra("PAR_CER",194)},
			{},
			{getElemIra("mq",195)},
			{},
			{getElemIra("mq",196)},
			{},
			{getElemIra("m0",197)},
			{},
			{getElemIra("m0",198)},
			{},
			{getElemIra("m0",199)},
			{getElemIra("m0",200)},
			{getElemIra("m0",201)},
			{},
			{},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("eand",202),getElemIra("erel",43),getElemIra("arit",44),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("erel",203),getElemIra("arit",44),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("arit",204),getElemIra("term",45),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("term",205),getElemIra("fact",46),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("fact",206),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("fact",207),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{getElemIra("ID",35),getElemIra("OP_ADIT",36),getElemIra("NUM",38),getElemIra("PAR_ABR",39),getElemIra("BOOL",40),getElemIra("fact",208),getElemIra("rando",47),getElemIra("var",48),getElemIra("lista",49)},
			{},
			{},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("sent",209),getElemIra("var",29),getElemIra("lista",30)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("eand",210),getElemIra("erel",60),getElemIra("arit",61),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("erel",211),getElemIra("arit",61),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("arit",212),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("term",213),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("fact",214),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("fact",215),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("fact",216),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("P_COMA",217)},
			{getElemIra("mq",218)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("OP_NOT",221),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("expr",225),getElemIra("eand",226),getElemIra("erel",227),getElemIra("arit",228),getElemIra("term",229),getElemIra("fact",230),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("P_COMA",234)},
			{},
			{getElemIra("PAR_CER",235)},
			{getElemIra("m0",236)},
			{},
			{getElemIra("m0",237)},
			{getElemIra("m0",238)},
			{getElemIra("m0",239)},
			{},
			{},
			{getElemIra("aldagaia",6),getElemIra("decl",240)},
			{},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("eand",241),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("erel",242),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("arit",243),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("term",244),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("fact",245),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("fact",246),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("fact",247),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{getElemIra("m4",248)},
			{getElemIra("m3",249)},
			{getElemIra("m2",250)},
			{getElemIra("m1",251)},
			{getElemIra("m1",252)},
			{getElemIra("m1",253)},
			{getElemIra("m1",254)},
			{},
			{getElemIra("m4",255)},
			{getElemIra("m3",256)},
			{getElemIra("m2",257)},
			{getElemIra("m1",258)},
			{getElemIra("m1",259)},
			{getElemIra("m1",260)},
			{getElemIra("m1",261)},
			{},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("sent",262),getElemIra("var",29),getElemIra("lista",30)},
			{getElemIra("m0",32)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("fact",263),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("OP_NOT",221),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("expr",264),getElemIra("eand",226),getElemIra("erel",227),getElemIra("arit",228),getElemIra("term",229),getElemIra("fact",230),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{},
			{getElemIra("ID",81),getElemIra("OP_ADIT",82),getElemIra("OP_NOT",83),getElemIra("NUM",84),getElemIra("PAR_ABR",85),getElemIra("BOOL",86),getElemIra("expr",265),getElemIra("eand",88),getElemIra("erel",89),getElemIra("arit",90),getElemIra("term",91),getElemIra("fact",92),getElemIra("rando",93),getElemIra("var",94),getElemIra("lista",95)},
			{},
			{getElemIra("m6",266)},
			{getElemIra("OP_OR",267),getElemIra("exprp",268)},
			{getElemIra("OP_AND",269),getElemIra("eandp",270)},
			{getElemIra("OP_REL",271),getElemIra("erelp",272)},
			{getElemIra("OP_ADIT",273),getElemIra("aritp",274)},
			{getElemIra("OP_MULT",275),getElemIra("OP_C",276),getElemIra("OP_EXP",277),getElemIra("termp",278)},
			{},
			{},
			{getElemIra("COMA",73),getElemIra("COR_CER",279)},
			{},
			{},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("term",280),getElemIra("fact",139),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("fact",281),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("fact",282),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{getElemIra("ID",132),getElemIra("OP_ADIT",133),getElemIra("NUM",134),getElemIra("PAR_ABR",135),getElemIra("BOOL",136),getElemIra("fact",283),getElemIra("rando",140),getElemIra("var",141),getElemIra("lista",142)},
			{},
			{getElemIra("m4",284)},
			{getElemIra("m3",285)},
			{getElemIra("m2",286)},
			{getElemIra("m1",287)},
			{getElemIra("m1",288)},
			{getElemIra("m1",289)},
			{getElemIra("m1",290)},
			{getElemIra("OP_OR",97),getElemIra("exprp",291)},
			{getElemIra("OP_AND",99),getElemIra("eandp",292)},
			{getElemIra("OP_REL",101),getElemIra("erelp",293)},
			{getElemIra("OP_ADIT",103),getElemIra("aritp",294)},
			{getElemIra("OP_MULT",105),getElemIra("OP_C",106),getElemIra("OP_EXP",107),getElemIra("termp",295)},
			{getElemIra("OP_MULT",105),getElemIra("OP_C",106),getElemIra("OP_EXP",107),getElemIra("termp",296)},
			{getElemIra("OP_MULT",105),getElemIra("OP_C",106),getElemIra("OP_EXP",107),getElemIra("termp",297)},
			{getElemIra("OP_OR",115),getElemIra("exprp",298)},
			{getElemIra("OP_AND",117),getElemIra("eandp",299)},
			{getElemIra("OP_REL",119),getElemIra("erelp",300)},
			{getElemIra("OP_ADIT",121),getElemIra("aritp",301)},
			{getElemIra("OP_MULT",123),getElemIra("OP_C",124),getElemIra("OP_EXP",125),getElemIra("termp",302)},
			{getElemIra("OP_MULT",123),getElemIra("OP_C",124),getElemIra("OP_EXP",125),getElemIra("termp",303)},
			{getElemIra("OP_MULT",123),getElemIra("OP_C",124),getElemIra("OP_EXP",125),getElemIra("termp",304)},
			{},
			{},
			{},
			{getElemIra("PAR_CER",305)},
			{getElemIra("heldu-arte",306)},
			{getElemIra("mq",307)},
			{},
			{getElemIra("mq",308)},
			{},
			{getElemIra("m0",309)},
			{},
			{getElemIra("m0",310)},
			{},
			{getElemIra("m0",311)},
			{getElemIra("m0",312)},
			{getElemIra("m0",313)},
			{},
			{},
			{getElemIra("m1",314)},
			{getElemIra("m1",315)},
			{getElemIra("m1",316)},
			{getElemIra("m1",317)},
			{getElemIra("OP_OR",150),getElemIra("exprp",318)},
			{getElemIra("OP_AND",152),getElemIra("eandp",319)},
			{getElemIra("OP_REL",154),getElemIra("erelp",320)},
			{getElemIra("OP_ADIT",156),getElemIra("aritp",321)},
			{getElemIra("OP_MULT",158),getElemIra("OP_C",159),getElemIra("OP_EXP",160),getElemIra("termp",322)},
			{getElemIra("OP_MULT",158),getElemIra("OP_C",159),getElemIra("OP_EXP",160),getElemIra("termp",323)},
			{getElemIra("OP_MULT",158),getElemIra("OP_C",159),getElemIra("OP_EXP",160),getElemIra("termp",324)},
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
			{},
			{},
			{},
			{},
			{getElemIra("ID",52),getElemIra("OP_ADIT",53),getElemIra("OP_NOT",54),getElemIra("NUM",55),getElemIra("PAR_ABR",56),getElemIra("BOOL",57),getElemIra("expr",325),getElemIra("eand",59),getElemIra("erel",60),getElemIra("arit",61),getElemIra("term",62),getElemIra("fact",63),getElemIra("rando",64),getElemIra("var",65),getElemIra("lista",66)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("eand",326),getElemIra("erel",227),getElemIra("arit",228),getElemIra("term",229),getElemIra("fact",230),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("erel",327),getElemIra("arit",228),getElemIra("term",229),getElemIra("fact",230),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("arit",328),getElemIra("term",229),getElemIra("fact",230),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("term",329),getElemIra("fact",230),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("fact",330),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("fact",331),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("ID",219),getElemIra("OP_ADIT",220),getElemIra("NUM",222),getElemIra("PAR_ABR",223),getElemIra("BOOL",224),getElemIra("fact",332),getElemIra("rando",231),getElemIra("var",232),getElemIra("lista",233)},
			{getElemIra("OP_ADIT",186),getElemIra("aritp",333)},
			{getElemIra("OP_MULT",188),getElemIra("OP_C",189),getElemIra("OP_EXP",190),getElemIra("termp",334)},
			{getElemIra("OP_MULT",188),getElemIra("OP_C",189),getElemIra("OP_EXP",190),getElemIra("termp",335)},
			{getElemIra("OP_MULT",188),getElemIra("OP_C",189),getElemIra("OP_EXP",190),getElemIra("termp",336)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{getElemIra("m7",337)},
			{getElemIra("m4",338)},
			{getElemIra("m3",339)},
			{getElemIra("m2",340)},
			{getElemIra("m1",341)},
			{getElemIra("m1",342)},
			{getElemIra("m1",343)},
			{getElemIra("m1",344)},
			{},
			{},
			{},
			{},
			{getElemIra("egin",345)},
			{getElemIra("OP_OR",267),getElemIra("exprp",346)},
			{getElemIra("OP_AND",269),getElemIra("eandp",347)},
			{getElemIra("OP_REL",271),getElemIra("erelp",348)},
			{getElemIra("OP_ADIT",273),getElemIra("aritp",349)},
			{getElemIra("OP_MULT",275),getElemIra("OP_C",276),getElemIra("OP_EXP",277),getElemIra("termp",350)},
			{getElemIra("OP_MULT",275),getElemIra("OP_C",276),getElemIra("OP_EXP",277),getElemIra("termp",351)},
			{getElemIra("OP_MULT",275),getElemIra("OP_C",276),getElemIra("OP_EXP",277),getElemIra("termp",352)},
			{getElemIra("ID",15),getElemIra("P_COMA",16),getElemIra("hasi",17),getElemIra("irakurri",18),getElemIra("erakutzi",19),getElemIra("idatzi",20),getElemIra("lerro-berria",21),getElemIra("gertatu-ezkero",22),getElemIra("egin",23),getElemIra("gertatzen-den-bitartian",24),getElemIra("hemendik-hasita",25),getElemIra("sentc",26),getElemIra("sent",353),getElemIra("var",29),getElemIra("lista",30)},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
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
		TEltoAccion[][] bindVars = new TEltoAccion[354][];
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
