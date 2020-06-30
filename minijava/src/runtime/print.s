	.text
	.globl	_asm_main	# label for "main" program

_asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$5,%rdi
	callq	_put

	movq	%rbp,%rsp
	popq	%rbp
	ret