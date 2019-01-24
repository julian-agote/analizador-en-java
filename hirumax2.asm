.title hirumax2
MAX: .word 1;
C: .word 1;
B: .word 1;
A: .word 1;
.proc main
       in
       st r1, A
       in
       st r1, B
       in
       ld r2, A
       ld r3, B
       sub r4, r2, r3
       bgt r4, etiq2
       jmp etiq5
etiq2: sub r5, r2, r1
       bgt r5, etiq4
       jmp etiq5
etiq4: mov r6, r2
etiq5: sub r7, r3, r2
       bgt r7, etiq7
       jmp etiq10
etiq7: sub r8, r3, r1
       bgt r8, etiq9
       jmp etiq10
etiq9: mov r6, r3
etiq10:sub r9, r1, r2
       bgt r9, etiq12
       jmp etiq15
etiq12:sub r10, r1, r3
       bgt r10, etiq14
       jmp etiq15
etiq14:mov r6, r1
etiq15:st r1, C
       mov r1, r6
       out
       retm
.endp main
.end
