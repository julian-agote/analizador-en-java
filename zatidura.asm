%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _DATA public align=4 class=DATA use32
%else
segment .data
%endif
prompt1 db "Sartu izendatzailea(>=0)?", 0
prompt2 db "Sartu zenbakitzailea(>0)?", 0
prompt3 db "zatidura osoa:", 0
prompt4 db "hondarra:", 0
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
Z resd 1
H resd 1
Y resd 1
X resd 1
t1 resd 1
t2 resd 1
%ifdef OBJ_TYPE
group DGROUP _BSS _DATA
segment _TEXT public align=1 class=CODE use32
%else
segment .text
%endif
       global _asm_main
_asm_main:
       enter 0,0 
       pusha
       mov eax, prompt1
       call print_string
       call read_int
       mov [X], eax
       mov eax, prompt2
       call print_string
       call read_int
       mov ebx, 0
       mov ecx, [X]
       mov [Z], ebx
       mov [H], ecx
       mov [Y], eax
etiq1: mov eax, [H]
       cmp eax, [Y]
       jge etiq3
       jmp etiq4
etiq3: mov eax, [Z]
       inc eax
       mov ebx, eax
       mov ecx, [H]
       sub ecx, [Y]
       mov edx, ecx
       mov [Z], ebx
       mov [H], edx
       mov [t1], eax
       mov [t2], ecx
       jmp etiq1
etiq4: mov eax, prompt3
       call print_string
       mov eax, [Z]
       call print_int
       mov eax, prompt4
       call print_string
       mov eax, [H]
       call print_int
       popa
       mov eax, 0 ; retornar a C
       leave
       ret
