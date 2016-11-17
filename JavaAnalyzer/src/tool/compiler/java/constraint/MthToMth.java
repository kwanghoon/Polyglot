package tool.compiler.java.constraint;

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
	private ArrayList<? extends DataFlowSetVariable> cxs;				// Cs, Xs ( C1{X1}, ..., Cn{Xn} )
	private LinkedHashMap<EffectName, DataFlowSetVariable> effects1;	// (effect1) (nullable)
	private DataFlowSetVariable e1x;									// E1, X
	private ArrayList<? extends DataFlowSetVariable> dys;				// Ds, Ys ( D1{Y1}, ..., Dn{Yn} )
	private LinkedHashMap<EffectName, DataFlowSetVariable> effects2;	// (effect2) (nullable)
	private DataFlowSetVariable e2y;									// E2, Y
	
	
	// constructors
	
	/**
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs	set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param e1x	set E1, X	( E1{X} )
	 * @param dys	set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param e2y	set E2, Y	( E2{Y} )
	 */
	public MthToMth(List<? extends DataFlowSetVariable> cxs, DataFlowSetVariable e1x, List<? extends DataFlowSetVariable> dys, DataFlowSetVariable e2y) {
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
	 * @param cxs		set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param effects1	set effects1
	 * @param e1x		set E1, X	( E1{X} )
	 * @param dys		set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param effects2	set effects2
	 * @param e2y		set E2, Y	( E2{Y} )
	 */
	private MthToMth(List<? extends DataFlowSetVariable> cxs, Map<EffectName, ? extends DataFlowSetVariable> effects1, DataFlowSetVariable e1x, 
			List<? extends DataFlowSetVariable> dys, Map<EffectName, ? extends DataFlowSetVariable> effects2, DataFlowSetVariable e2y) {
		this(cxs, e1x, dys, e2y);
		if (effects1 != null && !effects1.isEmpty()) {
			this.effects1 = new LinkedHashMap<>();
			for (EffectName type : EffectName.values()) {
				DataFlowSetVariable effect = effects1.get(type);
				if (effect != null) {
					this.effects1.put(type, effect);
				}
			}
		} else {
			this.effects1 = null;
		}
		if (effects2 != null && !effects2.isEmpty()) {
			this.effects2 = new LinkedHashMap<>();
			for (EffectName type : EffectName.values()) {
				DataFlowSetVariable effect = effects2.get(type);
				if (effect != null) {
					this.effects2.put(type, effect);
				}
			}
		} else {
			this.effects2 = null;
		}
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxs		set Cs, Xs	( C1{X1}, ..., Cn{Xn} )
	 * @param effects1	set effects1
	 * @param e1x		set E1, X	( E1{X} )
	 * @param dys		set Ds, Ys	( D1{Y1}, ..., Dn{Yn} )
	 * @param effects2	set effects2
	 * @param e2y		set E2, Y	( E2{Y} )
	 * @return			Substituted New Constraint
	 */
	public MthToMth substitute(List<TypedSetVariable> cxs, Map<EffectName, TypedSetVariable> effects1, TypedSetVariable e1x, 
			List<TypedSetVariable> dys, Map<EffectName, TypedSetVariable> effects2, TypedSetVariable e2y) {
		if (this.cxs == null) {				// 'cxs' is nullable
			if (cxs != null && !cxs.isEmpty()) {
				throw new IllegalArgumentException("The Orig cxs is null.");
			}
		} else {
			if (cxs == null || cxs.isEmpty()) {
				throw new IllegalArgumentException("The Orig cxs is NOT null.");
			}
			if (this.cxs.size() != cxs.size()) {
				throw new IllegalArgumentException("The Size Mismatch for cxs.");
			}
			int i = 0;
			for (TypedSetVariable cx : cxs) {
				AbsObjSet thiscx = this.cxs.get(i);
				if (!thiscx.equalsForType(cx)) {
					throw new IllegalArgumentException("The Type Mismatch for cx" + ++i + ". "
							+ "(orig: " + thiscx.getType() + ", subst: " + cx.getType() + ")");
				}
				i++;
			}
		}
		
		if (this.effects1 == null) {		// 'effects1' is nullable
			if (effects1 != null && !effects1.isEmpty()) {
				throw new IllegalArgumentException("The Orig 'effects' is null.");
			}
		} else {
			if (effects1 == null || effects1.isEmpty()) {
				throw new IllegalArgumentException("The Orig 'effects' is NOT null.");
			}
			for (EffectName key : effects1.keySet()) {
				if (!this.effects1.containsKey(key)) {
					throw new IllegalArgumentException("The Orig 'effects' has no " + key + ".");
				}
			}
			for (Entry<EffectName, DataFlowSetVariable> effect : this.effects1.entrySet()) {
				EffectName type = effect.getKey();
				if (effects1.containsKey(type)) {
					if (!effect.getValue().equalsForType(effects1.get(type))) {
						throw new IllegalArgumentException("The Type Mismatch for effect " + type + ". "
								+ "(orig: " + effect.getValue() + ", subst: " + effects1.get(type) + ")");
					}
				} else {
					throw new IllegalArgumentException("The Subst 'effects' has no " + type + ".");
				}
			}
		}
		
		if (this.e1x == null) {			// 'e1x' is nullable
			if (e1x != null) {
				throw new IllegalArgumentException("The Orig e1x is null.");
			}
		} else {
			if (e1x == null) {
				throw new IllegalArgumentException("The Orig e1x is NOT null.");
			}
			if (!this.e1x.equalsForType(e1x)) {
				throw new IllegalArgumentException("The Type Mismatch for e1x. "
						+ "(orig: " + this.e1x.getType() + ", subst: " + e1x.getType() + ")");
			}
		}
		
		if (this.dys == null) {			// 'dys' is nullable
			if (dys != null && !dys.isEmpty()) {
				throw new IllegalArgumentException("The Orig dys is null.");
			}
		} else {
			if (dys == null || dys.isEmpty()) {
				throw new IllegalArgumentException("The Orig dys is NOT null.");
			}
			if (this.dys.size() != dys.size()) {
				throw new IllegalArgumentException("The Size Mismatch for dys.");
			}
			int i = 0;
			for (TypedSetVariable dy : dys) {
				AbsObjSet thisdy = this.dys.get(i);
				if (!thisdy.equalsForType(dy)) {
					throw new IllegalArgumentException("The Type Mismatch for dy" + ++i + ". "
							+ "(orig: " + thisdy.getType() + ", subst: " + dy.getType() + ")");
				}
				i++;
			}
		}
		
		if (this.effects2 == null) {		// 'effects2' is nullable
			if (effects2 != null && !effects2.isEmpty()) {
				throw new IllegalArgumentException("The Orig 'effects' is null.");
			}
		} else {
			if (effects2 == null || effects2.isEmpty()) {
				throw new IllegalArgumentException("The Orig 'effects' is NOT null.");
			}
			for (EffectName key : effects2.keySet()) {
				if (!this.effects2.containsKey(key)) {
					throw new IllegalArgumentException("The Orig 'effects' has no " + key + ".");
				}
			}
			for (Entry<EffectName, DataFlowSetVariable> effect : this.effects2.entrySet()) {
				EffectName type = effect.getKey();
				if (effects2.containsKey(type)) {
					if (!effect.getValue().equalsForType(effects2.get(type))) {
						throw new IllegalArgumentException("The Type Mismatch for effect " + type + ". "
								+ "(orig: " + effect.getValue() + ", subst: " + effects2.get(type) + ")");
					}
				} else {
					throw new IllegalArgumentException("The Subst 'effects' has no " + type + ".");
				}
			}
		}
		
		if (this.e2y == null) {	// 'e2y' is nullable
			if (e2y != null) {
				throw new IllegalArgumentException("The Orig e2y is null.");
			}
		} else {
			if (e2y == null) {
				throw new IllegalArgumentException("The Orig e2y is NOT null.");
			}
			if (!this.e2y.equalsForType(e2y)) {
				throw new IllegalArgumentException("The Type Mismatch for e2y. "
						+ "(orig: " + this.e2y.getType() + ", subst: " + e2y.getType() + ")");
			}
		}
		
		return new MthToMth(cxs, effects1, e1x, dys, effects2, e2y);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * (C1{X1}, ..., Cn{Xn}) -- effect1 --> E1{X} 
	 * 	<: (D1{Y1}, ..., Dn{Ym}) -- effect2 --> E2{Y}
	 * @param cxseffs1e1xdyseffs2e2y	C1{X1}, ..., Cn{Xn}, E1{X}, D1{Y1}, ..., Dn{Ym}, E2{Y}
	 * @return							Substituted New Constraint
	 */
	@Override
	public Constraint substitute(List<TypedSetVariable> cxseffs1e1xdyseffs2e2y) {
		if(cxseffs1e1xdyseffs2e2y.size() != substitutableSize()) {
			throw new IllegalArgumentException("The size of tsvs must be " + substitutableSize() + ". "
					+ "(Current size is " + cxseffs1e1xdyseffs2e2y.size() + ".)");
		}
		LinkedList<TypedSetVariable> cxs;
		LinkedHashMap<EffectName, TypedSetVariable> effects1 = null;
		TypedSetVariable e1x = null;
		LinkedList<TypedSetVariable> dys = new LinkedList<>(cxseffs1e1xdyseffs2e2y);
		LinkedHashMap<EffectName, TypedSetVariable> effects2 = null;
		TypedSetVariable e2y = null;
		cxs = new LinkedList<>(dys.subList(0, this.cxs.size() - 1));
		dys.removeAll(cxs);
		if(this.e1x != null) {
			e1x = dys.removeFirst();
		}		
		if(this.e2y != null) {
			e2y = dys.removeLast();
		}
		if (this.effects1 != null) {
			LinkedList<TypedSetVariable> effectList1 = new LinkedList<>();
			for (int i = 0; i < this.effects1.size(); i++) {
				effectList1.addFirst(cxs.removeLast());
			}
			effects1 = new LinkedHashMap<>();
			Iterator<TypedSetVariable> iterator = effectList1.iterator();
			for (EffectName type : this.effects1.keySet()) {
				effects1.put(type, iterator.next());
			}
		}
		if (this.effects2 != null) {
			LinkedList<TypedSetVariable> effectList2 = new LinkedList<>();
			for (int i = 0; i < this.effects2.size(); i++) {
				effectList2.addFirst(dys.removeLast());
			}
			effects2 = new LinkedHashMap<>();
			Iterator<TypedSetVariable> iterator = effectList2.iterator();
			for (EffectName type : this.effects2.keySet()) {
				effects2.put(type, iterator.next());
			}
		}
		return substitute(cxs, effects1, e1x, dys, effects2, e2y);
	}
	
	
	// getter methods
	
	/**
	 * @return C1{X1}, ..., Cn{Xn}
	 */
	public List<? extends DataFlowSetVariable> getCXs() {
		try {
			return new ArrayList<>(cxs);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param i	index (i >= 1)
	 * @return Ci{Xi}
	 */
	public DataFlowSetVariable getCX(int i) {
		try {
			return cxs.get(i - 1);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return effects1
	 */
	public Map<EffectName, ? extends DataFlowSetVariable> getEffects1() {
		try {
			return new LinkedHashMap<>(effects1);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param type	Effect Type
	 * @return effect1
	 */
	public DataFlowSetVariable getEffect1(EffectName type) {
		try {
			return effects1.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the E1{X}
	 */
	public DataFlowSetVariable getE1X() {
		return e1x;
	}
	
	/**
	 * @return the E1
	 */
	public Type getE1() {
		try {
			return e1x.getType();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		try {
			return e1x.getID();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return D1{Y1}, ..., Dn{Yn}
	 */
	public List<? extends DataFlowSetVariable> getDYs() {
		try {
			return new ArrayList<>(dys);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param i	index (i >= 1)
	 * @return Di{Yi}
	 */
	public DataFlowSetVariable getDY(int i) {
		try {
			return dys.get(i - 1);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return effects2
	 */
	public Map<EffectName, ? extends DataFlowSetVariable> getEffects2() {
		try {
			return new LinkedHashMap<>(effects2);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @param type	Effect Type
	 * @return effect2
	 */
	public DataFlowSetVariable getEffect2(EffectName type) {
		try {
			return effects2.get(type);
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the E2{Y}
	 */
	public DataFlowSetVariable getE2Y() {
		return e2y;
	}
	
	/**
	 * @return the E2
	 */
	public Type getE2() {
		try {
			return e2y.getType();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		try {
			return e2y.getID();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	
	@Override
	public int absObjSetSize() {
		return (cxs != null ? cxs.size() : 0) + (effects1 != null ? effects1.size() : 0) + (e1x != null ? 1 : 0)
				+ (dys != null ? dys.size() : 0) + (effects2 != null ? effects2.size() : 0) + (e2y != null ? 1 : 0);
	}
	
	@Override
	public int substitutableSize() {
		return (cxs != null ? cxs.size() : 0) + (effects1 != null ? effects1.size() : 0) + (e1x != null ? 1 : 0)
				+ (dys != null ? dys.size() : 0) + (effects2 != null ? effects2.size() : 0) + (e2y != null ? 1 : 0);
	}
	
	@Override
	public List<? extends AbsObjSet> getAllAbsObjSets() {
		ArrayList<DataFlowSetVariable> abss = new ArrayList<>();
		if (cxs != null) {
			abss.addAll(cxs);
		}
		if (effects1 != null) {
			abss.addAll(effects1.values());
		}
		if (e1x != null) {
			abss.add(e1x);
		}
		if (dys != null) {
			abss.addAll(dys);
		}
		if (effects2 != null) {
			abss.addAll(effects2.values());
		}
		if (e2y != null) {
			abss.add(e2y);
		}
		return abss;
	}
	
	public List<EffectName> getEffect1List() {
		try {
			return new LinkedList<>(effects1.keySet());
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public List<EffectName> getEffect2List() {
		try {
			return new LinkedList<>(effects2.keySet());
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (cxs != null && cxs.contains(aos)) {
			return true;
		}
		if (effects1 != null && effects1.containsValue(aos)) {
			return true;
		}
		if (e1x != null && e1x.equals(aos)) {
			return true;
		}
		if (dys != null && dys.contains(aos)) {
			return true;
		}
		if (effects2 != null && effects2.containsValue(aos)) {
			return true;
		}
		if (e2y != null && e2y.equals(aos)) {
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
		result = prime * result + ((effects1 == null) ? 0 : effects1.hashCode());
		result = prime * result + ((e1x == null) ? 0 : e1x.hashCode());
		result = prime * result + ((dys == null) ? 0 : dys.hashCode());
		result = prime * result + ((effects2 == null) ? 0 : effects2.hashCode());
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
		if (effects1 == null) {
			if (other.effects1 != null) {
				return false;
			}
		} else if (!effects1.equals(other.effects1)) {
			return false;
		}
		if (e1x == null) {
			if (other.e1x != null) {
				return false;
			}
		} else if (!e1x.equals(other.e1x)) {
			return false;
		}
		if (dys == null) {
			if (other.dys != null) {
				return false;
			}
		} else if (!dys.equals(other.dys)) {
			return false;
		}
		if (effects2 == null) {
			if (other.effects2 != null) {
				return false;
			}
		} else if (!effects2.equals(other.effects2)) {
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