package advancedtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AdvancedTree
{
	public static boolean showHidden = false;
	public static boolean showOnlyDirectories = false;
	public static int maxDepth = -1;

	AdvancedTree parent;
	String path;
	List<AdvancedTree> childs;
	boolean hidden = false;

	public AdvancedTree(String path, AdvancedTree parent)
	{
		this.path = path;
		this.parent = parent;
		childs = null;
		if (path.length() > 1 && path.startsWith("."))
			hidden = true;
		if (parent != null)
			parent.addChild(this);
	}

	public AdvancedTree clone()
	{
		AdvancedTree s = new AdvancedTree(path, parent);
		s.hidden = hidden;
		s.childs = new ArrayList<>(childs);
		return s;
	}

	public boolean isHidden()
	{
		return hidden;
	}

	public boolean isDirectory()
	{
		return childs != null && childs.size() > 0;
	}

	public boolean isLeaf()
	{
		return childs == null;
	}

	public void addChild(AdvancedTree child)
	{
		if (childs == null)
			childs = new ArrayList<AdvancedTree>();
		if (!childs.contains(child))
			childs.add(child);
	}

	public AdvancedTree hasChild(AdvancedTree s)
	{
		if (childs == null)
			return s;
		for (AdvancedTree child : childs)
		{
			if (child.equals(s))
				return child;
		}
		return s;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof String)
			return path.equals(obj);

		if (!(obj instanceof AdvancedTree))
			return false;

		AdvancedTree s = (AdvancedTree) obj;

		if ((s.parent != null && !s.parent.equals(parent)) //
				|| (parent != null && !parent.equals(s.parent)))
			return false;

		if ((s.path != null && !s.path.equals(path)) //
				|| (path != null && !path.equals(s.path)))
			return false;

		return true;
	}

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		try
		{

			String S = in.nextLine();
			String F = in.nextLine();
			int N = in.nextInt();
			in.nextLine();

			String[] flags = F != null ? F.split(",") : new String[0];
			for (int i = 0; i < flags.length; i++)
			{
				String flag = flags[i];
				// System.err.println(flag);
				if ("".equals(flags[i]))
					continue;
				if ("-a".equals(flag))
					showHidden = true;
				if ("-d".equals(flag))
					showOnlyDirectories = true;
				if (flag.startsWith("-L") && flag.contains(" "))
					maxDepth = Integer.parseInt(flag.substring(flag.indexOf(" ") + 1));
			}

			AdvancedTree root = null;
			for (int i = 0; i < N; i++)
			{
				String line = in.nextLine();
				String[] nodes = line.split("/");
				AdvancedTree lastSolution = null;
				for (String n : nodes)
				{
					AdvancedTree s = new AdvancedTree(n, lastSolution);
					if (lastSolution != null)
						s = lastSolution.hasChild(s);

					if (root == null)
						root = s;
					else if (root != null && root.equals(s))
						s = root;
					lastSolution = s;
				}
			}

			String[] startPathArray = S.split("/");
			AdvancedTree lastSolution = (AdvancedTree) root.clone();
			for (String n : startPathArray)
			{
				AdvancedTree s;
				if (lastSolution.equals(n))
					s = root;
				else
					s = new AdvancedTree(n, lastSolution);

				s = lastSolution.hasChild(s);
				lastSolution = s;
			}

			if (!exist(root, lastSolution) || lastSolution.childs == null)
			{
				System.out.println(S + " [error opening dir]");
				System.out.println();
				if (showOnlyDirectories)
					System.out.println("0 directories");
				else
					System.out.println("0 directories, 0 files");
				return;
			}
			else
			{
				lastSolution.parent = null;
				lastSolution.path = S;
				printTree(lastSolution, 0, "");
			}

			System.out.println();

			int directories = getDirectories(lastSolution, 0) - 1;

			if (directories == 1)
				System.out.print(directories + " directory");
			else
				System.out.print(directories + " directories");
			if (!showOnlyDirectories)
			{
				int files = getFiles(lastSolution, 0);
				if (files == 1)
					System.out.print(", " + files + " file");
				else if (files > 1)
					System.out.print(", " + files + " files");
			}
		}
		finally
		{
			in.close();
		}
	}

	private static boolean exist(AdvancedTree s, AdvancedTree path)
	{
		boolean exist = false;

		if (s.equals(path))
			return true;

		if (s.childs != null)
		{
			for (AdvancedTree child : s.childs)
			{
				if (child.equals(path))
					return true;
			}
		}

		return exist;
	}

	private static void printTree(AdvancedTree s, int depth, String line)
	{
		if (!showHidden && s.isHidden())
			return;

		if (showOnlyDirectories && s.isLeaf())
			return;

		if (maxDepth != -1 && depth > maxDepth)
			return;

		if (s.parent != null)
		{
			String path = s.path;
			if (isLastChild(s))
			{
				System.out.println(line + "`-- " + path);
				line += "    ";
			}
			else
			{
				System.out.println(line + "|-- " + path);
				line += "|   ";
			}
		}
		else
		{
			System.out.println(s.path);
		}

		if (s.childs == null)
			return;

		Collections.sort(s.childs, new Comparator<AdvancedTree>()
		{
			public int compare(AdvancedTree s1, AdvancedTree s2)
			{
				String o1 = s1.path;
				String o2 = s2.path;
				o1 = getString(o1);
				o2 = getString(o2);

				return o1.compareTo(o2);
			};

			private String getString(String o)
			{
				if (o == null)
					return o;
				if (o.startsWith("."))
					o = o.substring(1);
				return o.toLowerCase();
			}
		});

		if (s.childs != null)
		{
			for (AdvancedTree child : s.childs)
			{
				printTree(child, depth + 1, line);
			}
		}
	}

	private static boolean isLastChild(AdvancedTree s)
	{
		List<AdvancedTree> childs2 = s.parent.childs;
		int index = childs2.indexOf(s);
		boolean result = index == childs2.size() - 1;
		if (!result && (showOnlyDirectories || !showHidden))
		{
			List<AdvancedTree> directoryChilds = new ArrayList<>();
			for (AdvancedTree solution : childs2)
			{
				if (showOnlyDirectories && solution.childs == null)
					continue;
				if (!showHidden && solution.hidden)
					continue;
				directoryChilds.add(solution);
			}

			index = directoryChilds.indexOf(s);

			result = index == directoryChilds.size() - 1;
		}
		return result;
	}

	private static int getDirectories(AdvancedTree s, int depth)
	{
		if (!showHidden && s.hidden)
			return 0;
		if (maxDepth != -1 && depth > maxDepth)
			return 0;
		int directories = 1;
		if (s.isLeaf() || s.childs == null)
			return 0;
		for (AdvancedTree child : s.childs)
			directories += getDirectories(child, depth + 1);
		return directories;
	}

	private static int getFiles(AdvancedTree s, int depth)
	{
		if (!showHidden && s.hidden)
			return 0;
		if (maxDepth != -1 && depth > maxDepth)
			return 0;
		int files = 0;
		if (s.childs == null)
			return 1;
		for (AdvancedTree child : s.childs)
			files += getFiles(child, depth + 1);
		return files;
	}
}