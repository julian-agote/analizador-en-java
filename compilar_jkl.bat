java geLRco_tablas grama_jkl.txt > sal_jkl.txt
cd parser_jkl
javac -g parser.java TGestorSemantico.java TEntrada.java TElemEnt.java TTablaSimbolos.java
cd ..
javac -g parser_JKL.java