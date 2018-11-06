package it.enhancer.enhancer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.*;
import com.github.javaparser.printer.JsonPrinter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.google.gson.Gson;

import it.enhancer.enhancer.withIdModel.*;
import it.enhancer.enhancer.withTextModel.*;

public class Enhancer {

	public static void main(String[] args) throws IOException {
		FileInputStream in = new FileInputStream(
				"/home/oreste/eclipse-workspace/Enhancer/enhancer/files/original_espresso_test.java");
		CompilationUnit cu = JavaParser.parse(in);
		
		addImportsInCompilationUnit(cu);
		
		addPrivateField(cu);
		
		//creates the getActivityInstanceMethod
		addActivityInstanceMethod(cu);
		
		// visit and print the methods names
		cu.accept(new MethodVisitor(), null);
		System.out.println(cu.toString());

		//generate enhanced java file
		PrintWriter w = new PrintWriter("enhanced_espresso_test.java", "UTF-8");
		w.print(cu.toString());
		w.close();
		
		// prints the resulting compilation unit to default system output
		// System.out.println(cu.toString());
	}

	private static void addPrivateField(CompilationUnit cu) {
		ClassOrInterfaceDeclaration ci = Navigator.findNodeOfGivenClass(cu, ClassOrInterfaceDeclaration.class);
		
		BodyDeclaration<?> field = JavaParser.parseBodyDeclaration("private Activity currentActivity;");
		ci.getMembers().add(0, field);
	}

	private static void addImportsInCompilationUnit(CompilationUnit cu) {
		// imports only if it does not exist
		cu.addImport("it.feio.android.omninotes.TOGGLETools", false, false);
		cu.addImport("java.util.Date", false, false);
		cu.addImport("android.app.Activity", false, false);
		cu.addImport("android.app.Instrumentation", false, false);
		cu.addImport("java.util.Collection", false, false);
		cu.addImport("android.support.test.InstrumentationRegistry", false, false);
		cu.addImport("android.util.Log", false, false);
	}

	private static void addActivityInstanceMethod(CompilationUnit cu) {
		ClassOrInterfaceDeclaration ci = Navigator.findNodeOfGivenClass(cu, ClassOrInterfaceDeclaration.class);
		MethodDeclaration md = new MethodDeclaration();
		
		String body = "{"
				+ "getInstrumentation().runOnMainSync(new Runnable() {\n" + 
				"            public void run() {\n" + 
				"                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);\n" + 
				"                if (resumedActivities.iterator().hasNext()){\n" + 
				"                    currentActivity = (Activity) resumedActivities.iterator().next();\n" + 
				"                }\n" + 
				"            }\n" + 
				"        });\n" + 
				"\n" + 
				"        return currentActivity;"
				+ "}";
		
		md.setName("getActivityInstance");
		md.setPublic(true);
		md.setType("Activity");
		BlockStmt b = JavaParser.parseBlock(body);
		md.setBody(b);
		
		//adds the method at the bottom of the class. The private field "currentActivity" is included in the members
		ci.getMembers().add(ci.getMembers().size(), md);
	}

	private static class MethodVisitor extends ModifierVisitor<Void> {
		@Override
		public BlockStmt visit(BlockStmt n, Void arg) {
			/*
			 * here you can access the attributes of the method. this method will be called
			 * for all methods in this CompilationUnit, including inner class methods
			 */
			super.visit(n, arg);
			if (n.toString().contains("onView")) {

				NodeList<Statement> nodes = n.getStatements();
				NodeList<Statement> tests = new NodeList<Statement>();
				List<LogCat> logs = new ArrayList<LogCat>();

				// the first part of this model is the same for these viewMatchers( withId,
				// withText, withContentDescription )
				WithIdModel model = new WithIdModel();

				// scan each statement
				for (Statement s : nodes) {
					LogCat log = parseStatement(model, s);

					// add each statement, if is not a test then log == null
					logs.add(log);
					tests.add(s);
				}

				if (logs.size() > 0 && tests.size() > 0)
					enhanceMethod(n, logs, tests);
			}
			return n;
		}

		private void enhanceMethod(BlockStmt b, List<LogCat> logs, NodeList<Statement> tests) {
			Statement firstTestDate = JavaParser.parseStatement("Date now = new Date();");
			Statement date = JavaParser.parseStatement("now = new Date();");
			Statement firstTestActivity = JavaParser.parseStatement("Activity activity = getActivityInstance();");
			Statement activity = JavaParser.parseStatement("activity = getActivityInstance();");
			Statement screenCapture = JavaParser.parseStatement("TOGGLETools.TakeScreenCapture(now, activity);");
			Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");
			TryStmt tryStmt = (TryStmt) JavaParser.parseStatement("try {\n" + "            Thread.sleep(2000);\n"
					+ "        } catch (Exception e) {\n" + "\n" + "        }");

			boolean firstTest = true;

			for (int i = 0; i < logs.size(); i++) {
				LogCat logValues = null;
				// if log != null then the statement is a test and we have to enhance the class
				if ((logValues = logs.get(i)) != null) {
					b.remove(tests.get(i));
					
					if (firstTest) {
						firstTest = false;
						b.addStatement(firstTestDate);
						b.addStatement(firstTestActivity);
					} else {
						b.addStatement(date);
						b.addStatement(activity);
					}
						
					Statement log = JavaParser.parseStatement("Log.d(\"touchtest\", now.getTime() + \", \" + \""
							+ logValues.getSearchType() + "\" +" + " \", \" + \"" + logValues.getSearchKw()
							+ "\" + \", \" + \"" + logValues.getInteractionType() + "\");");
					b.addStatement(log);

					b.addStatement(screenCapture);
					b.addStatement(dumpScreen);
					b.addStatement(tests.get(i));
					b.addStatement(tryStmt);
				}
			}
		}
	}

	public static LogCat parseStatement(WithIdModel m, Statement s) {
		LogCat logValues = null;

		if (s.toString().contains("onView")) {
			Statement ps = JavaParser.parseStatement(s.toString());
			JsonPrinter printer = new JsonPrinter(true);
			String json = printer.output(s);

			Gson gson = new Gson();
			m = gson.fromJson(json, WithIdModel.class);

			// get the viewMatcher of this statement
			it.enhancer.enhancer.withIdModel.Argument argument = m.getExpression().getScope().getArguments().get(0);
			String matcher = argument.getName().getIdentifier();

			logValues = getLog(json, matcher);

			if (logValues != null) {
				System.out.println(logValues.getSearchType());
				System.out.println(logValues.getSearchKw());
				System.out.println(logValues.getInteractionType());
			}

			// System.out.println(m.getExpression().getScope().getArguments().get(0).getArguments().get(0).getName().getIdentifier());

			JSONObject jsonObject = new JSONObject(printer.output(ps));
			System.out.println(jsonObject.toString());
			// System.out.println(s.toString());
		}
		return logValues;
	}

	// withIdModel can be used with identifiers
	// withTextModel can be used with values
	public static LogCat getLog(String json, String matcher) {
		Gson gson = new Gson();
		LogCat log = null;

		String searchType = ViewMatcher.getSearchType(matcher);
		String searchKw = "";
		String interactionType = "";

		switch (matcher) {
		case "withId":
			// using withId model
			WithIdModel im = gson.fromJson(json, WithIdModel.class);
			it.enhancer.enhancer.withIdModel.Argument ia = im.getExpression().getScope().getArguments().get(0);
			searchKw = ia.getArguments().get(0).getName().getIdentifier();
			// perform and click
			// System.out.println(im.getExpression().getName().getIdentifier());
			interactionType = im.getExpression().getArguments().get(0).getName().getIdentifier();
			break;

		case "withText":
		case "withContentDescription":
			// using withText model
			WithTextModel tm = gson.fromJson(json, WithTextModel.class);
			it.enhancer.enhancer.withTextModel.Argument ta = tm.getExpression().getScope().getArguments().get(0);
			searchKw = ta.getArguments().get(0).getValue();

			// perform and click
			// System.out.println(tm.getExpression().getName().getIdentifier());
			interactionType = tm.getExpression().getArguments().get(0).getName().getIdentifier();
			break;
		}

		if (!searchType.isEmpty() && !searchKw.isEmpty() && !interactionType.isEmpty()) {
			log = new LogCat(searchType, searchKw, interactionType);
		}
		return log;
	}

}
