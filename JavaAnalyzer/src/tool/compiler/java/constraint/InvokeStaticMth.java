package tool.compiler.java.constraint;

import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.types.Type;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.SetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.effect.Effect;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
 */
public class InvokeStaticMth implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;						// C
	 * MethodInstance m;			// method name m
	 * ArrayList<Type> ds;			// D1, ... , Dn
	 * ArrayList<SetVariable> xs;	// X1, ..., Xn
	 * (effect)						// effect
	 * Type e;						// E
	 * SetVariable y;				// Y
	 */
	
	/* ### Actual Fields ### */
	private JL5MethodInstance cm;		// C, m (NOT null)
	private ArrayList<? extends SetVariable> dxs;	// Ds, Xs ( D1{X1}, ..., Dn{Xn} ) (nullable)
	private HashMap<EffectName, Effect> effects;	// (effect) (nullable)
	private SetVariable ey;				// E, Y (nullable)
	
	
	// constructor
	
	/**
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cm	set C, m	( C.m )
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeStaticMth(JL5MethodInstance cm, List<? extends SetVariable> dxs, SetVariable ey) {
		super();
		this.cm = cm;
		if(dxs != null) {
			this.dxs = new ArrayList<SetVariable>(dxs);
		} else {
			this.dxs = null;
		}
		this.ey = ey;
	}
	
	/**
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cm	set C, m	( C.m )
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param effects	set effects
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeStaticMth(JL5MethodInstance cm, List<? extends SetVariable> dxs, Collection<Effect> effects, SetVariable ey) {
		this(cm, dxs, ey);
		if(effects != null) {
			this.effects = new HashMap<>();
			for(Effect effect : effects) {
				this.effects.put(effect.getType(), effect);
			}
		} else {
			this.effects = null;
		}
	}
	
	/**
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cm	set C, m	( C.m )
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param effects	set effects
	 * @param ey	set E, Y	( E{Y} )
	 */
	private InvokeStaticMth(JL5MethodInstance cm, List<? extends SetVariable> dxs, Map<EffectName, Effect> effects, SetVariable ey) {
		this(cm, dxs, ey);
		if(effects != null) {
			this.effects = new HashMap<>(effects);
		} else {
			this.effects = null;
		}
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 * @return		Substituted New Constraint
	 */
	public InvokeStaticMth substitute(List<TypedSetVariable> dxs, TypedSetVariable ey) {
		if(dxs != null) {
			if(this.dxs.size() != dxs.size()) {
				throw new IllegalArgumentException("The Size Mismatch for dxs.");
			}
			
			int i = 0;
			for(TypedSetVariable dx : dxs) {
				AbsObjSet thisdx = this.dxs.get(i);
				if(!thisdx.equalsForType(dx)) {
					throw new IllegalArgumentException("The Type Mismatch for dx" + ++i + ". "
							+ "(orig: " + thisdx.getType() + ", subst: " + dx.getType() + ")");
				}
				i++;
			}
		}
		
		if(ey != null && !this.ey.equalsForType(ey)) {
			throw new IllegalArgumentException("The Type Mismatch for ey. "
					+ "(orig: " + this.ey.getType() + ", subst: " + ey.getType() + ")");
		}
		
		return new InvokeStaticMth(this.cm, dxs, effects, ey);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param dxsey	D1{X1}, ..., Dn{Xn}, E{Y}
	 * @return		Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> dxsey) {
		if(dxsey.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + dxsey.size() + ".)");
		}
		LinkedList<TypedSetVariable> dxs = new LinkedList<>(dxsey);
		TypedSetVariable ey = null;
		if(this.ey != null) {
			ey = dxs.removeLast();
		}
		return substitute(dxs, ey);
	}
	
	
	// getter methods
	
	/**
	 * @return the C
	 */
	public Type getC() {
		return cm.container();
	}
	
	/**
	 * @return the m
	 */
	public JL5MethodInstance getM() {
		return cm;
	}
	
	/**
	 * @return D1{X1}, ..., Dn{Xn}
	 */
	public List<? extends SetVariable> getDXs() {
		return new ArrayList<>(dxs);
	}
	
	/**
	 * @param i	index
	 * @return Di{Xi}
	 */
	public SetVariable getDX(int i) {
		return dxs.get(i);
	}
	
	/**
	 * @return effects
	 */
	public final List<Effect> getEffects() {
		try {
			ArrayList<Effect> result =  new ArrayList<>();
			for(EffectName type : EffectName.values()) {
				Effect effect = effects.get(type);
				if (effect != null) {
					result.add(effect);
				}
			}
			return result;
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param type	Effect Type
	 * @return effect
	 */
	public final Effect getEffect(EffectName type) {
		try {
			return effects.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the E{Y}
	 */
	public SetVariable getEY() {
		return ey;
	}
	
	/**
	 * @return the E
	 */
	public Type getE() {
		return ey.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return ey.getID();
	}
	
	
	@Override
	public int absObjSetSize() {
		return (dxs != null ? dxs.size() : 0) + (ey != null ? 1 : 0);
	}
	
	@Override
	public int substitutableSize() {
		return (dxs != null ? dxs.size() : 0) + (ey != null ? 1 : 0);
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.addAll(dxs);
		abss.add(ey);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (dxs.contains(aos)) {
			return true;
		}
		if (ey.equals(aos)) {
			return true;
		}
		return false;
	}
	
	public boolean contains(Effect effect) {
		return effects.containsValue(effect);
	}
	
	public boolean contains(EffectName type) {
		return effects.containsKey(type);
	}
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Form:	C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 */
	@Override
	public String toString() {
		return getC() + "." + getM().name() + " <: " + CollUtil.getStringOf(getDXs(), '(', ')') 
				+ " --" + (effects != null ? " " + effects + " " : "") + "--> "	+ getEY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cm == null) ? 0 : cm.hashCode());
		result = prime * result + ((dxs == null) ? 0 : dxs.hashCode());
		result = prime * result + ((ey == null) ? 0 : ey.hashCode());
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
		InvokeStaticMth other = (InvokeStaticMth) obj;
		if (cm == null) {
			if (other.cm != null) {
				return false;
			}
		} else if (!cm.equals(other.cm)) {
			return false;
		}
		if (dxs == null) {
			if (other.dxs != null) {
				return false;
			}
		} else if (!dxs.equals(other.dxs)) {
			return false;
		}
		if (ey == null) {
			if (other.ey != null) {
				return false;
			}
		} else if (!ey.equals(other.ey)) {
			return false;
		}
		return true;
	}
}