package tool.compiler.java.env;

import java.util.Collection;
import java.util.LinkedHashSet;

import polyglot.types.CodeInstance;
import tool.compiler.java.constraint.Constraint;

public abstract class CodeConstraint implements ConstraintFunction {
	
	private CodeInstance instance;
	private ClassConstraint containerClassConstraint = null;	// Not inheritance, but containment relationship
	private LinkedHashSet<ClassConstraint> localClassConstraints;
	private LinkedHashSet<Constraint> metaConstraints;
	
	/**
	 * @param instance
	 */
	protected CodeConstraint(CodeInstance instance) {
		this.instance = instance;
	}
	
	/**
	 * @return the instance
	 */
	public CodeInstance getInstance() {
		return instance;
	}
	
	/**
	 * @return the containerClassConstraint
	 */
	@Override
	public ClassConstraint getOuter() {
		return containerClassConstraint;
	}
	
	/**
	 * @param containerClassConstraint the containerClassConstraint to set
	 */
	public void setOuter(ClassConstraint containerClassConstraint) {
		this.containerClassConstraint = containerClassConstraint;
	}
	
	/**
	 * @return localClassConstraints
	 */
	@Override
	public LinkedHashSet<ClassConstraint> getInners() {
		try {
			return new LinkedHashSet<>(localClassConstraints);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param localClassConstraints localClassConstraints to set
	 */
	@Override
	public void setInners(Collection<ClassConstraint> localClassConstraints) {
		if (localClassConstraints != null) {
			if (this.localClassConstraints == null) {
				this.localClassConstraints = new LinkedHashSet<>(localClassConstraints);
			} else {
				this.localClassConstraints.clear();
				this.localClassConstraints.addAll(localClassConstraints);
			}
		} else {
			this.localClassConstraints = null;
		}
	}
	
	/**
	 * @param localClassConstraints localClassConstraints to add
	 */
	@Override
	public void addInners(Collection<ClassConstraint> localClassConstraints) {
		if (localClassConstraints != null) {
			if (this.localClassConstraints == null) {
				this.localClassConstraints = new LinkedHashSet<>(localClassConstraints);
			} else {
				this.localClassConstraints.addAll(localClassConstraints);
			}
		}
	}
	
	/**
	 * @param localClassConstraint the localClassConstraint to add
	 */
	@Override
	public void addInner(ClassConstraint localClassConstraint) {
		if (localClassConstraint != null) {
			try {
				this.localClassConstraints.add(localClassConstraint);
			} catch (NullPointerException e) {
				this.localClassConstraints = new LinkedHashSet<>();
				this.localClassConstraints.add(localClassConstraint);
			}
		}
	}
	
	/**
	 * @return the metaConstraints
	 */
	@Override
	public LinkedHashSet<? extends Constraint> getMetaConstraints() {
		try {
			return new LinkedHashSet<>(metaConstraints);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param metaConstraints the metaConstraints to set
	 */
	@Override
	public void setMetaConstraints(Collection<? extends Constraint> metaConstraints) {
		if (metaConstraints != null) {
			if (this.metaConstraints == null) {
				this.metaConstraints = new LinkedHashSet<>(metaConstraints);
			} else {
				this.metaConstraints.clear();
				this.metaConstraints.addAll(metaConstraints);
			}
		} else {
			this.metaConstraints = null;
		}
	}
	
	/**
	 * @param metaConstraints the metaConstraints to add
	 */
	@Override
	public void addMetaConstraints(Collection<? extends Constraint> metaConstraints) {
		if (metaConstraints != null) {
			if (this.metaConstraints == null) {
				this.metaConstraints = new LinkedHashSet<>(metaConstraints);
			} else {
				this.metaConstraints.addAll(metaConstraints);
			}
		}
	}
	
	/**
	 * @param metaConstraint the metaConstraint to add
	 */
	@Override
	public void addMetaConstraint(Constraint metaConstraint) {
		if (metaConstraint != null) {
			try {
				this.metaConstraints.add(metaConstraint);
			} catch (NullPointerException e) {
				this.metaConstraints = new LinkedHashSet<>();
				this.metaConstraints.add(metaConstraint);
			}
		}
	}
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((metaConstraints == null) ? 0 : metaConstraints.hashCode());
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CodeConstraint other = (CodeConstraint) obj;
		if (instance == null) {
			if (other.instance != null) {
				return false;
			}
		} else if (!instance.equals(other.instance)) {
			return false;
		}
		if (metaConstraints == null) {
			if (other.metaConstraints != null) {
				return false;
			}
		} else if (!metaConstraints.equals(other.metaConstraints)) {
			return false;
		}
		return true;
	}
}
