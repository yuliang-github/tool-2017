package com.ala.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ala.tool.EncryptServiceImpl;

//针对字段的注解
@Target(ElementType.FIELD)
//保留时间，一般注解就是为了框架开发时代替配置文件使用，JVM运行时用反射取参数处理，所以一般都为RUNTIME类型  
@Retention(RetentionPolicy.RUNTIME)
//用于描述其它类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化
@Documented 
public @interface NeedEncrypt {
	
	//是否需要解密
	public boolean need() default true;
	
	//解密类型 目前只支持EncryptServiceImpl
	@SuppressWarnings("rawtypes")
	public Class type() default EncryptServiceImpl.class;
	
}
