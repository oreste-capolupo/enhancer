package it.enhancer.enhancer;

public class Main {
	public static void main(String[] args) {
		Enhancer en = new Enhancer("/home/oreste/eclipse-workspace/Enhancer/enhancer/", "it.feio.android.omninotes");
		en.generateEnhancedClassFrom("/home/oreste/eclipse-workspace/Enhancer/enhancer/files/BaseEspressoTest.java");
		en.generateEnhancedClassFrom("/home/oreste/eclipse-workspace/Enhancer/enhancer/files/OriginalEspressoTest.java");
		
		en = new Enhancer("/home/oreste/Scaricati/Tests/abdyer/android-test-demo/", "org.andydyer.androidtestdemo");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/abdyer/android-test-demo/_app_src_androidTest_java_org_andydyer_androidtestdemo_LoginActivityTest.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/abdyer/android-test-demo/_app_src_androidTest_java_org_andydyer_androidtestdemo_MainActivityTest.java");
		
		en = new Enhancer("/home/oreste/Scaricati/Tests/adelolmo/mine-sync/", "org.ado.minesync");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/adelolmo/mine-sync/_minesync-instrumentation_src_main_java_org_ado_minesync_BasicTest.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/adelolmo/mine-sync/_minesync-instrumentation_src_main_java_org_ado_minesync_MineSyncConfigActivityTest.java");
		
		en = new Enhancer("/home/oreste/Scaricati/Tests/WheresMyBus/android/", "UITests");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/WheresMyBus/android/_app_src_androidTest_java_UITests_TestHomePage.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/WheresMyBus/android/_app_src_androidTest_java_UITests_TestSubmitAlert.java");
	}
}
