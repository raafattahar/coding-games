package advancedtree;
import java.util.ArrayList;
import java.util.List;

public class Node
{
	String node;
	Node parent;
	List<Node> childs;

	public Node(Node parent, String node)
	{
		this.parent = parent;
		this.node = node;
		childs = new ArrayList<>();
	}

	public void addChild(Node n)
	{
		if (!childs.contains(n))
			childs.add(n);
	}

	public boolean isLeaf()
	{
		return childs.isEmpty();
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Node))
			return false;

		if (this == o)
			return true;

		if (((Node) o).parent == parent && ((Node) o).node == node)
			return true;

		return false;
	}
}
