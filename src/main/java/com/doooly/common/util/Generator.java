package com.doooly.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Generator {
		
		private static final Logger log = LoggerFactory.getLogger(Generator.class);
	
		private final static SnowflakeIdWorker worker = new SnowflakeIdWorker(31, 31) ;
		
		public static long nextValue() {
			long value =  worker.nextId();
			return value;
		}
        
   }  
