package it.enhancer.enhancer;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Enhancer en = new Enhancer("it.feio.android.omninotes");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/ThePassEditActivity.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/passandroid1.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/AntennaPod/AntennaPod/AntennaPod_AntennaPod_app_src_androidTest_java_de_test_antennapod_ui_PreferencesTest.java.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/aragaer/yama_android/aragaer_yama_android_src_androidTest_java_com_aragaer_yama_ListActivityTest.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/Leonis0813/adhafera/Leonis0813_adhafera_app_src_androidTest_java_com_leonis_android_adhafera_MainActivityTest.java.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/Elvisthacoder/Android/Elvisthacoder_Android_combined_iosched-master_android_src_androidTest_java_com_google_samples_apps_iosched_videolibrary_VideoLibraryActivityTest.java.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/Benjythebee/android-support/Benjythebee_android-support_design_tests_src_android_support_design_widget_TextInputLayoutTest.java.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/Catrobat/Catroid/Catrobat_Catroid_catroid_src_androidTest_java_org_catrobat_catroid_uiespresso_ui_fragment_CopyLookTest.java.java");
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/only_test_classes/Bondes87/ShppCourseAndroid/Bondes87_ShppCourseAndroid_PersonalNotes_app_src_androidTest_java_com_dbondarenko_shpp_personalnotes_BaseUITest.java.java");

		Statistic.statisticsOnListOfFiles("F:\\results_msr\\sorted_file_names_try.txt", "F:\\Espresso_new_projects\\espresso\\");

		
		//Statistic.createCSVLine("F:\\Espresso_new_projects\\espresso\\", "ASDFDev/PAS-Quiet-Android/app/src/androidTest/java/com/setsuna/client/quiet/com/setsuna/client/quiet/debug/LecturerTest.java");
		
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
