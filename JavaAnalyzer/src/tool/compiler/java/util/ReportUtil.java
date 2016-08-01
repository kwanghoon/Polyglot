package tool.compiler.java.util;

import java.util.Collection;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.main.Report;
import tool.compiler.java.aos.AbstractObject;
import tool.compiler.java.aos.MetaSetVariable;
import tool.compiler.java.ast.EquGenExt;
import tool.compiler.java.constraint.Constraint;
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
		sb.append("\t[MetaSetVariable] ")
			.append(msv)
			.append(' ').append('(').append(' ')
			.append(src.translate())
			.append(": For ")
			.append(goal)
			.append(' ').append(')');
		Report.report(3, sb.toString());
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
		Environment,
		ArrayInit,
		ArrayLength,
		ArrayDimension,
		ArrayBase,
		ArrayElement;
		
		private String translate() {
			switch(this) {
			case New:
				return "New";
			case Receiver:
				return "From Receiver";
			case Lvalue:
				return "From L-value";
			case Rvalue:
				return "From R-value";
			case SubExpression:
				return "From Sub-Expression";
			case Argument:
				return "From Argument";
			case Environment:
				return "From Environment";
			case ArrayInit:
				return "From Initialization of Array";
			case ArrayLength:
				return "From Length of Array";
			case ArrayDimension:
				return "From Dimension of Array";
			case ArrayBase:
				return "From Base of Array";
			case ArrayElement:
				return "From Element of Array";
			default:
				return this.toString();
			}
		}
	}
	
	public static enum MetaSetVarGoal {
		Return,
		Environment,
		Flow,
		ArraySubFlow;
	}
}