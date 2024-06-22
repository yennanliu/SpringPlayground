package com.yen.springWarehouse.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeUtils {

  public static SimpleDateFormat getFormatter(String formatPattern) {
    SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
    return formatter;
  }

  public static String getCurrentDate() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date date = new Date();
    String currentDate = formatter.format(date);
    return currentDate;
  }

  public static String getCurrentDateYYYYMMDDHHMMSS() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
    // formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date date = new Date();
    String currentDate = formatter.format(date);
    return currentDate;
  }

  public static String getCurrentDateUTC() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    DateTime now = DateTime.now(DateTimeZone.UTC);
    String currentDate = formatter.format(now);
    return currentDate;
  }

  public static String getNDayBeforeDate(Integer dateBefore) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1 * dateBefore);
    String yesterday_date = dateFormat.format(cal.getTime());
    return yesterday_date;
  }

  public static String getYesterdayDate() {
    return getNDayBeforeDate(1);
  }

  public static String getYesterdayDateWithoutDash() {
    return getYesterdayDate().replace("-", "");
  }

  public static String addDashToDateTime(String dateTime) {

    DateFormat fromFormat = new SimpleDateFormat("yyyyMMdd");
    DateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");

    Date d = null;
    String d1 = null;

    try {
      d = fromFormat.parse(dateTime);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    d1 = toFormat.format(d);
    return d1;
  }
}
