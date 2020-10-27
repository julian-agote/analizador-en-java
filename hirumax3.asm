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
       jmp etiq9
etiq2: mov eax, [A]
       cmp eax, [C]
       jg etiq4
       jmp etiq5
etiq4: mov eax, [A]
       mov [MAX], eax
       jmp etiq8
etiq5: cmp eax, [C]
       jl etiq7
       jmp etiq8
etiq7: mov eax, [C]
       mov [MAX], eax
       jmp etiq8
etiq8: jmp etiq18
etiq9: cmp eax, [B]
       jl etiq11
       jmp etiq18
etiq11:mov eax, [B]
       cmp eax, eax
       jg etiq13
       jmp etiq14
etiq13:mov eax, [B]
       mov [MAX], eax
       jmp etiq17
etiq14:cmp eax, eax
       jl etiq16
       jmp etiq17
etiq16:mov eax, eax
       mov [MAX], eax
       jmp etiq17
etiq17:jmp etiq18
etiq18:mov eax, [MAX]
       call print_int
       popa
       mov eax, 0 ; retornar a C
       leave
       ret
