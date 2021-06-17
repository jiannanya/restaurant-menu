package restaurant;

import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


public class OrderingWindow extends JFrame implements ActionListener{
  private static final long serialVersionUID=1L;
  JPanel pNorth,pCenter,pSouth;
  JButton meatButton,vegetarianButton;
  JButton stapleFoodButton,soupAndPorridgeButton;
  JButton showButton,stopOrderingButton;
  JTextField idTextField,dateTextField;
  MealMenu mealMenus;
  File file;
  HashSet<String>idSet;

  public OrderingWindow(){
    setTitle("顾客点菜界面");
    idSet=new HashSet<String>();
    
    // 根据图10-37设计的布局来添加组件、设置组件的状态及注册监听器
    pNorth=new JPanel();
    idTextField=new JTextField(10);
    idTextField.addActionListener(this);
    dateTextField=new JTextField(20);
    
    //设置dateTextField为不可编辑的
    dateTextField.setEditable(false);
    
    pNorth.add(new JLabel("请输入您的桌号："));
    pNorth.add(idTextField);
    pNorth.add(new JLabel("点餐日期和时间："));
    pNorth.add(dateTextField);
    pCenter=new JPanel();
    //设置pCenter的带标题的边框
    pCenter.setBorder(new TitledBorder(new LineBorder(Color.BLUE),"分类点菜",TitledBorder.LEFT,TitledBorder.TOP));
    
    meatButton=new JButton("荤菜");
    meatButton.addActionListener(this);
    meatButton.setEnabled(false);
    
    vegetarianButton=new JButton("素菜");
    vegetarianButton.addActionListener(this);
    vegetarianButton.setEnabled(false);
    
    stapleFoodButton=new JButton("主食");
    stapleFoodButton.addActionListener(this);
    stapleFoodButton.setEnabled(false);

    soupAndPorridgeButton=new JButton("汤粥");
    soupAndPorridgeButton.addActionListener(this);
    soupAndPorridgeButton.setEnabled(false);

    Box baseBox=Box.createHorizontalBox();
    Box box1=Box.createVerticalBox();
    
    box1.add(meatButton);
    box1.add(Box.createVerticalStrut(20));
    box1.add(stapleFoodButton);

    Box box2=Box.createVerticalBox();
    box2.add(vegetarianButton);
    box2.add(Box.createVerticalStrut(20));
    box2.add(soupAndPorridgeButton);
    baseBox.add(box1);
    baseBox.add(Box.createHorizontalStrut(100));
    baseBox.add(box2);
    pCenter.add(baseBox);

    pSouth=new JPanel();
    this.showButton=new JButton("显示点菜明细、结账");
    showButton.addActionListener(this);
    showButton.setEnabled(false);
    this.stopOrderingButton=new JButton("结束本次点菜");
    stopOrderingButton.addActionListener(this);
    pSouth.add(showButton);
    pSouth.add(stopOrderingButton);

    add(pNorth,"North");
    add(pCenter,"Center");
    add(pSouth,"South");

    setBounds(100,100,600,230);
    this.setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  //实现接口ActionListener中的actionPerformed(ActionEvent)方法
  public void actionPerformed(ActionEvent e){
    //此事件表明用户在idTextField中按了回车键}
    if(e.getSource()==this.idTextField){
      //如果idTextField文本框中的值不为空
      if(idTextField!=null){
        //如果用户输入的桌号已经有顾客了
        if(!idSet.add(idTextField.getText())){
          JOptionPane.showMessageDialog(this,"此桌已有客人，请重新选桌！");
        }
        //如果该桌号无人，同意点菜，同时激活相关点菜的各个按钮
        else{
          idTextField.setEditable(false);
          this.meatButton.setEnabled(true);
          this.vegetarianButton.setEnabled(true);
          this.stapleFoodButton.setEnabled(true); 
          this.soupAndPorridgeButton.setEnabled(true);
          this.showButton.setEnabled(true);
          //dateTextField文本框的值显示为当前格式化了的日期和时间
          Date nowTime=new Date();
          SimpleDateFormat matter=new SimpleDateFormat("点菜时间：yyyy-MM-dd HH:mm:ss");
          String date=matter.format(nowTime);
          this.dateTextField.setText(date);
          //如果可以点菜，则生成一个文本文件的引用file
          //该文本将保存该桌顾客的点菜明细
          String filename=idTextField.getText()+"号桌点菜菜单.txt";
          file=new File(filename);
        }
      }
    }
    //如果用户单击了“点菜明细、结账”按钮，则显示showOrdering对话框
    else if(e.getSource()==this.showButton){
      ShowOrderingRecord showOrdering=new ShowOrderingRecord(file);
      showOrdering.setVisible(true);
      //对话框调用showRecord()方法将显示顾客点菜明细及消费金额
      showOrdering.showRecord();
    }
    //如果用户选择“结束本次点菜”按钮，则将相应组件恢复到初始状态
    else if(e.getSource()==this.stopOrderingButton){
      this.idTextField.setText(null);
      this.idTextField.setEditable(true);
      this.dateTextField.setText(null);
      this.meatButton.setEnabled(false);
      this.vegetarianButton.setEnabled(false);
      this.stapleFoodButton.setEnabled(false);
      this.soupAndPorridgeButton.setEnabled(false);
      this.showButton.setEnabled(false);
    }
    //如果用户单击了“荤菜”、“素菜”、“主食”、“汤粥”按钮，程序将执行下面的代码
    else{
      //字符串menusName用来设置orderDishes对话框的标题
      String menusName=null;
      //如果单击“荤菜”按钮，设置当前饭店菜单为肉菜菜单、当前按钮失效等
      if(e.getSource()==this.meatButton){
        mealMenus=new MeatMenu();
        menusName="荤菜 点菜";
      this.meatButton.setEnabled(false);
      }
      //如果单击“素菜”按钮，设置当前mealMenus为素菜菜单等。
      if(e.getSource()==this.vegetarianButton){
        mealMenus=new VegetarianMenu();
        menusName="素菜 点菜";
        this.vegetarianButton.setEnabled(false);
      }
      //如果单击“主食”按钮，设置当前mealMenus为素菜菜单等。
      if(e.getSource()==this.stapleFoodButton){
        mealMenus=new StapleFoodMenu();
        menusName="主食 点菜";
        this.stapleFoodButton.setEnabled(false);
      }
      //如果单击“汤粥”按钮，设置当前mealMenus为素菜菜单等。
      if(e.getSource()==this.soupAndPorridgeButton){
        mealMenus=new SoupAndPorridgeMenu();
        menusName="汤粥 点菜";
        this.soupAndPorridgeButton.setEnabled(false);
      }
      //创建各类点菜界面，并显示出来
      new OrderDishes(mealMenus,menusName,file).setVisible(true);
    }
    }
  //main()方法，程序入口，创建应用程序主界面
  public static void main(String[] args){
    new OrderingWindow();
  }
}
