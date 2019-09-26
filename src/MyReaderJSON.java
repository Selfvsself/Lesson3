import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyReaderJSON {
    String strJson;

    public MyReaderJSON(String file) {
        strJson = readFile(file);                                                                                       //При создании экземпляра читаем файл из конструктора
    }

    public List<Company> getCompany() {                                                                                 //Получаем ArrayList обьектов типа Company
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Company>>() {
        }.getType();
        try {
            List<Company> companies = gson.fromJson(strJson, listType);                                                 //Пытаемся получить коллекцию обьектов, в случае неудачи возвращаем пустой ArrayList
            return companies;
        } catch (Exception e) {
            return null;
        }
    }

    private String readFile(String file) {                                                                              //Посимвольно читаем файл и преобразуем байты в символы
        StringBuilder jsonStr = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int symbol = reader.read();
            while (symbol != -1) {
                jsonStr.append((char) symbol);
                symbol = reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr.toString();
    }
}