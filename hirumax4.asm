%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
MAX resd 1
C resd 1
B resd 1
A resd 1
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
       mov [A], eax
       call read_int
       mov [B], eax
       call read_int
       mov ebx, [A]
       cmp ebx, [B]
       mov [C], eax
       jg etiq2
       jmp etiq8
etiq2: mov eax, [A]
       cmp eax, [C]
       jg etiq4
       jmp etiq5
etiq4: mov eax, [A]
       mov [MAX], eax
etiq5: cmp eax, [C]
       jl etiq7
       jmp etiq17
etiq7: mov eax, [C]
       mov [MAX], eax
etiq8: cmp eax, [B]
       jl etiq10
       jmp etiq16
etiq10:mov eax, [B]
       cmp eax, eax
       jg etiq12
       jmp etiq13
etiq12:mov eax, [B]
       mov [MAX], eax
etiq13:cmp eax, eax
       jl etiq15
       jmp etiq17
etiq15:mov eax, eax
       mov [MAX], eax
etiq16:mov eax, [MAX]
       call print_int
etiq17:popa
       mov eax, 0 ; retornar a C
       leave
       ret
