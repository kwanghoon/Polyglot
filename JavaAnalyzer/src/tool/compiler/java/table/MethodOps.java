package tool.compiler.java.table;

import java.util.List;

import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import tool.compiler.java.effect.Effect;
import tool.compiler.java.effect.EffectName;

public interface MethodOps extends TypingOps {
	public List<TypeVariable> getTypeParams();
	public List<? extends Type> getFormalTypes();
	public List<? extends ReferenceType> getMethodSubstitutionTypes();
	public List<Effect> getEffects();
	public Effect getEffect(EffectName type);
	public boolean isConstructor();
	public boolean isNormalMethod();
	public boolean isGenericMethod();
}
