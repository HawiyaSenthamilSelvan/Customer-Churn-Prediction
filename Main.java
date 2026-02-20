import java.util.*; 
public class Main { 
 static boolean detectCycleBFS(int V, ArrayList<ArrayList<Integer>> graph) {  int[] indegree = new int[V]; 
 for (int i = 0; i < V; i++) 
 for (int v : graph.get(i)) 
 indegree[v]++; 
 Queue<Integer> q = new LinkedList<>(); 
 for (int i = 0; i < V; i++) 
 if (indegree[i] == 0) 
 q.add(i); 
 int count = 0; 
 while (!q.isEmpty()) { 
 int u = q.poll(); 
 count++; 
 for (int v : graph.get(u)) { 
 indegree[v]--; 
 if (indegree[v] == 0) 
 q.add(v); 
 } 
 } 
 return count != V;  
 } 
 static void dfs1(int v, boolean[] visited, Stack<Integer> stack,ArrayList<ArrayList<Integer>> graph) { 
     visited[v] = true; 
 for (int n : graph.get(v)) 
 if (!visited[n]) 
 dfs1(n, visited, stack, graph); 
 stack.push(v);
 } 
 static void dfs2(int v, boolean[] visited, 
 ArrayList<ArrayList<Integer>> revGraph) {  visited[v] = true; 
 System.out.print(v + " "); 
 for (int n : revGraph.get(v)) 
 if (!visited[n]) 
 dfs2(n, visited, revGraph); 
 } 
 static void findSCC(int V, ArrayList<ArrayList<Integer>> graph) {  Stack<Integer> stack = new Stack<>(); 
 boolean[] visited = new boolean[V]; 
 for (int i = 0; i < V; i++) 
 if (!visited[i]) 
 dfs1(i, visited, stack, graph) ;
 ArrayList<ArrayList<Integer>> revGraph = new ArrayList<>();  for (int i = 0; i < V; i++) 
 revGraph.add(new ArrayList<>()); 
 for (int i = 0; i < V; i++) 
 for (int v : graph.get(i)) 
 revGraph.get(v).add(i); 
 Arrays.fill(visited, false); 
 System.out.println("Strongly Connected Components:");  while (!stack.isEmpty()) { 
 int v = stack.pop(); 
 if (!visited[v]) { 
 dfs2(v, visited, revGraph); 
 System.out.println(); 
 }
 } 
 } 
 public static void main(String[] args) { 
 Scanner sc = new Scanner(System.in); 
 System.out.println("AAKSHAYA R H004"); 
 System.out.print("Enter number of vertices: "); 
 int V = sc.nextInt(); 
 System.out.print("Enter number of edges: "); 
 int E = sc.nextInt() ;
 ArrayList<ArrayList<Integer>> graph = new ArrayList<>();  for (int i = 0; i < V; i++) 
 graph.add(new ArrayList<>()); 
 System.out.println("Enter edges (source destination):");  for (int i = 0; i < E; i++) { 
 int u = sc.nextInt(); 
 int v = sc.nextInt(); 
 graph.get(u).add(v); 
 } 
 if (detectCycleBFS(V, graph)) 
 System.out.println("Cycle detected in the directed graph (BFS)");  else 
 System.out.println("No cycle detected in the directed graph (BFS)");  findSCC(V, graph); 
 sc.close(); 
 } 
}