import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class JavaCollection {

  public static void main(String[] args) {
    hashMapExample();
    treeMapExample();
    treeSetExample();
    StackExample();
  }

public static void treeMapExample() {
    // 创建一个TreeMap
    TreeMap<Integer, Integer> treeMap = new TreeMap<>();

    // 向TreeMap中放入键值对
    treeMap.put(25, 25);
    treeMap.put(30, 30);
    treeMap.put(28, 28);
    treeMap.put(35, 35);
    treeMap.put(37, 37);
    treeMap.put(38, 38);
    treeMap.put(39, 39);
    treeMap.put(40, 38);
    //treeMap.put(41, 39);
    TreeMap<String,String> a =new TreeMap<>();
    // 输出TreeMap中的键值对
    for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
      System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
    }
}
public static void hashMapExample() {
    // 创建一个HashMap
    HashMap<String, Integer> hashMap = new HashMap<>();

    // 向HashMap中放入键值对
    hashMap.put("Alice", 25);
    hashMap.put("Bob", 30);
    hashMap.put("Charlie", 28);
    hashMap.put("David", 35);

    // 输出HashMap中的键值对
    System.out.println("HashMap Elements: " + hashMap);
}
public static void treeSetExample() {
    // 创建一个TreeSet
    TreeSet<Integer> treeSet = new TreeSet<>();

    // 向TreeSet中添加整数元素
    treeSet.add(5);
    treeSet.add(3);
    treeSet.add(8);
    treeSet.add(1);

    // 输出TreeSet中的整数元素（已排序）
    System.out.println("TreeSet Elements: " + treeSet);

    // 使用迭代器遍历TreeSet
    System.out.println("Iterating over TreeSet:");
    for (int number : treeSet) {
      System.out.println(number);
    }
}

  public static void StackExample() {
    // 创建一个空栈
    Stack<Integer> stack = new Stack<>();

    // 向栈中压入元素
    stack.push(1);
    stack.push(2);
    stack.push(3);

    // 输出栈顶元素
    System.out.println("Top element: " + stack.peek());

    // 弹出栈顶元素
    stack.pop();

    // 输出栈中元素个数
    System.out.println("Stack size: " + stack.size());

    // 判断栈是否为空
    System.out.println("Is stack empty? " + stack.isEmpty());
  }

  public static void ArrayListExample(){
    // 创建一个ArrayList
    ArrayList<String> arrayList = new ArrayList<>();

    // 向ArrayList中添加元素
    arrayList.add("Alice");
    arrayList.add("Bob");
    arrayList.add("Charlie");

    // 输出ArrayList中的元素
    System.out.println("ArrayList Elements: " + arrayList);

    // 使用迭代器遍历ArrayList
    System.out.println("Iterating over ArrayList:");
    for (String name : arrayList) {
      System.out.println(name);
    }
  }


}
