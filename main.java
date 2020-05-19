import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
public class main //DVR Java Program By NCUE CSIE Grade 108 S0454019 Yu Hao Wu
{
	 static int graph[][];
	 static int via[][];
	 static int rt[][];
	 static int v;
	 static int e;
	 static String nodes[];
	
	 public static void main(String args[]) throws IOException
	{
		  menu();	//Run the menu function
	}
	
	static void menu() throws IOException{	//Menu function
		Scanner scn = new Scanner(System.in);
		int input;
		System.out.println("What do you want to do?");
		System.out.print("\t1.Network Initialization\n\t2.Link cost update\n\t3.Exit\nSelect: ");
		input = scn.nextInt();	//Read the input of the options
		if(input == 1){	//If choose the first option then run the DVR algorithm and go back to menu
			DVR();
			menu();
		}
		else if(input == 2){	//If choose the second option then update the DVR algorithm and go back to menu
			DVR_update();
			menu();
		}
		else if(input == 3){	//If choose the third option then quit the program
			System.out.println("程式結束!");
			System.exit(0);
		}
		else{	//If input is in wrong form then the program will ask you to retry
			System.out.println("Wrong Input! Plz retry!\n\n");
			menu();
		}
	}
	
	static void DVR() throws IOException{	//The function of DVR
		int edges = 0, src, dest;
		String filename, tmp;
		Scanner scn = new Scanner(System.in);
		System.out.println("Input the file name (ex. C:\\XXX.txt): ");
		filename = scn.nextLine();	//Save the input file name
		  FileReader fr = new FileReader(filename);	//open the file
		  BufferedReader br = new BufferedReader(fr);
		  //first line of file
		  tmp = br.readLine();	//read the first line and spilt the string to save nodes
		  nodes = tmp.split(" ");
		  v = nodes.length;	//Variable v is the number of nodes
		  //==================
		  //the cost in the file
		  LinkedList<String> edge_list = new LinkedList<String>();	//Use linked-list to save other lines in the file and compute the number of edges
		  while((tmp = br.readLine()) != null && tmp.length() > 0){
			  edges++;
			  edge_list.add(tmp);
		  }
		  //====================
		  e = edges;
		  graph = new int[v][v];	//According to the number of nodes, declare the array for graph, via and routing table. 
		  via = new int[v][v];
		  rt = new int[v][v];
		  for(int i = 0; i < v; i++)	//Initial the graph
			   for(int j = 0; j < v; j++)
			   {
				    if(i == j)
				    	graph[i][j] = 0;
				    else
				    	graph[i][j] = 9999;
			   }
		  
		  for(int i = 0; i < e; i++)	//Get the string in the linked-list and start to process the data
		  {
			  tmp = edge_list.get(i);
			  for(src = 0; src < v;src++){
				  if((tmp.charAt(0)+"").equals(nodes[src])){
					  break;
				  }
			  }
			   int s = src;
			   for(dest = 0; dest < v;dest++){
					 if((tmp.charAt(2)+"").equals(nodes[dest])){
						 break;
					 }
			   }
			   int d = dest;
			   int c = (Integer.parseInt(tmp.substring(4)) == -1) ? 9999 : Integer.parseInt(tmp.substring(4));
			   graph[s][d] = c;
			   graph[d][s] = c;
		  }
		  
		  dvr_calc_disp("The initial Routing Tables are: ");	//Print the routing table for the file
		  System.out.println();
		  fr.close();	//Close the file
	 }
	
	static void DVR_update() throws IOException{	//The function of DVR_update
		int edges = 0, src, dest;
		String filename, tmp;
		Scanner scn = new Scanner(System.in);
		System.out.println("Input the file name (ex. C:/XXX.txt): ");
		filename = scn.nextLine();	//Read the input file name
		  FileReader fr = new FileReader(filename);	//Open the file and read
		  BufferedReader br = new BufferedReader(fr);
		  //first line of file
		  tmp = br.readLine();
		  nodes = tmp.split(" ");	//Spilt the first string to save nodes
		  v = nodes.length;
		  //==================
		  //the cost in the file
		  LinkedList<String> edge_list = new LinkedList<String>();	//Use a new linked-list to save other lines of the file
		  while((tmp = br.readLine()) != null && tmp.length() > 0){
			  edges++;
			  edge_list.add(tmp);
		  }
		  //====================
		  
		  e = edges;
		  for(int i = 0; i < e; i++)	//Get the data from the linked-list and start to process
		  {
			  tmp = edge_list.get(i);
			  for(src = 0; src < v;src++){
				  if((tmp.charAt(0)+"").equals(nodes[src])){
					  break;
				  }
			  }
			   int s = src;
			   for(dest = 0; dest < v;dest++){
					 if((tmp.charAt(2)+"").equals(nodes[dest])){
						 break;
					 }
			   }
			   int d = dest;
			   int c = (Integer.parseInt(tmp.substring(4)) == -1) ? 9999 : Integer.parseInt(tmp.substring(4));
			   if(graph[s][d] != c){
				   graph[s][d] = c;
				   System.out.println("原本 node " + nodes[s] + "到 node " + nodes[d] + " 的 link cost 為 :" + graph[d][s] + " 更新為 : " + ((c == 9999)?-1:c));
				   graph[d][s] = c;
			   }
		  }
		  update_from_file("The new Routing Tables are: ");	//Print the updated routing table
		  fr.close();
	}
	 
	 static void dvr_calc_disp(String message)	//The function of calling calculate function and print the table
	 {
		  System.out.println();
		  init_tables();
		  update_tables();
		  System.out.println(message);
		  print_tables();
		  System.out.println();
		  print_rtable();
		  System.out.println();
	 }
	 
	 static void update_table(int source)	//The function of update table
	 {
		  for(int i = 0; i < v; i++)
		  {
			   if(graph[source][i] != 9999)	//If two nodes have a edge connected then calaulate the distance
			   {
				    int dist = graph[source][i];
				    for(int j = 0; j < v; j++)
				    {
					     int inter_dist = rt[i][j];
					     if(via[i][j] == source)
					    	 inter_dist = 9999;
					     if(dist + inter_dist < rt[source][j])
					     {
						     rt[source][j] = dist + inter_dist;
						     via[source][j] = i;
					     }
				    }
			   }
		  }
	 }
	 
	 static void update_from_file(String message){	//The second version of dvr_calc_disp function to process the data
		  update_tables();
		  System.out.println(message);
		  print_tables();
		  System.out.println();
		  print_rtable();
		  System.out.println();
	 }
	 
	 static void update_tables()	//recursive the update table.
	 {
		  int k = 0;
		  for(int i = 0; i < 4*v; i++)
		  {
			  update_table(k);
			  k++;
			  if(k == v)
				  k = 0;
		  }
	 }
	 
	 static void init_tables()	//initial the table.
	 {
		  for(int i = 0; i < v; i++)
		  {
			   for(int j = 0; j < v; j++)
			   {
				    if(i == j)	//if i is equal to j (from itself to itself) then set cost to 0, via itself.
				    {
					     rt[i][j] = 0;
					     via[i][j] = i;
				    }
				    else	//else set the cost to 9999, via 100.
				    {
					     rt[i][j] = 9999;
					     via[i][j] = 100;
				    }
			   }
		  }
	 }
	 
	 static void print_tables()	//print the table.
	 {
		 int tmp;
		 for(int i = 0; i < v; i++){
			  System.out.print("\t" + nodes[i]);
		  }
		  System.out.println();
		  for(int i = 0; i < v + 1; i++){
			  System.out.print("-------");
		  }
		  System.out.println();
		  for(int i = 0; i < v; i++)
		  {
			  System.out.print(nodes[i] + "|");
			   for(int j = 0; j < v; j++)
			   {
				   tmp = (rt[i][j] == 9999) ? -1 : rt[i][j];
				   System.out.print("\t" + tmp);	//each cost in the table.
			   }
			   System.out.println();
		  }
	 }
	 
	 static void print_rtable()	//print the forwarding table.
	 {
		 int tmp;
		  for(int i = 0; i < v; i++)
		  {
			  System.out.println("Forwarding table for node \"" + nodes[i] + "\"");
			  System.out.println("Dest.\tNext.\tCost");
			   for(int j = 0; j < v; j++)
			   {
				   if(i == j) continue;
				   tmp = (rt[i][j] == 9999) ? -1 : rt[i][j];
				   System.out.println(nodes[j] + "\t" + nodes[via[i][j]] + "\t" + tmp);	//each cost in the table.
			   }
			   System.out.println();
		  }
	 }
}