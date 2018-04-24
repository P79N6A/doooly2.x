package com.doooly.common.persistence;

public class CustomerContextHolder {
	public static final String DATA_SOURCE_DEFAULT = "dataSource";  
    public static final String DATA_SOURCE_LIFE= "lifeDataSource";  
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
    public static void setCustomerType(String customerType) {  
        contextHolder.set(customerType);  
    }  
    public static String getCustomerType() {  
        return contextHolder.get();  
    }  
    public static void clearCustomerType() {  
        contextHolder.remove();  
    }  
}
