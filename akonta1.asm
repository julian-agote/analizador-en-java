; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE akonta1.asm
; bcc32c akonta1.obj driver.obj asm_io.obj

%include "asm_io.inc"
L1 equ "a"
L2 equ "."
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
KAR resb 1
KONTA resd 1
t1 resd 1
;
;	KONTA := 0;
;	errepika {KONTA-k ageritako 'a' letran kopurua dauka}
;		irakur (KAR)
;		baldin KAR = 'a' orduan KONTA := KONTA + 1
;	harik-eta KAR = '.';
;	{KONTA-k sekuentzia osoan ageritako 'a' letren	kopurua dauka}
;	idatz (KONTA)
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
        mov eax, 0
        mov [KONTA], eax;<--
etiq1:  call read_char
        cmp eax, 0AH
        jne no_nl2
        call read_char
no_nl2: cmp al, L1
        mov [KAR], al;<--
        je etiq3
        jmp etiq4
etiq3:  mov eax, [KONTA]
        inc eax
        mov ebx, eax
        mov [KONTA], ebx;<--
etiq4:  mov al, [KAR]
        cmp al, L2
        je etiq6
        jmp etiq1
etiq6:  mov eax, [KONTA]
        call print_int
        popa
        mov eax, 0 ; retornar a C
        leave
        ret
