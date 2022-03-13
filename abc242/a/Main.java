public final class Main {
  private static void execute(final IO io) throws Exception {
    double a = (double) io.nextInt();
    double b = (double) io.nextInt();
    double c = (double) io.nextInt();
    int x = io.nextInt();

     if (x <= a) {
       System.out.printf("%.12f%n", 1D);
     } else if (x <= b) {
      System.out.printf("%.12f%n", (c / (b - a)));
     } else {
      System.out.printf("%.12f%n", 0D);
     }
  }
 
  public static void main(final String[] args) throws Exception {
    try (IO io = new IO(System.in, System.out)) {
      execute(io);
    }
  }
 
  public static final class IO implements AutoCloseable {
    private final java.io.InputStream in;
    private final java.io.BufferedOutputStream out;
    private static final int BUFFER_SIZE = 1 << 16;
    private final byte[] buf = new byte[BUFFER_SIZE];
    private int pos = 0;
    private int end = 0;
    public IO(final java.io.InputStream in, final java.io.OutputStream out) throws java.io.IOException {
      this.in = in;
      this.out = new java.io.BufferedOutputStream(out);
    }
 
    @Override
    public void close() throws java.io.IOException {
      out.close();
    }
 
    private int fetchByte() throws java.io.IOException {
      if (pos >= end) {
        pos = 0;
        end = in.read(buf);
        if (end <= 0) {
          return -1;
        }
      }
      return buf[pos++];
    }
 
    public String nextString() throws java.io.IOException {
      int c;
      for (c = fetchByte(); c <= ' '; c = fetchByte()) {
      }
      final StringBuilder sb = new StringBuilder();
      for (; c > ' '; c = fetchByte()) {
        sb.append((char) c);
      }
      return sb.toString();
    }
 
    public String[] nextString(final int n) throws java.io.IOException {
      final String[] result = new String[n];
      for (int i = 0; i < n; i++) {
        result[i] = nextString();
      }
      return result;
    }
 
    public int nextInt() throws java.io.IOException {
      int val = 0;
      int c;
      for (c = fetchByte(); c <= ' '; c = fetchByte()) {
      }
      boolean neg = c == '-';
      if (c == '-' || c == '+') {
        c = fetchByte();
      }
      for (; c >= '0' && c <= '9'; c = fetchByte()) {
        val = (val << 3) + (val << 1) + (c & 15);
      }
      return neg ? -val : val;
    }
 
    public int[] nextInt(final int n) throws java.io.IOException {
      final int[] result = new int[n];
      for (int i = 0; i < n; i++) {
        result[i] = nextInt();
      }
      return result;
    }
 
    public long nextLong() throws java.io.IOException {
      long val = 0;
      int c;
      for (c = fetchByte(); c <= ' '; c = fetchByte()) {
      }
      boolean neg = c == '-';
      if (c == '-' || c == '+') {
        c = fetchByte();
      }
      for (; c >= '0' && c <= '9'; c = fetchByte()) {
        val = (val << 3) + (val << 1) + (c & 15);
      }
      return neg ? -val : val;
    }
 
    public long[] nextLong(final int n) throws java.io.IOException {
      final long[] result = new long[n];
      for (int i = 0; i < n; i++) {
        result[i] = nextLong();
      }
      return result;
    }
 
    public void print(final Object a) throws java.io.IOException {
      out.write(a.toString().getBytes());
    }
 
    private static final byte[] SP = new byte[] { 0x20 };
    public void printsp(final Object a) throws java.io.IOException {
      out.write(a.toString().getBytes());
      out.write(SP);
    }
 
    private static final byte[] CRLF = System.lineSeparator().getBytes();
    public void println() throws java.io.IOException {
      out.write(CRLF);
    }
 
    public void println(final Object a) throws java.io.IOException {
      out.write(a.toString().getBytes());
      out.write(CRLF);
    }
 
    public void printaln(final int[] a) throws java.io.IOException {
      for (int i = 0, n = a.length; i < n; i++) {
        out.write(Integer.toString(a[i]).getBytes());
        out.write(CRLF);
      }
    }
 
    public void printasp(final int[] a) throws java.io.IOException {
      for (int i = 0, n = a.length; i < n; i++) {
        out.write(Integer.toString(a[i]).getBytes());
        out.write(SP);
      }
    }
 
    public void printaln(final long[] a) throws java.io.IOException {
      for (int i = 0, n = a.length; i < n; i++) {
        out.write(Long.toString(a[i]).getBytes());
        out.write(CRLF);
      }
    }
 
    public void printasp(final long[] a) throws java.io.IOException {
      for (int i = 0, n = a.length; i < n; i++) {
        out.write(Long.toString(a[i]).getBytes());
        out.write(SP);
      }
    }
 
    public void printaln(final Object[] a) throws java.io.IOException {
      for (int i = 0, n = a.length; i < n; i++) {
        out.write(a[i].toString().getBytes());
        out.write(CRLF);
      }
    }
 
    public void printasp(final Object[] a) throws java.io.IOException {
      for (int i = 0, n = a.length; i < n; i++) {
        out.write(a[i].toString().getBytes());
        out.write(SP);
      }
    }
 
    public void flush() throws java.io.IOException {
      out.flush();
    }
  }
}