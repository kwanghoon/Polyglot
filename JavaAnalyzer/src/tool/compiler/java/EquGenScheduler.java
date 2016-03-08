package tool.compiler.java;

import polyglot.ast.NodeFactory;
import polyglot.frontend.CyclicDependencyException;
import polyglot.frontend.ExtensionInfo;
import polyglot.ext.jl7.JL7ExtensionInfo;
import polyglot.ext.jl7.JL7Scheduler;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.VisitorGoal;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import tool.compiler.java.visit.EquGenerator;

public class EquGenScheduler extends JL7Scheduler {
	
	public EquGenScheduler(JL7ExtensionInfo extInfo) { // tool.compiler.java.ExtensionInfo??
		super(extInfo);
	}

	public Goal EquGenerated(Job job) {
		ExtensionInfo extInfo = job.extensionInfo();
		TypeSystem ts = extInfo.typeSystem();
		NodeFactory nf = extInfo.nodeFactory();
		Goal goal = new VisitorGoal(job, new EquGenerator(job,ts,nf));
		try {
			goal.addPrerequisiteGoal(TypeChecked(job), this); // TODO: TypeChecked???
//			goal.addPrerequisiteGoal(Serialized(job), this);
		}
		catch(CyclicDependencyException e) {
			throw new InternalCompilerError(e);
		}
		return internGoal(goal);
	}
	
	// Add a pass before ReachabilityChecked (after TypeChecked)
	@Override
	public Goal ReachabilityChecked(Job job) {
		Goal goal = super.ReachabilityChecked(job);
		try {
			goal.addPrerequisiteGoal(EquGenerated(job), this);
		}
		catch(CyclicDependencyException e) {
			throw new InternalCompilerError(e);
		}
		return internGoal(goal);
	}
	
	// Add a pass before CodeGenerated
//	@Override
//	public Goal CodeGenerated(Job job) {
//		Goal cgGoal = super.CodeGenerated(job);
//		try {
//			cgGoal.addPrerequisiteGoal(EquGenerated(job), this);
//		}
//		catch(CyclicDependencyException e) {
//			throw new InternalCompilerError(e);
//		}
//		return internGoal(cgGoal);
//	}
}