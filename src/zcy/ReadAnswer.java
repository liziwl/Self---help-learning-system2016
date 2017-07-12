package zcy;

import java.awt.*;
import javax.swing.*;
import zcy.Management;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

@SuppressWarnings("serial")
public class ReadAnswer extends JFrame {

	private JPanel contentPane;
	private int chapter;
	private int index;
	private int stuID;
	private ResultSet rs_question;
	private ResultSet rs_answer;
	private ResultSet rs_score;
	private static ReadAnswer frame;

	private JComboBox<Integer> indexb;
	private JTextArea questionta;
	private JTextArea answerta;

	private JLabel thisScore;
	private JLabel chapterScore;
	private JTextArea stuta;

	private JButton preBtn;
	private JButton nextBtn;
	private JButton trueBtn;
	private JButton falseBtn;

	public static void showPage(int id) {
		frame = new ReadAnswer(id);
		frame.setVisible(true);
	}

	public ReadAnswer(int ID) {
		super("Java����ѧϰϵͳ�����Ĵ��");
		setSize(800, 600);
		setLocationRelativeTo(null);// ʹ����λ����Ļ����
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		stuID = ID;
		indexb = new JComboBox<Integer>();
		questionta = new JTextArea();
		answerta = new JTextArea();
		chapterScore = new JLabel("");
		stuta = new JTextArea();
		thisScore = new JLabel("");
		preBtn = new JButton("��һ��");
		nextBtn = new JButton("��һ��");

		// ��ʼ������ʱ����chapter
		if (!Management.isUndone(stuID)) {// �޴������½ڣ���ӵڶ��µ�һ������⿪ʼ��ʾ
			chapter = 2;
		} else {
			for (int chapter = 2; chapter <= 3; chapter++) {// ���ҵ�һ���������½�
				if (Management.isUndone(stuID, chapter)) {
					this.chapter = chapter;
					break;
				}
			}
		}
		atChapter(chapter);

		contentPane.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
		contentPane.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
		contentPane.add(Box.createVerticalStrut(20), BorderLayout.NORTH);

		Box Hbox = Box.createHorizontalBox();// ���ùؼ��ؼ�
		contentPane.add(Hbox, BorderLayout.CENTER);

		Box vbox = Box.createVerticalBox();// ������Ŀ����
		Hbox.add(vbox);

		Box hBox = Box.createHorizontalBox();// �����½ں����
		hBox.setMaximumSize(new Dimension(2147483647, 25));
		vbox.add(hBox);

		JLabel chapterl = new JLabel("�½�");
		hBox.add(chapterl);

		hBox.add(Box.createHorizontalStrut(20));

		ArrayList<Integer> ch = new ArrayList<>();
		for (int chapter = 2; chapter < 9; chapter++) {
			ResultSet rs = DBUtility.executeQuery("select * from students where StudentID=" + stuID);
			try {
				rs.next();
				if (!rs.getString(chapter + 2).equals("unlearned"))
					ch.add(chapter);// ����ʾδѧϰ״̬���½�
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBUtility.closeConnection();
		}
		Integer[] chapters = new Integer[ch.size()];
		for (int i = 0; i < ch.size(); i++)
			chapters[i] = ch.get(i);

		JComboBox<Integer> chapterb = new JComboBox<>(chapters);
		chapterb.setPreferredSize(new Dimension(60, 24));
		chapterb.setMinimumSize(new Dimension(10, 24));
		chapterb.setMaximumSize(new Dimension(100, 32767));
		chapterb.setEditable(true);
		chapterb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if ((int) chapterb.getSelectedItem() >= 2 && (int)
				// chapterb.getSelectedItem() <= 3) {
				chapter = (int) chapterb.getSelectedItem();
				atChapter(chapter);
				// } else {
				// indexb.removeAllItems();
				// index = 0;
				// questionta.setText("");
				// answerta.setText("");
				// thisScore.setText("");
				// chapterScore.setText("");
				// stuta.setText("");
				// preBtn.setEnabled(false);
				// nextBtn.setEnabled(false);
				// trueBtn.setEnabled(false);
				// falseBtn.setEnabled(false);
				// }
			}
		});
		hBox.add(chapterb);

		hBox.add(Box.createHorizontalGlue());

		JLabel indexl = new JLabel("���");
		hBox.add(indexl);

		hBox.add(Box.createHorizontalStrut(20));

		indexb.setPreferredSize(new Dimension(60, 24));
		indexb.setMinimumSize(new Dimension(10, 24));
		indexb.setMaximumSize(new Dimension(100, 32767));
		indexb.setEditable(true);
		indexb.addActionListener(new chooseIndexHandler());
		hBox.add(indexb);

		vbox.add(Box.createVerticalStrut(20));

		JScrollPane questionsp = new JScrollPane();
		questionsp.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 2));
		vbox.add(questionsp);

		JLabel questionl = new JLabel("��Ŀ");
		questionl.setHorizontalAlignment(SwingConstants.CENTER);
		questionsp.setColumnHeaderView(questionl);

		questionta.setLineWrap(true);
		questionta.setEditable(false);
		questionsp.setViewportView(questionta);

		vbox.add(Box.createVerticalStrut(20));

		JScrollPane answersp = new JScrollPane();
		answersp.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 2));
		vbox.add(answersp);

		JLabel answerl = new JLabel("�ο���");
		answerl.setHorizontalAlignment(SwingConstants.CENTER);
		answersp.setColumnHeaderView(answerl);

		answerta.setEditable(false);
		answersp.setViewportView(answerta);

		vbox.add(Box.createVerticalStrut(20));
		Hbox.add(Box.createHorizontalStrut(20));

		Box svBox = Box.createVerticalBox();// ����ѧ����
		Hbox.add(svBox);

		Box shBox = Box.createHorizontalBox();
		svBox.add(shBox);

		JLabel thisScorel = new JLabel("����÷֣�");
		shBox.add(thisScorel);
		shBox.add(thisScore);

		shBox.add(Box.createHorizontalGlue());

		JLabel chapterScorel = new JLabel("���³ɼ���");
		shBox.add(chapterScorel);
		shBox.add(chapterScore);

		shBox.add(Box.createHorizontalGlue());
		svBox.add(Box.createVerticalStrut(20));

		JScrollPane stusp = new JScrollPane();
		stusp.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
		svBox.add(stusp);

		JLabel label = new JLabel("ѧ���ش�");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		stusp.setColumnHeaderView(label);

		stuta = new JTextArea();
		stuta.setEditable(false);
		stusp.setViewportView(stuta);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				questionsp.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 2));
				answersp.setPreferredSize(new Dimension(getWidth() / 2, getHeight() / 2));
				stusp.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
			}
		});

		svBox.add(Box.createVerticalStrut(20));

		Box btns = Box.createHorizontalBox();// ���ܰ�ť
		contentPane.add(btns, BorderLayout.SOUTH);

		btns.add(Box.createHorizontalGlue());
		btns.add(Box.createHorizontalGlue());
		btns.add(Box.createHorizontalGlue());

		changeIndexHandler handler = new changeIndexHandler();// �����ⰴť���¼���Ӧ

		preBtn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		preBtn.setEnabled(false);
		preBtn.addActionListener(handler);
		btns.add(preBtn);

		btns.add(Box.createHorizontalGlue());

		nextBtn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		if (indexb.getItemCount() > 1)
			nextBtn.setEnabled(true);
		else
			nextBtn.setEnabled(false);
		nextBtn.addActionListener(handler);
		btns.add(nextBtn);

		btns.add(Box.createHorizontalGlue());
		btns.add(Box.createVerticalStrut(20));

		giveMarkHandler handler2 = new giveMarkHandler();// ����ť�¼���Ӧ

		trueBtn = new JButton("��ȷ");
		trueBtn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		trueBtn.addActionListener(handler2);
		btns.add(trueBtn);

		btns.add(Box.createHorizontalGlue());

		falseBtn = new JButton("����");
		falseBtn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		falseBtn.addActionListener(handler2);
		btns.add(falseBtn);

		btns.add(Box.createHorizontalGlue());
		btns.add(Box.createHorizontalGlue());

		JButton quitBtn = new JButton("����");// ������������
		quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isDone = true;// �Ƿ��������
				ResultSet rs_question = DBUtility
						.executeQuery("select * From chapter" + chapter + "_questions Where Property=\"blank\"");// ���ұ��������
				try {
					while (rs_question.next()) {
						int index = rs_question.getInt(1);
						ResultSet rs_score = DBUtility
								.executeQuery("select * From chapter" + chapter + "_scores Where StudentID=" + stuID);
						if (rs_score.next()) {
							if (rs_score.getString(index + 1) == null) {// ��δ���ĵ������
								isDone = false;
								break;
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (isDone) {// �������
					String state = "";// ѧ�����½�ԭ״̬
					ResultSet rs_state = DBUtility.executeQuery("select * From students Where StudentID=" + stuID);
					try {
						if (rs_state.next()) {
							state = rs_state.getString(chapter + 2);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					String[] ifPass = { "ͨ��", "��ͨ��" };
					int passORnot = JOptionPane.showOptionDialog(null, "���ĳɹ���\n�Ƿ�ͨ����\n����ǰ״̬��" + state + "��", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, ifPass, ifPass[0]);
					if (passORnot == 0 || passORnot == 1) {
						if (passORnot == 0) {// ѡ��ͨ��
							DBUtility.executeUpdate(
									"Update students set Chapter" + chapter + "=\"passed\" Where StudentID=" + stuID);
						} else {// ѡ��ͨ��
							DBUtility.executeUpdate(
									"Update students set Chapter" + chapter + "=\"unpassed\" Where StudentID=" + stuID);
						}
						DBUtility.executeUpdate(
								"Update chapter" + chapter + "_scores set Done='done' Where StudentID=" + stuID);// ���ݿ���״̬��Ϊ������
						int continueORnot = JOptionPane.showConfirmDialog(null, "�޸�ѧϰ״̬�ɹ���\n�Ƿ�������ģ�", "",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (continueORnot == 1) {// ������
							frame.dispose();
						}
					}
				} else if (!isDone) {// δ������
					int exitOrNot = JOptionPane.showConfirmDialog(null, "����Ŀδ���ģ�\n�Ƿ��˳���", "", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (exitOrNot == 0) {
						frame.dispose();
					}
				}
				DBUtility.closeConnection();
			}
		});
		quitBtn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btns.add(quitBtn);

		btns.add(Box.createHorizontalGlue());
	}

	class chooseIndexHandler implements ActionListener {// ��JComboBox�������ʱ���¼���Ӧ
		public void actionPerformed(ActionEvent e) {
			try {
				index = (int) indexb.getSelectedItem();
				atIndex(index);
			} catch (Exception ex) {// �����½�ʱ�ܻ��׳��쳣����������������
			}
		}
	}

	class changeIndexHandler implements ActionListener {// �������ⰴť�������ʱ���¼���Ӧ
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == preBtn)
				index = indexb.getItemAt(indexb.getSelectedIndex() - 1);
			else if (e.getSource() == nextBtn)
				index = indexb.getItemAt(indexb.getSelectedIndex() + 1);
			indexb.setSelectedItem(index);
			atIndex(index);
			if (indexb.getSelectedIndex() == 0) // ѡ�е�һ����ʱ��һ�ⰴť������
				preBtn.setEnabled(false);
			else
				preBtn.setEnabled(true);
			if (indexb.getSelectedIndex() + 1 == indexb.getItemCount()) // ѡ�����һ��ʱ��һ�ⰴť������
				nextBtn.setEnabled(false);
			else
				nextBtn.setEnabled(true);
		}
	}

	class giveMarkHandler implements ActionListener {// ����ť���¼���Ӧ
		@Override
		public void actionPerformed(ActionEvent e) {
			int mark = 0;
			if (e.getSource() == trueBtn) {
				mark = 1;// ��Ը�1��
			} else if (e.getSource() == falseBtn) {
				mark = 0;// ����0��
			}
			DBUtility.executeUpdate("Update chapter" + chapter + "_scores set Answer" + index + "=" + mark
					+ " Where StudentID=" + stuID);

			int premark;// ����ԭ�÷�
			try {
				premark = Integer.parseInt(thisScore.getText());
			} catch (Exception ex) {// ��һ������ʱpremarkΪ�գ����׳��쳣
				premark = 0;
			}
			if (mark != premark) {// ����÷ָı䣬�������¼��㲢�޸ı��µ÷�
				int totalmark = Integer.parseInt(chapterScore.getText()) - premark + mark;
				DBUtility.executeUpdate(
						"Update chapter" + chapter + "_scores set Score=" + totalmark + " Where StudentID=" + stuID);
				thisScore.setText(Integer.toString(totalmark));
			}

			rs_score = DBUtility.executeQuery("select * From chapter" + chapter + "_scores Where StudentID=" + stuID);
			try {
				if (rs_score.next()) {
					thisScore.setText(rs_score.getString(index + 1));
					chapterScore.setText(rs_score.getString(22));
				}
			} catch (Exception ex) {
			}
			DBUtility.closeConnection();
		}
	}

	private void atChapter(int chapter) {// �л��½�ʱ������ʾ���ݣ�Ĭ����ʾ��һ������⣩
		if (indexb.getItemCount() > 0)
			indexb.removeAllItems();
		ResultSet rs_question = DBUtility
				.executeQuery("select * From chapter" + chapter + "_questions Where Property=\"blank\"");
		try {
			while (rs_question.next()) {// ��������Ӧ�����������ѡ���JComboBox
				indexb.addItem(rs_question.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		indexb.setSelectedIndex(0);
		index = indexb.getItemAt(0);
		atIndex(index);
		preBtn.setEnabled(false);
		if (indexb.getItemCount() > 1)
			nextBtn.setEnabled(true);
		else
			nextBtn.setEnabled(false);

		DBUtility.closeConnection();
	}

	private void atIndex(int index) {// �л����ʱ������ʾ����
		rs_question = DBUtility
				.executeQuery("select * From chapter" + chapter + "_questions where QuestionID=" + index);
		try {
			if (rs_question.next()) {
				questionta.setText(rs_question.getString(3));
				answerta.setText(rs_question.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtility.closeConnection();
		}
		questionta.setCaretPosition(0);
		answerta.setCaretPosition(0);// ����������

		rs_answer = DBUtility.executeQuery("select * From chapter" + chapter + "_answers Where StudentID=" + stuID);
		rs_score = DBUtility.executeQuery("select * From chapter" + chapter + "_scores Where StudentID=" + stuID);
		try {
			if (rs_score.next() && rs_answer.next()) {
				thisScore.setText(rs_score.getString(index + 1));
				chapterScore.setText(rs_score.getString(22));
				stuta.setText(rs_answer.getString(index + 1));
				stuta.setCaretPosition(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtility.closeConnection();
		}
	}
}