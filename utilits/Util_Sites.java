package utilits;

import javafx.scene.control.TextField;
import obgect.Reservation;
import obgect.Sites;
import obgektDB.ReservationDb;
import obgektDB.SitesDB;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Util_Sites {
    private static Util_Sites instance;
    private static int MILLISECOND = 1000;
    private static int YEAR = 365;
    private static int MOUNTH = 30;

    public static Util_Sites getInstance() {
        if (instance == null) {
            instance = new Util_Sites();
        }
        return instance;
    }

    private Util_Sites() {
    }

    public boolean writeSitesFromBD(Sites sites) {// Не реализовано
        boolean flag = false;
         flag =  SitesDB.getInstance().insertObgect(sites);
        return flag;
    }

    public boolean chekTextFaild(TextField textField) {
        boolean flag = true;
        if (textField.getText().trim().isEmpty()) {
            flag = false;
        }
        return flag;
    }

    public void chekDate(Sites sites, boolean status) {
        if (!status) {
            chekData_Issue(sites);
        } else checkDate_delivery(sites);
    }

    private void chekData_Issue(Sites sites) {
        StringBuilder str = new StringBuilder();
        Calendar thisDate = new GregorianCalendar();
        long result = thisDate.getTimeInMillis() - (sites.getReservation().getDate_issue()) * MILLISECOND;
        long days = TimeUnit.MILLISECONDS.toDays(result);
        if (days > 0) {
            str.append("В обработке : ");

            int year = (int) days / YEAR;
            if (year > 0) {
                days = days - year * YEAR;
                sites.getReservation().setLongInwork(true);
                str.append("" + year);
                if (year == 1) {
                    str.append(" год");
                }
                if (year > 1 && year < 5) {
                    str.append(" года");
                }
                if (year > 4) {
                    str.append(" лет");
                }
            }

            if (days > 0) {
                int mounth = (int) days / MOUNTH;
                if (mounth > 0) {

                    days = days - mounth * MOUNTH;
                }
                str.append(" " + mounth);
                if (mounth == 1) {
                    str.append(" месяц");
                }
                if (mounth > 1 && mounth < 5) {
                    str.append(" месяца");
                }
                if (mounth > 4) {
                    str.append(" месяцев");
                    sites.getReservation().setLongInwork(true);
                }

                if (days > 0) {
                    str.append(" " + days);
                    if (days == 1) {
                        str.append(" день");
                    }
                    if (days > 1 && days < 5) {
                        str.append(" дня");
                    }
                    if (days > 4) {
                        str.append(" дней");
                    }
                }
            }
        }

        sites.setTimeStatus(str.toString());
    }

    private void checkDate_delivery(Sites sites) {
        StringBuilder str = new StringBuilder();
        Calendar thisDate = new GregorianCalendar();
        int year = 0;
        int mounth = 0;
        long result = thisDate.getTimeInMillis() - (sites.getReservation().getDate_delivery()) * MILLISECOND;
        long days = TimeUnit.MILLISECONDS.toDays(result);
        if (days > 0) {
            str.append("Сдан : ");
        }
        year = (int) days / YEAR;
        if (year > 0) {
            days = days - year * YEAR;
            sites.getReservation().setLongInwork(true);
            str.append("" + year);
            if (year == 1) {
                str.append(" год");
            }
            if (year > 1 && year < 5) {
                str.append(" года");
            }
            if (year > 4) {
                str.append(" лет");
            }
        }

        if (days > 0) {
            mounth = (int) days / MOUNTH;
            if (mounth > 0) {

                days = days - mounth * MOUNTH;
                str.append(" " + mounth);
                if (mounth == 1) {
                    str.append(" месяц");
                }
                if (mounth > 1 && mounth < 5) {
                    str.append(" месяца");
                }
                if (mounth > 4) {
                    str.append(" месяцев");
                    sites.getReservation().setLongInwork(true);
                }

            }


            if (days > 0) {
                str.append(" " + days);
                if (days == 1) {
                    str.append(" день");
                }
                if (days > 1 && days < 5) {
                    str.append(" дня");
                }
                if (days > 4) {
                    str.append(" дней");
                }
            }
        }
        if (year > 0 || mounth > 0 || days > 0) {
            str.append(" тому назад");
        } else {
            str.append("Mеньше дня тому назад");
        }
        sites.setTimeStatus(str.toString());
    }

    public boolean returnSites(Reservation reservation) {
        boolean flag = false;

        flag = ReservationDb.getInstance().returnObgect(reservation);
        return flag;
    }


}
