class X {				// [ClassMember] ClassDecl
	public X() {		// [ClassMember] ConstructorDecl
		super();		// [Stmt] ConstructorCall (SuperCall)
	}
	
	public X(int i) {	// [ClassMember] ConstructorDecl
		this();			// [Stmt] ConstructorCall (ThisCall)
	}
	
	public int mth(int i, String s) {	// [ClassMember] MethodDecl
		char c;							// [Stmt] LocalDecl
		c = 'c';						// [Stmt] Eval
		
		do {
			if(c != '9') {
				switch (c){
				case 'a':
					c = '1';
					break;
				case 'b':
					c = '2';
					break;
				default:
					c = '9';
					break;
				}
				break;
			}
			else if (c == '2'){
				continue;
			}
			
			c = 'c';
		} while(i > 100);
		
		return 1;		// [Stmt] Return
	}
}

// Block
// { stmt }

// ConstructorCall
// super( e1, ... , en)
// this( e1, ... , en)

// LocalDecl
// 

// Eval
// eval( Expr )

// Return
// return Expr

// Do
// do Block while Cond

// If
// if Cond Block1 else Bolck2

// Switch
// switch switchblock

// Case
// case Expr:

// SwitchBlock
// Block

// Branch
// break
// coutine


//------------------------
// Empty??

//LocalClassDecl??