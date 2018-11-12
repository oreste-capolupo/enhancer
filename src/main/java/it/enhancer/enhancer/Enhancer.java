package it.enhancer.enhancer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
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

public class Enhancer {

	public static void main(String[] args) throws IOException {
		FileInputStream in = new FileInputStream(
				"/home/oreste/eclipse-workspace/Enhancer/enhancer/files/original_espresso_test.java");
		CompilationUnit cu = JavaParser.parse(in);

		addImportsInCompilationUnit(cu);

		addPrivateField(cu);

		// creates the getActivityInstanceMethod
		addActivityInstanceMethod(cu);

		// visit and print the methods names
		cu.accept(new MethodVisitor(), null);
		// System.out.println(cu.toString());

		// generate enhanced java file
		PrintWriter w = new PrintWriter("enhanced_espresso_test.java", "UTF-8");
		w.print(cu.toString());
		w.close();
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

		String body = "{" + "getInstrumentation().runOnMainSync(new Runnable() {\n"
				+ "            public void run() {\n"
				+ "                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);\n"
				+ "                if (resumedActivities.iterator().hasNext()){\n"
				+ "                    currentActivity = (Activity) resumedActivities.iterator().next();\n"
				+ "                }\n" + "            }\n" + "        });\n" + "\n" + "        return currentActivity;"
				+ "}";

		md.setName("getActivityInstance");
		md.setPublic(true);
		md.setType("Activity");
		BlockStmt b = JavaParser.parseBlock(body);
		md.setBody(b);

		// adds the method at the bottom of the class. The private field
		// "currentActivity" is included in the members
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

				// scan each statement
				for (Statement s : nodes) {
					LogCat log = parseStatement(s);

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
				// remove each statement even if it's not a test to maintain order
				b.remove(tests.get(i));

				LogCat logValues = null;
				// if log != null then the statement is a test and we have to enhance the class
				if ((logValues = logs.get(i)) != null) {
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
				} else
					b.addStatement(tests.get(i));
			}
		}
	}
	
	public static void parseJsonScope(List<String> op, JSONObject j) {
		try {
			parseJsonScope(op, j = j.getJSONObject("scope"));
			op.add(j.getJSONObject("name").getString("identifier"));
			parseJsonArgument(op, j, null, 0);
		} catch (JSONException e) {
			// TODO: handle exception

		}
	}

	public static void parseJsonArgument(List<String> op, JSONObject j, JSONArray a, int i) {
		try {
			if (a == null)
				parseJsonArgument(op, j, a = j.getJSONArray("arguments"), 0);	
			else
				parseJsonArgument(op, j, a = ((JSONObject) a.get(i)).getJSONArray("arguments"), 0);
			methodOverloading(op, a, i);
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}
	
	private static void methodOverloading(List<String> op, JSONArray a, int i) {
		try {
			op.add(a.getJSONObject(i).getJSONObject("name").getString("identifier"));
			parseJsonArgument(op, null, a, ++i);
			methodOverloading(op, a, i);
		} catch (JSONException e) {
			// TODO: handle exception
			try {
				op.add(a.getJSONObject(0).getString("value"));
			} catch (JSONException e2) {
				// TODO: handle exception
			}
		}
	}


	public static LogCat parseStatement(Statement s) {
		LogCat logValues = null;

		if (s.toString().contains("onView")) {
			JsonPrinter printer = new JsonPrinter(true);
			String json = printer.output(s);

			List<String> op = new ArrayList<String>();
			JSONObject j = new JSONObject(json);
//			System.out.println(j.toString());
			j = j.getJSONObject("expression");

			parseJsonScope(op, j);
			op.add(j.getJSONObject("name").getString("identifier"));
			parseJsonArgument(op, j, null, 0);
			
			System.out.println(op.toString());
			
//			logValues = getLog(json, matcher);
		}
		return logValues;
	}

	// withIdModel can be used with identifiers
	// withTextModel can be used with values
	public static LogCat getLog(String json, String matcher) {
		LogCat log = null;

		String searchType = ViewMatchers.getSearchType(matcher);
		String searchKw = "";
		String interactionType = "";

		

		if (!searchType.isEmpty() && !searchKw.isEmpty() && !interactionType.isEmpty()) {
			log = new LogCat(searchType, searchKw, interactionType);
		}
		return log;
	}

}
