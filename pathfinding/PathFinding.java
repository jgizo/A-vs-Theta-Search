import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PathFinding{

    static HeapPriorityQueue<Point> Fringe;
    static ArrayList<Point> Explored;
    static int[][] grid;
    static int row;
    static int col;
    static Point start, end;
    static String path = null;
    static final float d = (float) Math.sqrt(2);

    public static boolean FindPath(Point start, Point end, boolean a) throws NullPointerException{
        start.g = 0;
        start.parent = start;
        if (a) start.h = AStarH(start, end);
        else start.h = ThetaH(start, end);
        Fringe = new HeapPriorityQueue<Point>(); // update heap functions
        Fringe.initializequeue();
        Fringe.enqueue(start);
        Explored = new ArrayList<Point>();

        while (Fringe.isEmpty() != true){
            Point temp = Fringe.dequeue(); // update heap functions
            Explored.add(temp);
            if (temp.equals(end)) return true; //implement an equals method that compare points based on their coordinates
            for (int i = 0; i < 8; i++){ //search the 8 neighbor that a point has 
                switch (i){
                    case 0: 
                        if (temp.y == 1 || temp.x == 1) break; // for the top most row and left most column, neighbor 1 (top-left) doesn't exist
                        else if (grid[temp.y - 2 ][temp.x - 2] == 1) break; // it's not a neighbor if top-left grid is block
                        else{ //it is a legit neighbor
                            Point tl = new Point(temp.x-1, temp.y-1);
                            if (Explored.contains(tl)) break;
                            if (Fringe.contains(tl)) {
                                tl = Fringe.getPoint(tl);
                            }
                            if (a == true) UpdateA(temp, tl);
                            else UpdateT(temp, tl);
                            break;
                        }
                    case 1:
                        if (temp.y == 1) break;
                        else if ((temp.x == 1 || grid[temp.y-2][temp.x-2] == 1) && (temp.x == col || grid[temp.y-2][temp.x-1] == 1)) break;
                        else{
                            Point tm = new Point(temp.x, temp.y-1);
                            if (Explored.contains(tm)) break;
                            if (Fringe.contains(tm)){
                                tm = Fringe.getPoint(tm);
                            } 
                            if (a == true) UpdateA(temp, tm);
                            else UpdateT(temp, tm);
                            break;
                        }
                    case 2:
                        if (temp.y == 1 || temp.x == col) break;
                        else if (grid[temp.y-2][temp.x-1] == 1) break;
                        else{
                            Point tr = new Point(temp.x+1, temp.y-1);
                            if (Explored.contains(tr)) break;
                            if (Fringe.contains(tr)) {
                                tr = Fringe.getPoint(tr);
                            }
                            if (a == true) UpdateA(temp, tr);
                            else UpdateT(temp, tr);
                            break;
                        }
                    case 3:
                        if (temp.x == 1) break;
                        else if ((temp.y == 1 || grid[temp.y-2][temp.x-2] == 1) && (temp.y == row || grid[temp.y-1][temp.x-2] == 1)) break;
                        else{
                            Point l = new Point(temp.x-1, temp.y);
                            if (Explored.contains(l)) break;
                            if (Fringe.contains(l)) {
                                l = Fringe.getPoint(l);
                            }
                            if (a == true) UpdateA(temp, l);
                            else UpdateT(temp, l);
                            break;
                        }
                    case 4:
                        if (temp.x == col) break;
                        else if ((temp.y == 1 || grid[temp.y-2][temp.x-1] == 1) && (temp.y == row || grid[temp.y-1][temp.x-1] == 1)) break;
                        else{
                            Point r = new Point(temp.x+1, temp.y);
                            if (Explored.contains(r)) break;
                            if (Fringe.contains(r)) {
                                r = Fringe.getPoint(r);
                            }
                            if (a == true) UpdateA(temp, r);
                            else UpdateT(temp, r);
                            break;
                        }
                    case 5:
                        if (temp.y == col || temp.x == 1) break; // for the bottom most row and left most column, neighbor 1 (top-left) doesn't exist
                        else if (grid[temp.y - 1][temp.x - 2] == 1) break; // it's not a neighbor if bottom-left grid is block
                        else{ 
                            Point bl = new Point(temp.x-1, temp.y+1);
                            if (Explored.contains(bl)) break;
                            if (Fringe.contains(bl)){
                                bl = Fringe.getPoint(bl);
                            }
                            if (a == true) UpdateA(temp, bl);
                            else UpdateT(temp, bl);
                            break;
                        }
                    case 6:
                        if (temp.y == col) break;
                        else if ((temp.x == 1 || grid[temp.y-1][temp.x-2] == 1) && (temp.x == col || grid[temp.y-1][temp.x-1] == 1)) break;
                        else{
                            Point bm = new Point(temp.x, temp.y+1);
                            if (Explored.contains(bm)) break;
                            if (Fringe.contains(bm)) {
                                bm = Fringe.getPoint(bm);
                            }
                            if (a == true) UpdateA(temp, bm);
                            else UpdateT(temp, bm);
                            break;
                        }
                    case 7:
                        if (temp.y == 1 || temp.x == col) break;
                        else if (grid[temp.y-1][temp.x-1] == 1) break;
                        else{
                            Point br = new Point(temp.x+1, temp.y+1);
                            if (Explored.contains(br)) break;
                            if (Fringe.contains(br)) {
                                br = Fringe.getPoint(br);
                            }
                            if (a == true) UpdateA(temp, br);
                            else UpdateT(temp, br);
                            break;
                        }
                }
            }

        }
        return false;
    }

    public static void UpdateA (Point p, Point c) throws NullPointerException{
        float tc = p.g + stepC(p, c);
        if (tc < c.g){
            c.g = tc;
            c.h = AStarH(c, end);
            c.parent = p;
            if (!Fringe.contains(c)) Fringe.enqueue(c); 
        }
    }
 
    public static void UpdateT (Point p, Point c) throws NullPointerException{
        if (lineOfSight(p.parent, c)){
            Point pp = p.parent;
            float tc = pp.g + ThetaH(pp, c);
            if (tc < c.g){
                c.g = tc;
                c.h = ThetaH(c, end);
                c.parent = pp;
                if (!Fringe.contains(c)) Fringe.enqueue(c);
            }
        }
        else{
            float tc = p.g + stepC(p, c);
            if (tc < c.g) {
                c.g = tc;
                c.h = ThetaH(c, end);
                c.parent = p;
                if (!Fringe.contains(c)) Fringe.enqueue(c);
            }
        }
    }

    public static boolean lineOfSight (Point p, Point c) throws NullPointerException{
        int x0 = p.x-1;
        int y0 = p.y-1;
        int x1 = c.x-1;
        int y1 = c.y-1;
        int f = 0;
        int dy = y1 - y0;
        int dx = x1 - x0;
        int sy, sx;

        if (dy < 0){
            dy = (-1)*dy;
            sy = -1;    
        }
        else sy = 1;

        if (dx < 0){
            dx = (-1)*dx;
            sx = -1;
        }
        else sx = 1;

        if (dx >= dy) {
            while (x0 != x1){
                f += dy;
                if (f >= dx) {
                    if (grid[y0+((sy-1)/2)][x0+((sx-1)/2)] == 1) return false;
                    y0 += sy;
                    f -= dx;
                }
                if ((f != 0) && (grid[y0+((sy-1)/2)][x0+((sx-1)/2)] == 1)) return false;
                if ((dy == 0) && (grid[y0][x0+((sx-1)/2)] == 1) && (grid[y0-1][x0+((sx-1)/2)] == 1)) return false;
                x0 += sx;
            }
        }
        else{
            while (y0 != y1) {
                f += dx;
                if (f >= dy){
                    if (grid[y0+((sy-1)/2)][x0+((sx-1)/2)] == 1) return false;
                    x0 += sx;
                    f -= dy;
                }
                if ((f != 0) && (grid[y0+((sy-1)/2)][x0+((sx-1)/2)] == 1)) return false;
                if ((dx == 0) && (grid[y0+((sy-1)/2)][x0] == 1) && (grid[y0+((sy-1)/2)][x0-1] == 1)) return false;
                y0 += sy;
            }

        }
        return true;
    }

    public static float stepC(Point x, Point end){
        if ((x.x != end.x) && (x.y != end.y)) return d;
        return 1;
    }

    public static float ThetaH(Point x, Point end) throws NullPointerException{
        return (float) Math.sqrt( Math.pow((x.x - end.x), 2) + Math.pow(x.y - end.y, 2));
    }

    public static float AStarH (Point x, Point end) throws NullPointerException{
        return (float) Math.sqrt(2) * Math.min( Math.abs(x.x - end.x), Math.abs(x.y - end.y) ) + Math.max( Math.abs(x.x-end.x), Math.abs(x.y-end.y) ) - Math.min( Math.abs(x.x-end.x), Math.abs(x.y-end.y));
    }

    public static void clear(){
        start.g = (float) Double.POSITIVE_INFINITY;
        start.h = (float) Double.POSITIVE_INFINITY;
        start.parent = null;

        end.g = (float) Double.POSITIVE_INFINITY;
        end.h = (float) Double.POSITIVE_INFINITY;
        end.parent = null;
    }

    public static void toFile() throws IOException, NullPointerException{
        File file = new File(path + "\\solution.txt");
        file.createNewFile();
        PrintWriter output = new PrintWriter(file);
        output.write(start.x + " " + start.y + "\n");
		output.write(end.x + " " + end.y + "\n");
		output.write((col-1) + " " + (row-1) + "\n");
        for (int i = 0; i < col-1; i++){
            for (int j = 0; j < row-1; j++){
                output.write((i+1) + " " + (j+1) + " " + grid[j][i] + "\n");
            }
        }

        output.write("=\n");

        long startTime = System.currentTimeMillis();
        if (!FindPath(start, end, true)) System.out.printf("No Path Found by A* \n");
        long ARuntime = System.currentTimeMillis() - startTime;

        System.out.printf("A* runtime is %d\n", ARuntime);

        float Alength = 0;

        if (Explored.contains(end)){
            Point pt = null;
            for (Point x : Explored){
                if (x.equals(end)){
                    pt = x;
                    break;
                }
            }
            boolean check = false;
            while (pt != null){
                output.write(pt + "\n");
                Alength += ThetaH(pt, pt.parent);
                pt = pt.parent; 
                if (check) break;
                else if (pt.equals(start)) check = true;
            }
        }

        System.out.printf("A* path is of length %f\n", Alength);

        output.write("&\n");

        for (Point x : Explored){
            output.write(x + " " + x.g + " " + x.h + "\n");
        }

        output.write("+\n");

        clear();

        startTime = System.currentTimeMillis();
        if (!FindPath(start, end, false)) System.out.printf("No Path Found by Theta* \n");
        long TRuntime = System.currentTimeMillis() - startTime;

        System.out.printf("Theta* runtime is %d\n", TRuntime);

        float Tlength = 0;

        if (Explored.contains(end)){
            Point pt = null;
            for (Point x : Explored){
                if (x.equals(end)){
                    pt = x;
                    break;
                }
            }
            boolean check = false;
            while (pt != null){
                output.write(pt + "\n");
                Tlength += ThetaH(pt, pt.parent);
                pt = pt.parent; 
                if (check) break;
                else if (pt.equals(start)) check = true;
            }
        }

        System.out.printf("Theta* path is of length %f\n", Tlength);

        output.write("$\n");

        for (Point x : Explored){
            output.write(x + " " + x.g + " " + x.h + "\n");
        }

        output.close();

    }

    public static void main(String[] args) throws IOException, NullPointerException {
        File file;
    	int lineNo = 1;
		int sI =0, sJ = 0, gI = 0, gJ = 0;
		int start_x= 0, start_y=0, end_x=0, end_y=0;

		if (args.length == 0){
		    System.err.println("Enter a file output path as the first command line argument");
		    return;
		}
		else if(args.length == 1){
			start_x = (int) (Math.random() * 100);
	        start_y = (int) (Math.random() * 50);
	        
	        end_x = (int) (Math.random() * 100);
	        end_y = (int) (Math.random()* 50);

            row = 51;
	        col = 101;
	        grid = new int[row][col];

			for (int j = 0; j < row; j++){
	            for (int i = 0; i < col; i++){
	                if (j == row-1) grid[j][i] = 1;
	                else if (i == col-1) grid[j][i] = 1;
	                else {
	                    int rand = (int) (Math.random() * 10 + 1);
	                    if (rand == 1) grid[j][i] = 1;
	                    else grid[j][i] = 0;
	                }
	            }
	        }
	        end = new Point(end_x+1, end_y+1);
            end.h = 0;
	        start = new Point(start_x+1, start_y+1);
		} else{
			
		    file = new File(args[1]);
    	    Scanner scan = new Scanner(file);
		    while(scan.hasNext()){
			    int i = 0, j =0 ,k =0;
			    if (lineNo == 1){
				    i = scan.nextInt();
				    j = scan.nextInt();
				    sI = i;
				    sJ = j;
				    start_x = sI;
				    start_y = sJ;
				    lineNo++;
			    }else if (lineNo == 2){
				    i = scan.nextInt();
				    j = scan.nextInt();
				    gI = i;
				    gJ = j;
				    end_x = gI;
				    end_y = gJ;
				    lineNo++;
			    }else if (lineNo == 3){
				    i = scan.nextInt();
				    j = scan.nextInt();
				
				    col = i+1;
		            row = j+1;
				    grid = new int[row][col];
		        
		        
		            for (int n = 0; n < row; n++){
		                for (int m = 0; m < col; m++){
		                    if (n == row-1) grid[n][m] = 1;
		                    else if (m == col-1) grid[n][m] = 1;
		                    else {                   
		                        grid[n][m] = 0;
		                    }
		                }
		            }
		        
		            lineNo++;
			    } else{
				    i = scan.nextInt();
				    j = scan.nextInt();
				    k = scan.nextInt();
				    if (k == 1){
				        grid[j-1][i-1] = 1;
				    }
				    lineNo++;
			    }
			
		    }
	
		    scan.close();
            end = new Point(end_x, end_y);
            end.h = 0;
            start = new Point(start_x, start_y);
		}

        path = args[0];
        toFile();
    }

}