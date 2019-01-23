package com.doooly.common.meituan;

public class Main {

    /**
     * 免登签名
     * @param args
     */
    public static void main(String[] args) {
	// write your code here

//        if (null!= args && args.length >=2) {
//
//
////            System.out.println("签名字段:" + args[0]);
////            System.out.println("私钥:" + args[1]);
//
//            String sign = null;
//            try {
////                System.out.println(sign= RsaUtil.sign(args[0].getBytes(), RsaUtil.loadPrivateKey(args[1])));
//
////                System.out.println(RsaUtil.validate(args[0].getBytes(), RsaUtil.loadPublicKey(RsaUtil.public_key), sign));
////                validate(text.getBytes(), loadPublicKey(my_public_key), sign);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("请输入 [需要签名字段] [私钥]");
//        }
        //Main.channelOrderQueryByOrderSn();
        try {
            RsaUtil.genKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}