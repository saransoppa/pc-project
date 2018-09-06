package com.aws.lambda.function.plans;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Hello world!
 *
 */
public class Sample implements RequestHandler<Object, String>  
{
	public String handleRequest(Object input, Context context) {
		return "This is my simple lambda function ..!";
	}
}
