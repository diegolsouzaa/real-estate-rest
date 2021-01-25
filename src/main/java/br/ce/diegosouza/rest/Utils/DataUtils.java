package br.ce.diegosouza.rest.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtils {

    public static String getDataFuture(Integer qtdDays){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, qtdDays);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(cal.getTime());



    }
}
