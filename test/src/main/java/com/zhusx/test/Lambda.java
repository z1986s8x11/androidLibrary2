package com.zhusx.test;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/8/18 14:05
 */

public class Lambda {
    public static void main(String[] args) {
//        /*1 实现Runnable线程案例*/
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Before Java8 ");
//            }
//        });
//        new Thread(() -> System.out.println("In Java8!"));
//
//        /*2.实现事件处理*/
//        JButton show = new JButton("Show");
//        show.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("without lambda expression is boring");
//            }
//        });
//        show.addActionListener((e) -> {
//            System.out.println("Action !! Lambda expressions Rocks");
//        });
//        show.addActionListener((e) -> System.out.println("Action !! Lambda expressions Rocks")
//        );
//
//        /*3.使用Lambda表达式遍历List集合*/
//        List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
//        for (String f : features) {
//            System.out.println(f);
//        }
//        features.forEach(n -> System.out.println(n));
//        features.forEach(System.out::println);
//
//        /*4.使用Lambda表达式和函数接口*/
//        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
//        Predicate<String> condition = (str) -> str.length() > 4;
//        Predicate<String> condition1 = (str) -> false;
//        Predicate<String> condition2 = (str) -> str.endsWith("a");
//        languages.forEach((name) -> {
//            if (condition.test(name)) {
//                System.out.println(name);
//            }
//        });
//        languages.stream().filter((name) -> (condition.test(name)))
//                .forEach((name) -> {
//                    System.out.println(name + " ");
//                });
//        languages.stream().filter((name) -> (condition.test(name))).forEach(System.out::println);
//        languages.stream().filter((str) -> str.length() > 4).forEach(System.out::println);
//
//        /*5 复杂的结合Predicate 使用*/
//        List<String> names = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
//        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
//        Predicate<String> fourLetterLong = (n) -> n.length() == 4;
//        names.stream()
//                .filter(startsWithJ.and(fourLetterLong))
//                .forEach((n) -> System.out.println("Name, which starts with'J' and four letter long is : " + n));
//
//        /*6.使用Lambda实现Map 和 Reduce*/
//        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
//        for (Integer cost : costBeforeTax) {
//            double price = cost + .12 * cost;
//            System.out.println(price);
//        }
//        costBeforeTax.stream().map((cost) -> cost + .12 * cost)
//                .forEach(System.out::println);
//
//        double total = 0;
//        for (Integer cost : costBeforeTax) {
//            double price = cost + .12 * cost;
//            total = total + price;
//        }
//        System.out.println("Total : " + total);
//        double bill = costBeforeTax.stream().map((cost) -> cost + .12 * cost)
//                .reduce((sum, cost) -> sum + cost)
//                .get();
//        System.out.println("Total : " + bill);
//
//        /*7 通过filtering 创建一个字符串String的集合*/
//        List<String> strList = Arrays.asList("Java", "Scala", "C++", "B","Haskell", "Lisp","A");
//        List<String> filtered = strList.stream().filter(x -> x.length() > 2).collect(Collectors.toList());
//        System.out.printf("Original List : %s, \nfiltered list : %s %n", strList, filtered);
//
//        /*8.对集合中每个元素应用函数*/
//        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.", "Canada");
//        String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
//        String G7Countries = G7.stream().map(String::toUpperCase).collect(Collectors.joining(", "));
//        System.out.println(G7Countries);
//        /*9 通过复制不同的值创建一个子列表*/
//        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
//        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct()
//                .collect(Collectors.toList());
//        System.out.printf("Original List : %s,  Square Without duplicates :%s \n", numbers, distinct);
//        /*10 计算List中的元素的最大值，最小值，总和及平均值*/
//        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
//        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
//        System.out.println("Highest prime number in List : " + stats.getMax());
//        System.out.println("Lowest prime number in List : " + stats.getMin());
//        System.out.println("Sum of all prime numbers : " + stats.getSum());
//        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }
}
