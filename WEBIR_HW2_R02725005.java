import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class WEBIR_HW2_R02725005 {
	
	public static void main(String args[]){ //args==>[0]:d,[1]:e,[2]:output,[3]:input
		String filePath=args[3];
		int totalNode=0;
		double[] score=null;
		double[] temp_score=null;
		int[] out_node_num=null; 
		double d=Double.parseDouble(args[0]);
		double e=Double.parseDouble(args[1]);
		HashMap<Integer, HashSet<Integer>> node_graph=new HashMap<Integer, HashSet<Integer>>(); 
		HashSet<Integer> noOutNode_graph=new HashSet<Integer>(); 

		
		
		try{
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			String result[]=line.split(" ");
			totalNode=Integer.parseInt(result[1]);
			
			
			//initial score for each node
			score=new double[totalNode];
			for(int i=0;i<totalNode;i++){
				score[i]=1;
			}
			
		
			line=br.readLine();
			
			//construct the node graph
			out_node_num=new int[totalNode];
			System.out.println("Start constructing the graph...");
			int last_node_id=0;
			while(line!=null){
				String out_node[]=line.split(" ");
				String node_info[]=out_node[0].split(":");
				int from_node=Integer.parseInt(node_info[0]);
				int num_of_out_node=Integer.parseInt(node_info[1]);
				out_node_num[from_node-1]=num_of_out_node;
				
//				System.out.println("==================="+from_node);
				Integer[] to_nodes=new Integer[num_of_out_node];
				for(int j=1;j<=to_nodes.length;j++){
					int to_node_id=Integer.parseInt(out_node[j]);
//					System.out.println(to_node_id);
					if(node_graph.containsKey(to_node_id)){ // if node is not first met
						HashSet<Integer> from_nodes = node_graph.get(to_node_id);
						from_nodes.add(from_node);
						node_graph.put(to_node_id, from_nodes);
						
					}
					else{ // if node is first met
						HashSet<Integer> from_nodes = new HashSet<Integer>();
						from_nodes.add(from_node);
						node_graph.put(to_node_id, from_nodes);
					}
					
				
					
					
//					temp_out_node[j-1]=Integer.parseInt(out_node[j]);
				}
//				node_graph.put(from_node, temp_out_node);
				
				
				if(from_node>last_node_id){
					for(int x=last_node_id+1;x<from_node;x++){
						out_node_num[x-1]=totalNode;
						noOutNode_graph.add(x);
						
					}
					
				}
				
				last_node_id=from_node;
			
				line=br.readLine();
			}
			
			for(int x=last_node_id+1;x<=totalNode;x++){
				noOutNode_graph.add(x);
				out_node_num[x-1]=totalNode;
			}
			
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
//		for(int key:node_graph.keySet()){
//			System.out.println(key);
//		}
//		if(node_graph.get(1)==null){
//			System.out.println("nobody wants meQQ");
//		}
		
		
		//pageRank
		System.out.println("Start ranking pages...");
		
		double score_sum_of_noOutNode=(double)noOutNode_graph.size()/totalNode;
		
		double l2_norm=1;
		while(l2_norm>e){
			l2_norm=0;
			temp_score=new double[totalNode];
			
			
			for(int i=0;i<totalNode;i++){
				
				double score_sum=0;
				temp_score[i]=0;
				HashSet<Integer> from_nodes = node_graph.get((i+1));
				if(from_nodes!=null){
					Iterator from_node=from_nodes.iterator();
					while (from_node.hasNext()){
						int from_node_id=(int) from_node.next();
//						System.out.println(from_node_id);
						score_sum+=score[from_node_id-1]/out_node_num[from_node_id-1];
					
					}
				}
				
				score_sum+=score_sum_of_noOutNode;
				
				temp_score[i]=(1-d)+(d*score_sum);
				l2_norm+=Math.abs(temp_score[i]-score[i])*Math.abs(temp_score[i]-score[i]);

				
				
			}
			
			l2_norm=Math.sqrt(l2_norm);
//			System.out.println("================>"+l2_norm);
			
			//update node score
			System.arraycopy(temp_score, 0, score, 0, totalNode);
			score_sum_of_noOutNode=0;
			Iterator noOut_node=noOutNode_graph.iterator();
			while(noOut_node.hasNext()){
				int noOut_node_id=(int) noOut_node.next();
				score_sum_of_noOutNode+=(double)score[noOut_node_id-1]/(double)out_node_num[noOut_node_id-1];
			}
			
//			System.out.println(score_sum_of_noOutNode);
			
			
		}
		
		//output result
		System.out.println("Starts outputing the result...");
		try{
			FileWriter fw=new FileWriter(args[2]);
			for(int i=0;i<totalNode;i++){
				fw.append((i+1)+":"+score[i]+"\n");
			}
			
			fw.close();
			
			
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		
		System.out.println("DONE.");
		
		
		
	}

}
