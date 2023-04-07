package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger count3 = new AtomicInteger(0);
    public static AtomicInteger count4 = new AtomicInteger(0);
    public static AtomicInteger count5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int countStrings = 10_000;
        String[] texts = new String[countStrings];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        // Код 3 потоков
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == 3) {
                    if (isLexicographic(texts[i])) {
                        count3.getAndIncrement();
                    } else if (isPalindrom(texts[i])) {
                        count3.getAndIncrement();
                    }
                }
            }
        }
        );

        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == 4) {
                    if (isLexicographic(texts[i])) {
                        count4.getAndIncrement();
                    } else if (isPalindrom(texts[i])) {
                        count4.getAndIncrement();
                    }
                }
            }
        }
        );

        Thread thread5 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == 5) {
                    if (isLexicographic(texts[i])) {
                        count5.getAndIncrement();
                    } else if (isPalindrom(texts[i])) {
                        count5.getAndIncrement();
                    }
                }
            }
        }
        );

        // Запускаем потоки
        thread3.start();
        thread4.start();
        thread5.start();

        //Ждем пока потоки завершат работу
        try {
            thread3.join();
            thread4.join();
            thread5.join();
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
    public static boolean isPalindrom(String text) {
        boolean isPalindrom;
        StringBuilder reverse = new StringBuilder();
        String clean = text.replaceAll("\\s+", "").toLowerCase();
        char[] charArray = clean.toCharArray();
        for (int i = charArray.length - 1; i >= 0; i--) {
            reverse.append(charArray[i]);
        }
        isPalindrom = (reverse.toString()).equals(clean);
        return isPalindrom;
    }

    //Функция - выбираем слова последовательность букв которых расположена лексикографически
    public static boolean isLexicographic(String text) {
        String clean = text.replaceAll("\\s+", "").toLowerCase();
        if (clean.charAt(0) == clean.charAt(clean.length() - 1)) {
            return false;
        } else {
            for (int i = 0; i < clean.length() - 1; i++) {
                if (clean.charAt(i) > clean.charAt(i + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

}