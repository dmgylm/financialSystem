package cn.springmvc.test;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class TestFormula {
	
	public static void main(String[] args) {
		Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        String formula = "a+b";

        binding.setVariable("a",1);
        binding.setVariable("b",2);
        binding.setVariable("c",2);
            Object obj =shell.evaluate("return "+formula);
          System.out.println(Double.valueOf(obj.toString()));
	}

}
