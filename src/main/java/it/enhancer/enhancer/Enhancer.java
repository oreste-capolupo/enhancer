package it.enhancer.enhancer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.*;
import com.github.javaparser.printer.JsonPrinter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

public class Enhancer {

	private CompilationUnit cu;
	private List<Operation> operations;
	private boolean firstTest;
	private StringBuilder parameters;
	private StringBuilder field;

	private Map<String, Integer> statistic;
	private String packageName;

	public Enhancer(String packageName) {
		this.packageName = packageName;
		this.statistic = new HashMap<String, Integer>();
	}

	public void generateEnhancedClassFrom(String filePath) {
		try {
			populateEmptyStatistic();

			int slashIndex = filePath.lastIndexOf('/');
			int dotIndex = filePath.lastIndexOf('.');

			String folderPath = filePath.substring(0, slashIndex + 1);
			String filename = filePath.substring(slashIndex + 1, dotIndex);

			FileInputStream in = new FileInputStream(filePath);
			cu = JavaParser.parse(in);

			addImportsToCompilationUnit();

			addPrivateField();

			changeConstructorsName();

			addActivityInstanceMethod();

			// visit the body of all methods in the class
			cu.accept(new MethodVisitor(), null);
			// System.out.println(cu.toString());

			System.out.println("");

			String filenameEnhanced = folderPath + filename + "Enhanced.java";

			// generate enhanced java file
			PrintWriter w = new PrintWriter(filenameEnhanced, "UTF-8");
			w.print(cu.toString());
			w.close();

			// save statistic into file
			String statisticFilename = folderPath + filename + "_Statistic.txt";
			//Statistic.writeDataToFile(statistic, statisticFilename);
		} catch (FileNotFoundException f) {
			System.out.println("File: " + filePath + " not found!");
		} catch (UnsupportedEncodingException u) {
			System.out.println("Unsupported encoding on enhanced file");
		}
	}

	@SuppressWarnings("unused")
	private void populateStatisticFromFile(String statisticFilePath) {
		statistic = Statistic.readDataFromFile(statisticFilePath);
	}

	private void populateEmptyStatistic() {
		statistic = Statistic.populateInitialMap();
	}

	private void addPrivateField() {
		ClassOrInterfaceDeclaration ci = Navigator.findNodeOfGivenClass(cu, ClassOrInterfaceDeclaration.class);
		ci.setName(ci.getName() + "Enhanced");

		if (isNotInMembersList(ci, "currentActivity")) {
			BodyDeclaration<?> field = JavaParser.parseBodyDeclaration("private Activity currentActivity;");
			ci.getMembers().add(0, field);
		}
	}

	private void changeConstructorsName() {
		ClassOrInterfaceDeclaration ci = Navigator.findNodeOfGivenClass(cu, ClassOrInterfaceDeclaration.class);
		List<ConstructorDeclaration> cd = ci.getConstructors();
		for (ConstructorDeclaration c : cd) {
			c.setName(c.getName() + "Enhanced");
		}
	}

	private void addImportsToCompilationUnit() {
		// imports only if it does not exist
		cu.addImport(packageName + ".TOGGLETools", false, false);
		cu.addImport("java.util.Date", false, false);
		cu.addImport("android.app.Activity", false, false);
		cu.addImport("android.app.Instrumentation", false, false);
		cu.addImport("java.util.Collection", false, false);
		cu.addImport("android.support.test.InstrumentationRegistry", false, false);
		cu.addImport("android.widget.TextView", false, false);
		cu.addImport("android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry", false, false);
		cu.addImport("android.support.test.uiautomator.UiDevice", false, false);
		cu.addImport("android.graphics.Rect", false, false);
		cu.addImport("java.util.concurrent.FutureTask", false, false);
		cu.addImport("android.support.test.runner.lifecycle.Stage.RESUMED", false, false);
		cu.addImport("android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread", true,
				false);
	}

	private void addActivityInstanceMethod() {
		ClassOrInterfaceDeclaration ci = Navigator.findNodeOfGivenClass(cu, ClassOrInterfaceDeclaration.class);

		if (isNotInMembersList(ci, "getActivityInstance")) {
			MethodDeclaration md = new MethodDeclaration();

			String body = "{" + "getInstrumentation().runOnMainSync(new Runnable() {\n"
					+ "            public void run() {\n"
					+ "                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);\n"
					+ "                if (resumedActivities.iterator().hasNext()){\n"
					+ "                    currentActivity = (Activity) resumedActivities.iterator().next();\n"
					+ "                }\n" + "            }\n" + "        });\n" + "\n"
					+ "        return currentActivity;" + "}";

			md.setName("getActivityInstance");
			md.setPublic(true);
			md.setType("Activity");
			BlockStmt b = JavaParser.parseBlock(body);
			md.setBody(b);

			// adds the method at the bottom of the class. The private field
			// "currentActivity" is included in the members
			ci.getMembers().add(ci.getMembers().size(), md);
		}
	}

	private boolean isNotInMembersList(ClassOrInterfaceDeclaration ci, String member) {
		NodeList<BodyDeclaration<?>> members = ci.getMembers();

		for (BodyDeclaration<?> bd : members) {
			if ((bd.isFieldDeclaration() && bd.getChildNodes().get(0).toString().equals(member))
					|| (bd instanceof MethodDeclaration
							&& ((MethodDeclaration) bd).getName().getIdentifier().equals(member)))
				return false;
		}

		return true;
	}

	private class MethodVisitor extends ModifierVisitor<Void> {
		@Override
		public MethodDeclaration visit(MethodDeclaration m, Void arg) {
			/*
			 * here you can access the attributes of the method. this method will be called
			 * for all methods in this CompilationUnit, including inner class methods
			 */
			super.visit(m, arg);

			BlockStmt b = m.getBody().get();

			String body = b.toString();
			String methodName = m.getNameAsString();

			if (body.contains("onView") || body.contains("onData") || body.contains("intended")
					|| body.contains("intending")) {

				NodeList<Statement> nodes = b.getStatements();
				firstTest = true;
				parameters = new StringBuilder("");
				field = new StringBuilder("");

				// scan each statement
				int i = 0;
				while (i < nodes.size())
					// gets the new index because the method has been enhanced
					i = parseStatement(b, methodName, nodes.get(i), i);

				// add fullcheck at the bottom of the method
				if (!firstTest)
					addFullCheck(b, methodName, i);
			}

			return m;
		}

	}

	private void parseJsonScope(JSONObject j) {
		try {
			parseJsonScope(j = j.getJSONObject("scope"));

			// gets onView or onData and all nested performs and checks but the last one
			String name = j.getJSONObject("name").getString("identifier");

			if (!name.equals("onView") && !name.equals("onData") && !name.equals("intended")
					&& !name.equals("intending") && !name.equals("perform") && !name.equals("check"))
				operations.add(new Operation(name, ""));

			parseJsonArgument(j, null, 0);
		} catch (JSONException e) {
			// TODO: handle exception

		}
	}

	private void parseJsonArgument(JSONObject j, JSONArray a, int i) {
		try {
			if (a == null)
				parseJsonArgument(j, a = j.getJSONArray("arguments"), 0);
			else {
				parseJsonArgument(j, a = ((JSONObject) a.get(i)).getJSONArray("arguments"), 0);
				
				// check followed by perform or vice versa
				if (((JSONObject)a.get(0)).getString("type").equals("EnclosedExpr"))
					a = new JSONArray().put(((JSONObject)a.get(0)).getJSONObject("inner"));
				
				parseLeftInArgument((JSONObject) a.get(0));
				parseRightInArgument((JSONObject) a.get(0));
				parseScopeInArgument((JSONObject) a.get(0));
			}

			// field is empty if the parameter is not a FieldAccessExpr otherwise contains
			// only the first part ES: obj. or R.id.
			methodOverloading(a, i);
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}

	private void parseLeftInArgument(JSONObject j) {
		try {
			parseLeftInArgument(j = j.getJSONObject("left"));
			parseJsonArgument(j, null, 0);
			parseScopeInArgument(j);

			// call methodOverloading(...);
			methodOverloading(new JSONArray("[" + j + "]"), 0);

			parseRightInArgument(j);
		} catch (Exception e) {

		}
	}

	private void parseRightInArgument(JSONObject j) {
		try {
			parseRightInArgument(j = j.getJSONObject("right"));
			parseJsonArgument(j, null, 0);
			parseScopeInArgument(j);

			// call methodOverloading(...);
			// methodOverloading(new JSONArray("[" + j + "]"), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void parseScopeInArgument(JSONObject j) {
		try {
			if (field.toString().isEmpty()) {
				parseScopeInArgument(j = j.getJSONObject("scope"));
				String type = j.getString("type");
				String name = "";
				String index = "";

				if (type.equals("ArrayAccessExpr")) {
					name = j.getJSONObject("name").getJSONObject("name").getString("identifier");
					if (j.getJSONObject("index").getString("type").equals("NameExpr"))
						index = j.getJSONObject("index").getJSONObject("name").getString("identifier");
					else
						index = j.getJSONObject("index").getString("value");
				} else
					name = j.getJSONObject("name").getString("identifier");

				if (!type.equals("MethodCallExpr") && type.equals("ArrayAccessExpr"))
					field.append(name + "[" + index + "].");
				else if (!type.equals("MethodCallExpr"))
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
				// caught exception
			}
		}
	}

	private void methodOverloading(JSONArray a, int i) {
		try {
			String type = a.getJSONObject(i).getString("type");
			String name = "";

			name = a.getJSONObject(i).getJSONObject("name").getString("identifier");

			if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
			// && !field.toString().startsWith("ViewMatchers.") &&
			// !field.toString().startsWith("ViewActions.")
			/* && !field.toString().startsWith("Matchers.") */ && isNotAnEspressoCommand(name)) {
				String fd = field.toString();
				name = fd.concat(name);
			}

			String parametersValue = parameters.toString();

			if ((type.equals("MethodCallExpr")) && isNotAnEspressoCommand(name)) {

				if (parametersValue.isEmpty())
					parameters.append(name + "()");
				else
					parameters = new StringBuilder(name + "(" + parametersValue + ")");
				field = new StringBuilder("");

			} else if (type.equals("MethodCallExpr")) {
				// if the command is an assertion then "order" the list
				if (!ViewAssertions.getSearchType(name).equals("") || name.equals("allOf") || name.equals("anyOf")) {
					int numberOfArguments = a.getJSONObject(i).getJSONArray("arguments").length();
					if (operations.size() >= numberOfArguments)
						operations.add(operations.size() - numberOfArguments, new Operation(name, parametersValue));
					else
						operations.add(new Operation(name, parametersValue));
				} else
					operations.add(new Operation(name, parametersValue));

				// save occurrences
				Integer oldStatistic = statistic.get(name);
				statistic.put(name, oldStatistic.intValue() + 1);

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

	private boolean isNotAnEspressoCommand(String name) {
		String[] espressoCommands = EspressoCommands.getCommands();

		int low = 0;
		int high = espressoCommands.length - 1;
		int mid;

		while (low <= high) {
			mid = (low + high) / 2;

			if (espressoCommands[mid].compareTo(name) < 0) {
				low = mid + 1;
			} else if (espressoCommands[mid].compareTo(name) > 0) {
				high = mid - 1;
			} else {
				return false;
			}
		}

		return true;
	}

	private void methodParameters(JSONArray a, int j) {
		try {
			String type = a.getJSONObject(j).getString("type");
			String value = "";

			if (type.equals("BinaryExpr"))
				value = a.getJSONObject(j).getJSONObject("right").getString("value");
			else
				value = a.getJSONObject(j).getString("value");

			String parametersValue = parameters.toString();

			if (value.contains("\n")) {
				StringBuffer v = new StringBuffer(value);
				int index = v.indexOf("\n");
				while (index >= 0) {
					v.replace(index, index + 1, "\\n");
					index = v.indexOf("\n", index + 2);
				}
				value = v.toString();
			}

			// if (!parameters.toString().contains(value) || type.equals("BinaryExpr")) {
			if (field.toString().isEmpty() || type.equals("BinaryExpr")) {
				if (type.equals("BinaryExpr")) {
					if (parametersValue.isEmpty())
						parameters.append("\"" + value + "\"");
					else
						parameters.append("+" + "\"" + value + "\"");
				} else if (type.equals("StringLiteralExpr")) {
					if (parametersValue.isEmpty())
						parameters.append("\"" + value + "\"");
					else
						parameters.append("," + "\"" + value + "\"");
				} else {
					if (parametersValue.isEmpty())
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
			// }

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
				} else if (type.equals("BinaryExpr")) {
					name = a.getJSONObject(j).getJSONObject("right").getJSONObject("name").getString("identifier");
				} else
					name = a.getJSONObject(j).getJSONObject("name").getString("identifier");

				if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
				// && !field.toString().startsWith("ViewMatchers.") &&
				// !field.toString().startsWith("ViewActions.")
				/* && !field.toString().startsWith("Matchers.") */ && isNotAnEspressoCommand(name)) {
					field.append(name + ",");
					name = field.toString();
				}

				String parametersValue = parameters.toString();

				if (type.equals("BinaryExpr") || !type.equals("MethodCallExpr") && !parameters.toString().contains(name)
						|| (type.equals("MethodCallExpr") && isNotAnEspressoCommand(name)
								&& !parameters.toString().contains(name))) {

					if (type.equals("BinaryExpr")) {
						type = a.getJSONObject(j).getString("type");

						if (name.endsWith(","))
							name = name.substring(0, name.length() - 1);
						if (type.equals("StringLiteralExpr"))
							name = "\"" + name + "\"";
						if (parametersValue.isEmpty())
							parameters.append(name);
						else {
							type = a.getJSONObject(j).getJSONObject("right").getString("type");
							/*
							 * try { type =
							 * a.getJSONObject(j).getJSONObject("left").getJSONObject("right").getString(
							 * "type"); if (!field.toString().isEmpty()) { int dotIndex =
							 * field.lastIndexOf("."); field = new StringBuilder(field.subSequence(0,
							 * dotIndex + 1));
							 * 
							 * int plusIndex = parameters.lastIndexOf("+"); parameters.replace(plusIndex +
							 * 1, plusIndex + 1, field.toString());
							 * 
							 * int commaIndex = -1; if (type.equals("MethodCallExpr") && (commaIndex =
							 * parameters.lastIndexOf(",")) != -1) { int numberOfArguments =
							 * a.getJSONObject(j).getJSONObject("left").getJSONObject("right")
							 * .getJSONArray("arguments").length(); for (int w = 0; w < numberOfArguments -
							 * 1; w++) commaIndex = parameters.substring(0, commaIndex).lastIndexOf(",");
							 * parameters.replace(commaIndex, commaIndex + 1, "("); parameters.append(")");
							 * } }
							 * 
							 * } catch (Exception e2) {
							 */
							int commaIndex = -1;
							if (type.equals("MethodCallExpr") && (commaIndex = parameters.lastIndexOf(",")) != -1) {
								int numberOfArguments = a.getJSONObject(j).getJSONObject("right")
										.getJSONArray("arguments").length();
								for (int w = 0; w < numberOfArguments - 1; w++)
									commaIndex = parametersValue.substring(0, commaIndex).lastIndexOf(",");
								parameters.replace(commaIndex, commaIndex + 1, "+" + name + "(");
								parameters.append(")");
							} else
								parameters.append("+" + name);
							// }

						}
						// field access
					} else if (type.equals("FieldAccessExpr") && field.toString().startsWith("R.id.")) {
						if (parametersValue.isEmpty())
							parameters.append("\"" + name + "\"");
						else
							parameters.append("," + "\"" + name + "\"");

						field = new StringBuilder("");

						// field access
					} else if (type.equals("FieldAccessExpr")) {
						if (parametersValue.isEmpty())
							parameters.append(name.substring(0, name.length() - 1));
						else
							parameters.append("," + name.substring(0, name.length() - 1));

						// array access
					} else if (type.equals("ArrayAccessExpr")) {
						if (name.charAt(name.length() - 1) != ',') {

							if (parametersValue.isEmpty())
								parameters.append(name + "[" + index + "]");
							else
								parameters.append("," + name + "[" + index + "]");
						} else {
							if (parametersValue.isEmpty())
								parameters.append(name.substring(0, name.length() - 1) + "[" + index + "]");
							else
								parameters.append("," + name.substring(0, name.length() - 1) + "[" + index + "]");
						}

						// name expr
					} else {
						if (field.toString().isEmpty()) {
							if (parametersValue.isEmpty())
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

	private int parseStatement(BlockStmt b, String methodName, Statement s, int i) {
		int index = i;
		String stmtString = s.toString();

		operations = new ArrayList<Operation>();

		if (stmtString.contains("onView") || stmtString.contains("onData") || stmtString.contains("intended")
				|| stmtString.contains("intending")) {
			JsonPrinter printer = new JsonPrinter(true);
			String json = printer.output(s);

			// TEST CASES like : ViewInteraction vi = onView(withId(...)).perform(...);
			if (json.contains("VariableDeclarator")) {
				String type = "type";

				// Substitute type with typeV to avoid key duplicate conflict
				for (int j = -1; (j = json.indexOf(type, j + 1)) != -1; j++) {
					String old = json.substring(j, j + 26);
					if (old.equals("type\":\"VariableDeclarator\"")) {
						json = json.substring(0, j) + "typeV\": \"VariableDeclarator\"" + json.substring(j + 26);
						break;
					}
				}

			}

			try {
				JSONObject j = new JSONObject(json);
				// System.out.println(j.toString());
				j = j.getJSONObject("expression");

				String type = j.getString("type");

				// vi = onView(withId(...)).perform(...);
				if (type.equals("AssignExpr")) {
					j = j.getJSONObject("value");

					// ViewInteraction vi = onView(withId(...)).perform(...);
				} else if (type.equals("VariableDeclarationExpr")) {
					j = j.getJSONArray("variables").getJSONObject(0).getJSONObject("initializer");
				}

				parseJsonScope(j);

				// gets the last check or perform
				// operations.add(new Operation(j.getJSONObject("name").getString("identifier"),
				// ""));

				parseJsonArgument(j, null, 0);

				System.out.println(operations.toString());

				// returns the next index after enhancing the method
				return enhanceMethod(b, methodName, s, i);
			} catch (JSONException e) {
				// CAN'T PARSE STATEMENT
			}
		} else {
			String op = "";
			if (stmtString.contains("closeSoftKeyboard();")) {
				op = "closeSoftKeyboard";
			} else if (stmtString.contains("pressBack();")) {
				op = "pressBack";
			} else if (stmtString.contains("pressBackUnconditionally();")) {
				op = "pressBackUnconditionally";
			} else if (stmtString.contains("openActionBarOverflowOrOptionsMenu(")) {
				op = "openActionBarOverflowOrOptionsMenu";
			} else if (stmtString.contains("openContextualActionModeOverflowMenu();")) {
				op = "openContextualActionModeOverflowMenu";
			} else if (stmtString.contains("typeTextIntoFocusedView(")) {
				op = "typeTextIntoFocusedView";
			}

			if (!op.isEmpty()) {
				Integer oldStatistic = statistic.get(op);
				statistic.put(op, oldStatistic.intValue() + 1);

				// enhance interaction
				operations.add(new Operation("blank", "-"));
				operations.add(new Operation(op, ""));
				return enhanceMethod(b, methodName, s, i);
			}

		}
		// return the next index if the statement is not a test
		return ++index;
	}

	private int enhanceMethod(BlockStmt b, String methodName, Statement s, int i) {
		Statement captureTask = JavaParser.parseStatement("FutureTask<Boolean> capture_task = null;");
		Statement resTask = JavaParser.parseStatement("boolean res_task = false;");
		Statement instrumentation = JavaParser
				.parseStatement("Instrumentation instr = InstrumentationRegistry.getInstrumentation();");
		Statement device = JavaParser.parseStatement("UiDevice device = UiDevice.getInstance(instr);");
		Statement firstTestDate = JavaParser.parseStatement("Date now = new Date();");
		Statement date = JavaParser.parseStatement("now = new Date();");
		Statement firstTestActivity = JavaParser
				.parseStatement("Activity activityTOGGLETools = getActivityInstance();");
		Statement activity = JavaParser.parseStatement("activityTOGGLETools = getActivityInstance();");
		Statement captureTaskValue = JavaParser.parseStatement(
				"capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));");
		TryStmt screenCapture = (TryStmt) JavaParser.parseStatement(
				"try { runOnUiThread(capture_task); res_task = capture_task.get();} catch (Throwable t) { t.printStackTrace(); }");
		Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");
		TryStmt tryStmt = (TryStmt) JavaParser.parseStatement("try {\n" + "            Thread.sleep(1000);\n"
				+ "        } catch (Exception e) {\n" + "\n" + "        }");

		// this works on test cases with one matcher
		String searchType = "";
		String searchKw = "";
		if (operations.size() > 0) {
			searchType = ViewMatchers.getSearchType(operations.get(0).getName());
			searchKw = operations.get(0).getParameter();
		}

		if (!searchType.isEmpty() || searchType.equals("-")) {
			String stmtString = s.toString();
			Statement st = JavaParser.parseStatement(stmtString);

			if (operations.size() > 1)
				b.remove(s);

			for (int j = 1; j < operations.size(); j++) {
				String interactionType = ViewActions.getSearchType(operations.get(j).getName());
				String interactionParams = operations.get(j).getParameter();
				/*
				 * if (interactionType.isEmpty()) { new Exception(operations.get(j).getName() +
				 * " is not supported or is not an Espresso command").printStackTrace(); }
				 */

				// if (!interactionType.equals("perform") && !interactionType.equals("check")) {

				if (interactionType.isEmpty()) {
					interactionType = ViewAssertions.getSearchType(operations.get(j).getName());

					if (searchType.isEmpty() || interactionType.isEmpty()) {
						b.addStatement(i, st);
						break;
					}

					// log only if the assertion is 'matches'. Leave out isLeft, isRight ecc... for
					// now.
					if (interactionType.equals("matches") && canItBeAnAssertionParameter(operations.get(++j)))
						interactionType = "check";
					else {
						b.addStatement(i, st);
						break;
					}

				}

				LogCat log = new LogCat(methodName, searchType, searchKw, interactionType, interactionParams);

				if (firstTest) {
					firstTest = false;
					b.addStatement(i, captureTask);
					b.addStatement(++i, resTask);
					b.addStatement(++i, instrumentation);
					b.addStatement(++i, device);
					b.addStatement(++i, firstTestDate);
					b.addStatement(++i, firstTestActivity);
				} else if (j == 2 && interactionType.equals("check") || j == 1) {
					b.addStatement(i, date);
					b.addStatement(++i, activity);

					// this makes it work on test cases with multiple interactions avoiding the try
					// statements to stay to the bottom
				} else {
					b.addStatement(++i, date);
					b.addStatement(++i, activity);
				}

				b.addStatement(++i, captureTaskValue);
				b.addStatement(++i, screenCapture);

				i = addInteractionToCu(interactionType, log, i, b);

				b.addStatement(++i, dumpScreen);
				b.addStatement(++i, st);
				b.addStatement(++i, tryStmt);
			}

		}
		// }

		return ++i;
	}

	private boolean canItBeAnAssertionParameter(Operation operation) {
		String[] assertionParameters = { "hasEllipsizedText", "hasFocus", "isChecked", "isClickable",
				"isCompletelyDisplayed", "isDisplayed", "isEnabled", "isNotChecked", "isSelected",
				"withEffectiveVisibility", "withSpinnerText", "withText" };
		String name = operation.getName();

		int low = 0;
		int high = assertionParameters.length - 1;
		int mid;

		while (low <= high) {
			mid = (low + high) / 2;

			if (assertionParameters[mid].compareTo(name) < 0) {
				low = mid + 1;
			} else if (assertionParameters[mid].compareTo(name) > 0) {
				high = mid - 1;
			} else {
				return true;
			}
		}

		/*
		 * for (String par : assertionParameters) { if (par.equals(operation.getName()))
		 * return true; }
		 */

		return false;
	}

	private int addInteractionToCu(String interactionType, LogCat log, int i, BlockStmt b) {
		Statement l = null;
		String stmt = "";

		if (log.getInteractionType().isEmpty())
			log.setInteractionType("");

		// default handles the normal behavior of the parameters. ES: click(),
		// typeText("TextToBeReplaced")
		switch (interactionType) {
		case "replacetext":
			// the 'i' in the variable name is used to make it unique in case we have
			// multiple interactions of the same type
			// substring removes the " from the string

			// TODO: handle different behaviors based on the type of parameter
			if (log.getSearchKw().charAt(0) == '"') {
				log.setSearchKw(log.getSearchKw().substring(1, log.getSearchKw().length() - 1));
				if (log.getSearchType().equals("id")) {
					stmt = "int textToBeReplacedLength" + i + " = ((TextView) activityTOGGLETools.findViewById(R.id."
							+ log.getSearchKw() + ")).getText().length();";

					b.addStatement(++i, JavaParser.parseStatement(stmt));

					l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName()
							+ "\", " + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + ","
							+ "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 1)
							+ ")+\";\"+" + log.getInteractionParams() + ");");
				} else {
					stmt = "String searchKw = \"" + log.getSearchKw() + "\";";
					String stmt2 = "int textToBeReplacedLength" + i + " = searchKw.length();";

					b.addStatement(++i, JavaParser.parseStatement(stmt));
					b.addStatement(++i, JavaParser.parseStatement(stmt2));

					l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName()
							+ "\", " + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + ","
							+ "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 2)
							+ ")+\";\"+" + log.getInteractionParams() + ");");
				}
			} else {
				if (log.getSearchType().equals("id"))
					stmt = "int textToBeReplacedLength" + i + " = ((TextView) activityTOGGLETools.findViewById("
							+ log.getSearchKw() + ")).getText().length();";
				else
					stmt = "int textToBeReplacedLength" + i + " = " + log.getSearchKw() + ".length();";

				b.addStatement(++i, JavaParser.parseStatement(stmt));

				l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
						+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
						+ log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 1) + ")+\";\"+"
						+ log.getInteractionParams() + ");");

			}
			break;
		case "cleartext":

			// TODO: handle different behaviors based on the type of parameter
			if (log.getSearchKw().charAt(0) == '"') {
				log.setSearchKw(log.getSearchKw().substring(1, log.getSearchKw().length() - 1));
				if (log.getSearchType().equals("id")) {
					stmt = "int textToBeClearedLength" + i + " = ((TextView) activityTOGGLETools.findViewById(R.id."
							+ log.getSearchKw() + ")).getText().length();";
					b.addStatement(++i, JavaParser.parseStatement(stmt));

					l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
							+ "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + "," + "\""
							+ log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");
				} else {
					stmt = "String searchKw = \"" + log.getSearchKw() + "\";";
					String stmt2 = "int textToBeReplacedLength" + i + " = searchKw.length();";

					b.addStatement(++i, JavaParser.parseStatement(stmt));
					b.addStatement(++i, JavaParser.parseStatement(stmt2));
					
					l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
							+ "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + "," + "\""
							+ log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 2) + "));");
				}
			} else {
				if (log.getSearchType().equals("id"))
					stmt = "int textToBeClearedLength" + i + " = ((TextView) activityTOGGLETools.findViewById("
							+ log.getSearchKw() + ")).getText().length();";
				else
					stmt = "int textToBeReplacedLength" + i + " = " + log.getSearchKw() + ".length();";
				
				b.addStatement(++i, JavaParser.parseStatement(stmt));

				l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
						+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
						+ log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");
			}
			break;
		case "presskey":
			Statement val = JavaParser.parseStatement(
					"String espressoKeyVal" + i + " = String.valueOf(" + log.getInteractionParams() + ");");
			Statement keyArray = JavaParser
					.parseStatement("String[] espressoKeyArray" + i + " = espressoKeyVal" + i + ".split(\",\");");
			IfStmt ifStmt = (IfStmt) JavaParser.parseStatement("if(espressoKeyArray" + i + ".length > 1) {\n"
					+ "            int espressoKeyArrayIndex" + i + " = espressoKeyArray" + i + "[0].indexOf(\":\");\n"
					+ "            espressoKeyVal" + i + " = espressoKeyArray" + i
					+ "[0].substring(espressoKeyArrayIndex" + i + "+1).trim();\n" + "        }");

			b.addStatement(++i, val);
			b.addStatement(++i, keyArray);
			b.addStatement(++i, ifStmt);

			stmt = "TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\"," + "\"" + log.getSearchType()
					+ "\"" + "," + log.getSearchKw() + "," + "\"" + log.getInteractionType() + "\"" + ", espressoKeyVal"
					+ (i - 3) + ");";

			l = JavaParser.parseStatement(stmt);

			break;
		case "pressback":
		case "pressbackunconditionally":
		case "closekeyboard":
		case "openactionbaroverfloworoptionsmenu":
		case "opencontextualactionmodeoverflowmenu":
		case "typeintofocused":
			l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
					+ "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\"" + ");");
			break;
		default:
			if (log.getInteractionParams().isEmpty())
				l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
						+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
						+ log.getInteractionType() + "\"" + ");");
			else
				l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
						+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
						+ log.getInteractionType() + "\"" + "," + log.getInteractionParams() + ");");
			break;
		}

		if (l != null)
			b.addStatement(++i, l);

		return i;
	}

	private void addFullCheck(BlockStmt b, String methodName, int i) {
		Statement date = JavaParser.parseStatement("now = new Date();");
		Statement activity = JavaParser.parseStatement("activityTOGGLETools = getActivityInstance();");
		Statement captureTaskValue = JavaParser.parseStatement(
				"capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));");
		TryStmt screenCapture = (TryStmt) JavaParser.parseStatement(
				"try { runOnUiThread(capture_task); res_task = capture_task.get();} catch (Throwable t) { t.printStackTrace(); }");
		Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");

		Statement currDisp = JavaParser
				.parseStatement("Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activityTOGGLETools);");

		String stmt = "TOGGLETools.LogInteraction(now," + "\"" + methodName
				+ "\",\"-\", \"-\", \"fullcheck\", currdisp.bottom+\";\"+currdisp.top+\";\"+currdisp.right+\";\"+currdisp.left);";
		Statement log = JavaParser.parseStatement(stmt);

		// if (i > b.getStatements().size())
		// --i;

		b.addStatement(i, date);
		b.addStatement(++i, activity);
		b.addStatement(++i, captureTaskValue);
		b.addStatement(++i, currDisp);
		b.addStatement(++i, screenCapture);
		b.addStatement(++i, log);
		b.addStatement(++i, dumpScreen);
	}

}