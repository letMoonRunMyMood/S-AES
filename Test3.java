import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Test3 extends JFrame {
    JTextField textfield1,textfield2,textfield3;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JButton btn;

    //构造函数
    public Test3 (String s, int x, int y, int w, int h){
        init();
        setTitle(s);
        setLocation(x, y);
        setSize(w, h);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    //将16为0,1数组转为ascii码的具体实现
    public static String binaryArrayToAscii(int[] binaryArray) {
        StringBuilder asciiBuilder = new StringBuilder();
        for (int i = 0; i < binaryArray.length; i += 8) {
            // 每8位二进制数转换为一个ASCII字符
            int asciiValue = 0;
            for (int j = 0; j < 8; j++) {
                asciiValue = (asciiValue << 1) | binaryArray[i + j];
            }
            asciiBuilder.append((char) asciiValue);
        }
        return asciiBuilder.toString();
    }

    //将两位String类型的字符转为二进制码的具体实现方法
    public static int[] stringtobinaryascii(String str) {
        int[] asciiValue =new int[str.length()];
        int []output=new int[16];
        String binaryAscii;
        for (int i = 0; i < str.length(); i++) {
            asciiValue[i] = str.charAt(i); // 获取字符的ASCII值
            binaryAscii= String.format("%8s", Integer.toBinaryString(asciiValue[i])).replace(' ', '0');
            int[] n = new int[binaryAscii.length()];
            for(int j=0;j<binaryAscii.length();j++){
                n[j] = Integer.parseInt(binaryAscii.substring(j,j+1));
                output[i*8+j]=n[j];
            }
        }
        return output;
    }

    //初始化JFrame
    private void init(){
        setLayout(new GridLayout(4, 2));
        textfield1=new  JTextField(200);
        textfield2=new  JTextField(200);
        textfield3=new  JTextField(200);
        label1=new JLabel("    输入明文对应的ASCLL码:");
        label2=new JLabel("    输入初始密钥:");
        label3=new JLabel("    输出密文:");
        btn=new JButton("加密");
        add(label1);
        add(textfield1);
        add(label2);
        add(textfield2);
        add(label3);
        add(textfield3);
        add(btn);


        //添加鼠标按钮事件
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取文本框内容并转为数组
                String input = textfield1.getText();
                int[]input1=stringtobinaryascii(input);
                String key = textfield2.getText();
                int[]key1=stringtobinaryascii(key);
                Encryption encryption=new Encryption();

                if(input.length()!=2||key.length()!=2){
                    JOptionPane.showMessageDialog(null,"此ASCII扩展加密只能输入2位字符");
                    return;
                }
                // 这里执行加密操作，
                int[] encrypted = encryption.Encrypt(input1, key1);
                String output= binaryArrayToAscii(encrypted);
                textfield3.setText(output);
            }
        });
    }

    public static void main(String[] args) {
        JFrame test = new Test3("S-AES加解密", 200, 300, 500, 300);
    }
}
