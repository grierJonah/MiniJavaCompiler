	.text
	.globl	_asm_main	# label for "main" program

_asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$100,%rdi	# Store 100 into register rdi
	subq	$13,%rdi	# Subtract 13 from register rdi

	call	_put

	movq	%rbp,%rsp
	popq	%rbp
	ret