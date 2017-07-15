package microassembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MicroAssembly
{
	enum Instructions
	{
		ADD, SUB, MOV, JNE;
	}

	enum Registers
	{
		A, B, C, D, IMM
	}

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		try
		{
			int a = in.nextInt();
			int b = in.nextInt();
			int c = in.nextInt();
			int d = in.nextInt();
			int n = in.nextInt();
			in.nextLine();

			Register regA = new Register(Registers.A, a);
			Register regB = new Register(Registers.B, b);
			Register regC = new Register(Registers.C, c);
			Register regD = new Register(Registers.D, d);

			List<Instruction> instructions = new ArrayList<>();
			for (int i = 0; i < n; i++)
			{
				String line = in.nextLine();
				String[] params = line.split(" ");
				Instructions ins = getInstructions(params[0]);
				Register dest = getRegister(regA, regB, regC, regD, params[1]);
				Register srcImm1 = getRegister(regA, regB, regC, regD, params[2]);
				Register srcImm2 = params.length == 3 ? null : getRegister(regA, regB, regC, regD, params[3]);
				Instruction instruction = new Instruction(ins, dest, srcImm1, srcImm2);
				instructions.add(instruction);
			}

			for (int i = 0; i < n; i++)
			{
				Instruction instruction = instructions.get(i);

				int result = instruction.execute();
				if (instruction.instruction.equals(Instructions.JNE) && result != -2)
					i = result;
			}

			System.out.println(regA + " " + regB + " " + regC + " " + regD);
		}
		finally
		{
			in.close();
		}
	}

	private static Register getRegister(Register regA, Register regB, Register regC, Register regD, String reg)
	{
		switch (reg)
		{
			case "a":
				return regA;
			case "b":
				return regB;
			case "c":
				return regC;
			case "d":
				return regD;
			default:
				Integer i = Integer.valueOf(reg);
				int value = i.intValue();
				return new Register(null, value);
		}
	}

	private static Instructions getInstructions(String s)
	{
		switch (s)
		{
			case "ADD":
				return Instructions.ADD;
			case "SUB":
				return Instructions.SUB;
			case "MOV":
				return Instructions.MOV;
			case "JNE":
				return Instructions.JNE;
			default:
				break;
		}
		return null;
	}

	public static class Instruction
	{
		Instructions instruction;
		Register dest;
		Register srcImm1;
		Register srcImm2;

		public Instruction(Instructions instruction, Register dest, Register srcImm1, Register srcImm2)
		{
			this.instruction = instruction;
			this.dest = dest;
			this.srcImm1 = srcImm1;
			this.srcImm2 = srcImm2;
		}

		public int execute()
		{
			if (Instructions.ADD.equals(instruction))
				add(dest, srcImm1, srcImm2);
			else if (Instructions.SUB.equals(instruction))
				sub(dest, srcImm1, srcImm2);
			else if (Instructions.MOV.equals(instruction))
				move(dest, srcImm1);
			else if (Instructions.JNE.equals(instruction))
				return jne(dest, srcImm1, srcImm2);
			return -1;
		}

		public void move(Register dest, Register v)
		{
			dest.setValue(v.getValue());
		}

		public void add(Register dest, Register v1, Register v2)
		{
			dest.setValue(v1.getValue() + v2.getValue());
		}

		public void sub(Register dest, Register v1, Register v2)
		{
			dest.setValue(v1.getValue() - v2.getValue());
		}

		public int jne(Register dest, Register v1, Register v2)
		{
			if (v1.getValue() != v2.getValue())
				return dest.getValue() - 1;
			return -2;
		}
	}

	public static class Register
	{
		int value;
		Registers reg;

		public Register(Registers reg, int value)
		{
			this.reg = reg;
			this.value = value;
		}

		public void setValue(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}

		@Override
		public String toString()
		{
			return "" + value;
		}
	}
}
