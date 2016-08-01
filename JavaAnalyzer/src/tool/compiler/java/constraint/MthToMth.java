package tool.compiler.java.constraint;

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
 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}<br>
 * 	(Not mandatory)
 */
public class MthToMth implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * ArrayList<Type> cs;			// C1, ... , Cn
	 * ArrayList<SetVariable> xs;	// X1, ..., Xn
	 * (effect1)					// effect1
	 * Type e1;						// E1
	 * SetVariable x;				// X
	 * 
	 * ArrayList<Type> ds;			// D1, ... , Dn
	 * ArrayList<SetVariable> ys;	// Y1, ..., Yn
	 * (effect2)					// effect2
	 * Type e2;						// E2
	 * SetVariable y;				// Y
	 */
	
	/* ### Actual Fields ### */
	private ArrayList<? extends SetVariable> cxs;	// Cs, Xs ( C1{X1}, ..., Cn{Xn} )
	private HashMap<EffectName, Effect> effects1;	// (effect1) (nullable)
	private SetVariable e1x;				// E1, X
	private ArrayList<? extends SetVariable> dys;	// Ds, Ys ( D1{Y1}, ..., Dn{Yn} )
	private HashMap<EffectName, Effect> effects2;	// (effect2) (nullable)
	private SetVariable e2y;				// E2, Y
	
	
	// constructor
	
	/**
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs	set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param e1x	set E1, X	( E1{X} )
	 * @param dys	set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param e2y	set E2, Y	( E2{Y} )
	 */
	public MthToMth(List<? extends SetVariable> cxs, SetVariable e1x, List<? extends SetVariable> dys, SetVariable e2y) {
		super();
		try {
			this.cxs = new ArrayList<>(cxs);
		} catch (NullPointerException e) {
			this.cxs = null;
		}
		this.e1x = e1x;
		try {
			this.dys = new ArrayList<>(dys);
		} catch (NullPointerException e) {
			this.dys = null;
		}
		this.e2y = e2y;
	}
	
	/**
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs	set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param effects1	set effects1
	 * @param e1x	set E1, X	( E1{X} )
	 * @param dys	set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param effects2	set effects2
	 * @param e2y	set E2, Y	( E2{Y} )
	 */
	public MthToMth(List<? extends SetVariable> cxs, Collection<Effect> effects1, SetVariable e1x, List<? extends SetVariable> dys, Collection<Effect> effects2, SetVariable e2y) {
		this(cxs, e1x, dys, e2y);
		if(effects1 != null) {
			this.effects1 = new HashMap<>();
			for(Effect effect : effects1) {
				this.effects1.put(effect.getType(), effect);
			}
		} else {
			this.effects1 = null;
		}
		if(effects2 != null) {
			this.effects2 = new HashMap<>();
			for(Effect effect : effects2) {
				this.effects2.put(effect.getType(), effect);
			}
		} else {
			this.effects2 = null;
		}
	}
	
	/**
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs	set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param effects1	set effects1
	 * @param e1x	set E1, X	( E1{X} )
	 * @param dys	set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param effects2	set effects2
	 * @param e2y	set E2, Y	( E2{Y} )
	 */
	private MthToMth(List<? extends SetVariable> cxs, Map<EffectName, Effect> effects1, SetVariable e1x, List<? extends SetVariable> dys, Map<EffectName, Effect> effects2, SetVariable e2y) {
		this(cxs, e1x, dys, e2y);
		if(effects1 != null) {
			this.effects1 = new HashMap<>(effects1);
		} else {
			this.effects1 = null;
		}
		if(effects2 != null) {
			this.effects2 = new HashMap<>(effects2);
		} else {
			this.effects2 = null;
		}
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs	set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param e1x	set E1, X	( E1{X} )
	 * @param dys	set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param e2y	set E2, Y	( E2{Y} )
	 * @return		Substituted New Constraint
	 */
	public MthToMth substitute(List<TypedSetVariable> cxs, TypedSetVariable e1x, List<TypedSetVariable> dys, TypedSetVariable e2y) {
		if(cxs != null) {
			if(this.cxs.size() != cxs.size()) {
				throw new IllegalArgumentException("The Size Mismatch for cxs.");
			}
			
			int i = 0;
			for(TypedSetVariable cx : cxs) {
				AbsObjSet thiscx = this.cxs.get(i);
				if(!thiscx.equalsForType(cx)) {
					throw new IllegalArgumentException("The Type Mismatch for cx" + ++i + ". "
							+ "(orig: " + thiscx.getType() + ", subst: " + cx.getType() + ")");
				}
				i++;
			}
		}
		
		if(e1x != null && !this.e1x.equalsForType(e1x)) {
			throw new IllegalArgumentException("The Type Mismatch for e1x. "
					+ "(orig: " + this.e1x.getType() + ", subst: " + e1x.getType() + ")");
		}
		
		if(dys != null) {
			if(this.dys.size() != dys.size()) {
				throw new IllegalArgumentException("The Size Mismatch for dys.");
			}
			
			int i = 0;
			for(TypedSetVariable dy : dys) {
				AbsObjSet thisdy = this.dys.get(i);
				if(!thisdy.equalsForType(dy)) {
					throw new IllegalArgumentException("The Type Mismatch for dys" + ++i + ". "
							+ "(orig: " + thisdy.getType() + ", subst: " + dy.getType() + ")");
				}
				i++;
			}
		}
		
		if(e2y != null && !this.e2y.equalsForType(e2y)) {
			throw new IllegalArgumentException("The Type Mismatch for ey. "
					+ "(orig: " + this.e2y.getType() + ", subst: " + e2y.getType() + ")");
		}
		
		return new MthToMth(cxs, effects1, e1x, dys, effects2, e2y);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxse1xdyse2y	C1{X1}, ..., Cn{Xn}, E1{X}, D1{Y1}, ..., Dn{Ym}, E2{Y}
	 * @return				Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> cxse1xdyse2y) {
		if(cxse1xdyse2y.size() != substitutableSize()) {
			throw new IllegalArgumentException("The size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + cxse1xdyse2y.size() + ".)");
		}
		List<TypedSetVariable> cxs;
		TypedSetVariable e1x = null;
		LinkedList<TypedSetVariable> dys = new LinkedList<>(cxse1xdyse2y);
		TypedSetVariable e2y = null;
		cxs = dys.subList(0, this.cxs.size() - 1);
		dys.removeAll(cxs);
		if(this.e1x != null) {
			e1x = dys.removeFirst();
		}		
		if(this.e2y != null) {
			e2y = dys.removeLast();
		}
		return substitute(cxs, e1x, dys, e2y);
	}
	
	
	// getter methods
	
	/**
	 * @return C1{X1}, ..., Cn{Xn}
	 */
	public List<? extends SetVariable> getCXs() {
		return new ArrayList<>(cxs);
	}
	
	/**
	 * @param i	index
	 * @return Ci{Xi}
	 */
	public SetVariable getCX(int i) {
		return cxs.get(i);
	}
	
	/**
	 * @return effects1
	 */
	public final List<Effect> getEffects1() {
		try {
			ArrayList<Effect> result =  new ArrayList<>();
			for(EffectName type : EffectName.values()) {
				Effect effect = effects1.get(type);
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
	 * @return effect of effects1
	 */
	public final Effect getEffect1(EffectName type) {
		try {
			return effects1.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the E1{X}
	 */
	public SetVariable getE1X() {
		return e1x;
	}
	
	/**
	 * @return the E1
	 */
	public Type getE1() {
		return e1x.getType();
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		return e1x.getID();
	}
	
	/**
	 * @return D1{Y1}, ..., Dn{Yn}
	 */
	public List<? extends SetVariable> getDYs() {
		return new ArrayList<>(dys);
	}
	
	/**
	 * @param i	index
	 * @return Di{Yi}
	 */
	public SetVariable getDY(int i) {
		return dys.get(i);
	}
	
	/**
	 * @return effects2
	 */
	public final List<Effect> getEffects2() {
		try {
			ArrayList<Effect> result =  new ArrayList<>();
			for(EffectName type : EffectName.values()) {
				Effect effect = effects2.get(type);
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
	 * @return effect of effects2
	 */
	public final Effect getEffect2(EffectName type) {
		try {
			return effects2.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the E2{Y}
	 */
	public SetVariable getE2Y() {
		return e2y;
	}
	
	/**
	 * @return the E2
	 */
	public Type getE2() {
		return e2y.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return e2y.getID();
	}
	
	
	@Override
	public int absObjSetSize() {
		return (cxs != null ? cxs.size() : 0) + (e1x != null ? 1 : 0)
				+ (dys != null ? dys.size() : 0) + (e2y != null ? 1 : 0);
	}
	
	@Override
	public int substitutableSize() {
		return (cxs != null ? cxs.size() : 0) + (e1x != null ? 1 : 0)
				+ (dys != null ? dys.size() : 0) + (e2y != null ? 1 : 0);
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<SetVariable> abss = new ArrayList<>();
		abss.addAll(cxs);
		abss.add(e1x);
		abss.addAll(dys);
		abss.add(e2y);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (cxs.contains(aos)) {
			return true;
		}
		if (e1x.equals(aos)) {
			return true;
		}
		if (dys.contains(aos)) {
			return true;
		}
		if (e2y.equals(aos)) {
			return true;
		}
		return false;
	}
	
	public boolean contains(Effect effect) {
		if (effects1.containsValue(effect)) {
			return true;
		}
		if (effects2.containsValue(effect)) {
			return true;
		}
		return false;
	}
	
	public boolean contains(EffectName type) {
		if (effects1.containsKey(type)) {
			return true;
		}
		if (effects2.containsKey(type)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getKind() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Form:	(C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 				<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 */
	@Override
	public String toString() {
		return CollUtil.getStringOf(getCXs(), '{', '}') + " --" + (effects1 != null ? " " + effects1 + " " : "") + "--> "	+ getE1X() 
				+ " <: " + CollUtil.getStringOf(getDYs(), '{', '}') + " --" + (effects2 != null ? " " + effects2 + " " : "") + "--> "	+ getE2Y();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cxs == null) ? 0 : cxs.hashCode());
		result = prime * result + ((e1x == null) ? 0 : e1x.hashCode());
		result = prime * result + ((dys == null) ? 0 : dys.hashCode());
		result = prime * result + ((e2y == null) ? 0 : e2y.hashCode());
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
		MthToMth other = (MthToMth) obj;
		if (cxs == null) {
			if (other.cxs != null) {
				return false;
			}
		} else if (!cxs.equals(other.cxs)) {
			return false;
		}
		if (dys == null) {
			if (other.dys != null) {
				return false;
			}
		} else if (!dys.equals(other.dys)) {
			return false;
		}
		if (e1x == null) {
			if (other.e1x != null) {
				return false;
			}
		} else if (!e1x.equals(other.e1x)) {
			return false;
		}
		if (e2y == null) {
			if (other.e2y != null) {
				return false;
			}
		} else if (!e2y.equals(other.e2y)) {
			return false;
		}
		return true;
	}
}