import java.util.Arrays;

public class Encryption extends KExpansion {

    //GF(x4)上的乘法盒第四行乘以4
    final static int[][]GF_4={{0,0,0,0},{0,1,0,0},{1,0,0,0},{1,1,0,0},
            {0,0,1,1},{0,1,1,1},{1,0,1,1},{1,1,1,1},
            {0,1,1,0},{0,0,1,0},{1,1,1,0},{1,0,1,0},
            {0,1,0,1},{0,0,0,1},{1,1,0,1},{1,0,0,1}};

    //GF(x2)上的乘法盒，代表乘以2的操作
    final static int[][]GF_2={{0,0,0,0},{0,0,1,0},{0,1,0,0},{0,1,1,0},
            {1,0,0,0},{1,0,1,0},{1,1,0,0},{1,1,1,0},
            {0,0,1,1},{0,0,0,1},{0,1,1,1},{0,1,0,1},
            {1,0,1,1},{1,0,0,1},{1,1,1,1},{1,1,0,1}};

    //GF(x9)上的乘法盒，代表乘以4的操作
    final static int[][]GF_9={{0,0,0,0},{1,0,0,1},{0,0,0,1},{1,0,0,0},
            {0,0,1,0},{1,0,1,0},{0,0,1,1},{1,0,1,0},
            {0,1,0,0},{1,1,0,1},{0,1,0,1},{1,1,0,0},
            {0,1,1,0},{1,1,1,1},{0,1,1,1},{1,1,1,0}};


    //封装对数组的Xor操作
    int[] Xor(int[] byte1, int[] byte2) {
        int[] end_byte = new int[byte2.length];
        for (int i = 0; i < byte1.length; i++) {
            end_byte[i] = byte1[i] ^ byte2[i];
        }
        return end_byte;
    }

    //轮密钥加操作
    int[]Add_roundkey(int[] plaintext0, int[]k) {
        int[]end=Xor(k,plaintext0);
        return end;
    }

    //字节替换操作，明文默认16位
    void Sub_byte(int[] plaintext1) {
        int[][] temp = new int[4][4];
        int row = 0;
        int column = 0;
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                temp[i][j]=plaintext1[4*i+j];
            }
            row = temp[i][0] * 2 + temp[i][1];
            column = temp[i][2] * 2 + temp[i][3];
            for (int k = 0; k < 4; k++) {
                plaintext1[i * 4 + k] = S[row][column][k];
            }
        }
    }

    //半字节替代的逆函数操作
    void Sub_byte_1(int[]cryption){
        int[][] temp = new int[4][4];
        int row = 0;
        int column = 0;
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                temp[i][j]=cryption[4*i+j];
            }
            row = temp[i][0] * 2 + temp[i][1];
            column = temp[i][2] * 2 + temp[i][3];
            for (int k = 0; k < 4; k++) {
                cryption[i * 4 + k] = S_1[row][column][k];
            }
        }
    }

    //行移位操作,对于plaintext2为16位,半byte 由1234变为1432
    void Shift_row(int[] plaintext2) {
        int[] bit2 = Arrays.copyOfRange(plaintext2, 4, 8);
        int[] bit4 = Arrays.copyOfRange(plaintext2, 12, 16);
        for (int i = 0; i < 16; i++) {
            if (i < 8 && i >= 4) {
                plaintext2[i] = bit4[i - 4];
            } else if (i >= 12) {
                plaintext2[i] = bit2[i - 12];
            }
        }
    }

    //列混淆操作，明文与矩阵{{1,4}，{4,1}}相乘，乘四操作位左移两位
    void Mix_column(int[] plaintext3) {
        int[][] temp = new int[4][4];
        int[][]bit= new int[4][4];

        //通过明文找到对应的乘GF（4）的密文
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                temp[i][j]=plaintext3[4*i+j];
                bit[i][j]=temp[i][j];
            }
            int column=0;
            for (int k = 0; k <4 ; k++) {
                column= (int) (column+temp[i][3-k]*(Math.pow(2,k)));
            }

            for (int m = 0; m <4 ; m++) {
                temp[i][m]=GF_4[column][m];
            }
        }
        int[]bit1=Xor(bit[0],temp[1]);
        int[]bit2=Xor(bit[1],temp[0]);
        int[]bit3=Xor(bit[2],temp[3]);
        int[]bit4=Xor(bit[3],temp[2]);
        for (int i = 0; i <16 ; i++) {
            if(i<4){
                plaintext3[i]=bit1[i];
            }else if(i<8){
                plaintext3[i]=bit2[i-4];
            }else if(i<12){
                plaintext3[i]=bit3[i-8];
            }else {
                plaintext3[i]=bit4[i-12];
            }
        }
    }




    //列混淆的逆函数操作
    void Mix_column_1(int[]cryption){
        int[][] temp2 = new int[4][4];
        int[][]temp9= new int[4][4];
        //通过明文找到对应的乘GF（2）的密文
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                temp2[i][j]=cryption[4*i+j];
            }
            int column=0;
            for (int k = 0; k <4 ; k++) {
                column= (int) (column+temp2[i][3-k]*(Math.pow(2,k)));
            }

            for (int m = 0; m <4 ; m++) {
                temp2[i][m]=GF_2[column][m];
            }
        }

        //通过明文找到对应的乘GF（9）的密文
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                temp9[i][j]=cryption[4*i+j];
            }
            int column=0;
            for (int k = 0; k <4 ; k++) {
                column= (int) (column+temp9[i][3-k]*(Math.pow(2,k)));
            }

            for (int m = 0; m <4 ; m++) {
                temp9[i][m]=GF_9[column][m];
            }
        }
        int[]bit1=Xor(temp2[1],temp9[0]);
        int[]bit2=Xor(temp2[0],temp9[1]);
        int[]bit3=Xor(temp2[3],temp9[2]);
        int[]bit4=Xor(temp2[2],temp9[3]);
        for (int i = 0; i <16 ; i++) {
            if(i<4){
                cryption[i]=bit1[i];
            }else if(i<8){
                cryption[i]=bit2[i-4];
            }else if(i<12){
                cryption[i]=bit3[i-8];
            }else {
                cryption[i]=bit4[i-12];
            }
        }
    }


    //封装S-AES的两轮加密操作,输入为明文和初始密钥
    int[]Encrypt(int[]plaintext,int[]k){
        int[] tmpTxt = new int[16];
        int[] cipherTxt = new int[plaintext.length];
        for(int n = 0;n < plaintext.length;n+=16) {
            System.arraycopy(plaintext, n, tmpTxt, 0, tmpTxt.length);
            int[] plaintext1 = Add_roundkey(tmpTxt, k);

            Sub_byte(plaintext1);
            Shift_row(plaintext1);
            Mix_column(plaintext1);
            int[] k1 = produce_key(k, 0);
            int[] plaintext2 = Add_roundkey(plaintext1, k1);

            Sub_byte(plaintext2);
            Shift_row(plaintext2);
            int[] k2 = produce_key(k1, 1);
            int[] cipher=Add_roundkey(plaintext2, k2);
            System.arraycopy(cipher,0,cipherTxt,n,16);
        }
        return cipherTxt;
    }

    //封装解密操作，输入密文和初始密钥
    int[]Decrypt(int[]cryption,int[]k) {
        int[] k1 = produce_key(k, 0);
        int[] k2 = produce_key(k1, 1);
        int[] tmpTxt = new int[16];
        int[] plaintext = new int[cryption.length];
        for (int i = 0; i < cryption.length; i += 16) {
            System.arraycopy(cryption, i, tmpTxt, 0, 16);
            int[] cryption1 = Add_roundkey(tmpTxt, k2);
            Shift_row(cryption1);
            Sub_byte_1(cryption1);
            int[] cryption2 = Add_roundkey(cryption1, k1);
            Mix_column_1(cryption2);

            Shift_row(cryption2);
            Sub_byte_1(cryption2);
            int[] plainTxt = Add_roundkey(cryption2, k);
            System.arraycopy(plainTxt, 0, plaintext, i, 16);
        }
        return plaintext;
    }
}
