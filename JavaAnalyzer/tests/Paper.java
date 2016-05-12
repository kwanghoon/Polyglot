class Y {
	static char stf;
	static char right;
	static void stm() {
		
	}

}

class Z extends Y {
	
}
class X {
	Y f;
	static Y s;
	
	Y set (Y r) {
		this.f = r;
		X.s = r;
		Z z = new Z();
		Y y, y2;
		y = this.f;
		y2 = X.s;
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
		
		YY.<Y>gstm();
	}
	
}

class YY<T> {
	static void gstm() {
//		T t;	// wrong
	}
}


class Box<E> {
	E elem;

	E get() {
		return elem;
	}

	void set(E elem) {
		this.get(); // this의 타입은? this:Box<E> ===> this:Box<E>{Xi}

		Box<E> boxe = new Box<E>(); // o3
		Box<String> boxstr = new Box<String>(); // o4
		if (true) {

		}

		Box2<Integer> box2 = new Box2<>();	// o5

		Box<Long> boxl = new Box<>();	// o6
		isEmpty(boxl, elem);

		this.<E, String>isEmpty(boxstr, this.elem);
		this.<E, String>isEmpty(boxstr, this.elem);
		this.<Integer, Integer>AB(1,2);
		
		AB(3,4);
		
		ABC(2,"t",3.1);
		
		ABCD(2.1,"t",'y', box2);
		
		BoxClass<String> bcstr = new BoxClass<>(2);
		bcstr.test("aa");
		
		String str = new String();	// o7
		BoxClass<Integer> bcInt = new BoxClass<>("QQQQ");
		
	}

	<H extends E, G> boolean isEmpty(Box<G> box, Object hjkl) {
		return true;
	}

	<A, B> Integer AB(A box, B hjkl) {
		return 1;
	}
	
	<A, B> int ABC(B box, String aaa, A hjkl) {
		return 1;
	}
	
	<A, B ,C, D> int ABCD(B box, C aaa, A hjkl, D dd) {
		return 1;
	}
	
	class Box2<F> {
		<G> boolean mtd(Box<E> box) {
			return true;
		}
	}
	
	
}

class BoxClass<E> {
	
	public BoxClass() {
		
	}
	
	public BoxClass(int i) {
		
	}
	
	public <T> BoxClass(T tp) {
		
	}
	
	
	public void test(E elem) {
		Box<E> box = new Box<>();
		mth(2.1,"t",'y', box);
	}
	
	public <A, B ,C, D> int mth(A box, B aaa, C hjkl, D dd) {
		return 1;
	}
	
	private class Box<T> {
	}
}