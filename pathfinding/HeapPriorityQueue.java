
class HeapOverflowException extends RuntimeException
{
public HeapOverflowException()
{
super("Heap Overflow");
}

public HeapOverflowException(String msg)
{
super(msg);
}
}
class HeapUnderflowException extends RuntimeException
{
public HeapUnderflowException()
{
super("Heap Underflow");
}

public HeapUnderflowException(String msg)
{
super(msg);
}
}
interface HeapPQ<T>
{
	public void initializequeue();

	public double count();

	public boolean isEmpty();

	public boolean isFull();

	public Point front() throws HeapUnderflowException;

	public Point back() throws HeapUnderflowException;

	public void enqueue(Point queueElement) throws HeapOverflowException;

	public Point dequeue() throws HeapUnderflowException;
	
	public String preorder();

	public String inorder();

	public String postorder();



	public String toString();
}
	
class HeapPriorityQueue<T> implements HeapPQ<T>
{
private int maxQueueSize, count, queueFront, queueRear;
private double priorityIndex; // the dequeue method removes the Point with the smallest priority index
private Point[] list;

public HeapPriorityQueue()
{
maxQueueSize=5000;
queueFront = 0;
queueRear = maxQueueSize-1;
count = 0;
list =  new Point[maxQueueSize];
}
public HeapPriorityQueue(Point t){
	maxQueueSize=5000;
	queueFront = 0;
	queueRear = maxQueueSize-1;
	count = 1;
	list[0] =  t;
}
public HeapPriorityQueue(int queueSize)
{
if(queueSize<=0)
{
System.err.println("The size of the array to implement the queue must be positive.");
System.err.println("Creating an array of the size 500.");
maxQueueSize = 500;
}else{
maxQueueSize=queueSize;
}

queueFront=0;
queueRear=maxQueueSize-1;
count=0;
list = new Point[maxQueueSize];
}

public void initializequeue()
{
for(int i=queueFront; i<queueRear; i=(i+1)% maxQueueSize)
list[i]=null;

queueFront=0;
queueRear=maxQueueSize-1;
count=0;
}

public double count()
{
return count ;
}

public boolean isEmpty()
{
return (count == 0);
}

public boolean isFull()
{
return(count==maxQueueSize);
}


public void enqueue(Point queueElement) throws HeapOverflowException, NullPointerException
{
if(isFull()){
throw new HeapOverflowException();
}
queueRear=(queueRear+1)%maxQueueSize;
count++;
list[queueRear]= queueElement;

reheapifyUpward(queueRear);
}

private void reheapifyUpward(int index) throws NullPointerException
{
	//(Integer)list[index] > (Integer)list[(index-1)/2]

if(list[index].compareTo(list[(index-1)/2]) < 0)
{
Point temp=list[(index-1)/2];
list[(index-1)/2]=list[index];
list[index]=temp;
reheapifyUpward((index-1)/2);
}
}

public void Remove(Point a){
	int i = 0;
	while (i < count){
		if (a.equals(list[i])){
			list[i] = list[i+1];
			break;
		}
		i++;
	}

	count--;
	while (i < count){
		list[i] = list[i+1];
		i++;
	}

	queueRear = (queueRear-1) % maxQueueSize;

	list[count] = null;
}

/*public Point getPoint(Point a) throws NullPointerException{
	int i = 0;
	while (i < count){
		if (a.equals(list[i])) return list[i];
		i++;
	}
	return a;
}*/

public Point getPoint(Point a){
	Point temp = a;
	int i = 0;
	while (i < count){
		if (a.equals(list[i])){
			temp = list[i];
			list[i] = list[i+1];
			break;
		}
		i++;
	}

	count--;
	while (i < count){
		list[i] = list[i+1];
		i++;
	}

	queueRear = (queueRear-1) % maxQueueSize;

	list[count] = null;

	return temp;
}


public Point dequeue() throws HeapUnderflowException, NullPointerException
{
	if(isEmpty()){
	throw new HeapUnderflowException();
	}
count--;

Point temp = list[queueFront]; 
list[queueFront] = list[queueRear];
list[queueRear] = null;

queueRear = (queueRear-1) % maxQueueSize;
reheapifyDownward(queueFront);
return temp;
}


private void reheapifyDownward(int index) throws NullPointerException
{
	
if(list[2 * index+1] != null && list[2*index+2] != null)
{
Point temp1=list[2*index+1];
Point temp2=list[2*index+2];

//(Integer) temp1 > (Integer) temp2
if( temp1.compareTo(temp2) < 0)
{
//	System.out.println(temp1.finalCost + " " + temp2.finalCost);
list[2*index+1]=list[index];
list[index]=temp1;
reheapifyDownward(2*index+1);
}
else
{
list[2*index+2]=list[index];
list[index]=temp2;
reheapifyDownward(2*index+2);
	}
}

else if(list[2*index+1]!=null && list[2*index+2]==null)
{
Point temp=list[2*index+1];
//(Integer) temp > (Integer) list[index]
if( temp.compareTo(list[index]) < 0)
{
list[2*index+1]=list[index];
list[index]=temp;
	}
}

else if(list[2*index+1]==null && list[2*index+2]!=null)
{
Point temp=list[2*index+2];
//(Integer) temp > (Integer) list[index]
if( temp.compareTo(list[index]) < 0)
{
list[2*index+2]=list[index];
list[index]=temp;
	}
}

}

public boolean contains(Point t) {
	for (int i = 0; i < count; i++){
		if (t.equals(list[i])){
			return true;
		}
	}
	return false;
}

public String preorder()
{
String s="Preorder traversal is: " + doPreorder(0) + "\n";
return s;
}

private String doPreorder(int index)
{
String s= "";

if(list[index]!=null)
{
s += list[index] + " ";
s += doPreorder(2*index+1);
s += doPreorder(2*index+2);
}
return s;
}

public String inorder()
{
String s= "Inorder traversal is: " + doInorder(0) + "\n";
return s;
}

private String doInorder(int index)
{
String s = "";

if(list[index] != null)
{
s += doInorder(2*index+1);
s += list[index] + " ";
s += doInorder(2*index+2);
}
return s;
}

public String postorder()
{
String s = "Postorder traversal is: " + doPostorder(0) + "\n";
return s;
}

private String doPostorder(int index)
{
String s="";

if(list[index]!=null)
{
s += doPostorder(2*index+1);
s += doPostorder(2*index+2);
s += list[index] + " ";
}
return s;
}

public String toString()
{
String s="";
s += preorder()+inorder()+postorder();
return s;
	}
@Override
public Point front() throws HeapUnderflowException {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Point back() throws HeapUnderflowException {
	// TODO Auto-generated method stub
	return null;
}
}
