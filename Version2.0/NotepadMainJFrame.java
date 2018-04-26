/**
 * fun:（1）新建 （2）打开（3）保存（4）另存为（5）退出（6）查看（状态栏）
 * author:creator
 * date:2018/04/26 星期四
 */


package com.hejin.notepad;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class NotepadMainJFrame extends JFrame implements WindowListener, ActionListener
, KeyListener,MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static void main(String[] args) {
		
		NotepadMainJFrame frame = null;
		//没有参数  
		if(args == null || args.length == 0) {
			frame = new NotepadMainJFrame(null);
		}else {
			//有参数
			frame = new NotepadMainJFrame(new File(args[0]));
		}
	
		frame.setVisible(true);
	}
	
	private JMenuItem newCreateFile = new JMenuItem("\u65B0\u5EFA");
	private JMenuItem openFile = new JMenuItem("\u6253\u5F00");
	private JMenuItem saveFile = new JMenuItem("\u4FDD\u5B58");
	private JMenuItem saveAs = new JMenuItem("\u53E6\u5B58\u4E3A");
	private JMenuItem printFile = new JMenuItem("\u6253\u5370");
	private JMenuItem exitAPP = new JMenuItem("\u9000\u51FA");
	private JMenuItem backFile = new JMenuItem("\u64A4\u9500");
	private JMenuItem cutFile = new JMenuItem("\u526A\u5207");
	private JMenuItem copyFile = new JMenuItem("\u590D\u5236");
	private JMenuItem pasteFile = new JMenuItem("\u7C98\u8D34");
	private JMenuItem deleteFile = new JMenuItem("\u5220\u9664");
	private JMenuItem findFile = new JMenuItem("\u67E5\u627E");
	private JMenuItem findNext = new JMenuItem("\u67E5\u627E\u4E0B\u4E00\u4E2A");
	private JMenuItem replaceFile = new JMenuItem("\u66FF\u6362");
	private JMenuItem goToFile = new JMenuItem("\u8F6C\u5230");
	private JMenuItem checkAll = new JMenuItem("\u5168\u9009");
	private JMenuItem dateTime = new JMenuItem("\u65E5\u671F/\u65F6\u95F4");
	private JMenuItem menuItem_17 = new JMenuItem("\u81EA\u52A8\u6362\u884C");
	private JMenuItem menuItem_18 = new JMenuItem("\u5B57\u4F53");
	private JMenuItem menuItem_20 = new JMenuItem("\u67E5\u770B\u5E2E\u52A9");
	private  JMenuItem menuItem_21 = new JMenuItem("\u5173\u4E8E\u8BB0\u4E8B\u672C");
	private JMenuItem menuItem_22 = new JMenuItem("\u989C\u8272");
	private JTextArea textArea = new JTextArea();
	private JMenu developJava = new JMenu("\u5F00\u53D1");
	private final JMenuItem pageSetting = new JMenuItem("\u9875\u9762\u8BBE\u7F6E");
	private final JMenuItem runJavaFile = new JMenuItem("\u8FD0\u884C\uFF08java\uFF09");;
	//是否动过
	private boolean updateTxt = false;
	//是否为新建文件(为null)
	private File openFileStatus = null;
	private final JPanel statusBar = new JPanel();
	private final JLabel statusShow = new JLabel("\u6211\u7CFB\u6E23\u6E23\u8F89\u662F\u5144\u5F1F\u5C31\u6765\u5543\u6211 ");
	private final JLabel lineColStatus = new JLabel("\u7B2C1\u884C,\u7B2C1\u5217");
	private final JCheckBox checkBoxStatus = new JCheckBox("\u72B6\u6001\u680F");
	
	/**
	 * 打開文件
	 * @param file  文件對象
	 */
	public  void openFile(File file) {
		
		if(file == null) {
			this.setTitle("无标题 记事本");
			return;
		}
				
		if(!file.exists()) {
			//文件不存在
			javax.swing.JOptionPane.showMessageDialog(this, file.getPath()+" 此文件不存在!");
		}
				
		if(file.length() > Integer.MAX_VALUE) {
			javax.swing.JOptionPane.showConfirmDialog(this, file.getPath()+" 此文件太大，不能读取!");
			return;
		}
				
		boolean all = false;	
				
		//文件大于2M
		if(file.length() > 1024*1024*2) {
			int i = javax.swing.JOptionPane.showConfirmDialog(this, "文件過大, 是否要全部读取?");
			if(i == 2) {
				return;  //取消操作
			}
			//选择全部加载
			if(i == 0) {
				all = true;
			}
		}
		
		//修改文件是否動過狀態
		updateTxt = false;
				
		try {
			byte[] bytes = null;
			if(all) {
				bytes = new byte[(int)file.length()];
			}else {
				//默认2M
				bytes = new byte[1024*1024*2];
			}
			FileInputStream fileInfo = new FileInputStream(file);
			int len = fileInfo.read(bytes);
					
			fileInfo.close();
					
			//显示文字
			String txt = new String(bytes, 0, len);
			textArea.setText(txt);	
			this.setTitle(file.getName()+ " 记事本");
					
			//保存打开状态
			openFileStatus = file;
					
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		//是java文件
		if(file.getName().trim().endsWith(".java")) {
			developJava.setEnabled(true);
		}else {
			developJava.setEnabled(false);
		}
				
	}
	
	/**
	 * 保存文件
	 * @param file 文件對象
	 */
	public void saveFile(File file) {
		
		if(!file.exists()) {
			try {
				file.createNewFile(); //路徑不存在，創建
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			//保存內容
			fileOut.write(textArea.getText().getBytes());
			fileOut.close();
			this.setTitle(file.getName() + " 记事本");
			openFileStatus = file;
			updateTxt = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public NotepadMainJFrame(File file) {
		super();
		openFile(file);
		//调用open()方法
		openWindow();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//添加事件
		newCreateFile.addActionListener(this);
		openFile.addActionListener(this); 
		saveFile.addActionListener(this); 
		saveAs.addActionListener(this);
		printFile.addActionListener(this); 
		exitAPP.addActionListener(this); 
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("宋体", Font.PLAIN, 12));
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu fileMenu = new JMenu("\u6587\u4EF6");
		fileMenu.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(fileMenu);
		
		
		newCreateFile.setFont(new Font("宋体", Font.PLAIN, 12));
		fileMenu.add(newCreateFile);
		
		openFile.setFont(new Font("宋体", Font.PLAIN, 12));
		fileMenu.add(openFile);
		
		saveFile.setFont(new Font("宋体", Font.PLAIN, 12));
		fileMenu.add(saveFile);
		
		saveAs.setFont(new Font("宋体", Font.PLAIN, 12));
		fileMenu.add(saveAs);
		
		JSeparator separator = new JSeparator();
		fileMenu.add(separator);
		pageSetting.setFont(new Font("宋体", Font.PLAIN, 12));
		
		fileMenu.add(pageSetting);
		
		printFile.setFont(new Font("宋体", Font.PLAIN, 12));
		fileMenu.add(printFile);
		
		JSeparator separator_1 = new JSeparator();
		fileMenu.add(separator_1);
		
		exitAPP.setFont(new Font("宋体", Font.PLAIN, 12));
		fileMenu.add(exitAPP);
		
		JMenu editFile = new JMenu("\u7F16\u8F91");
		editFile.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(editFile);
	
		backFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(backFile);
		
		JSeparator separator_2 = new JSeparator();
		editFile.add(separator_2);
		
		cutFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(cutFile);
		
		copyFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(copyFile);
		
		pasteFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(pasteFile);
		
		deleteFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(deleteFile);
		
		JSeparator separator_3 = new JSeparator();
		editFile.add(separator_3);
		
		findFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(findFile);
		
		findNext.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(findNext);
		
		replaceFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(replaceFile);
		
		goToFile.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(goToFile);
		
		JSeparator separator_4 = new JSeparator();
		editFile.add(separator_4);
		
		
		checkAll.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(checkAll);
		
		dateTime.setFont(new Font("宋体", Font.PLAIN, 12));
		editFile.add(dateTime);
		
		
		developJava.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(developJava);
		
		JMenuItem compileJavaFile = new JMenuItem("\u7F16\u8BD1\uFF08javac\uFF09");
		compileJavaFile.setFont(new Font("宋体", Font.PLAIN, 12));
		developJava.add(compileJavaFile);
		
		developJava.add(runJavaFile);
		
		JMenu menu_2 = new JMenu("\u683C\u5F0F");
		menu_2.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(menu_2);
		
		menuItem_17.setFont(new Font("宋体", Font.PLAIN, 12));
		menu_2.add(menuItem_17);
		
		menuItem_18.setFont(new Font("宋体", Font.PLAIN, 12));
		menu_2.add(menuItem_18);
		
		menuItem_22.setFont(new Font("宋体", Font.PLAIN, 12));
		menu_2.add(menuItem_22);
		
		JMenu menu_3 = new JMenu("\u67E5\u770B");
		menu_3.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(menu_3);
		checkBoxStatus.setSelected(true);
		
		menu_3.add(checkBoxStatus);
		
		JMenu menu_4 = new JMenu("\u5E2E\u52A9");
		menu_4.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(menu_4);
		
		menuItem_20.setFont(new Font("宋体", Font.PLAIN, 12));
		menu_4.add(menuItem_20);
		
		JSeparator separator_5 = new JSeparator();
		menu_4.add(separator_5);
		
		menuItem_21.setFont(new Font("宋体", Font.PLAIN, 12));
		menu_4.add(menuItem_21);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(textArea);
		
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setLayout(new BorderLayout(0, 0));
		statusShow.setFont(new Font("宋体", Font.PLAIN, 12));
		
		statusBar.add(statusShow);
		lineColStatus.setFont(new Font("宋体", Font.PLAIN, 12));
		
		statusBar.add(lineColStatus, BorderLayout.EAST);
		//添加事件
		this.addWindowListener(this);   //窗口事件
		textArea.addKeyListener(this);  //键盘事件
		textArea.addMouseListener(this);//鼠标事件
		checkBoxStatus.addActionListener(this);  //选择事件
	}
	
	//打开窗口时
	public void openWindow() {
		//信息保存路径：系统临时路径或当前路径
		String temp = System.getenv("TEMP");
		if(temp == null || temp.trim().equals("")) {
			temp = ".";
		}
		File file = new File(temp,"notepad.temp");
		if(file.exists()) {
			try {
				FileInputStream fin = new FileInputStream(file);
				DataInputStream datain = new DataInputStream(fin);
				//读取保存的信息
				int w, h, x, y;
				w = datain.readInt();
				h = datain.readInt();
				x = datain.readInt();
				y = datain.readInt();
				
				this.setSize(w, h);
				this.setLocation(x, y);
				
				datain.close();
				fin.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {   //如果没有信息，显示在屏幕中间
			
			Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
			//获得屏幕的宽和高
			int ww = toolkit.getScreenSize().width;
			int hh = toolkit.getScreenSize().height;
			this.setSize(300, 300);
			this.setLocation((ww-300)/2, (hh-300)/2);
			
		}
		
		
	}
	
	
	//退出窗口时
	public void exitWindow() {
		int w = this.getWidth();
		int h = this.getHeight();
		int x = this.getX();
		int y = this.getY();
		
		//信息保存路径：系统临时路径或当前路径
		String temp = System.getenv("TEMP");
		if(temp == null || temp.trim().equals("")) {
			temp = ".";
		}
		File file = new File(temp,"notepad.temp");
		try {
			file.createNewFile();
			FileOutputStream fout = new FileOutputStream(file);
			DataOutputStream dataout = new DataOutputStream(fout);
			dataout.writeInt(w);  //写入宽
			dataout.writeInt(h);  //写入高
			dataout.writeInt(x);  //写入X
			dataout.writeInt(y);  //写入Y
			
			dataout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//销毁窗口
		this.setVisible(false);
		this.dispose();
		
	}
	
	/**
	 * 菜单：打开
	 */
	public void actionOpen() {
		//打开文件被修改
		if(updateTxt) {
			int i = javax.swing.JOptionPane.showConfirmDialog(this, "文件有修改，需要保存吗?","提示保存",JOptionPane.YES_NO_CANCEL_OPTION);
			if(i == 2) {
				//撤銷此項操作
				return;
			}
			if(i == 0) {
				if(openFileStatus == null) {   //新建文件
					remindSave();
					}
				}else { //保存文件
					saveFile(openFileStatus);
				}
			}
		//文件选择器,默认打开C盘
		JFileChooser jfc = new JFileChooser(File.listRoots()[0]);
		//只能选择文件
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//显示打开窗口
		jfc.showOpenDialog(this);
		//得到选中文件
		File file = jfc.getSelectedFile();
		if(file != null) {
			openFile(file);
		}
	}
	
	/**
	 * 菜单：新建
	 */
	public void actionNewCreateFile() {
		if(openFileStatus != null) {   //打开过文件
			if(updateTxt) {            //是否更改过文件
				int i = javax.swing.JOptionPane.showConfirmDialog(this, "文件有修改，需要保存吗?","提示保存",JOptionPane.YES_NO_CANCEL_OPTION);
				if(i == 0) {      //需要保存
					saveFile(openFileStatus);
				}else if(i == 2) {  //放弃操作
					return;
				}
			}
		}else if(textArea.getText().length() >= 1) {  //输入板有内容
			int i = javax.swing.JOptionPane.showConfirmDialog(this, "文件有修改，需要保存吗?","提示保存",JOptionPane.YES_NO_CANCEL_OPTION);
			if(i == 0) {
				remindSave();
			}else if(i == 2) {
				return;
			}
		}
			
			openFileStatus = null; //因为是新建文件
			updateTxt = false;     //因为是新建文件
			this.setTitle("无标题 记事本");
			textArea.setText("");
	}
	
	//弹出窗口选择路径提示保存
	public void remindSave() {
		JFileChooser jfc = new JFileChooser(File.listRoots()[0]);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.showSaveDialog(this);
		File file = jfc.getSelectedFile();
		if(file != null) {
			saveFile(file);
		}else {
			return;
		}
	}
	
	/**
	 * 菜单：保存
	 */
	public void actionSaveFile() {
		//新建的文件，彈出窗口選擇保存
		if(openFileStatus == null) {   
			remindSave();
		}else {  //對打開的文件進行了修改
			saveFile(openFileStatus);
		}
		
	}
	
	/**
	 * 菜单：另存为
	 */
	public void actionSaveAs() {
		remindSave();
	}
	
	/**
	 * 菜单：退出
	 */
	public void actionExitAPP() {
		if(openFileStatus != null) {
			if(updateTxt) {
				int i = javax.swing.JOptionPane.showConfirmDialog(this, "文件有修改，需要保存吗?","提示保存",JOptionPane.YES_NO_CANCEL_OPTION);
				if(i == 0) {
					saveFile(openFileStatus);
				}else if(i == 2) {
					return;
				}
			}
		}else if(textArea.getText().length() >= 1) {
			int i = javax.swing.JOptionPane.showConfirmDialog(this, "文件有修改，需要保存吗?","提示保存",JOptionPane.YES_NO_CANCEL_OPTION);
			if(i == 0) {
				remindSave();
			}else if(i == 2) {
				return;
			}
		}
		
		exitWindow();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == openFile) {   //打开
			actionOpen();
			
		} else if(e.getSource() == newCreateFile) {  //新建
			actionNewCreateFile();
			
		} else if(e.getSource() == saveFile) {     //保存
			actionSaveFile();
			
		} else if(e.getSource() == saveAs) {       //另存为
			actionSaveAs();
			
		} else if(e.getSource() == exitAPP) {      //退出
			actionExitAPP();
			
		} else if(e.getSource() == checkBoxStatus) { //状态栏
			if(checkBoxStatus.isSelected()) {
				statusBar.setVisible(true);
			}else {
				statusBar.setVisible(false);
			}
			
		}
			
	}
	
	/**
	 * 更新坐标（行，列）
	 */
	public void updateXY() {
		//获取光标所在位置
		int position = textArea.getSelectionStart();
		String txt = textArea.getText();
		char[] chars = txt.toCharArray();
		int row = 0;
		int index = 0;
		int col = 0;
		for (char c : chars) {
			if(index++ == position) {
				break;
			}
			col++;
			if(c == '\n'){
				row++;
				col = 0;
				continue;
			}
		}
		lineColStatus.setText("第 "+(row+1)+" 行,第 "+(col+1)+" 列");
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowClosing(WindowEvent e) {

		exitWindow();		
	}




	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//动过键盘
		updateTxt = true;
		updateXY();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		updateXY();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
