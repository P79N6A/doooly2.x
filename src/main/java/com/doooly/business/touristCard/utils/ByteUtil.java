package com.doooly.business.touristCard.utils;

public class ByteUtil {

	private final static char[] HEX = "0123456789abcdef".toCharArray();

	public static byte[] intToBytes(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}

	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	public static byte[] short2byte(short s) {
		byte[] shortBuf = new byte[2];
		shortBuf[0] = (byte) (s >> 8);
		shortBuf[1] = (byte) (s >> 0);
		return shortBuf;
	}

	public static short byte2short(byte[] b) {
		return (short) (((b[0] << 8) | b[1] & 0xff));
	}

	public static byte[] long2byte(long x) {
		byte[] bb = new byte[8];
		bb[0] = (byte) (x >> 56);
		bb[1] = (byte) (x >> 48);
		bb[2] = (byte) (x >> 40);
		bb[3] = (byte) (x >> 32);
		bb[4] = (byte) (x >> 24);
		bb[5] = (byte) (x >> 16);
		bb[6] = (byte) (x >> 8);
		bb[7] = (byte) (x >> 0);
		return bb;
	}

	public static long byte2long(byte[] bb) {
		return ((((long) bb[0] & 0xff) << 56) | (((long) bb[1] & 0xff) << 48) | (((long) bb[2] & 0xff) << 40)
				| (((long) bb[3] & 0xff) << 32) | (((long) bb[4] & 0xff) << 24) | (((long) bb[5] & 0xff) << 16)
				| (((long) bb[6] & 0xff) << 8) | (((long) bb[7] & 0xff) << 0));
	}

	public static void xor(byte[] base, byte[] data, int offset) {
		for (int i = 0; i < base.length; i++) {
			base[i] ^= data[offset + i];
		}
	}

	/**
	 * 锟斤拷锟街斤拷锟斤拷锟斤拷转锟斤拷 16 锟斤拷锟狡碉拷锟街凤拷锟斤拷锟斤拷示锟斤拷每锟斤拷锟街节诧拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷示锟斤拷每锟斤拷锟街节硷拷锟斤拷锟?一锟斤拷锟秸革拷指锟?br />
	 * 锟斤拷锟斤拷实锟街斤拷为锟斤拷效锟斤拷 bytes2Hex 锟斤拷锟斤拷
	 * 
	 * @param bys
	 *            锟斤拷要转锟斤拷锟斤拷 16 锟斤拷锟狡碉拷锟街斤拷锟斤拷锟斤拷
	 * @return
	 * @deprecated
	 */
	public static String bytes2Hex1(byte[] bys) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bys.length; i++) {
			if (i > 0) {
				sb.append(" ");
			}
			sb.append(HEX[bys[i] >> 4 & 0xf]);
			sb.append(HEX[bys[i] & 0xf]);
		}
		return sb.toString();
	}

	/**
	 * 锟斤拷锟街斤拷锟斤拷锟斤拷转锟斤拷 16 锟斤拷锟狡碉拷锟街凤拷锟斤拷锟斤拷示锟斤拷每锟斤拷锟街节诧拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷示锟斤拷每锟斤拷锟街节硷拷锟斤拷锟?一锟斤拷锟秸革拷指锟?
	 * 
	 * @param bys
	 *            锟斤拷要转锟斤拷锟斤拷 16 锟斤拷锟狡碉拷锟街斤拷锟斤拷锟斤拷
	 * @return
	 */
	public static String bytes2HexSpace(byte[] bys) {
		char[] chs = new char[bys.length * 2 + bys.length - 1];
		for (int i = 0, offset = 0; i < bys.length; i++) {
			if (i > 0) {
				chs[offset++] = ' ';
			}
			chs[offset++] = HEX[bys[i] >> 4 & 0xf];
			chs[offset++] = HEX[bys[i] & 0xf];
		}
		return new String(chs);
	}

	/**
	 * 锟斤拷锟街斤拷锟斤拷锟斤拷转锟斤拷 16 锟斤拷锟狡碉拷锟街凤拷锟斤拷锟斤拷示锟斤拷每锟斤拷锟街节诧拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷示
	 * 
	 * @param bys
	 *            锟斤拷要转锟斤拷锟斤拷 16 锟斤拷锟狡碉拷锟街斤拷锟斤拷锟斤拷
	 * @return
	 */
	public static String bytes2Hex(byte[] bys) {
		char[] chs = new char[bys.length * 2];
		for (int i = 0, offset = 0; i < bys.length; i++) {
			chs[offset++] = HEX[bys[i] >> 4 & 0xf];
			chs[offset++] = HEX[bys[i] & 0xf];
		}
		return new String(chs);
	}

	/**
	 * 锟斤拷锟街斤拷锟斤拷锟斤拷转锟斤拷 16 锟斤拷锟狡碉拷锟街凤拷锟斤拷锟斤拷示锟斤拷每锟斤拷锟街节诧拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷示锟斤拷锟街节硷拷没锟叫分革拷 锟斤拷<br />
	 * 锟斤拷锟斤拷实锟街斤拷为锟斤拷效锟斤拷 bytes2Hex 锟斤拷锟斤拷
	 * 
	 * @param bys
	 *            锟斤拷要转锟斤拷锟斤拷 16 锟斤拷锟狡碉拷锟街斤拷锟斤拷锟斤拷
	 * @return
	 * @deprecated
	 */
	public static String bytes2Hex2(byte[] bys) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bys.length; i++) {
			sb.append(HEX[bys[i] >> 4 & 0xf]);
			sb.append(HEX[bys[i] & 0xf]);
		}
		return sb.toString();
	}

	public static byte[] int2BytesBE(int num) {
		byte[] bys = new byte[Integer.SIZE / Byte.SIZE];
		for (int i = 0, k = bys.length; i < k; i++) {
			bys[i] = (byte) (num >>> ((k - 1 - i) * Byte.SIZE) & 0xff);
		}
		return bys;
	}

	public static byte[] int2BytesLE(int num) {
		return int2BytesBE(Integer.reverseBytes(num));
	}

	/**
	 * 锟斤拷锟斤拷 Big-Endian 锟斤拷式锟斤拷 long 锟斤拷锟阶?byte 锟斤拷锟斤拷
	 * 
	 * @param num
	 * @return 转为 Big-Endian 锟斤拷式锟斤拷 byte 锟斤拷锟斤拷
	 */
	public static byte[] long2BytesBE(long num) {
		byte[] bys = new byte[Long.SIZE / Byte.SIZE];
		for (int i = 0, k = bys.length; i < k; i++) {
			bys[i] = (byte) (num >>> ((k - 1 - i) * Byte.SIZE) & 0xff);
		}
		return bys;
	}

	/**
	 * 锟斤拷锟斤拷 Little-Endian 锟斤拷式锟斤拷 long 锟斤拷锟阶?byte 锟斤拷锟斤拷
	 * 
	 * @param num
	 * @return 转为 Little-Endian 锟斤拷式锟斤拷 byte 锟斤拷锟斤拷
	 */
	public static byte[] long2BytesLE(long num) {
		return long2BytesBE(Long.reverseBytes(num));
	}

	/**
	 * 锟斤拷 Little-Endian 锟斤拷锟街斤拷锟斤拷锟斤拷转为 int 锟斤拷锟酵碉拷锟斤拷锟?br />
	 * Little-Endian 锟斤拷示锟斤拷位锟街斤拷锟节革拷位锟斤拷锟斤拷锟斤拷
	 * 
	 * @param bys
	 *            锟街斤拷锟斤拷锟斤拷
	 * @param start
	 *            锟斤拷要转锟斤拷锟侥匡拷始锟斤拷锟斤拷位锟斤拷
	 * @param len
	 *            锟斤拷要转锟斤拷锟斤拷锟街斤拷锟斤拷锟斤拷
	 * @return 指锟斤拷锟斤拷始位锟矫和筹拷锟斤拷锟斤拷 LE 锟斤拷式锟斤拷示锟斤拷 int 锟斤拷值
	 */
	public static int bytes2IntLE(byte[] bys, int start, int len) {
		return bytes2Int(bys, start, len, false);
	}

	public static int bytes2IntLE(byte[] bys) {
		return bytes2Int(bys, 0, bys.length, false);
	}

	/**
	 * 锟斤拷 Big-Endian 锟斤拷锟街斤拷锟斤拷锟斤拷转为 int 锟斤拷锟酵碉拷锟斤拷锟?br />
	 * Big-Endian 锟斤拷示锟斤拷位锟街斤拷锟节碉拷位锟斤拷锟斤拷锟斤拷
	 * 
	 * @param bys
	 *            锟街斤拷锟斤拷锟斤拷
	 * @param start
	 *            锟斤拷要转锟斤拷锟侥匡拷始锟斤拷锟斤拷位锟斤拷
	 * @param len
	 *            锟斤拷要转锟斤拷锟斤拷锟街斤拷锟斤拷锟斤拷
	 * @return 指锟斤拷锟斤拷始位锟矫和筹拷锟斤拷锟斤拷 BE 锟斤拷式锟斤拷示锟斤拷 int 锟斤拷值
	 */
	public static int bytes2IntBE(byte[] bys, int start, int len) {
		return bytes2Int(bys, start, len, true);
	}

	public static int bytes2IntBE(byte[] bys) {
		return bytes2Int(bys, 0, bys.length, true);
	}

	/**
	 * 锟斤拷锟街斤拷锟斤拷锟斤拷转为 Java 锟叫碉拷 int 锟斤拷值
	 * 
	 * @param bys
	 *            锟街斤拷锟斤拷锟斤拷
	 * @param start
	 *            锟斤拷要转锟斤拷锟斤拷锟斤拷始锟斤拷锟斤拷锟?
	 * @param len
	 *            锟斤拷要转锟斤拷锟斤拷锟街节筹拷锟斤拷
	 * @param isBigEndian
	 *            锟角凤拷锟斤拷 BE锟斤拷true -- BE 锟斤拷false -- LE 锟斤拷
	 * @return
	 */
	private static int bytes2Int(byte[] bys, int start, int len, boolean isBigEndian) {
		int n = 0;
		for (int i = start, k = start + len % (Integer.SIZE / Byte.SIZE + 1); i < k; i++) {
			n |= (bys[i] & 0xff) << ((isBigEndian ? (k - i - 1) : i) * Byte.SIZE);
		}
		return n;
	}

	/**
	 * 锟斤拷 Little-Endian 锟斤拷锟街斤拷锟斤拷锟斤拷转为 long 锟斤拷锟酵碉拷锟斤拷锟?br />
	 * Little-Endian 锟斤拷示锟斤拷位锟街斤拷锟节革拷位锟斤拷锟斤拷锟斤拷
	 * 
	 * @param bys
	 *            锟街斤拷锟斤拷锟斤拷
	 * @param start
	 *            锟斤拷要转锟斤拷锟侥匡拷始锟斤拷锟斤拷位锟斤拷
	 * @param len
	 *            锟斤拷要转锟斤拷锟斤拷锟街斤拷锟斤拷锟斤拷
	 * @return 指锟斤拷锟斤拷始位锟矫和筹拷锟斤拷锟斤拷 LE 锟斤拷式锟斤拷示锟斤拷 long 锟斤拷值
	 */
	public static long bytes2LongLE(byte[] bys, int start, int len) {
		return bytes2Long(bys, start, len, false);
	}

	public static long bytes2LongLE(byte[] bys) {
		return bytes2Long(bys, 0, bys.length, false);
	}

	/**
	 * 锟斤拷 Big-Endian 锟斤拷锟街斤拷锟斤拷锟斤拷转为 long 锟斤拷锟酵碉拷锟斤拷锟?br />
	 * Big-Endian 锟斤拷示锟斤拷位锟街斤拷锟节碉拷位锟斤拷锟斤拷锟斤拷
	 * 
	 * @param bys
	 *            锟街斤拷锟斤拷锟斤拷
	 * @param start
	 *            锟斤拷要转锟斤拷锟侥匡拷始锟斤拷锟斤拷位锟斤拷
	 * @param len
	 *            锟斤拷要转锟斤拷锟斤拷锟街斤拷锟斤拷锟斤拷
	 * @return 指锟斤拷锟斤拷始位锟矫和筹拷锟斤拷锟斤拷 BE 锟斤拷式锟斤拷示锟斤拷 long 锟斤拷值
	 */
	public static long bytes2LongBE(byte[] bys, int start, int len) {
		return bytes2Long(bys, start, len, true);
	}

	public static long bytes2LongBE(byte[] bys) {
		return bytes2Long(bys, 0, bys.length, true);
	}

	private static long bytes2Long(byte[] bys, int start, int len, boolean isBigEndian) {
		long n = 0L;
		for (int i = start, k = start + len % (Long.SIZE / Byte.SIZE + 1); i < k; i++) {
			n |= (bys[i] & 0xffL) << ((isBigEndian ? (k - i - 1) : i) * Byte.SIZE);
		}
		return n;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷ASCII锟街凤拷铣锟揭伙拷锟斤拷纸冢锟?锟界："EF"--> 0xEF
	 * 
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 锟斤拷指锟斤拷锟街凤拷src锟斤拷锟斤拷每锟斤拷锟斤拷锟街凤拷指锟阶拷锟轿?6锟斤拷锟斤拷锟斤拷式 锟界："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] HexStr2Bytes(String src) {
		if(src.length()%2!=0){
			src="0"+src;
		}
		int len = src.length() / 2;
		byte[] ret = new byte[len];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < len; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 锟饺帮拷src锟斤拷锟斤拷len指锟斤拷锟斤拷锟饺ｏ拷然锟斤拷锟斤拷锟紿exStr2Bytes
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] HexStr2BytesLen(String src,int len) {
		if(src.length()<len){
			for(int i = src.length();i < len; i++)
			{
				src="0"+src;				
			}
		}
		return HexStr2Bytes(src);
	}

	/**
	 * 
	 * 锟街凤拷转锟斤拷锟斤拷十锟斤拷锟斤拷锟斤拷址锟?
	 */

	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}


	
	/**
	 * 
	 * 十锟斤拷锟斤拷锟阶拷锟斤拷址锟?
	 */

	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷byte[]锟斤拷锟斤拷锟斤拷铣锟揭伙拷锟?
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static byte[] uniteBytes(byte[] b1, byte[] b2){
		byte[] ret = new byte[b1.length + b2.length];
		System.arraycopy(b1, 0, ret, 0, b1.length);
		System.arraycopy(b2, 0, ret, b1.length, b2.length);
		return ret;
	}
	
	/**
	 * 锟斤拷锟斤拷byte[]锟斤拷锟斤拷b1,b2
	 * @param 源 b2锟斤拷p2锟斤拷始锟斤拷锟斤拷len锟斤拷锟斤拷
	 * @param 目锟斤拷 b1锟斤拷p1锟斤拷始锟斤拷锟斤拷len锟斤拷锟斤拷
	 * @return len or -1
	 */
	public static int bytesCopy(byte[] b1,int p1, byte[] b2,int p2,int len){
		if (b1.length < p1+len)
		{
			return -1;
		}
		for(int i = 0; i < len; i++)
		{
			b1[p1+i] = b2[p2+i];
		}
		return len;
	}

	/**
	 * 锟斤拷锟斤拷锟絙yte锟斤拷锟斤拷全锟斤拷锟斤拷0x00
	 */
	public static void bytesZero(byte[] b){
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (byte)0x00;
		}
		return;
	}
	
	public static void bytesStringZero(byte[] b){
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (byte)0x30;
		}
		return;
	}

}
