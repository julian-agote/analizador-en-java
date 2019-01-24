.title hirumax4
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
       jmp etiq8
etiq2: sub r5, r2, r1
       bgt r5, etiq4
       jmp etiq5
etiq4: mov r6, r2
etiq5: sub r7, r2, r1
       bls r7, etiq7
       jmp etiq8
etiq7: mov r6, r1
etiq8: sub r8, r2, r3
       bls r8, etiq10
       jmp etiq16
etiq10:sub r9, r3, r1
       bgt r9, etiq12
       jmp etiq13
etiq12:mov r6, r3
etiq13:sub r10, r3, r1
       bls r10, etiq15
       jmp etiq16
etiq15:mov r6, r1
etiq16:st r1, C
       mov r1, r6
       out
       retm
.endp main
.end
