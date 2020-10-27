; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE frogaoinalder.asm
; bcc32c frogaoinalder.obj driver.obj asm_io.obj

%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
OIN resd 1
N resd 1
R resd 1
t1 resd 1
;
;	irakur (N)
;	irakur (OIN)
;	R := OINARRIAALDER(N,OIN)
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
        mov [N], eax
        call read_int
        push dword [N]
        push eax; parametro OIN
        mov [OIN], eax;<--
        push dword t1; empuja la direccion de t1 en la pila
        call OINARRIAALDER
        add esp, 12 ; quita los parametros de la pila
        mov eax, [t1]
        call print_int
        popa
        mov eax, 0 ; retornar a C
        leave
        ret
; subprograma OINARRIAALDER
;
;	baldin N = 0 orduan
;		M := 0
;	bestela
;		hasiera
;			M := OINARRIAALDER(N div OIN,OIN)
;			M := N mod OIN + 10 * M
;		amaia
;
%ifndef OBJ_TYPE
segment .text
%endif
OINARRIAALDER:
        enter 24,0
        mov eax, [ebp + 16]
        cmp eax, 0
        je etiq102
        jmp etiq103
etiq102:mov eax, 0
        mov [ebp - 4], eax;<--
        jmp etiq105
etiq103:mov eax,[ebp + 16]
        cdq
        mov ecx,[ebp + 12]
        idiv ecx
        push eax; parametro t2
        push ecx; parametro OIN
        lea eax, [ebp - 12]
        push dword eax; empuja la direccion de t3 en la pila
        call OINARRIAALDER
        add esp, 12 ; quita los parametros de la pila
        mov eax, [ebp - 12]
        mov [ebp - 4], eax
        mov eax,[ebp + 16]
        cdq
        mov ecx,[ebp + 12]
        idiv ecx
        mov eax, 10
        mov [ebp - 16], edx
        cdq
        mov ecx,[ebp - 4]
        imul ecx
        mov ebx, [ebp - 16]
        add ebx, eax
        mov ecx, ebx
        mov [ebp - 4], ecx;<--
etiq105:mov ebx,[EBP + 8 + 0]
        mov eax,[ebp - 4]
        mov [ebx],eax
        leave
        ret ; retorna al llamador
