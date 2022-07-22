import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        List<Integer> list = new KmList<>();
        List<Integer> emptyList = new KmList<>();;
        List<Integer> removeList = new LinkedList<>();
        List<Integer> containsList = new KmList<>();
        List<Integer> retainList = new KmList<>();
        List<Integer> addAll = new KmList<>();

        retainList.add(1);
        retainList.add(2);
        retainList.add(3);

        addAll.add(999);
        addAll.add(null);
        addAll.add(1);
        addAll.add(2);
        addAll.add(3);
        addAll.add(4);
        addAll.add(5);
        addAll.add(777);

        list.addAll(addAll);

        containsList.add(null);
        containsList.add(1);
        containsList.add(2);
        containsList.add(3);
        containsList.add(2);


//        assert list.retainAll(retainList);
//        assert list.get(3) == 2;
//        list.clear();
//        list.addAll(addAll);

        List<Integer> sub = list.subList(0,6);
        sub.retainAll(retainList);
//        sub.add(2);
//        sub.addAll(addAll);
        for (int i = 0; i < sub.size(); i ++) {
            System.out.println(list.get(i));
            System.out.println(sub.get(i));
        }
        assert sub.lastIndexOf(null) == 7;
        assert sub.lastIndexOf(1) == 1;
        assert sub.lastIndexOf(5) == 5;
        assert sub.lastIndexOf(2) == 4;
        assert sub.containsAll(containsList);
//        sub.retainAll(retainList);

        assert list.retainAll(emptyList);
        list.clear();
        assert list.containsAll(emptyList);
        assert !list.retainAll(emptyList);
    }
}
