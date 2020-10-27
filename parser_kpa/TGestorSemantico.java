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
    public static String getElemento(String s,int j){
        int nelem=1,sig_index=0;
        do{
            if(s.indexOf(',',sig_index)>0){
                if(nelem==j)
                    return s.substring(sig_index,s.indexOf(',',sig_index));
                else{
                    nelem++;
                    sig_index = s.indexOf(',',sig_index)+1;
                }
            }else return s.substring(sig_index);
        }while(sig_index<s.length());
        return s;
    }
    public static int cuentaElementos(String s){
        int nelem=0,sig_index=0;
        do{
            if(s.indexOf(',',sig_index)>0){
                nelem++;
                sig_index = s.indexOf(',',sig_index)+1;
            }
        }while(s.indexOf(',',sig_index)>0);
        nelem++;
        return nelem; 
    }
}   
class Var {
    private String nombre;
    private Float valf;
    private Integer vali;
    private char valc;
    private String tipo;
    private boolean asignacion;
    private boolean temporal;
    private boolean parametro;
    private boolean dev;
    private boolean local;
    ArrayList<Integer> registros;
    int[] anArray;
    private Hashtable<String, Integer> sig_uso;
    private Hashtable<String, Integer> nodo;
    Var(String _nombre){
        this.nombre = _nombre;
        this.tipo = "osoa";
        this.sig_uso = new Hashtable<String, Integer>();
        this.nodo = new Hashtable<String, Integer>();
        this.registros = new ArrayList<Integer>();
        this.temporal = false;
        this.local = false;
        this.parametro = false;
        this.dev = false;
    }   
    Var(String _nombre, String _tipo){
        this.nombre = _nombre;
        this.tipo = _tipo;
        this.sig_uso = new Hashtable<String, Integer>();
        this.nodo = new Hashtable<String, Integer>();
        this.registros = new ArrayList<Integer>();
        this.temporal = false;
        this.local = true;
        this.parametro = false;
        this.dev = false;
    }   
    Var(String _nombre,float pval){
        this.nombre = _nombre;
        this.tipo = "erreala";
        this.valf = new Float(pval);
        this.sig_uso = new Hashtable<String, Integer>();
        this.nodo = new Hashtable<String, Integer>();
        this.registros = new ArrayList<Integer>();
        this.temporal = false;
        this.local = false;
        this.parametro = false;
        this.dev = false;
    }   
    Var(String _nombre,int pval){
        this.nombre = _nombre;
        this.tipo = "osoa";
        this.vali = new Integer(pval);
        this.sig_uso = new Hashtable<String, Integer>();
        this.nodo = new Hashtable<String, Integer>();
        this.registros = new ArrayList<Integer>();
        this.temporal = false;
        this.local = false;
        this.parametro = false;
        this.dev = false;
    }   
    Var(String _nombre,char pval){
        this.nombre = _nombre;
        this.tipo = "karakterea";
        this.valc = pval;
        this.sig_uso = new Hashtable<String, Integer>();
        this.nodo = new Hashtable<String, Integer>();
        this.registros = new ArrayList<Integer>();
        this.temporal = false;
        this.local = false;
        this.parametro = false;
        this.dev = false;
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
    public void set_tipo(String ptipo){
        this.tipo = ptipo;
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
        this.sig_uso.remove("B"+Integer.toString(nbloque));
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
        if(this.nodo.containsKey("B"+Integer.toString(nbloque)))
            this.nodo.remove("B"+Integer.toString(nbloque));
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
    public void borrar_registros(){
        this.registros.clear();
    }
    public void anadir_registro(Integer pos){
        if(this.registros.isEmpty())
            this.registros.add(pos);
        else if(this.registros.indexOf(pos)==-1)
            this.registros.add(pos);
    }
    public void senialar_asig(){
        this.asignacion = true;
    }
    public void borrar_asig(){
        this.asignacion = false;
    }
    public boolean asig_marcada(){
        return this.asignacion;
    }
    public void senialar_temp(){
        this.temporal = true;
        this.local = false;
    }
    public void senialar_param(){
        this.parametro = true;
        this.local = false;
    }
    public void senialar_dev(){
        this.dev = true;
        this.local = true;
    }
    public boolean es_temporal(){
        return this.temporal;
    }
    public boolean es_local(){
        return this.local;
    }
    public boolean es_dev(){
        return this.dev;
    }
    public boolean es_parametro(){
        return this.parametro;
    }
    Iterator<Integer> devuelve_regs(){
        return this.registros.iterator();
    } 
}
class Tvariables { // tabla de simbolos para la ejecucion del programa
    String procedimiento;
    String comentario;
    String codigo;
    ArrayList<Bloque_basico> tbloques;
    ArrayList<String> bucles;
    private ArrayList<Var> ts;
    Tvariables(){
        procedimiento = new String();
        ts = new ArrayList<Var>();
        tbloques = new ArrayList<Bloque_basico>();
        bucles = new ArrayList<String>();
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
    int insertar_temp(String p_lex,String p_tipo) {
        int pos = -1;
        Var v;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(p_lex)){
                pos = i;
                break;
            }   
        if (pos == -1) {
            pos = ts.size();
            v = new Var(p_lex,p_tipo);
            v.senialar_temp();
            ts.add(v);
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
    int insertar_prin(String p_lex,String p_tipo) {
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(p_lex)){
                pos = i;
                break;
            }   
        if (pos == -1) {
            pos = ts.size();
            if(pos>0 && ts.get(pos-1).tipo().equals(p_tipo)){
                ts.add(pos-1,new Var(p_lex,p_tipo));
                pos--;
            }else    
                ts.add(new Var(p_lex,p_tipo));
        }
        return pos;
    }
    void marcar_como_parametros(){// las primeras vbles. que se inserten en la tabla de simbolos del proc. seran los parametros
        Var v;
        for(int i=0;i<ts.size();i++){
            v = ts.get(i);
            v.senialar_param();
            ts.set(i,v);
        }
    }
    Hashtable<String,String> marcar_como_dev(){// correspondera a la vble que devuelve como valor la funcion
        Var v; 
        Hashtable<String,String> vbles_retorno = new Hashtable<String,String>();
        for(int i=0;i<ts.size();i++){
            v = ts.get(i);
            if(!v.es_parametro()){
                v.senialar_dev();
                ts.set(i,v);
                vbles_retorno.put(v.nombre(),v.tipo());
            }
        }
        return(vbles_retorno);
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
    boolean es_local_temp(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            if(ts.get(pos).es_local()||ts.get(pos).es_temporal())
                //if(!Util.stringEmpty(ts.get(pos).tipo()) && !ts.get(pos).tipo().substring(0,4).equals("arra"))
                    return true;
        return false;  
    }
    boolean es_temporal(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            if(ts.get(pos).es_temporal())
                    return true;
        return false;  
    }
    int es_parametro(String nombre){ // si nombre es un parametro devuelve el num. de parametro
        int pos = -1,nparam=0;
        String tipo_array;
        for(int i=ts.size()-1;i>=0;i--){
            if(ts.get(i).es_parametro()){ 
                if(ts.get(i).tipo().substring(0,4).equals("arra")){
                    // si el tipo de un parametro previo es un array hay que sumar las posiciones que ocupa el array
                    tipo_array = ts.get(i).tipo();
                    nparam += Integer.parseInt(tipo_array.substring(tipo_array.indexOf(",")+1,tipo_array.indexOf(")")));
                }else 
                    nparam++;
            }    
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        }    
        if (pos != -1)
            if(ts.get(pos).es_parametro())
                return nparam;
        return -1;  
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
        return "";  
    }
    void set_tipo(String nombre,String _tipo){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).set_tipo(_tipo);
    }        
    ArrayList<String> leer_tipo_dev(){
        int pos = -1;
        ArrayList<String> lista_tipos = new ArrayList<String>();
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).es_dev()) {
                lista_tipos.add(ts.get(i).tipo());
            }   
        return lista_tipos;  
    }
    int num_val_dev(){
        int num = 0;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).es_dev()) {
                num++;
            }   
        return num;
    }
    int num_val_dev(String nombre){
        int num = 0;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).es_dev()) {
                num++;
                if(ts.get(i).nombre().equals(nombre)){
                    break;
                }   
            }   
        return (num-1);
    }
    Hashtable <String,String> get_dev(){
        Hashtable<String,String> vbles_retorno = new Hashtable<String,String>();
        for(int i=0;i<ts.size();i++){
            if(ts.get(i).es_dev()) {
                vbles_retorno.put(ts.get(i).nombre(),ts.get(i).tipo());
            }
        }
        return(vbles_retorno);
    }
    boolean leer_asig_marcada(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            if(ts.get(pos).es_local()||ts.get(pos).es_temporal())
                return ts.get(pos).asig_marcada();
        return false;  
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
    void borrar_asig(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).borrar_asig();
    }
    void activar(String nombre, int nbloque, int ind){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).set_activo(ind,nbloque);
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
            return (ts.get(pos).get_nodo(nbloque)>=0?false:true);
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
    int obtener_pos_en_curso(String nombre,String reg_pref){
        int pos = -1;
        int rbuscado = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            if(ts.get(pos).devolver_registro("")>0)
                if(Util.stringEmpty(reg_pref))
                    rbuscado = (ts.get(pos).devolver_registro(""));
                else{
                    rbuscado = ts.get(pos).devolver_registro(reg_pref);
                    if(rbuscado<=0)
                        rbuscado= ts.get(pos).devolver_registro("");
                }
        return rbuscado;    
    }  
    void marcar_asignacion(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).senialar_asig();
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
    void borrar_todos_regs(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        if (pos != -1)
            ts.get(pos).borrar_registros();
    }
    void actualiza_descrip_direcc(String nombre, String ldir, boolean asignacion){
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
                if(asignacion) ts.get(pos).senialar_asig();
            }   catch(NumberFormatException e) {
                return;
                }
        }
    }
    Iterator<Integer> devuelve_regs(String nombre){
        int pos = -1;
        for(int i=0;i<ts.size();i++)
            if(ts.get(i).nombre().equals(nombre)){
                pos = i;
                break;
            }   
        return ts.get(pos).devuelve_regs();
    }
    Iterator<Var> devuelve_vbles(){
        return ts.iterator();
    }  
    void set_proc(String _p){
        this.procedimiento = _p;
    } 
    void set_comentario(String _p){
        this.comentario = _p;
    } 
    String get_proc(){
        return this.procedimiento;
    } 
    String get_comentario(){
        return this.comentario;
    } 
    void set_codigo(String _p){
        this.codigo = _p;
    } 
    String get_codigo(){
        return this.codigo;
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
    public void set_quad(String _q) {
        this.quad  = _q;
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
    public void set_arg1(String _arg) {
        this.arg1 = _arg;
    }
    public void set_arg2(String _arg) {
        this.arg2 = _arg;
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
    NodoGDA(String _op, int _hi, int _hd){
        this.etiqueta = _op;
        this.identificadores = new ArrayList<String>();
        this.hijo_izdo = _hi;
        this.hijo_dcho = _hd;
    }
    public String etiqueta() {
        return this.etiqueta;
    }
    public int get_hi(){
        return this.hijo_izdo;
    }
    public int get_hd(){
        return this.hijo_dcho;
    }
    public boolean esta_id(String _id){
        return this.identificadores.contains(_id);
    }
    public void aniadir_id(String _id){
        this.identificadores.add(_id);
    }
    public void quitar_id(String _id){
        this.identificadores.remove(_id);
    }
    public String primer_id(){ // devuelve el primero de los identificadores asociados al nodo
        String id;
        if(this.identificadores.size()==0)
            id = this.etiqueta;
        else
            id = this.identificadores.get(0);
        return id;
    }
}
class Bloque_basico{
    int nbloque,lider;
    ArrayList<String> cuadruplos,succ,pred;
    ArrayList<NodoGDA> gda;
    ArrayList<Integer> listados;
    ArrayList<String> lvaract;
    Bloque_basico(int _b,int _lider){
        this.nbloque = _b;
        this.lider = _lider;
        this.cuadruplos = new ArrayList<String>();
        this.succ = new ArrayList<String>();
        this.pred = new ArrayList<String>();
        this.gda = new ArrayList<NodoGDA>();
        this.listados = new ArrayList<Integer>();
        this.lvaract = new ArrayList<String>();
    }
    Bloque_basico(){
        this.cuadruplos = new ArrayList<String>();
        this.succ = new ArrayList<String>();
        this.pred = new ArrayList<String>();
        this.gda = new ArrayList<NodoGDA>();
        this.listados = new ArrayList<Integer>();
        this.lvaract = new ArrayList<String>();
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
    boolean existe_hoja(String _etiq){
        NodoGDA vnodo;
        for(Iterator<NodoGDA> iter=this.gda.iterator();iter.hasNext();){ 
            vnodo = (NodoGDA)iter.next();
            if(vnodo.etiqueta().equalsIgnoreCase(_etiq))
              return true;
        }
        return false;  
    }
    int encontrar_hoja(String _etiq){
        NodoGDA vnodo;
        for(Iterator<NodoGDA> iter=this.gda.iterator();iter.hasNext();){ 
            vnodo = (NodoGDA)iter.next();
            if(vnodo.etiqueta().equalsIgnoreCase(_etiq))
                return this.gda.indexOf(vnodo);
        }  
        return -1;  
    }
    boolean esta_indeterminado(String _op,int hi,int hd){ // devuelve true si no existe el nodo en el gda
        NodoGDA vnodo;
        for(Iterator<NodoGDA> iter=this.gda.iterator();iter.hasNext();){ 
            vnodo = (NodoGDA)iter.next();
            if(vnodo.etiqueta().equalsIgnoreCase(_op) && hi == vnodo.get_hi() && hd == vnodo.get_hd())
              return false;
        }
        return true;  
    }
    int encontrar_nodo(String _op,int hi,int hd,String _id){
        NodoGDA vnodo;
        int indice = -1;
        for(Iterator<NodoGDA> iter=this.gda.iterator();iter.hasNext();){ 
            vnodo = (NodoGDA)iter.next();
            if(vnodo.etiqueta().equalsIgnoreCase(_op) && hi == vnodo.get_hi() && hd == vnodo.get_hd())
                indice = this.gda.indexOf(vnodo);
            else if(vnodo.esta_id(_id))
                    vnodo.quitar_id(_id);
        }    
        if(!Util.stringEmpty(_id)){
            vnodo = this.gda.get(indice); //aniadir id en la nueva posicion calculada de gda
            vnodo.aniadir_id(_id);
            this.gda.set(indice,vnodo);
        }
        return indice;
    }
    int aniadir_nodo(String _op,int hi,int hd,String _id){
        gda.add(new NodoGDA(_op,hi,hd));
        int indice = gda.size()-1;
        // buscar en otros nodos del gda en donde pudiera estar _id para quitarlo
        NodoGDA vnodo;
        for(Iterator<NodoGDA> iter=this.gda.iterator();iter.hasNext();){ 
            vnodo = (NodoGDA)iter.next();
            if(vnodo.esta_id(_id))
                vnodo.quitar_id(_id);
        }    
        if(!Util.stringEmpty(_id)){
            vnodo = this.gda.get(indice); //aniadir id en la nueva posicion calculada de gda
            vnodo.aniadir_id(_id);
            this.gda.set(indice,vnodo);
        }
        return indice;
    }   
    void listar(NodoGDA vnodo){
        NodoGDA vnodo2,vnodo3;
        int indice2;
        boolean padres_sin_listar;
        Iterator<NodoGDA> iter;
        int indice = this.gda.indexOf(vnodo);
        this.listados.add(new Integer(indice));
        indice = vnodo.get_hi();
        if(indice != -1){
            vnodo2 = this.gda.get(indice);
            //ver que no sea una hoja y no tenga padres sin listar
            padres_sin_listar=false;
            iter = this.gda.iterator();
            while(iter.hasNext() && !padres_sin_listar){
                vnodo3=(NodoGDA)iter.next();
                if(vnodo3.get_hi()==indice || vnodo3.get_hd()==indice){
                    indice2 = this.gda.indexOf(vnodo3);
                    if(!this.listados.contains(new Integer(indice2))){
                        padres_sin_listar = true;
                    }
                }
            }
            if(!padres_sin_listar)
                listar(vnodo2);
        }
        indice = vnodo.get_hd();
        if(indice != -1){
            vnodo2 = this.gda.get(indice);
            //ver que no sea una hoja y no tenga padres sin listar
            padres_sin_listar=false;
            iter = this.gda.iterator();
            while(iter.hasNext() && !padres_sin_listar){
                vnodo3=(NodoGDA)iter.next();
                if(vnodo3.get_hi()==indice || vnodo3.get_hd()==indice){
                    indice2 = this.gda.indexOf(vnodo3);
                    if(!this.listados.contains(new Integer(indice2))){
                        padres_sin_listar = true;
                    }
                }
            }
            if(!padres_sin_listar)
                listar(vnodo2);
        }
    }
    String devolver_listados(){
        String llistados = new String();
        Integer ultimo_ind;
        for(int k=this.listados.size()-1;k>=0;k--){
            ultimo_ind = this.listados.get(k);
            if(!Util.stringEmpty(llistados))
                llistados += ","+ultimo_ind.toString();
            else
                llistados = ultimo_ind.toString();
        }
        return llistados;   
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
    String nombre_reg_x86(String tipo){
        String nom_reg = new String();
        if(tipo.equalsIgnoreCase("karakterea")||tipo.equalsIgnoreCase("boolearra")){
            switch (posicion) {
                case 1: nom_reg = new String("al");
                        break;        
                case 2: nom_reg = new String("bl");
                        break;        
                case 3: nom_reg = new String("cl");
                        break;        
                case 4: nom_reg = new String("dl");
                        break;        
                case 5: nom_reg = new String("si");
                        break;        
                case 6: nom_reg = new String("di");
                        break;        
            }
        }else{
            switch (posicion) {
                case 1: nom_reg = new String("eax");
                        break;        
                case 2: nom_reg = new String("ebx");
                        break;        
                case 3: nom_reg = new String("ecx");
                        break;        
                case 4: nom_reg = new String("edx");
                        break;        
                case 5: nom_reg = new String("esi");
                        break;        
                case 6: nom_reg = new String("edi");
                        break;        
            }
        }
        return nom_reg; 
    }           
}
public class TGestorSemantico{
    PrintWriter outputStream = null;
    Stack<Reg> pila_sem = null;
    ArrayList<Cuad> tcuad;
    int indice = 0;
    Tvariables variables;
    ArrayDeque<Tvariables> tproc; // hay mas de un algoritmo, hay que utilizar una pila
    ArrayList<Descriptor_reg> treg;
    String opcion,nom_algoritmo;
    Var vble;
    TEntrada ent;
    Hashtable<String,Integer> correspondencia; // en x86 cuando se guardan las variables locales en la pila, para identificar su posicion
    TGestorSemantico(String _opc,TEntrada pent) throws IOException {
        pila_sem = new Stack<Reg>();
        outputStream = new PrintWriter(new FileWriter("a.out"));
        tcuad = new ArrayList<Cuad>();       
        tproc = new ArrayDeque<Tvariables>();
        treg = new ArrayList<Descriptor_reg>();
        treg.add(new Descriptor_reg(0));
        opcion = new String(_opc);
        ent = pent;
        if(opcion.equalsIgnoreCase("x86")){
            for(int i=1;i<=6;i++){
                treg.add(new Descriptor_reg(i));
            }
        }else{    
            for(int i=1;i<32;i++){
                treg.add(new Descriptor_reg(i));
            }
        }
    }
    String gen(String op, String arg1, String arg2, String res){
        tcuad.add(new Cuad(op,arg1,arg2,res));
        return String.valueOf(tcuad.size()-1);
    }   
    String nuevotemp(){
        indice++;
        return ("t"+String.valueOf(indice));
    }
    String num_reg_x86(String reg){
        if(reg.equalsIgnoreCase("eax")||reg.equalsIgnoreCase("al"))
            return ("r1");
        else if(reg.equalsIgnoreCase("ebx")||reg.equalsIgnoreCase("bl"))
            return ("r2");
        else if(reg.equalsIgnoreCase("ecx")||reg.equalsIgnoreCase("cl"))
            return ("r3");
        else if(reg.equalsIgnoreCase("edx")||reg.equalsIgnoreCase("dl"))
            return ("r4");
        else if(reg.equalsIgnoreCase("edi")||reg.equalsIgnoreCase("di"))
            return ("r6");
        else if(reg.equalsIgnoreCase("esi")||reg.equalsIgnoreCase("si"))
            return ("r5");
        return "r0";
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
        String vble;
        for(int i=1;i<treg.size();i++)
            if(treg.get(i).vacio()){
                hay_uno_vacio = true;
                break;
            }else{
                hay_uno_vacio = true;    
                for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
                    vble = (String)iter.next();
                    if(variables.leer_asig_marcada(vble)){
                        hay_uno_vacio = false;
                        break;
                    }
                }
                if(hay_uno_vacio) break;        
            }    
        return (!hay_uno_vacio);
    }   
    String quitar_vbles_reg(String nombre, String ldir, String etiq){ // se pone que el registro va a contener solo una vble.
        String vble;
        boolean primera_inst = true;
        int i= Integer.parseInt(ldir.substring(1));
        String etiqueta = new String(etiq);
        char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
        for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
            vble = (String)iter.next();
            if(!vble.equalsIgnoreCase(nombre)){
                if(variables.leer_asig_marcada(vble)){
                    if(opcion.equalsIgnoreCase("x86"))
                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                            outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(i).nombre_reg_x86(variables.leer_tipo(vble)));
                        }else{
                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(i).nombre_reg_x86(variables.leer_tipo(vble)));
                        }
                    else    
                       outputStream.println(etiqueta+"st r"+String.valueOf(i)+", "+vble);
                    variables.borrar_asig(vble);               
                }
                if(primera_inst){
                    primera_inst = false;
                    etiqueta = new String(tabulador);
                }                                           
                variables.quitar_desc_direcc(vble,"r"+String.valueOf(i)); // la vble. ya no esta en Ri                    
            }
        }    
        treg.get(i).contiene.clear(); // se pone al registro que ya no contiene a ninguna vble, excepto al que se pasa por parametro
        treg.get(i).contiene.add(nombre); 
        return etiqueta;   
    }
    String obtenreg(String nombre, int nbloque, String etiq){
        String vble; 
        boolean primera_inst = true;
        String etiqueta = new String(etiq);
        char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
        // devolver un registro vacio si hay alguno
        for(int i=1;i<treg.size();i++){
            if(treg.get(i).vacio()){
                treg.get(i).contiene.add(nombre);
                if(opcion.equalsIgnoreCase("x86"))
                    return (treg.get(i).nombre_reg_x86(variables.leer_tipo(nombre)));
                else    
                    return ("r"+String.valueOf(i));
            }   
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
                    if(variables.leer_asig_marcada(vble)){
                        if(opcion.equalsIgnoreCase("x86"))
                            if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                                outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(i).nombre_reg_x86(variables.leer_tipo(vble)));
                            }else{
                                outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(i).nombre_reg_x86(variables.leer_tipo(vble)));
                            }    
                        else    
                           outputStream.println(etiqueta+"st r"+String.valueOf(i)+", "+vble);
                        if(primera_inst){
                            primera_inst = false;
                            etiqueta = new String(tabulador);
                        }           
                        variables.borrar_asig(vble);
                    }                               
                    variables.quitar_desc_direcc(vble,"r"+String.valueOf(i)); // la vble. ya no esta en Ri
                }    
                treg.get(i).contiene.clear(); // se pone al registro que ya no contiene a ninguna vble, excepto al que se pasa por parametro
                treg.get(i).contiene.add(nombre);               
                // quitar a nombre de todos los otros registros 
                /*for(int k=1;k<treg.size();k++)
                    if(k!=i){
                        int j2 = treg.get(k).contiene.indexOf(nombre);
                        if (j2>=0){
                            treg.get(k).contiene.remove(j2);
                            variables.quitar_desc_direcc(nombre,"r"+String.valueOf(k)); 
                        }    
                    }*/ 
                if(opcion.equalsIgnoreCase("x86"))
                    return (treg.get(i).nombre_reg_x86(variables.leer_tipo(nombre)));
                else    
                    return ("r"+String.valueOf(i));
            }
        } 
        if(!treg.get(1).vacio()){
            for(Iterator<String> iter=treg.get(1).contiene.iterator();iter.hasNext();){ // si no se ha encontrado ningun registro ocupado con vbles sin uso en el bloque se coge el primer registro
                vble = (String)iter.next();
                if(variables.leer_asig_marcada(vble)){
                    if(opcion.equalsIgnoreCase("x86"))
                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                            outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(1).nombre_reg_x86(variables.leer_tipo(vble)));
                        }else{
                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(1).nombre_reg_x86(variables.leer_tipo(vble)));
                        }    
                    else    
                        outputStream.println(etiqueta+"st r1, "+vble);
                    if(primera_inst){
                        primera_inst = false;
                        etiqueta = new String(tabulador);
                    }                       
                    variables.borrar_asig(vble);
                }                   
                variables.quitar_desc_direcc(vble,"r1"); // la vble. ya no esta en R1
            }       
            treg.get(1).contiene.clear();   
        }
        treg.get(1).contiene.add(nombre);
        // quitar a nombre de todos los otros registros 
        /*for(int i=2;i<treg.size();i++){
            int j2 = treg.get(i).contiene.indexOf(nombre);
            if (j2>=0){
                treg.get(i).contiene.remove(j2);
                variables.quitar_desc_direcc(nombre,"r"+String.valueOf(i)); 
            }   
        }*/ 
        if(opcion.equalsIgnoreCase("x86"))
            return (treg.get(1).nombre_reg_x86(variables.leer_tipo(nombre)));
        else    
            return ("r1");
    }
    // metodo sobrecargado igual que el anterior pero con un parametro mas, para decir que se salte un determinado registro en las busqueda
    String obtenreg(String nombre, int nbloque, String etiq, int excepto){
        String vble; 
        boolean primera_inst = true;
        String etiqueta = new String(etiq);
        char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
        // devolver un registro vacio si hay alguno
        for(int i=1;i<treg.size();i++){
            if(excepto>0 && i==excepto) continue;
            if(treg.get(i).vacio()){
                treg.get(i).contiene.add(nombre);
                if(opcion.equalsIgnoreCase("x86"))
                    return (treg.get(i).nombre_reg_x86(variables.leer_tipo(nombre)));
                else    
                    return ("r"+String.valueOf(i));
            }   
        }    
        // si no se puede, encuentrese un registro ocupado R, almacenese el valor de R en una posicion de memoria
        // MOV R,M actualicese el descriptor de direcciones de M y devuelvase R
        for(int i=1;i<treg.size();i++){
            if(excepto>0 && i==excepto) continue;
            boolean enc = false;
            for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
                vble = (String)iter.next();
                if(variables.esta_activo(vble,nbloque))
                    enc = true;
            }
            if(!enc){
                for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
                    vble = (String)iter.next();
                    if(variables.leer_asig_marcada(vble)){
                        if(opcion.equalsIgnoreCase("x86"))
                            if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                                outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(i).nombre_reg_x86(variables.leer_tipo(vble)));
                            }else{
                                outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(i).nombre_reg_x86(variables.leer_tipo(vble)));
                            }    
                        else    
                           outputStream.println(etiqueta+"st r"+String.valueOf(i)+", "+vble);
                        if(primera_inst){
                            primera_inst = false;
                            etiqueta = new String(tabulador);
                        }           
                        variables.borrar_asig(vble);
                    }                               
                    variables.quitar_desc_direcc(vble,"r"+String.valueOf(i)); // la vble. ya no esta en Ri
                }    
                treg.get(i).contiene.clear(); // se pone al registro que ya no contiene a ninguna vble, excepto al que se pasa por parametro
                treg.get(i).contiene.add(nombre);               
                // quitar a nombre de todos los otros registros 
                /*for(int k=1;k<treg.size();k++)
                    if(k!=i){
                        int j2 = treg.get(k).contiene.indexOf(nombre);
                        if (j2>=0){
                            treg.get(k).contiene.remove(j2);
                            variables.quitar_desc_direcc(nombre,"r"+String.valueOf(k)); 
                        }    
                    }*/ 
                if(opcion.equalsIgnoreCase("x86"))
                    return (treg.get(i).nombre_reg_x86(variables.leer_tipo(nombre)));
                else    
                    return ("r"+String.valueOf(i));
            }
        } 
        if(!treg.get(1).vacio()){
            for(Iterator<String> iter=treg.get(1).contiene.iterator();iter.hasNext();){ // si no se ha encontrado ningun registro ocupado con vbles sin uso en el bloque se coge el primer registro
                vble = (String)iter.next();
                if(variables.leer_asig_marcada(vble)){
                    if(opcion.equalsIgnoreCase("x86"))
                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                            outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(1).nombre_reg_x86(variables.leer_tipo(vble)));
                        }else{
                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(1).nombre_reg_x86(variables.leer_tipo(vble)));
                        }    
                    else    
                        outputStream.println(etiqueta+"st r1, "+vble);
                    if(primera_inst){
                        primera_inst = false;
                        etiqueta = new String(tabulador);
                    }                       
                    variables.borrar_asig(vble);
                }                   
                variables.quitar_desc_direcc(vble,"r1"); // la vble. ya no esta en R1
            }       
            treg.get(1).contiene.clear();   
        }
        treg.get(1).contiene.add(nombre);
        // quitar a nombre de todos los otros registros 
        /*for(int i=2;i<treg.size();i++){
            int j2 = treg.get(i).contiene.indexOf(nombre);
            if (j2>=0){
                treg.get(i).contiene.remove(j2);
                variables.quitar_desc_direcc(nombre,"r"+String.valueOf(i)); 
            }   
        }*/ 
        if(opcion.equalsIgnoreCase("x86"))
            return (treg.get(1).nombre_reg_x86(variables.leer_tipo(nombre)));
        else    
            return ("r1");
    }
    boolean hay_volcado_r1(){
        String vble;
        boolean volcar = false;
        if(!treg.get(1).vacio())
            for(Iterator<String> iter=treg.get(1).contiene.iterator();iter.hasNext();){ // leer las vbles que estan en ese registro
                vble = (String)iter.next();
                if(variables.leer_asig_marcada(vble)){ // hace falta salvar en memoria?
                    volcar = true;
                    break;
                }    
            }        
        return volcar;
    }   
    void almacenar_reg(int nreg,String etiq){
        String vble; 
        boolean primera_inst = true;
        String etiqueta = new String(etiq);
        char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
        if(!treg.get(nreg).vacio()){
            for(Iterator<String> iter=treg.get(nreg).contiene.iterator();iter.hasNext();){ // leer las vbles que estan en ese registro
                vble = (String)iter.next();
                if(nreg==2) System.out.println(vble);
                if(variables.leer_asig_marcada(vble)){ // hace falta salvar en memoria?
                    if(opcion.equalsIgnoreCase("x86"))
                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                            outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(nreg).nombre_reg_x86(variables.leer_tipo(vble)));
                        }else{
                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(nreg).nombre_reg_x86(variables.leer_tipo(vble)));
                        }    
                    else    
                        outputStream.println(etiqueta+"st r"+Integer.toString(nreg)+", "+vble);
                    if(primera_inst){
                        primera_inst = false;
                        etiqueta = new String(tabulador);
                    }
                    variables.borrar_asig(vble);                        
                }                   
                variables.quitar_desc_direcc(vble,"r"+Integer.toString(nreg)); // la vble. ya no esta en Rnreg
            }  
            treg.get(nreg).contiene.clear();   
        }
    }    
    void almacenar_reg_r1(String nombre,String etiq){
        String vble; 
        boolean primera_inst = true;
        String etiqueta = new String(etiq);
        char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
        if(!treg.get(1).vacio()){
            for(Iterator<String> iter=treg.get(1).contiene.iterator();iter.hasNext();){ // leer las vbles que estan en ese registro
                vble = (String)iter.next();
                if(variables.leer_asig_marcada(vble)){ // hace falta salvar en memoria?
                    if(opcion.equalsIgnoreCase("x86"))
                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(vble)) { 
                            outputStream.println(etiqueta+"mov ["+vble+"], "+treg.get(1).nombre_reg_x86(variables.leer_tipo(vble)));
                        }else{
                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble))+"], "+treg.get(1).nombre_reg_x86(variables.leer_tipo(vble)));
                        }    
                    else    
                        outputStream.println(etiqueta+"st r1, "+vble);
                    if(primera_inst){
                        primera_inst = false;
                        etiqueta = new String(tabulador);
                    }
                    variables.borrar_asig(vble);                        
                }                   
                variables.quitar_desc_direcc(vble,"r1"); // la vble. ya no esta en R1
            }  
            treg.get(1).contiene.clear();   
        }
        if(!(nombre.charAt(0)>='0' && nombre.charAt(0)<='9')) {
            treg.get(1).contiene.add(nombre);
            // quitar a nombre de todos los otros registros 
            for(int i=2;i<treg.size();i++){
                if(!treg.get(i).vacio()){
                    int j2 = treg.get(i).contiene.indexOf(nombre);
                    if (j2>=0)
                        treg.get(i).contiene.remove(j2);
                }
            }           
        }
    }
    void inicializar_registros(){
        String vble;
        Var vble2; 
        for(int i=1;i<treg.size();i++){
            for(Iterator<String> iter=treg.get(i).contiene.iterator();iter.hasNext();){
               vble = (String)iter.next();
               variables.quitar_desc_direcc(vble,"r"+String.valueOf(i));
            }   
            treg.get(i).contiene.clear(); 
        }  
        for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){ // no deberia ser necesario pero parece ser treg(i).contiene no tiene una vble la cual en cambio si que tiene en registros a i
            vble2 = (Var)iter.next(); 
            vble2.registros.clear();
        }              
    }
    void ejecutarAccionSemantica(int numRegla,String ps) throws IOException {
        Reg arg1,arg2,op,ini;
        StringBuffer sb,sb2;
        String t,cierto,falso,cod,nom_sub,nom_dev,lizdo,ldcho;
        Cuad q,q2;
        String ind_cuad[],aux_cuad[],ultimo_quad,sig_cuad,tipotemp,grafo,lista_temp;                        
        int[] lideres; int n,i,j,l,local_bytes,nbloque,nparam,lugar,nelem;
        Bloque_basico bloque, bbloque, bloque_pred;
        Iterator<String> iter_bl,iter_bl2; 
        ArrayDeque<Tvariables> tproc2 = new ArrayDeque<Tvariables>(); 
        Tvariables var_aux; 
        ArrayList<String> mensajes,constantes,parametros,lista_tipos; 
        Hashtable<String,String> vbles_retorno;
        try {
            switch (numRegla) {
                case 1: do{
                            variables = (Tvariables)tproc.pollFirst();
                            if(variables==null) break;
                            n = 0;
                            aux_cuad = variables.get_codigo().split(",");
                            ind_cuad = new String[aux_cuad.length+1];
                            System.arraycopy(aux_cuad,0,ind_cuad,0,aux_cuad.length);
                            // escribir el programa en lenguaje intermedio de cuadruplos    
                            for (int k = 0; k < ind_cuad.length; k++)
                                if(!Util.stringEmpty(ind_cuad[k])){
                                    q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[k]).intValue());
                                    if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/") || q.op().equals("div") || q.op().equals("mod"))
                                        outputStream.println(ind_cuad[k]+": "+q.res()+":="+q.arg1()+" "+q.op()+" "+q.arg2());
                                    else if(q.op().equals(":=")) 
                                        outputStream.println(ind_cuad[k]+": "+q.res()+":="+q.arg1());
                                    else if(q.op().substring(0,2).equals("IF")) 
                                        outputStream.println(ind_cuad[k]+": IF "+q.arg1()+" "+q.op().substring(2)+" "+q.arg2()+" GOTO "+q.res());   
                                    else if(q.op().equals("GOTO")) 
                                        outputStream.println(ind_cuad[k]+": GOTO "+q.res());
                                    else if(q.op().equals("INPUT")) 
                                        outputStream.println(ind_cuad[k]+": INPUT "+q.arg1());
                                    else if(q.op().equals("WRITE")) 
                                        outputStream.println(ind_cuad[k]+": WRITE "+q.arg1());
                                    else if(q.op().equals("WRITEC")) 
                                        outputStream.println(ind_cuad[k]+": WRITE \""+q.arg1()+"\"");
                                    else if(q.op().equals("RET")) 
                                        outputStream.println(ind_cuad[k]+": RET");
                                    else if(q.op().equals("PARAM")) 
                                        outputStream.println(ind_cuad[k]+": PARAM "+q.res());
                                    else if(q.op().equals("CALL")) 
                                        if(Util.stringEmpty(q.arg2()))
                                            outputStream.println(ind_cuad[k]+": CALL "+q.res()+","+q.arg1());
                                        else
                                            outputStream.println(ind_cuad[k]+": "+q.arg2()+":= CALL "+q.res()+","+q.arg1());
                                    else if(q.op().equals("PROC")) 
                                        outputStream.println(ind_cuad[k]+": PROC "+q.res());
                                    else if(q.op().equals("[]=")) 
                                        outputStream.println(ind_cuad[k]+": "+q.arg1()+"["+q.arg2()+"]:="+q.res());
                                    else if(q.op().equals("=[]")) 
                                        outputStream.println(ind_cuad[k]+": "+q.res()+":="+q.arg1()+"["+q.arg2()+"]");
                                    else outputStream.println(ind_cuad[k]+": op desc "+q.op()+" arg1="+Util.getString(q.arg1(),"")+" arg2="+Util.getString(q.arg2(),"")+" res="+Util.getString(q.res(),""));
                                }   
                            //Particionar en bloques basicos
                            // obtener la lista de lideres, la 1a. instr., los destinos de un salto, y los que van despues de un salto
                            boolean enc;
                            lideres=new int[ind_cuad.length];
                            for (j = 0; j < ind_cuad.length; j++)
                                if(!Util.stringEmpty(ind_cuad[j])) { 
                                    lideres[n++]= Integer.valueOf(ind_cuad[j]).intValue(); // la 1a. instr. 
                                    break;
                                }   
                            for(;j<ind_cuad.length;)
                                if(!Util.stringEmpty(ind_cuad[j])){
                                    i = Integer.valueOf(ind_cuad[j]).intValue();
                                    q = (Cuad)tcuad.get(i); 
                                    if(q.op().length()>1 && (q.op().equals("CALL"))){
                                        for(j=j+1;j<ind_cuad.length;j++)
                                            if(!Util.stringEmpty(ind_cuad[j])){
                                                enc = false;
                                                for(int k=0;k<n && !enc;k++)
                                                    if(lideres[k]==Integer.valueOf(ind_cuad[j]).intValue())
                                                        enc=true;
                                                if(!enc)                                            
                                                    lideres[n++]=Integer.valueOf(ind_cuad[j]).intValue(); // los que van despues de una llamada
                                                break;
                                            }   
                                    }else if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF"))){
                                        enc = false;
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
                                        variables.tbloques.add(bloque);
                                        b++;n_cuad=1;
                                        lider = Integer.valueOf(ind_cuad[j]).intValue();
                                        bloque = new Bloque_basico(b,lider);
                                        j++;
                                    }                               
                                }else j++;
                            System.out.println("B"+b+" "+lider+" "+n_cuad);
                            variables.tbloques.add(bloque);                           
                            // para saber los sucesores de un bloque, se va al ultimo cuadruplo y se mira a que cuadruplo va el salto y 
                            // el bloque al que pertenece el destino del salto
                            for(j=0;j<variables.tbloques.size();j++){
                                bloque = variables.tbloques.get(j);
                                ultimo_quad = String.valueOf(bloque.lider);
                                for(Iterator<String> iter=bloque.cuadruplos.iterator();iter.hasNext();)
                                    ultimo_quad=(String)iter.next();
                                q=(Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF"))){
                                    // obtener el bloque destino del salto
                                    for(int k=0;k<variables.tbloques.size();k++){
                                        bbloque = variables.tbloques.get(k);
                                        if(bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                            bbloque.aniadir_pred(bloque.nbloque);
                                            variables.tbloques.set(k,bbloque);
                                            bloque.aniadir_succ(bbloque.nbloque);
                                            variables.tbloques.set(j,bloque);
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
                                        for(int k=0;k<variables.tbloques.size();k++){
                                            bbloque = variables.tbloques.get(k);
                                            if(bbloque.lider==Integer.valueOf(ultimo_quad).intValue()){
                                                bbloque.aniadir_pred(bloque.nbloque);
                                                variables.tbloques.set(k,bbloque);
                                                bloque.aniadir_succ(bbloque.nbloque);
                                                variables.tbloques.set(j,bloque);
                                                break;
                                            }
                                        }                                   
                                    }
                                }else if(j<variables.tbloques.size()-1){
                                        // ver cual es el cuadruplo a continuacion de ultimo_quad
                                        for (int k = 0; k < ind_cuad.length; k++)
                                            if(!Util.stringEmpty(ind_cuad[k]) && ind_cuad[k].equals(ultimo_quad)){
                                                do{k++;}while(k<ind_cuad.length && Util.stringEmpty(ind_cuad[k]));
                                                ultimo_quad=ind_cuad[k];
                                                break;
                                            }
                                        // repetir buscar el bloque, en este caso en lugar de q.res() con ultimo_quad
                                        for(int k=0;k<variables.tbloques.size();k++){
                                            bbloque = variables.tbloques.get(k);
                                            if(bbloque.lider==Integer.valueOf(ultimo_quad).intValue()){
                                                bbloque.aniadir_pred(bloque.nbloque);
                                                variables.tbloques.set(k,bbloque);
                                                bloque.aniadir_succ(bbloque.nbloque);
                                                variables.tbloques.set(j,bloque);
                                                break;
                                            }
                                        }                                                                   
                                }
                            }
                            // mostrar la inf. de los bloques basicos
                            grafo = new String("Graph[{");
                            for(j=0;j<variables.tbloques.size();j++){
                                bloque = variables.tbloques.get(j);
                                String lcuad = String.valueOf(bloque.lider);
                                for(Iterator<String> iter=bloque.cuadruplos.iterator();iter.hasNext();)
                                    lcuad+=","+(String)iter.next();
                                //for(ListIterator<String> iter=bloque.cuadruplos.listIterator();iter.hasPrevious();){
                                //    ultimo_quad = (String)iter.previous();
                                for(int k=bloque.cuadruplos.size()-1;k>=0;k--){
                                    ultimo_quad=bloque.cuadruplos.get(k);
                                    System.out.println("trato:"+ultimo_quad);
                                    q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                    if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/") || q.op().equals("=[]") || q.op().equals("div") || q.op().equals("mod")){
                                        variables.desactivar(q.res(),bloque.nbloque);
                                        if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                            variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                        if(!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))
                                            variables.activar(q.arg2(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                    }else if(q.op().equals(":=")) {
                                        variables.desactivar(q.res(),bloque.nbloque);
                                        if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                            variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                    }else if(q.op().length()>1 && q.op().substring(0,2).equals("IF")) {
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
                                ultimo_quad=String.valueOf(bloque.lider);
                                System.out.println("trato:"+ultimo_quad);
                                q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/") || q.op().equals("=[]") || q.op().equals("div") || q.op().equals("mod")){
                                    variables.desactivar(q.res(),bloque.nbloque);
                                    if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                        variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                    if(!Util.stringEmpty(q.arg2()) && !(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))
                                        variables.activar(q.arg2(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                }else if(q.op().equals(":=")) {
                                    variables.desactivar(q.res(),bloque.nbloque);
                                    if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                        variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                }else if(q.op().length()>1 && q.op().substring(0,2).equals("IF")) {
                                    if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                        variables.activar(q.arg1(),bloque.nbloque,Integer.valueOf(ultimo_quad).intValue());
                                    if(!Util.stringEmpty(q.arg2())&& !(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))
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
                                int n1,n2,n3;
                                while(!Util.stringEmpty(ultimo_quad)) {
                                    q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                    if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/") || q.op().equals("div") || q.op().equals("mod")){
                                        if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                            if(variables.esta_indefinido(q.arg1(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n1 = bloque.aniadir_hoja(q.arg1());
                                                variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);
                                            }else
                                                n1 = variables.obtener_nodo(q.arg1(),bloque.nbloque);
                                        else if(bloque.existe_hoja(q.arg1()))
                                                n1 = bloque.encontrar_hoja(q.arg1());
                                             else
                                                n1 = bloque.aniadir_hoja(q.arg1()); 
                                        if(!Util.stringEmpty(q.arg2()) && !(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))    
                                            if(variables.esta_indefinido(q.arg2(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n2 = bloque.aniadir_hoja(q.arg2());
                                                variables.apuntar_nodo(q.arg2(),bloque.nbloque,n2);
                                            }else
                                                n2 = variables.obtener_nodo(q.arg2(),bloque.nbloque);
                                        else if(!Util.stringEmpty(q.arg2()))
					       if(bloque.existe_hoja(q.arg2()))
                                                  n2 = bloque.encontrar_hoja(q.arg2());
                                               else
                                                  n2 = bloque.aniadir_hoja(q.arg2());
 					     else n2 = -1;
                                        if(bloque.esta_indeterminado(q.op(),n1,n2)){
                                            n1 = bloque.aniadir_nodo(q.op(),n1,n2,q.res());
                                        }else n1 = bloque.encontrar_nodo(q.op(),n1,n2,q.res()); 
                                        variables.apuntar_nodo(q.res(),bloque.nbloque,n1);
                                    }else if(q.op().equals(":=")){
					if(q.arg1().indexOf(',')<0){
					    if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
						if(variables.esta_indefinido(q.arg1(),bloque.nbloque)){
						    // creese una hoja etiquetada
						    n1 = bloque.aniadir_hoja(q.arg1());
						    variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);
						}else
						    n1 = variables.obtener_nodo(q.arg1(),bloque.nbloque);
					    else{ if(bloque.existe_hoja(q.arg1()))
						    n1 = bloque.encontrar_hoja(q.arg1());
						  else
						    n1 = bloque.aniadir_hoja(q.arg1()); 
					    }    
					    variables.apuntar_nodo(q.res(),bloque.nbloque,n1);
                                        }else{
                                            // es una asignacion multiple
					    nelem = Util.cuentaElementos(q.arg1());
					    for(int k=1;k<=nelem;k++){
                                                lizdo = Util.getElemento(q.res(),k);
						ldcho = Util.getElemento(q.arg1(),k);
   						n1 = variables.obtener_nodo(ldcho,bloque.nbloque);
						variables.apuntar_nodo(lizdo,bloque.nbloque,n1);
					    }
					}
                                    }else if(q.op().length()>1 && (q.op().equals("GOTO")||q.op().substring(0,2).equals("IF"))) {
                                        if(q.op().substring(0,2).equals("IF")){
                                            if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                                if(variables.esta_indefinido(q.arg1(),bloque.nbloque)){
                                                    // creese una hoja etiquetada
                                                    n1 = bloque.aniadir_hoja(q.arg1());
                                                    variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);
                                                }else
                                                    n1 = variables.obtener_nodo(q.arg1(),bloque.nbloque);
                                            else if(bloque.existe_hoja(q.arg1()))
                                                    n1 = bloque.encontrar_hoja(q.arg1());
                                                 else
                                                    n1 = bloque.aniadir_hoja(q.arg1()); 
                                            if(!Util.stringEmpty(q.arg2()) && !(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))    
                                                if(variables.esta_indefinido(q.arg2(),bloque.nbloque)){
                                                    // creese una hoja etiquetada
                                                    n2 = bloque.aniadir_hoja(q.arg2());
                                                    variables.apuntar_nodo(q.arg2(),bloque.nbloque,n2);
                                                }else
                                                    n2 = variables.obtener_nodo(q.arg2(),bloque.nbloque);
                                            else if(!Util.stringEmpty(q.arg2()))
						   if(bloque.existe_hoja(q.arg2()))
                                                      n2 = bloque.encontrar_hoja(q.arg2());
                                                   else
                                                       n2 = bloque.aniadir_hoja(q.arg2());
					         else n2 = -1;
                                            if(bloque.esta_indeterminado(q.op(),n1,n2)){
                                                n1 = bloque.aniadir_nodo(q.op(),n1,n2,"");
                                            }else n1 = bloque.encontrar_nodo(q.op(),n1,n2,""); 
                                        }else{
                                            if(bloque.existe_hoja(q.res()))
                                                n1 = bloque.encontrar_hoja(q.res());
                                            else
                                                n1 = bloque.aniadir_hoja(q.res()); 
                                            n1 = bloque.aniadir_nodo(q.op(),n1,-1,"");
                                        }
                                    }else if(q.op().equals("PARAM")) {
					if(!(q.res().charAt(0)>='0' && q.res().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.res())))
                                            if(variables.esta_indefinido(q.res(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n1 = bloque.aniadir_hoja(q.res());
                                                variables.apuntar_nodo(q.res(),bloque.nbloque,n1);
                                            }else
                                                n1 = variables.obtener_nodo(q.res(),bloque.nbloque);
                                        else if(bloque.existe_hoja(q.res()))
                                                n1 = bloque.encontrar_hoja(q.res());
                                             else
                                                n1 = bloque.aniadir_hoja(q.res()); 
                                        n1 = bloque.aniadir_nodo(q.op(),n1,-1,"");
                                    }else if(q.op().equals("CALL")) {
                                        if(Util.stringEmpty(q.arg2())){
					    //hoja para el num. parametros
					    if(bloque.existe_hoja(q.arg1()))
                                                n1 = bloque.encontrar_hoja(q.arg1());
                                             else
                                                n1 = bloque.aniadir_hoja(q.arg1());
					    //hoja para el nombre de procedimiento
					    n2 = bloque.aniadir_hoja(q.res());
					    //el procedimiento no devuelve valores
                                            n1 = bloque.aniadir_nodo(q.op(),n1,n2,"");
                                            //outputStream.println(ind_cuad[k]+": CALL "+q.res()+","+q.arg1());
                                        }else{
                                            //outputStream.println(ind_cuad[k]+": "+q.arg2()+":= CALL "+q.res()+","+q.arg1());
					    if(bloque.existe_hoja(q.arg1()))
                                                n1 = bloque.encontrar_hoja(q.arg1());
                                             else
                                                n1 = bloque.aniadir_hoja(q.arg1());
					    n2 = bloque.aniadir_hoja(q.res());
					    //el(los) valor(es) que devuelve la funcion asignarla(s) al nodo
                                            if(q.arg2().indexOf(',')<0){
						n1 = bloque.aniadir_nodo(q.op(),n1,n2,q.arg2());
					    }else{
                                                // es una asignacion multiple
						nelem = Util.cuentaElementos(q.arg2());
						for(int k=1;k<=nelem;k++){
                                                    lizdo = Util.getElemento(q.arg2(),k);
						    if(k==1)
							n3 = bloque.aniadir_nodo(q.op(),n1,n2,lizdo);
						    else
							n3 = bloque.encontrar_nodo(q.op(),n1,n2,lizdo);
						    variables.apuntar_nodo(lizdo,bloque.nbloque,n3);
						}
					    }
					}
				    }else if(q.op().equals("INPUT")) 
                                        n1 = bloque.aniadir_nodo(q.op(),-1,-1,q.arg1());
                                    else if(q.op().equals("WRITE")) {
                                        if(!(q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                            if(variables.esta_indefinido(q.arg1(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n1 = bloque.aniadir_hoja(q.arg1());
                                                variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);
                                            }else
                                                n1 = variables.obtener_nodo(q.arg1(),bloque.nbloque);
                                        else if(bloque.existe_hoja(q.arg1()))
                                                n1 = bloque.encontrar_hoja(q.arg1());
                                             else
                                                n1 = bloque.aniadir_hoja(q.arg1()); 
                                        n1 = bloque.aniadir_nodo(q.op(),n1,-1,"");
                                    }else if(q.op().equals("RET")) 
                                        n1 = bloque.aniadir_nodo(q.op(),-1,-1,"");
                                    else if(q.op().equals("[]=")) {
                                        //outputStream.println(ind_cuad[j]+": "+q.arg1()+"["+q.arg2()+"]:="+q.res());
                                        if(!(q.res().charAt(0)>='0' && q.res().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.res())))
                                            if(variables.esta_indefinido(q.res(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n1 = bloque.aniadir_hoja(q.res());
                                                variables.apuntar_nodo(q.res(),bloque.nbloque,n1);
                                            }else
                                                n1 = variables.obtener_nodo(q.res(),bloque.nbloque);
                                        else if(bloque.existe_hoja(q.res()))
                                                n1 = bloque.encontrar_hoja(q.res());
                                             else
                                                n1 = bloque.aniadir_hoja(q.res()); 
                                        if(!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))    
                                            if(variables.esta_indefinido(q.arg2(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n2 = bloque.aniadir_hoja(q.arg2());
                                                variables.apuntar_nodo(q.arg2(),bloque.nbloque,n2);
                                            }else
                                                n2 = variables.obtener_nodo(q.arg2(),bloque.nbloque);
                                        else if(bloque.existe_hoja(q.arg2()))
                                                n2 = bloque.encontrar_hoja(q.arg2());
                                             else
                                                n2 = bloque.aniadir_hoja(q.arg2()); 
                                        n1 = bloque.aniadir_nodo(q.op(),n1,n2,q.arg1());
                                        variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);

                                    }else if(q.op().equals("=[]")) {
                                        //outputStream.println(ind_cuad[j]+": "+q.res()+":="+q.arg1()+"["+q.arg2()+"]");
                                        // creese una hoja etiquetada
                                        n1 = bloque.aniadir_hoja(q.arg1());
                                        variables.apuntar_nodo(q.arg1(),bloque.nbloque,n1);
                                        if(!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<= '9') && !Util.stringEmpty(variables.leer_tipo(q.arg2())))    
                                            if(variables.esta_indefinido(q.arg2(),bloque.nbloque)){
                                                // creese una hoja etiquetada
                                                n2 = bloque.aniadir_hoja(q.arg2());
                                                variables.apuntar_nodo(q.arg2(),bloque.nbloque,n2);
                                            }else
                                                n2 = variables.obtener_nodo(q.arg2(),bloque.nbloque);
                                        else if(bloque.existe_hoja(q.arg2()))
                                                n2 = bloque.encontrar_hoja(q.arg2());
                                             else
                                                n2 = bloque.aniadir_hoja(q.arg2()); 
                                        n1 = bloque.aniadir_nodo(q.op(),n1,n2,q.res());
                                        variables.apuntar_nodo(q.res(),bloque.nbloque,n1);

                                    }   
                                        
                                    if(iter_bl.hasNext())
                                        ultimo_quad=(String)iter_bl.next();
                                    else
                                        ultimo_quad="";
                                }
                                // obtener un orden para listar los nodos del gda
                                Iterator<NodoGDA> iter1, iter2;
                                NodoGDA vnodo,vnodo2;
                                int ind;
                                iter1 = bloque.gda.iterator();
                                while(iter1.hasNext()){
                                    vnodo = (NodoGDA)iter1.next();
                                    ind = bloque.gda.indexOf(vnodo);
                                    if(!bloque.listados.contains(new Integer(ind))){
                                        boolean raiz = true;
                                        iter2 = bloque.gda.iterator(); // ver si hay un nodo padre
                                        while(iter2.hasNext() && raiz){
                                            vnodo2 = (NodoGDA)iter2.next();
                                            if(vnodo2.get_hi()==ind || vnodo2.get_hd()==ind)
                                                raiz = false;
                                        }
                                        if(raiz)
                                            bloque.listar(vnodo);
                                    }
                                }   
                                System.out.println("orden="+bloque.devolver_listados()); 
                                variables.tbloques.set(j,bloque);                   
				}
                            //grafo+="},VertexLabels->All]";
                            //System.out.println(grafo);
                            tproc2.addLast(variables);
                        }while(true);
                        outputStream.close();
                        // ver si hay lazos: empezando en cada bloque ver si siguiendo sus sucesores se llega al mismo bloque
                        int[] lazo;
                        String aux[];
                        for(Iterator<Tvariables> iter_v = tproc2.descendingIterator();iter_v.hasNext();){
                            variables = (Tvariables)iter_v.next();
                            variables.bucles = new ArrayList<String>();
                            for(j=0;j<variables.tbloques.size();j++){
                                ArrayList<String> llazos = new ArrayList<String>();
                                lazo = new int[variables.tbloques.size()];
                                bloque = variables.tbloques.get(j);
                                lazo[0]=bloque.nbloque;
                                encontrar_lazos(variables.tbloques,lazo,0,llazos);
                                for(Iterator<String> iter=llazos.iterator();iter.hasNext();){
                                    grafo = (String)iter.next();
                                    if(no_esta(grafo,variables.bucles))
                                        variables.bucles.add(grafo);
                                }
                            }   
                            for(Iterator<String> iter=variables.bucles.iterator();iter.hasNext();){ // faltaria para cada lazo, calcular la cuenta de uso de las variables que aparecen en el lazo
                                grafo = (String)iter.next();
                                System.out.println("lazo:"+grafo);
                                aux = grafo.split("->");
                                for(int k=0;k<aux.length-1;k++){
                                    nbloque = Integer.parseInt(aux[k]); // pintar las vbles activas en el bloque
                                    bloque = variables.tbloques.get(nbloque);
                                    bloque.lvaract = new ArrayList<String>();
                                    for(Iterator<Var> iter_vble=variables.devuelve_vbles();iter_vble.hasNext();){
                                        vble = (Var)iter_vble.next();
                                        if(vble.activo(nbloque)>=0) {
                                            bloque.lvaract.add(vble.nombre());
                                        }
                                    }
                                    String lvar = new String(aux[k]+":");
                                    for(Iterator<String> iter_var=bloque.lvaract.iterator();iter_var.hasNext();){
                                        String var1 = (String)iter_var.next();
                                        lvar += var1+",";
                                    }
                                    System.out.println(lvar);
                                    variables.tbloques.set(nbloque,bloque);     
                                }
                            } 
                        }    
                        // generar codigo objeto en el lenguaje ensamblador x86 (nasm)
                        // obtener de la pila de procedimientos (del fondo de la pila) el nombre del algoritmo
                        variables = (Tvariables)tproc2.getLast();
                        nom_algoritmo = new String(variables.get_proc());
                        String factor,tipo_array; 
                        int bdestino = 0;
                        int nproc=0;
                        ArrayList<String> lista_id = new ArrayList<String>();
                        if(opcion.equalsIgnoreCase("x86")){
                            outputStream = new PrintWriter(new FileWriter(nom_algoritmo.toLowerCase()+".asm"));                            
                            for(Iterator<Tvariables> iter_v = tproc2.descendingIterator();iter_v.hasNext();nproc++){
                                variables = (Tvariables)iter_v.next();
                                char tabulador[]={' ',' ',' ',' ',' ',' ',' ',' '};
                                String ldir,xp,yp,zp,etiqueta,espacios;
                                boolean poner_etiqueta,saltar,primera_inst,limpiar_etiq,salvado_memoria,enc_op2,enc_op1;
                                mensajes = new ArrayList<String>();
                                constantes = new ArrayList<String>();                                
                                correspondencia = new Hashtable<String,Integer>();                                
                                i=1;l=1;local_bytes=0;
                                etiqueta = new String(tabulador);
                                if(variables.get_proc().equals(nom_algoritmo)){ 
                                    outputStream.println("; Para crear el ejecutable usando bcc32c");
                                    outputStream.println(";");
                                    outputStream.println("; nasm -f obj -DOBJ_TYPE "+nom_algoritmo.toLowerCase()+".asm");
                                    outputStream.println("; bcc32c "+nom_algoritmo.toLowerCase()+".obj driver.obj asm_io.obj");
                                    outputStream.println("");
                                    // aqui irian las directivas %include
                                    outputStream.println("%include \"asm_io.inc\"");
                                    // las constantes con equ
                                    for(j=0;j<variables.tbloques.size();j++){
                                        bloque = variables.tbloques.get(j);
                                        ultimo_quad = String.valueOf(bloque.lider);
                                        iter_bl = bloque.cuadruplos.iterator();
                                        while(!Util.stringEmpty(ultimo_quad)){
                                            q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                            if(q.op().equals(":=")) {
                                                if(q.arg1().substring(0,1).equals("\"") && !constantes.contains(q.arg1())) {
                                                    outputStream.println("L"+Integer.toString(l)+" equ "+q.arg1());
                                                    constantes.add(q.arg1());
                                                    l++;                                                    
                                                }
                                            }else if(q.op().substring(0,1).equals("I") && q.op().substring(0,2).equals("IF")) {
                                                if(q.arg1().substring(0,1).equals("\"") && !constantes.contains(q.arg1())) {
                                                    outputStream.println("L"+Integer.toString(l)+" equ "+q.arg1());
                                                    constantes.add(q.arg1());
                                                    l++;                                                    
                                                }
                                                if(q.arg2().substring(0,1).equals("\"") && !constantes.contains(q.arg2())) {
                                                    outputStream.println("L"+Integer.toString(l)+" equ "+q.arg2());
                                                    constantes.add(q.arg2());
                                                    l++;                                                    
                                                }
                                            }    
                                            if(iter_bl.hasNext())
                                                ultimo_quad=(String)iter_bl.next();
                                            else
                                                ultimo_quad="";
                                        }                                
                                    }    
                                    // como en nuestro lenguaje todavia no estan definidas inicializaciones al declarar las variables, para que haya sement data se buscan si hay cadenas de texto
                                    for(j=0;j<variables.tbloques.size();j++){
                                        bloque = variables.tbloques.get(j);
                                        ultimo_quad = String.valueOf(bloque.lider);
                                        iter_bl = bloque.cuadruplos.iterator();
                                        while(!Util.stringEmpty(ultimo_quad)){
                                            q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                            if(q.op().equals("WRITEC")){
                                                if(i==1){
                                                    outputStream.println("%ifdef OBJ_TYPE");
                                                    outputStream.println("segment _DATA public align=4 class=DATA use32"); // usa borland c
                                                    outputStream.println("%else");
                                                    outputStream.println("segment .data"); // usa djgpp
                                                    outputStream.println("%endif");
                                                }    
                                                outputStream.println("prompt"+Integer.toString(i)+" db "+q.arg1()+", 0");
                                                mensajes.add(q.arg1());
                                                i++;
                                            }    
                                            if(iter_bl.hasNext())
                                                ultimo_quad=(String)iter_bl.next();
                                            else
                                                ultimo_quad="";
                                        }                                
                                    }    
                                    // declaracion de las variables globales
                                    i=1;
                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                        vble = (Var)iter.next(); 
                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                            !Util.stringEmpty(vble.tipo()) && (vble.es_local()||vble.es_temporal())){
                                            if(i==1){
                                                outputStream.println("%ifdef OBJ_TYPE");
                                                outputStream.println("segment _BSS public align=4 class=BSS use32"); // usa borland c
                                                outputStream.println("%else");
                                                outputStream.println("segment .bss"); // usa djgpp
                                                outputStream.println("%endif");
                                            }
                                            i++;
                                            if(vble.tipo().substring(0,4).equals("arra")){
                                                if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("osoa"))
                                                    outputStream.println(vble.nombre()+" resd "+String.valueOf(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1));
                                                else if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("erreala"))
                                                        outputStream.println(vble.nombre()+" resq "+String.valueOf(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1));
                                                else if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("karakterea"))
                                                        outputStream.println(vble.nombre()+" resb "+String.valueOf(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1));
                                                else if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("boolearra"))
                                                        outputStream.println(vble.nombre()+" resb "+String.valueOf(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1));
                                                     else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter())
                                                         outputStream.println(vble.nombre()+" resd "+String.valueOf(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1));
                                            }else
                                                if(vble.tipo().equals("osoa"))
                                                    outputStream.println(vble.nombre()+" resd 1");
                                                else if(vble.tipo().equals("erreala"))
                                                        outputStream.println(vble.nombre()+" resq 1");
                                                else if(vble.tipo().equals("karakterea"))
                                                        outputStream.println(vble.nombre()+" resb 1");
                                                else if(vble.tipo().equals("boolearra"))
                                                        outputStream.println(vble.nombre()+" resb 1");
                                                     else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter())
                                                         outputStream.println(vble.nombre()+" resd 1");
                                        }         
                                    }
                                    outputStream.println(variables.get_comentario());
                                    outputStream.println("%ifdef OBJ_TYPE");
                                    if(mensajes.isEmpty())
                                        outputStream.println("group DGROUP _BSS");
                                    else
                                        outputStream.println("group DGROUP _BSS _DATA");
                                    outputStream.println("segment _TEXT public align=1 class=CODE use32"); // usa borland c
                                    outputStream.println("%else");
                                    outputStream.println("segment .text"); // usa djgpp
                                    outputStream.println("%endif");
                                    outputStream.println(etiqueta+"global _asm_main");
                                    outputStream.println("_asm_main:");
                                    outputStream.println(etiqueta+"enter 0,0 ");
                                    outputStream.println(etiqueta+"pusha");
                                }else{
                                    outputStream.println("; subprograma "+variables.get_proc());
                                    for(j=0;j<variables.tbloques.size();j++){
                                        bloque = variables.tbloques.get(j);
                                        ultimo_quad = String.valueOf(bloque.lider);
                                        iter_bl = bloque.cuadruplos.iterator();
                                        while(!Util.stringEmpty(ultimo_quad)){
                                            q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                            if(q.op().equals(":=")) {
                                                if(q.arg1().substring(0,1).equals("\"") && !constantes.contains(q.arg1())){
                                                    outputStream.println("L"+String.valueOf(nproc*10+l)+" equ "+q.arg1());
                                                    constantes.add(q.arg1());
                                                    l++;                                                    
                                                }
                                            }else if(q.op().substring(0,1).equals("I") && q.op().substring(0,2).equals("IF")) {
                                                if(q.arg1().substring(0,1).equals("\"") && !constantes.contains(q.arg1())){
                                                    outputStream.println("L"+String.valueOf(nproc*10+l)+" equ "+q.arg1());
                                                    constantes.add(q.arg1());
                                                    l++;                                                    
                                                }
                                                if(q.arg2().substring(0,1).equals("\"") && !constantes.contains(q.arg2())){
                                                    outputStream.println("L"+String.valueOf(nproc*10+l)+" equ "+q.arg2());
                                                    constantes.add(q.arg2());
                                                    l++;                                                    
                                                }
                                            }
                                            if(iter_bl.hasNext())
                                                ultimo_quad=(String)iter_bl.next();
                                            else
                                                ultimo_quad="";
                                        }                                
                                    }    
                                    for(j=0;j<variables.tbloques.size();j++){
                                        bloque = variables.tbloques.get(j);
                                        ultimo_quad = String.valueOf(bloque.lider);
                                        iter_bl = bloque.cuadruplos.iterator();
                                        while(!Util.stringEmpty(ultimo_quad)){
                                            q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                            if(q.op().equals("WRITEC")){
                                                if(i==1){
                                                    outputStream.println("%ifdef OBJ_TYPE");
                                                    outputStream.println("segment _DATA public align=4 class=DATA use32"); // usa borland c
                                                    outputStream.println("%else");
                                                    outputStream.println("segment .data"); // usa djgpp
                                                    outputStream.println("%endif");
                                                }
                                                outputStream.println("prompt"+String.valueOf(nproc*10+i)+" db \""+q.arg1()+"\", 0");
                                                mensajes.add(q.arg1());
                                                i++;
                                            }
                                            if(iter_bl.hasNext())
                                                ultimo_quad=(String)iter_bl.next();
                                            else
                                                ultimo_quad="";
                                        }                                
                                    }    
                                    // declaracion de las variables globales
                                    i=1;
                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                        vble = (Var)iter.next(); 
                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                            !Util.stringEmpty(vble.tipo()) && (vble.es_local()||vble.es_temporal())){
                                            if(vble.tipo().substring(0,4).equals("arra")){
                                                if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("osoa")){
                                                    local_bytes += 4*(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1);
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));                                                    
                                                }
                                                else if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("erreala")){
                                                    local_bytes += 8*(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1);
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));                                                    

                                                }
                                                else if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("karakterea")){
                                                    local_bytes += (Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1);
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));                                                                                                        
                                                }
                                                else if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("boolearra")){
                                                    local_bytes += (Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1);
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));                                                                                                                                                            
                                                }
                                                else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter()){
                                                    local_bytes += 4*(Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1);
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));                                                                                                        
                                                }
                                            }else
                                                if(vble.tipo().equals("osoa")){
                                                    local_bytes += 4;
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));
                                                }else if(vble.tipo().equals("erreala")){
                                                    local_bytes += 8;
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));
                                                }else if(vble.tipo().equals("karakterea")){
                                                    local_bytes += 1;
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));
                                                }else if(vble.tipo().equals("boolearra")){
                                                    local_bytes += 1;
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));
                                                }else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter()){
                                                    local_bytes += 4;
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));
                                                }
                                        }         
                                    }
                                    outputStream.println(variables.get_comentario());
                                    outputStream.println("%ifndef OBJ_TYPE");
                                    outputStream.println("segment .text"); // usa djgpp
                                    outputStream.println("%endif");
                                    outputStream.println(variables.get_proc().replace('-','_')+":");
                                    if(local_bytes>0)
                                        outputStream.println(etiqueta+"enter "+Integer.toString(local_bytes)+",0");
                                    else{ 
                                        outputStream.println(etiqueta+"push ebp");
                                        outputStream.println(etiqueta+"mov ebp, esp");
                                    }    
                                }
                                // mostrar por pantalla vbles.
                                /*if(!variables.get_proc().equals(nom_algoritmo)){    
                                    System.out.println("variables");
                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                        vble = (Var)iter.next(); 
                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&&!Util.stringEmpty(vble.tipo())){
                                            System.out.println(vble.nombre()+" : "+vble.tipo());
                                            if(vble.es_local())
                                                if(!variables.get_proc().equals(nom_algoritmo))
                                                    System.out.println("local"+Integer.toString(correspondencia.get(vble.nombre())));
                                                else
                                                    System.out.println("local");
                                            else
                                                System.out.println("no local");
                                            if(vble.es_temporal())
                                                if(!variables.get_proc().equals(nom_algoritmo))
                                                    System.out.println("temporal"+Integer.toString(correspondencia.get(vble.nombre())));
                                                else
                                                    System.out.println("temporal");
                                            else
                                                System.out.println("no temporal");
                                        } 
                                    }   
                                } */        
                                // fin mostrar por pantalla vbles.
                                for(j=0;j<variables.tbloques.size();j++){
                                    bloque = variables.tbloques.get(j);
                                    inicializar_registros();
                                    salvado_memoria = false;
                                    // ver si a la primera instruccion hay que ponerle etiqueta
                                    poner_etiqueta = false;
                                    for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext() && !poner_etiqueta;){
                                        String nbpred=(String)iter.next();
                                        bbloque = variables.tbloques.get(Integer.valueOf(nbpred).intValue());
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
                                        etiqueta = "etiq"+String.valueOf(nproc*100+bloque.nbloque)+":";
                                        espacios = new String(tabulador,0,8 - etiqueta.length());
                                        etiqueta += espacios;
                                    }else
                                        etiqueta = new String(tabulador);
                                    primera_inst = true;
                                    ultimo_quad = String.valueOf(bloque.lider);
                                    iter_bl = bloque.cuadruplos.iterator();
                                    String lcuad = String.valueOf(bloque.lider);
                                    for(Iterator<String> iter=bloque.cuadruplos.iterator();iter.hasNext();)
                                        lcuad+=","+(String)iter.next();
                                    System.out.println("cuad="+lcuad);
                                    while(!Util.stringEmpty(ultimo_quad)){
                                        q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                        System.out.println("instr="+q.op());
                                        if(q.op().equals("+") || q.op().equals("-") || q.op().equals("/")){
                                            // ver si se va a usar q.arg1() y q.arg2() en alguna instruccion a continuacion, por si interesa moverla a registro
                                            iter_bl2 = bloque.cuadruplos.iterator();
                                            do{
                                                if(iter_bl2.hasNext())
                                                    sig_cuad = (String)iter_bl2.next();
                                            }while(iter_bl2.hasNext() && iter_bl!=iter_bl2);
                                            if(iter_bl2.hasNext())
                                                sig_cuad = (String)iter_bl2.next();
                                            else
                                                sig_cuad = "";
                                            enc_op1 = false; enc_op2=false;
                                            while(!Util.stringEmpty(sig_cuad)){
                                                q2 = (Cuad)tcuad.get(Integer.valueOf(sig_cuad).intValue());
                                                if(q2.arg1().equalsIgnoreCase(q.arg2()) || q2.arg2().equalsIgnoreCase(q.arg2()))
                                                    enc_op2 = true;
                                                if(q2.arg1().equalsIgnoreCase(q.arg1()) || q2.arg2().equalsIgnoreCase(q.arg1()))
                                                    enc_op1 = true;
                                                if(iter_bl2.hasNext())
                                                    sig_cuad = (String)iter_bl2.next();
                                                else
                                                    sig_cuad = "";
                                            }
                                            ldir = new String();
                                            // invocar obtenreg para determinar donde se guarda el resultado
                                            // falta ver si alguno de los operandos es una cte.
                                            if(variables.esta_en_reg(q.arg1())){
                                                yp = treg.get(variables.obtener_pos_en_curso(q.arg1(),"")).nombre_reg_x86(variables.leer_tipo(q.arg1()));
                                                limpiar_etiq = false;
                                                if(primera_inst)
                                                    if(hay_volcado_registro())
                                                        limpiar_etiq = true;
                                                xp = obtenreg(q.res(),bloque.nbloque,etiqueta);
                                                if(limpiar_etiq){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                } 
                                                outputStream.println(etiqueta+"mov "+xp+", "+yp);
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                } 
                                                yp = xp;
                                            }else{
                                               // if(!enc_op1 && !enc_op2){
                                                    limpiar_etiq = false;
                                                    if(primera_inst)
                                                        if(hay_volcado_registro())
                                                            limpiar_etiq = true;
                                                    yp = obtenreg(q.arg1(),bloque.nbloque,etiqueta);
                                                    if(limpiar_etiq){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }                                           
                                                    if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||(q.arg1().charAt(0)=='-'))
                                                        outputStream.println(etiqueta+"mov "+yp+", "+q.arg1());
                                                    else{
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                            if(variables.get_proc().equals(nom_algoritmo))  
                                                                outputStream.println(etiqueta+"mov "+yp+", ["+q.arg1()+"]");
                                                            else
                                                                outputStream.println(etiqueta+"mov "+yp+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg1()))+"]");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov "+yp+", [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]");
                                                        }   
                                                    }
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }                                    
                                               // }else if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))){
                                               //         yp = q.arg1();
                                               //       }else if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                               //         yp = "["+q.arg1()+"]"; 
                                               //       }else{
                                               //         yp = "[ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]";
                                               //       }        
                                            }    
                                            if(variables.esta_en_reg(q.arg2())){
                                                zp = treg.get(variables.obtener_pos_en_curso(q.arg2(),"")).nombre_reg_x86(variables.leer_tipo(q.arg2()));
                                            }else{ 
                                                if(enc_op2){
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
                                                        outputStream.println(etiqueta+"mov "+zp+", "+q.arg2());
                                                    else{
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                            if(variables.get_proc().equals(nom_algoritmo))  
                                                                outputStream.println(etiqueta+"mov "+zp+", ["+q.arg2()+"]");
                                                            else
                                                                outputStream.println(etiqueta+"mov "+zp+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov "+zp+", [ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]");
                                                        } 
                                                        variables.actualiza_descrip_direcc(q.arg2(),num_reg_x86(zp),false);
                                                    }
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }                                    
                                                }else if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2()))){
                                                         zp = q.arg2();   
                                                      }else if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                         if(variables.get_proc().equals(nom_algoritmo))  
                                                            zp =  "["+q.arg2()+"]";
                                                         else
                                                            zp = "[ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]";
                                                      }else{
                                                         zp = "[ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]";
                                                      }    
                                            } 
                                            if((zp.charAt(0)>='0' && zp.charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(zp))) {
                                                if(q.op().equals("+"))
                                                    if(zp.equals("1"))
                                                        outputStream.println(etiqueta+"inc "+yp);
                                                    else    
                                                        outputStream.println(etiqueta+"add "+yp+", "+zp);
                                                else if(q.op().equals("-"))
                                                    if(zp.equals("1"))
                                                        outputStream.println(etiqueta+"dec "+yp);
                                                    else  
                                                        outputStream.println(etiqueta+"sub "+yp+", "+zp);
                                                else if(q.op().equals("/"))
                                                    outputStream.println(etiqueta+"div "+yp+", "+zp);
                                            }else{
                                                if(q.op().equals("+"))
                                                    outputStream.println(etiqueta+"add "+yp+", "+zp);
                                                else if(q.op().equals("-"))
                                                    outputStream.println(etiqueta+"sub "+yp+", "+zp);
                                                else if(q.op().equals("/"))
                                                    outputStream.println(etiqueta+"div "+yp+", "+zp);                                                
                                            }
                                            if(primera_inst){
                                                primera_inst = false;
                                                etiqueta = new String(tabulador);
                                            }
                                            // se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
                                            variables.actualiza_descrip_direcc(q.res(),num_reg_x86(yp),true);
                                            //etiqueta=quitar_vbles_reg(q.res(),num_reg_x86(yp),etiqueta);
                                        }else if(q.op().equals("*") || q.op().equals("div") || q.op().equals("mod")){
                                            if(!(variables.esta_en_reg(q.arg1()) && variables.obtener_pos_en_curso(q.arg1(),"r1")==1)){
                                                almacenar_reg_r1(q.arg1(),etiqueta);
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||(q.arg1().charAt(0)=='-'))
                                                    outputStream.println(etiqueta+"mov eax, "+q.arg1());
                                                else{ 
                                                    if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                         if(variables.get_proc().equals(nom_algoritmo))  
                                                            outputStream.println(etiqueta+"mov eax,["+q.arg1()+"]");
                                                         else
                                                            outputStream.println(etiqueta+"mov eax,[ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg1()))+"]");
                                                    }else{
                                                       outputStream.println(etiqueta+"mov eax,[ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]");
                                                    }    
                                                    treg.get(1).contiene.add(q.arg1());
                                                    variables.actualiza_descrip_direcc(q.arg1(),"r1",false);
                                                }
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }                                    
                                            }
                                            almacenar_reg(4,etiqueta);
                                            outputStream.println(etiqueta+"cdq");
                                            if(primera_inst){
                                                primera_inst = false;
                                                etiqueta = new String(tabulador);
                                            }  
                                            almacenar_reg(3,etiqueta);
                                            if(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9'){
                                                outputStream.println(etiqueta+"mov ecx, "+q.arg2());
                                            }else if(!(variables.esta_en_reg(q.arg2()) && variables.obtener_pos_en_curso(q.arg2(),"r3")==3)){
                                                if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                     if(variables.get_proc().equals(nom_algoritmo))  
                                                        outputStream.println(etiqueta+"mov ecx,["+q.arg2()+"]");
                                                     else
                                                        outputStream.println(etiqueta+"mov ecx,[ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]");
                                                }else{
                                                   outputStream.println(etiqueta+"mov ecx,[ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]");
                                                }    
                                                treg.get(3).contiene.add(q.arg2());
                                                variables.actualiza_descrip_direcc(q.arg2(),"r3",false);
                                            }
                                            if(q.op().equals("*")){
                                                outputStream.println(etiqueta+"imul ecx");
                                                treg.get(1).contiene.add(q.res());
                                                variables.actualiza_descrip_direcc(q.res(),"r1",true);
                                            }else{
                                                outputStream.println(etiqueta+"idiv ecx");
                                                if(q.op().equals("mod")){
                                                    treg.get(4).contiene.add(q.res());
                                                    variables.actualiza_descrip_direcc(q.res(),"r4",true);
                                                }else{
                                                    treg.get(1).contiene.add(q.res());
                                                    variables.actualiza_descrip_direcc(q.res(),"r1",true);
                                                }    
                                            }
                                        }else if(q.op().equals(":=")){
                                            nelem = Util.cuentaElementos(q.res());
                                            if(q.res().indexOf(',')<0){
                                                // es una asignacion normal
                                                yp = new String();
                                                if(variables.esta_en_reg(q.res())){
                                                    ldir = treg.get(variables.obtener_pos_en_curso(q.res(),"")).nombre_reg_x86(variables.leer_tipo(q.res()));
                                                    variables.marcar_asignacion(q.res());
                                                    etiqueta=quitar_vbles_reg(q.res(),num_reg_x86(ldir),etiqueta); // si en el registro hubiera otras variables distintas a q.res(), hay que salvar a memoria esas variables y quitarlas de los descriptores
                                                }else{
                                                    limpiar_etiq = false; 
                                                    if(primera_inst)
                                                        if(hay_volcado_registro())
                                                            limpiar_etiq = true;
                                                    ldir = obtenreg(q.res(),bloque.nbloque,etiqueta,5);
                                                    if(limpiar_etiq){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }                                           
                                                    variables.actualiza_descrip_direcc(q.res(),num_reg_x86(ldir),true);
                                                }
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||(q.arg1().substring(0,1).equals("\""))||
						   (q.arg1().charAt(0)=='-')) {
                                                    if(!((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||(q.arg1().charAt(0)=='-'))) {
                                                        for(int k=0;k<constantes.size();k++)
                                                            if(constantes.get(k).equalsIgnoreCase(q.arg1())){
                                                                yp = "L"+Integer.toString(nproc*10+k+1);
                                                                break;    
                                                            }        
                                                    }else yp = q.arg1();            
                                                    outputStream.println(etiqueta+"mov "+ldir+", "+yp);
                                                }else
                                                    if(!variables.esta_en_reg(q.arg1())) {
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                            if(variables.get_proc().equals(nom_algoritmo))  
                                                                outputStream.println(etiqueta+"mov "+ldir+", ["+q.arg1()+"]");
                                                            else
                                                                outputStream.println(etiqueta+"mov "+ldir+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg1()))+"]");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov "+ldir+", [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]");
                                                        }  
                                                        variables.actualiza_descrip_direcc(q.arg1(),num_reg_x86(ldir),false);
                                                    }else{
                                                        yp = treg.get(variables.obtener_pos_en_curso(q.arg1(),"")).nombre_reg_x86(variables.leer_tipo(q.arg1()));
                                                        if(!ldir.equals(yp))
                                                            outputStream.println(etiqueta+"mov "+ldir+", "+yp);
                                                    }
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                            }else{
                                                // es una asignacion multiple
                                                for(int k=1;k<=nelem;k++){
                                                    lizdo = Util.getElemento(q.res(),k);
                                                    ldcho = Util.getElemento(q.arg1(),k);
                                                    yp = new String();
                                                    if(variables.esta_en_reg(lizdo)){
                                                        ldir = treg.get(variables.obtener_pos_en_curso(lizdo,"")).nombre_reg_x86(variables.leer_tipo(lizdo));
                                                        variables.marcar_asignacion(lizdo);
                                                        etiqueta=quitar_vbles_reg(lizdo,num_reg_x86(ldir),etiqueta); // si en el registro hubiera otras variables distintas a q.res()[k], hay que salvar a memoria esas variables y quitarlas de los descriptores
                                                    }else{
                                                        limpiar_etiq = false; 
                                                        if(primera_inst)
                                                            if(hay_volcado_registro())
                                                                limpiar_etiq = true;
                                                        ldir = obtenreg(lizdo,bloque.nbloque,etiqueta,5);
                                                        if(limpiar_etiq){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }                                           
                                                        variables.actualiza_descrip_direcc(lizdo,num_reg_x86(ldir),true);
                                                    }
                                                    if(!variables.esta_en_reg(ldcho)) {
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(ldcho)){ 
                                                            if(variables.get_proc().equals(nom_algoritmo))  
                                                                outputStream.println(etiqueta+"mov "+ldir+", ["+ldcho+"]");
                                                            else
                                                                outputStream.println(etiqueta+"mov "+ldir+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(ldcho))+"]");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov "+ldir+", [ebp - "+Integer.toString(correspondencia.get(ldcho))+"]");
                                                        }  
                                                        variables.actualiza_descrip_direcc(ldcho,num_reg_x86(ldir),false);
                                                    }else{
                                                        yp = treg.get(variables.obtener_pos_en_curso(ldcho,"")).nombre_reg_x86(variables.leer_tipo(ldcho));
                                                        if(!ldir.equals(yp))
                                                            outputStream.println(etiqueta+"mov "+ldir+", "+yp);
                                                    }
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }    
                                            }
                                        }else if(q.op().substring(0,2).equals("IF")){
                                                yp = new String();
                                                zp = new String();
                                                // ver si se va a usar q.arg1() y q.arg2() en alguna instruccion a continuacion, por si interesa moverla a registro
                                                iter_bl2 = bloque.cuadruplos.iterator();
                                                do{
                                                    if(iter_bl2.hasNext())
                                                        sig_cuad = (String)iter_bl2.next();
                                                }while(iter_bl2.hasNext() && iter_bl!=iter_bl2);
                                                if(iter_bl2.hasNext())
                                                    sig_cuad = (String)iter_bl2.next();
                                                else
                                                    sig_cuad = "";
                                                enc_op1 = false; enc_op2=false;
                                                while(!Util.stringEmpty(sig_cuad)){
                                                    q2 = (Cuad)tcuad.get(Integer.valueOf(sig_cuad).intValue());
                                                    if(q2.arg1().equalsIgnoreCase(q.arg2()) || q2.arg2().equalsIgnoreCase(q.arg2()))
                                                        enc_op2 = true;
                                                    if(q2.arg1().equalsIgnoreCase(q.arg1()) || q2.arg2().equalsIgnoreCase(q.arg1()))
                                                        enc_op1 = true;
                                                    if(iter_bl2.hasNext())
                                                        sig_cuad = (String)iter_bl2.next();
                                                    else
                                                        sig_cuad = "";
                                                }
                                                if(variables.esta_en_reg(q.arg1())){
                                                    yp = treg.get(variables.obtener_pos_en_curso(q.arg1(),"")).nombre_reg_x86(variables.leer_tipo(q.arg1()));
                                                }else{
                                                    if(enc_op1 ||(!enc_op1 && !enc_op2)){
                                                        limpiar_etiq = false; 
                                                        if(primera_inst)
                                                            if(hay_volcado_registro())
                                                                limpiar_etiq = true;
                                                        yp = obtenreg(q.arg1(),bloque.nbloque,etiqueta);
                                                        if(limpiar_etiq){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }                                           
                                                        if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
							   (q.arg1().charAt(0)=='-')){
                                                            outputStream.println(etiqueta+"mov "+yp+", "+q.arg1());
                                                        }else{
                                                            if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){
                                                                if(variables.get_proc().equals(nom_algoritmo))  
                                                                    outputStream.println(etiqueta+"mov "+yp+", ["+q.arg1()+"]");
                                                                else
                                                                    outputStream.println(etiqueta+"mov "+yp+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg1()))+"]");
                                                            }else{
                                                                outputStream.println(etiqueta+"mov "+yp+", [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]");
                                                            }    
                                                            variables.actualiza_descrip_direcc(q.arg1(),num_reg_x86(yp),false);
                                                        }
                                                        if(primera_inst){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }
                                                    }else 
                                                        if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||q.arg1().substring(0,1).equals("\"")||(q.arg1().charAt(0)=='-'))
                                                            if((!((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||(q.arg1().charAt(0)=='-')) && Util.stringEmpty(variables.leer_tipo(q.arg1())))||q.arg1().substring(0,1).equals("\"")) {
                                                                for(int k=0;k<constantes.size();k++)
                                                                    if(constantes.get(k).equalsIgnoreCase(q.arg1())){
                                                                        yp = "L"+Integer.toString(nproc*10+k+1);
                                                                        break;    
                                                                    }        
                                                            }else yp = q.arg1();            
                                                        else if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                                if(variables.get_proc().equals(nom_algoritmo))  
                                                                   yp =  "["+q.arg1()+"]";
                                                                else
                                                                   yp = "[ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg1()))+"]";
                                                             }else{
                                                                yp = "[ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]";
                                                             }
                                                }    
                                                if(variables.esta_en_reg(q.arg2())){
                                                    zp = treg.get(variables.obtener_pos_en_curso(q.arg2(),"")).nombre_reg_x86(variables.leer_tipo(q.arg2()));
                                                }else{
                                                    if(enc_op2){
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
                                                            outputStream.println(etiqueta+"mov "+zp+", "+q.arg2());
                                                        else{
                                                            if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                                if(variables.get_proc().equals(nom_algoritmo))  
                                                                    outputStream.println(etiqueta+"mov "+zp+", ["+q.arg2()+"]");
                                                                else
                                                                    outputStream.println(etiqueta+"mov "+zp+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]");
                                                            }else{
                                                                outputStream.println(etiqueta+"mov "+zp+", [ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]");
                                                            }
                                                            variables.actualiza_descrip_direcc(q.arg2(),num_reg_x86(zp),false);
                                                        }
                                                        if(primera_inst){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }
                                                    }else 
                                                        if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2()))||q.arg1().substring(0,1).equals("\""))
                                                            if((!(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9') && Util.stringEmpty(variables.leer_tipo(q.arg2()))||q.arg2().substring(0,1).equals("\"")) ) {
                                                                for(int k=0;k<constantes.size();k++)
                                                                    if(constantes.get(k).equalsIgnoreCase(q.arg2())){
                                                                        zp = "L"+Integer.toString(nproc*10+k+1);
                                                                        break;    
                                                                    } 
                                                            }else zp = q.arg2();
                                                        else if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                                if(variables.get_proc().equals(nom_algoritmo))  
                                                                   zp =  "["+q.arg2()+"]";
                                                                else
                                                                   zp = "[ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]";
                                                             }else{
                                                                zp = "[ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]";
                                                             }   
                                                }  
                                                outputStream.println(etiqueta+"cmp "+yp+", "+zp);
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                                // averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
                                                for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
                                                    String sucesor = (String)iter.next();
                                                    bbloque = variables.tbloques.get(Integer.valueOf(sucesor).intValue());
                                                    if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                                        bdestino = bbloque.nbloque;
                                                        break;
                                                    }
                                                }
                                                salvado_memoria = true;     
                                                for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                                    vble = (Var)iter.next(); 
                                                    if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                        !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && vble.es_local()) {
                                                        ldir = new String(treg.get(vble.devolver_registro("")).nombre_reg_x86(vble.tipo()));
                                                        if(variables.get_proc().equals(nom_algoritmo)) { 
                                                            outputStream.println(etiqueta+"mov ["+vble.nombre()+"], "+ldir+";<--");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble.nombre()))+"], "+ldir+";<--");
                                                        }    
                                                        vble.borrar_asig();
                                                    }
                                                }          
                                                if(q.op().substring(2).trim().equals(">")){
                                                    outputStream.println(etiqueta+"jg etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals(">=")){  
                                                    outputStream.println(etiqueta+"jge etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("<")){
                                                    outputStream.println(etiqueta+"jl etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("<=")){
                                                    outputStream.println(etiqueta+"jle etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("<>")){
                                                    outputStream.println(etiqueta+"jne etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("=")){
                                                    outputStream.println(etiqueta+"je etiq"+Integer.toString(nproc*100+bdestino)); 
                                                }   
                                        }else if(q.op().equals("GOTO")){
                                                // averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
                                                for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
                                                    String sucesor = (String)iter.next();
                                                    bbloque = variables.tbloques.get(Integer.valueOf(sucesor).intValue());
                                                    if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                                        bdestino = bbloque.nbloque;
                                                        break;
                                                    }
                                                }
                                                salvado_memoria = true; 
                                                for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                                    vble = (Var)iter.next(); 
                                                    if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                        !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && vble.es_local()){
                                                        ldir = new String(treg.get(vble.devolver_registro("")).nombre_reg_x86(vble.tipo()));
                                                        if(variables.get_proc().equals(nom_algoritmo)) { 
                                                            outputStream.println(etiqueta+"mov ["+vble.nombre()+"], "+ldir+";<--");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble.nombre()))+"], "+ldir+";<--");
                                                        }    
                                                        vble.borrar_asig();
                                                        if(primera_inst){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }
                                                    }
                                                }                               
                                                outputStream.println(etiqueta+"jmp etiq"+Integer.toString(nproc*100+bdestino));
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("PARAM")){
                                                if((q.res().charAt(0)>='0' && q.res().charAt(0)<='9')||(q.res().substring(0,1).equals("\""))) {
                                                    yp = q.res();
                                                    if(!(q.res().charAt(0)>='0' && q.res().charAt(0)<='9')) {
                                                        for(int k=0;k<constantes.size();k++)
                                                            if(constantes.get(k).equalsIgnoreCase(q.res())){
                                                                yp = "L"+Integer.toString(nproc*10+k+1);
                                                                break;    
                                                            }        
                                                    }             
                                                    outputStream.println(etiqueta+"push dword "+yp);
                                                }else if(variables.leer_tipo(q.res()).substring(0,4).equals("arra")){
                                                        factor = new String("1");
                                                        tipo_array = variables.leer_tipo(q.res());
                                                        if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("osoa"))
                                                            factor = "4";
                                                        else if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("karakterea"))
                                                            factor = "1";
                                                        // falta el factor para el tipo real
                                                        // obtener el numero de elementos del array
                                                        int num = Integer.parseInt(tipo_array.substring(tipo_array.indexOf(",")+1,tipo_array.indexOf(")")));
                                                        for(int k=1;k<num;k++){
                                                            if(variables.get_proc().equals(nom_algoritmo)) {
                                                                if(!factor.equals("1")){
                                                                    outputStream.println(etiqueta+"push dword ["+q.res()+" + "+factor+"*"+Integer.toString(k)+"]");
                                                                }else{ 
                                                                    outputStream.println(etiqueta+"push dword ["+q.res()+" + "+Integer.toString(k)+"]");
                                                                }
                                                            }else if(!variables.es_local_temp(q.res())) {
                                                                if(!factor.equals("1")){
                                                                    outputStream.println(etiqueta+"push dword [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.res()))+" - "+factor+"*"+Integer.toString(k)+"]");
                                                                }else{ 
                                                                    outputStream.println(etiqueta+"push dword [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.res()))+" - "+Integer.toString(k)+"]");
                                                                }                                                                
                                                            }else{    // es una vble local de un procedimiento
                                                                if(!factor.equals("1")){
                                                                    outputStream.println(etiqueta+"push dword [ebp - "+Integer.toString(correspondencia.get(q.res()))+" - "+factor+"*"+Integer.toString(k)+"]");
                                                                }else{ 
                                                                    outputStream.println(etiqueta+"push dword [ebp - "+Integer.toString(correspondencia.get(q.res()))+" - "+Integer.toString(k)+"]");
                                                                }                                                                
                                                            }
                                                            if(primera_inst){
                                                                primera_inst = false;
                                                                etiqueta = new String(tabulador);
                                                            }
                                                        }
                                                }else if(variables.esta_en_reg(q.res())){
                                                    if(variables.leer_tipo(q.res()).equalsIgnoreCase("karakterea")||variables.leer_tipo(q.res()).equalsIgnoreCase("boolearra"))
                                                        yp = treg.get(variables.obtener_pos_en_curso(q.res(),"")).nombre_reg_x86("osoa");
                                                    else    
                                                        yp = treg.get(variables.obtener_pos_en_curso(q.res(),"")).nombre_reg_x86(variables.leer_tipo(q.res()));
                                                    outputStream.println(etiqueta+"push "+yp+"; parametro "+q.res());
                                                }else if(variables.get_proc().equals(nom_algoritmo)) {    
                                                        outputStream.println(etiqueta+"push dword ["+q.res()+"]");
                                                      }else if(!variables.es_local_temp(q.res())) {
                                                        outputStream.println(etiqueta+"push dword [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.res()))+"]; parametro "+q.res());
                                                      }else{  
                                                        outputStream.println(etiqueta+"push dword [ebp - "+Integer.toString(correspondencia.get(q.res()))+"]; parametro "+q.res());
                                                      } 
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }

                                        }else if(q.op().equals("CALL")){
                                                salvado_memoria = true; 
                                                for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                                    vble = (Var)iter.next(); 
                                                    if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                        !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && vble.es_local()) {
                                                        ldir = new String(treg.get(vble.devolver_registro("")).nombre_reg_x86(vble.tipo()));
                                                        if(variables.get_proc().equals(nom_algoritmo)) { 
                                                            outputStream.println(etiqueta+"mov ["+vble.nombre()+"], "+ldir+";<--");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble.nombre()))+"], "+ldir+";<--");
                                                        }    
                                                        vble.borrar_asig();
                                                        if(primera_inst){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }
                                                    }
                                                }                               
                                                nelem = 0;
                                                // añadir el parametro para recibir el valor devuelto por el procedimiento, ahora puede haber mas de uno
                                                if(!Util.stringEmpty(q.arg2()))
                                                    if(q.arg2().indexOf(',')<0){
                                                        nelem = 1; 
                                                        if(variables.get_proc().equals(nom_algoritmo)) { 
                                                            outputStream.println(etiqueta+"push dword "+q.arg2()+"; empuja la direccion de "+q.arg2()+" en la pila");
                                                        }else{
                                                            outputStream.println(etiqueta+"lea eax, [ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]");
                                                            outputStream.println(etiqueta+"push dword eax; empuja la direccion de "+q.arg2()+" en la pila");
                                                        }                                                                                                                  
                                                        etiqueta = new String(tabulador);
                                                    }else{
                                                        nelem = Util.cuentaElementos(q.arg2());
                                                        for(int k=1;k<=nelem;k++){
                                                            if(variables.get_proc().equals(nom_algoritmo)) { 
                                                                outputStream.println(etiqueta+"push dword "+Util.getElemento(q.arg2(),k));
                                                            }else{
                                                                outputStream.println(etiqueta+"lea eax, [ebp - "+Integer.toString(correspondencia.get(Util.getElemento(q.arg2(),k)))+"]");
                                                                outputStream.println(etiqueta+"push dword eax; empuja la direccion de "+Util.getElemento(q.arg2(),k)+" en la pila");
                                                            } 
                                                            etiqueta = new String(tabulador);
                                                        } 
                                                    }           
                                                // sumar si fuera necesario los parametros de tipo array
                                                for(Iterator<Tvariables> iter_v2 = tproc2.descendingIterator();iter_v2.hasNext();){
                                                    var_aux = (Tvariables)iter_v2.next();
                                                    if(var_aux.get_proc().equalsIgnoreCase(q.res())) {
                                                        for(Iterator<Var> iter=var_aux.devuelve_vbles();iter.hasNext();){
                                                            vble = (Var)iter.next(); 
                                                            if(var_aux.es_parametro(vble.nombre())>0 && !Util.stringEmpty(vble.tipo()) && vble.tipo().substring(0,4).equals("arra")) {
                                                                nelem += Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))-2;
                                                            }    
                                                        }
                                                        break;
                                                    }
                                                }           
                                                outputStream.println(etiqueta+"call "+q.res().replace('-','_')); 
                                                outputStream.println(etiqueta+"add esp, "+String.valueOf(4*(Integer.parseInt(q.arg1())+nelem))+" ; quita los parametros de la pila");
                                        }else if(q.op().equals("RET")){                                                
                                                salvado_memoria = true; 
                                                if(variables.get_proc().equals(nom_algoritmo)){
                                                    outputStream.println(etiqueta+"popa");
                                                    etiqueta = new String(tabulador);
                                                    outputStream.println(etiqueta+"mov eax, 0 ; retornar a C");
                                                    outputStream.println(etiqueta+"leave");
                                                    outputStream.println(etiqueta+"ret");
                                                }
                                                else{
                                                    // valor de retorno de la funcion, ahora puede haber mas de uno
                                                    vbles_retorno = variables.get_dev(); 
                                                    for (Enumeration e = vbles_retorno.keys();e.hasMoreElements(); ){
                                                        nom_dev = (String)e.nextElement();
                                                        tipo_array = vbles_retorno.get(nom_dev);
                                                        if(tipo_array.substring(0,4).equals("arra")) {
                                                            factor = new String("1");
                                                            if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("osoa"))
                                                                factor = "4";
                                                            else if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("karakterea"))
                                                                factor = "1";
                                                            // falta el factor para el tipo real
                                                            // obtener el numero de elementos del array
                                                            int num = Integer.parseInt(tipo_array.substring(tipo_array.indexOf(",")+1,tipo_array.indexOf(")")));
                                                            outputStream.println(etiqueta+"mov ebx,[EBP + 8 + "+Integer.toString(variables.num_val_dev(nom_dev)*4)+"]");
                                                            etiqueta = new String(tabulador);
                                                            for(int k=1;k<num;k++)
                                                                if(!factor.equals("1")){
                                                                    outputStream.println(etiqueta+"mov eax,[ebp - "+Integer.toString(correspondencia.get(nom_dev))+" + "+factor+"*"+Integer.toString(k)+"]");
                                                                    outputStream.println(etiqueta+"mov [ebx + "+factor+"*"+Integer.toString(k)+"], eax");
                                                                }else{ 
                                                                    outputStream.println(etiqueta+"mov ax,[ebp - "+Integer.toString(correspondencia.get(nom_dev))+" + "+Integer.toString(k)+"]");                                                    
                                                                    outputStream.println(etiqueta+"mov [ebx + "+Integer.toString(k)+"], ax");
                                                                }
                                                        }else if(variables.esta_en_reg(nom_dev)){
                                                            yp = treg.get(variables.obtener_pos_en_curso(nom_dev,"")).nombre_reg_x86(tipo_array);
                                                            almacenar_reg(5,etiqueta);
                                                            zp = "esi";
                                                            outputStream.println(etiqueta+"mov "+zp+",[EBP + 8 + "+Integer.toString(variables.num_val_dev(nom_dev)*4)+"]");
                                                            etiqueta = new String(tabulador);
                                                            outputStream.println(etiqueta+"mov ["+zp+"],"+yp);
                                                        }else{
                                                            outputStream.println(etiqueta+"mov ebx,[EBP + 8 + "+Integer.toString(variables.num_val_dev(nom_dev)*4)+"]");
                                                            etiqueta = new String(tabulador);
                                                            yp = obtenreg(nom_dev,bloque.nbloque,etiqueta,2);
                                                            outputStream.println(etiqueta+"mov "+yp+",[ebp - "+Integer.toString(correspondencia.get(nom_dev))+"]");
                                                            outputStream.println(etiqueta+"mov [ebx],"+yp);
                                                        }                                                        
                                                    }
                                                    if(local_bytes>0)                          
                                                        outputStream.println(etiqueta+"leave");
                                                    else{
                                                        outputStream.println(etiqueta+"mov esp, ebp");
                                                        outputStream.println(etiqueta+"pop ebp");
                                                    }
                                                    outputStream.println(etiqueta+"ret ; retorna al llamador");
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
                                                if(variables.leer_tipo(q.arg1()).equalsIgnoreCase("osoa")){                                         
                                                    outputStream.println(etiqueta+"call read_int"); // guarda el entero tecleado en el registro r1
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }else if(variables.leer_tipo(q.arg1()).equalsIgnoreCase("karakterea")){                                         
                                                    outputStream.println(etiqueta+"call read_char");
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                    outputStream.println(etiqueta+"cmp eax, 0AH"); // ver si lo leido es el salto de linea del read_char anterior
                                                    indice++;
                                                    outputStream.println(etiqueta+"jne no_nl"+String.valueOf(indice));
                                                    outputStream.println(etiqueta+"call read_char");
                                                    etiqueta = new String("no_nl"+String.valueOf(indice)+":");
                                                    espacios = new String(tabulador,0,8 - etiqueta.length());
                                                    etiqueta += espacios;
                                                    primera_inst = true;
                                                } 
                                                Integer ind_reg; // quitar a q.arg1 de otros registros en los que pudiera estar
                                                for(Iterator<Integer> iter=variables.devuelve_regs(q.arg1());iter.hasNext();){
                                                        ind_reg = (Integer)iter.next();
                                                        treg.get(ind_reg.intValue()).contiene.remove(q.arg1());
                                                }        
                                                variables.borrar_todos_regs(q.arg1());
                                                variables.actualiza_descrip_direcc(q.arg1(),"r1",true);
                                        }else if(q.op().equals("WRITEC")){
                                                limpiar_etiq = false;
                                                if(primera_inst)
                                                    if(hay_volcado_r1())
                                                        limpiar_etiq = true;
                                                almacenar_reg_r1(q.arg1(),etiqueta);
                                                if(limpiar_etiq){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }  
                                                for(int k=0;k<mensajes.size();k++)
                                                    if(mensajes.get(k).equalsIgnoreCase(q.arg1())){
                                                        outputStream.println(etiqueta+"mov eax, prompt"+Integer.toString(nproc*10+k+1));
                                                        break;    
                                                    }        
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                                outputStream.println(etiqueta+"call print_string");// visualiza la cadena de texto
                                        }else if(q.op().equals("WRITE")){
                                                if(variables.esta_en_reg(q.arg1())){
                                                    yp = treg.get(variables.obtener_pos_en_curso(q.arg1(),"r1")).nombre_reg_x86(variables.leer_tipo(q.arg1()));
                                                    if(!yp.equalsIgnoreCase("eax") && !yp.equalsIgnoreCase("al")){
                                                        limpiar_etiq = false;
                                                        if(primera_inst)
                                                            if(hay_volcado_r1())
                                                                limpiar_etiq = true;
                                                        almacenar_reg_r1(q.arg1(),etiqueta);
                                                        if(limpiar_etiq){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }                                           
                                                        outputStream.println(etiqueta+"mov eax, "+yp);
                                                        variables.actualiza_descrip_direcc(q.arg1(),"r1",false);
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
                                                    if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
						       (q.arg1().charAt(0)=='-'))
                                                        outputStream.println(etiqueta+"mov eax, "+q.arg1());
                                                    else{
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                            outputStream.println(etiqueta+"mov eax, ["+q.arg1()+"]");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov eax, [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+"]");
                                                        }    
                                                        variables.actualiza_descrip_direcc(q.arg1(),"r1",false);
                                                    }   
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
						   (q.arg1().charAt(0)=='-')) 
                                                    outputStream.println(etiqueta+"call print_int");// visualiza el contenido del registro r1
                                                else if(variables.leer_tipo(q.arg1()).equalsIgnoreCase("osoa"))                                         
                                                    outputStream.println(etiqueta+"call print_int"); // guarda el entero tecleado en el registro r1
                                                else if(variables.leer_tipo(q.arg1()).equalsIgnoreCase("karakterea"))                                         
                                                    outputStream.println(etiqueta+"call print_char");
                                                else if((q.arg1().charAt(0)>='a' && q.arg1().charAt(0)<='z')||(q.arg1().charAt(0)>='A' && q.arg1().charAt(0)<='Z')||Util.stringEmpty(variables.leer_tipo(q.arg1())))
                                                    outputStream.println(etiqueta+"call print_char"); 
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("[]=")){
                                                if(variables.esta_en_reg(q.res())){
                                                    yp = treg.get(variables.obtener_pos_en_curso(q.res(),"")).nombre_reg_x86(variables.leer_tipo(q.res()));
                                                }else{
                                                    limpiar_etiq = false; 
                                                    if(primera_inst)
                                                        if(hay_volcado_registro())
                                                            limpiar_etiq = true;
                                                    yp = obtenreg(q.res(),bloque.nbloque,etiqueta,5);
                                                    if(limpiar_etiq){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    } 
                                                    if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.res())){                                           
                                                        outputStream.println(etiqueta+"mov "+yp+", ["+q.res()+"]");
                                                    }else{
                                                        outputStream.println(etiqueta+"mov "+yp+", [ebp - "+Integer.toString(correspondencia.get(q.res()))+"]");
                                                    }    
                                                    // se actualiza el descriptor de direcciones de q.res para indicar que esta en yp
                                                    if(!variables.es_temporal(q.res()))
                                                        variables.actualiza_descrip_direcc(q.res(),num_reg_x86(yp),false);
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }  
                                                // cargo el indice en un registro  
                                                if(variables.esta_en_reg(q.arg2()) && variables.obtener_pos_en_curso(q.arg2(),"r5")==5){
                                                    zp = treg.get(variables.obtener_pos_en_curso(q.arg2(),"r5")).nombre_reg_x86(variables.leer_tipo(q.arg2()));
                                                }else{
                                                    almacenar_reg(5,etiqueta);
                                                    zp = "esi";
                                                    if((q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg2()))){
                                                        outputStream.println(etiqueta+"mov "+zp+", "+q.arg2());
						                            }else{
                                                        if(variables.esta_en_reg(q.arg2())){
                                                            xp = treg.get(variables.obtener_pos_en_curso(q.arg2(),"")).nombre_reg_x86(variables.leer_tipo(q.arg2()));
                                                            outputStream.println(etiqueta+"mov "+zp+", "+xp);
                                                        }else
                                                            if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                                if(variables.get_proc().equals(nom_algoritmo))  
                                                                    outputStream.println(etiqueta+"mov "+zp+", ["+q.arg2()+"]");
                                                                else
                                                                    outputStream.println(etiqueta+"mov "+zp+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]");
                                                            }else{
                                                                outputStream.println(etiqueta+"mov "+zp+", [ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]");    
                                                            }    
                                                        treg.get(5).contiene.add(q.arg2());
                                                        variables.actualiza_descrip_direcc(q.arg2(),zp,false);
                                                    }   
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                } 
                                                factor = new String("1");
                                                tipo_array = variables.leer_tipo(q.arg1());
                                                System.out.println(q.arg1()+" tipo_array="+tipo_array);
                                                if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("osoa"))
                                                    factor = "4";
                                                else if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("karakterea"))
                                                    factor = "1";
                                                // falta el factor para el tipo real
                                                if(variables.get_proc().equals(nom_algoritmo)) { 
                                                    if(!factor.equals("1"))
                                                        outputStream.println(etiqueta+"mov ["+q.arg1()+" + "+factor+"*"+zp+"], "+yp);
                                                    else outputStream.println(etiqueta+"mov ["+q.arg1()+" + "+zp+"], "+yp);
                                                }else{
                                                    if(!factor.equals("1"))
                                                        outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+" + "+factor+"*"+zp+"], "+yp);
                                                    else outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+" + "+zp+"], "+yp);                                                    
                                                }    
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("=[]")){
                                                if(variables.esta_en_reg(q.res())){
                                                    yp = treg.get(variables.obtener_pos_en_curso(q.res(),"")).nombre_reg_x86(variables.leer_tipo(q.res()));
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
                                                    // se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
                                                    variables.actualiza_descrip_direcc(q.res(),num_reg_x86(yp),true);
                                                }    
                                                if(variables.esta_en_reg(q.arg2())){
                                                    zp = treg.get(variables.obtener_pos_en_curso(q.arg2(),"r5")).nombre_reg_x86(variables.leer_tipo(q.arg2()));
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
                                                        outputStream.println(etiqueta+"mov "+zp+", "+q.arg2());
                                                    else{
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                            if(variables.get_proc().equals(nom_algoritmo))  
                                                                outputStream.println(etiqueta+"mov "+zp+", ["+q.arg2()+"]");
                                                            else
                                                                outputStream.println(etiqueta+"mov "+zp+", [ebp + "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg2()))+"]");
                                                        }else{
                                                            outputStream.println(etiqueta+"mov "+zp+", [ebp - "+Integer.toString(correspondencia.get(q.arg2()))+"]");
                                                        }    
                                                        // se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
                                                        variables.actualiza_descrip_direcc(q.arg2(),num_reg_x86(zp),false);
                                                    }   
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }  
                                                factor = new String("1");
                                                tipo_array = variables.leer_tipo(q.arg1());
                                                System.out.println(q.arg1()+" tipo_array="+tipo_array);
                                                if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("osoa"))
                                                    factor = "4";
                                                else if(tipo_array.substring(6,tipo_array.indexOf(",")).equals("karakterea"))
                                                    factor = "1";
                                                // falta el factor para el tipo real
                                                if(variables.get_proc().equals(nom_algoritmo)|| !variables.es_local_temp(q.arg1())) {
                                                    if(variables.get_proc().equals(nom_algoritmo)) 
                                                        if(!factor.equals("1"))
                                                            outputStream.println(etiqueta+"mov "+yp+", ["+q.arg1()+" + "+factor+"*"+zp+"]");
                                                        else outputStream.println(etiqueta+"mov "+yp+", ["+q.arg1()+" + "+zp+"]");
                                                    else{
                                                        //outputStream.println(etiqueta+"mov "+yp+", [ebp + "+Integer.toString(8+4*variables.es_parametro(q.arg1()))+" - 4*"+zp+"]");
                                                        outputStream.println(etiqueta+"mov esi, "+Integer.toString(4+4*variables.num_val_dev()+4*variables.es_parametro(q.arg1())));
                                                        if(primera_inst){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }
                                                        outputStream.println(etiqueta+"shl "+zp+",2");
                                                        outputStream.println(etiqueta+"sub esi, "+zp);
                                                        outputStream.println(etiqueta+"add esi, ebp");
                                                        outputStream.println(etiqueta+"mov "+yp+", [esi]");
                                                    }    
                                                }else{
                                                    if(!factor.equals("1"))
                                                        outputStream.println(etiqueta+"mov "+yp+", [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+" + "+factor+"*"+zp+"]");
                                                    else outputStream.println(etiqueta+"mov "+yp+", [ebp - "+Integer.toString(correspondencia.get(q.arg1()))+" + "+zp+"]");                                                    
                                                }    
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                                if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.res())){ 
                                                    outputStream.println(etiqueta+"mov ["+q.res()+"], "+yp);
                                                }else{
                                                    outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(q.res()))+"], "+yp);
                                                } 
                                        }  // resto de instrucciones

                                        if(iter_bl.hasNext())
                                            ultimo_quad=(String)iter_bl.next();
                                        else
                                            ultimo_quad="";
                                    }   //while(!Util.stringEmpty(ultimo_quad)){                             
                                    // antes de abandonar el bloque hay que salvar las vbles destino de una asignacion
                                    if(!salvado_memoria)
                                        for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                            vble = (Var)iter.next(); 
                                            if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && vble.es_local() && vble.devolver_registro("")>0){
                                                ldir = new String(treg.get(vble.devolver_registro("")).nombre_reg_x86(vble.tipo()));
                                                if(variables.get_proc().equals(nom_algoritmo)) { 
                                                    outputStream.println(etiqueta+"mov ["+vble.nombre()+"], "+ldir+";<--");
                                                }else{
                                                    outputStream.println(etiqueta+"mov [ebp - "+Integer.toString(correspondencia.get(vble.nombre()))+"], "+ldir+";<--");
                                                }    
                                                vble.borrar_asig();
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                            }
                                        }                               
                                } // for(j=0;j<variables.tbloques.size();j++){ 
                            }   // for(Iterator<Tvariables> iter_v = tproc2.descendingIterator();iter_v.hasNext();){
                            outputStream.close();                                
                        }else if(opcion.equalsIgnoreCase("TXORI")){
                            outputStream = new PrintWriter(new FileWriter(nom_algoritmo.toLowerCase()+".asm"));
                            outputStream.println(".title "+nom_algoritmo.toLowerCase());
                            for(Iterator<Tvariables> iter_v = tproc2.descendingIterator();iter_v.hasNext();nproc++){
                                variables = (Tvariables)iter_v.next();
                                for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                    vble = (Var)iter.next(); 
                                    if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z')) && !Util.stringEmpty(vble.tipo()) && (vble.es_local()||vble.es_temporal())) {
                                        if(!lista_id.contains(vble.nombre())){
                                            lista_id.add(vble.nombre());
                                            if(vble.tipo().substring(0,4).equals("arra"))
                                                outputStream.println(vble.nombre()+": .word "+vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")"))+";");
                                            else{
                                                outputStream.println(vble.nombre()+": .word 1;");
                                                }
                                        }/*else{
                                            lista_id.add(vble.nombre()+"_"+variables.get_proc());
                                            if(vble.tipo().substring(0,4).equals("arra"))
                                                outputStream.println(vble.nombre()+"_"+variables.get_proc()+": .word "+vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")"))+";");
                                            else{
                                                outputStream.println(vble.nombre()+"_"+variables.get_proc()+": .word 1;");
                                                }
                                            boolean actualiza;
                                            aux_cuad = variables.get_codigo().split(",");
                                            ind_cuad = new String[aux_cuad.length+1];
                                            System.arraycopy(aux_cuad,0,ind_cuad,0,aux_cuad.length);
                                            // cambiar el nombre de las vbles en el programa en lenguaje intermedio de cuadruplos    
                                            for (int k = 0; k < ind_cuad.length; k++)
                                                if(!Util.stringEmpty(ind_cuad[k])){
                                                    actualiza = false;
                                                    q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[k]).intValue());
                                                    if(lista_id.contains(Util.getString(q.arg1(),"")) && lista_id.contains(Util.getString(q.arg1(),"")+"_"+variables.get_proc())){
                                                        q.set_arg1(q.arg1()+"_"+variables.get_proc());
                                                        actualiza = true;
                                                    }
                                                    if(lista_id.contains(Util.getString(q.arg2(),"")) && lista_id.contains(Util.getString(q.arg2(),"")+"_"+variables.get_proc())){
                                                        q.set_arg2(q.arg2()+"_"+variables.get_proc());
                                                        actualiza = true;                                                        
                                                    }
                                                    if(lista_id.contains(Util.getString(q.res(),"")) && lista_id.contains(Util.getString(q.res(),"")+"_"+variables.get_proc())){
                                                        q.set_res(q.res()+"_"+variables.get_proc());
                                                        actualiza = true;                                                        
                                                    }
                                                    if(actualiza)
                                                        tcuad.set(Integer.valueOf(ind_cuad[k]).intValue(),q);
                                                }    
                                        }*/    
                                    }        
                                }
                            }
			    outputStream.println(variables.get_comentario());
                            for(Iterator<Tvariables> iter_v = tproc2.descendingIterator();iter_v.hasNext();){
                                variables = (Tvariables)iter_v.next();
				local_bytes=0;
                                inicializar_registros();
                                if(variables.get_proc().equals(nom_algoritmo)){
                                    outputStream.println(".proc main");
                                }else{    
                                    outputStream.println(".proc "+variables.get_proc());
				    outputStream.println("push r30");
				    outputStream.println("mov r30, sp");
                                    i=1;
                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                        vble = (Var)iter.next(); 
                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                            !Util.stringEmpty(vble.tipo()) && (vble.es_local()||vble.es_temporal())){
                                            if(vble.tipo().substring(0,4).equals("arra")){
                                                if(vble.tipo().substring(6,vble.tipo().indexOf(",")).equals("osoa")){
                                                    local_bytes += (Integer.parseInt(vble.tipo().substring(vble.tipo().indexOf(",")+1,vble.tipo().indexOf(")")))+1);
                                                    correspondencia.put(vble.nombre(),new Integer(local_bytes));                                                    
                                                }
					    }else{
						if(vble.tipo().equals("osoa")){
						    local_bytes +=1;
						    correspondencia.put(vble.nombre(),new Integer(local_bytes));
						}
					    }
					}
				    }
				    outputStream.println("addi sp, sp, "+Integer.toString(local_bytes));
                                }   
                                char tabulador[]={' ',' ',' ',' ',' ',' ',' '};
                                String ldir,yp,zp,etiqueta,espacios;
                                boolean poner_etiqueta,saltar,primera_inst,limpiar_etiq,salvado_memoria;
                                for(j=0;j<variables.tbloques.size();j++){
                                    bloque = variables.tbloques.get(j);
				    inicializar_registros();
                                    salvado_memoria = false;
                                    // ver si a la primera instruccion hay que ponerle etiqueta
                                    poner_etiqueta = false;
                                    for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext() && !poner_etiqueta;){
                                        String nbpred=(String)iter.next();
                                        bbloque = variables.tbloques.get(Integer.valueOf(nbpred).intValue());
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
                                        etiqueta = "etiq"+String.valueOf(nproc*100+bloque.nbloque)+":";
                                        espacios = new String(tabulador,0,8 - etiqueta.length());
                                        etiqueta += espacios;
                                    }else
                                        etiqueta = new String(tabulador);
                                    primera_inst = true;
                                    ultimo_quad = String.valueOf(bloque.lider);
                                    iter_bl = bloque.cuadruplos.iterator();
                                    while(!Util.stringEmpty(ultimo_quad)){
                                        q = (Cuad)tcuad.get(Integer.valueOf(ultimo_quad).intValue());
                                        if(q.op().equals("+") || q.op().equals("-") || q.op().equals("*") || q.op().equals("/")){
                                            saltar = false;ldir = new String();zp=new String();
                                            // invocar obtenreg para determinar donde se guarda el resultado
                                            // falta ver si alguno de los operandos es una cte.
                                            if(variables.esta_en_reg(q.arg1())){
                                                yp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg1(),"")));
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
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
						   (q.arg1().charAt(0)=='-'))
                                                    outputStream.println(etiqueta+"movi "+yp+", #"+q.arg1());
                                                else{
                                                    if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                        if(variables.get_proc().equals(nom_algoritmo))  
                                                            outputStream.println(etiqueta+"ld "+yp+", "+q.arg1());
                                                        else
                                                            outputStream.println(etiqueta+"ldd "+yp+", -"+Integer.toString(1+variables.es_parametro(q.arg1()))+"[r30]");
                                                    }else{
                                                        outputStream.println(etiqueta+"ldd "+yp+", "+Integer.toString(correspondencia.get(q.arg1()))+"[r30]");
                                                    }   
                                                    variables.actualiza_descrip_direcc(q.arg1(),yp,false);
                                                }
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }                                           
                                            }    
                                            if(variables.esta_en_reg(q.arg2())){
                                                zp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg2(),"")));
                                            }else{
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
                                                        outputStream.println(etiqueta+"addi "+ldir+", "+yp+", #"+q.arg2());
                                                    else if(q.op().equals("-"))
                                                        outputStream.println(etiqueta+"subi "+ldir+", "+yp+", #"+q.arg2());
                                                    else if(q.op().equals("*"))
                                                        outputStream.println(etiqueta+"muli "+ldir+", "+yp+", #"+q.arg2());
                                                    else if(q.op().equals("/"))
                                                        outputStream.println(etiqueta+"divi "+ldir+", "+yp+", #"+q.arg2());
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
                                                    if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg2())){ 
                                                        if(variables.get_proc().equals(nom_algoritmo))  
                                                            outputStream.println(etiqueta+"ld "+zp+", "+q.arg2());
                                                        else
                                                            outputStream.println(etiqueta+"ldd "+zp+", -"+Integer.toString(1+variables.es_parametro(q.arg2()))+"[r30]");
                                                    }else{
                                                        outputStream.println(etiqueta+"ldd "+zp+", "+Integer.toString(correspondencia.get(q.arg2()))+"[r30]");
                                                    }   
                                                    variables.actualiza_descrip_direcc(q.arg2(),zp,false);
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
                                            variables.actualiza_descrip_direcc(q.res(),ldir,true);
                                        }else if(q.op().equals(":=")){
                                            nelem = Util.cuentaElementos(q.res());
                                            if(q.res().indexOf(',')<0){
                                                // es una asignacion normal
                                                yp = new String();
                                                if(variables.esta_en_reg(q.res())){
                                                    ldir = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.res(),"")));
                                                    variables.marcar_asignacion(q.res());
                                                    etiqueta=quitar_vbles_reg(q.res(),ldir,etiqueta); // si en el registro hubiera otras variables distintas a q.res(), hay que salvar a memoria esas variables y quitarlas de los descriptores
                                                }else{
                                                    limpiar_etiq = false; 
                                                    if(primera_inst)
                                                        if(hay_volcado_registro())
                                                            limpiar_etiq = true;
                                                    ldir = obtenreg(q.res(),bloque.nbloque,etiqueta);
                                                    if(limpiar_etiq){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }                                           
                                                    variables.actualiza_descrip_direcc(q.res(),ldir,true);
                                                }
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
						   (q.arg1().charAt(0)=='-'))
                                                    outputStream.println(etiqueta+"movi "+ldir+", #"+q.arg1());
                                                else
                                                    if(!variables.esta_en_reg(q.arg1())){
                                                        if(variables.get_proc().equals(nom_algoritmo) || !variables.es_local_temp(q.arg1())){ 
                                                            if(variables.get_proc().equals(nom_algoritmo))  
                                                                outputStream.println(etiqueta+"ld "+ldir+", "+q.arg1());
                                                            else
                                                                outputStream.println(etiqueta+"ldd "+ldir+", -"+Integer.toString(1+variables.es_parametro(q.arg1()))+"[r30]");
                                                        }else{
                                                            outputStream.println(etiqueta+"ldd "+ldir+", "+Integer.toString(correspondencia.get(q.arg1()))+"[r30]");
                                                        }  
                                                        variables.actualiza_descrip_direcc(q.arg1(),ldir,false);
                                                    }else{
                                                        yp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg1(),"")));
                                                        outputStream.println(etiqueta+"mov "+ldir+", "+yp);
                                                    }
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                            }else{
                                                // es una asignacion multiple
                                                for(int k=1;k<=nelem;k++){
                                                    lizdo = Util.getElemento(q.res(),k);
                                                    yp = new String();
                                                    if(variables.esta_en_reg(lizdo)){
                                                        ldir = new String("r"+Integer.toString(variables.obtener_pos_en_curso(lizdo,"")));
                                                        variables.marcar_asignacion(lizdo);
                                                        etiqueta=quitar_vbles_reg(lizdo,ldir,etiqueta); // si en el registro hubiera otras variables distintas a q.res()[k], hay que salvar a memoria esas variables y quitarlas de los descriptores
                                                    }else{
                                                        limpiar_etiq = false; 
                                                        if(primera_inst)
                                                            if(hay_volcado_registro())
                                                                limpiar_etiq = true;
                                                        ldir = obtenreg(lizdo,bloque.nbloque,etiqueta,5);
                                                        if(limpiar_etiq){
                                                            primera_inst = false;
                                                            etiqueta = new String(tabulador);
                                                        }                                           
                                                        variables.actualiza_descrip_direcc(lizdo,num_reg_x86(ldir),true);
                                                    }
						    outputStream.println(etiqueta+"pop r1");
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                    outputStream.println(etiqueta+"mov "+ldir+", r1");
                                                }    
                                            }
                                        }else if(q.op().substring(0,2).equals("IF")){
                                                if(variables.esta_en_reg(q.arg1())){
                                                    yp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg1(),"")));
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
                                                    if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
						       (q.arg1().charAt(0)=='-'))
                                                        outputStream.println(etiqueta+"movi "+yp+", #"+q.arg1());
                                                    else{
                                                        outputStream.println(etiqueta+"ld "+yp+", "+q.arg1());
                                                        variables.actualiza_descrip_direcc(q.arg1(),yp,false);
                                                    }
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }    
                                                if(variables.esta_en_reg(q.arg2())){
                                                    zp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg2(),"")));
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
                                                        variables.actualiza_descrip_direcc(q.arg2(),zp,false);
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
                                                    bbloque = variables.tbloques.get(Integer.valueOf(sucesor).intValue());
                                                    if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                                        bdestino = bbloque.nbloque;
                                                        break;
                                                    }
                                                }
                                                salvado_memoria = true;     
                                                for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                                    vble = (Var)iter.next(); 
                                                    if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                        !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && vble.es_local()) {
                                                        outputStream.println(etiqueta+"st r"+Integer.toString(vble.devolver_registro(""))+", "+vble.nombre()+" {gorde}");  
                                                        vble.borrar_asig();
                                                    }
                                                }          
                                                if(q.op().substring(2).trim().equals(">")){
                                                    outputStream.println(etiqueta+"bgt "+ldir+", etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals(">=")){  
                                                    outputStream.println(etiqueta+"bge "+ldir+", etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("<")){
                                                    outputStream.println(etiqueta+"bls "+ldir+", etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("<=")){
                                                    outputStream.println(etiqueta+"ble "+ldir+", etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("<>")){
                                                    outputStream.println(etiqueta+"bne "+ldir+", etiq"+Integer.toString(nproc*100+bdestino));
                                                }else if(q.op().substring(2).trim().equals("=")){
                                                    outputStream.println(etiqueta+"beq "+ldir+", etiq"+Integer.toString(nproc*100+bdestino)); 
                                                }   
                                        }else if(q.op().equals("GOTO")){
                                                // averiguar el bloque al que pertenece la instr. destino del salto, mirando entre los bloques sucesores
                                                for(Iterator<String> iter=bloque.succ.iterator();iter.hasNext();){
                                                    String sucesor = (String)iter.next();
                                                    bbloque = variables.tbloques.get(Integer.valueOf(sucesor).intValue());
                                                    if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                                        bdestino = bbloque.nbloque;
                                                        break;
                                                    }
                                                }
                                                salvado_memoria = true; 
                                                for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                                    vble = (Var)iter.next(); 
                                                    if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                        !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && !vble.es_temporal()){
                                                        ldir = new String("r"+Integer.toString(vble.devolver_registro("")));
                                                        outputStream.println(etiqueta+"st "+ldir+", "+vble.nombre()+" {gorde}");
                                                        vble.borrar_asig();
                                                    }
                                                }                               

                                                outputStream.println(etiqueta+"jmp etiq"+Integer.toString(nproc*100+bdestino));
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("=[]")){
                                                if(variables.esta_en_reg(q.res())){
                                                    yp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.res(),"")));
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
                                                    // se actualiza el descriptor de direcciones de q.res para indicar que esta en ldir
                                                    variables.actualiza_descrip_direcc(q.res(),yp,true);
                                                }    
                                                if(variables.esta_en_reg(q.arg2())){
                                                    zp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg2(),"")));
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
                                                        variables.actualiza_descrip_direcc(q.arg2(),zp,false);
                                                    }   
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }  
                                                outputStream.println(etiqueta+"ldx "+yp+", "+q.arg1()+"["+zp+"]");
                                                outputStream.println(etiqueta+"st "+yp+", "+q.res());
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("[]=")){
                                                if(variables.esta_en_reg(q.res())){
                                                    yp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.res(),"")));
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
                                                    variables.actualiza_descrip_direcc(q.res(),yp,false);
                                                    if(primera_inst){
                                                        primera_inst = false;
                                                        etiqueta = new String(tabulador);
                                                    }
                                                }    
                                                if(variables.esta_en_reg(q.arg2())){
                                                    zp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg2(),"")));
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
                                                         variables.actualiza_descrip_direcc(q.arg2(),zp,false);
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
                                        }else if(q.op().equals("WRITEC")){
                                                outputStream.println(etiqueta+"outs "+q.arg1());// visualiza la cadena de texto
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("WRITE")){
                                                if(variables.esta_en_reg(q.arg1())){
                                                    yp = new String("r"+Integer.toString(variables.obtener_pos_en_curso(q.arg1(),"r1")));
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
                                                        variables.actualiza_descrip_direcc(q.arg1(),"r1",false);
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
                                                    if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||Util.stringEmpty(variables.leer_tipo(q.arg1()))||
						       (q.arg1().charAt(0)=='-'))
                                                        outputStream.println(etiqueta+"movi r1, #"+q.arg1());
                                                    else{
                                                        outputStream.println(etiqueta+"ld r1, "+q.arg1());
                                                        variables.actualiza_descrip_direcc(q.arg1(),"r1",false);
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
                                                variables.actualiza_descrip_direcc(q.arg1(),"r1",true);
                                                if(primera_inst){
                                                    primera_inst = false;
                                                    etiqueta = new String(tabulador);
                                                }
                                        }else if(q.op().equals("RET")){
                                                
                                                if(variables.get_proc().equals(nom_algoritmo)){
                                                    outputStream.println(etiqueta+"retm");
                                                    outputStream.println(".endp main");
                                                }
                                                else{
                                                    salvado_memoria = true; 
                                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                                        vble = (Var)iter.next(); 
                                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                            !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && !vble.es_temporal() && vble.devolver_registro("")>0){
                                                            ldir = new String("r"+Integer.toString(vble.devolver_registro("")));
                                                            outputStream.println(etiqueta+"st "+ldir+", "+vble.nombre()+";<--");
                                                            vble.borrar_asig();
                                                        }
                                                    }                               
                                                    outputStream.println(etiqueta+"ret");
                                                    outputStream.println(".endp "+variables.get_proc());
                                                }
                                              }  
                                        if(iter_bl.hasNext())
                                            ultimo_quad=(String)iter_bl.next();
                                        else
                                            ultimo_quad="";
                                    }                                
                                    // antes de abandonar el bloque hay que salvar las vbles destino de una asignacion
                                    if(!salvado_memoria)
                                        for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                            vble = (Var)iter.next(); 
                                            if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                                !Util.stringEmpty(vble.tipo()) && vble.asig_marcada() && !vble.es_temporal() && vble.devolver_registro("")>0){
                                                ldir = new String("r"+Integer.toString(vble.devolver_registro("")));
                                                outputStream.println(etiqueta+"st "+ldir+", "+vble.nombre()+" {gorde}");
                                                vble.borrar_asig();
                                            }
                                        }                               
                                }  
                            }                                   
                            outputStream.println(".end");
                            outputStream.close();
                        }else if(opcion.equalsIgnoreCase("UDMPs99")){
                            outputStream = new PrintWriter(new FileWriter(nom_algoritmo.toLowerCase()+".mpv"));
                            // declaracion de las variables globales
                            for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                vble = (Var)iter.next(); 
                                if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                    !Util.stringEmpty(vble.tipo()) && vble.es_local())
                                    if(vble.tipo().equals("osoa"))
                                        outputStream.println("globali "+vble.nombre());
                                    else if(vble.tipo().equals("erreala"))
                                            outputStream.println("globalr "+vble.nombre());
                                         else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter())
                                             outputStream.println("globalb "+vble.nombre());
                            }
                            // empieza el programa principal de la maquina de pila
                            for(Iterator<Tvariables> iter_v = tproc2.iterator();iter_v.hasNext();){
                                variables = (Tvariables)iter_v.next();
                                if(variables.get_proc().equals(nom_algoritmo)){
                                    outputStream.println("etiqv main");
                                    outputStream.println("ponerbase");
                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                        vble = (Var)iter.next(); 
                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                            !Util.stringEmpty(vble.tipo()) && vble.es_temporal())
                                            if(vble.tipo().equals("osoa"))
                                                outputStream.println("locali "+vble.nombre());
                                            else if(vble.tipo().equals("erreala"))
                                                    outputStream.println("localr "+vble.nombre());
                                                 else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter())
                                                     outputStream.println("localb "+vble.nombre());
                                    }                                    
                                }else{    
                                    outputStream.println("etiqv "+variables.get_proc());
                                    outputStream.println("ponerbase");
                                    for(Iterator<Var> iter=variables.devuelve_vbles();iter.hasNext();){
                                        vble = (Var)iter.next(); 
                                        if(((vble.nombre().charAt(0)>='a' && vble.nombre().charAt(0)<='z')||(vble.nombre().charAt(0)>='A' && vble.nombre().charAt(0)<='Z'))&& 
                                            !Util.stringEmpty(vble.tipo()) && vble.es_local())
                                            if(vble.tipo().equals("osoa"))
                                                outputStream.println("locali "+vble.nombre());
                                            else if(vble.tipo().equals("erreala"))
                                                    outputStream.println("localr "+vble.nombre());
                                                 else if(vble.nombre().length()>1 || vble.nombre().charAt(0)!=vble.devuelve_caracter())
                                                     outputStream.println("localb "+vble.nombre());
                                    }                                    
                                }
                                String ldir,yp,zp,etiqueta;
                                boolean poner_etiqueta;
                                for(j=0;j<variables.tbloques.size();j++){
                                    bloque = variables.tbloques.get(j);
                                    // ver si a la primera instruccion hay que ponerle etiqueta
                                    poner_etiqueta = false;
                                    for(Iterator<String> iter=bloque.pred.iterator();iter.hasNext() && !poner_etiqueta;){
                                        String nbpred=(String)iter.next();
                                        bbloque = variables.tbloques.get(Integer.valueOf(nbpred).intValue());
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
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||(q.arg1().charAt(0)=='-'))
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
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')|| q.arg1().charAt(0)=='.'|| q.arg1().charAt(0)=='-')
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
                                            if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")<0) || variables.leer_tipo(q.arg1()).equals("osoa")||(q.arg1().charAt(0)=='-')){
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||(q.arg1().charAt(0)=='-'))
                                                    outputStream.println("insi "+q.arg1());
                                                else 
                                                    outputStream.println("valord "+q.arg1());
                                                outputStream.println("asigna");
                                            }else if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")>=0)||variables.leer_tipo(q.arg1()).equals("erreala")||(q.arg1().charAt(0)=='-')){
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')|| q.arg1().charAt(0)=='.'||(q.arg1().charAt(0)=='-'))
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
                                            if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")<0) || variables.leer_tipo(q.arg1()).equals("osoa")||(q.arg1().charAt(0)=='-'))
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||(q.arg1().charAt(0)=='-'))
                                                    outputStream.println("insi "+q.arg1());
                                                else 
                                                    outputStream.println("valord "+q.arg1());
                                            else if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9' && q.arg1().indexOf(".")>=0) || variables.leer_tipo(q.arg1()).equals("erreala")||(q.arg1().charAt(0)=='-'))
                                                if((q.arg1().charAt(0)>='0' && q.arg1().charAt(0)<='9')||q.arg1().charAt(0)=='.'||(q.arg1().charAt(0)=='-'))
                                                    outputStream.println("insr "+q.arg1());
                                                else 
                                                    outputStream.println("valord "+q.arg1());
                                            else{
                                                if(q.arg1().length()==1 && q.arg1().equals(variables.leer_valor(q.arg1())))
                                                    outputStream.println("insb "+q.arg1());
                                                else 
                                                    outputStream.println("valord "+q.arg1());                                           
                                            }
                                            if(variables.leer_tipo(q.arg2()).equals("osoa"))
                                                if(q.arg2().charAt(0)>='0' && q.arg2().charAt(0)<='9')
                                                    outputStream.println("insi "+q.arg2());
                                                else 
                                                    outputStream.println("valord "+q.arg2());
                                            else if(variables.leer_tipo(q.arg2()).equals("erreala"))
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
                                                bbloque = variables.tbloques.get(Integer.valueOf(sucesor).intValue());
                                                if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                                    bdestino = bbloque.nbloque;
                                                    break;
                                                }
                                            }   
                                            if(variables.leer_tipo(q.arg1()).equals("osoa"))
                                                if(q.op().substring(2).trim().equals("="))
                                                    outputStream.println("==i");
                                                else if(q.op().substring(2).trim().equals("<>"))
                                                    outputStream.println("!=i");
                                                else
                                                    outputStream.println(q.op().substring(2).trim()+"i");
                                            else if(variables.leer_tipo(q.arg1()).equals("erreala"))
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
                                                bbloque = variables.tbloques.get(Integer.valueOf(sucesor).intValue());
                                                if (bbloque.lider==Integer.valueOf(q.res()).intValue()){
                                                    bdestino = bbloque.nbloque;
                                                    break;
                                                }
                                            }   
                                            outputStream.println("ir–a #B"+Integer.toString(bdestino));
                                        }else if(q.op().equals("CALL")){
                                            outputStream.println("llamar "+q.res());
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
                                            outputStream.println("cogerbase");
                                            outputStream.println("ret");
                                            if(variables.get_proc().equals(nom_algoritmo))
                                                outputStream.println("fin main");
                                            else
                                                outputStream.println("fin "+variables.get_proc());
                                        }else if(q.op().equals("WRITEC")){
                                                outputStream.println("escribirs \""+q.arg1()+"\"");
                                        }        
                                        if(iter_bl.hasNext())
                                            ultimo_quad=(String)iter_bl.next();
                                        else
                                            ultimo_quad="";
                                    }                               
                                }  
                            }   
                            outputStream.println("inicio");
                            outputStream.println("llamar main");
                            outputStream.println("fin");                        
                            outputStream.close();
                        }   
                        break;
                case 6: pila_sem.push(new Reg("karakterea"));
                        break;                      
                case 7: pila_sem.push(new Reg("osoa"));
                        break;      
                case 8: pila_sem.push(new Reg("boolearra"));
                        break;      
                case 9: arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();
                        pila_sem.push(new Reg("array("+arg2.nombre()+","+arg1.nombre()+")"));
                        break;
                case 13:arg1 = pila_sem.pop();
                        asocia(arg1.cierto(),String.valueOf(tcuad.size()));
                        asocia(arg1.falso(),String.valueOf(tcuad.size()));
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
                case 16:arg2 = pila_sem.pop();
                        arg1 = pila_sem.pop();
                        cod = gen(":=", arg2.nombre(), null, arg1.nombre());
                        if(arg2.codigo()!=null) cod = arg2.codigo()+","+cod;
                        if(arg1.codigo()!=null) cod = cod+","+arg1.codigo();
                        pila_sem.push(new Reg(null,null,cod,null,null,null));
                        break;
                case 18:arg1 = pila_sem.pop();
                        cod = gen("INPUT", arg1.nombre(), null, null);
                        pila_sem.push(new Reg(arg1.nombre(),String.valueOf(variables.insertar(arg1.nombre())),cod,null,null,null));
                        break;
                case 19:arg1 = pila_sem.pop();
                        if(!Util.stringEmpty(arg1.lugar()))
                            cod = gen("WRITE", variables.leer(Integer.valueOf(arg1.lugar()).intValue()), null, null);
                        else
                            cod = gen("WRITEC", arg1.nombre(), null, null);
                        pila_sem.push(new Reg(arg1.nombre(),arg1.lugar(),arg1.codigo()+","+cod,null,null,null));
                        break;  
                case 21:op=pila_sem.pop();
                        arg1=pila_sem.pop();
                        asocia(arg1.cierto(),op.quad());
                        asocia(arg1.falso(),op.quad());
                        // hacer relleno de retroceso
                        aux_cuad = arg1.codigo().split(",");
                        ind_cuad = new String[aux_cuad.length+1];
                        System.arraycopy(aux_cuad,0,ind_cuad,0,aux_cuad.length);
                        for (int k = 0; k < ind_cuad.length; k++)
                            if(!Util.stringEmpty(ind_cuad[k])){
                                q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[k]).intValue());
                                if((q.op().equals("GOTO") || (!Util.stringEmpty(q.op()) && q.op().length()>=2 && q.op().substring(0,2).equals("IF"))) && Util.stringEmpty(q.res())){
                                    q.set_res(op.quad());
                                    tcuad.set(Integer.valueOf(ind_cuad[k]).intValue(),q);
                                }   
                            }
                        op=pila_sem.pop();
                        pila_sem.push(new Reg(null,null,arg1.codigo(),null,null,null));
                        break;
                case 24:ini = pila_sem.pop(); 
                        arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        cod = arg1.codigo()+","+arg2.codigo();
                        asocia(arg1.cierto(),primera_pos(arg2.codigo()));
                        cierto = arg2.cierto();                        
                        if(!Util.stringEmpty(ini.quad())){
                            asocia(arg1.falso(),primera_pos(ini.codigo()));
                            falso = arg2.falso()+","+String.valueOf(tcuad.size());
                            cod += ","+gen("GOTO",null,null,null);
                            cod += ","+ini.codigo();
                            cierto += ","+ ini.cierto();
                            falso += ini.falso();
                        }else falso = arg1.falso()+","+arg2.falso();                         
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;
                case 25:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        asocia(arg1.cierto(),op.quad());
                        asocia(arg1.falso(),op.quad());
                        cod = arg1.codigo();
                        while(!pila_sem.empty() && Util.stringEmpty(pila_sem.peek().quad())){
                            arg1 = pila_sem.pop();
                            asocia(arg1.cierto(),primera_pos(cod));
                            asocia(arg1.falso(),primera_pos(cod));
                            cod = arg1.codigo()+","+cod;
                        }
                        if(!pila_sem.empty() && !Util.stringEmpty(pila_sem.peek().quad())){
                            ini = pila_sem.pop();
                        }
                        asocia(arg2.falso(),primera_pos(cod));
                        cod = cod+","+arg2.codigo();   
                        cierto = arg2.cierto();
                        pila_sem.push(new Reg(null,null,cod,null,cierto,null));
                        break;
                case 26:arg2 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        ini = pila_sem.pop();   
                        asocia(arg1.cierto(),primera_pos(arg2.codigo()));
                        asocia(arg2.cierto(),primera_pos(arg1.codigo()));
                        asocia(arg2.falso(),primera_pos(arg1.codigo()));
                        cod = arg1.codigo()+","+arg2.codigo()+","+gen("GOTO",null,null,primera_pos(arg1.codigo()));
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
                        if(Util.stringEmpty(variables.leer_tipo(arg1.nombre())))
                            tipotemp = variables.leer_tipo(arg2.nombre());
                        else
                            tipotemp = variables.leer_tipo(arg1.nombre());
                        pila_sem.push(new Reg(t,String.valueOf(variables.insertar_temp(t,tipotemp)),arg1.codigo()+","+arg2.codigo()+","+cod,null,null,null));
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
                case 55:if(ps.equalsIgnoreCase("egiazkoa"))
                            pila_sem.push(new Reg("1"));
                        else
                            pila_sem.push(new Reg("0"));    
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
                case 58:// ahora puede venir una lista de parametros, antes se esperaba solo un indice para el arreglo, pongo una marca saber hasta donde recuperar
                        // los parametros reales
                        cod = ""; nparam=0;
                        parametros = new ArrayList<String>();
                        do{ // leer el resto de parametros si los hay
                            arg2 = pila_sem.pop();
                            nparam++;
                            parametros.add(arg2.nombre());
                            if(arg2.codigo()!=null && !arg2.codigo().equals(""))
                                if(Util.stringEmpty(cod)) 
                                    cod = arg2.codigo();
                                else
                                    cod = arg2.codigo()+","+cod;
                        }while(!pila_sem.empty() && (Util.stringEmpty(pila_sem.peek().quad()) || !pila_sem.peek().quad().equals("#")));    
                        arg1 = pila_sem.pop();
                        // ver si arg1.nombre es un procedimiento o un arreglo
                        if(!Util.stringEmpty(variables.leer_tipo(arg1.nombre())) && variables.leer_tipo(arg1.nombre()).substring(0,4).equalsIgnoreCase("arra") ) {
                            t = nuevotemp();
                            cod = gen("=[]",arg1.nombre(),arg2.nombre(),t);
                            if(arg2.codigo()!=null && !arg2.codigo().equals("")) cod = arg2.codigo()+","+cod;
                            if(!Util.stringEmpty(variables.leer_tipo(arg1.nombre())))
                                tipotemp=variables.leer_tipo(arg1.nombre()).substring(6,variables.leer_tipo(arg1.nombre()).indexOf(","));
                            else tipotemp="osoa";
                            pila_sem.push(new Reg(t,String.valueOf(variables.insertar_temp(t,tipotemp)),cod,String.valueOf(tcuad.size()),null,null));
                        }else{
                            for(int k=parametros.size()-1;k>=0;k--)
                                if(Util.stringEmpty(cod))
                                    cod = gen("PARAM", null, null, parametros.get(k));
                                else
                                    cod = cod+","+gen("PARAM", null, null, parametros.get(k));
                            sb=new StringBuffer();  nelem=1;  
                            // poner el tipo que devuelve la funcion a la vble temporal que recoge el valor en la llamada a dicha funcion
                            // para ello recorrer el codigo de los procedimientos previos buscando llamadas a la funcion                        
                            for(Iterator<Tvariables> iter_v = tproc.descendingIterator();iter_v.hasNext();){
                                var_aux = (Tvariables)iter_v.next();
                                if(var_aux.get_proc().equalsIgnoreCase(arg1.nombre())) {
                                    vbles_retorno = var_aux.get_dev();
                                    for (Enumeration e = vbles_retorno.keys(); e.hasMoreElements();nelem++) {
                                        nom_dev = (String)e.nextElement();
                                        tipotemp = vbles_retorno.get(nom_dev);
                                        t=nuevotemp();
                                        lugar = variables.insertar_temp(t,tipotemp);
                                        if(nelem==1) sb.append(t);
                                        else sb.append(','+t);// las vbles temporales se van a guardar separadas por comas en el mismo orden que las vbles de retorno
                                    }
                                }    
                            }                                 
                            if(Util.stringEmpty(sb.toString()) && variables.get_proc().equalsIgnoreCase(arg1.nombre())) { // no encuentra el nombre del procedimiento, puede sea una llamada recursiva, por si es el caso se recogen sus vbles. de retorno
                                vbles_retorno=variables.get_dev();
                                for (Enumeration e = vbles_retorno.keys(); e.hasMoreElements();nelem++) {
                                    nom_dev = (String)e.nextElement();
                                    tipotemp = vbles_retorno.get(nom_dev);
                                    t=nuevotemp();
                                    lugar = variables.insertar_temp(t,tipotemp);
                                    if(nelem==1) sb.append(t);
                                    else sb.append(','+t);// las vbles temporales se van a guardar separadas por comas en el mismo orden que las vbles de retorno
                                }
                            }    
                            if(Util.stringEmpty(sb.toString())) sb.append(arg1.nombre());
                            cod += ","+gen("CALL", String.valueOf(nparam), sb.toString(), arg1.nombre());
                            pila_sem.push(new Reg(sb.toString(),null,cod,null,null,null));
                        }
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
                        while(!pila_sem.empty() && Util.stringEmpty(arg1.quad())){
                            variables.insertar_prin(arg1.nombre(),arg2.nombre());
                            arg1 = pila_sem.pop();
                        }
                        variables.insertar_prin(arg1.nombre(),arg2.nombre());
                        // se vuelve a insertar en la pila, lo que sería el nombre del algoritmo/procedimiento
                        //pila_sem.push(arg1);
                        break;
                case 66:arg2 = pila_sem.pop(); // cada una de las condiciones de aukera se trata como si fuera una instruccion de baldin expr orduan
                        op = pila_sem.pop();
                        arg1 = pila_sem.pop();  
                        cod = arg1.codigo()+","+arg2.codigo();
                        asocia(arg1.cierto(),op.quad());
                        cierto = arg2.cierto()+","+arg2.falso();
                        falso = arg1.falso();
                        cod += ","+gen("GOTO",null,null,null);
                        if(!pila_sem.empty() && Util.stringEmpty(pila_sem.peek().quad())){
                            arg1 = pila_sem.pop();
                            asocia(arg1.falso(),primera_pos(cod));
                            cod = arg1.codigo()+","+cod;
                            cierto = arg1.cierto()+","+cierto;
                        }
                        pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        break;
                case 67:if(opcion.equalsIgnoreCase("x86")){
                            pila_sem.push(new Reg(ps));
                        }else{
                            int ascci = (int)ps.charAt(0);
                            pila_sem.push(new Reg(String.valueOf(ascci)));
                        }    
                        break; 
                case 68:pila_sem.push(new Reg(ps));
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
                case 71:arg1 = pila_sem.pop();  
                        if(Util.stringEmpty(arg1.nombre()))
                            pila_sem.push(arg1);
                        else{
                            cierto = String.valueOf(tcuad.size());
                            cod = arg1.codigo();
                            cod += ","+gen("IF<>",arg1.nombre(),"0",null);
                            falso = String.valueOf(tcuad.size());
                            cod += ","+gen("GOTO",null,null,null);
                            pila_sem.push(new Reg(null,null,cod,null,cierto,falso));
                        }
                        break;
                case 72:arg1 = pila_sem.pop();
                        op = pila_sem.pop();
                        arg1.set_quad(op.quad());
                        pila_sem.push(arg1);
                        break;       
                case 73:pila_sem.push(new Reg(null,null,null,null,null,null));
                        break;
                case 76:pila_sem.push(new Reg("karakterea"));
                        break;
                case 77:vbles_retorno=variables.marcar_como_dev();
                        // poner el tipo que devuelve la funcion a la vble temporal que recoge el valor en la llamada a dicha funcion
                        // para ello recorrer el codigo de los procedimientos previos buscando llamadas a la funcion                        
                        nom_sub = pila_sem.peek().nombre();
                        if(nom_sub.equalsIgnoreCase("LEHENA")){
                            System.out.println("Busco las llamadas");
                        }
                        for(Iterator<Tvariables> iter_v = tproc.descendingIterator();iter_v.hasNext();){
                            var_aux = (Tvariables)iter_v.next();
                            if(nom_sub.equalsIgnoreCase("LEHENA")){
                                System.out.println("en :"+var_aux.get_proc());
                            }        
                            aux_cuad = var_aux.get_codigo().split(",");
                            ind_cuad = new String[aux_cuad.length+1];
                            System.arraycopy(aux_cuad,0,ind_cuad,0,aux_cuad.length);
                            sb=new StringBuffer();
                            for (int k = 0; k < ind_cuad.length; k++)
                                if(!Util.stringEmpty(ind_cuad[k])){
                                    q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[k]).intValue());
                                    if((q.op().equals("CALL") && q.res().equalsIgnoreCase(nom_sub) && q.arg2().equalsIgnoreCase(nom_sub))||
                                       (q.op().equals(":=") && q.arg1().equalsIgnoreCase(nom_sub))||
                                       (q.op().equals("PARAM") && q.res().equalsIgnoreCase(nom_sub))) {
                                        if(q.op().equals("CALL")){
                                            nelem=1; 
                                            if(!Util.stringEmpty(sb.toString())) sb=new StringBuffer();
                                            for (Enumeration e = vbles_retorno.keys(); e.hasMoreElements();nelem++) {
                                                nom_dev = (String)e.nextElement();
                                                tipotemp = vbles_retorno.get(nom_dev);
                                                t=nuevotemp();
                                                lugar = var_aux.insertar_temp(t,tipotemp);
                                                if(nelem==1) sb.append(t);
                                                else sb.append(','+t);
                                            }  
                                            q.set_arg2(sb.toString());
                                        }else if(q.op().equals(":=")) { 
                                            q.set_arg2(nom_sub);
                                            q.set_arg1(sb.toString());
                                        }else
                                            q.set_res(sb.toString());
                                        tcuad.set(Integer.valueOf(ind_cuad[k]).intValue(),q);// las vbles temporales se van a guardar separadas por comas en el mismo orden que las vbles de retorno
                                    }    
                                } 
                        } 
                        break;
                case 78:variables.marcar_como_parametros();
                        break;                                      
                case 79:cod = gen("PROC", null, null, ps);
                        pila_sem.push(new Reg(ps,null,cod,null,null,null));
                        variables = new Tvariables();
                        variables.set_proc(ps);
                        break;
                case 80:arg1 = pila_sem.pop();
                        cod = gen("RET",null,null,null);
                        aux_cuad = arg1.codigo().split(",");
                        ind_cuad = new String[aux_cuad.length+1];
                        System.arraycopy(aux_cuad,0,ind_cuad,0,aux_cuad.length);
                        ind_cuad[aux_cuad.length] = cod;
                        for (int k = 0; k < ind_cuad.length; k++)
                            if(!Util.stringEmpty(ind_cuad[k])){
                                q = (Cuad)tcuad.get(Integer.valueOf(ind_cuad[k]).intValue());
                                if((q.op().equals("GOTO") || (!Util.stringEmpty(q.op()) && q.op().length()>=2 && q.op().substring(0,2).equals("IF"))) && Util.stringEmpty(q.res())){
                                    q.set_res(cod);
                                    tcuad.set(Integer.valueOf(ind_cuad[k]).intValue(),q);
                                }   
                            }
                        arg2 = pila_sem.pop();
                        variables.set_codigo(arg2.codigo()+","+arg1.codigo()+","+cod);   
                        variables.set_proc(arg2.nombre());
                        variables.set_comentario(ent.obtener_parte_codigo(arg2.nombre()));
                        tproc.addFirst(variables);
                        break;        
                case 81:pila_sem.push(new Reg("osoa"));
                        break;    
                case 82:pila_sem.push(new Reg(ps,null,null,"#",null,null));
                        break;
                case 83:arg1 = pila_sem.pop(); lizdo="";
                        while(!pila_sem.empty() && Util.stringEmpty(arg1.quad())){
                            if(Util.stringEmpty(lizdo))
                                lizdo =arg1.nombre();
                            else
                                lizdo = arg1.nombre()+","+lizdo;
                            arg1 = pila_sem.pop();
                        }
                        lizdo = arg1.nombre()+","+lizdo;
                        pila_sem.push(new Reg(lizdo));
                        break;
            }
        } finally{
        }
    }
}   
