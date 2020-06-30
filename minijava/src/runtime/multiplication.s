	.text
	.globl	_asm_main	# label for "main" program

_asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$50,%rax	# Multiplication requires register rax
	movq	$12,%rdi	# Store 12 in rdi
	imul	%rax,%rdi	# multiply both registers
	imul	$3,%rdi		# extra fun
	imul	$2,%rdi		# extra fun (2)

	callq	_put

	popq	%rdi		# pop register (dont pop to save value later)
	popq	%rax		# pop register 
	movq	%rbp,%rsp	# epilogue
	popq	%rbp
	ret