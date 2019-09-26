import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int MAIN_MENU = 0;
    private static final int CHOICE_MENU = -2;
    private static final int ALL_INFO_ABOUT_COMPANY = 1;
    private static final int ALL_INFO_ABOUT_SECURITIES = 2;
    private static final int ALL_INFO_ABOUT_DELAY_SECURITIES = 3;
    private static final int FILTER_COMPANY = 4;
    private static final int FILTER_SECURITIES = 5;
    private static final int EXIT_PROGRAM = -1;

    private static int count;
    private static List<Company> list;

    public static void main(String[] args) {
        int navigator = 0;

        while (navigator != -1) {                                                                                       //Запускаем цикл меню
            switch (navigator) {
                case MAIN_MENU:
                    navigator = mainMenu();
                    break;
                case CHOICE_MENU:
                    navigator = choiseMenu();
                    break;
                case ALL_INFO_ABOUT_COMPANY:
                    navigator = infoAboutCompany();
                    break;
                case ALL_INFO_ABOUT_SECURITIES:
                    navigator = infoAboutSecurities();
                    break;
                case ALL_INFO_ABOUT_DELAY_SECURITIES:
                    navigator = infoAboutDelaySecurities();
                    break;
                case FILTER_COMPANY:
                    navigator = filterCompany();
                    break;
                case FILTER_SECURITIES:
                    navigator = filterSecurities();
                    break;
            }
        }
    }

    public static int mainMenu() {                                                                                      //Класс для главного меню
        int answerInt = CHOICE_MENU;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите JSON файл для чтения");
        System.out.println("0 - Чтобы выйти");
        System.out.println();
        System.out.println("--------------------------------");
        System.out.println("Введите путь чтобы прочитать файл: ");
        System.out.println();
        String pathFile = scanner.nextLine();

        File file = new File(pathFile);
        if (pathFile.equals("0")) {                                                                                     //Если ввести "0" то мы выйдем из программы
            answerInt = EXIT_PROGRAM;
        } else if (file.exists() && file.isFile()) {                                                                    //Если файл существует и является файлом
            MyReaderJSON myReader = new MyReaderJSON(pathFile);                                                         //Создаю свой reader и даю ему файл
            list = myReader.getCompany();                                                                               //Читаю файл и заполняю коллекцию
            if (list.isEmpty()) {                                                                                         //Если лист пустой то считаем что json файл не рапарсен
                answerInt = MAIN_MENU;
            } else {
                System.out.println();
                System.out.println("Файл успешно загружен");
                System.out.println();
            }
        } else {                                                                                                        //Если файла не существует или он является папкой то выходим обратно в меню
            System.out.println();
            System.out.println("Такого файла не существует " + file);
            System.out.println("Введите другой файл");
            System.out.println();
            answerInt = MAIN_MENU;
        }
        return answerInt;
    }

    public static int choiseMenu() {                                                                                    //Следующее меню где выполняются действия над файлом
        int answerInt = CHOICE_MENU;
        Scanner scanner = new Scanner(System.in);
        System.out.println("--------------------------------");
        System.out.println("Выберите дальнейшее действие: ");
        System.out.println("1 - Вывести все компании");
        System.out.println("2 - Вывести все ценные бумаги");
        System.out.println("3 - Вывести все просроченые ценные бумаги");
        System.out.println("4 - Отфильтровать компании по дате");
        System.out.println("5 - Отфильтровать компании по коду валют");
        System.out.println("0 - Выйти");
        System.out.println();
        int index = CHOICE_MENU;
        try {
            index = scanner.nextInt();
        } catch (InputMismatchException ignored) {
            System.out.println("Нужно ввести цифру");
            System.out.println();
        }

        switch (index) {                                                                                                //Смотрим что ввел пользователь
            case 0:
                answerInt = EXIT_PROGRAM;
                break;
            case 1:
                answerInt = ALL_INFO_ABOUT_COMPANY;
                break;
            case 2:
                answerInt = ALL_INFO_ABOUT_SECURITIES;
                break;
            case 3:
                answerInt = ALL_INFO_ABOUT_DELAY_SECURITIES;
                break;
            case 4:
                answerInt = FILTER_COMPANY;
                break;
            case 5:
                answerInt = FILTER_SECURITIES;
                break;
            default:
                System.out.println();
                System.out.println("Ошибка выбора");
                System.out.println();
                answerInt = CHOICE_MENU;
                break;
        }
        return answerInt;
    }

    public static int infoAboutCompany() {                                                                              //Выводим список всех компаний, если список не пустой
        System.out.println();
        System.out.println("Список всех компаний:");
        System.out.println();
        if (list != null) {
            list.forEach(Company::writeShortInfo);
        }
        System.out.println();
        return CHOICE_MENU;
    }

    public static int infoAboutSecurities() {                                                                           //Выводим список всех бумаг
        count = 0;                                                                                                      //Счетчик бумаг
        System.out.println();
        System.out.println("Список всех бумаг:");
        System.out.println();
        if (list != null) {
            list.forEach(company -> {
                company.getSecurities().forEach(companySecurities -> {
                    System.out.println(companySecurities.getCode() + " " + companySecurities.getDate_to() + " " + company.getName_full());
                    count++;
                });
            });
        }
        System.out.println("Всего бумаг: " + count);
        System.out.println();
        return CHOICE_MENU;
    }

    public static int infoAboutDelaySecurities() {                                                                      //Выводим список всех просроченных бумаг относительно сегодняшней даты
        count = 0;                                                                                                      //Счетчик бумаг
        System.out.println("Список всех просроченых бумаг:");
        System.out.println();
        if (list != null) {
            list.forEach(company -> {
                company.getSecurities().stream().filter(CompanySecurities::checkOnDelay).forEach(companySecurities -> {
                    System.out.println(companySecurities.getCode() + " " + companySecurities.getDate_to() + " " + company.getName_full());
                    count++;
                });
            });
        }
        System.out.println("Всего просроченных бумаг: " + count);
        System.out.println();
        return CHOICE_MENU;
    }

    public static int filterCompany() {                                                                                 //Фильтруем компании которые основанны после введеной даты
        int answerInt = CHOICE_MENU;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Для фильтрации компаний введите дату в формате ДД.ММ.ГГГГ");
        System.out.println("0 - Чтобы выйти");
        System.out.println();
        String dateStr = scanner.nextLine();
        LocalDate filterDate;
        if (dateStr.equals("0")) {                                                                                      //Если пользователь ввел ноль то выходим в предыдущее меню
            answerInt = CHOICE_MENU;
        } else {
            filterDate = stringToDate(dateStr);                                                                         //Преобразуем введеную строку в LocalDate

            if (list != null && filterDate != null) {                                                                       //Проверяем что все переменные не пустые
                System.out.println("Компании которые основаны после " + filterDate);
                System.out.println();
                list.stream().filter(comp -> ChronoUnit.DAYS.between(filterDate, comp.getEgrul_date()) > 0)
                        .forEach(Company::writeInfoDateCompany);                                                            //Пробегаемся по всей коллекции и там где дата основания после введеной выводим на экран
                System.out.println();
                answerInt = CHOICE_MENU;
            }
        }
        return answerInt;
    }

    private static LocalDate stringToDate(String str) {                                                                 //Перевод строки в дату
        LocalDate answer = null;
        String[] valuesDate = str.split("[.,/-]");
        if (valuesDate.length > 2) {                                                                                    //Смотрим что с помощью разделителей получилось не меньше 3 значений
            try {
                answer = LocalDate.of(convertYearToNormal(valuesDate[2]),
                        Integer.valueOf(valuesDate[1]), Integer.valueOf(valuesDate[0]));                                //Пытаемся перевести строку в дату, смотрим что бы год был четырех значный
            } catch (DateTimeException e) {
                System.out.println();
                System.out.println("Неправильно введена дата");
                System.out.println();
            }
        }
        return answer;
    }

    private static int convertYearToNormal(String valueYear) {                                                          //Если пользователь ввел двух значный год, то переводим его в четырех значный
        int year;
        try {
            year = Integer.parseInt(valueYear);
        } catch (IllegalStateException e) {
            year = -1;
        }
        if (year < 50) {
            year += 2000;
        } else if (year < 100) {
            year += 1900;
        }
        return year;
    }

    private static int filterSecurities() {                                                                             //Выводим все бумаги у которых код валюты совпадает с введеным
        int answer = CHOICE_MENU;
        count = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Для фильтрации бумаг введите код валюты, например RUB");
        System.out.println("0 - Чтобы выйти");
        System.out.println();
        String codeStr = scanner.nextLine();

        if (codeStr.equals("0")) {
            answer = CHOICE_MENU;
        } else {

            list.forEach(company -> {
                company.getSecurities().stream().filter(security -> security.getCurrency()
                        .getCode().equals(codeStr)).forEach(companySecurities -> {
                            companySecurities.writeFilterCode();
                            count++;
                });
            });

            if (count < 1) {
                System.out.println();
                System.out.println("Найденых бумаг нет");
            }
            System.out.println();
        }
        return answer;
    }
}
