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
			Statistic.writeDataToFile(statistic, statisticFilename);
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
		public BlockStmt visit(BlockStmt b, Void arg) {
			/*
			 * here you can access the attributes of the method. this method will be called
			 * for all methods in this CompilationUnit, including inner class methods
			 */
			super.visit(b, arg);
			String body = b.toString();

			if (body.contains("onView") || body.contains("onData")) {

				NodeList<Statement> nodes = b.getStatements();
				firstTest = true;
				parameters = new StringBuilder("");
				field = new StringBuilder("");

				// scan each statement
				int i = 0;
				while (i < nodes.size())
					// gets the new index because the method has been enhanced
					i = parseStatement(b, nodes.get(i), i);

				// add fullcheck at the bottom of the method
				if (!firstTest)
					addFullCheck(b, i);
			}

			return b;
		}

	}

	private void parseJsonScope(JSONObject j) {
		try {
			parseJsonScope(j = j.getJSONObject("scope"));

			// gets onView or onData and all nested performs and checks but the last one
			String name = j.getJSONObject("name").getString("identifier");

			if (!name.equals("onView") && !name.equals("onData"))
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
				parseScopeInArgument((JSONObject) a.get(0));
			}

			// field is empty if the parameter is not a FieldAccessExpr otherwise contains
			// only the first part ES: obj. or R.id.
			methodOverloading(a, i);
		} catch (JSONException e) {
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
				// TODO: handle exception
			}
		}
	}

	private void methodOverloading(JSONArray a, int i) {
		try {
			String type = a.getJSONObject(i).getString("type");
			String name = a.getJSONObject(i).getJSONObject("name").getString("identifier");

			if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
			// && !field.toString().startsWith("ViewMatchers.") &&
			// !field.toString().startsWith("ViewActions.")
			/* && !field.toString().startsWith("Matchers.") */ && isNotAnEspressoCommand(name)) {
				String fd = field.toString();
				name = fd.concat(name);
			}

			String parametersValue = parameters.toString();

			if (type.equals("MethodCallExpr") && isNotAnEspressoCommand(name)) {

				if (parametersValue.isEmpty())
					parameters.append(name + "()");
				else
					parameters = new StringBuilder(name + "(" + parametersValue + ")");
				field = new StringBuilder("");

			} else if (type.equals("MethodCallExpr")) {
				// if the command is an assertion then "order" the list
				if (!ViewAssertions.getSearchType(name).equals("") || name.equals("allOf") || name.equals("anyOf")) {
					int numberOfArguments = a.getJSONObject(i).getJSONArray("arguments").length();
					operations.add(operations.size() - numberOfArguments, new Operation(name, parametersValue));
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
			String value = a.getJSONObject(j).getString("value");
			String parametersValue = parameters.toString();

			if (field.toString().isEmpty()) {
				if (type.equals("StringLiteralExpr")) {
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
				// && !field.toString().startsWith("ViewMatchers.") &&
				// !field.toString().startsWith("ViewActions.")
				/* && !field.toString().startsWith("Matchers.") */ && isNotAnEspressoCommand(name)) {
					field.append(name + ",");
					name = field.toString();
				}

				String parametersValue = parameters.toString();

				if (!type.equals("MethodCallExpr") || (type.equals("MethodCallExpr") && isNotAnEspressoCommand(name)
						&& !parameters.toString().contains(name + "("))) {

					// field access
					if (type.equals("FieldAccessExpr") && field.toString().startsWith("R.id.")) {
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

	private int parseStatement(BlockStmt b, Statement s, int i) {
		int index = i;
		String stmtString = s.toString();

		if (stmtString.contains("onView") || stmtString.contains("onData")) {
			JsonPrinter printer = new JsonPrinter(true);
			String json = printer.output(s);

			operations = new ArrayList<Operation>();

			try {
				JSONObject j = new JSONObject(json);
				// System.out.println(j.toString());
				j = j.getJSONObject("expression");

				parseJsonScope(j);

				// gets the last check or perform
				operations.add(new Operation(j.getJSONObject("name").getString("identifier"), ""));

				parseJsonArgument(j, null, 0);

				System.out.println(operations.toString());

				// returns the next index after enhancing the method
				return index = enhanceMethod(b, s, i);
			} catch (JSONException e) {
				// CAN'T PARSE STATEMENT
			}
		} else if (stmtString.contains("closeSoftKeyboard();")) {
			// TODO: enhance interaction

			Integer oldStatistic = statistic.get("closeSoftKeyboard");
			statistic.put("closeSoftKeyboard", oldStatistic.intValue() + 1);
		}
		// return the next index if the statement is not a test
		return ++index;
	}

	private int enhanceMethod(BlockStmt b, Statement s, int i) {
		Statement instrumentation = JavaParser
				.parseStatement("Instrumentation instr = InstrumentationRegistry.getInstrumentation();");
		Statement device = JavaParser.parseStatement("UiDevice device = UiDevice.getInstance(instr);");
		Statement firstTestDate = JavaParser.parseStatement("Date now = new Date();");
		Statement date = JavaParser.parseStatement("now = new Date();");
		Statement firstTestActivity = JavaParser.parseStatement("Activity activityTOGGLETools = getActivityInstance();");
		Statement activity = JavaParser.parseStatement("activityTOGGLETools = getActivityInstance();");
		Statement screenCapture = JavaParser.parseStatement("TOGGLETools.TakeScreenCapture(now, activityTOGGLETools"
				+ ");");
		Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");
		TryStmt tryStmt = (TryStmt) JavaParser.parseStatement("try {\n" + "            Thread.sleep(1000);\n"
				+ "        } catch (Exception e) {\n" + "\n" + "        }");

		// this works on test cases with one matcher
		String searchType = ViewMatchers.getSearchType(operations.get(0).getName());
		String searchKw = operations.get(0).getParameter();

		if (!searchType.isEmpty()) {
			String stmtString = s.toString();
			Statement st = JavaParser.parseStatement(stmtString);

			b.remove(s);

			for (int j = 1; j < operations.size(); j++) {
				String interactionType = ViewActions.getSearchType(operations.get(j).getName());
				String interactionParams = operations.get(j).getParameter();
				/*
				 * if (interactionType.isEmpty()) { new Exception(operations.get(j).getName() +
				 * " is not supported or is not an Espresso command").printStackTrace(); }
				 */

				if (!interactionType.equals("perform") && !interactionType.equals("check")) {

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

					LogCat log = new LogCat(searchType, searchKw, interactionType, interactionParams);

					if (firstTest) {
						firstTest = false;
						b.addStatement(i, instrumentation);
						b.addStatement(++i, device);
						b.addStatement(++i, firstTestDate);
						b.addStatement(++i, firstTestActivity);
					} else if (j == 3 && interactionType.equals("check") || j == 2) {
						b.addStatement(i, date);
						b.addStatement(++i, activity);

						// this makes it work on test cases with multiple interactions avoiding the try
						// statements to stay to the bottom
					} else {
						b.addStatement(++i, date);
						b.addStatement(++i, activity);
					}

					i = addInteractionToCu(interactionType, log, i, b);

					b.addStatement(++i, screenCapture);
					b.addStatement(++i, dumpScreen);
					b.addStatement(++i, st);
					b.addStatement(++i, tryStmt);
				}

			}
		}

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

		// default handles the normal behavior of the parameters. ES: click(),
		// typeText("TextToBeReplaced")
		switch (interactionType) {
		case "replacetext":
			// the 'i' in the variable name is used to make it unique in case we have
			// multiple interactions of the same type
			// substring removes the " from the string
			stmt = "int textToBeReplacedLength" + i + " = ((TextView) activityTOGGLETools.findViewById(R.id."
					+ log.getSearchKw().substring(1, log.getSearchKw().length() - 1) + ")).getText().length();";
			b.addStatement(++i, JavaParser.parseStatement(stmt));

			l = JavaParser.parseStatement(
					"TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw()
							+ "," + "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength"
							+ (i - 1) + ")+\";\"+" + log.getInteractionParams() + ");");
			break;
		case "cleartext":
			stmt = "int textToBeClearedLength" + i + " = ((TextView) activityTOGGLETools.findViewById(R.id."
					+ log.getSearchKw().substring(1, log.getSearchKw().length() - 1) + ")).getText().length();";
			b.addStatement(++i, JavaParser.parseStatement(stmt));

			l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\"" + ","
					+ log.getSearchKw() + "," + "\"" + log.getInteractionType()
					+ "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");
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

			stmt = "TOGGLETools.LogInteraction(now, " + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw()
					+ "," + "\"" + log.getInteractionType() + "\"" + ", espressoKeyVal" + (i - 3) + ");";

			l = JavaParser.parseStatement(stmt);

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

		if (l != null)
			b.addStatement(++i, l);

		return i;
	}

	private void addFullCheck(BlockStmt b, int i) {
		Statement date = JavaParser.parseStatement("now = new Date();");
		Statement activity = JavaParser.parseStatement("activityTOGGLETools = getActivityInstance();");
		Statement screenCapture = JavaParser.parseStatement("TOGGLETools.TakeScreenCapture(now, activityTOGGLETools);");
		Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");

		Statement currDisp = JavaParser.parseStatement("Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activityTOGGLETools);");

		String stmt = "TOGGLETools.LogInteraction(now, \"-\", \"-\", \"fullcheck\", currdisp.bottom+\";\"+currdisp.top+\";\"+currdisp.right+\";\"+currdisp.left);";
		Statement log = JavaParser.parseStatement(stmt);

		b.addStatement(i, date);
		b.addStatement(++i, activity);
		b.addStatement(++i, currDisp);
		b.addStatement(++i, log);
		b.addStatement(++i, screenCapture);
		b.addStatement(++i, dumpScreen);
	}

}