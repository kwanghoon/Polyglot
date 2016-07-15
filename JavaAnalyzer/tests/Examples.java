class Y {						// CC
}								// MC (Default Constructor)

class X {						// CC
	Y f;
	
	void set(Y r) {				// MC
		this.f = r;				// ReadField, AssignField
	}
	
	void main() {				// MC
		X p = new X();			// o1, ObjsSubseteqX, InvokeMth
		Y q = new Y();			// o2, ObjsSubseteqX, InvokeMth
		p.set(q);				// InvokeMth
	}
}								// MC (Default Constructor)

class Z<T> {					// CC

	
	static void setX(X s) {		// MC
		fX = s;					// ReadStaticField, AssignStaticField
	}
	
	void setT(T t) {			// MC
		fT = t;					// ReadField, AssignField
	}
	
	boolean run(String[] args, boolean b) {	// MC
		X r = new X();			// o6, ObjsSubseteqX, InvokeMth
		setX(r);				// InvokeStaticMth
		X n = null;				// o7 (NullLit)
		Class<X> a = X.class;	// o8 (ClassLit)
		return true;			// o9 (booleanLit)
	}
	static X fX;
	T fT;
	int i = 1;					// o3 (IntLit)
	char c = 'c';				// o4 (CharLit)
	double d = 1.23;			// o5 (FloatLit)
	int ii = 22;
}								// MC (Default Constructor)

class C {						// CC
	Z<String> z;
	
	C() {						// MC
		z = new Z<>();			// o10, ObjsSubseteqX, InvokeMth
		z.setT("abc");			// o11 (StringLit), InvokeMth
	}
	
	int mth() {
		return 1;
	}
}

class CC extends C {
	int mth() {
		return 2;
	}
}

class CCC extends CC {
}

class CCCC extends CC {
	void mm () {
		CCC cc = new CCC();
		cc.mth();
		
		int[] intArr1 = {1, 2, 3};	// ArrayInit
		int[] intArr2 = new int[3];	// NewArray
		int[][] intArr3 = {{1, 1}, {2, 2}, {3, 3}};
		int[][] intArr4 = new int[3][4];	// NewArray
		
//		int leng = intArr.length;
	}
}