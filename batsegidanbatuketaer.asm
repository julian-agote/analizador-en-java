; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE batsegidanbatuketaer.asm
; bcc32c batsegidanbatuketaer.obj driver.obj asm_io.obj

%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _DATA public align=4 class=DATA use32
%else
segment .data
%endif
prompt1 db "Sartu bat segidako azken zenbakia(<100)?", 0
prompt2 db "lehenengo n osoen batuketa da:", 0
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
X resd 1
I resd 1
N resd 1
A resd 101
t1 resd 1
t2 resd 1
t3 resd 1
;
;	idatz ("Sartu bat segidako azken zenbakia(<100)?")
;	irakur (N)
;	I := 0
;	bitartean I < N egin
;		I := I+1
;		A(I) := I
;	ambitartean
;	X := I-SUMA(A,N)  
;	idatz ("lehenengo n osoen batuketa da:")
;	idatz (X)
;
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
        mov ebx, 0
        mov [I], ebx;<--
        mov [N], eax;<--
etiq1:  mov eax, [I]
        cmp eax, [N]
        jl etiq3
        jmp etiq4
etiq3:  mov eax, [I]
        inc eax
        mov ebx, eax
        mov ecx, ebx
        mov esi, ebx
        mov [A + 4*esi], ecx
        mov [I], ebx;<--
        jmp etiq1
etiq4:  push dword [A + 4*1]
        push dword [A + 4*2]
        push dword [A + 4*3]
        push dword [A + 4*4]
        push dword [A + 4*5]
        push dword [A + 4*6]
        push dword [A + 4*7]
        push dword [A + 4*8]
        push dword [A + 4*9]
        push dword [A + 4*10]
        push dword [A + 4*11]
        push dword [A + 4*12]
        push dword [A + 4*13]
        push dword [A + 4*14]
        push dword [A + 4*15]
        push dword [A + 4*16]
        push dword [A + 4*17]
        push dword [A + 4*18]
        push dword [A + 4*19]
        push dword [A + 4*20]
        push dword [A + 4*21]
        push dword [A + 4*22]
        push dword [A + 4*23]
        push dword [A + 4*24]
        push dword [A + 4*25]
        push dword [A + 4*26]
        push dword [A + 4*27]
        push dword [A + 4*28]
        push dword [A + 4*29]
        push dword [A + 4*30]
        push dword [A + 4*31]
        push dword [A + 4*32]
        push dword [A + 4*33]
        push dword [A + 4*34]
        push dword [A + 4*35]
        push dword [A + 4*36]
        push dword [A + 4*37]
        push dword [A + 4*38]
        push dword [A + 4*39]
        push dword [A + 4*40]
        push dword [A + 4*41]
        push dword [A + 4*42]
        push dword [A + 4*43]
        push dword [A + 4*44]
        push dword [A + 4*45]
        push dword [A + 4*46]
        push dword [A + 4*47]
        push dword [A + 4*48]
        push dword [A + 4*49]
        push dword [A + 4*50]
        push dword [A + 4*51]
        push dword [A + 4*52]
        push dword [A + 4*53]
        push dword [A + 4*54]
        push dword [A + 4*55]
        push dword [A + 4*56]
        push dword [A + 4*57]
        push dword [A + 4*58]
        push dword [A + 4*59]
        push dword [A + 4*60]
        push dword [A + 4*61]
        push dword [A + 4*62]
        push dword [A + 4*63]
        push dword [A + 4*64]
        push dword [A + 4*65]
        push dword [A + 4*66]
        push dword [A + 4*67]
        push dword [A + 4*68]
        push dword [A + 4*69]
        push dword [A + 4*70]
        push dword [A + 4*71]
        push dword [A + 4*72]
        push dword [A + 4*73]
        push dword [A + 4*74]
        push dword [A + 4*75]
        push dword [A + 4*76]
        push dword [A + 4*77]
        push dword [A + 4*78]
        push dword [A + 4*79]
        push dword [A + 4*80]
        push dword [A + 4*81]
        push dword [A + 4*82]
        push dword [A + 4*83]
        push dword [A + 4*84]
        push dword [A + 4*85]
        push dword [A + 4*86]
        push dword [A + 4*87]
        push dword [A + 4*88]
        push dword [A + 4*89]
        push dword [A + 4*90]
        push dword [A + 4*91]
        push dword [A + 4*92]
        push dword [A + 4*93]
        push dword [A + 4*94]
        push dword [A + 4*95]
        push dword [A + 4*96]
        push dword [A + 4*97]
        push dword [A + 4*98]
        push dword [A + 4*99]
        push dword [N]
        push dword t3; empuja la direccion de t3 en la pila
        call I_SUMA
        add esp, 404 ; quita los parametros de la pila
        mov eax, [t3]
        mov [X], eax
        mov eax, prompt2
        call print_string
        mov eax, [X]
        call print_int
        popa
        mov eax, 0 ; retornar a C
        leave
        ret
; subprograma I-SUMA
;
;	baldin I = 0 orduan
;		S := 0
;	bestela
;		hasiera
;			S := I-SUMA(A,I - 1)
;			S := S + A(I)
;		amaia
;
%ifndef OBJ_TYPE
segment .text
%endif
I_SUMA:
        enter 20,0
        mov eax, [ebp + 12]
        cmp eax, 0
        je etiq102
        jmp etiq103
etiq102:mov eax, 0
        mov [ebp - 4], eax;<--
        jmp etiq105
etiq103:mov eax, [ebp + 12]
        dec eax
        push dword [ebp + 412 - 4*1]
        push dword [ebp + 412 - 4*2]
        push dword [ebp + 412 - 4*3]
        push dword [ebp + 412 - 4*4]
        push dword [ebp + 412 - 4*5]
        push dword [ebp + 412 - 4*6]
        push dword [ebp + 412 - 4*7]
        push dword [ebp + 412 - 4*8]
        push dword [ebp + 412 - 4*9]
        push dword [ebp + 412 - 4*10]
        push dword [ebp + 412 - 4*11]
        push dword [ebp + 412 - 4*12]
        push dword [ebp + 412 - 4*13]
        push dword [ebp + 412 - 4*14]
        push dword [ebp + 412 - 4*15]
        push dword [ebp + 412 - 4*16]
        push dword [ebp + 412 - 4*17]
        push dword [ebp + 412 - 4*18]
        push dword [ebp + 412 - 4*19]
        push dword [ebp + 412 - 4*20]
        push dword [ebp + 412 - 4*21]
        push dword [ebp + 412 - 4*22]
        push dword [ebp + 412 - 4*23]
        push dword [ebp + 412 - 4*24]
        push dword [ebp + 412 - 4*25]
        push dword [ebp + 412 - 4*26]
        push dword [ebp + 412 - 4*27]
        push dword [ebp + 412 - 4*28]
        push dword [ebp + 412 - 4*29]
        push dword [ebp + 412 - 4*30]
        push dword [ebp + 412 - 4*31]
        push dword [ebp + 412 - 4*32]
        push dword [ebp + 412 - 4*33]
        push dword [ebp + 412 - 4*34]
        push dword [ebp + 412 - 4*35]
        push dword [ebp + 412 - 4*36]
        push dword [ebp + 412 - 4*37]
        push dword [ebp + 412 - 4*38]
        push dword [ebp + 412 - 4*39]
        push dword [ebp + 412 - 4*40]
        push dword [ebp + 412 - 4*41]
        push dword [ebp + 412 - 4*42]
        push dword [ebp + 412 - 4*43]
        push dword [ebp + 412 - 4*44]
        push dword [ebp + 412 - 4*45]
        push dword [ebp + 412 - 4*46]
        push dword [ebp + 412 - 4*47]
        push dword [ebp + 412 - 4*48]
        push dword [ebp + 412 - 4*49]
        push dword [ebp + 412 - 4*50]
        push dword [ebp + 412 - 4*51]
        push dword [ebp + 412 - 4*52]
        push dword [ebp + 412 - 4*53]
        push dword [ebp + 412 - 4*54]
        push dword [ebp + 412 - 4*55]
        push dword [ebp + 412 - 4*56]
        push dword [ebp + 412 - 4*57]
        push dword [ebp + 412 - 4*58]
        push dword [ebp + 412 - 4*59]
        push dword [ebp + 412 - 4*60]
        push dword [ebp + 412 - 4*61]
        push dword [ebp + 412 - 4*62]
        push dword [ebp + 412 - 4*63]
        push dword [ebp + 412 - 4*64]
        push dword [ebp + 412 - 4*65]
        push dword [ebp + 412 - 4*66]
        push dword [ebp + 412 - 4*67]
        push dword [ebp + 412 - 4*68]
        push dword [ebp + 412 - 4*69]
        push dword [ebp + 412 - 4*70]
        push dword [ebp + 412 - 4*71]
        push dword [ebp + 412 - 4*72]
        push dword [ebp + 412 - 4*73]
        push dword [ebp + 412 - 4*74]
        push dword [ebp + 412 - 4*75]
        push dword [ebp + 412 - 4*76]
        push dword [ebp + 412 - 4*77]
        push dword [ebp + 412 - 4*78]
        push dword [ebp + 412 - 4*79]
        push dword [ebp + 412 - 4*80]
        push dword [ebp + 412 - 4*81]
        push dword [ebp + 412 - 4*82]
        push dword [ebp + 412 - 4*83]
        push dword [ebp + 412 - 4*84]
        push dword [ebp + 412 - 4*85]
        push dword [ebp + 412 - 4*86]
        push dword [ebp + 412 - 4*87]
        push dword [ebp + 412 - 4*88]
        push dword [ebp + 412 - 4*89]
        push dword [ebp + 412 - 4*90]
        push dword [ebp + 412 - 4*91]
        push dword [ebp + 412 - 4*92]
        push dword [ebp + 412 - 4*93]
        push dword [ebp + 412 - 4*94]
        push dword [ebp + 412 - 4*95]
        push dword [ebp + 412 - 4*96]
        push dword [ebp + 412 - 4*97]
        push dword [ebp + 412 - 4*98]
        push dword [ebp + 412 - 4*99]
        push eax; parametro t4
        lea eax, [ebp - 12]
        push dword eax; empuja la direccion de t5 en la pila
        call I_SUMA
        add esp, 404 ; quita los parametros de la pila
        mov eax, [ebp - 12]
        mov ecx, [ebp + 12]
        mov esi, 412
        shl ecx,2
        sub esi, ecx
        add esi, ebp
        mov ebx, [esi]
        mov [ebp - 16], ebx
        mov edx, eax
        add edx, ebx
        mov eax, edx
        mov [ebp - 4], eax;<--
etiq105:mov ebx,[EBP + 8 + 0]
        mov eax,[ebp - 4]
        mov [ebx],eax
        leave
        ret ; retorna al llamador
