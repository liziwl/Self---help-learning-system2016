package drager;

import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import drager.DBUtility;
import zcy.Management;

public class Login {

	private static int flag = 1;// 1��ʾѧ����2��ʾ��ʦ
	private static String studentID;

	// ���Ե�¼
	public static boolean loginOK(String account, String password) {
		if (account.equals("")) {
			JOptionPane.showMessageDialog(null, "������ѧ�ţ�");
			return false;
		}
		if (password.equals("")) {
			JOptionPane.showMessageDialog(null, "���������룡");
			return false;
		}

		if (account.charAt(0) >= '0' && account.charAt(0) <= '9') {// ѧ��
			try {
				ResultSet rs = DBUtility
						.executeQuery("SELECT StudentPassword, Activation From students WHERE StudentID=" + account);
				if (rs.next()) {
					if (rs.getString(1).equals(password)) {
						if (rs.getString(2).equals("no")) {
							JOptionPane.showMessageDialog(null, "�˺�δ��������һ����룡");
							DBUtility.closeConnection();
							return false;
						}

						flag = 1;
						studentID = account;
						DBUtility.closeConnection();
						return true;
					} else {
						JOptionPane.showMessageDialog(null, "�������");
						DBUtility.closeConnection();
						return false;
					}

				}

				else {
					JOptionPane.showMessageDialog(null, "�û������ڣ�");
					DBUtility.closeConnection();
					return false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				DBUtility.closeConnection();
				return false;
			}
		}

		if (account.equals("admin")) {
			try {
				ResultSet rs = DBUtility
						.executeQuery("SELECT StudentPassword From students WHERE StudentID=" + 11111111);
				if (rs.next()) {
					if (rs.getString(1).equals(password)) {
						flag = 2;// ��ʦ
						DBUtility.closeConnection();
						return true;
					} else {
						JOptionPane.showMessageDialog(null, "�������");
						DBUtility.closeConnection();
						return false;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				DBUtility.closeConnection();
				return false;
			}
		}
		return false;

	}

	public static void nextPage() {
		if (flag == 1) {
			StudentPage.showPage(studentID);
		} else if (flag == 2) {
			Management.run();// ��ʦ����
		}

	}

	public static boolean changePassword(String newPassword) {

		return DBUtility.executeUpdate("UPDATE students SET StudentPassword = " + "\"" + newPassword + "\""
				+ " WHERE StudentID = " + studentID);
	}

	public static boolean changeName(String newName) {

		return DBUtility.executeUpdate(
				"UPDATE students SET StudentName = " + "\"" + newName + "\"" + " WHERE StudentID = " + studentID);
	}

	public static String getName(String studentID) {
		try {
			ResultSet rs = DBUtility.executeQuery("SELECT StudentName FROM students where StudentID = " + studentID);
			rs.next();
			String name = rs.getString(1);
			DBUtility.closeConnection();
			return name;

		} catch (Exception e) {
			e.printStackTrace();
			DBUtility.closeConnection();
			return null;
		}
	}
}
