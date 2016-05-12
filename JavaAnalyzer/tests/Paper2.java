class Y {
	Y() {
		int aaaaa;
	}
	
	static char stf;
	static char right;
	static void stm() {
		X.stt();
	}

}

class Z extends Y {
	
	Z() {
		super();
	}
	
	int aa (){
		X a = new X();
		Y y = null;
		a.f = y;
		
		return 1;
	}
}
class X {
	Y f;
	static Y s;
	Y ss;
	
	Y set (Y r) {
		
		X.s = r;
		Z z = new Z();
		Y y, y2, y3;
		y = this.f;// = r;
		y2 = X.s;
		
		y3 = f;
		ss = y3;
		
		
		return z;
	}
	
	void main() {
		Y.stm();
		char a = Y.stf;
		a = Y.stf = Y.right;
		
		X p = new X();	// o1
		Y q = new Y();	// o2
		X r = new X();
		p.set(q);
		
	}
	
	static int stt() {
		return 0;
	}
	
}
