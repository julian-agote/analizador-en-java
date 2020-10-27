%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
N resd 1
I resd 1
t1 resd 1
t2 resd 1
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
        call read_int
        mov ebx, 1
        mov [N], eax
        mov [I], ebx
etiq1:  mov eax, [I]
        cmp eax, [N]
        jle etiq3
        jmp etiq7
etiq3:  mov eax,[N]
        cdq
        mov ecx,[I]
        idiv ecx
        cmp edx, 0
        mov [t1], edx
        je etiq5
        jmp etiq6
etiq5:  mov eax, [I]
        call print_int
etiq6:  mov eax, [I]
        inc eax
        mov ebx, eax
        mov [I], ebx
        mov [t2], eax
        jmp etiq1
etiq7:  popa
        mov eax, 0 ; retornar a C
        leave
        ret
