	.text
	.globl	_asm_main

_asm_main:
	pushq	%rbp		# Prologue
	movq	%rsp,%rbp

	movq	$15,%rdi	# Store 15 into register rdi
	addq	$30,%rdi	# Add 30 to 15

	callq	_put		# Print 45

	movq	%rbp,%rsp	# epilogue
	popq	%rbp
	ret