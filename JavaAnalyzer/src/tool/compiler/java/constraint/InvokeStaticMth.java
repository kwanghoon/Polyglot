package tool.compiler.java.constraint;

import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.types.Type;
import tool.compiler.java.aos.AbsObjSet;
import tool.compiler.java.aos.DataFlowSetVariable;
import tool.compiler.java.aos.TypedSetVariable;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	private JL5MethodInstance cm;							// C, m (NOT null)
	private ArrayList<? extends DataFlowSetVariable> dxs;			// Ds, Xs ( D1{X1}, ..., Dn{Xn} ) (nullable)
	private LinkedHashMap<EffectName, DataFlowSetVariable> effects;	// (effect) (nullable)
	private DataFlowSetVariable ey;									// E, Y (NOT null)
	
	
	// constructors
	
	/**
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cm	set C, m	( C.m )
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeStaticMth(JL5MethodInstance cm, List<? extends DataFlowSetVariable> dxs, DataFlowSetVariable ey) {
		super();
		this.cm = cm;
		if(dxs != null && !dxs.isEmpty()) {
			this.dxs = new ArrayList<DataFlowSetVariable>(dxs);
		} else {
			this.dxs = null;
		}
		this.ey = ey;
	}
	
	/**
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cm		set C, m	( C.m )
	 * @param dxs		set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param effects	set effects
	 * @param ey		set E, Y	( E{Y} )
	 */
	private InvokeStaticMth(JL5MethodInstance cm, List<? extends DataFlowSetVariable> dxs, 
			Map<EffectName, ? extends DataFlowSetVariable> effects, DataFlowSetVariable ey) {
		this(cm, dxs, ey);
		if (effects != null && !effects.isEmpty()) {
			this.effects = new LinkedHashMap<>();
			for (EffectName type : EffectName.values()) {
				DataFlowSetVariable effect = effects.get(type);
				if (effect != null) {
					this.effects.put(type, effect);
				}
			}
		} else {
			this.effects = null;
		}
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param effects	set effects
	 * @param ey	set E, Y	( E{Y} )
	 * @return		Substituted New Constraint
	 */
	public InvokeStaticMth substitute(List<TypedSetVariable> dxs, 
			Map<EffectName, TypedSetVariable> effects, TypedSetVariable ey) {
		if (this.dxs == null) {				// 'dxs' is nullable
			if (dxs != null && !dxs.isEmpty()) {
				throw new IllegalArgumentException("The Orig dxs is null.");
			}
		} else {
			if (dxs == null || dxs.isEmpty()) {
				throw new IllegalArgumentException("The Orig dxs is NOT null.");
			}
			if (this.dxs.size() != dxs.size()) {
				throw new IllegalArgumentException("The Size Mismatch for dxs.");
			}
			int i = 0;
			for (TypedSetVariable dx : dxs) {
				AbsObjSet thisdx = this.dxs.get(i);
				if (!thisdx.equalsForType(dx)) {
					throw new IllegalArgumentException("The Type Mismatch for dx" + ++i + ". "
							+ "(orig: " + thisdx.getType() + ", subst: " + dx.getType() + ")");
				}
				i++;
			}
		}
		
		if (this.effects == null) {			// 'effects' is nullable
			if (effects != null && !effects.isEmpty()) {
				throw new IllegalArgumentException("The Orig 'effects' is null.");
			}
		} else {
			if (effects == null || effects.isEmpty()) {
				throw new IllegalArgumentException("The Orig 'effects' is NOT null.");
			}
			for (EffectName key : effects.keySet()) {
				if (!this.effects.containsKey(key)) {
					throw new IllegalArgumentException("The Orig 'effects' has no " + key + ".");
				}
			}
			for (Entry<EffectName, DataFlowSetVariable> effect : this.effects.entrySet()) {
				EffectName type = effect.getKey();
				if (effects.containsKey(type)) {
					if (!effect.getValue().equalsForType(effects.get(type))) {
						throw new IllegalArgumentException("The Type Mismatch for effect " + type + ". "
								+ "(orig: " + effect.getValue() + ", subst: " + effects.get(type) + ")");
					}
				} else {
					throw new IllegalArgumentException("The Subst 'effects' has no " + type + ".");
				}
			}
		}
		
		if (ey == null) {	// 'ey' is NOT null
			throw new IllegalArgumentException("The Orig ey is NOT null.");
		}
		if (!this.ey.equalsForType(ey)) {
			throw new IllegalArgumentException("The Type Mismatch for cx. "
					+ "(orig: " + this.ey.getType() + ", subst: " + ey.getType() + ")");
		}
		
		return new InvokeStaticMth(this.cm, dxs, effects, ey);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param dxseffsey	D1{X1}, ..., Dn{Xn}, effects and E{Y}
	 * @return			Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> dxseffsey) {
		if(dxseffsey.size() != substitutableSize()) {
			throw new IllegalArgumentException("The Size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + dxseffsey.size() + ".)");
		}
		LinkedList<TypedSetVariable> dxs = new LinkedList<>(dxseffsey);
		LinkedHashMap<EffectName, TypedSetVariable> effects = null;
		TypedSetVariable ey = dxs.removeLast();
		if (this.effects != null) {
			LinkedList<TypedSetVariable> effectList = new LinkedList<>();
			for (int i = 0; i < this.effects.size(); i++) {
				effectList.addFirst(dxs.removeLast());
			}
			effects = new LinkedHashMap<>();
			Iterator<TypedSetVariable> iterator = effectList.iterator();
			for (EffectName type : this.effects.keySet()) {
				effects.put(type, iterator.next());
			}
		}
		return substitute(dxs, effects, ey);
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
	public List<? extends DataFlowSetVariable> getDXs() {
		try {
			return new ArrayList<>(dxs);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param i	index (i >= 1)
	 * @return Di{Xi}
	 */
	public DataFlowSetVariable getDX(int i) {
		try {
			return dxs.get(i - 1);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return effects
	 */
	public Map<EffectName, ? extends DataFlowSetVariable> getEffects() {
		try {
			return new LinkedHashMap<>(effects);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param type	Effect Type
	 * @return effect
	 */
	public DataFlowSetVariable getEffect(EffectName type) {
		try {
			return effects.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the E{Y}
	 */
	public DataFlowSetVariable getEY() {
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
		return (dxs != null ? dxs.size() : 0) + (effects != null ? effects.size() : 0) + 1;
	}
	
	@Override
	public int substitutableSize() {
		return (dxs != null ? dxs.size() : 0) + (effects != null ? effects.size() : 0) + 1;
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		if (dxs != null) {
			abss.addAll(dxs);
		}
		if (effects != null) {
			abss.addAll(effects.values());
		}
		abss.add(ey);
		return abss;
	}
	
	public List<EffectName> getEffectList() {
		try {
			return new LinkedList<>(effects.keySet());
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (dxs.contains(aos)) {
			return true;
		}
		if (effects.containsValue(aos)) {
			return true;
		}
		if (ey.equals(aos)) {
			return true;
		}
		return false;
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
		result = prime * result + ((effects == null) ? 0 : effects.hashCode());
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
		if (effects == null) {
			if (other.effects != null) {
				return false;
			}
		} else if (!effects.equals(other.effects)) {
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