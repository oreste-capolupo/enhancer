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
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import it.enhancer.enhancer.withIdModel.*;
import it.enhancer.enhancer.withTextModel.*;

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

		String json = "{\n" + 
				"  \"expression\": {\n" + 
				"    \"scope\": {\n" + 
				"      \"scope\": {\n" + 
				"        \"name\": {\n" + 
				"          \"identifier\": \"onView\",\n" + 
				"          \"type\": \"SimpleName\"\n" + 
				"        },\n" + 
				"        \"arguments\": [\n" + 
				"          {\n" + 
				"            \"name\": {\n" + 
				"              \"identifier\": \"withId\",\n" + 
				"              \"type\": \"SimpleName\"\n" + 
				"            },\n" + 
				"            \"arguments\": [\n" + 
				"              {\n" + 
				"                \"scope\": {\n" + 
				"                  \"scope\": {\n" + 
				"                    \"name\": {\n" + 
				"                      \"identifier\": \"R\",\n" + 
				"                      \"type\": \"SimpleName\"\n" + 
				"                    },\n" + 
				"                    \"type\": \"NameExpr\"\n" + 
				"                  },\n" + 
				"                  \"name\": {\n" + 
				"                    \"identifier\": \"id\",\n" + 
				"                    \"type\": \"SimpleName\"\n" + 
				"                  },\n" + 
				"                  \"type\": \"FieldAccessExpr\"\n" + 
				"                },\n" + 
				"                \"name\": {\n" + 
				"                  \"identifier\": \"fab_expand_menu_button\",\n" + 
				"                  \"type\": \"SimpleName\"\n" + 
				"                },\n" + 
				"                \"type\": \"FieldAccessExpr\"\n" + 
				"              }\n" + 
				"            ],\n" + 
				"            \"type\": \"MethodCallExpr\"\n" + 
				"          }\n" + 
				"        ],\n" + 
				"        \"type\": \"MethodCallExpr\"\n" + 
				"      },\n" + 
				"      \"name\": {\n" + 
				"        \"identifier\": \"perform\",\n" + 
				"        \"type\": \"SimpleName\"\n" + 
				"      },\n" + 
				"      \"arguments\": [\n" + 
				"        {\n" + 
				"          \"name\": {\n" + 
				"            \"identifier\": \"typeText\",\n" + 
				"            \"type\": \"SimpleName\"\n" + 
				"          },\n" + 
				"          \"arguments\": [\n" + 
				"            {\n" + 
				"              \"type\": \"IntegerLiteralExpr\",\n" + 
				"              \"value\": \"0\"\n" + 
				"            }\n" + 
				"          ],\n" + 
				"          \"type\": \"MethodCallExpr\"\n" + 
				"        },\n" + 
				"        {\n" + 
				"          \"name\": {\n" + 
				"            \"identifier\": \"click\",\n" + 
				"            \"type\": \"SimpleName\"\n" + 
				"          },\n" + 
				"          \"type\": \"MethodCallExpr\"\n" + 
				"        }\n" + 
				"      ],\n" + 
				"      \"type\": \"MethodCallExpr\"\n" + 
				"    },\n" + 
				"    \"name\": {\n" + 
				"      \"identifier\": \"check\",\n" + 
				"      \"type\": \"SimpleName\"\n" + 
				"    },\n" + 
				"    \"arguments\": [\n" + 
				"      {\n" + 
				"        \"name\": {\n" + 
				"          \"identifier\": \"doesNotExist\",\n" + 
				"          \"type\": \"SimpleName\"\n" + 
				"        },\n" + 
				"        \"type\": \"MethodCallExpr\"\n" + 
				"      },\n" + 
				"      {\n" + 
				"        \"name\": {\n" + 
				"          \"identifier\": \"doesNotExist\",\n" + 
				"          \"type\": \"SimpleName\"\n" + 
				"        },\n" + 
				"        \"type\": \"MethodCallExpr\"\n" + 
				"      }\n" + 
				"    ],\n" + 
				"    \"type\": \"MethodCallExpr\"\n" + 
				"  },\n" + 
				"  \"type\": \"ExpressionStmt\"\n" + 
				"}";
		JSONObject j = new JSONObject(json);
		j = j.getJSONObject("expression");
		Scope o = new Scope(parseJsonScope(j), j.getJSONObject("name").getString("identifier"), parseJsonArgument(j, null));
		System.out.println(o.toString());
		// prints the resulting compilation unit to default system output
		// System.out.println(cu.toString());
	}

	public static Scope parseJsonScope(JSONObject j) {
		try {
			return new Scope(parseJsonScope(j = j.getJSONObject("scope")),
					j.getJSONObject("name").getString("identifier"), parseJsonArgument(j, null));
		} catch (JSONException e) {
			// TODO: handle exception
			return null;
		}
	}

	public static Argument parseJsonArgument(JSONObject j, JSONArray a) {
		try {
			if (a == null)
				return new Argument(parseJsonArgument(j, a = j.getJSONArray("arguments")),
						a.getJSONObject(0).getJSONObject("name").getString("identifier"), null);
			else
				return new Argument(parseJsonArgument(j, a = ((JSONObject) a.get(0)).getJSONArray("arguments")),
						a.getJSONObject(0).getJSONObject("name").getString("identifier"), null);
		} catch (JSONException e) {
			// TODO: handle exception
			try {
				String value = a.getJSONObject(0).getString("value");
				return new Argument(null, null, value);
			} catch(JSONException v) {
				return null;
			}
		}
	}

	static class Scope {
		protected Scope scope;
		protected String name;
		protected Argument argument;

		public Scope(Scope scope, String name, Argument argument) {
			this.scope = scope;
			this.name = name;
			this.argument = argument;
		}

		public Scope() {
			// TODO Auto-generated constructor stub
		}

		public Scope getScope() {
			return scope;
		}

		public void setScope(Scope scope) {
			this.scope = scope;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Argument getArgument() {
			return argument;
		}

		public void setArgument(Argument argument) {
			this.argument = argument;
		}

		@Override
		public String toString() {
			return "Scope [scope=" + scope + ", name=" + name + ", argument=" + argument + "]";
		}

	}

	static class Argument {
		private String name;
		private String value;
		private Argument argument;

		public Argument(Argument argument, String name, String value) {
			this.name = name;
			this.value = value;
			this.argument = argument;
		}

		public Argument() {
			// TODO Auto-generated constructor stub
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Argument getArgument() {
			return argument;
		}

		public void setArgument(Argument argument) {
			this.argument = argument;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Argument [name=" + name + ", value=" + value + ", argument=" + argument + "]";
		}

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

	public static LogCat parseStatement(WithIdModel m, Statement s) {
		LogCat logValues = null;

		if (s.toString().contains("onView")) {
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

			JSONObject jsonObject = new JSONObject(json);
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

		String searchType = ViewMatchers.getSearchType(matcher);
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
