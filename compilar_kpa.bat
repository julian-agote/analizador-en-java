java geLRco_tablas grama_kpa.txt > sal_kpa.txt
cd parser_kpa
javac parser.java TGestorSemantico.java TEntrada.java TElemEnt.java TTablaSimbolos.java
cd ..
javac parser_KPA.java