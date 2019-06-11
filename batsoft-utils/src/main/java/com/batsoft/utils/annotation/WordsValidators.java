package com.batsoft.utils.annotation;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

//import org.junit.Test;

/**
 * 敏感词过滤
 * @author Bat Admin
 *
 */
public class WordsValidators implements ConstraintValidator<Words, String> {

	/**
	 * 敏感词上传路径
	 */
	public final static String SENSITIVE_UPLOAD_URL = "/sensitive";
	
	@Value("${batsoft.file.basepath}")
	public  String fileBasePath;
	
	@Override
	public void initialize(Words wordsAnnotation) {
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext content) {
		
		SensitivewordFilter filter = new SensitivewordFilter(fileBasePath + SENSITIVE_UPLOAD_URL);
		Set<String> set = filter.getSensitiveWord(value, 1);
		int size = set.size();
		return size > 0 ? false : true;
	}
	
//	@Test
//	public void testWords(){
//		AnnotationDescriptor<Words> desc = new AnnotationDescriptor<Words>(Words.class);
//		Words words = AnnotationFactory.create(desc);
//		WordsValidators wordsValidate = new WordsValidators();
//		wordsValidate.initialize(words);
//		//Assert.assertTrue(!wordsValidate.isValid("123", null));
//	}

}
