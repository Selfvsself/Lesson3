import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Company {                                                                                                  //Класс в котором переменные и методы такие же как в обьекте в json
    private int id;
    private String code;
    private String name_full;
    private String name_short;
    private String inn;

    private CompanyType company_type;

    private String ogrn;
    private String egrul_date;

    private CompanyCountry country;

    private String fio_head;
    private String address;
    private String phone;
    private String e_mail;
    private String www;
    private boolean is_resident;

    private List<CompanySecurities> securities;

    public void writeShortInfo() {
        System.out.println(getName_short() + " - Дата основания " + getEgrul_date());                                   //Выводим на экран информацию о обьекте как 1 пункте дз
    }

    public void writeInfoDateCompany() {
        System.out.println(getName_full() + " " + getEgrul_date());                                                     //Выводит информацию о компании часть ответа для 2 пункта дз
    }

    public void filterCode(String str) {                                                                                //Выводит бумаги в которых код валюты совпадает с запрашиваемым
        securities.stream().filter(security -> security.getCurrency()
                .getCode().equals(str)).forEach(CompanySecurities::writeFilterCode);
    }

    public static LocalDate getDate(String str) {                                                                              //Класс возвращает LocalDate из строки, которая используется json. Используется здесь и ниже в подклассах
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getName_full() {
        return name_full;
    }

    public String getName_short() {
        return name_short;
    }

    public LocalDate getEgrul_date() {                                                                                  //При запросе даты основания компании получаем LocalDate из строки, которая приходит из json
        LocalDate dateEgrulDate = getDate(egrul_date);
        return dateEgrulDate;
    }

    public List<CompanySecurities> getSecurities() {                                                                    //Получает список бумаг компании
        return securities;
    }
}


class CompanyType {

    private int id;
    private String name_short;
    private String name_full;
}


class CompanyCountry {
    private int id;
    private String code;
    private String name;
}


class CompanySecurities {
    private int id;
    private String code;
    private String name_full;
    private String cfi;
    private String date_to;
    private String state_reg_date;

    private CompanySecuritiesState state;

    private CompanySecuritiesCurrency currency;

    public boolean checkOnDelay() {                                                                                     //Проверяет просроченна ли бумага, если просрочена то возвращает истину
        if (ChronoUnit.DAYS.between(LocalDate.now(), getDate_to()) < 0) {
            return true;
        }
        return false;
    }

    public void writeFilterCode() {                                                                                            //Печатает инфу по бумаге, для пункта 4 дз
        System.out.println(getId() + " " + getCode());
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public LocalDate getDate_to() {
        return Company.getDate(date_to);
    }

    public CompanySecuritiesCurrency getCurrency() {
        return currency;
    }
}

class CompanySecuritiesState {
    private int id;
    private String name;
}

class CompanySecuritiesCurrency {
    private int id;
    private String code;
    private String name_short;
    private String name_full;

    public String getCode() {
        return code;
    }
}

