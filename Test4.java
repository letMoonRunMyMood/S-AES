import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Test4 {

    static int[]Stringtoint(String s){
        int[] n = new int[s.length()];
        for(int i=0;i<s.length();i++){
            n[i] = Integer.parseInt(s.substring(i,i+1));
        }
        return n;
    }
    //双重加密的实现方法，其中plaintext为16位，k为32位
    public void Doublencryption(){
        System.out.println("双重加密，请输入16位明文和32位密钥");
        System.out.print("请输入16位明文：");
        Scanner sc1=new Scanner(System.in);
        String plaintext1=sc1.next();
        int[] plaintext=Stringtoint(plaintext1);


        System.out.print("请输入32位密钥：");
        Scanner sc2=new Scanner(System.in);
        String key1=sc2.next();
        int[] k=Stringtoint(key1);

        Encryption e=new Encryption();
        if (plaintext.length != 16||k.length!=32) {
            throw new IllegalArgumentException("明文必须是16位，密钥必须为32位");
        }
        int[]k1= Arrays.copyOfRange(k,0,16);
        int[]k2= Arrays.copyOfRange(k,16,32);
        int[]c1= e.Encrypt(plaintext,k1);
        int[]c2=e.Encrypt(c1,k2);
        System.out.print("双重加密后的结果是："+Arrays.toString(c2));
    }

    //双重加密的重载，用于中间攻击
    public int[]Doublencryption(int[]plaintext,int[]k){
        Encryption e=new Encryption();
        if (plaintext.length != 16||k.length!=32) {
            throw new IllegalArgumentException("明文必须是16位，密钥必须为32位");
        }
        int[]k1= Arrays.copyOfRange(k,0,16);
        int[]k2= Arrays.copyOfRange(k,16,32);
        int[]c1= e.Encrypt(plaintext,k1);
        return e.Encrypt(c1,k2);
    }


    //实现将两个数组合在一起的功能
    public int[] Addarray(int[]array1,int[]array2){
        int len=array1.length+array2.length;
        int[]array=new int[len];
        for (int i=0;i<len;i++){
            if(i<array1.length){
                array[i]=array1[i];
            }else {
                array[i]=array2[i-array1.length];
            }
        }
        return array;
    }


    //实现中间相遇攻击，给点明文，密文，找到32位密钥,一对明密文可能有个32位密钥对应
    public void findkey(int[]plaintext,int[]cipherText){
        int[]output=new int[plaintext.length+cipherText.length];
        Encryption e=new Encryption();
        for (int i = 0; i < 65536; i++) {
            String binaryString = Integer.toBinaryString(i).replaceAll("^0+(?!$)", "");
            binaryString = String.format("%16s", binaryString).replace(' ', '0');
            int[] key1 = Stringtoint(binaryString);
            int[] p1 = e.Encrypt(plaintext, key1);
            for (int j = 0; j < 65536; j++) {
                String binaryString2 = Integer.toBinaryString(j).replaceAll("^0+(?!$)", "");
                binaryString2 = String.format("%16s", binaryString2).replace(' ', '0');
                int[] key2 = Stringtoint(binaryString2);
                int[] c1 = e.Decrypt(cipherText, key2);
                if (Arrays.equals(p1, c1)) {
                    System.arraycopy(key1,0,output,0,key1.length);
                    System.arraycopy(key2,0,output,key1.length,key2.length);
                    System.out.println("可能的结果：");
                    System.out.println(Arrays.toString(output));
                }
            }
        }
    }


    //中间相遇攻击的程序
    public void mid_attack(){
        Scanner sc=new Scanner(System.in);
        System.out.println("中间相遇攻击，请输入16位明文和16位密文");

        System.out.print("请输入16位明文：");
        String plaintext1=sc.next();
        int[] plaintext=Stringtoint(plaintext1);
        System.out.print("请输入16位密文：");
        String cryption1=sc.next();
        int[] ciphertext=Stringtoint(cryption1);
        if (plaintext.length != 16||ciphertext.length!=16) {
            throw new IllegalArgumentException("明文密文必须为16位");
        }
        System.out.println("可能的结果为：");
        findkey(plaintext,ciphertext);
    }

    //(K1+K2)的三重加密
    public void triple_aes(){
        System.out.println("三重加密，请输入16位明文和32位密钥");
        System.out.print("请输入16位明密文对数：");

        System.out.print("请输入16位明文：");
        Scanner sc1=new Scanner(System.in);
        String plaintext1=sc1.next();
        //将输入的string转为数组
        int[]plaintext=Stringtoint(plaintext1);

        System.out.print("请输入32位密钥：");
        Scanner sc2=new Scanner(System.in);
        String key1=sc2.next();
        int[]key=Stringtoint(key1);

        Encryption e=new Encryption();
        if (plaintext.length != 16||key.length!=32) {
            throw new IllegalArgumentException("明文必须是16位，密钥必须为32位");
        }
        int[]k1= Arrays.copyOfRange(key,0,16);
        int[]k2= Arrays.copyOfRange(key,16,32);
        int[]c1= e.Encrypt(plaintext,k1);
        int[]c2= e.Encrypt(c1,k2);
        int[]c3= e.Encrypt(c2,k1);
        System.out.println("三重加密后的16位密文："+Arrays.toString(c3));
    }


    public static void main(String[] args) {
        Test4 test4=new Test4();
        System.out.println("1，双重加密");
        System.out.println("2，中间相遇攻击");
        System.out.println("3，三重加密");
        System.out.println("其他：结束");
        System.out.print("选择功能：");
        Scanner scanner=new Scanner(System.in);
        int choice=scanner.nextInt();
        if(choice==1){
            test4.Doublencryption();
        } else if (choice==2) {
            test4.mid_attack();
        } else if (choice==3) {
            test4.triple_aes();
        }

    }
}
