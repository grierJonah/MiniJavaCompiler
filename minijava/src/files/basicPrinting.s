	.text
	.globl	_asm_main
_asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$5,%rdi
	callq	_put

	addq	$5,%rdi

	movq	$5,%rdi

	movq	%rsp,%rbp
	popq	%rbp
	ret