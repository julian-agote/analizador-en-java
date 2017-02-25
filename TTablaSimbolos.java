package parser_jkl;
import java.util.*;
public class TTablaSimbolos {
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
	String leer(int pos){
		if(pos<ts.size())
			return (String)ts.elementAt(pos);
		return null;	
	}
}
