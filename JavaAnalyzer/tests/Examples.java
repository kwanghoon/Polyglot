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
	int i;
	{
		i = 1;
	}
	Z<String> z;
	
	C() {						// MC
		z = new Z<>();			// o10, ObjsSubseteqX, InvokeMth
		z.setT("abc");			// o11 (StringLit), InvokeMth
	}
	
	int mth() {
		I i = new I(){
			public void m() {
				int i = 0;
			}
			class IC{
				
			}
		};
		
		class LocalClass {
			
		}
		return 1;
	}
	
	private class Inner {
		
	}
}

interface I {
	void m();
}

class CC extends C {
	int mth() {
		return 2;
	}
}

class CCC extends CC {
	static int ii = 1;
}

class CCCC extends CC {
	
	void mm () {
		CCC cc = new CCC();
		cc.ii += 3;
		
		cc.mth();
		
		int[] intArr1 = {1, 2, 3};	// ArrayInit
		int[] intArr2 = new int[3];	// NewArray
		int[][] intArr3 = {{1, 1}, {2, 2}, {3, 3}};	// ArrayInit
		int[][] intArr4 = new int[3][4];	// NewArray
		int[] intArr5 = new int[]{1,2,3};
		
		int aa;
		aa = intArr1[3];
		intArr1[2] = 4;
		intArr1[1] += 5;
		aa += 3;
		aa = 2;
		intArr3[2][3] = 8;
		
		
		int iii = cc.ii;
		int leng = intArr4.length;
		
		if((iii = leng) != 100000) {
			int iiii = 1;
			iii = 11;
		} else {
			int iiii = 2;
			iii = 22;
		}
		
		int a = 1;
		while(iii == 33) {
			a++;
		}
		
		class RR implements java.lang.AutoCloseable {
			int i = 1;
			@Override
			public void close() throws NullPointerException {
				i = 0;
				throw new IllegalArgumentException();
			}
		}
		
		try (RR r = new RR(); RR r2 = new RR()) {
			r.close();
			int it = 1;
			throw new NullPointerException();
		} catch(NullPointerException | IndexOutOfBoundsException e) {
			int ic = 2;
		} finally {
			int iff = 3;
		}
		
		do {
			aa = 10;
		} while (i == 0);
		
		LL: for (int elem : intArr1) {
			for (int i1 = 0, i2 = 1; i1 < 10 ; i1++, i2++) {
				if (i1 == 5) {
					continue LL;
				}
			}
		}
		
		switch (aa) {
		case 10:
			
			break;
			
		default:
			break;
		}
		
	}
}