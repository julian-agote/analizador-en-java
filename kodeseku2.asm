%include "asm_io.inc"
L1 equ "A"
L2 equ "B"
L3 equ "C"
L4 equ "D"
L5 equ "E"
L6 equ "F"
L7 equ "G"
L8 equ "H"
L9 equ "I"
L10 equ "J"
L11 equ "K"
L12 equ "L"
L13 equ "M"
L14 equ "N"
L15 equ "O"
L16 equ "P"
L17 equ "Q"
L18 equ "R"
L19 equ "S"
L20 equ "T"
L21 equ "U"
L22 equ "V"
L23 equ "X"
L24 equ "Y"
L25 equ "Z"
L26 equ "."
L27 equ " "
%ifdef OBJ_TYPE
segment _DATA public align=4 class=DATA use32
%else
segment .data
%endif
prompt1 db ".", 0
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
KAR resb 1
KARKODE resb 1
ALFABETO resb 27
P resd 1
Q resd 1
t1 resb 1
t2 resb 1
t3 resb 1
t4 resb 1
t5 resb 1
t6 resb 1
t7 resb 1
t8 resb 1
t9 resb 1
t10 resb 1
t11 resb 1
t12 resb 1
t13 resb 1
t14 resb 1
t15 resb 1
t16 resb 1
t17 resb 1
t18 resb 1
t19 resb 1
t20 resb 1
t21 resb 1
t22 resb 1
t23 resb 1
t24 resb 1
t25 resb 1
t26 resb 1
t27 resd 1
t28 resd 1
t29 resd 1
t30 resb 1
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
        mov al, L1
        mov esi, 1
        mov [ALFABETO + esi], al
        mov bl, L2
        mov esi, 2
        mov [ALFABETO + esi], bl
        mov cl, L3
        mov esi, 3
        mov [ALFABETO + esi], cl
        mov dl, L4
        mov esi, 4
        mov [ALFABETO + esi], dl
        mov di, L5
        mov esi, 5
        mov [ALFABETO + esi], di
        mov [t1], al
        mov al, L6
        mov esi, 6
        mov [ALFABETO + esi], al
        mov [t6], al
        mov al, L7
        mov esi, 7
        mov [ALFABETO + esi], al
        mov [t7], al
        mov al, L8
        mov esi, 8
        mov [ALFABETO + esi], al
        mov [t8], al
        mov al, L9
        mov esi, 9
        mov [ALFABETO + esi], al
        mov [t9], al
        mov al, L10
        mov esi, 10
        mov [ALFABETO + esi], al
        mov [t10], al
        mov al, L11
        mov esi, 11
        mov [ALFABETO + esi], al
        mov [t11], al
        mov al, L12
        mov esi, 12
        mov [ALFABETO + esi], al
        mov [t12], al
        mov al, L13
        mov esi, 13
        mov [ALFABETO + esi], al
        mov [t13], al
        mov al, L14
        mov esi, 14
        mov [ALFABETO + esi], al
        mov [t14], al
        mov al, L15
        mov esi, 15
        mov [ALFABETO + esi], al
        mov [t15], al
        mov al, L16
        mov esi, 16
        mov [ALFABETO + esi], al
        mov [t16], al
        mov al, L17
        mov esi, 17
        mov [ALFABETO + esi], al
        mov [t17], al
        mov al, L18
        mov esi, 18
        mov [ALFABETO + esi], al
        mov [t18], al
        mov al, L19
        mov esi, 19
        mov [ALFABETO + esi], al
        mov [t19], al
        mov al, L20
        mov esi, 20
        mov [ALFABETO + esi], al
        mov [t20], al
        mov al, L21
        mov esi, 21
        mov [ALFABETO + esi], al
        mov [t21], al
        mov al, L22
        mov esi, 22
        mov [ALFABETO + esi], al
        mov [t22], al
        mov al, L23
        mov esi, 23
        mov [ALFABETO + esi], al
        mov [t23], al
        mov al, L24
        mov esi, 24
        mov [ALFABETO + esi], al
        mov [t24], al
        mov al, L25
        mov esi, 25
        mov [ALFABETO + esi], al
        mov [t25], al
        call read_char
        cmp eax, 0AH
        jne no_nl31
        call read_char
no_nl31:mov [KAR], al
        mov [t2], bl
        mov [t3], cl
        mov [t4], dl
        mov [t5], di
etiq1:  mov al, [KAR]
        cmp al, L26
        jne etiq3
        jmp etiq16
etiq3:  mov al, [KAR]
        cmp al, L27
        je etiq5
        jmp etiq6
etiq5:  mov al, L27
        mov [KARKODE], al
        jmp etiq15
etiq6:  mov eax, 1
        mov [P], eax
etiq7:  mov ebx, [P]
        mov al, [ALFABETO + ebx]
        mov [t26], al
        mov cl, [KAR]
        cmp cl, al
        mov [t26], al
        jne etiq9
        jmp etiq10
etiq9:  mov eax, [P]
        inc eax
        mov ebx, eax
        mov [P], ebx
        mov [t27], eax
        jmp etiq7
etiq10: mov eax, [P]
        cmp eax, 22
        jg etiq12
        jmp etiq13
etiq12: mov eax, [P]
        sub eax, 22
        mov ebx, eax
        mov [Q], ebx
        mov [t28], eax
        jmp etiq14
etiq13: mov eax, [P]
        add eax, 3
        mov ebx, eax
        mov [Q], ebx
        mov [t29], eax
etiq14: mov ebx, [Q]
        mov al, [ALFABETO + ebx]
        mov [t30], al
        mov cl, al
        mov [KARKODE], cl
        mov [t30], al
etiq15: mov eax, [KARKODE]
        call print_char
        call read_char
        cmp eax, 0AH
        jne no_nl32
        call read_char
no_nl32:mov [KAR], al
        jmp etiq1
etiq16: mov eax, prompt1
        call print_string
        popa
        mov eax, 0 ; retornar a C
        leave
        ret