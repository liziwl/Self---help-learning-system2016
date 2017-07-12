package zcy;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import java.awt.event.*;
import java.sql.ResultSet;
import drager.HelloPage;
import java.sql.SQLException;
import drager.ChangeTPassword;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class Management extends JFrame {

	private static JPanel contentPane = new JPanel();
	private static Management frame;

	private static JList<String> list;
	private static int stuID;
	private static JTextArea info;
	private static JButton readBtn;

	private static JComboBox<String> selectChapter;
	private static JButton viewBtn, addBtn, delBtn, editBtn;
	private JTable table;
	private static DefaultTableModel tmodel;

	// // ����ʱֱ��ɾȥ��������ע��uimanager������ڳ����������в������ã�
	// public static void main(String[] args) {
	// SwingUtilities.updateComponentTreeUI(contentPane);
	// UIManager.put("TabbedPane.font", new Font("΢���ź�", Font.PLAIN, 20));
	// UIManager.put("Label.font", new Font("΢���ź�", Font.PLAIN, 18));
	// UIManager.put("Button.font", new Font("΢���ź�", Font.PLAIN, 18));
	// UIManager.put("ComboBox.font", new Font("΢���ź�", Font.PLAIN, 18));
	// UIManager.put("TextField.font", new Font("΢���ź�", Font.PLAIN, 18));
	// UIManager.put("List.font", new Font("΢���ź�", Font.PLAIN, 16));
	// UIManager.put("TextArea.font", new Font("΢���ź�", Font.PLAIN, 16));
	// UIManager.put("Table.font", new Font("΢���ź�", Font.PLAIN, 16));
	// UIManager.put("OptionPane.font", new Font("΢���ź�", Font.PLAIN, 15));
	// run();
	// }

	public static void run() {
		frame = new Management();
		frame.setVisible(true);
	}

	public Management() {
		super("Java����ѧϰϵͳ����ʦ����");
		setSize(750, 900);
		setLocationRelativeTo(null);// ʹ����λ����Ļ����
		setMinimumSize(new Dimension(580, 750));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel manageAccount = new JPanel();
		tabbedPane.addTab("�˻�����", null, manageAccount, null);
		manageAccount.setLayout(null);

		JPanel panel = new JPanel();// ���ù��ܰ���
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
		panel.setBounds(getWidth() / 2 - 110, getHeight() / 2 - 220, 220, 300);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		manageAccount.add(panel);
		manageAccount.addComponentListener(new ComponentAdapter() {// ���ڴ�С�ı�ʱ�����λ�ã���Ӧ�仯
			@Override
			public void componentResized(ComponentEvent arg0) {
				panel.setBounds(getWidth() / 2 - 110, getHeight() / 2 - 220, 220, 300);
			}
		});

		// �˻��������
		panel.add(Box.createVerticalGlue());

		JButton editPswd = new JButton("�޸�����");
		editPswd.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(editPswd);
		editPswd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				ChangeTPassword cp = new ChangeTPassword();
			}
		});

		panel.add(Box.createVerticalStrut(20));

		JButton changeAccount = new JButton("�л��˺�");
		changeAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(changeAccount);
		changeAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				@SuppressWarnings("unused")
				HelloPage jHelloPage = new HelloPage();// ��ʼ����
			}
		});

		panel.add(Box.createVerticalStrut(20));

		JButton logout = new JButton("�˳�ϵͳ");
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(logout);
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBUtility.closeConnection();
				dispose();// �رճ���
			}
		});

		panel.add(Box.createVerticalGlue());

		// ѧ����Ϣ����
		JPanel studentInfo = new JPanel();
		studentInfo.setLayout(new BoxLayout(studentInfo, BoxLayout.X_AXIS));
		tabbedPane.addTab("ѧ����Ϣ", null, studentInfo, null);

		studentInfo.add(Box.createHorizontalStrut(20));

		// ��ע��ѧ��
		Box verticalBox_stu = Box.createVerticalBox();
		studentInfo.add(verticalBox_stu);

		verticalBox_stu.add(Box.createVerticalStrut(20));
		verticalBox_stu.add(Box.createHorizontalGlue());

		JLabel label_stu = new JLabel("��ע��ѧ����");
		label_stu.setHorizontalAlignment(SwingConstants.CENTER);
		label_stu.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox_stu.add(label_stu);

		JScrollPane stuScrollPane = new JScrollPane();
		verticalBox_stu.add(stuScrollPane);

		JPanel panel_stu = new JPanel();
		panel_stu.setBackground(Color.WHITE);
		stuScrollPane.setViewportView(panel_stu);

		list = new JList<String>();// �б���ʾ��ע��ѧ��
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateStu();
		panel_stu.add(list);

		JLabel header = new JLabel("    ѧ��          ����");// ѧ����Ϣ�б��ͷ
		header.setFont(new Font("΢���ź�", Font.PLAIN, 17));
		header.setBackground(Color.WHITE);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		stuScrollPane.setColumnHeaderView(header);

		verticalBox_stu.add(Box.createVerticalStrut(20));
		verticalBox_stu.add(Box.createVerticalStrut(20));
		studentInfo.add(Box.createHorizontalStrut(20));

		Box verticalBox_info = Box.createVerticalBox();
		studentInfo.add(verticalBox_info);

		verticalBox_info.add(Box.createVerticalStrut(20));
		verticalBox_info.add(Box.createHorizontalGlue());

		JLabel label_info = new JLabel("ѧ����ϸ��Ϣ��");
		label_info.setHorizontalAlignment(SwingConstants.CENTER);
		label_info.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox_info.add(label_info);

		JScrollPane infoScrollPane = new JScrollPane();
		verticalBox_info.add(infoScrollPane);

		info = new JTextArea();
		info.setEditable(false);
		infoScrollPane.setViewportView(info);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				updateInfo();// ��list��ѡ��ѧ����info�м���ʾ��Ӧ��Ϣ
			}
		});

		readBtn = new JButton("����");
		readBtn.setEnabled(false);// ��ʼδѡ��ѧ����Ĭ�ϰ�ť������
		readBtn.setPreferredSize(new Dimension(70, 25));
		readBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox_info.add(readBtn);
		readBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReadAnswer.showPage(stuID);// �Ķ������
			}
		});

		Box btnhBox1 = Box.createHorizontalBox();
		verticalBox_info.add(btnhBox1);

		btnhBox1.add(Box.createHorizontalGlue());

		JButton updateBtn1 = new JButton("��������");
		updateBtn1.setBorder(new LineBorder(new Color(0, 0, 0)));
		updateBtn1.setContentAreaFilled(false);
		updateBtn1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// ��꾭��ʱ��۸ı�
		updateBtn1.setFont(new Font("΢���ź�", Font.PLAIN, 15));
		updateBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = list.getSelectedIndex();
				updateStu();
				list.setSelectedIndex(i);
				updateInfo();
			}
		});
		btnhBox1.add(updateBtn1);

		studentInfo.add(Box.createHorizontalStrut(20));

		// ���������
		JPanel manageQuestions = new JPanel();
		manageQuestions.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("������", null, manageQuestions, null);

		Box verticalBox = Box.createVerticalBox();
		manageQuestions.add(verticalBox, BorderLayout.CENTER);

		Box horizontalBox = Box.createHorizontalBox();// �����½�ѡ��͹��ܰ�ť
		horizontalBox.setPreferredSize(new Dimension(2147483647, 25));
		verticalBox.add(horizontalBox);

		selectChapter = new JComboBox<String>();
		selectChapter.setMinimumSize(new Dimension(37, 25));
		String[] chapters = new String[2];
		for (int i = 0; i < 2; i++)
			chapters[i] = "��" + (i + 2) + "��";
		selectChapter.setModel(new DefaultComboBoxModel<String>(chapters));
		selectChapter.setSelectedIndex(0);
		selectChapter.setMaximumSize(new Dimension(80, 25));
		selectChapter.setAlignmentX(Component.LEFT_ALIGNMENT);
		horizontalBox.add(selectChapter);

		Box btns = Box.createHorizontalBox();

		btns.setAlignmentX(Component.RIGHT_ALIGNMENT);
		horizontalBox.add(btns);

		btns.add(Box.createVerticalStrut(20));
		btns.add(Box.createHorizontalGlue());

		Handler handler = new Handler();

		viewBtn = new JButton("�鿴");
		viewBtn.setPreferredSize(new Dimension(70, 25));
		viewBtn.addActionListener(handler);
		btns.add(viewBtn);

		btns.add(Box.createHorizontalGlue());

		addBtn = new JButton("����");
		addBtn.setPreferredSize(new Dimension(70, 25));
		addBtn.setEnabled(true);
		addBtn.addActionListener(handler);
		btns.add(addBtn);

		btns.add(Box.createHorizontalGlue());

		delBtn = new JButton("ɾ��");
		delBtn.setPreferredSize(new Dimension(70, 25));
		delBtn.addActionListener(handler);
		btns.add(delBtn);

		btns.add(Box.createHorizontalGlue());

		editBtn = new JButton("�޸�");
		editBtn.setPreferredSize(new Dimension(70, 25));
		editBtn.addActionListener(handler);
		btns.add(editBtn);

		verticalBox.add(Box.createVerticalStrut(20));

		JScrollPane QAScrollPane = new JScrollPane();
		QAScrollPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		verticalBox.add(QAScrollPane);

		table = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;// ��ֹ�༭�������
			}
		};
		// ������в����ñ�ͷ
		tmodel = new DefaultTableModel();
		tmodel.addColumn("���");
		tmodel.addColumn("����");
		tmodel.addColumn("��Ŀ");
		tmodel.addColumn("��");
		updateQuestion();

		table.setModel(tmodel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setFont(new Font("΢���ź�", Font.PLAIN, 17));
		table.setRowHeight(25);
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);// ʹ����в����϶�

		// ���ñ���ʼ�п�
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(40);
		table.getColumnModel().getColumn(2).setPreferredWidth(390);
		table.getColumnModel().getColumn(3).setPreferredWidth(180);

		// ���ò��������־���
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumn("���").setCellRenderer(render);
		table.getColumn("����").setCellRenderer(render);
		table.getColumn("��").setCellRenderer(render);
		QAScrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (table.getSelectedRow() != -1) {
					viewBtn.setEnabled(true);
					delBtn.setEnabled(true);
					editBtn.setEnabled(true);
				}
			}
		});

		selectChapter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				updateQuestion();// �л��½ں���±������
			}
		});

		manageQuestions.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
		manageQuestions.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
		manageQuestions.add(Box.createHorizontalStrut(20), BorderLayout.EAST);

		Box btnhBox2 = Box.createHorizontalBox();
		manageQuestions.add(btnhBox2, BorderLayout.SOUTH);

		btnhBox2.add(Box.createHorizontalGlue());

		JButton updateBtn2 = new JButton("��������");
		updateBtn2.setFont(new Font("΢���ź�", Font.PLAIN, 15));
		updateBtn2.setContentAreaFilled(false);
		updateBtn2.setBorder(new LineBorder(new Color(0, 0, 0)));
		updateBtn2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// �������ʱ�ı����
		updateBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateQuestion();// ���±������
			}
		});
		btnhBox2.add(updateBtn2);

		btnhBox2.add(Box.createHorizontalStrut(20));
	}

	class Handler implements ActionListener {// ���ܰ�ť����ɾ�ģ��ļ���
		@Override
		public void actionPerformed(ActionEvent e) {
			int a = selectChapter.getSelectedIndex() + 2;// �½���
			int b = 0;
			try {
				b = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));
			} // ���
			catch (Exception ex) {
			}
			if (e.getSource() == viewBtn)
				Question.view(a, b);// �鿴��Ŀ
			else if (!canChange(a)) {// ������Ŀ�����޸�
				JOptionPane.showMessageDialog(null, "��������ѧ���ύ��������ٶ���Ŀ�����޸ģ�");
			} else {// ���޸�
				if (e.getSource() == addBtn) // ������Ӱ�ť������Ҫѡ����Ŀ
					Question.add(a);// �����Ŀ//��֪Ϊ�ξ����Զ���ӡ�getContentPane().��������˱���ɾȥ����
				else if (e.getSource() == delBtn)
					Question.del(a, b);// ɾ����Ŀ
				else if (e.getSource() == editBtn)
					Question.edit(a, b);// �༭��Ŀ
			}
		}
	}

	protected static boolean canChange(int chapter) {// ������Ŀ�Ƿ���޸�
		ResultSet rs = DBUtility.executeQuery("select * From students");
		try {
			while (rs.next()) {
				if (rs.getInt(1) == 11111111) // ������ʦ
					continue;
				if (rs.getString(chapter + 2).equals("unlearned"))
					continue;
				else {// ��ѧ������ѧϰ״̬��δѧϰ��˵�����ύ����򲻿����޸ı�����Ŀ
					DBUtility.closeConnection();
					return false;
				}
			}
			DBUtility.closeConnection();
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			DBUtility.closeConnection();
			return false;
		}
	}

	protected static boolean isUndone(int stuID, int chapter) {// �ж�ĳѧ��ĳ�½�ϰ���Ƿ������
		ResultSet rs = DBUtility.executeQuery("select * From chapter" + chapter + "_scores Where Done=\"undone\"");// ��ȡ���д��½ڴ����ĵ�ѧ��
		try {
			while (rs.next()) {
				if (rs.getInt(1) == stuID) {
					DBUtility.closeConnection();
					return true;// δ����
				}
			}
		} catch (SQLException e) {
			DBUtility.closeConnection();
			e.printStackTrace();
		}
		DBUtility.closeConnection();
		return false;// �Ǵ�����
	}

	protected static boolean isUndone(int stuID) {// �ж�ĳѧ���Ƿ��д����ĵ��½�ϰ��
		for (int chapter = 2; chapter <= 3; chapter++) {// �ñ������������½�ʱ�޸Ĵ���
			boolean each = isUndone(stuID, chapter);// �ֱ��ж�ÿ���½��Ƿ������
			if (each)
				return each;// ��δ���ĵ��½�
		}
		return false;// û��δ���ĵ��½�

		// ��һ���жϷ�ʽ
		// for (int chapter = 2; chapter <= 3; chapter++) {
		// ResultSet rs = DBUtility.executeQuery("select * From students Where
		// StudentID =" + stuID);
		// try {
		// while (rs.next())
		// if (rs.getString(chapter + 2).equals("submitted")) // ���ύ�ȼ��ڴ�����
		// return true;
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		// return false;
	}

	// ������Ż�������ѧ��δ������Ŀʱ���ݿ���done������Ϊnull�����Ȳ���undoneҲ����done����undone��done��״̬��ֱ��жϣ�Ҳ����ڸ����߼�
	protected static boolean isDone(int stuID) {// �ж�ĳѧ���Ƿ�����������½�
		for (int chapter = 2; chapter <= 3; chapter++) {
			ResultSet rs = DBUtility.executeQuery("select * From students Where StudentID =" + stuID);
			try {
				while (rs.next())
					if (rs.getString(chapter + 2).equals("passed") || rs.getString(chapter + 2).equals("unpassed")) {
						DBUtility.closeConnection();
						return true;
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBUtility.closeConnection();
		return false;
	}

	protected static void updateStu() {// ����ѧ���б�����
		DefaultListModel<String> lmodel = new DefaultListModel<String>();
		ResultSet lrs = DBUtility.executeQuery("select * From students");
		try {
			while (lrs.next()) {
				if (lrs.getInt(1) != 11111111) {// 11111111�����ʦ������ʾ
					if (isUndone(lrs.getInt(1)))
						lmodel.addElement(lrs.getString(1) + "      " + lrs.getString(2) + "�������ģ�");// ��ʾ�д������½�
					else
						lmodel.addElement(lrs.getString(1) + "      " + lrs.getString(2));
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		list.setModel(lmodel);
		DBUtility.closeConnection();
	}

	protected static void updateInfo() {// ����ѧ����ϸ��Ϣ
		int i = list.getSelectedIndex() + 1;// ��ѡѧ�������ݿ������������б��в���ʾ��ʦ��Ϣ����������ݿ�ѧ��������1
		ResultSet rs = DBUtility.executeQuery("select * From students limit " + i + ",1");
		try {
			String str = "";
			if (rs.next()) {
				stuID = rs.getInt(1);
				for (int k = 1; k <= 14; k++) {
					if (k == 3 || k == 13)
						continue;// ����ѧ���������֤��
					str += rs.getMetaData().getColumnName(k) + "��" + rs.getString(k) + "\n";
				}
			}
			info.setText(str);
			info.setCaretPosition(0);// ����������
			readBtn.setEnabled(true);
			if (isUndone(stuID)) // ���½�δ����
				readBtn.setText("����");
			else if (isDone(stuID)) // ���½�������
				readBtn.setText("��������");
			else {// �����ύ��������״̬
				readBtn.setText("����");
				readBtn.setEnabled(false);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		DBUtility.closeConnection();
	}

	protected static void updateQuestion() {// ������ʾ��Ŀ�ı������
		for (int i = 0; i < tmodel.getRowCount();)// ���ԭ����
			tmodel.removeRow(tmodel.getRowCount() - 1);
		int n = selectChapter.getSelectedIndex() + 2;
		if (n >= 2 && n <= 3) {
			ResultSet trs = DBUtility.executeQuery("select * From learning_system.chapter" + n + "_questions");
			try {
				while (trs.next()) {
					Vector<String> row = new Vector<String>();
					row.addElement(trs.getString(1));
					row.addElement(trs.getString(2));
					row.addElement(trs.getString(3));
					row.addElement(trs.getString(4));
					tmodel.addRow(row);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		viewBtn.setEnabled(false);
		delBtn.setEnabled(false);
		editBtn.setEnabled(false);
		DBUtility.closeConnection();
	}
}