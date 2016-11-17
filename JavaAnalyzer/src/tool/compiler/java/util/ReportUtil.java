package tool.compiler.java.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.main.Report;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.EquGenExt;
import tool.compiler.java.constraint.Constraint;
import tool.compiler.java.effect.EffectSetVariable;
import tool.compiler.java.env.ConstraintFunction;
import tool.compiler.java.info.Info;

public final class ReportUtil {
	/**
	 * Report Node when node enter. If the node is expression, it also reports node's type.
	 * @param nodeExtention
	 */
	public static final void enterReport(EquGenExt nodeExtention) {
		report(nodeExtention, "Enter");
	}
	
	/**
	 * Report Node when node leave. If the node is expression, it also reports node's type.
	 * @param nodeExtention
	 */
	public static final void leaveReport(EquGenExt nodeExtention) {
		report(nodeExtention, "Leave");
	}
	
	/**
	 * Report Node. If the node is expression, it also reports node's type.
	 * @param nodeExtention
	 * @param header
	 */
	private static final void report(EquGenExt nodeExtention, String header) {
		Node node = nodeExtention.node();
		StringBuilder sb = new StringBuilder();
		sb.append('[')
			.append(header)
			.append(']').append(' ')
			.append(nodeExtention.getKind())
			.append(':').append(' ')
			.append(node);
		if(node instanceof Expr) {
			sb.append(" ( Type: ")
				.append(((Expr)node).type())
				.append(' ').append(')');
		}
		Report.report(2, sb.toString());
	}
	
	/**
	 * Report AbstractObject.
	 * @param absObj
	 */
	public static final void report(AbstractObject absObj) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t[AbstractObject] ")
			.append(absObj)
			.append(' ').append('(').append(' ')
			.append(absObj.getType())
			.append(' ').append(')');
		AbstractObject.Info info = absObj.getInfo();
		if(info != null) {
			sb.append(':').append(' ')
				.append(info);
		}
		Report.report(3, sb.toString());
	}
	
	/**
	 * Report MetaSetVariable.
	 * @param msv
	 * @param src
	 * @param goal
	 * @see ReportUtil.MetaSetVarSource
	 * @see ReportUtil.MetaSetVarGoal
	 */
	public static final void report(MetaSetVariable msv, 
			MetaSetVarSource src, MetaSetVarGoal goal) {
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append('[')
			.append(msv.getClass().getSimpleName())
			.append(']').append(' ')
			.append(msv)
			.append(' ').append('(').append(' ')
			.append(src.translate())
			.append(": To ")
			.append(goal)
			.append(' ').append(')');
		Report.report(3, sb.toString());
	}
	
	/**
	 * Report EffectSetVariable.
	 * @param esv
	 * @param src
	 * @param goal
	 * @see ReportUtil.EffectSetVarSource
	 */
	public static final void report(EffectSetVariable esv, 
			EffectSetVarSource src, EffectSetVarGoal goal) {
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append('[')
		.append(esv.getClass().getSimpleName())
		.append(']').append(' ')
		.append(esv)
		.append(' ').append('(').append(' ')
		.append(src)
		.append(": To ")
		.append(goal)
		.append(' ').append(')');
		Report.report(3, sb.toString());
	}
	
	/**
	 * Report EffectSetVariables.
	 * @param esvs
	 * @param src
	 * @param goal
	 * @see ReportUtil.EffectSetVarSource
	 */
	public static final void report(Collection<? extends EffectSetVariable> esvs, 
			EffectSetVarSource src, EffectSetVarGoal goal) {
		for (EffectSetVariable esv : esvs) {
			report(esv, src, goal);
		}
	}
	
	/**
	 * Report EffectSetVariables.
	 * @param esvs
	 * @param goal
	 * @see ReportUtil.EffectSetVarSource
	 */
	public static final void report(Map<? extends EffectSetVariable, EffectSetVarSource> esvs, 
			EffectSetVarGoal goal) {
		for (Entry<? extends EffectSetVariable, EffectSetVarSource> esv : esvs.entrySet()) {
			report(esv.getKey(), esv.getValue(), goal);
		}
	}
	
	/**
	 * Report Method/Field Info.
	 * @param info
	 */
	public static final void report(Info info) {
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append('[')
			.append(info.getClass().getSimpleName())
			.append(']').append(' ')
			.append(info);
		Report.report(3, sb.toString());
	}
	
	/**
	 * Report Constraint.
	 * @param constraint
	 */
	public static final void report(Constraint constraint) {
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append('[')
			.append(constraint.getKind())
			.append(']').append(' ')
			.append(constraint);
		Report.report(3, sb.toString());
	}
	
	/**
	 * Report Constraints.
	 * @param constraints
	 */
	public static final void report(Collection<? extends Constraint> constraints) {
		for(Constraint constraint : constraints) {
			report(constraint);
		}
	}
	
	/**
	 * Report Constraint Function.
	 * @param constraint
	 */
	public static final void report(ConstraintFunction constraintFunction) {
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append('[')
			.append(constraintFunction.getKind())
			.append(']').append(' ')
			.append(constraintFunction);
		Report.report(3, sb.toString());
	}
	
	public static enum MetaSetVarSource {
		New,
		Receiver,
		Lvalue,
		Rvalue,
		SubExpression,
		Argument,
		ClassEnvironment,
		MethodEnvironment,
		LocalEnvironment,
		ArrayInit,
		ArrayLength,
		ArrayDimension,
		ArrayBase,
		ArrayElement;
		
		@Override
		public String toString() {
			switch (this) {
			case New:
				return "New";
			case Receiver:
				return "Receiver";
			case Lvalue:
				return "L-value";
			case Rvalue:
				return "R-value";
			case SubExpression:
				return "Sub-Expression";
			case Argument:
				return "Argument";
			case ClassEnvironment:
				return "Class Environment";
			case MethodEnvironment:
				return "Method Environment";
			case LocalEnvironment:
				return "Local Environment";
			case ArrayInit:
				return "Initialization of Array";
			case ArrayLength:
				return "Length of Array";
			case ArrayDimension:
				return "Dimension of Array";
			case ArrayBase:
				return "Base of Array";
			case ArrayElement:
				return "Element of Array";
			default:
				return super.toString();
			}
		}
		
		private String translate() {
			switch (this) {
			case New:
				return toString();
			default:
				return "From " + toString();
			}
		}
	}
	
	public static enum MetaSetVarGoal {
		Return,
		ClassEnvironment,
		MethodEnvironment,
		LocalEnvironment,
		Flow,
		ArraySubFlow,
		Effect;
		
		@Override
		public String toString() {
			switch (this) {
			case Return:
				return "Return";
			case ClassEnvironment:
				return "Class Environment";
			case MethodEnvironment:
				return "Method Environment";
			case LocalEnvironment:
				return "Local Environment";
			case Flow:
				return "Flow";
			case ArraySubFlow:
				return "Array Sub-Flow";
			case Effect:
				return "Effect";
			default:
				return super.toString();
			}
		}
	}
	
	public static enum EffectSetVarSource {
		New,
		MethodEnvironment,
		SubExpression,
		SubStatement,
		MethodCall;
		
		@Override
		public String toString() {
			switch (this) {
			case New:
				return "New";
			case MethodEnvironment:
				return "Method Environment";
			case SubExpression:
				return "Sub-Expression";
			case SubStatement:
				return "Sub-Statement";
			case MethodCall:
				return "Method Call";
			default:
				return super.toString();
			}
		}
	}
	
	public static enum EffectSetVarGoal {
		Return,
		MethodEnvironment,
		Flow;
		
		@Override
		public String toString() {
			switch (this) {
			case Return:
				return "Return";
			case MethodEnvironment:
				return "Method Environment";
			case Flow:
				return "Flow";
			default:
				return super.toString();
			}
		}
	}
}