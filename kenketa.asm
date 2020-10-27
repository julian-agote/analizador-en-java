%include "asm_io.inc"
L1 equ t28
%ifdef OBJ_TYPE
segment _DATA public align=4 class=DATA use32
%else
segment .data
%endif
prompt1 db "Sartu zenbakia A (3 digitu)?", 0
prompt2 db "Sartu beste zenbakia B (3 digitu)?", 0
prompt3 db "A - B =", 0
%ifdef OBJ_TYPE
segment _BSS public align=4 class=BSS use32
%else
segment .bss
%endif
C resd 3
B resd 3
A resd 3
BEHEKO_GAIN resd 1
D resd 1
N resd 1
I resd 1
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
t26 resd 1
t27 resd 1
t29 resd 1
t30 resd 1
t31 resd 1
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
        mov ebx, 1
        mov ecx, 100
        cdq
        idiv ecx
        mov edx, eax
        mov [D], ecx<--
        mov [N], eax<--
        mov [I], ebx<--
        mov [t1], edx<--
        mov [t2], eax<--
etiq1:  mov eax, [t1]
        mov esi, [I]
        mov [A + 4*esi], eax
        mov ecx, [I]
        mov ebx, [A + 4*ecx]
        mov [t3], ebx
        mov edx, ebx
        imul edx, [D]
        mov edi, [N]
        sub edi, edx
       mov eax, edi
       mov [N], eax
       mov eax,[D]
       mov [t4], edx
       cdq
       mov ecx,[10]
       idiv ecx
       mov edx, [I]
       inc edx
       mov [t3], ebx
       mov ebx, edx
       cmp ebx, 3
       mov [D], eax<--
       mov [I], ebx<--
       mov [t5], edi<--
       mov [t6], eax<--
       mov [t7], edx<--
       jg etiq3
        jmp etiq1
etiq3:  mov eax, prompt2
        call print_string
        call read_int
        mov ebx, 1
        mov ecx, 100
        cdq
        idiv ecx
        mov edx, eax
        mov [D], ecx<--
        mov [N], eax<--
        mov [I], ebx<--
        mov [t8], edx<--
        mov [t9], eax<--
etiq4:  mov eax, [t8]
        mov esi, [I]
        mov [B + 4*esi], eax
        mov ecx, [I]
        mov ebx, [B + 4*ecx]
        mov [t10], ebx
        mov edx, ebx
        imul edx, [D]
        mov edi, [N]
        sub edi, edx
       mov eax, edi
       mov [N], eax
       mov eax,[D]
       mov [t11], edx
       cdq
       mov ecx,[10]
       idiv ecx
       mov edx, [I]
       inc edx
       mov [t10], ebx
       mov ebx, edx
       cmp ebx, 3
       mov [D], eax<--
       mov [I], ebx<--
       mov [t12], edi<--
       mov [t13], eax<--
       mov [t14], edx<--
       jg etiq6
        jmp etiq4
etiq6:  mov eax, 3
        mov ebx, 0
        mov [BEHEKO_GAIN], ebx<--
        mov [I], eax<--
etiq7:  mov ebx, [I]
        mov eax, [A + 4*ebx]
        mov [t15], eax
        mov ecx, [B + 4*ebx]
        mov [t16], ecx
        mov edx, ecx
        add edx, [BEHEKO_GAIN]
        cmp eax, edx
        mov [t15], eax<--
        mov [t16], ecx<--
        mov [t17], edx<--
        jge etiq10
        jmp etiq12
        mov ebx, [I]
        mov eax, [A + 4*ebx]
        mov [t19], eax
        mov ecx, [B + 4*ebx]
        mov [t20], ecx
        mov edx, ecx
        add edx, [BEHEKO_GAIN]
        mov esi, eax
        sub esi, edx
        mov edi, esi
        mov [t18], edi<--
        mov [t19], eax<--
        mov [t20], ecx<--
        mov [t21], edx<--
        mov [t22], esi<--
etiq10: mov eax, [t18]
        mov esi, [I]
        mov [C + 4*esi], eax
        mov ebx, 0
        mov [BEHEKO_GAIN], ebx<--
        jmp etiq13
        mov ebx, [I]
        mov eax, [A + 4*ebx]
        mov [t24], eax
        mov ecx, 10
        add ecx, eax
       mov edx, [B + 4*ebx]
       mov [t26], edx
       mov esi, edx
       add esi, [BEHEKO_GAIN]
       mov edi, ecx
       sub edi, esi
       mov [t24], eax
       mov eax, L1
       mov [t23], eax<--
       mov [t26], edx<--
       mov [t27], esi<--
etiq12: mov eax, [t23]
        mov esi, [I]
        mov [C + 4*esi], eax
        mov ebx, 1
        mov [BEHEKO_GAIN], ebx<--
etiq13: mov eax, [I]
        dec eax
       mov ebx, eax
       cmp ebx, 0
       mov [I], ebx<--
       mov [t29], eax<--
       je etiq15
        jmp etiq7
etiq15: mov eax, prompt3
        call print_string
        mov ebx, 1
        mov [I], ebx<--
etiq16: mov ebx, [I]
        mov eax, [C + 4*ebx]
        mov [t30], eax
        call print_int
        mov ecx, ebx
        inc ecx
        mov ebx, ecx
        cmp ebx, 3
        mov [I], ebx<--
        mov [t30], eax<--
        mov [t31], ecx<--
        jg etiq18
        jmp etiq16
etiq18: popa
        mov eax, 0 ; retornar a C
        leave
        ret
