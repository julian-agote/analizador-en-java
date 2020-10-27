; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE frogafakt.asm
; bcc32c frogafakt.obj driver.obj asm_io.obj

%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
I resd 1
R resd 1
t1 resd 1
;
;	irakur (I)
;	R := FAKTORIALA(I)
;	idatz (R)
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
        push eax; parametro I
        mov [I], eax;<--
        push dword t1; empuja la direccion de t1 en la pila
        call FAKTORIALA
        add esp, 8 ; quita los parametros de la pila
        mov eax, [t1]
        call print_int
        popa
        mov eax, 0 ; retornar a C
        leave
        ret
; subprograma FAKTORIALA
;
;	baldin N = 0 orduan
;		F := 1
;	bestela
;		hasiera
;			F := FAKTORIALA(N - 1)
;			F := F * N
;		amaia
;
%ifndef OBJ_TYPE
segment .text
%endif
FAKTORIALA:
        enter 16,0
        mov eax, [ebp + 12]
        cmp eax, 0
        je etiq102
        jmp etiq103
etiq102:mov eax, 1
        mov [ebp - 4], eax;<--
        jmp etiq105
etiq103:mov eax, [ebp + 12]
        dec eax
        push eax; parametro t2
        lea eax, [ebp - 12]
        push dword eax; empuja la direccion de t3 en la pila
        call FAKTORIALA
        add esp, 8 ; quita los parametros de la pila
        mov eax, [ebp - 12]
        cdq
        mov ecx,[ebp + 12]
        imul ecx
        mov [ebp - 16], eax
       mov eax, [ebp - 16]
       mov [ebp - 4], eax;<--
etiq105:mov ebx,[EBP + 8 + 0]
        mov eax,[ebp - 4]
        mov [ebx],eax
        leave
        ret ; retorna al llamador
