package tool.compiler.java.effect;

public enum EffectName {
	ExnEff,
	ActivityEff;
	
	@Override
	public String toString() {
		switch (this) {
		case ExnEff:
			return "Exception Effect";
		case ActivityEff:
			return "Activity Effect";
		}
		return "Effect";
	}
	
	public String getSimpleString() {
		switch (this) {
		case ExnEff:
			return "Exception";
		case ActivityEff:
			return "Activity";
		}
		return "Effect";
	}
}