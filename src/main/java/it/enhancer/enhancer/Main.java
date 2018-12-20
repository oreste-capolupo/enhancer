package it.enhancer.enhancer;

public class Main {
	public static void main(String[] args) {
		Enhancer en = new Enhancer("/home/oreste/eclipse-workspace/Enhancer/enhancer/");
		en.generateEnhancedClassFrom("/home/oreste/eclipse-workspace/Enhancer/enhancer/files/BaseEspressoTest.java");
		en.generateEnhancedClassFrom("/home/oreste/eclipse-workspace/Enhancer/enhancer/files/OriginalEspressoTest.java");
	}
}
