class Y {					// CC
}							// MC (Default Constructor)

class X {					// CC
	Y f;
	
	void set(Y r) {			// MC
		this.f = r;			// ReadField, AssignField
	}
	
	void main() {			// MC
		X p = new X();		// o1, ObjsSubseteqX, InvokeMth
		Y q = new Y();		// o2, ObjsSubseteqX, InvokeMth
		p.set(q);			// InvokeMth
	}
}							// MC (Default Constructor)

class Z<T> {				// CC
	static X fX;
	T fT;
	int i = 1;
	
	static void setX(X s) {	// MC
		fX = s;				// ReadStaticField, AssignStaticField
	}
	
	void setT(T t) {		// MC
		fT = t;				// ReadField, AssignField
	}
	
	int run(String[] args, boolean b) {	// MC
		X r = new X();		// o3, ObjsSubseteqX, InvokeMth
		setX(r);			// InvokeStaticMth
		return 0;
	}
}							// MC (Default Constructor)

class C {					// CC
	Z<String> z;
	
	C() {					// MC
		z = new Z<>();		// o4, ObjsSubseteqX, InvokeMth
		z.setT("abc");		// InvokeMth
	}
}