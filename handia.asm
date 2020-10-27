; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE handia.asm
; bcc32c handia.obj driver.obj asm_io.obj

%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
B resd 1
A resd 1
C resd 1
; irakur(A)
;    irakur(B)
;    aukera A > B: C:= A
;       A = B:C:=A
;	   A< B:C:=B
;	amaukera
;	idatz(C)
;
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
        mov ebx, [A]
        cmp ebx, eax
        mov [B], eax;<--
        jg etiq2
        jmp etiq3
etiq2:  mov eax, [A]
        mov [C], eax;<--
        jmp etiq9
etiq3:  mov eax, [A]
        cmp eax, [B]
        je etiq5
        jmp etiq6
etiq5:  mov eax, [A]
        mov [C], eax;<--
        jmp etiq9
etiq6:  mov eax, [A]
        cmp eax, [B]
        jl etiq8
        jmp etiq9
etiq8:  mov eax, [B]
        mov [C], eax;<--
        jmp etiq9
etiq9:  mov eax, [C]
        call print_int
        popa
        mov eax, 0 ; retornar a C
        leave
        ret
