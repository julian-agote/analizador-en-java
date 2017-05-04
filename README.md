# analizador-en-java
Son programas en java para a partir de una definicion de gramatica generar un programa que sirva de analizador sintactico de una expresion escrita en la gramatica especificada.
Para obtener el analizador sintactico a partir de un fichero con la gramatica p.ej. grama_jkl.txt se introduce el siguiente comando:
java geLRco_tablas grama_jkl.txt
Como salida se generará un fichero parser.java (dentro de un directorio parser_jkl, que nosotros habremos creado previamente) que contendra el analizador propiamente dicho.
A este directorio (parser_jkl) moveremos los ficheros TElement.java, TEntrada.java (que contendra el analizador lexico personalizado para la gramatica), TTablaSimbolos.java y TGestorSemantico.java (que contendra las acciones semanticas para la gramatica). Todos estos tienen que tener la linea "package parser_jkl;" como cabecera.
Si la gramatica fuera otra p.ej. grama_ops5.txt, se crearia un directorio parser_ops5 y se moverian los mismos ficheros pero ahora tendrian en la cabecera "package parser_ops5;".
Faltaria compilar el proyecto completo. Para ello basta ejecutar desde la linea de comandos un fichero (.bat) como el que aparace compilar_jkl.bat.
Como se ve en la 
