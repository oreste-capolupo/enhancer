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

	public static List<Operation> operations;

	public static void main(String[] args) throws IOException {
		FileInputStream in = new FileInputStream(
				"/home/oreste/eclipse-workspace/Enhancer/enhancer/files/original_espresso_test.java");
		CompilationUnit cu = JavaParser.parse(in);

		addImportsInCompilationUnit(cu);

		addPrivateField(cu);

		addActivityInstanceMethod(cu);

		// visit the body of all methods in the class
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
			String body = n.toString();
			if (body.contains("onView") || body.contains("onData")) {

				NodeList<Statement> nodes = n.getStatements();
				NodeList<Statement> tests = new NodeList<Statement>();
				List<LogCat> logs = new ArrayList<LogCat>();

				// scan each statement
				for (Statement s : nodes) {
					LogCat log = parseStatement(s);
					
					// if "s" is not a test then log == null
					logs.add(log);

					// copy each statement because using nodes in enhanceMethod removes the elements
					// from the compilation unit and goes outOfBounds
					tests.add(s);
				}
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
				// remove each statement from the compilation unit even if it's not a test to
				// maintain order
				b.remove(tests.get(i));

				LogCat logValues = null;
				// if log != null then the statement is a test and we need to enhance the method
				if ((logValues = logs.get(i)) != null) {
					if (firstTest) {
						firstTest = false;
						b.addStatement(firstTestDate);
						b.addStatement(firstTestActivity);
					} else {
						b.addStatement(date);
						b.addStatement(activity);
					}
					Statement log = null;
					if (logValues.getInteractionParams().isEmpty())
						log = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\""
								+ logValues.getSearchType() + "\"" + "," + "\"" + logValues.getSearchKw() + "\"" + ","
								+ "\"" + logValues.getInteractionType() + "\"" + ");");
					else
						log = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\""
								+ logValues.getSearchType() + "\"" + "," + "\"" + logValues.getSearchKw() + "\"" + ","
								+ "\"" + logValues.getInteractionType() + "\"" + "," + "\""
								+ logValues.getInteractionParams() + "\"" + ");");

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

	public static void parseJsonScope(JSONObject j) {
		try {
			parseJsonScope(j = j.getJSONObject("scope"));
			// gets onView or onData and all embedded performs or checks but the last one
			// System.out.println("1: "+j.getJSONObject("name").getString("identifier"));
			parseJsonArgument(j, null, 0);
		} catch (JSONException e) {
			// TODO: handle exception

		}
	}

	public static void parseJsonArgument(JSONObject j, JSONArray a, int i) {
		try {
			if (a == null)
				parseJsonArgument(j, a = j.getJSONArray("arguments"), 0);
			else
				parseJsonArgument(j, a = ((JSONObject) a.get(i)).getJSONArray("arguments"), 0);
			methodOverloading(a, i);
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}

	private static void methodOverloading(JSONArray a, int i) {
		try {
			String name = a.getJSONObject(i).getJSONObject("name").getString("identifier");

			if (a.getJSONObject(i).getString("type").equals("FieldAccessExpr"))
				operations.add(new Operation("", name));
			else if (operations.size() == 0 || operations.get(operations.size() - 1).getName() != "")
				operations.add(new Operation(name, ""));
			else
				operations.get(operations.size() - 1).setName(name);

			parseJsonArgument(null, a, ++i);
			methodOverloading(a, i);
		} catch (JSONException e) {
			// TODO: handle exception
			try {
				operations.add(new Operation("", a.getJSONObject(0).getString("value")));
			} catch (JSONException e2) {
				// TODO: handle exception
			}
		}
	}

	public static LogCat parseStatement(Statement s) {
		LogCat logValues = null;

		if (s.toString().contains("onView") || s.toString().contains("onData")) {
			JsonPrinter printer = new JsonPrinter(true);
			String json = printer.output(s);

			operations = new ArrayList<Operation>();

			try {
				JSONObject j = new JSONObject(json);
				// System.out.println(j.toString());
				j = j.getJSONObject("expression");

				parseJsonScope(j);
				// gets the last check or perform
				// System.out.println("2: "+j.getJSONObject("name").getString("identifier"));
				parseJsonArgument(j, null, 0);

				System.out.println(operations.toString());

				logValues = getLog();
			} catch (JSONException e) {
				// CAN'T PARSE STATEMENT
			}
		}
		return logValues;
	}

	public static LogCat getLog() {
		LogCat log = null;

		// this works only on test cases with one matcher and one action
		String searchType = ViewMatchers.getSearchType(operations.get(0).getName());
		String searchKw = operations.get(0).getParameter();
		String interactionType = operations.get(1).getName();
		String interactionParams = operations.get(1).getParameter();;

		if (!searchType.isEmpty() && !interactionType.isEmpty()) {
			if (interactionParams.isEmpty())
				log = new LogCat(searchType, searchKw, interactionType, "");
			else
				log = new LogCat(searchType, searchKw, interactionType, interactionParams);
		}
		return log;
	}

}
