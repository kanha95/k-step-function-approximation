import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

class point{
    int x,y;
   point(int x,int y){
    this.x=x;
    this.y=y;
    }        
}

class point2{
    int x,s,e;
    double y;
   point2(int x, double y, int s, int e){
    this.x=x;
    this.y=y;
    this.s=s;
    this.e=e;
    }        
}

class stepDp{
    int n;
    double[][] g_one0;
    double[][] g_one1;
    double[][] mean0;
    double[][] mean1;
    
    double computeMean(int r, int t, point[] point){
        double sum=0.0;
        for(int i=r;i<=t;i++){
            sum+=point[i].y;
        }
       // if(r==1 && t==2) System.out.println(sum+" sum");
        return sum/(t-r+1);
        
    }
    
     double computeMid(int r, int t, point[] point){
        double min=Double.MAX_VALUE;
        double max=Double.MIN_VALUE;
        for(int i=r;i<=t;i++){
            min=Math.min(min,point[i].y);
            max=Math.max(max,point[i].y);
        }
       // if(r==1 && t==2) System.out.println(sum+" sum");
        return (min+max)/2;
        
    }
    
    //compute g*error for Mean squared error
    void computeGone0(int n, point[] point){
        g_one0=new double[n+1][n+1];
        mean0=new double[n+1][n+1];
        
        
        for(int i=1;i<=n;i++){
            for(int k=i;k<=n;k++){
                mean0[i][k]=computeMean(i,k,point);
               
           
                double diff_sum=0.0;
           
                for(int j=i;j<=k;j++){
                    double diff=Math.abs(point[j].y - mean0[i][k]);
                     //if(i==1 && k==2) System.out.println(diff+" diff"+mean0[i][k]);
                    diff_sum+=(diff/(k-i+1))*(diff);
                    
                }
            //    if(i==1 && k==2) System.out.println(diff_sum);
                g_one0[i][k]=diff_sum;
          // if(i==1 && k==2) System.out.println(g_one0[1][k]);
                if(k!=n){
                diff_sum=0.0;
                 mean0[k+1][n]=computeMean(k+1,n,point);
                for(int j=k+1;j<=n;j++){
                    double diff=Math.abs(point[j].y-mean0[k+1][n]);
                    diff_sum+=(diff/(n-k))*(diff);
                }
                g_one0[k+1][n]=diff_sum;
                }
           
            }
        }
        /*
          for (int i = 0; i <= n; i++) {
             for (int j = 0; j <= n; j++) {
                 System.out.print(g_one0[i][j]+" ");
             }System.out.println("");
         }
        System.out.println("------------");
        */
    }
    
     //compute g*error for Max error
    void computeGone1(int n, point[] point){
        g_one1=new double[n+1][n+1];
        mean1=new double[n+1][n+1];
        
        
        for(int i=1;i<=n;i++){
            for(int k=i;k<=n;k++){
                mean1[i][k]=computeMid(i,k,point);
               
           
                double diff_sum=0.0;
           
                for(int j=i;j<=k;j++){
                    double diff=Math.abs(point[j].y - mean1[i][k]);
                     //if(i==1 && k==2) System.out.println(diff+" diff"+mean0[i][k]);
                    diff_sum=Math.max(diff_sum,diff);
                    //System.out.println(diff_sum+" di");
                    
                }
            //    if(i==1 && k==2) System.out.println(diff_sum);
                g_one1[i][k]=diff_sum;
          // if(i==1 && k==2) System.out.println(g_one0[1][k]);
                if(k!=n){
                diff_sum=0.0;
                 mean1[k+1][n]=computeMid(k+1,n,point);
                for(int j=k+1;j<=n;j++){
                    double diff=Math.abs(point[j].y-mean1[k+1][n]);
                    diff_sum=Math.max(diff_sum,diff);
                }
                g_one1[k+1][n]=diff_sum;
                }
           
            }
        }
         /*
          for (int i = 0; i <= n; i++) {
             for (int j = 0; j <= n; j++) {
                 System.out.print(g_one1[i][j]+" ");
             }System.out.println("");
         }
        System.out.println("------------");
        */
    }
    
   
    
    //top-down approach, diffcult to keep track of required points
    /*
    double computeAnswer0(int c, int d, point[] point){
        
        if(d==1){
            return g_one0[1][c];
        }
        for(int i=1;i<=c-d+1;i++){
            ans.add(new point(c-i+1,d));
            double min = computeAnswer0(c-i,d-1,point) + g_one0[c-i+1][d];
            
        
        
        }
        
    }
    */
    
     void computeAnswer0(int n, int k, point[] point, PrintWriter pw){
        
         double[][] ans=new double[n+1][n+1];
         point2[][] track =new point2[n+1][n+1];
         
         
         for (int i = 0; i < n+1; i++) {
             for (int j = 0; j < n+1; j++) {
                 ans[i][j]=Double.MAX_VALUE;
             }
         }
      
         for (int i = 1; i <= n; i++) {
             ans[i][1]=g_one0[1][i];
             track[i][1]=new point2(point[1].x,mean0[1][i],-1,-1);
         }
         
         for (int i = 2; i <= n; i++) {
             for (int j = 2; j <= i; j++) {
               
                 
                 for (int l = i-1; l >= j-1; l--) {
                     
                     double temp=ans[i][j];
                     ans[i][j]=Math.min(ans[i][j], ((ans[l][j-1]*l)+g_one0[l+1][i]*(i-l))/i);
                     if(ans[i][j]!= temp){
                    
                      track[i][j]=new point2(point[l+1].x,mean0[l+1][i],l,j-1);
                     }
                   //  if(i==2 && j==2) System.out.println(ans[l][j-1]);
                 }
                
                 
                 
             }
         }
         /*
         for (int i = 0; i <= n; i++) {
             for (int j = 0; j <= n; j++) {
                 System.out.print(ans[i][j]+" ");
             }System.out.println("");
         }
         */
         //System.out.println("---------------");
           String printAns="";
         printAns+=(track[n][k].x+" "+track[n][k].y+"\n");
           int s=track[n][k].s;
           int e=track[n][k].e;
         while(true){
             if(s<0 || e<0 || ans[s][e]==Double.MAX_VALUE){
                 break;
             }
             
             printAns=(track[s][e].x+" "+track[s][e].y+"\n")+printAns;
             int temp=s;
             s=track[s][e].s;             
             e=track[temp][e].e;

             
             
         }
         
        pw.println(printAns);
        //pw.println(ans[n][k]);
        pw.flush();
    }
    
     void computeAnswer1(int n, int k, point[] point, PrintWriter pw){
        
         double[][] ans=new double[n+1][n+1];
         point2[][] track =new point2[n+1][n+1];
         
         for (int i = 0; i < n+1; i++) {
             for (int j = 0; j < n+1; j++) {
                 ans[i][j]=Double.MAX_VALUE;
             }
         }
      
         for (int i = 1; i <= n; i++) {
             ans[i][1]=g_one1[1][i];
             track[i][1]=new point2(point[1].x,mean1[1][i],-1,-1);
         }
         
         for (int i = 2; i <= n; i++) {
             for (int j = 2; j <= i; j++) {
               
                 
                 for (int l = i-1; l >= j-1; l--) {
                     
                     double temp=ans[i][j];
                     ans[i][j]=Math.min(ans[i][j], Math.max(ans[l][j-1],g_one1[l+1][i]));
                      if(ans[i][j]!= temp){
                      track[i][j]=new point2(point[l+1].x,mean1[l+1][i],l,j-1);
                     }
                   //  if(i==2 && j==2) System.out.println(ans[l][j-1]);
                 }
                
                 
                 
             }
         }
         /*
         for (int i = 0; i <= n; i++) {
             for (int j = 0; j <= n; j++) {
                 System.out.print(ans[i][j]+" ");
             }System.out.println("");
         }
         */
           //System.out.println("---------------");
           String printAns="";
         printAns+=(track[n][k].x+" "+track[n][k].y+"\n");
           int s=track[n][k].s;
           int e=track[n][k].e;
         while(true){
             if(s<0 || e<0 || ans[s][e]==Double.MAX_VALUE){
                 break;
             }
             
             printAns=(track[s][e].x+" "+track[s][e].y+"\n")+printAns;
             int temp=s;
             s=track[s][e].s;             
             e=track[temp][e].e;

             
             
         }
         
         pw.println(printAns);
         pw.flush();
         //pw.println(ans[n][k]);
    }
    
    
    
    public static void main(String... args) throws IOException{
        Scanner sc=new Scanner(System.in);
       PrintWriter pw=new PrintWriter(System.out);
        
        stepDp obj=new stepDp();
        
        int k=sc.nextInt();
        int error_Type=sc.nextInt();
        int n=sc.nextInt(); //no of points
        
        point[] p=new point[n+1];
        
        for(int i=1;i<=n;i++){
            p[i]=new point(sc.nextInt(),sc.nextInt());
        }
        pw.println(k);
        pw.flush();
        if(k==n){
            for (int i = 1; i <= n; i++) {
                pw.println(p[i].x+" "+p[i].y);
                pw.flush();
            }
        }
        else if(error_Type==0){
        obj.computeGone0(n,p);
        obj.computeAnswer0(n,k,p,pw);
        }else{
          obj.computeGone1(n,p);
        obj.computeAnswer1(n,k,p,pw);  
        }
        
        
        pw.close();
        
        
    }
}
