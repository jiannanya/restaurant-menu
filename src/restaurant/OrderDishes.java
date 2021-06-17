package restaurant;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OrderDishes extends JDialog implements ActionListener,ListSelectionListener{
  private static final long serialVersionUID=1L;
  MealMenu mealMenu;
  JPanel pTop,pBottom,pImage;
  private JList<String> menusList,orderList;
  private JButton addButton,randonButton,deleteButton,saveButton;
  private LinkedList<Meal>mealMenus;
  private Vector<String>mealNames,mealDiscriptions;
  private Vector<Image> mealImages;
  private Vector<Double>mealPrice;
  private Vector<Meal>orderDishes;
  private Meal meal;
  private int selectedIndex,orderIndex;
  private DefaultListModel<String> orderMealNames;
  private File file;

  public OrderDishes(){}
  //OrderDishes窗口的构造方法，实现窗口初始化
  public OrderDishes(MealMenu mealMenu,String name,File file){
    this.file=file;
    setTitle(name);
    /**获得mealMenu对象的LinkedList<Meal>类型的成员变量，保存到链表mealMenus中，这是OrderDishes对象处理的数据，即顾客要点的菜的集合
  （饭店菜单）*/
    mealMenus=mealMenu.getMealMenu();
    /**创建存储菜、菜名、菜价格、菜图像、菜说明的向量对象*/
    orderDishes=new Vector<Meal>();
    mealNames=new Vector<String>();
    mealPrice=new Vector<Double>();
    mealImages=new Vector<Image>();
    mealDiscriptions=new Vector<String>();
    /**遍历mealMenus 中的每个Meal元素，并将其相关成员变量作为元素分别添加到向量MealNames、mealPrice、mealImage、mealDiscription中*/
    Iterator<Meal>iterator=mealMenus.iterator();
    while(iterator.hasNext()){
      meal=iterator.next();
      mealNames.add(meal.getName());
      mealPrice.add(meal.getPrice());
      mealImages.add(meal.getImage());
      mealDiscriptions.add(meal.getDescription());
    }
      //窗口布局设置为BorderLayout型
    setLayout(new BorderLayout());
    //设置面板pTop的布局和它所添加的各个组件
    pTop=new JPanel();
    pTop.setLayout(new GridLayout(1,3));
    JPanel pMenus=new JPanel();
    pMenus.setLayout(new BorderLayout());
    menusList=new JList<String>(mealNames);
    //设置显示饭店菜单的列表框默认选项第一选项
    menusList.setSelectedIndex(0);
    //设置显示饭店菜单的列表框只能单选
    menusList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    menusList.addListSelectionListener(this);
    JScrollPane listScrollPane=new JScrollPane(menusList);
    pMenus.add(new JLabel("菜单",JLabel.CENTER),"North");
    pMenus.add(listScrollPane,"Center");


    JPanel pButton=new JPanel();
    Box box =Box.createVerticalBox();
    this.addButton=new JButton("点        菜");
    this.addButton.addActionListener(this);
    this.randonButton=new JButton("随机推荐");
    this.randonButton.addActionListener(this);
    this.deleteButton=new JButton("撤销点菜");
    this.deleteButton.addActionListener(this);
    this.saveButton=new JButton("下        单");
    this.saveButton.addActionListener(this);
    box.add(Box.createVerticalStrut(20));
    box.add(addButton);
    box.add(Box.createVerticalStrut(15));
    box.add(randonButton);
    box.add(Box.createVerticalStrut(15));
    box.add(deleteButton);
    box.add(Box.createVerticalStrut(15));
    box.add(saveButton);
    pButton.add(box);

    JPanel pOrder=new JPanel();
    pOrder.setLayout(new BorderLayout());
    orderMealNames=new DefaultListModel<String>();
    orderList=new JList<String>(orderMealNames);
    orderList.addListSelectionListener(this);
    JScrollPane listScrollPane2=new JScrollPane(orderList);
    pOrder.add(new JLabel("已点",JLabel.CENTER),"North");
    pOrder.add(listScrollPane2,"Center");
    
    pTop.add(pMenus);
    pTop.add(pButton);
    pTop.add(pOrder);

    pBottom=new ImagePanel();
    add(pTop,"North");
    add(pBottom,"Center");
    
    setBounds(300,10,900,600);
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
  }
   //实现ActionListener接口中的方法actionPerformed(ActionEvent),处理窗口事件
  public void actionPerformed(ActionEvent e){
    //单击addButton按钮，实现点菜功能
    if(e.getSource()==addButton){
      Meal meal=mealMenus.get(selectedIndex); //获取索引处的Meal对象meal
      orderDishes.add(meal);//将此meal添加到用户点菜向量orderDishes
      //获取此meal中的name，在用户点菜列表框中显示出来
      orderMealNames.addElement(meal.getName());
    }
    //实现撤销点菜功能
    else if(e.getSource()==deleteButton){
      orderIndex=orderList.getSelectedIndex();//获取选中索引orderIndex
      orderDishes.remove(orderIndex);//从orderDishes中删除orderIndex处元素
      orderMealNames.removeElementAt(orderIndex);//删除列表框中该元素}
    }
      //实现随机推荐菜
    else if(e.getSource()==randonButton){
      int size=mealMenus.size();
      Random rand=new Random();
      selectedIndex=rand.nextInt(size); //产生一个随机数，保存到selectedIndex
      menusList.setSelectedIndex(selectedIndex);//设置列表框此位置为选中状态
    }
    //实现下单功能
    else if(e.getSource()==saveButton){
      saveButton.setEnabled(false);
      //将用户点菜向量中所有菜的菜名、价格写入file文件
      try{
        RandomAccessFile out=new RandomAccessFile(file,"rw");
        if(file.exists()){
          long length=file.length();
          out.seek(length);
        }
        for(int i=0;i<this.orderDishes.size();i++){
          out.writeUTF(orderDishes.get(i).getName());
          out.writeDouble(orderDishes.get(i).getPrice());
        }
        out.close();

      }catch(IOException e1){}
  
      setVisible(false);
      }
  }
  //实现接口ListSelectionListener中方法，响应列表上的事件，实现菜单浏览功能
  public void valueChanged(ListSelectionEvent e){
      if(e.getSource()==this.menusList){
        selectedIndex=menusList.getSelectedIndex();
        pBottom.repaint();
    }
  } 
    //内部类，负责绘制菜的相关各类信息
  class ImagePanel extends JPanel{
    private static final long serialVersionUID=1L;
    //重写paint方法
    public void paint(Graphics g){
      //清空ImagePanel容器中的内容
      g.setColor(getBackground());
      g.fillRect(0,0,getWidth(),getHeight());
      //绘制菜的名称、价格、图像、说明
      g.setColor(Color.RED);
      g.setFont(new Font("宋体",Font.BOLD,18));
      g.drawString(mealNames.get(selectedIndex),350,50);
      g.drawString("价格:"+mealPrice.get(selectedIndex),470,50);
      g.drawImage(mealImages.get(selectedIndex),300,70,350,260,this);
      g.drawString(mealDiscriptions.get(selectedIndex),170,360);
    }
  }
}




