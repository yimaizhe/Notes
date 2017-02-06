package unicode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UnicodeUtils {
	public static void main(String[] args) {
		/*byte[] bs = charToBytes('你');
		for(byte b:bs){
			System.out.println(b);
		}
		System.out.println(printHexString(bs));*/
		/*byte[] bs = hexStringToBytes("e4bda0");
		for(byte b:bs){
			System.out.println(b);
		}*/
		
		byte b1 = Byte.parseByte("E", 16);
		System.out.println(b1);
		byte b2 = Byte.parseByte("4", 16);
		System.out.println(b2);
		byte b3 = (byte) (b1 << 4 | b2);
		System.out.println(b3);
		/*for(byte b:bs){
			System.out.print(b+"\t");
			System.out.println(Integer.toHexString(b));
		}
		System.out.println(urlEncode("你"));*/
		/*System.out.println(Byte.MAX_VALUE);
		System.out.println(Byte.MIN_VALUE);
		byte by = -28;
		System.out.println(Byte.toString(by));
		//byte b = Byte.parseByte("A4", 16);
		//System.out.println(b);
*/	}
	//汉字转unicode
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";   
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {   
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
              if (hexB.length() <= 2) {   
                  hexB = "00" + hexB;   
             }   
             unicodeBytes = unicodeBytes + "\\u" + hexB;   
        }   
        System.out.println("unicodeBytes is: " + unicodeBytes);   
        return unicodeBytes;   
    }
    
    //unicode转汉字
    public static String decodeUnicode(final String dataStr) {   
       int start = 0;   
       int end = 0;   
       final StringBuffer buffer = new StringBuffer();   
       while (start > -1) {   
           end = dataStr.indexOf("\\u", start + 2);   
           String charStr = "";   
           if (end == -1) {   
               charStr = dataStr.substring(start + 2, dataStr.length());   
           } else {   
               charStr = dataStr.substring(start + 2, end);   
           }   
           char letter = (char) Integer.parseInt(charStr, 16);   
           buffer.append(new Character(letter).toString());   
           start = end;   
       }   
       return buffer.toString();   
    }
    
    //字符转字节（utf-8编码）
    public static byte[] charToBytes(char c){
    	byte b1=0,b2=0,b3=0,b4=0;
    	if (c<0x80){
    		b1 = (byte)(c>>0 & 0x7F | 0x00);
			b2 = 0;
			b3 = 0;
			b4 = 0;
    	}else if (c<0x0800){
    		b1 = (byte)(c>>6 & 0x1F | 0xC0);
    		b2 = (byte)(c>>0 & 0x3F | 0x80);
    		b3 = 0;
			b4 = 0;
    	}else if (c<0x010000){
    		b1 = (byte)(c>>12 & 0x0F | 0xE0);
    		b2 = (byte)(c>>6 & 0x3F | 0x80);
    		b3 = (byte)(c>>0 & 0x3F | 0x80);
    		b4 = 0;
    	}else if (c<0x110000){
    		b1 = (byte)(c>>18 & 0x07 | 0xF0);
    		b2 = (byte)(c>>12 & 0x3F | 0x80);
    		b3 = (byte)(c>>6 & 0x3F | 0x80);
    		b4 = (byte)(c>>0 & 0x3F | 0x80);
    	}
    	return new byte[]{b1,b2,b3,b4};
    }
    
    //urlEncoder(模拟浏览器)
    private static String urlEncode(String s){
    	try {
			return URLEncoder.encode(s,"utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    //字节转十六进制显示
	public static String printHexString(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			a = a + hex;
		}
		return a;
	}
	
	//十六进制字符串转字节数组
	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            System.out.println((byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1])));
        }
        return d;
    }
	
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
