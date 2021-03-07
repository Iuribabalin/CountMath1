import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        FileReader fr;
        WorkMatrix workMatrix = null;
        int i = 0;

        try {
            System.out.println("Начало работы программы");

            while (true) {
                System.out.print("Ввести данные через файл?(y/n)");
                String answer = in.nextLine().toLowerCase().trim();

                if (answer.equals("y") || answer.equals("")) {
                    System.out.print("Введите имя файла (файл должен быть формата .txt): ");
                    String fileName = in.nextLine().trim();
                    try {
                        fr = new FileReader("src/main/resources/"+fileName+".txt");
                        Scanner scan = new Scanner(fr);
                        int iteratorNext = 1;
                        int n = 0;
                        double e = 0;
                        while (scan.hasNextLine()) {
                            if(iteratorNext == 1){
                                try {
                                    String str = scan.nextLine().trim().replaceAll(",", "\\.");
                                    e = Double.parseDouble(str);

                                    iteratorNext++;
                                }catch (NumberFormatException ex){
                                    System.out.println("Ошибка в данных файла при получении точности");
                                    break;
                                }
                            }else if(iteratorNext == 2) {
                                try {
                                    n = Integer.parseInt(scan.nextLine().trim());
                                    if(n <= 0){
                                        System.out.println("Матрица не может иметь размерность 0 и меньше");
                                        break;
                                    }
                                    iteratorNext++;
                                    workMatrix = new WorkMatrix(n,e);
                                }catch (NumberFormatException ex){
                                    System.out.println("Ошибка в данных файла при получении размерности матрицы");
                                    break;
                                }
                            }else{
                                String input = scan.nextLine().trim().toLowerCase();
                                if (input.split(" ").length == n+1) {
                                    for (int j = 0; j < n + 1; j++) {
                                        try {
                                            workMatrix.matrix[i][j] = Float.parseFloat(input.split(" ")[j]);
                                        } catch (NumberFormatException ex) {
                                            System.out.println("Ошибка в данных файла (неверный формат данных внутри строки)");
                                            break;
                                        }
                                    }
                                    i++;
                                } else {
                                    System.out.println("Ошибка в данных файла (строка матрицы не верной длинны)");
                                    break;
                                }
                            }
                        }

                        if (workMatrix != null) {
                            workMatrix.startIterativeMethod();
                        }

                        break;
                    } catch (FileNotFoundException e) {
                        System.out.println("Ошибка в имени файла, его не существует");
                    }
                } else if (answer.equals("n")) {
                    System.out.println("Введите матрицу вручную");
                    System.out.print("Введите точность вычислений: ");
                    double e = Double.parseDouble(in.nextLine().trim().replaceAll(",", "\\."));
                    System.out.print("\nВведите размерность матрицы числом: ");
                    int n = Integer.parseInt(in.nextLine());
                    workMatrix = new WorkMatrix(n,e);

                    while (i < n) {
                        String input = in.nextLine().trim().toLowerCase();
                        if (input.split(" ").length == n + 1) {
                            for (int j = 0; j < n + 1; j++) {
                                try {
                                    workMatrix.matrix[i][j] = Float.parseFloat(input.split(" ")[j]);
                                } catch (NumberFormatException ex) {
                                    System.out.println("Проблемы с введённой строкой, присутствуют лишние символы\n" +
                                            "Введите строку состоящию только из цифр");
                                    i--;
                                    break;
                                }
                            }
                            i++;
                        } else {
                            System.out.println("Строка матрицы введена некорректно");
                        }
                    }

                    workMatrix.startIterativeMethod();

                    break;
                } else {
                    System.out.println("Вы ввели некорректный символ продолжения");
                }
            }
        }catch (NoSuchElementException ex){
            System.out.println("Программа завершилась экстренным способом, через ctrl+d ");
        }

    }
}
