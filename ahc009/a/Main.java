import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.lang.Math;

public final class Main {

  static final int MOD = 998244353;
  static final int INF = Integer.MAX_VALUE;
  static final StringBuilder sb = new StringBuilder();
  static final FastReader fs = new FastReader();

  static Cell[][] map = new Cell[20][20];

  public static void main(String[] args) throws IOException {
    int si = fs.nextInt();
    int sj = fs.nextInt();
    int ti = fs.nextInt();
    int tj = fs.nextInt();
    double p = fs.nextDouble();

    // initialize
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        map[i][j] = new Cell();
      }
    }

    // read wall
    for (int i = 0; i < 20; i++) {
      String wall = fs.next();
      for (int j = 0; j < 19; j++) {
        map[i][j].R = (wall.charAt(j) == '0');
        map[i][j + 1].L = (wall.charAt(j) == '0');
      }
    }
    for (int i = 0; i < 19; i++) {
      String wall = fs.next();
      for (int j = 0; j < 20; j++) {
        map[i][j].D = (wall.charAt(j) == '0');
        map[i + 1][j].U = (wall.charAt(j) == '0');
      }
    }
    System.out.println("");

    // goal setting
    map[ti][tj].far = 0;

    search(ti, tj);

    // 短い距離から確定させていく
    for (int target = 1; target < 400; target++) {
      for (int i = 19; i >= 0; i--) {
        for (int j = 19; j >= 0; j--) {
          if (map[i][j].far == target) {
            search(i, j);
          }
        }
      }
    }

    String ans = searchRoute(si, sj, p).repeat(10).substring(0, 200);
    System.out.println(ans);

    // // display for debug
    // for (int i = 0; i < 20; i++) {
    // for (int j = 0; j < 20; j++) {
    // System.out.printf("%02d ", map[i][j].far);
    // }
    // System.out.println("");
    // }

  }

  static String searchRoute(int y, int x, double p) {
    StringBuilder route = new StringBuilder();
    // 0...to up
    // 1...to right
    // 2...to down
    // 3...to left
    int[] dx = {0, 1, 0, -1};
    int[] dy = {-1, 0, 1, 0};
    String[] ds = {"U", "R", "D", "L"};

    int posy = y;
    int posx = x;

    int di = 0;
    while (true) {
      int loopCnt = 0;
      while (true) {
        loopCnt++;
        int sx = posx + dx[di];
        int sy = posy + dy[di];
        if (sx < 0 || sx > 19 || sy < 0 || sy > 19) {
          // なるべく向きを変えずに探索
          route.append(ds[di].repeat((int) Math.round(loopCnt * p)));
          di = (di + 1) % 4;
          break;
        }
        // 壁判定
        if ((di == 0 && !map[sy][sx].D || di == 1 && !map[sy][sx].L || di == 2 && !map[sy][sx].U
            || di == 3 && !map[sy][sx].R)) {
          // なるべく向きを変えずに探索
          route.append(ds[di].repeat((int) Math.round(loopCnt * p)));
          di = (di + 1) % 4;
          break;
        }
        if (map[posy][posx].far - 1 == map[sy][sx].far) {
          route.append(ds[di]);
          posy = sy;
          posx = sx;
          if (map[posy][posx].far == 0) {
            return route.toString();
          }
        } else {
          // なるべく向きを変えずに探索
          route.append(ds[di].repeat((int) Math.round(loopCnt * p)));
          di = (di + 1) % 4;
          break;
        }
      }
    }
  }

  static void search(int y, int x) {
    // 0...to up
    // 1...to right
    // 2...to down
    // 3...to left
    int[] dx = {0, 1, 0, -1};
    int[] dy = {-1, 0, 1, 0};

    for (int i = 0; i < 4; i++) {
      int loopcnt = 0;
      while (true) {
        loopcnt++;
        int sx = x + dx[i] * loopcnt;
        int sy = y + dy[i] * loopcnt;
        // マップ外判定
        if (sx < 0 || sx > 19 || sy < 0 || sy > 19)
          break;
        // 壁判定
        if ((i == 0 && !map[sy][sx].D || i == 1 && !map[sy][sx].L || i == 2 && !map[sy][sx].U
            || i == 3 && !map[sy][sx].R)) {
          break;
        }
        map[sy][sx].far = Math.min(map[sy - dy[i]][sx - dx[i]].far + 1, map[sy][sx].far);
      }
    }
  }

  static class Cell {
    public boolean U = false;
    public boolean D = false;
    public boolean L = false;
    public boolean R = false;
    public int far = INF;
    public boolean fix = false;

    Cell() {

    }

    Cell(boolean u, boolean d, boolean l, boolean r) {
      this.U = u;
      this.D = d;
      this.L = l;
      this.R = r;
    }
  }

  static <T extends Comparable<T>> int myLowerBound(List<T> list, T target) {
    return ~Collections.binarySearch(list, target, (x, y) -> x.compareTo(y) >= 0 ? 1 : -1);
  }

  static <T extends Comparable<T>> int myUpperBound(List<T> list, T target) {
    return ~Collections.binarySearch(list, target, (x, y) -> x.compareTo(y) > 0 ? 1 : -1);
  }

  static class UnionFind {
    int[] parent;
    int[] rank;

    public UnionFind(int n) {
      // 初期化コンストラクタ
      this.parent = new int[n];
      this.rank = new int[n];

      // 最初はすべてが根
      for (int i = 0; i < n; i++) {
        parent[i] = i;
        rank[i] = 0;
      }
    }

    /**
     * 要素の根を返す。 経路圧縮付き。（1→3→2となっていて2をfindした際、1→3,2と木の深さを浅くする。）
     *
     * @param x
     * @return 要素xの根
     */
    public int find(int x) {
      if (x == parent[x]) {
        return x;
      } else {
        // 経路圧縮時はrank変更しない
        parent[x] = find(parent[x]);
        return parent[x];
      }
    }

    /**
     * ２つの要素が同じ集合に属するかどうかを返す。
     *
     * @param x
     * @param y
     * @return 同じ集合であればtrue
     */
    public boolean same(int x, int y) {
      return find(x) == find(y);
    }

    /**
     * 要素xが属する集合と要素yが属する集合を連結する。 木の高さ（ランク）を気にして、低い方に高い方をつなげる。（高い方の根を全体の根とする。）
     *
     * @param x
     * @param y
     */
    public void unite(int x, int y) {
      int xRoot = find(x);
      int yRoot = find(y);

      if (xRoot == yRoot) {
        // 属する集合が同じな場合、何もしない
        return;
      }

      // rankを比較して共通の根を決定する。
      // ※find時の経路圧縮はrank考慮しない
      if (rank[xRoot] > rank[yRoot]) {
        // xRootのrankのほうが大きければ、共通の根をxRootにする
        parent[yRoot] = xRoot;
      } else if (rank[xRoot] < rank[yRoot]) {
        // yRootのrankのほうが大きければ、共通の根をyRootにする
        parent[xRoot] = yRoot;
      } else {
        // rankが同じであれば、どちらかを根として、rankを一つ上げる。
        parent[xRoot] = yRoot;
        rank[xRoot]++;
      }
    }
  }

  static final class Utils {

    private static class Shuffler {

      private static void shuffle(int[] x) {
        final Random r = new Random();

        for (int i = 0; i <= x.length - 2; i++) {
          final int j = i + r.nextInt(x.length - i);
          swap(x, i, j);
        }
      }

      private static void shuffle(long[] x) {
        final Random r = new Random();

        for (int i = 0; i <= x.length - 2; i++) {
          final int j = i + r.nextInt(x.length - i);
          swap(x, i, j);
        }
      }

      private static void swap(int[] x, int i, int j) {
        final int t = x[i];
        x[i] = x[j];
        x[j] = t;
      }

      private static void swap(long[] x, int i, int j) {
        final long t = x[i];
        x[i] = x[j];
        x[j] = t;
      }
    }

    public static void shuffleSort(int[] arr) {
      Shuffler.shuffle(arr);
      Arrays.sort(arr);
    }

    public static void shuffleSort(long[] arr) {
      Shuffler.shuffle(arr);
      Arrays.sort(arr);
    }

    private Utils() {}
  }

  static class FastReader {

    private static final int BUFFER_SIZE = 1 << 16;
    private final DataInputStream din;
    private final byte[] buffer;
    private int bufferPointer, bytesRead;

    FastReader() {
      din = new DataInputStream(System.in);
      buffer = new byte[BUFFER_SIZE];
      bufferPointer = bytesRead = 0;
    }

    FastReader(String file_name) throws IOException {
      din = new DataInputStream(new FileInputStream(file_name));
      buffer = new byte[BUFFER_SIZE];
      bufferPointer = bytesRead = 0;
    }

    public String readLine() throws IOException {
      final byte[] buf = new byte[1024]; // line length
      int cnt = 0, c;
      while ((c = read()) != -1) {
        if (c == '\n') {
          break;
        }
        buf[cnt++] = (byte) c;
      }
      return new String(buf, 0, cnt);
    }

    public int readSign() throws IOException {
      byte c = read();
      while ('+' != c && '-' != c) {
        c = read();
      }
      return '+' == c ? 0 : 1;
    }

    private static boolean isSpaceChar(int c) {
      return !(c >= 33 && c <= 126);
    }

    private int skip() throws IOException {
      int b;
      // noinspection StatementWithEmptyBody
      while ((b = read()) != -1 && isSpaceChar(b)) {
      }
      return b;
    }

    public char nc() throws IOException {
      return (char) skip();
    }

    public String next() throws IOException {
      int b = skip();
      final StringBuilder sb = new StringBuilder();
      while (!isSpaceChar(b)) { // when nextLine, (isSpaceChar(b) && b != ' ')
        sb.appendCodePoint(b);
        b = read();
      }
      return sb.toString();
    }

    public int nextInt() throws IOException {
      int ret = 0;
      byte c = read();
      while (c <= ' ') {
        c = read();
      }
      final boolean neg = c == '-';
      if (neg) {
        c = read();
      }
      do {
        ret = ret * 10 + c - '0';
      } while ((c = read()) >= '0' && c <= '9');

      if (neg) {
        return -ret;
      }
      return ret;
    }

    public int[] nextIntArray(int n) throws IOException {
      final int[] res = new int[n];
      for (int i = 0; i < n; i++) {
        res[i] = nextInt();
      }
      return res;
    }

    public long nextLong() throws IOException {
      long ret = 0;
      byte c = read();
      while (c <= ' ') {
        c = read();
      }
      final boolean neg = c == '-';
      if (neg) {
        c = read();
      }
      do {
        ret = ret * 10 + c - '0';
      } while ((c = read()) >= '0' && c <= '9');
      if (neg) {
        return -ret;
      }
      return ret;
    }

    public long[] nextLongArray(int n) throws IOException {
      final long[] res = new long[n];
      for (int i = 0; i < n; i++) {
        res[i] = nextLong();
      }
      return res;
    }

    public double nextDouble() throws IOException {
      double ret = 0, div = 1;
      byte c = read();
      while (c <= ' ') {
        c = read();
      }
      final boolean neg = c == '-';
      if (neg) {
        c = read();
      }

      do {
        ret = ret * 10 + c - '0';
      } while ((c = read()) >= '0' && c <= '9');

      if (c == '.') {
        while ((c = read()) >= '0' && c <= '9') {
          ret += (c - '0') / (div *= 10);
        }
      }

      if (neg) {
        return -ret;
      }
      return ret;
    }

    private void fillBuffer() throws IOException {
      bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
      if (bytesRead == -1) {
        buffer[0] = -1;
      }
    }

    private byte read() throws IOException {
      if (bufferPointer == bytesRead) {
        fillBuffer();
      }
      return buffer[bufferPointer++];
    }

    public void close() throws IOException {
      din.close();
    }
  }
}
