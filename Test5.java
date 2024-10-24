import java.util.Arrays;
import java.util.Scanner;

public class Test5 {

    //对于数组异或操作的封装
    int[] Xor(int[] byte1, int[] byte2) {
        int[] end_byte = new int[byte2.length];
        for (int i = 0; i < byte1.length; i++) {
            end_byte[i] = byte1[i] ^ byte2[i];
        }
        return end_byte;
    }

    //String类型转为数组类型
    static int[]Stringtoint(String s){
        int[] n = new int[s.length()];
        for(int i=0;i<s.length();i++){
            n[i] = Integer.parseInt(s.substring(i,i+1));
        }
        return n;
    }

    //CBC模式下的加密实现
    public int[]CBC_encryption(int[]plaintext,int[]k,int[]array){
        Encryption e=new Encryption();
        int len=plaintext.length;

        //将temp初始化成plaintext的nx16矩阵,并且将第一行直接转为第一次计算后的密文
        int[][]temp=new int[len/16][16];
        for (int i = 0; i <len/16 ; i++) {
            for (int j = 0; j < 16; j++) {
                temp[i][j]=plaintext[i*16+j];
            }
        }
        //第一次Xor
        int[]plaintext0=Xor(temp[0],array);

        temp[0]=e.Encrypt(plaintext0, k);
        //从第二行开始迭代
        for (int i = 1; i < len/16; i++) {
            temp[i]=Xor(temp[i-1],temp[i]);
            temp[i]=e.Encrypt(temp[i],k);
        }

        int[]output=new int[len];
        //将temp铺平
        for (int i = 0; i <len/16 ; i++) {
            for (int j = 0; j <16 ; j++) {
                output[i*16+j]=temp[i][j];
            }
        }

        return output;
    }

    public int[]CBC_decryption(int[]cryption,int[]k,int[]array){
        Encryption e=new Encryption();
        int len=cryption.length;
        //将temp初始化成cryption的nx16矩阵,并且将最后一行直接转为第一次计算后的明文
        int[][]temp=new int[len/16][16];

        //原始密钥的副本
        int[][]Sub_cryption=new int[len/16][16];
        for (int i = 0; i <len/16 ; i++) {
            for (int j = 0; j < 16; j++) {
                temp[i][j]=cryption[i*16+j];
            }
        }

        for (int i = 0; i <len/16 ; i++) {
            for (int j = 0; j < 16; j++) {
                    Sub_cryption[i][j] = cryption[i * 16 + j];
            }
        }

        temp[0]=e.Decrypt(temp[0],k);
        temp[0]=Xor(temp[0],array);

        //从第第二行开始迭代
        for (int i = 1; i <len/16; i++) {
            temp[i]=e.Decrypt(temp[i],k);
            temp[i]=Xor(temp[i],Sub_cryption[i-1]);
        }

        int[]output=new int[len];
        //将temp铺平
        for (int i = 0; i <len/16 ; i++) {
            for (int j = 0; j <16 ; j++) {
                output[i*16+j]=temp[i][j];
            }
        }

        return output;
    }
    void CBC(){
        System.out.println("CBC加密模式");
        System.out.print("输入初始向量：");
        Scanner sc1=new Scanner(System.in);
        String array=sc1.next();
        int[]array0=Stringtoint(array);

        System.out.print("请输入16的整数倍明文：");
        Scanner sc2=new Scanner(System.in);
        String plaintext=sc2.next();
        int[] plaintext0=Stringtoint(plaintext);


        System.out.print("请输入16位密钥：");
        Scanner sc3=new Scanner(System.in);
        String key=sc3.next();
        int[]key0=Stringtoint(key);
        if (plaintext0.length%16 != 0||key0.length!=16||array0.length!=16) {
            throw new IllegalArgumentException("明文必须被16整除，密钥和初始向量必须为16位");
        }
        int[]cryption=CBC_encryption(plaintext0,key0,array0);
        int[]plaintext_out=CBC_decryption(cryption,key0,array0);
        System.out.println("CBC模式加密后的密文为："+ Arrays.toString(cryption));
        System.out.println("CBC模式解密后的明文为："+ Arrays.toString(plaintext_out));

        System.out.print("请输入篡改后的密文：");
        Scanner sc4=new Scanner(System.in);
        String cryption0=sc4.next();
        int[]cryption_out=Stringtoint(cryption0);
        int[]plain_alter=CBC_decryption(cryption_out,key0,array0);
        System.out.println("CBC模式解密篡改后的密文的明文为："+ Arrays.toString(plain_alter));


    }
    public static void main(String[] args) {
         Test5 test5=new Test5();
        Encryption e=new Encryption();
        test5.CBC();
    }
}
