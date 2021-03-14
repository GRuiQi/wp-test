package wx.util;

import java.util.Arrays;

/**
 * 把传入的三个参数中的两个参数进行拼接，并加密。
 * 然后与第三个参数进行比较。返回一个Boolean值
 */
@SuppressWarnings("all")
public class CheckUtil {
    //令牌token
    private static final String token = "AliceAndMike";
    public static boolean checkSignature(String signature,
            String timestamp,String nonce){
        String[] arr = new String[]{token,timestamp,nonce};
        //排序
        Arrays.sort(arr);

        //生成字符串
        StringBuffer content = new StringBuffer();
        for(int i=0;i<arr.length;i++){
            content.append(arr[i]);
        }
        //sha1加密
        String temp = Sha1Util.encode(content.toString());
        return temp.equals(signature);


    }
}
