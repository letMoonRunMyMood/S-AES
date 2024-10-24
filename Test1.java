import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;


public class Test1 extends JFrame {
    JTextField textfield1,textfield2,textfield3;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JButton btn1;
    JButton btn2;


    //构造函数
    public Test1 (String s, int x, int y, int w, int h){
        init();
        setTitle(s);
        setLocation(x, y);
        setSize(w, h);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    //将Textfield的文本String转为int[]数组
    static int[]Stringtoint(String s){
        int[] n = new int[s.length()];
        for(int i=0;i<s.length();i++){
            n[i] = Integer.parseInt(s.substring(i,i+1));
        }
        return n;
    }

    //窗口初始化
    private void init(){
        textfield1=new  JTextField(200);
        textfield2=new  JTextField(200);
        textfield3=new  JTextField(200);
        label1=new JLabel("    输入明文或密文:");
        label2=new JLabel("    输入初始密钥:");
        label3=new JLabel("    输出明文或密文:");
        btn1=new JButton("加密");
        btn2=new JButton("解密");
        setLayout(new GridLayout(4, 2));
        add(label1);
        add(textfield1);
        add(label2);
        add(textfield2);
        add(label3);
        add(textfield3);
        add(btn1);
        add(btn2);


        //加密按钮的事件监听器
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取文本框内容并转为数组
                String input = textfield1.getText();
                int[]input1=Stringtoint(input);
                String key = textfield2.getText();
                int[]key1=Stringtoint(key);
                Encryption encryption=new Encryption();

                if(input1.length!=16||key1.length!=16){
                    JOptionPane.showMessageDialog(null,"明文和密钥必须为16位");
                    return;
                }
                // 这里执行加密操作，
                int[] encrypted = encryption.Encrypt(input1, key1);
                String output= Arrays.toString(encrypted);
                textfield3.setText(output);
            }
        });


        //解密按钮的事件监听器
        btn2.addActionListener(e -> {
            //获取文本框内容并转为数组
            String input = textfield1.getText();
            int[]input1=Stringtoint(input);
            String key = textfield2.getText();
            int[]key1=Stringtoint(key);
            Encryption encryption=new Encryption();
            if(input1.length!=16||key1.length!=16){
                JOptionPane.showMessageDialog(null,"密文和密钥必须为16位");
                return;
            }
            int[]decrypted=encryption.Decrypt(input1,key1);
            String output=Arrays.toString(decrypted);
            textfield3.setText(output);
        });

    }

    public static void main(String[] args) {
        JFrame test = new Test1("S-AES加解密", 200, 300, 500, 200);
    }
}
