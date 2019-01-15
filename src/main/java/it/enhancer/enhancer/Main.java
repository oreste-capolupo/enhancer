package it.enhancer.enhancer;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Enhancer en = new Enhancer("it.feio.android.omninotes");
		
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/A-Kubista/MvvmExample/A-Kubista_MvvmExample_app_src_androidTest_java_com_example_alek_task_mvvm_view_ui_DetailedUserActivityBasicTest.java.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/A-Kubista/MvvmExample/A-Kubista_MvvmExample_app_src_androidTest_java_com_example_alek_task_mvvm_view_ui_UserListActivityBasicTest.java.java");
		//en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/ArunaMahaGamage/edx-app-android-master-current/ArunaMahaGamage_edx-app-android-master-current_OpenEdXMobile_src_androidTest_java_org_edx_mobile_test_feature_interactor_RegistrationScreenInteractor.java.java");
		
		//en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/AlexeyPakhtusov/AndroidExamples/AlexeyPakhtusov_AndroidExamples_GithubDagger_app_src_androidTest_java_ru_gdgkazan_githubmvp_screen_walkthrough_WalkthroughActivityTest.java.java");
		
		//en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/EricChows/SystemUI/EricChows_SystemUI_Smartron_frameworks_base_core_tests_coretests_src_android_widget_TextViewActivityMouseTest.java.java");
		
		/*en.generateEnhancedClassFrom("/home/oreste/eclipse-workspace/Enhancer/enhancer/files/BaseEspressoTest.java");
		en.generateEnhancedClassFrom("/home/oreste/eclipse-workspace/Enhancer/enhancer/files/OriginalEspressoTest.java");

		//Statistic.statisticsOnListOfFiles("F:\\results_msr\\sorted_file_names.txt", "F:\\Espresso_new_projects\\espresso\\");

		
		Statistic.createCSVLine("F:\\Espresso_new_projects\\espresso\\", "ASDFDev/PAS-Quiet-Android/app/src/androidTest/java/com/setsuna/client/quiet/com/setsuna/client/quiet/debug/LecturerTest.java");
		
		/*
		en = new Enhancer("org.andydyer.androidtestdemo");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/abdyer/android-test-demo/_app_src_androidTest_java_org_andydyer_androidtestdemo_LoginActivityTest.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/abdyer/android-test-demo/_app_src_androidTest_java_org_andydyer_androidtestdemo_MainActivityTest.java");
		
		en = new Enhancer("org.ado.minesync");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/adelolmo/mine-sync/_minesync-instrumentation_src_main_java_org_ado_minesync_BasicTest.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/adelolmo/mine-sync/_minesync-instrumentation_src_main_java_org_ado_minesync_MineSyncConfigActivityTest.java");
		
		en = new Enhancer("UITests");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/WheresMyBus/android/_app_src_androidTest_java_UITests_TestHomePage.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/WheresMyBus/android/_app_src_androidTest_java_UITests_TestSubmitAlert.java");*/
	}
}
