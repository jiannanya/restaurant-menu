package  restaurant;

import java.awt.BorderLayout;

import java.awt.Font;
import java.io.File;
import java.io.RandomAccessFile;


import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ShowOrderingRecord extends JDialog{
   private static final long serialVersionUID=1l;
   private File file;
   private JTextArea showArea;
 
  public ShowOrderingRecord(File fiel){
   this.file=fiel;
   showArea=new JTextArea(4,2);
   showArea.setFont(new Font("楷体",Font.BOLD,20));
   add(new JScrollPane(showArea),BorderLayout.CENTER);
   setBounds(200,200,300,400);
  }
   //显示该桌客人点菜清单及消费金额 
  public void showRecord(){
    showArea.setText(null);
    showArea.append("----菜名-------------价格----------");
    double totalPrice=0;//总消费金额totalPrice
    try{
      @SuppressWarnings("resource")
		  RandomAccessFile in  =new RandomAccessFile(file,"r");
      String mealName=null;
      while((mealName=in.readUTF())!=null){
	      showArea.append("\n"+mealName);
	      double mealPrice=in.readDouble();
	      showArea.append("         "+mealPrice);
        totalPrice=totalPrice+mealPrice;
      }
    }catch (Exception e){}
    
    showArea.append("\n-----------------------------");
    showArea.append("\n结账：        "+totalPrice);
  }
}