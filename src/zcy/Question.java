package zcy;

import java.awt.*;
import Qiang.find;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Question extends JFrame {
	static Question frame;

	public static void view(int chapter, int index) {// �鿴��Ŀ
		frame = new Question(chapter, index, true, false, "view");
		frame.setVisible(true);
	}

	public static void add(int chapter) {// �����Ŀ
		frame = new Question(chapter, 0, false, true, "add");
		frame.setVisible(true);
	}

	public static void del(int chapter, int index) {// ɾ����Ŀ
		frame = new Question(chapter, index, true, false, "del");
		frame.setVisible(true);
	}

	public static void edit(int chapter, int index) {// �༭��Ŀ
		frame = new Question(chapter, index, true, true, "edit");
		frame.setVisible(true);
	}

	private String method;
	private int chapter;
	private int index;
	private ResultSet rs;
	private JPanel contentPane;
	private JTextField numbert;
	private JComboBox<Object> typeb;
	private JTextArea questionta;
	private JTextArea answerta;
	private JButton preBtn;
	private JButton nextBtn;
	private JButton OKBtn;
	private JButton cancelBtn;

	// filled��ʾ�Ƿ���ʾ���ݣ��������Ŀʱ����ʾ����editable��ʾ�Ƿ�ɱ༭
	public Question(int c, int i, boolean filled, boolean editable, String m) {
		chapter = c;// ��ǰ��������½�
		index = i;// ѡ�е����
		method = m;// ���ô��ڵķ�����
		preBtn = new JButton("��һ��");
		nextBtn = new JButton("��һ��");
		
		switch (method) {
		case "view":
			setTitle("�鿴��Ŀ");
			break;
		case "add":
			setTitle("������Ŀ");
			break;
		case "del":
			setTitle("ɾ����Ŀ");
			break;
		case "edit":
			setTitle("�޸���Ŀ");
			break;
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450, 600);
		setMinimumSize(new Dimension(250, 350));
		setLocationRelativeTo(null);// ʹ����λ����Ļ����

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
		contentPane.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
		contentPane.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
		contentPane.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);

		Box vbox = Box.createVerticalBox();// ���ùؼ��ؼ�
		contentPane.add(vbox, BorderLayout.CENTER);

		Box hBox = Box.createHorizontalBox();// ������ź�����
		hBox.setMaximumSize(new Dimension(2147483647, 25));
		vbox.add(hBox);

		JLabel numberl = new JLabel("���");
		hBox.add(numberl);

		hBox.add(Box.createHorizontalStrut(20));

		numbert = new JTextField();
		numbert.setPreferredSize(new Dimension(60, 24));
		numbert.setMinimumSize(new Dimension(10, 24));
		numbert.setEditable(editable);
		numbert.setMaximumSize(new Dimension(100, 32767));
		numbert.setBounds(0, 0, 50, 25);
		hBox.add(numbert);

		hBox.add(Box.createHorizontalGlue());

		JLabel typel = new JLabel("����");
		hBox.add(typel);

		hBox.add(Box.createHorizontalStrut(20));

		typeb = new JComboBox<Object>();
		typeb.setMaximumSize(new Dimension(100, 32767));
		typeb.setEnabled(editable);
		typeb.setPreferredSize(new Dimension(80, 24));
		typeb.setMinimumSize(new Dimension(10, 24));
		typeb.setModel(new DefaultComboBoxModel<Object>(new String[] { "choice", "blank" }));
		hBox.add(typeb);

		vbox.add(Box.createVerticalStrut(20));

		JScrollPane questionsp = new JScrollPane();
		questionsp.setPreferredSize(new Dimension(2, getHeight() / 2));
		vbox.add(questionsp);

		questionta = new JTextArea();
		questionta.setLineWrap(true);
		questionta.setEditable(editable);
		questionsp.setViewportView(questionta);

		JLabel questionl = new JLabel("��Ŀ");
		questionl.setHorizontalAlignment(SwingConstants.CENTER);
		questionsp.setColumnHeaderView(questionl);

		vbox.add(Box.createVerticalStrut(20));

		JScrollPane answersp = new JScrollPane();
		answersp.setPreferredSize(new Dimension(2, getHeight() / 2));
		vbox.add(answersp);

		answerta = new JTextArea();
		answerta.setEditable(editable);
		answersp.setViewportView(answerta);

		if (filled) // �ж��Ƿ���Ҫ��ʾ���ݣ��ȼ����Ƿ��������Ӱ�ť��
			update();

		JLabel answerl = new JLabel("�ο���");
		answerl.setHorizontalAlignment(SwingConstants.CENTER);
		answersp.setColumnHeaderView(answerl);

		Box btns = Box.createHorizontalBox();// ���ܰ�ť
		vbox.add(btns);

		btns.add(Box.createHorizontalGlue());
		btns.add(Box.createHorizontalGlue());

		changeIndexHandler handler = new changeIndexHandler();

		preBtn.setAlignmentY(TOP_ALIGNMENT);
		preBtn.addActionListener(handler);

		nextBtn.setAlignmentY(TOP_ALIGNMENT);
		nextBtn.addActionListener(handler);

		if (!method.equals("add")) {// ֻ�������Ŀʱ����Ҫ��ʾ����һ�ⰴť
			btns.add(preBtn);
			btns.add(Box.createHorizontalGlue());
			btns.add(Box.createVerticalStrut(20));
			btns.add(nextBtn);
			btns.add(Box.createHorizontalGlue());
			btns.add(Box.createVerticalStrut(20));
		}

		JButton editBtn = new JButton("�޸�");// �鿴��Ŀʱ�����������޸Ĵ���
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!Management.canChange(chapter))
					JOptionPane.showMessageDialog(null, "��������ѧ���ύ��������ٶ���Ŀ�����޸ģ�");
				else {
					dispose();
					edit(chapter, index);
				}
			}
		});
		editBtn.setAlignmentY(TOP_ALIGNMENT);

		questionChangedHandler handler2 = new questionChangedHandler();

		OKBtn = new JButton("ȷ��");
		OKBtn.setAlignmentY(TOP_ALIGNMENT);
		OKBtn.addActionListener(handler2);

		cancelBtn = new JButton("ȡ��");
		cancelBtn.setAlignmentY(TOP_ALIGNMENT);
		cancelBtn.addActionListener(handler2);

		if (method.equals("view")) {// �鿴��Ŀ�Ĵ�����ӱ༭��ȷ�ϰ�ť
			btns.add(editBtn);
			btns.add(Box.createHorizontalGlue());
			btns.add(Box.createVerticalStrut(20));
			btns.add(OKBtn);
		} else {// �����������ȷ�Ϻ�ȡ����ť
			btns.add(OKBtn);
			btns.add(Box.createHorizontalGlue());
			btns.add(Box.createVerticalStrut(20));
			btns.add(cancelBtn);
		}

		btns.add(Box.createHorizontalGlue());
		btns.add(Box.createHorizontalGlue());
	}

	private boolean isChanged(int chapter, int index) {// �ж��Ƿ����Ŀ�������޸�
		boolean result = false;// Ĭ��δ�޸�
		rs = DBUtility.executeQuery("select * From chapter" + chapter + "_questions Where QuestionID=" + index);
		try {
			if (rs.next()) {
				if (!numbert.getText().equals(rs.getString(1)) || !typeb.getSelectedItem().equals(rs.getString(2))
						|| !questionta.getText().equals(rs.getString(3)) || !answerta.getText().equals(rs.getString(4)))
					result = true;// �Ա����ݣ���һ��ı�����Ŀ�ı�
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return result;
	}

	private boolean check() {// ��������Ƿ����
		try {
			int number = Integer.parseInt(numbert.getText());
			if (number < 1 || number > 20)
				return false;// �������ݿ�Ԥ����ŷ�Χ
			if (method.equals("add") || (method.equals("edit") && number != index)) {// ������Ŀʱ���޸���Ŀ���޸������ʱ����
				ResultSet rs = DBUtility.executeQuery("select * From chapter" + chapter + "_questions");
				while (rs.next()) {
					if (number == rs.getInt(1))
						return false;// �������������ظ�
				}
			}
		} catch (Exception e) {
			return false;// ��ŷ����ֻ���������
		}
		String type = typeb.getSelectedItem().toString();
		String question = questionta.getText();
		String answer = answerta.getText();
		if (numbert.getText().equals("") || question.equals("") || answer.equals(""))
			return false;// ��δ����
		if (type.equals("choice")
				&& !(answer.equals("A") || answer.equals("B") || answer.equals("C") || answer.equals("D")))
			return false;// ѡ����𰸲���ѡ��ABCD
		return true;
	}

	private void update() {// ��Ÿı�ʱ��������
		rs = DBUtility.executeQuery("select * From chapter" + chapter + "_questions Where QuestionID=" + index);
		try {
			if (rs.next()) {
				numbert.setText(rs.getString(1));
				typeb.setSelectedItem(rs.getString(2));
				questionta.setText(rs.getString(3));
				answerta.setText(rs.getString(4));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		questionta.setCaretPosition(0);
		answerta.setCaretPosition(0);// ����������

		try {
			ResultSet rs1 = DBUtility.executeQuery("select * From chapter" + chapter + "_questions limit 0,1");// ��ȡ���ݿ��е�һ������
			rs1.next();
			if (rs1.getInt(1) == index)
				preBtn.setEnabled(false);// �����һ����ʱ��һ�ⰴť������
			else
				preBtn.setEnabled(true);
			ResultSet rs2 = DBUtility.executeQuery(
					"select * From chapter" + chapter + "_questions limit " + (find.num2questNum(chapter) - 1) + ",1");// ��ȡ���ݿ������һ������
			rs2.next();
			if (rs2.getInt(1) == index)
				nextBtn.setEnabled(false);// ������һ����ʱ��һ�ⰴť������
			else
				nextBtn.setEnabled(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	class changeIndexHandler implements ActionListener {// ������һ�ⰴť�л���Ŀ���¼�����
		@Override
		public void actionPerformed(ActionEvent e) {
			ResultSet rs;
			try {
				do {
					if (e.getSource() == preBtn) {
						index--;
					} else if (e.getSource() == nextBtn) {
						index++;
					} // ��ű�1
					rs = DBUtility
							.executeQuery("select * From chapter" + chapter + "_questions where QuestionID=" + index);
				} while (!rs.next());// ���ݿ����޸���Ŷ�Ӧ����ʱ��ż����������Լ�
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			update();
		}
	}

	class questionChangedHandler implements ActionListener {// ��ť��ɾ�ĵ��¼�����
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reread = false;// ��Ŀ�Ƿ�ı䣬�Ƿ���Ҫ������ʾ����
			if (e.getSource() == OKBtn) {// ���ȷ��
				if (method.equals("del")) {// ɾ����Ŀ
					int delORnot = JOptionPane.showConfirmDialog(null, "ȷ��ɾ�����⣿�˲������ɻָ�", "", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (delORnot == JOptionPane.YES_OPTION) {// ȷ��ɾ��
						DBUtility.executeUpdate(
								"Delete from chapter" + chapter + "_questions where QuestionID=" + index);
						JOptionPane.showMessageDialog(null, "ɾ���ɹ���");
						frame.dispose();
						reread = true;
					}
				} else if (!check()) // ���벻�Ϸ�
					JOptionPane.showMessageDialog(null, "�������");
				else if (method.equals("add")) {// �����Ŀ
					DBUtility.executeUpdate("Insert into chapter" + chapter
							+ "_questions(QuestionID,Property,Description,Reference) values(" + numbert.getText() + ",'"
							+ typeb.getSelectedItem() + "','" + questionta.getText() + "','" + answerta.getText()
							+ "')");
					JOptionPane.showMessageDialog(null, "��ӳɹ���");
					frame.dispose();
					reread = true;
				} else if (method.equals("edit") && isChanged(chapter, index)) {// ������Ŀ����Ŀ���ݱ��༭
					DBUtility.executeUpdate("Update chapter" + chapter + "_questions set QuestionID="
							+ numbert.getText() + ", Property='" + typeb.getSelectedItem() + "', Description='"
							+ questionta.getText() + "', Reference='" + answerta.getText() + "' where QuestionID="
							+ index);
					JOptionPane.showMessageDialog(null, "�޸ĳɹ���");
					frame.dispose();
					reread = true;
				} else
					dispose();
			} else// ���ȡ��
				dispose();
			if (reread) {// ��Ŀ�����仯
				Management.updateQuestion();// ������Ŀ����
			}
		}
	}
}