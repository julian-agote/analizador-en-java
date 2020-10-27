%include "asm_io.inc"
L1 equ "."
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
GORDELEKU resb 101
KAR resb 1
I resd 1
t1 resd 1
t2 resb 1
t3 resb 1
t4 resd 1
%ifdef OBJ_TYPE
group DGROUP _BSS
segment _TEXT public align=1 class=CODE use32
%else
segment .text
%endif
        global _asm_main
_asm_main:
        enter 0,0 
        pusha
        call read_char
        cmp eax, 0AH
        jne no_nl5
        call read_char
no_nl5: mov ebx, 0
        mov [KAR], al
        mov [I], ebx
etiq1:  mov al, [KAR]
        cmp al, L1
        jne etiq3
        jmp etiq4
etiq3:  mov eax, [I]
        inc eax
        mov ebx, eax
        mov cl, [KAR]
        mov esi, ebx
        mov [GORDELEKU + esi], cl
        mov [I], eax
        call read_char
        cmp eax, 0AH
        jne no_nl6
        call read_char
no_nl6: mov [KAR], al
        mov [t1], eax
        mov [t2], cl
        jmp etiq1
etiq4:  mov eax, [I]
        cmp eax, 1
        jge etiq6
        jmp etiq7
etiq6:  mov ebx, [I]
        mov al, [GORDELEKU + ebx]
        mov [t3], al
        call print_char
        mov ecx, ebx
        dec ecx
        mov ebx, ecx
        mov [I], ebx
        mov [t3], al
        mov [t4], ecx
        jmp etiq4
etiq7:  popa
        mov eax, 0 ; retornar a C
        leave
        ret