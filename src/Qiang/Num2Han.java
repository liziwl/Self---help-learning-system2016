package Qiang;

public class Num2Han {
	static String[] units = { "", "ʮ", "��", "ǧ", "��", "ʮ��", "����", "ǧ��", "��", "ʮ��", "����", "ǧ��", "����" };
	static char[] numArray = { '��', 'һ', '��', '��', '��', '��', '��', '��', '��', '��' };

	public static String fomatInteger(int num) {
		char[] val = String.valueOf(num).toCharArray();
		int len = val.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			String m = val[i] + "";
			int n = Integer.valueOf(m);
			boolean isZero = n == 0;
			String unit = units[(len - 1) - i];
			if (isZero) {
				if ('0' == val[i - 1]) {
					// not need process if the last digital bits is 0
					continue;
				} else {
					// no unit for 0
					sb.append(numArray[n]);
				}
			} else {
				sb.append(numArray[n]);
				sb.append(unit);
			}
		}
		return sb.toString();
	}
}
