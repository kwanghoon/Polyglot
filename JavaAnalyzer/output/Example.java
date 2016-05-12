class X {
    
    public X() { super(); }
    
    public X(int i) { this(); }
    
    public int mth(int i, String s) {
        char c;
        c = 'c';
        do  {
            if (c != '9') {
                switch (c) {
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
            } else
                if (c == '2') { continue; }
            c = 'c';
        }while(i > 100); 
        return 1;
    }
    
    public static final String jlc$CompilerVersion$jl7 = "2.7.0";
    public static final long jlc$SourceLastModified$jl7 = 1462869792000L;
    public static final String jlc$ClassType$jl7 =
      ("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");
}
