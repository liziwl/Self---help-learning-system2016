package drager;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class FindPassword extends JFrame{

	private JLabel[] labels;
	private JPasswordField[] passwords;
	private JTextField studentID;
	private JButton confirm;
	private JButton cancle;
	
	private GridBagLayout layout;
	GridBagConstraints constraints;
	
	
	public FindPassword(){
		super("�һ�����");
		getContentPane().setBackground(HelloPage.myBlue);
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		setLayout(layout);
		
		labels = new JLabel[3];
		passwords = new JPasswordField[2];
		labels[0] = new JLabel("������ѧ�ţ�",SwingConstants.LEFT);
		labels[1] = new JLabel("�����������룺",SwingConstants.LEFT);
		labels[2] = new JLabel("��ȷ�������룺",SwingConstants.LEFT);
		
		Handler handler = new Handler();
		for(int i = 0; i < 2; i++){
			passwords[i] = new JPasswordField(16);
			passwords[i].addActionListener(handler);
			passwords[i].setBackground(HelloPage.myGray);
			passwords[i].setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		confirm = new JButton("ȷ��");
		confirm.addActionListener(handler);
		confirm.setBackground(HelloPage.myGray);
		cancle = new JButton("ȡ��");
		cancle.addActionListener(handler);
		cancle.setBackground(HelloPage.myGray);
		studentID = new JTextField(8);
		studentID.addActionListener(handler);
		studentID.setBackground(HelloPage.myGray);
		studentID.setBorder(new EmptyBorder(0, 0, 0, 0));
		addComponent(labels[0], 0, 0, 2, 1);
		addComponent(studentID, 0, 1, 2, 1);
		addComponent(labels[1], 0, 2, 2, 1);
		addComponent(passwords[0], 0, 3, 2, 1);
		addComponent(labels[2], 0, 4, 2, 1);
		addComponent(passwords[1], 0, 5, 2, 1);
		constraints.fill = GridBagConstraints.NONE;
		addComponent(confirm, 0, 6, 1, 1);
		addComponent(cancle, 1, 6, 1, 1);
		
		setVisible(true);
		setSize(300, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	private void addComponent(Component component, int column, int row, int width, int height){
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridheight = height;
		constraints.gridwidth = width;
		layout.setConstraints(component, constraints);
		add(component);
	}
	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == cancle){
				dispose();
				new HelloPage();
				return;
			}
			
			int id = Integer.parseInt(studentID.getText());
			String newPassword1 = String.valueOf(passwords[0].getPassword());
			String newPassword2 = String.valueOf(passwords[1].getPassword());
			
			if(id > 11510937 || id <11510000){
				JOptionPane.showMessageDialog(null, "ѧ�Ų����ڣ�");
				return;
			}
			try {//�жϸ��˺��Ƿ���ע��
				ResultSet rs = DBUtility.executeQuery("SELECT Activation FROM students WHERE StudentID = " + id);
				if(!rs.next()){
						JOptionPane.showMessageDialog(null, "���˺�δע�ᣬ��ע�ᣡ");
						DBUtility.closeConnection();
						return;
				}
				DBUtility.closeConnection();
			} catch (Exception e2) {
				e2.printStackTrace();
				DBUtility.closeConnection();
			}
			
			
			if(newPassword1.equals(newPassword2)){
				if(newPassword1.length() == 0){
					JOptionPane.showMessageDialog(null, "���������룡");
					return;
				}
				if(newPassword1.length() > 16){
					JOptionPane.showMessageDialog(null, "���볤�Ȳ��ܳ���16��");
					passwords[0].setText("");
					passwords[1].setText("");
					return;
				}
				int vcode = (int)(Math.random()*900000 + 100000);//����6λ����֤��
				DBUtility.executeUpdate("UPDATE students SET Vcode = " + vcode 
						+ ", StudentPassword = '" + newPassword1 + "', Activation = \"" + "no\"" + " WHERE StudentID = " + id);
				
				String toEmail = id + "@mail.sustc.edu.cn";
				SSLEmail.sendCode(toEmail, vcode);//������֤��
				
				int getVcode = Integer.parseInt(JOptionPane.showInputDialog("�ѷ�����֤�뵽�������\n��������֤�룺"));
				while(getVcode != vcode){
					getVcode = Integer.parseInt(JOptionPane.showInputDialog("��֤�����\n������������֤�룺"));
				}
					DBUtility.executeUpdate("UPDATE students SET Activation = " + "\"yes\""  + " WHERE StudentID = " + id);
					JOptionPane.showMessageDialog(null, "�����޸ĳɹ������¼");
					dispose();
					new HelloPage();
					return;
			}
			else 
				JOptionPane.showMessageDialog(null, "������������벻һ�£�");
			passwords[0].setText("");
			passwords[1].setText("");
			return;
		}
		
	}

}
