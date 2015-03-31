package vbst;

import java.io.File;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *	<p>
 *	VisualBST constructs a Binary Search Tree while displaying the tree (graphically with <a href = "http://www.graphviz.org/"> Graphviz </a>) as you create it. It saves the graph as a .png.
 *
 *	Here is an example:
 *	</p>
 *
 *	<img src = "../../resources/out.png">
 *
 *	@author Peter Tran
 */
public class VisualBST
{
	private final Node root;
	private String graph;
	private File file;
	private PrintWriter writer;
	private Runtime runtime;

	// helper Node class
	private class Node
	{
		private int value;
		private Node left, right, parent;

		private Node(int value)
		{
			this.value = value;
			this.left = null;
			this.right = null;
		}
	}

	/**
	 *	Constructs the empty VisualBST.
	 */
	public VisualBST()
	{
		this.root = null;
	}

	/**
	 *	Adds Item x to the VisualBST. Also generates a .png of the Visual BST as each element is added.
	 */
	public void add(int x)
	{
		if (this.root == null)
		{
			this.root = new Node(x);

			graph = "digraph G {\n" + x + "\n}";
			this.generate();

			try
			{
				runtime = Runtime.getRuntime();
				runtime.exec("xdg-open out.png");
			} catch(Exception e) {System.out.println("ERROR");}

			graph = "digraph G {\n";
		}

		else
			add(x, this.root);
	}

	// recursive helper for add
	private void add(int x, Node node)
	{
		if (x < node.value)
		{
			if (node.left == null)
			{
				node.left = new Node(x);

				graph = graph + (node.value + " -> " + node.left.value + ";\n");
				this.generate();
			}

			else
				add(x, node.left);
		}

		if (x > node.value)
		{
			if (node.right == null)
			{
				node.right = new Node(x);

				graph = graph + (node.value + " -> " + node.right.value + ";\n");
				this.generate();
			}

			else
				add(x, node.right);
		}
	}

	// generate creates the .png using Graphviz
	private void generate()
	{
		graph = graph + "}";
		file = new File("vbst/graph.dot");

		try
		{
			writer = new PrintWriter(file);
			writer.print(graph);
			writer.close();
		} catch(Exception e) {System.out.println("ERROR");}

		try
		{
			runtime = Runtime.getRuntime();
			runtime.exec("dot -Tpng " + file + " -o out.png").waitFor();
			file.delete();
		} catch(Exception e) {System.out.println("ERROR");}

		graph = graph.replaceAll("}", "");
	}

	/**
	 *	Prints the elements of the tree in order.
	 */
	public void print()
	{
		print(this.root);
	}

	// recursive helper for print
	private void print(Node node)
	{
		if (node.left != null)
			print(node.left);

		System.out.println(node.value);

		if (node.right != null)
			print(node.right);
	}

	// 5 3 18 1 8 24 13 19
	/**
	 * Simple client that constructs a tree from command line inputs.
	 */
	public static void main(String[] args)
	{
		VisualBST bst = new VisualBST();

		Scanner in = new Scanner(new InputStreamReader(System.in));
		System.out.println("\nBegin entering the values you want to construct your binary search tree out of. When finished, enter an empty line.\n");

		String line = "line";	
		while (!line.equals(""))
		{
			line = in.nextLine();

			if (!line.equals(""))
				bst.add(Integer.parseInt(line));
		}
	}
}
