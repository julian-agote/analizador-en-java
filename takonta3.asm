%include "asm_io.inc"
L1 equ "."
L2 equ "T"
L3 equ "A"
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
KONTA resd 1
KAR1 resb 1
KAR2 resb 1
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
       mov eax, 0
       mov [KONTA], eax<--
       call irakur_lehenengo_bikote
       mov al, [KAR2]
       cmp al, L1
       jne etiq3
       jmp etiq11
etiq3: mov al, [KAR1]
       cmp al, L2
       je etiq5
       jmp etiq8
etiq5: mov al, [KAR2]
       cmp al, L3
       je etiq7
       jmp etiq8
etiq7: mov eax, [KONTA]
       inc eax
       mov ebx, eax
       mov [KONTA], ebx<--
       call irakur_bikote
       mov al, [KAR2]
       cmp al, L1
       je etiq11
       jmp etiq3
etiq11:mov eax, [KONTA]
       call print_int
       popa
       mov eax, 0 ; retornar a C
       leave
       ret
; subprograma irakur-lehenengo-bikote
L11 equ " "
%ifndef OBJ_TYPE
segment .text
%endif
irakur_lehenengo_bikote:
       push ebp
       mov ebp, esp
       mov eax, L11
       mov [KAR1], eax
       call read_int
       mov esp, ebp
       pop ebp
       ret ; retorna al llamador
; subprograma irakur-bikote
%ifndef OBJ_TYPE
segment .text
%endif
irakur_bikote:
       push ebp
       mov ebp, esp
       mov eax, [KAR2]
       mov [KAR1], eax
       call read_int
       mov esp, ebp
       pop ebp
       ret ; retorna al llamador
