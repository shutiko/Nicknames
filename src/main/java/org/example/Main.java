package org.example;


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger count3 = new AtomicInteger(0);
    public static AtomicInteger count4 = new AtomicInteger(0);
    public static AtomicInteger count5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int countStrings = 100_000;
        String[] texts = new String[countStrings];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        // Код 3 потоков
        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !isSameChar(text)) {
                    incrementCounter(text.length());
                }
            }
        }
        );

        Thread sameChar = new Thread(() -> {
            for (String text : texts) {
                if (isSameChar(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread ascendingOrder = new Thread(() -> {
            for (String text : texts) {
                if (!isSameChar(text) && isLexicographic(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        // Запускаем потоки
        palindrome.start();
        sameChar.start();
        ascendingOrder.start();

        //Ждем пока потоки завершат работу
        try {
            palindrome.join();
            sameChar.join();
            ascendingOrder.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Выводим результат по задаче
        System.out.println("Красивых слов с длиной 3: " + count3 + " шт.");
        System.out.println("Красивых слов с длиной 4: " + count4 + " шт.");
        System.out.println("Красивых слов с длиной 5: " + count5 + " шт.");

    }

    // Функция - генерируем слова и пишем их в массив
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    //Функция - выбираем палиндромы и слова в составе которых одна буква, что тоже палиндромы
    public static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    //Функция - выбираем слова последовательность букв которых расположена лексикографически
    public static boolean isLexicographic(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1))
                return false;
        }
        return true;
    }

    //Функция - выбираем слова состоящие из одной буква
    public static boolean isSameChar(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    //Функция - увеличения счетчиков
    public static void incrementCounter(int textLength) {
        if (textLength == 3) {
            count3.getAndIncrement();
        } else if (textLength == 4) {
            count4.getAndIncrement();
        } else {
            count5.getAndIncrement();
        }
    }
}