	.text
	.globl	_asm_main
_asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$300,%rdi

	addq	$40,%rdi
	callq	_put

	movq	$3,%rdi

	movq	%rsp,%rbp
	popq	%rbp
	ret