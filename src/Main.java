import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.event.UndoableEditListener;

import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
	
	public void writeFile(String filename) {  
        String str = "this  is a program"; // 要写入的内容  
        try {  
            FileOutputStream out = new FileOutputStream(str); // 输出文件路径  
            out.write(str.getBytes());  
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	public static void visitmap( Map<String, Double> umap){
		for (Map.Entry<String, Double> entry : umap.entrySet()) {  
			 String key = entry.getKey().toString();  
			 String value = entry.getValue().toString();  
			 //System.out.println("key=" + key + " weight=" + value);  
		}  
	}
	
	public static void visitUmap( Map<String, Unode> umap){
		for (Map.Entry<String, Unode> entry : umap.entrySet()) {  
			 String key = entry.getKey().toString();  
			// System.out.println("node=" + key + "  value="+entry.getValue().getValue()+" weight=" +entry.getValue().getWeight());  
		}  
	}
	
	
	public static void visitset(Set<Integer> set){  //输出set中的值（即ut序号）
		for(Integer x:set)
			//System.out.print(x+" ");
		System.out.println("");
	}
	
	public static void printUnode(ArrayList<Unode> ulist,double  bweight[]){  //样例输出属性值、权重、边值
		for(int i=0;i<ulist.size();i++)  
			System.out.println("第"+i+"行   value:"+ulist.get(i).getValue()+"   weight:"+ulist.get(i).getWeight()+
					"   bweight:"+bweight[i]);
	}
	
	public static TreeSet<Integer> selectSing(TreeSet<Integer>  set,Map<Integer, Double> umap,int len1){  
		//map存放Unode序号，选出前len1个单个节点，放入set中
		//System.out.println("选出"+len1+"个单个节点");
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(umap.entrySet());
	    Collections.sort(list, new MyComparator());  //根据权重进行降序排序
		for (int i = 0; i < len1; i++) {   //筛选出单个节点的中的前len1大的
	         Map.Entry<Integer, Double> map = list.get(i);
	         //System.out.println("第"+map.getKey()+"个:"+map.getValue());
	         
	         set.add(map.getKey());
	     }
		return set;
	}
	
	
	public static TreeSet<Integer> selectDou(ArrayList<Unode> ulist,TreeSet<Integer>  set,double  bweight[],int len2){  
		//map存放Unode序号，选出前len2个双个节点，放入set中
		//System.out.println("选出"+len2+"组双个节点");
		
		Map<Integer, Double> umap=new TreeMap<>();
		 for(int i=0;i<ulist.size()-1;i++){
			 double temp=ulist.get(i).getWeight()+bweight[i]+ulist.get(i+1).getWeight();  //计算两个节点以及边值的总权重
			 
			 umap.put(i, temp);
		 }
		 
		 List<Map.Entry<Integer, Double>> list2 = new ArrayList<Map.Entry<Integer, Double>>(umap.entrySet());
		 Collections.sort(list2, new MyComparator());
		    for (int i = 0; i < len2; i++) {   //筛选出双个个节点的中的前len2大的
		         Map.Entry<Integer, Double> map = list2.get(i);
		         //System.out.println("第"+map.getKey()+"个:"+map.getValue());
		       
		         set.add(map.getKey());
		         set.add(map.getKey()+1);
		     }
		 
		return set;
	}
	
/*	public static List<Map> CreaListInMap(){
		List<Map> lis = new ArrayList<Map>();
		for(Integer x:set){
			Map<Integer, Unode> map = new HashMap<Integer, Unode>();
		    Unode uu=ulist.get(x);
		    map.put(x, uu);
		    lis.add(map);
		}
		return lis;
	}*/
	
	public static void vistLis(List<Map> lis){
		for (int i=0;i<lis.size();i++) {  
			Map m=lis.get(i);
			Iterator<Map.Entry<Integer, Unode>> it = m.entrySet().iterator();  
			while (it.hasNext()) {  
				Map.Entry<Integer, Unode> entry = it.next();  
				System.out.println("序号："+entry.getKey() + " Unode value:" + entry.getValue().getValue()+
            		 " weight:"+entry.getValue().getWeight());  
			}
		}
	}
	
	public static Map<String, Unode> compact1(ArrayList<Unode> ulist,TreeSet<Integer>  set,double  bweight[],int pack){  //根据set进行第一步合并
		ArrayList<Integer> list=new ArrayList<Integer>();
		for(Integer x:set){
			list.add(x);
		}
		double nu_value=0;
		double nu_weight=0;
		String path="";
		Map<String, Unode> umap3 = new TreeMap<>();
		for(int i=0;i<list.size()-1;){
			if(nu_value+ulist.get(list.get(i)).getValue()<=pack){   //加上当前节点小于属性和
				nu_value+=ulist.get(list.get(i)).getValue();
				nu_weight+=ulist.get(list.get(i)).getWeight();
				path=list.get(i)+"";
				try {
					
					while(list.get(i)+1==list.get(i+1)&&nu_value+ulist.get(list.get(i+1)).getValue()<=pack){  //如果当前节点和下一个节点连续，并且属性不超过
						
						nu_value+=ulist.get(list.get(i+1)).getValue();
						nu_weight+=ulist.get(list.get(i+1)).getWeight()+bweight[list.get(i)];
						path=path+"-"+list.get(i+1);
						i++;
						if(i==list.size()-1){
							break;
						}
					}
					Unode ut=new Unode(nu_value, nu_weight);
					umap3.put(path, ut);
					nu_value=0;
					nu_weight=0;
					path="";
					i++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("i:"+i+" size:"+list.size());
					e.printStackTrace();
				}
			}
			
		
		}
		return umap3;
	}
	
	
	
	public static TreeSet<Integer> compact2(Map<String, Unode> umap,double  bweight[],int pack,int up)  throws IOException{  //up是元素上届个数
		visitUmap(umap);
		//System.out.println("上界元素个数为:"+up+"y值为:"+pack);
		Map<String, Unode> umapin=new TreeMap<>();
		int max=0;   //表示初始集里面现在有几个节点了
		String path="";
		for (String temp : umap.keySet()) {  
			 String arr[]=temp.split("-");  
			 int len=arr.length;
			 if(len>max){
				 max=len;
				 path=temp;
			 }
		} 
		//System.out.println(path);
		TreeSet<Integer>  usetin=new TreeSet<Integer>();
		String ar[] =path.split("-");
		for(int i=0;i<ar.length;i++)
			usetin.add(Integer.parseInt(ar[i]));    //表示初始挑选出来有的节点
		umapin.put(path, umap.get(path)); //umap2只有一个，初始集，从umap里面挑
		double inc_value=umap.get(path).getValue();   //记录初始集里面的初始属性和、权重和
		double inc_weight=0;
		
		umap.remove(path);  
		
		double inc_max=0;
		String inc_path="";
		
		
		while(true){
			boolean flag=false;
			Iterator<Map.Entry<String, Unode>> it = umap.entrySet().iterator();  
			while(it.hasNext()){  
				
				Map.Entry<String, Unode> entry = it.next();  
				String temp=entry.getKey();   //这个是原始只剔除了一个的那个,还剩下很多，从中遍历
				String ar2[]=temp.split("-");
				if(inc_value+entry.getValue().getValue()<=pack&&usetin.size()+ar2.length<=up){   //初始集+这个权重不超过.元素个数也不超过
					flag=true;
					double t=entry.getValue().getWeight();
					int low=Integer.parseInt(ar2[0]);
					int high=Integer.parseInt(ar2[ar2.length-1]);
					if(usetin.contains(low-1)){    //这串的第一个节点可以和前面的连起来        
						t=t+bweight[low-1];
						}
					if(usetin.contains(high+1)){   //这串的最后一个节点可以和后面的连起来 
						t=t+bweight[high];
						}
					if(t>inc_max){   //循环记录最大值
						inc_max=t;
						inc_path=temp;
						}
					}
				}
			if(flag){
				String ar3[]=inc_path.split("-");   //将最终要加入的节点加入set中
				for(int i=0;i<ar3.length;i++)
					usetin.add(Integer.parseInt(ar3[i]));
				try {
					inc_value+=umap.get(inc_path).getValue();     //加入节点后更新属性值之和
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			//只能使用迭代器删除
				Iterator<Map.Entry<String, Unode>> itemp = umap.entrySet().iterator(); 
				while(itemp.hasNext()){
					Map.Entry<String, Unode> entrytemp = itemp.next();  
					String pathtemp=entrytemp.getKey(); 
					if(pathtemp==inc_path){
						itemp.remove();
					}
				}
				inc_max=0;
				inc_path="";
			}else{
				break;
			}
		}
		return usetin;
		/*System.out.println("选出来的节点为个数为:"+usetin.size());
		visitset(usetin);
		System.out.println("------");*/
		//visitUmap(umap);
	}
	
	
	
	/*public static void Dp(double res[][],int pack)  //res表示筛选出来的节点信息，res[][0]表示节点序号，res[][1]表示节点属性，res[][2]表示权重
													//pack 表示属性之和上限，即y
    {  
    //第一维下标i表示物品id,第二维下标j表示各个容量值,数组实际值代表价值,c[0][..]全部赋值为0,以便第一  
	int length=res.length; 
	
    int c[][]=new int[length+1][pack+1];     //记录在前i个物品，总属性容量和为j的情况下的最大权重和
    String path[][]=new String[length+1][pack+1];   //记录选择的节点，中间用"-"分割
  
    for (int a=0;a<=pack;a++)  {
        c[0][a]=0;  
        path[0][a]="";
    }
   
    for (int i=0;i<length;i++)  
    {  
        int index=i+1; //物品id从1开始  
        for (int j=0;j<=pack;j++)  
        {  
            //如果当前物品的重量小于容量j,才考虑后续处理 
        	int nowvalue=(int) res[i][1];
        	int nowweight=(int) res[i][2];
            if (nowvalue<=j)   //当前节点属性和小于j，才能往里加
            {  
            int k1=	c[index-1][j-nowvalue]+nowweight;  //加入物品i的情况,当前容量(j)减去i的容量即是前i-1种物品可使用的容量,容量下i-1种物品的价值+当前物品i的价值  
            int k2= c[index-1][j];   //与不加物品i的价值
           
            String[] att=path[index-1][j-nowvalue].split("-");   //取出加这个节点之前的路径，分割
            String ts=att[att.length-1];  //取出前一个路径的最后一个节点
            if(ts!=""&&ts!=null){
            	int temp1=(int) Double.parseDouble(ts);
                int temp2=(int) res[i][0];
                if(temp1+1==temp2){   //如果这个节点和当前节点连续
                	k1=k1+bweight[(int)Double.parseDouble(att[att.length-1])];   //那就加这条边的权重
                }
            }
            
            if(k1>k2){    //说明加入当前节点权重大，更新路径
            	path[index][j]=path[index-1][j-nowvalue]+"-"+res[i][0];
            }else{      //不加入当前节点，路径为上一个的
            	path[index][j]=path[index-1][j];
            }
            c[index][j]=Math.max(k1,k2);    //记录当前i个物品容量j情况下的最大权重和
             System.out.println("c["+index+"]["+j+"] :"+temp);     
             System.out.println("path["+index+"]["+j+"] :"+path[index][j]);
            }  
            else{  //当前节点属性超过容量j，不往里面加，权重和路径还是上一个的
            c[index][j]=c[index-1][j];
            path[index][j]=path[index-1][j];
            System.out.println("else:c["+index+"]["+j+"]:"+c[index][j]);;
            System.out.println("else:path["+index+"]["+j+"] :"+path[index][j]);
            }  
                 
        }  
          
    }  
    for(int i=0;i<length;i++){
    	for(int j=0;j<pack+1;j++){
    		System.out.print(c[i][j]+"  ");
    	}
    	System.out.println("");
    }
    System.out.println(c[length][pack]);  
    System.out.println(path[length][pack]);
  
      
    } 
	*/
	
	
	public static long Test( ArrayList<Unode> ulist,double  bweight[],int k,double t1,double t2,int conver_id,String filepath) throws IOException{
		
	//初始化	
	
	//System.out.println("conversation_id:"+conver_id);

	Date a = new Date();
	Map<Integer, Double> umap = new TreeMap<>();
	Map<Integer, Double> umap2 = new TreeMap<>();
	TreeSet<Integer>  set=new TreeSet<Integer>();
	double  value_add=0.0;
	int ulength=ulist.size();    //即ui
	int max= (int) (ulength*t1);   //确定元素个数上界
	int min= (int) (ulength*t2);   //确定元素个数下界
	int len1=(int) (k*t1);  //单个节点中选出前kt1个
	int len2=(int) (2*k*t1-3);  //双节点中选出钱2kt1-3个
	
	
	for(int i=0;i<ulist.size();i++){   //假设生成100个节点
		umap.put(i, ulist.get(i).getWeight());   //map放的是ut的序号和ut权值的健值对
		value_add=value_add+ ulist.get(i).getValue();
	}
	value_add=value_add*t1;
	int y=(int )value_add;
	
	
	//第一次筛选出符合条件的节点
	//printUnode(ulist, bweight);   //样例输出Unode属性值、权重、边值
	set=selectSing(set,umap,len1); //选出前len1个单个节点

	 
	
	set=selectDou(ulist,set,bweight,len2);//选出前len2个双个节点
	//visitset(set); //查看加入双个节点后的序号
	
	/*double res[][]=new double[set.size()][3];  //把最终选出来的节点放入二维数组中
	int n=0;
	for(Integer x:set){
		res[n][0]=x;     //表示节点的序号
		res[n][1]=ulist.get(x).getValue();   //节点的属性
		res[n][2]=ulist.get(x).getWeight();    //节点的权重
		n++;
	}*/
	/*for(int i=0;i<set.size();i++)
		System.out.println("序号："+ res[i][0]+" Unode value:"+ res[i][1]+
				 " weight:"+ res[i][2]);*/
	//Dp(res,10); //属性值总和小于y
	Map<String, Unode> umap3=compact1(ulist,set,bweight,y);
	TreeSet<Integer> usetin= compact2(umap3,bweight,y,max);
	FileWriter writer = null;  
	 Date b = new Date();
	 
	 long interval = (b.getTime() - a.getTime());
	 
	try{
		
		writer = new FileWriter(filepath, true);
		writer.write("conversation_id:"+conver_id+" k:"+k+" t1:"+t1+"uset:"+usetin.size()+"\r\n");  
	    for(Integer x:usetin){
	    	writer.write("("+x+","+conver_id+")  ");
	    }
	    writer.write("\r\n");
	    writer.close(); 
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} finally {
		return interval;  //返回计算时间
	}
	 
	}
}
