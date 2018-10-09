package com.study._9nameservice;

/**
 * 测试主键生成器
 * 
 * @author xmy
 * @time：2018年10月9日 下午11:35:00
 */
public class TestIdMasker {
	public static void main(String[] args) throws Exception{
		IdMaker idMaker = new IdMaker("it01:2181", "/NameService/IdGen", "ID");
		idMaker.start();
		
		try {
			for (int i = 0; i < 10; i++) {
				String generateId = idMaker.generateId(RemoveMethodEnum.DELAY);
				System.out.println(generateId);
			}
			
		} finally {
			idMaker.stop();
		}
	}
}
