import java.util.Arrays;

public class KExpansion {
    //S_box和逆S_box，用于密钥的扩展
    final static int[][][]S={ {{1,0,0,1},{0,1,0,0},{1,0,1,0},{1,0,1,1}},
            {{1,1,0,1},{0,0,0,1},{1,0,0,0},{0,1,0,1}},
            {{0,1,1,0},{0,0,1,0},{0,0,0,0},{0,0,1,1}},
            {{1,1,0,0},{1,1,1,0},{1,1,1,1},{0,1,1,1}}};
    final static int[][][]S_1={{{1,0,1,0}, {0,1,0,1}, {1,0,0,1}, {1,0,1,1}},
            {{0,0,0,1}, {0,1,1,1}, {1,0,0,0}, {1,1,1,1}},
            {{0,1,1,0}, {0,0,0,0}, {0,0,1,0}, {0,0,1,1}},
            {{1,1,0,0}, {0,1,0,0}, {1,1,0,1}, {1,1,1,0}}};

    //轮常数Recon，用于密钥密钥扩展
    int[][]Recon={{1,0,0,0,0,0,0,0},{0,0,1,1,0,0,0,0}};

    //左循环移位操作
    public void l1(int[] Array){
        int len=Array.length;
        int[] temp = Array.clone();
        for(int i=0;i<len;i++){
            if(i<len-1) {
                Array[i] = temp[i + 1];
            }
            else {
                Array[i] = temp[0];
            }
        }
    }


    //S-box置换,用于置换密钥的右8位,两两分组，每组半字节，左2为行，右二为列
    void S_transform(int[] k){
        int row=k[0]*2+k[1];
        int column=k[2]*2+k[3];
        int len=k.length;
        for(int i=0;i<len;i++){
            k[i]=S[row][column][i];
        }
    }

    //g函数操作的具体实现,此处的key是八位数组
    int[] g_function(int[] key,int row){
        int len=key.length;

        //左右交换
        for (int i=0;i<len/2;i++) {
            l1(key);
        }

        //分裂8位密钥
        int[] l= Arrays.copyOfRange(key, 0, 4);
        int[] r= Arrays.copyOfRange(key, 4, 8);

        //密钥转换
        S_transform(l);
        S_transform(r);

        //合并
        for(int i=0;i<len;i++){
            if(i<len/2){
                key[i]=l[i];
            }
            else {
                key[i] = r[i-len/2];
            }
        }

        //按位异或
        for(int i=0;i<len;i++){
            key[i]=key[i]^Recon[row][i];
        }
        return key;
    }

    //左半部分密钥的生成,这里的k为16位
    int []produce_key_l(int[]k,int row){
        int len=k.length;

        int[] l= Arrays.copyOfRange(k, 0, len/2);
        int[] r= Arrays.copyOfRange(k, len/2, len);
        int[]temp_k =g_function(r,row);
        int[] k_left=new int[len/2];
        for(int i=0;i<len/2;i++)
        {
            k_left[i]=l[i]^temp_k[i];
        }
        return k_left;
    }

    //右半部分密钥的生成,这里的k为16位
    int[]produce_key_r(int[]k,int row){
        int len=k.length;

        int[] l= Arrays.copyOfRange(k, 0, 8);
        int[] r= Arrays.copyOfRange(k, 8, 16);
        int[] k_right=new int[len/2];
        int[]temp_k=produce_key_l(k,row);
        for(int i=0;i<len/2;i++)
        {
            k_right[i]=temp_k[i]^r[i];
        }
        return k_right;
    }

    //一轮密钥扩展的具体实现
    int []produce_key(int[]k,int row){
        int[]l_2=produce_key_l(k,row);
        int[]r_2=produce_key_r(k,row);
        int len=l_2.length+r_2.length;
        int[] end_k=new int[len];
        for(int i=0;i<len;i++){
            if(i<len/2){
                end_k[i]=l_2[i];
            }
            else {
                end_k[i]=r_2[i-len/2];
            }
        }
        return end_k;
    }
}
