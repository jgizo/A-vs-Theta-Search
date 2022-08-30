public class Point implements Comparable<Point>{
    int x, y;
    float g, h;
    Point parent;

    Point(int x, int y){
       this.x = x;
       this.y = y;
       g = (float) Double.POSITIVE_INFINITY;
       h = (float) Double.POSITIVE_INFINITY;
       parent = null;
    }

    @Override
    public boolean equals (Object a){
        if (a instanceof Point){
            Point b = (Point) a;
            if (this.x == b.x && this.y == b.y) return true;
            else return false;
        }
        else return false;
    }

    @Override
    public String toString(){
        return this.x + " " + this.y;
    }

    @Override
	public int compareTo (Point x) {
        float a = this.g + this.h;
        float b = x.g + x.h;
		if (a > b){
			return 1;
		}else if (a < b){
			return -1;
		} else {
		return 0;
		}
	}
}