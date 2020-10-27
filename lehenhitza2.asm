%include "asm_io.inc"
L1 equ " "
L2 equ "."
%ifdef OBJ_TYPE
segment _DATA public align=4 class=DATA use32
%else
segment .data
%endif
prompt1 db "ez dago bat ere", 0
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
KAR resb 1
LEHENKONTA resd 1
LEHENA resb 21
LUZERA resd 1
HITZA resb 21
HITZLUZERA resd 1
I resd 1
t1 resb 1
t2 resd 1
t3 resd 1
t4 resb 1
t5 resd 1
t6 resd 1
t7 resb 1
t8 resb 1
t9 resd 1
t10 resb 1
t11 resb 1
t12 resd 1
t13 resd 1
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
         call read_char
         cmp eax, 0AH
         jne no_nl14
         call read_char
no_nl14: mov [KAR], al
etiq1:   mov al, [KAR]
         cmp al, L1
         je etiq3
         jmp etiq4
etiq3:   call read_char
         cmp eax, 0AH
         jne no_nl15
         call read_char
no_nl15: mov [KAR], al
         jmp etiq1
etiq4:   mov al, [KAR]
         cmp al, L2
         je etiq6
         jmp etiq7
etiq6:   mov eax, prompt1
         call print_string
         jmp etiq42
etiq7:   mov eax, 1
         mov [I], eax
etiq8:   mov al, [KAR]
         cmp al, L1
         jne etiq10
         jmp etiq13
etiq10:  mov al, [KAR]
         cmp al, L2
         jne etiq12
         jmp etiq13
etiq12:  mov al, [KAR]
         mov esi, [I]
         mov [LEHENA + esi], al
         mov ebx, [I]
         inc ebx
         mov ecx, ebx
         mov [t1], al
         call read_char
         cmp eax, 0AH
         jne no_nl16
         call read_char
no_nl16: mov [KAR], al
         mov [I], ecx
         mov [t2], ebx
         jmp etiq8
etiq13:  mov eax, [I]
         dec eax
         mov ebx, eax
         mov [LUZERA], ebx
         mov [t3], eax
etiq14:  mov al, [KAR]
         cmp al, L1
         je etiq16
         jmp etiq17
etiq16:  call read_char
         cmp eax, 0AH
         jne no_nl17
         call read_char
no_nl17: mov [KAR], al
         jmp etiq14
etiq17:  mov eax, 0
         mov [LEHENKONTA], eax
etiq18:  mov al, [KAR]
         cmp al, L2
         jne etiq20
         jmp etiq41
etiq20:  mov eax, 1
         mov [I], eax
etiq21:  mov al, [KAR]
         cmp al, L1
         jne etiq23
         jmp etiq26
etiq23:  mov al, [KAR]
         cmp al, L2
         jne etiq25
         jmp etiq26
etiq25:  mov al, [KAR]
         mov esi, [I]
         mov [HITZA + esi], al
         mov ebx, [I]
         inc ebx
         mov ecx, ebx
         mov [t4], al
         call read_char
         cmp eax, 0AH
         jne no_nl18
         call read_char
no_nl18: mov [KAR], al
         mov [I], ecx
         mov [t5], ebx
         jmp etiq21
etiq26:  mov eax, [I]
         dec eax
         mov ebx, eax
         mov ecx, [LUZERA]
         cmp ecx, ebx
         mov [HITZLUZERA], ebx
         mov [t6], eax
         je etiq28
         jmp etiq37
etiq28:  mov eax, 1
         mov [I], eax
etiq29:  mov eax, [I]
         cmp eax, [LUZERA]
         jl etiq31
         jmp etiq34
etiq31:  mov ebx, [I]
         mov al, [LEHENA + ebx]
         mov [t7], al
         mov cl, [HITZA + ebx]
         mov [t8], cl
         cmp al, cl
         mov [t7], al
         mov [t8], cl
         je etiq33
         jmp etiq34
etiq33:  mov eax, [I]
         inc eax
         mov ebx, eax
         mov [I], ebx
         mov [t9], eax
         jmp etiq29
etiq34:  mov ebx, [I]
         mov al, [LEHENA + ebx]
         mov [t10], al
         mov cl, [HITZA + ebx]
         mov [t11], cl
         cmp al, cl
         mov [t10], al
         mov [t11], cl
         je etiq36
         jmp etiq37
etiq36:  mov eax, [LEHENKONTA]
         inc eax
         mov ebx, eax
         mov [LEHENKONTA], ebx
         mov [t12], eax
etiq37:  mov al, [KAR]
         cmp al, L1
         je etiq39
         jmp etiq18
etiq39:  call read_char
         cmp eax, 0AH
         jne no_nl19
         call read_char
no_nl19: mov [KAR], al
         jmp etiq37
         jmp etiq18
etiq41:  mov eax, [LEHENKONTA]
         inc eax
         call print_int
         mov [t13], eax
etiq42:  popa
         mov eax, 0 ; retornar a C
         leave
         ret