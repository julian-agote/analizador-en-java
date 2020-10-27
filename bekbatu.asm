; Para crear el ejecutable usando bcc32c
;
; nasm -f obj -DOBJ_TYPE bekbatu.asm
; bcc32c bekbatu.obj driver.obj asm_io.obj

%include "asm_io.inc"
%ifdef OBJ_TYPE
segment _DATA public align=4 class=DATA use32
%else
segment .data
%endif
prompt1 db "V1 eta V2 batu eta erakutsi emaitza:", 0
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
I resd 1
V2 resd 101
V1 resd 101
BATURA resd 101
t1 resd 1
t2 resd 1
t3 resd 1
t4 resd 1
t5 resd 1
t6 resd 1
t7 resd 1
t8 resd 1
t9 resd 1
t10 resd 1
t11 resd 1
t12 resd 1
t13 resd 1
t14 resd 1
t15 resd 1
t16 resd 1
t17 resd 1
t18 resd 1
t19 resd 1
t20 resd 1
t21 resd 1
t22 resd 1
t23 resd 1
t24 resd 1
t25 resd 1
t26 resd 1
t27 resd 1
;
;	V1(1):=5 
;	V1(2):=7 
;	V1(3):=7
;	V1(4):=100
;	V1(5):=-20
;	V1(6):=5 
;	V1(7):=5 
;	V1(8):=5 
;	V1(9):=5 
;	V1(10):=5             
;	V2(1):=1 
;	V2(2):=1 
;	V2(3):=1
;	V2(4):=1
;	V2(5):=1
;	V2(6):=1 
;	V2(7):=1 
;	V2(8):=1 
;	V2(9):=1 
;	V2(10):=1
;	I := 0
;    bitartean I < 10 egin 
;        I := I+1
;        BATURA(I) := V1(I) + V2(I)
;    ambitartean
;    idatz ("V1 eta V2 batu eta erakutsi emaitza:")   
;    I := 0
;    bitartean I < 10 egin 
;        I := I+1
;        idatz (BATURA(I))
;    ambitartean     
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
        mov eax, 5
        mov esi, 1
        mov [V1 + 4*esi], eax
        mov ebx, 7
        mov esi, 2
        mov [V1 + 4*esi], ebx
        mov ecx, 7
        mov esi, 3
        mov [V1 + 4*esi], ecx
        mov edx, 100
        mov esi, 4
        mov [V1 + 4*esi], edx
        mov edi, -20
        mov esi, 5
        mov [V1 + 4*esi], edi
        mov [t1], eax
        mov eax, 5
        mov esi, 6
        mov [V1 + 4*esi], eax
        mov [t6], eax
        mov eax, 5
        mov esi, 7
        mov [V1 + 4*esi], eax
        mov [t7], eax
        mov eax, 5
        mov esi, 8
        mov [V1 + 4*esi], eax
        mov [t8], eax
        mov eax, 5
        mov esi, 9
        mov [V1 + 4*esi], eax
        mov [t9], eax
        mov eax, 5
        mov esi, 10
        mov [V1 + 4*esi], eax
        mov [t10], eax
        mov eax, 1
        mov esi, 1
        mov [V2 + 4*esi], eax
        mov [t11], eax
        mov eax, 1
        mov esi, 2
        mov [V2 + 4*esi], eax
        mov [t12], eax
        mov eax, 1
        mov esi, 3
        mov [V2 + 4*esi], eax
        mov [t13], eax
        mov eax, 1
        mov esi, 4
        mov [V2 + 4*esi], eax
        mov [t14], eax
        mov eax, 1
        mov esi, 5
        mov [V2 + 4*esi], eax
        mov [t15], eax
        mov eax, 1
        mov esi, 6
        mov [V2 + 4*esi], eax
        mov [t16], eax
        mov eax, 1
        mov esi, 7
        mov [V2 + 4*esi], eax
        mov [t17], eax
        mov eax, 1
        mov esi, 8
        mov [V2 + 4*esi], eax
        mov [t18], eax
        mov eax, 1
        mov esi, 9
        mov [V2 + 4*esi], eax
        mov [t19], eax
        mov eax, 1
        mov esi, 10
        mov [V2 + 4*esi], eax
        mov [t20], eax
        mov eax, 0
        mov [I], eax;<--
etiq1:  mov eax, [I]
        cmp eax, 10
        jl etiq3
        jmp etiq4
etiq3:  mov eax, [I]
        inc eax
        mov ebx, eax
        mov ecx, [V1 + 4*ebx]
        mov [t23], ecx
        mov edx, [V2 + 4*ebx]
        mov [t24], edx
        mov esi, ecx
        add esi, edx
        mov edi, esi
        mov [t25], esi
        mov esi, ebx
        mov [BATURA + 4*esi], edi
        mov [I], ebx;<--
        jmp etiq1
etiq4:  mov eax, prompt1
        call print_string
        mov ebx, 0
        mov [I], ebx;<--
etiq5:  mov eax, [I]
        cmp eax, 10
        jl etiq7
        jmp etiq8
etiq7:  mov eax, [I]
        inc eax
        mov ebx, eax
        mov ecx, [BATURA + 4*ebx]
        mov [t27], ecx
        mov [I], eax
        mov eax, ecx
        call print_int
        jmp etiq5
etiq8:  popa
        mov eax, 0 ; retornar a C
        leave
        ret
