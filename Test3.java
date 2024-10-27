import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

public class Test3 extends JFrame {
    JTextField textfield1, textfield2, textfield3;
    JLabel label1, label2, label3;
    JButton btn1, btn2;

    // 构造函数
    public Test3(String s, int x, int y, int w, int h) {
        init();
        setTitle(s);
        setLocation(x, y);
        setSize(w, h);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    // 将16位0,1数组转为ASCII码的具体实现
    public static String binaryArrayToAscii(int[] binaryArray) {
        StringBuilder asciiBuilder = new StringBuilder();
        for (int i = 0; i < binaryArray.length; i += 8) {
            int asciiValue = 0;
            for (int j = 0; j < 8; j++) {
                asciiValue = (asciiValue << 1) | binaryArray[i + j];
            }
            asciiBuilder.append((char) asciiValue);
        }
        return asciiBuilder.toString();
    }

    // 将两位String类型的字符转为二进制码的具体实现方法
    public static int[] stringToBinaryAscii(String str) {
        int[] asciiValues = new int[str.length()];
        int[] output = new int[str.length() * 8];
        for (int i = 0; i < str.length(); i++) {
            asciiValues[i] = str.charAt(i);
            String binaryAscii = String.format("%8s", Integer.toBinaryString(asciiValues[i])).replace(' ', '0');
            for (int j = 0; j < binaryAscii.length(); j++) {
                output[i * 8 + j] = Integer.parseInt(binaryAscii.substring(j, j + 1));
            }
        }
        return output;
    }

    // 初始化JFrame
    private void init() {
        setLayout(new GridLayout(4, 2));
        textfield1 = new JTextField(200);
        textfield2 = new JTextField(200);
        textfield3 = new JTextField(200);
        label1 = new JLabel("输入明文对应的ASCII码:");
        label2 = new JLabel("输入初始密钥:");
        label3 = new JLabel("输出密文:");
        btn1 = new JButton("加密");
        btn2 = new JButton("解密");
        add(label1);
        add(textfield1);
        add(label2);
        add(textfield2);
        add(label3);
        add(textfield3);
        add(btn1);
        add(btn2);

        addActionListeners();
    }

    private void addActionListeners() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textfield1.getText();
                int[] inputArray = stringToBinaryAscii(input);
                String key = textfield2.getText();
                int[] keyArray = getKeyArray(key);

                if (input.length() % 2 != 0) {
                    JOptionPane.showMessageDialog(null, "输入数据字符数量必须为偶数！");
                    return;
                }

                if (keyArray.length != 16) {
                    JOptionPane.showMessageDialog(null, "密钥必须为16位");
                    return;
                }

                Encryption encryption = new Encryption();
                int[] resultArray;
                if (e.getSource() == btn1) {
                    resultArray = encryption.Encrypt(inputArray, keyArray);
                } else {
                    resultArray = encryption.Decrypt(inputArray, keyArray);
                }

                String output = binaryArrayToAscii(resultArray);
                textfield3.setText(output);
            }
        };

        btn1.addActionListener(actionListener);
        btn2.addActionListener(actionListener);
    }

    private int[] getKeyArray(String key) {
        int[] keyArray = new int[key.length()];
        try {
            for (int i = 0; i < key.length(); i++) {
                keyArray[i] = key.getBytes("ASCII")[i] - '0';
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return keyArray;
    }

    public static void main(String[] args) {
        JFrame test = new Test3("S-AES加解密", 200, 300, 500, 300);
    }
}
