package it.enhancer.enhancer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		
		
		Enhancer en;
		
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
		en.generateEnhancedClassFrom("/home/oreste/Scaricati/Tests/WheresMyBus/android/_app_src_androidTest_java_UITests_TestSubmitAlert.java");
		*/
	}
}
