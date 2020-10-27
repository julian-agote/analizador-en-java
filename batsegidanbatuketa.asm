; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE batsegidanbatuketa.asm
; bcc32c batsegidanbatuketa.obj driver.obj asm_io.obj

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
t4 resd 1
t5 resd 1
;
;	idatz ("Sartu bat segidako azken zenbakia(<100)?")
;	irakur (N)
;	I := 0
;	bitartean I < N egin
;		I := I+1
;		A(I) := I
;	ambitartean
;	I := 0
;	X := 0
;	bitartean I < N egin {x=Sum(A,1,i) ^ 0<=i<=n}
;		I := I+1
;		X := X+A(I)
;	ambitartean
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
etiq4:  mov eax, 0
        mov ebx, 0
        mov [X], ebx;<--
        mov [I], eax;<--
etiq5:  mov eax, [I]
        cmp eax, [N]
        jl etiq7
        jmp etiq8
etiq7:  mov eax, [I]
        inc eax
        mov ebx, eax
        mov ecx, [A + 4*ebx]
        mov [t4], ecx
        mov edx, [X]
        add edx, ecx
        mov edi, edx
        mov [X], edi;<--
        mov [I], ebx;<--
        jmp etiq5
etiq8:  mov eax, prompt2
        call print_string
        mov eax, [X]
        call print_int
        popa
        mov eax, 0 ; retornar a C
        leave
        ret
