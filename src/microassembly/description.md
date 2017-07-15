# [Micro Assembly](https://www.codingame.com/training/community/micro-assembly) 
(The description below is copied from the source)

The task is to write an interpreter for a very simplistic assembly language and print the four register values after the instructions have been processed.

**_Explanations_**:  
**_a, b, c, d_**: registers containing integer values  
**_DEST_**: write the operation result to this register  
**_SRC_**: read operand value from this register  
**_IMM_**: immediate/integer value  
**_SRC|IMM_** means that the operand can be either a register or an immediate value.

Command and operands are always separated by a blank.  
The program starts with the first instruction, iterates through all instructions and ends when the last instruction was processed.  
Only valid input is given and there are no endless loops or over-/underflows to be taken care of.

List of defined assembly instructions:  
MOV DEST SRC|IMM  
copies register or immediate value to destination register  
**_Example_**: MOV a 3 => a = 3

ADD DEST SRC|IMM SRC|IMM  
add two register or immediate values and store the sum in destination register  
**_Example_**: ADD b c d => b = c + d

SUB DEST SRC|IMM SRC|IMM  
subtracts the third value from the second and stores the result in destination register  
**_Example_**: SUB d d 2 => d = d - 2

JNE IMM SRC SRC|IMM  
jumps to instruction number IMM (zero-based) if the other two values are not equal  
**_Example_**: JNE 0 a 0 => continue execution at line 0 if a is not zero

**_Full example_**:

(line 0) MOV a 3      # a = 3  
(line 1) ADD a a -1   # a = a + (-1)  
(line 2) JNE 1 a 1    # jump to line 1 if a != 1  

**_Program execution_**:  
0: a = 3  
1: a = a + (-1) = 3 + (-1) = 2  
2: a != 1 -> jump to line 1  
1: a = a + (-1) = 2 + (-1) = 1  
2: a == 1 -> don't jump

Program finished, register a now contains value 1