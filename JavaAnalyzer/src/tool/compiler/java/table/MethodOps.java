package tool.compiler.java.table;

import java.util.List;
import java.util.Map;

import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.effect.EffectName;
import tool.compiler.java.effect.EffectSetVariable;

public interface MethodOps extends TypingOps {
	public List<TypeVariable> getTypeParams();
	public List<? extends Type> getFormalTypes();
	public List<? extends ReferenceType> getMethodSubstitutionTypes();
	public Map<EffectName, EffectSetVariable> getEffects();
	public EffectSetVariable getEffect(EffectName type);
	public boolean isConstructor();
	public boolean isNormalMethod();
	public boolean isGenericMethod();
}
