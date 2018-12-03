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

	public static CompilationUnit cu;
	public static List<Operation> operations;
	public static boolean firstTest;
	public static StringBuilder parameters;
	public static StringBuilder field;

	public static void main(String[] args) throws IOException {
		FileInputStream in = new FileInputStream("files/original_espresso_test.java");
		cu = JavaParser.parse(in);

		addImportsInCompilationUnit();

		addPrivateField();

		addActivityInstanceMethod();

		// visit the body of all methods in the class
		cu.accept(new MethodVisitor(), null);
		// System.out.println(cu.toString());

		// generate enhanced java file
		PrintWriter w = new PrintWriter("files/enhanced_espresso_test.java", "UTF-8");
		w.print(cu.toString());
		w.close();
	}

	private static void addPrivateField() {
		ClassOrInterfaceDeclaration ci = Navigator.findNodeOfGivenClass(cu, ClassOrInterfaceDeclaration.class);

		BodyDeclaration<?> field = JavaParser.parseBodyDeclaration("private Activity currentActivity;");
		ci.getMembers().add(0, field);
	}

	private static void addImportsInCompilationUnit() {
		// imports only if it does not exist
		cu.addImport("it.feio.android.omninotes.TOGGLETools", false, false);
		cu.addImport("java.util.Date", false, false);
		cu.addImport("android.app.Activity", false, false);
		cu.addImport("android.app.Instrumentation", false, false);
		cu.addImport("java.util.Collection", false, false);
		cu.addImport("android.support.test.InstrumentationRegistry", false, false);
		cu.addImport("android.widget.TextView", false, false);
		cu.addImport("android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry", false, false);
	}

	private static void addActivityInstanceMethod() {
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
				firstTest = true;
				parameters = new StringBuilder("");
				field = new StringBuilder("");

				// scan each statement
				int i = 0;
				while (i < nodes.size())
					// gets the new index because the method has been enhanced
					i = parseStatement(n, nodes.get(i), i);
			}
			return n;
		}

	}

	public static void parseJsonScope(JSONObject j) {
		try {
			parseJsonScope(j = j.getJSONObject("scope"));
			// gets onView or onData and all nested performs and checks but the last one
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
			else {
				parseJsonArgument(j, a = ((JSONObject) a.get(i)).getJSONArray("arguments"), 0);
				parseScopeInArgument((JSONObject) a.get(0));
			}

			// field is empty if the parameter is not a FieldAccessExpr otherwise contains
			// only the first part ES: obj. or R.id.
			methodOverloading(a, i);
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}

	private static void parseScopeInArgument(JSONObject j) {
		try {
			if (field.toString().isEmpty()) {
				parseScopeInArgument(j = j.getJSONObject("scope"));
				String type = j.getString("type");
				String name = j.getJSONObject("name").getString("identifier");

				if (!type.equals("MethodCallExpr"))
					field.append(name + ".");
				else
					field.append(name + "(");

				parseJsonArgument(j, null, 0);

				if (type.equals("MethodCallExpr")) {
					if (field.charAt(field.length() - 1) == ',')
						field.deleteCharAt(field.length() - 1);
					field.append(").");
				}
			}
		} catch (JSONException e) {
			try {
				parseScopeInArgument(j.getJSONObject("name"));
			} catch (JSONException e2) {
				// TODO: handle exception
			}
		}
	}

	private static void methodOverloading(JSONArray a, int i) {
		try {
			String type = a.getJSONObject(i).getString("type");
			String name = a.getJSONObject(i).getJSONObject("name").getString("identifier");

			if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
					&& !field.toString().startsWith("ViewMatchers.") && !field.toString().startsWith("ViewActions.")
					&& isNotAnEspressoCommand(name)) {
				String fd = field.toString();
				name = fd.concat(name);
			}

			if (type.equals("MethodCallExpr") && isNotAnEspressoCommand(name)) {
				if (parameters.toString().isEmpty())
					parameters.append(name + "()");
				else
					parameters = new StringBuilder(name + "(" + parameters.toString() + ")");
				field = new StringBuilder("");

			} else if (type.equals("MethodCallExpr") && !isNotAnEspressoCommand(name)) {
				operations.add(new Operation(name, parameters.toString()));
				parameters = new StringBuilder("");
				field = new StringBuilder("");
			}

			parseJsonArgument(null, a, ++i);
			methodOverloading(a, i);

		} catch (JSONException e) {
			// add parameters to the operation list
			methodParameters(a, 0);
		}
	}

	private static boolean isNotAnEspressoCommand(String name) {
		String[] actions = { "click", "longClick", "doubleClick", "typeText", "replaceText", "clearText", "check",
				"matches", "isDisplayed", "allOf" };

		for (String a : actions) {
			if (a.equals(name))
				return false;
		}

		if (!ViewMatchers.getSearchType(name).equals(""))
			return false;
		return true;
	}

	private static void methodParameters(JSONArray a, int j) {
		try {
			String type = a.getJSONObject(j).getString("type");
			String value = a.getJSONObject(j).getString("value");

			if (field.toString().isEmpty()) {
				if (type.equals("StringLiteralExpr")) {
					if (parameters.toString().isEmpty())
						parameters.append("\"" + value + "\"");
					else
						parameters.append("," + "\"" + value + "\"");
				} else {
					if (parameters.toString().isEmpty())
						parameters.append(value);
					else
						parameters.append("," + value);
				}
			} else {
				if (type.equals("StringLiteralExpr"))
					field.append("\"" + value + "\"" + ",");
				else
					field.append(value + ",");
			}

			methodParameters(a, ++j);
		} catch (JSONException e) {
			try {
				String type = a.getJSONObject(j).getString("type");
				String name = "";
				String index = "";

				if (type.equals("ArrayAccessExpr")) {
					name = a.getJSONObject(j).getJSONObject("name").getJSONObject("name").getString("identifier");
					if (a.getJSONObject(j).getJSONObject("index").getString("type").equals("NameExpr"))
						index = a.getJSONObject(j).getJSONObject("index").getJSONObject("name").getString("identifier");
					else
						index = a.getJSONObject(j).getJSONObject("index").getString("value");
				} else
					name = a.getJSONObject(j).getJSONObject("name").getString("identifier");

				if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
						&& !field.toString().startsWith("ViewMatchers.") && !field.toString().startsWith("ViewActions.")
						&& isNotAnEspressoCommand(name)) {
					field.append(name + ",");
					name = field.toString();
				}

				if (!type.equals("MethodCallExpr") || (type.equals("MethodCallExpr") && isNotAnEspressoCommand(name)
						&& !parameters.toString().contains(name + "("))) {

					// field access
					if (type.equals("FieldAccessExpr") && field.toString().startsWith("R.id.")) {
						if (parameters.toString().isEmpty())
							parameters.append("\"" + name + "\"");
						else
							parameters.append("," + "\"" + name + "\"");

						field = new StringBuilder("");

						// field access
					} else if (type.equals("FieldAccessExpr")) {
						if (parameters.toString().isEmpty())
							parameters.append(name.substring(0, name.length() - 1));
						else
							parameters.append("," + name.substring(0, name.length() - 1));

						// array access
					} else if (type.equals("ArrayAccessExpr")) {
						if (name.charAt(name.length() - 1) != ',') {

							if (parameters.toString().isEmpty())
								parameters.append(name + "[" + index + "]");
							else
								parameters.append("," + name + "[" + index + "]");
						} else {
							if (parameters.toString().isEmpty())
								parameters.append(name.substring(0, name.length() - 1) + "[" + index + "]");
							else
								parameters.append("," + name.substring(0, name.length() - 1) + "[" + index + "]");
						}

						// name expr
					} else {
						if (field.toString().isEmpty()) {
							if (parameters.toString().isEmpty())
								parameters.append(name);
							else
								parameters.append("," + name);
						}
					}
				}
				methodParameters(a, ++j);
			} catch (JSONException e1) {
				// TODO: handle exception
			}
		}
	}

	public static int parseStatement(BlockStmt b, Statement s, int i) {
		int index = i;
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
				if (j.getJSONObject("name").getString("identifier").equals("check"))
					operations.add(new Operation(j.getJSONObject("name").getString("identifier"), ""));

				parseJsonArgument(j, null, 0);

				System.out.println(operations.toString());

				// returns the next index after enhancing the method
				return index = enhanceMethod(b, s, i);
			} catch (JSONException e) {
				// CAN'T PARSE STATEMENT
			}
		}
		// return the next index if the statement is not a test
		return ++index;
	}

	private static int enhanceMethod(BlockStmt b, Statement s, int i) {
		Statement instrumentation = JavaParser.parseStatement("Instrumentation instr = InstrumentationRegistry.getInstrumentation();");
		Statement device = JavaParser.parseStatement("UiDevice device = UiDevice.getInstance(instr);");
		Statement firstTestDate = JavaParser.parseStatement("Date now = new Date();");
		Statement date = JavaParser.parseStatement("now = new Date();");
		Statement firstTestActivity = JavaParser.parseStatement("Activity activity = getActivityInstance();");
		Statement activity = JavaParser.parseStatement("activity = getActivityInstance();");
		Statement screenCapture = JavaParser.parseStatement("TOGGLETools.TakeScreenCapture(now, activity);");
		Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");
		TryStmt tryStmt = (TryStmt) JavaParser.parseStatement("try {\n" + "            Thread.sleep(2000);\n"
				+ "        } catch (Exception e) {\n" + "\n" + "        }");

		LogCat log = null;

		// this works only on test cases with one matcher and one action
		String searchType = ViewMatchers.getSearchType(operations.get(0).getName());
		String searchKw = operations.get(0).getParameter();
		String interactionType = operations.get(1).getName();
		String interactionParams = operations.get(1).getParameter();

		if (!searchType.isEmpty() && !interactionType.isEmpty())
			log = new LogCat(searchType, searchKw, interactionType, interactionParams);

		String stmtString = s.toString();
		Statement st = JavaParser.parseStatement(stmtString);
		b.remove(s);

		if (firstTest) {
			firstTest = false;
			b.addStatement(i, instrumentation);
			b.addStatement(++i, device);
			b.addStatement(++i, firstTestDate);
			b.addStatement(++i, firstTestActivity);
		} else {
			b.addStatement(i, date);
			b.addStatement(++i, activity);
		}

		Statement l = null;
		String stmt = "";

		// default handles the normal behavior of the parameters. ES: click(),
		// typeText("TextToBeReplaced")
		switch (interactionType) {
		case "replaceText":
			// the 'i' in the variable name is used to make it unique in case we have
			// multiple interactions of the same type
			// substring removes the " from the string
			stmt = "int textToBeReplacedLength" + i + " = ((TextView) activity.findViewById(R.id."
					+ searchKw.substring(1, searchKw.length() - 1) + ")).getText().length();";
			b.addStatement(++i, JavaParser.parseStatement(stmt));

			l = JavaParser.parseStatement(
					"TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw()
							+ "," + "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength"
							+ (i - 1) + ")+\";\"+" + log.getInteractionParams() + ");");
			break;
		case "clearText":
			stmt = "int textToBeClearedLength" + i + " = ((TextView) activity.findViewById(R.id."
					+ searchKw.substring(1, searchKw.length() - 1) + ")).getText().length();";
			b.addStatement(++i, JavaParser.parseStatement(stmt));

			l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\"" + ","
					+ log.getSearchKw() + "," + "\"" + log.getInteractionType()
					+ "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");
			break;
		default:
			if (log.getInteractionParams().isEmpty())
				l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\""
						+ "," + log.getSearchKw() + "," + "\"" + log.getInteractionType() + "\"" + ");");
			else
				l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\""
						+ "," + log.getSearchKw() + "," + "\"" + log.getInteractionType() + "\"" + ","
						+ log.getInteractionParams() + ");");
			break;
		}

		b.addStatement(++i, l);

		b.addStatement(++i, screenCapture);
		b.addStatement(++i, dumpScreen);
		b.addStatement(++i, st);
		b.addStatement(++i, tryStmt);

		return ++i;
	}

}
